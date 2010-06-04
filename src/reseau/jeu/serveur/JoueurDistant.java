package reseau.jeu.serveur;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import reseau.CanalTCP;
import reseau.CanalException;
import reseau.Port;

/**
 * Une classe qui modélise coté serveur un joueur.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Da Campo Aurélien
 */
public class JoueurDistant implements Runnable, ConstantesServeurJeu
{
	// Constantes pour les états de la connexion avec le joueur distant
	private final static int EN_ATTENTE = 0;
	private final static int VALIDATION = 1;
	private final static int EN_JEU = 2;
	private final static int EN_PAUSE = 3;
	private final static int QUITTE = 4;
	private final static int PARTIE_TERMINEE = 5;

	/** 
	 * Canal de dialogue commun
	 */
	private CanalTCP canal;
	
	/**
	 * Canal de mise à jour
	 */
	private CanalTCP canal_update;
	
	/**
	 * identificateur du jeu
	 */
	private int idJoueur;
	
	/**
	 * Serveur de jeu
	 */
	private ServeurJeu serveur;
	
	/**
	 * Etat (initialement en validation)
	 */
	private int etat = VALIDATION;
	
	/** 
	 * Message du client;
	 */
	private String str = "";
	
	/** 
	 * Offset pour les ports temporaires
	 */
	private static int offset_port = 5000;

	/**
	 * Niveau d'affichage des messages
	 */
	public static boolean verbeux = true;

	/**
	 * Crée un lien avec un joueur distant.
	 * 
	 * @param ID
	 *            L'ID associée au joueur.
	 * @param canal
	 *            Le canal de communication.
	 * @param serveur
	 *            Le serveur de jeu associé au joueur.
	 */
	public JoueurDistant(int ID, CanalTCP canal, ServeurJeu serveur)
	{
		this.canal    = canal;
		this.idJoueur = ID;
		this.serveur  = serveur;

		log("Nouveau client");
		(new Thread(this)).start();
	}

	@Override
	protected void finalize() throws Throwable
	{
		// En cas de déréférencement de l'objet, on aura tendance à couper le
		// canal de communication
		fermerCanal();
	}

	/**
	 * Tache d'écoute du canal
	 */
	public void run()
	{
		while (true)
		{
			try {
				actionsEtats();
			}
			catch (JSONException e) {
				logErreur("Récéption inconnue \"" + str + "\"");
				return;
			} 
			catch (CanalException e) {
			    logErreur("Canal erroné \"" + str + "\"");
                return;
            } 
			catch (IOException e)
            {
			    logErreur("ERROR : canal erroné \"" + str + "\"");
                return;
            } 
		}
	}

	/**
	 * Ecoute un message du client.
	 * 
	 * @return Une chaine de caractère.
	 * @throws CanalException 
	 */
	private String attendreMessage() throws CanalException
	{
		// Récéption du message du client
		synchronized (canal)
		{
			str = canal.recevoirString();
			log("Récéption de " + str);
			return str;
		}
	}
	
	/**
	 * Machine d'état du client
	 * 
	 * @throws JSONException
	 *             Erreur levée si la chaine de caractère reçu du serveur n'est
	 *             pas un format JSON
	 * @throws CanalException 
	 * @throws IOException 
	 */
	private void actionsEtats() throws JSONException, CanalException, IOException
	{
		log("Etat : " + getNomEtat(etat));

		switch (etat)
    	{
        	case VALIDATION: 
        	    actionsEtatValidation();                   
        		break;
    			
        	case EN_ATTENTE:
        		actionsEtatEnAttente();
        		break;
    			
        	case EN_JEU:
        	    actionsEtatEnJeu();
        	    break;
 
        	default:
        		logErreur("Etat inconnu");
		} 
	}

	private void actionsEtatValidation() throws CanalException, IOException
    {
	    // Envoi de la version du serveur au client
        envoyer(ServeurJeu.VERSION);
        
        // Réservation du port pour le canal temporaire
        Port port = new Port(offset_port++);
        port.reserver();
        
        // Envoi du numéro de port utilisé
        envoyer(port.getNumeroPort());
        
        // Création du canal de mise à jour et attente de la connexion
        log("Création du canal de synchronisation");
        
        // Création du canal
        canal_update = new CanalTCP(port, verbeux);
    
        log("Canal crée");
        
        // envoie de l'etat de la partie
        envoyerSurCanalMAJ(Protocole.construireMsgJoueursEtat(serveur.getJoueurs()));
        
        // Passage en état EN_ATTENTE
        etat = EN_ATTENTE; 
    }

    /**
	 * Attente 
	 * 
	 * @throws JSONException
	 * @throws CanalException
	 */
	private void actionsEtatEnAttente() throws JSONException, CanalException
    {
	    // Attente d'un message
        JSONObject json = new JSONObject(attendreMessage());
        
        // Extraction du type du message
        int type = json.getInt("TYPE");
        log("Récéption d'un message de type " + type + " dans l'état : "+ etat);
        
        switch (type)
        {
    	    // Changement d'equipe
            case JOUEUR_CHANGER_EQUIPE:
                
                int idEquipe = json.getInt("ID_EQUIPE");
                int idJoueur2 = json.getInt("ID_JOUEUR");
                
                // joueur identique ou admin
                if(idJoueur2 == idJoueur || idJoueur == serveur.getIdCreateur())
                    envoyer(serveur.changerEquipe(idJoueur2,idEquipe));
                else
                    repondreEtat(JOUEUR_CHANGER_EQUIPE, ACTION_NON_AUTORISEE);
        
                break;
            
             // Changement d'equipe
            case JOUEUR_PRET:
                
                log("Entre en jeu");
                etat = EN_JEU;
                break; 
                
            // Changement d'equipe
            case JOUEUR_DECONNEXION:
                
                log("Terminaison de la liaison avec le client");
                
                // On clos la liaison avec le client
                fermerCanal();
                
                // On supprime le joueur distant de la liste des clients
                desenregistrement();
                
                break;      
        }
    }

    /**
	 * Envoi un message au client
	 * 
	 * @param msg
	 *            Le message à envoyer.
	 * @throws CanalException 
	 */
	public synchronized void envoyer(final String msg) throws CanalException
	{
		log("Envoi du String " + msg);
		canal.envoyerString(msg);
	}

	public synchronized void envoyer(final int msg) throws CanalException
	{
		synchronized (canal)
		{
			log("Envoi de l'int " + msg);
			canal.envoyerInt(msg);
		}
	}

	private void actionsEtatEnJeu() throws JSONException, CanalException
	{
		// Attente d'un message
		JSONObject json = new JSONObject(attendreMessage());
		
		// Extraction du type du message
		int type = json.getInt("TYPE");
		
		log("Récéption d'un message de type " + type + " dans l'état : "+ etat);
		
		switch (type)
		{
    		// Récéption d'un message texte
    		case JOUEUR_MESSAGE:
    		    receptionMsgDemandeEnvoieMessage(json);
    			break;
    		
    		// Action sur une vague
    		case VAGUE:
    		    receptionMsgDemandeLancementVague(json);
    			break;
    			
    		// Requête de création d'une tour
    		case TOUR_AJOUT:
    		    receptionMsgDemandeAjoutTour(json);
    			break;
    			
    		// Requête d'amélioration d'une tour
    		case TOUR_AMELIORATION:
    		    receptionMsgDemandeAmeliorerTour(json);
    			break;
    			
    		// Requete de suppresion d'une tour
    		case TOUR_SUPRESSION:
    			receptionMsgDemandeSuppressionTour(json);
    			break;
    			
    		default:
    			logErreur("Type de message inconnu : " + type);
    			// Signaler au client qu'il envoi quelque chose d'incorecte
    			repondreEtat(ERREUR, ERREUR);
    			break;
    		}
	}

	private void receptionMsgDemandeSuppressionTour(JSONObject json) throws JSONException, CanalException
    {
	    // Récupération de la tour cible
        int tourCibleDel = json.getInt("ID_TOWER");
        // Demande au serveur de l'opération
        int code = serveur.supprimerTour(idJoueur, tourCibleDel);
        // Retour au client de code
        repondreEtat(TOUR_SUPRESSION, code);
    }

    private void receptionMsgDemandeAmeliorerTour(JSONObject json) throws JSONException, CanalException
    {
	    // Récupération de la tour cible
        int tourCible = json.getInt("ID_TOWER");
        // Demande au serveur de l'opération
        int code = serveur.ameliorerTour(idJoueur, tourCible);
        // Retour au client de code
        repondreEtat(TOUR_AMELIORATION, code);
    }

    private void receptionMsgDemandeAjoutTour(JSONObject json) throws JSONException, CanalException
    {
        // Extraction des coordonnées
        int x = json.getInt("X");
        int y = json.getInt("Y");
        // Extraction du type de tour
        int typeTour = json.getInt("TYPE_TOUR");
        // Demande d'ajout au serveur
        int code = serveur.poserTour(idJoueur, typeTour, x, y);
        // Retour au client du code
        repondreEtat(TOUR_AJOUT, code); 
    }

    private void receptionMsgDemandeLancementVague(JSONObject json) throws JSONException, CanalException
    {
	    // Récupération du type de vague
        int nbCreatures = json.getInt("NB_CREATURES");
        int typeCreature = json.getInt("TYPE_CREATURE");
        // Demande de lancement d'une vague
        int code = serveur.lancerVague(idJoueur, nbCreatures, typeCreature);  
        // Retour au client de l'information
        repondreEtat(VAGUE, code);
    }

    private void receptionMsgDemandeEnvoieMessage(JSONObject json) throws JSONException, CanalException
    {
	    log("Message reçu de " + idJoueur);
	   
        // Extraction de la cible du message
        int cible = json.getInt("CIBLE");
        log("Message pour " + cible);
        
        // Extraction du texte du message
        String text = json.getString("MESSAGE");
        
        log("Texte : " + text);
        
        if (cible == A_TOUS) {
            // On broadcast le message à tous les clients
            serveur.envoyerMessageChatPourTous(idJoueur, text);
        } 
        else {
            // On envoi un message à un client en particulier
            serveur.envoyerMsgClient(idJoueur, cible, text);
        }
    }

    /**
	 * Répond au client un code d'état pour une réponse donnée
	 * 
	 * @param player
	 * @param code
	 * @throws JSONException 
	 * @throws CanalException 
	 */
	private void repondreEtat(int type, int code)
	    throws JSONException, CanalException
	{
		// Message JSON
		JSONObject message = new JSONObject();
		
		// Construction de la structure JSON
		message.put("TYPE", type);
		message.put("STATUS", code);
		// Envoi de la structure à travers le réseau
		envoyer(message.toString());
	}

	/**
	 * Termine la liaison avec le client
	 * @throws CanalException 
	 */
	private void fermerCanal() throws CanalException
	{
		canal.fermer();
	}
	
	private void desenregistrement()
    {
        log("Suppression du joueur");
        serveur.supprimerJoueur(idJoueur);
    }

	/**
	 * Donne une représentation sous forme de chaine de caractère de l'état
	 * actuel du client
	 * 
	 * @param etat
	 *            L'état actuel du client
	 * @return Une chaine de caractère
	 */
	private static String getNomEtat(int etat)
	{
		switch (etat)
		{
		case EN_ATTENTE:
			return "En attente";
		case EN_JEU:
			return "En jeu";
		case EN_PAUSE:
			return "En pause";
		case VALIDATION:
			return "Validation";
		case PARTIE_TERMINEE:
			return "Partie terminée";
		case QUITTE:
			return "Quitte la partie";
		default:
			return "<BAD>";
		}
	}

	/**
	 * Envoi sur le canal de mise à jour le message en paramêtre
	 * 
	 * @param message
	 *            Le message à envoyer
	 * @throws CanalException 
	 */
	public synchronized void envoyerSurCanalMAJ(String message) 
	    throws CanalException
	{
		if(canal_update != null)
		    canal_update.envoyerString(message);
		else
		    logErreur("Canal_update null");
	}
	
	/**
	 * Permet d'afficher une message log
	 * 
	 * @param msg le message
	 */
	private void log(String msg)
    {
        if(verbeux)
            ServeurJeu.log("[JOUEUR " + idJoueur + "]" + msg);
    }
	
    /**
     * Permet d'afficher des message log d'erreur
     * 
     * @param msg le message
     */
    private void logErreur(String msg)
    {
        System.err.println("[JOUEUR " + idJoueur + "]" + msg);
    }

    public int getId()
    {
        return idJoueur;
    }
}
