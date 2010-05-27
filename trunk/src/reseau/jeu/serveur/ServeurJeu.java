package reseau.jeu.serveur;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import org.json.*;

import exceptions.*;
import models.animations.Animation;
import models.creatures.*;
import models.jeu.*;
import models.joueurs.Equipe;
import models.joueurs.Joueur;
import models.tours.*;
import reseau.*;
import sun.print.resources.serviceui;

/**
 * Cette classe contiendra le serveur de jeu sur lequel se connecteront tout les
 * cliens.
 * 
 * @author Pierre-Do
 * @author Da Campo Aurélien
 */
public class ServeurJeu extends Observable implements ConstantesServeurJeu,
		EcouteurDeJeu, IDTours, IDCreatures, Runnable
{
	/**
	 * La version courante du serveur
	 */
	public static final String VERSION = "0.2";

	/**
	 * Le port sur lequel le serveur écoute par defaut
	 */
	public final static int PORT = 2357;

	/**
     * TODO
     */
	private long TEMPS_DE_RAFFRAICHISSEMENT = 50;
	
	/**
	 * Fanion pour le mode debug
	 */
	private static final boolean verbeux = true;

	/**
	 * Liste des clients enregistrés sur le serveur
	 */
	private HashMap<Integer, JoueurDistant> clients = new HashMap<Integer, JoueurDistant>();

	/**
	 * Lien vers le module coté serveur du jeu
	 */
	private Jeu jeuServeur;

	/**
	 * Thead de rafraichissement pour les messages
	 */
	//private Watchdog notifieur;

	/**
	 * 
	 * @param jeuServeur
	 * @throws IOException
	 */
	public ServeurJeu(final Jeu jeuServeur) throws IOException
	{
		// Assignation du serveur
		this.jeuServeur = jeuServeur;
		
		// le serveur ecoute le jeu
		jeuServeur.setEcouteurDeJeu(this);
		
		// Lancement du thread serveur.
		(new Thread(this)).start();
	}

	@Override
	public void run()
	{
		// Réglage du niveau d'affichage des messages clients
		JoueurDistant.verbeux = verbeux;
		
		// Réservation du port d'écoute
		Port port = new Port(PORT);
		port.reserver();
		
		// Canal d'écoute
		CanalTCP canal;
		
		// Lancement de l'horloge interne
		//notifieur = new Watchdog(this, ATTENTE);
		// Lancement du thread de rafraichisement
		//Updater updater = new Updater(clients);
		//addObserver(updater);
		
		// Boucle d'attente de connections
		while (true)
		{
			// On attend qu'un joueur se présente
			log("Ecoute sur le port " + PORT);
			
			// Bloquant en attente d'une connexion
			canal = new CanalTCP(port, verbeux);
			
			String ip = canal.getIpClient();
			// Log
			log("Récéption de " + ip); 
			
			// Récéption du pseudo du joueur
			String pseudo = canal.recevoirString();
			
			// Création du joueur
			Joueur joueur = new Joueur(pseudo);
			
			try
			{
				// Ajout du joueur à l'ensemble des joueurs
				jeuServeur.ajouterJoueur(joueur);
				
				// Envoye de la réponse
				JSONObject requete = construireMsgInitialisationJoueur(joueur);
				canal.envoyerString(requete.toString());
				
				// Log
				log("Nouveau joueur ! ID : " + joueur.getId());
				
				// Enregistrement de l'ID et du canal dans la base interne
				enregistrerClient(joueur.getId(), canal);
			}
			catch (JeuEnCoursException e){
				e.printStackTrace();
				// TODO
				canal.envoyerString("Erreur, le jeu est en cours");
			}
			catch (AucunePlaceDisponibleException e){
				
			    log("Joueur refusé");

				JSONObject msg = new JSONObject();
		        
		        try {
		            msg.put("TYPE", JOUEUR_INITIALISATION);
		            msg.put("STATUS", PAS_DE_PLACE);
		        } 
		        catch (JSONException jsone){
		            jsone.printStackTrace();
		        }
				
				canal.envoyerString(msg.toString());
			}
		}
	}

    private void enregistrerClient(int IDClient, CanalTCP canal)
	{
		// On vérifie que l'ID passé en paramêtre soit bien unique
		if (clients.containsKey(IDClient))
		{
			log("ERROR : Le client " + IDClient + " est déjà dans la partie");
			// On déconnecte le client; // FIXME
			canal.fermer();
		} else
		{
			// On inscrit le joueur à la partie
			clients.put(IDClient, new JoueurDistant(IDClient, canal, this));
			setChanged();
			notifyObservers(clients);
		}
	}

	/**
	 * Affiche toutes les informations de tous les clients connectés.
	 */
	public synchronized void infos()
	{
		System.out.println("Serveur de jeu");
		System.out.println("Nombre de joueurs : " + clients.size());
		for (Entry<Integer, JoueurDistant> joueur : clients.entrySet())
			System.out.println(joueur.getValue());
	}

	protected synchronized static void log(String msg)
	{
		if(verbeux)
		    System.out.println("[SERVEUR] "+ msg);
	}

	private synchronized Joueur getJoueur(int ID)
	{
		for (Joueur joueur : jeuServeur.getJoueurs())
		{
			if (joueur.getId() == ID)
				return joueur;
		}
		throw new IllegalArgumentException("ID " + ID + " non trouvé");
	}

	public synchronized void lancerPartie()
	{
	    // Signalisation aux clients que la partie à commencé
		for (Entry<Integer, JoueurDistant> joueur : clients.entrySet())
			joueur.getValue().lancerPartie();
		
		JSONObject requete = construireMsgEtatPartie(PARTIE_LANCEE);
        envoyerATous(requete.toString());
	}

    /**************** NOTIFICATIONS **************/

	@Override
	public void creatureArriveeEnZoneArrivee(Creature creature)
	{
	    /*
	     *  TODO uniquement pour les joueurs concernée
	     *  -> les joueurs de l'equipe qui a perdu une vie
	     */
	    //JSONObject requete = construireMsgPerteVie(equipe);
	    
	    // envoyer aux membres de l'equipe
	}

	@Override
	public void creatureBlessee(Creature creature)
	{
	    // detectable lors de la mise a jour par l'état d'une creature 
	}

	@Override
	public void creatureTuee(Creature creature)
	{
        JSONObject requete = construireMsgSuppressionCreature(creature);
        envoyerATous(requete.toString());
	}

    @Override
	public void etoileGagnee(){}

	
	@Override
	public void partieTerminee()
	{
	    // TODO
	    //JSONObject requete = construireMsgFinPartie();
        //envoyerATous(requete.toString());
	}

	@Override
	public void vagueEntierementLancee(VagueDeCreatures vague){}

	@Override
	public void animationAjoutee(Animation animation){}

	@Override
	public void animationTerminee(Animation animation){}
	
	@Override
	public void creatureAjoutee(Creature creature)
	{
	    JSONObject requete = construireMsgAjoutCreature(creature);
	    
	    envoyerATous(requete.toString());
	}

    @Override
	public void joueurAjoute(Joueur joueur){}
    


	@Override
	public void partieDemarree()
	{
		lancerPartie();

		//--------------------------------------
		//-- tache de mise a jour des clients --
		//--------------------------------------
		Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(!jeuServeur.estTermine())
                {
                    JSONObject msg;

                    for(Creature creature : jeuServeur.getCreatures())
                    {
                        msg = construireMsgEtatCreature(creature);
                        envoyerATous(msg.toString());
                    }
                    
                    try{
                        Thread.sleep(TEMPS_DE_RAFFRAICHISSEMENT);
                    } 
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        
        t.start();
	}

	@Override
	public void tourAmelioree(Tour tour){}

	@Override
	public void tourPosee(Tour tour){}

	@Override
	public void tourVendue(Tour tour){}

	@Override
    public void joueurMisAJour(Joueur joueur)
    {
	    log("Mise a jour du joueur "+joueur.getPseudo());
	    
	    JSONObject msg = construireMsgEtatJoueur(joueur);
        envoyerATous(msg.toString());
    }
	
	
	/**
	 * Supprime un joueur de la partie
	 * 
	 * @param ID
	 *            l'ID du joueur à supprimer
	 */
	public synchronized void supprimerJoueur(int ID)
	{
		clients.remove(ID);
		
		//setChanged();
		//notifyObservers(clients);
	}

	/************************** ACTIONS DES JOUEURS ************************/

	/**
	 * TODO Contrôle de l'argent
	 * 
	 * @param typeVague
	 * @return
	 */
	public synchronized int lancerVague(int IDPlayer, int nbCreatures, int typeCreature)
	{
	    Creature creature;
        
        switch(typeCreature)
        {
            case AIGLE :
                creature = new Aigle(100, 100, 20);
                break;
            case ARAIGNEE : 
                creature = new Araignee(100, 100, 20);
                break;
            case ELEPHANT :
                creature = new Elephant(100, 100, 20);
                break;
            case GRANDE_ARAIGNEE : 
                creature = new GrandeAraignee(100, 100, 20);
                break;
            case MOUTON :
                creature = new Mouton(100, 100, 20);
                break;
            case PIGEON : 
                creature = new Pigeon(100, 100, 20);
                break;
            case RHINOCEROS :
                creature = new Rhinoceros(100, 100, 20);
                break;
            default :
                return ERREUR; // TODO erreur
        }
        
        log("Le joueur " + IDPlayer + " désire lancer une vague de "+nbCreatures+" créatures de type"
                + creature.getNom());
        
		VagueDeCreatures vague = new VagueDeCreatures(nbCreatures, creature, 500, true);
		
		
		Joueur j = jeuServeur.getJoueur(IDPlayer);
		
		jeuServeur.lancerVague(jeuServeur.getEquipeAvecJoueurSuivante(j.getEquipe()),vague);
		
		return OK;
	}
	
	   /**
     * 
     * @param IDPlayer
     * @param nouvelEtat
     * @return
     */
    public synchronized int changementEtatJoueur(int IDPlayer, int nouvelEtat)
    {
        log("Le joueur " + IDPlayer + " désire passer en état " + nouvelEtat);
        return 0;
    }

    /**
     * TODO
     */
    public synchronized int changementEtatPartie(int IDPlayer, int nouvelEtat)
    {
        log("Le joueur " + IDPlayer + " désire passer la partie en état "
                + nouvelEtat);

        // FIXME S'il est le créateur !
        switch (nouvelEtat)
        {
            case EN_PAUSE:
                // TODO
                break;
            case EN_JEU:
                // TODO
                break;
            default:
                break;
        }
        
        return 0;
    }
    
    //-----------------------
    //-- GESTION DES TOURS --
    //-----------------------

	/**
	 * Appelée lors d'un demande d'ajout d'une tour
	 * 
	 * @param idJoueur le joueur qui souhaite améliorer
	 * @param typeTour le type de la tour a ajouter
	 * @param x la position x de la tour
	 * @param y la position y de la tour
	 * @return l'état de l'action
	 */
	public synchronized int poserTour(int idJoueur, int typeTour, int x, int y)
	{
		log("Le joueur " + idJoueur + " veut poser une tour de type "
				+ typeTour);
		
		// Selection de la tour cible
		Tour tour = null;
		switch (typeTour)
		{
    		// Tour d'archer
    		case TOUR_ARCHER:
    			tour = new TourArcher();
    			break;
    		// Tour canon
            case TOUR_CANON:
                tour = new TourCanon();
                break;
    		// Tour Anti Aerienne
    		case TOUR_AA:
    			tour = new TourAntiAerienne();
    			break;
    		// Tour de glace (à la fraise)
            case TOUR_DE_GLACE:
                tour = new TourDeGlace();
                break;	
            // Bobine de Telsa
            case TOUR_ELECTRIQUE:
                tour = new TourElectrique();
                break;
            // Tour de feu
            case TOUR_DE_FEU:
                tour = new TourDeFeu();
                break;    
    		// Tour d'air
    		case TOUR_D_AIR:
    			tour = new TourDAir();
    			break;
    		// Tour de terre
            case TOUR_DE_TERRE:
                tour = new TourDeTerre();
                break;
    		default:
    			log("Tour " + typeTour + " inconnue.");
    			return ERREUR;
		}
		
		// Assignation des paramêtres
		tour.x = x;
		tour.y = y;
		
		// Assignation du propriétaire
		tour.setProprietaire(getJoueur(idJoueur));
		
		try
		{
			// Tentative de poser la tour
			jeuServeur.poserTour(tour);
		} 
		// Pas assez d'argent 
		catch (ArgentInsuffisantException e){
			return ARGENT_INSUFFISANT;
		} 
		// Pose dans une zone non accessible
		catch (ZoneInaccessibleException e){
			return ZONE_INACCESSIBLE; 
		} 
		// Chemin bloqué.
		catch (CheminBloqueException e){
			return CHEMIN_BLOQUE; 
		} 
		// Autre erreur
		catch (Exception e){
			e.printStackTrace();
			return ERREUR;
		}
		
		// Multicast aux clients
		envoyerATous(construireMsgDemandeAjoutTour(tour).toString());
  
		//setChanged();
		return OK;
	}

	/**
	 * Appelée lors d'un demande d'amélioration d'une tour
	 * 
	 * @param idJoueur le joueur qui souhaite améliorer
	 * @param idTour la tour a améliorer
	 * 
	 * @return l'état de l'action
	 */
	public synchronized int ameliorerTour(int idJoueur, int idTour)
	{
		log("Le joueur " + idJoueur + " désire améliorer la tour " + idTour);
		
		// Récupération de la tour à améliorer
		Tour tour = jeuServeur.getTour(idTour);
		
		if (tour == null)
			return ERREUR;
		
		// si le joueur est bien le propriétaire de la tour
		if(tour.getPrioprietaire().getId() != idJoueur)
		    return ACTION_NON_AUTORISEE;
		
		// On effectue l'action
		try {
		    jeuServeur.ameliorerTour(tour);  
		} 
		catch (ArgentInsuffisantException aie){
			return ARGENT_INSUFFISANT;
		}
		catch (NiveauMaxAtteintException e){
		    return NIVEAU_MAX_ATTEINT;
        } 
		catch (ActionNonAutoriseeException e)
        {
		    return ACTION_NON_AUTORISEE;
        }
		
		// Multicast aux clients
        envoyerATous(construireMsgDemanderAmeliorationTour(tour).toString());
		
		return OK;
	}

    /**
	 * 
	 * @param tourCibleDel
	 * @return
	 */
	public synchronized int supprimerTour(int IDPlayer, int tourCible)
	{
		log("Le joueur " + IDPlayer + " désire supprimer la tour " + tourCible);
		
		// Repérage de la tour à supprimer
		Tour tour = jeuServeur.getTour(tourCible);
		
		if (tour == null)
			return ERREUR;
		
		// seul le proprio peut vendre la tour
		if(tour.getPrioprietaire().getId() != IDPlayer)
		    return ACTION_NON_AUTORISEE;
		
		// On effectue l'action
		try
        {
            jeuServeur.vendreTour(tour);
        } 
		catch (ActionNonAutoriseeException e){}
		
		// Multicast aux clients
        envoyerATous(construireMsgDemanderSuppressionTour(tour).toString());
		
		return OK;
	}

	/**
	 * Envoi un message texte à l'ensemble des clients connectés.
	 * 
	 * @param IDFrom
	 *            L'ID de l'expéditeur.
	 * @param message
	 *            Le message à envoyer.
	 */
	public synchronized void direATous(int IDPlayer, String message)
	{
		log("Le joueur " + IDPlayer + " dit : " + message);
		for (Entry<Integer, JoueurDistant> joueur : clients.entrySet())
			joueur.getValue().envoyerMessageTexte(IDPlayer, message);
	}

	/**
	 * Envoi un message texte à un client en particulier.
	 * 
	 * @param IDFrom
	 *            L'ID de l'expéditeur
	 * @param IDTo
	 *            L'ID du destinataire
	 * @param message
	 *            Le message à envoyer.
	 */
	public synchronized void direAuClient(int IDPlayer, int IDTo, String message)
	{
		log("Le joueur " + IDPlayer + " désire envoyer un message à " + IDTo
				+ "(" + message + ")");
		clients.get(IDTo).envoyerMessageTexte(IDPlayer, message);
	}

	/* TODO pas besoins pour ma structure
	public synchronized ArrayList<Creature> getCreatures()
	{
		return new ArrayList<Creature>(jeuServeur.getCreatures());
	}

	public synchronized ArrayList<Tour> getTours()
	{
		return new ArrayList<Tour>(jeuServeur.getTours());
	}

	public synchronized ArrayList<Animation> getAnimations()
	{
		return new ArrayList<Animation>();
	}*/

	/**
	 * Permet de Mutli-caster a tous les clients
	 * 
	 * @param message le message à diffuser
	 */
	private void envoyerATous(String message)
	{   
	    synchronized(clients)
	    {
    	    for (Entry<Integer, JoueurDistant> joueur : clients.entrySet())
                joueur.getValue().envoyerSurCanalMAJ(message);
	    }
	}
	
	
	private JSONObject construireMsgInitialisationJoueur(Joueur joueur)
    {
	    JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", JOUEUR_INITIALISATION);
            msg.put("STATUS", OK);
            msg.put("ID_JOUEUR", joueur.getId());
            msg.put("ID_EMPLACEMENT", joueur.getEmplacement().getId());
            msg.put("ID_EQUIPE", joueur.getEquipe().getId());
            msg.put("NOM_FICHIER_TERRAIN", jeuServeur.getTerrain().getClass().getSimpleName()+".map");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg; 
    }
	
	private JSONObject construireMsgEtatPartie(int etat)
    {
	    JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", PARTIE_ETAT);
            msg.put("ETAT", etat); 
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg; 
    }
	
	/**
     * Permet de construire le message d'état d'un joueur
     * 
     * @param joueur le joueur
     * @return Une structure JSONObject
     */
    private JSONObject construireMsgEtatJoueur(Joueur joueur)
    {
        JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", JOUEUR_ETAT);
            msg.put("ID_JOUEUR", joueur.getId());
            msg.put("NB_PIECES_OR", joueur.getNbPiecesDOr());
            msg.put("SCORE", joueur.getScore());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg; 
    }
	
	
	/**
	 * Permet de construire le message de demande d'ajout d'une tour
	 * 
	 * @param tour la tour
	 * @return Une structure JSONObject
	 */
	private JSONObject construireMsgDemandeAjoutTour(Tour tour)
	{
	    JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", TOUR_AJOUT);
            msg.put("JOUEUR", tour.getPrioprietaire().getId());
            msg.put("ID_TOUR", tour.getId());
            msg.put("X", tour.x);
            msg.put("Y", tour.y);
            msg.put("TYPE_TOUR", Tour.getTypeDeTour(tour));
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg;
	}
	
	/**
     * Permet de construire le message de demande d'amélioration d'une tour
     * 
     * @param tour la tour
     * @return Une structure JSONObject
     */
	private Object construireMsgDemanderAmeliorationTour(Tour tour)
    {
	    JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", TOUR_AMELIORATION);
            msg.put("ID_TOUR", tour.getId());
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg;
    }
	
	/**
     * Permet de construire le message de demander la suppression d'une tour
     * 
     * @param tour la tour
     * @return Une structure JSONObject
     */
	private JSONObject construireMsgDemanderSuppressionTour(Tour tour)
    {
        JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", TOUR_SUPRESSION);
            msg.put("ID_TOUR", tour.getId());
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg;
    }
	
    // CREATURES
    
    /**
     * Permet de construire le message d'ajout d'une créature
     * 
     * @param creature la creature
     * @return Une structure JSONObject
     */
	private JSONObject construireMsgAjoutCreature(Creature creature)
    {
	    JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", CREATURE_AJOUT);
            msg.put("TYPE_CREATURE", Creature.getTypeCreature(creature));
            msg.put("ID_CREATURE", creature.getId());
            
            
            msg.put("X", creature.x);
            msg.put("Y", creature.y);
            msg.put("SANTE_MAX", creature.getSanteMax());
            msg.put("NB_PIECES_OR", creature.getNbPiecesDOr());
            msg.put("VITESSE", creature.getVitesseNormale());  
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg;  
    }
	
	/**
     * Permet de construire le message d'état d'une créature
     * 
     * @param creature la creature
     * @return Une structure JSONObject
     */
    private JSONObject construireMsgEtatCreature(Creature creature)
    {
        JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", CREATURE_ETAT);
            msg.put("ID_CREATURE", creature.getId());
           
            msg.put("X", creature.x);
            msg.put("Y", creature.y);
            msg.put("SANTE", creature.getSante());
            msg.put("ANGLE", creature.getAngle());
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg;
    }
	
	/**
     * Permet de construire le message de suppression d'une créature
     * 
     * @param creature la creature
     * @return Une structure JSONObject
     */
	private JSONObject construireMsgSuppressionCreature(Creature creature)
    {
	    JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", CREATURE_SUPPRESSION);
            msg.put("ID_CREATURE", creature.getId()); 
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg;  
    }

    public JSONObject changerEquipe(int idJoueur, int idEquipe)
    {
        Joueur joueur = jeuServeur.getJoueur(idJoueur);
        Equipe e = jeuServeur.getEquipe(idEquipe);
        
        
        JSONObject message = new JSONObject();
 
        try {
            try {
                
                message.put("TYPE", JOUEUR_CHANGER_EQUIPE);
                
                e.ajouterJoueur(joueur);
    
                // SUCCES
                message.put("STATUS", OK);
                message.put("ID_EQUIPE", joueur.getEquipe().getId());
                message.put("ID_EMPLACEMENT", joueur.getEmplacement().getId());
            }
            catch (AucunePlaceDisponibleException e1)
            {
                // ECHEC
                message.put("STATUS", PAS_DE_PLACE);
            }
        }
        catch (JSONException jsone)
        {
            jsone.printStackTrace();
        }
 
        return message;
    }

    @Override
    public void partieInitialisee()
    {
        JSONObject requete = construireMsgEtatPartie(PARTIE_INITIALISEE);
        envoyerATous(requete.toString());
    }
}
