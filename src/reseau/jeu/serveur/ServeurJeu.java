package reseau.jeu.serveur;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import org.json.JSONException;
import outils.Configuration;
import exceptions.*;
import models.animations.Animation;
import models.creatures.*;
import models.jeu.*;
import models.joueurs.Equipe;
import models.joueurs.Joueur;
import models.tours.*;
import reseau.*;

/**
 * Cette classe contiendra le serveur de jeu sur lequel se connecteront tout les
 * cliens.
 * 
 * @author Pierre-Do
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 */
public class ServeurJeu implements ConstantesServeurJeu, EcouteurDeJeu, Runnable
{
	/**
	 * La version courante du serveur
	 */
	public static final String VERSION = "2_0_beta";

	/**
     * Temps de rafraichissement des éléments
     */
	private long TEMPS_DE_RAFFRAICHISSEMENT = 80;
	
	/**
	 * Fanion pour le mode debug
	 */
	private static final boolean verbeux = false;

	/**
	 * Liste des clients enregistrés sur le serveur
	 */
	private HashMap<Integer, JoueurDistant> clients = new HashMap<Integer, JoueurDistant>();

	/**
	 * Lien vers le module coté serveur du jeu
	 */
	private Jeu jeuServeur;

	
	/**
	 * Canal d'attente de connexion de joueur
	 */
	private CanalTCP canalAttenteConnexion = null;
	
	/**
	 * Port du canal
	 */
	private Port port;
	
	/**
	 * Référence vers le créateur du jeu
	 */
	private Joueur createur;
	
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
		
        // Réservation du port d'écoute
        port = new Port(Configuration.getPortSJ());
        
        // reservation du port
        port.reserver();
          
		// Lancement du thread serveur.
		(new Thread(this)).start();
	}

	@Override
	public void run()
	{  
        try
        {
    	    // Boucle d'attente de connections
            while (true)
            {
                try
                {
                    // On attend qu'un joueur se présente
                    log("Ecoute sur le port " + Configuration.getPortSJ());
                    
                    // Bloquant en attente d'une connexion
                    canalAttenteConnexion = new CanalTCP(port, verbeux);
                    
                    String ip = canalAttenteConnexion.getIpClient();
                    
                    // Log
                    log("Récéption de " + ip); 
                    
                    // Récéption du pseudo du joueur
                    String pseudo = canalAttenteConnexion.recevoirString();
                    
                    // Création du joueur
                    Joueur joueur = new Joueur(pseudo);
                    
                    enregistrerClient(joueur, canalAttenteConnexion);
                } 
                catch (JeuEnCoursException e){
    
                    log("Joueur refusé - jeu est en cours");
                    
                    // Envoye de la réponse
                    canalAttenteConnexion.envoyerString(Protocole.construireMsgJoueurInitialisation(JEU_EN_COURS));   
                }
                catch (AucunePlaceDisponibleException e){
                    
                    log("Joueur refusé - aucune place disponible");
    
                    // Envoye de la réponse
                    canalAttenteConnexion.envoyerString(Protocole.construireMsgJoueurInitialisation(PAS_DE_PLACE));
                }
            }
        }  
        catch (CanalException e)
        {
            canalErreur(e);
        }       
	}

    private synchronized void enregistrerClient(Joueur joueur, CanalTCP canal) 
        throws JeuEnCoursException, AucunePlaceDisponibleException
	{
        try
        {
            // Ajout du joueur à l'ensemble des joueurs
            jeuServeur.ajouterJoueur(joueur);
            
            // Log
            log("Nouveau joueur ! ID : " + joueur.getId());
            
            // On vérifie que l'ID passé en paramêtre soit bien unique
    		if (clients.containsKey(joueur.getId()))
    		{
    			log("ERROR : Le client " + joueur.getId() + " est déjà dans la partie");
    			
    			// FIXME On déconnecte le client; 
                canal.fermer();
    		} 
    		else
    		{
    		    // le premier joueur qui se connect est admin
                if(createur == null)
                    createur = joueur;
    		    
    		    // Envoye de la réponse
                canal.envoyerString(Protocole.construireMsgJoueurInitialisation(joueur, jeuServeur.getTerrain()));
    
    		    // On inscrit le joueur à la partie
                JoueurDistant jd = new JoueurDistant(joueur, canal, this);
    			clients.put(joueur.getId(), jd);
    			
    			// Notification des clients 
    	        envoyerATous(Protocole.construireMsgJoueursEtat(jeuServeur.getJoueurs()));
    		}
		
        } 
        catch (CanalException e)
        {
            canalErreur(e);
        }
	}

    /**************** NOTIFICATIONS **************/

	@Override
	public void creatureArriveeEnZoneArrivee(Creature creature)
	{
	    envoyerATous(Protocole.construireMsgCreatureArrivee(creature));
	}

	@Override
	public void creatureBlessee(Creature creature)
	{
	    // detectable par les clients lors de la mise a jour par l'état d'une creature
	}


	@Override
	public void creatureTuee(Creature creature,Joueur tueur)
	{
	    // Multicast aux clients
	    envoyerATous(Protocole.construireMsgCreatureSuppression(creature,tueur));
	}

    @Override
	public void etoileGagnee(){}

	
	@Override
	public void partieTerminee(ResultatJeu resultatJeu)
	{
	    // TODO CHECK Multicast aux clients
	    envoyerATous(Protocole.construireMsgPartieTerminee(jeuServeur));
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
	    // Multicast aux clients
	    envoyerATous(Protocole.construireMsgCreatureAjout(creature));
	}

    @Override
	public void joueurAjoute(Joueur joueur){}
    
	@Override
	public void partieDemarree()
	{
        // Notification des joueurs
        
        //envoyerATous(Protocole.construireMsgPartieChangementEtat(PARTIE_LANCEE));
        envoyerATous(Protocole.construireMsgJoueursEtat(getJoueurs()));
      
        creerTacheDeMiseAJour();
	}

	private void creerTacheDeMiseAJour()
    {
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
                    for(Creature creature : jeuServeur.getCreatures())
                    {
                        if(!creature.estMorte())
                            envoyerATous(Protocole.construireMsgCreatureEtat(creature));
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
	public void tourAmelioree(Tour tour)
	{
	    // Multicast aux clients
        envoyerATous(Protocole.construireMsgTourAmelioration(tour).toString());
	}

	@Override
	public void tourPosee(Tour tour)
	{
	    // Multicast aux clients
        envoyerATous(Protocole.construireMsgTourAjout(tour).toString());
	}

	@Override
	public void tourVendue(Tour tour)
	{
	    // Multicast aux clients
        envoyerATous(Protocole.construireMsgTourSuppression(tour).toString());
	}

	@Override
    public void joueurMisAJour(Joueur joueur)
    {
	    // Multicast aux clients
        envoyerATous(Protocole.construireMsgJoueurEtat(joueur));
    }
	
	/**
	 * Supprime un joueur de la partie
	 * 
	 * @param ID
	 *            l'ID du joueur à supprimer
	 * @throws CanalException 
	 */
	private synchronized void supprimerJoueur(Joueur joueur)
	{
		if(joueur != null)
		{ 
		    envoyerATous(Protocole.construireMsgJoueurDeconnecte(joueur.getId()));
		    
		    joueur.getEquipe().retirerJoueur(joueur);
		    
		    envoyerATous(Protocole.construireMsgJoueursEtat(jeuServeur.getJoueurs()));
		}
		else
		    logErreur("Joueur inconnu");
	}

	/************************** ACTIONS DES JOUEURS ************************/

	/**
	 * TODO Contrôle de l'argent
	 * 
	 * @param typeVague
	 * @return
	 */
	public synchronized int lancerVague(Joueur joueur, int nbCreatures, int typeCreature)
	{
	    if(joueur == null)
	        return JOUEUR_INCONNU;
	    
	    // TODO les créatures disponibles dervaient être dans le terrain
	    Creature creature = TypeDeCreature.getCreature(typeCreature, jeuServeur.getNumVagueCourante(), false);
    
        log("Le joueur " + joueur.getPseudo() + " désire lancer une vague de "+nbCreatures+" créatures de type"
                + creature.getNom());
        
	    // si le joueur n'a pas perdu
        if(joueur.getEquipe().aPerdu())
            return JOUEUR_HORS_JEU; 
	    
	    synchronized (joueur)
        {
		    double argentApresAchat = joueur.getNbPiecesDOr() - creature.getNbPiecesDOr() * nbCreatures;
		    
		    if( (int) argentApresAchat >= 0)
		    {
		        VagueDeCreatures vague = new VagueDeCreatures(nbCreatures, creature, VagueDeCreatures.getTempsLancement(creature.getVitesseNormale()), true);

		        joueur.setNbPiecesDOr(argentApresAchat);
	            try
                {
                    jeuServeur.lancerVague(joueur, jeuServeur.getEquipeSuivanteNonVide(joueur.getEquipe()),vague);
                } 
	            catch (ArgentInsuffisantException e)
                {
                    // impossible que ca arrive... 
	                // c'est pas très propre mais j'en avais besoins pour 
                }
	            
	            return OK;
		    }
		    else
		        return ARGENT_INSUFFISANT;
        }  
	    
	    
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
	public synchronized int poserTour(Joueur joueur, int typeTour, int x, int y)
	{
		log("Le joueur " + joueur.getPseudo() + " veut poser une tour de type "
				+ typeTour);
		
        // si le joueur n'a pas perdu
        if(joueur.getEquipe().aPerdu())
            return JOUEUR_HORS_JEU;
		
		// Selection de la tour cible
		Tour tour = null;
        try
        {
            tour = TypeDeTour.getTour(typeTour);
            
            // Assignation des paramêtres
            tour.x = x;
            tour.y = y;
            
            // Assignation du propriétaire
            tour.setProprietaire(joueur);
            
			// Tentative de poser la tour
			jeuServeur.poserTour(tour);
			
		} 
		catch (TypeDeTourInvalideException e1)
        {
            return TYPE_TOUR_INVALIDE;
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
	public synchronized int ameliorerTour(Joueur joueur, int idTour)
	{
		log("Le joueur " + joueur.getPseudo() + " désire améliorer la tour " + idTour);
		
		// si le joueur n'a pas perdu
        if(joueur.getEquipe().aPerdu())
            return JOUEUR_HORS_JEU; 
		
		// Récupération de la tour à améliorer
		Tour tour = jeuServeur.getTour(idTour);
		
		if (tour == null)
			return TOUR_INCONNUE;
		
		// si le joueur est bien le propriétaire de la tour
		if(tour.getPrioprietaire().getId() != joueur.getId())
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
        } catch (JoueurHorsJeu e)
        {
            return JOUEUR_HORS_JEU;
        }
		
		return OK;
	}

    /**
	 * 
	 * @param tourCibleDel
	 * @return
	 */
	public synchronized int vendreTour(Joueur joueur, int tourCible)
	{
		log("Le joueur " + joueur.getPseudo() + " désire supprimer la tour " + tourCible);
		
        // si le joueur n'a pas perdu
        if(joueur.getEquipe().aPerdu())
            return JOUEUR_HORS_JEU;
		
		
		// Repérage de la tour à supprimer
		Tour tour = jeuServeur.getTour(tourCible);
		
		if (tour == null)
			return ERREUR;
		
		// seul le proprio peut vendre la tour
		if(tour.getPrioprietaire().getId() != joueur.getId())
		    return ACTION_NON_AUTORISEE;
		
		// On effectue l'action
		try
        {
            jeuServeur.vendreTour(tour);
        } 
		catch (ActionNonAutoriseeException e){}
		
		return OK;
	}

	/**
	 * Envoi un message texte à l'ensemble des clients connectés.
	 * 
	 * @param IDFrom
	 *            L'ID de l'expéditeur.
	 * @param message
	 *            Le message à envoyer.
	 * @throws CanalException 
	 * @throws JSONException 
	 */
	public synchronized void envoyerMessageChatPourTous(int idJoueur, String message) throws JSONException, CanalException
	{
		log("Le joueur " + idJoueur + " dit : " + message);
		
		for (Entry<Integer, JoueurDistant> joueur : clients.entrySet())
			joueur.getValue().envoyerSurCanalMAJ(Protocole.construireMsgMessage(idJoueur, message));
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
	 * @throws CanalException 
	 * @throws JSONException 
	 */
	public synchronized void envoyerMsgClient(int idJoueur, int IDTo, String message) throws JSONException, CanalException
	{
		log("Le joueur " + idJoueur + " désire envoyer un message à " + IDTo
				+ "(" + message + ")");
		clients.get(IDTo).envoyerSurCanalMAJ(Protocole.construireMsgMessage(idJoueur, message));
	}

	/**
	 * Permet de Mutli-caster a tous les clients
	 * 
	 * @param message le message à diffuser
	 * @throws CanalException 
	 */
	private synchronized void envoyerATous(String message)
	{   
	    ArrayList<Integer> joueurSupprimes = new ArrayList<Integer>();
	    
	    synchronized(clients)
	    {
	        for (Entry<Integer, JoueurDistant> joueur : clients.entrySet())
                try
                {
                    joueur.getValue().envoyerSurCanalMAJ(message);
                }
                catch (CanalException e)
                {
                    // le joueur à un canal corrompu
                    joueurSupprimes.add(joueur.getValue().getId());
                }
                

            // pour chaque suppression on indique aux autres joueurs 
            // la deconnexion du joueur
            for (Integer integer : joueurSupprimes) 
            {
                Joueur joueur = jeuServeur.getJoueur(integer);
                
                joueurDeconnecte(joueur);
            }
        }
	}
	
	
    public void joueurDeconnecte(Joueur joueur)
    { 
        // si il est pas déjà deconnecte ?
        if(clients.containsKey(joueur.getId()))
        {
            clients.remove(joueur.getId());
            
            // l'hote met fin à la partie
            //if(joueur.getId() == createur.getId())
            //    envoyerATous(Protocole.construireMsgPartieChangementEtat(PARTIE_STOPPEE_BRUTALEMENT));
            
            if(jeuServeur.estDemarre())
                mettreHorsJeu(joueur);
            else
                supprimerJoueur(joueur);  
        }
    }
    

    public String changerEquipe(int idJoueur, int idEquipe)
    {
        Joueur joueur   = jeuServeur.getJoueur(idJoueur);
        Equipe equipe   = jeuServeur.getEquipe(idEquipe);
        
        String message = null;
        
        try {
            
            equipe.ajouterJoueur(joueur);

            // SUCCES
            message = Protocole.construireMsgChangerEquipe(OK); 
            
            envoyerATous(Protocole.construireMsgJoueursEtat(getJoueurs()));
        }
        catch (AucunePlaceDisponibleException e)
        {
            // ECHEC
            message = Protocole.construireMsgChangerEquipe(PAS_DE_PLACE);
        } 

        return message;
    }

    @Override
    public void partieInitialisee()
    {
        envoyerATous(Protocole.construireMsgPartieChangementEtat(PARTIE_INITIALISEE));
    }
    
    protected synchronized static void log(String msg)
    {
        if(verbeux)
            System.out.println("[SERVEUR] "+ msg);
    }

    public void stopper()
    {
        port.liberer();
        
        envoyerATous(Protocole.construireMsgPartieChangementEtat(PARTIE_STOPPEE_BRUTALEMENT));
    }
    
    private void canalErreur(Exception e)
    {
        System.out.println("ServeurJeu.canalErreur");
        e.printStackTrace();
        
        // libération du port
        port.liberer(); 
    }

    public ArrayList<Joueur> getJoueurs()
    { 
        return jeuServeur.getJoueurs();
    }

    public int getIdCreateur()
    {
        return createur.getId();
    }
    
    /**
     * Permet d'afficher des message log d'erreur
     * 
     * @param msg le message
     */
    private void logErreur(String msg)
    {
        System.out.println("[SERVEUR][ERREUR] "+ msg);
    }
    
    /**
     * Permet d'afficher des message log d'erreur
     * 
     * @param msg le message
     */
    @SuppressWarnings("unused")
    private void logErreur(String msg,Exception e)
    {
        System.out.println("[SERVEUR][ERREUR] "+ msg);
        
        e.printStackTrace();
    }

    private void mettreHorsJeu(Joueur joueur)
    {
        joueur.mettreHorsJeu();
        
        envoyerATous(Protocole.construireMsgJoueurDeconnecte(joueur.getId()));
    }

    @Override
    public void equipeAPerdue(Equipe equipe)
    {
        envoyerATous(Protocole.construireMsgEquipeAPerdue(equipe.getId()));
    }
}
