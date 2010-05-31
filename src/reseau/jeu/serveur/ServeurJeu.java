package reseau.jeu.serveur;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.json.JSONException;

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
public class ServeurJeu extends Observable implements ConstantesServeurJeu,
		EcouteurDeJeu, Runnable
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
     * Temps de rafraichissement des éléments
     */
	private long TEMPS_DE_RAFFRAICHISSEMENT = 100;
	
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
	 * FIXME libérer le port !
	 */
	private Port port;
	
	
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
		
		// Réglage du niveau d'affichage des messages clients
        JoueurDistant.verbeux = verbeux;
		
        // Réservation du port d'écoute
        port = new Port(PORT);
        
        // reservation du port
        port.reserver();
          
		// Lancement du thread serveur.
		(new Thread(this)).start();
	}

	@Override
	public void run()
	{  
	    // Canal d'écoute
        CanalTCP canal = null;
        
        try
        {
    	    // Boucle d'attente de connections
            while (true)
            {
                try
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
                    
                    enregistrerClient(joueur, canal);
                } 
                catch (JeuEnCoursException e){
    
                    log("Joueur refusé - jeu est en cours");
                    
                    // Envoye de la réponse
                    canal.envoyerString(Protocole.construireMsgJoueurInitialisation(JEU_EN_COURS));   
                }
                
                catch (AucunePlaceDisponibleException e){
                    
                    log("Joueur refusé - aucune place disponible");
    
                    // Envoye de la réponse
                    canal.envoyerString(Protocole.construireMsgJoueurInitialisation(PAS_DE_PLACE));
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
    			// On déconnecte le client; // FIXME
    			
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
                JoueurDistant jd = new JoueurDistant(joueur.getId(), canal, this);
    			clients.put(joueur.getId(), jd);
    			
    			// Notification des clients
    			// TODO réellement nécessaire ?
    	        //envoyerATous(Protocole.construireMsgJoueurAjout(joueur));
    	        
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
	    /*
	     *  TODO uniquement pour les joueurs concernée
	     *  -> les joueurs de l'equipe qui a perdu une vie
	     */
	    try
        {
            envoyerATous(Protocole.construireMsgCreatureArrivee(creature));
        } 
	    catch (CanalException e)
        {
	        canalErreur(e);
        }
	}

	@Override
	public void creatureBlessee(Creature creature)
	{
	    // detectable lors de la mise a jour par l'état d'une creature
	}

	@Override
	public void creatureTuee(Creature creature)
	{
        try
        {
            envoyerATous(Protocole.construireMsgCreatureSuppression(creature));
        } 
        catch (CanalException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

    @Override
	public void etoileGagnee(){}

	
	@Override
	public void partieTerminee()
	{
        try
        {
            envoyerATous(Protocole.construireMsgPartieTerminee());
        } 
        catch (CanalException e)
        {
            canalErreur(e);
        }
	}

	@Override
	public void vagueEntierementLancee(VagueDeCreatures vague){}

	@Override
	public void animationAjoutee(Animation animation)
	{
	    // TODO
	    //JSONObject requete = Protocole.construireMsgAjoutAnimation();
        //envoyerATous(requete.toString());
	}

	@Override
	public void animationTerminee(Animation animation){}
	
	@Override
	public void creatureAjoutee(Creature creature)
	{
	    try
        {
            envoyerATous(Protocole.construireMsgCreatureAjout(creature));
        } 
	    catch (CanalException e)
        {
	        canalErreur(e);
        }
	}

    @Override
	public void joueurAjoute(Joueur joueur){}
    
	@Override
	public void partieDemarree()
	{
	    // Signalisation aux clients que la partie à commencé
        for (Entry<Integer, JoueurDistant> joueur : clients.entrySet())
            joueur.getValue().lancerPartie();
        
        // Notification des joueurs
        try
        {
            //envoyerATous(Protocole.construireMsgPartieChangementEtat(PARTIE_LANCEE));
        
            envoyerATous(Protocole.construireMsgJoueursEtat(getJoueurs()));
        } 
        catch (CanalException e)
        {
            canalErreur(e);
        }

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
                        try
                        {
                            envoyerATous(Protocole.construireMsgCreatureEtat(creature));
                        } 
                        catch (CanalException e)
                        {
                            canalErreur(e);
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
	    log("Mise à jour du joueur "+joueur.getPseudo());
	    
        try
        {
            envoyerATous(Protocole.construireMsgJoueurEtat(joueur));
        } 
        catch (CanalException e)
        {
            canalErreur(e);
        }
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
	    
	    // TODO FROM le terrain
	    Creature creature = TypeDeCreature.getCreature(typeCreature);
    
        log("Le joueur " + IDPlayer + " désire lancer une vague de "+nbCreatures+" créatures de type"
                + creature.getNom());
        
		Joueur j = jeuServeur.getJoueur(IDPlayer);
		
		if(j != null)
		{
    		synchronized (j)
            {
    		    int argentApresAchat = j.getNbPiecesDOr() - creature.getNbPiecesDOr() * nbCreatures;
    		    
    		    if(argentApresAchat >= 0)
    		    {
    		        // TODO... 
    		        int tempsLancement = 500;
    		        
    		        VagueDeCreatures vague = new VagueDeCreatures(nbCreatures, creature, tempsLancement, true);
    
    		        j.setNbPiecesDOr(argentApresAchat);
    	            try
                    {
                        jeuServeur.lancerVague(j, jeuServeur.getEquipeSuivanteNonVide(j.getEquipe()),vague);
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
		else
		    return JOUEUR_INCONNU;  
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
        try
        {
            tour = TypeDeTour.getTour(typeTour);
            
            // Assignation des paramêtres
            tour.x = x;
            tour.y = y;
            
            // Assignation du propriétaire
            tour.setProprietaire(jeuServeur.getJoueur(idJoueur));
       
			// Tentative de poser la tour
			jeuServeur.poserTour(tour);
			
			// Multicast aux clients
	        envoyerATous(Protocole.construireMsgTourAjout(tour).toString());
		} 
		catch (TypeDeTourInvalideException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
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
			return TOUR_INCONNUE;
		
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
        try
        {
            envoyerATous(Protocole.construireMsgTourAmelioration(tour).toString());
        } 
        catch (CanalException e)
        {
            canalErreur(e);
        }
		
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
        try
        {
            envoyerATous(Protocole.construireMsgTourSuppression(tour).toString());
        } 
        catch (CanalException e)
        {
            canalErreur(e);
        }
		
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
	public synchronized void direATous(int IDPlayer, String message) throws JSONException, CanalException
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
	 * @throws CanalException 
	 * @throws JSONException 
	 */
	public synchronized void direAuClient(int IDPlayer, int IDTo, String message) throws JSONException, CanalException
	{
		log("Le joueur " + IDPlayer + " désire envoyer un message à " + IDTo
				+ "(" + message + ")");
		clients.get(IDTo).envoyerMessageTexte(IDPlayer, message);
	}

	/**
	 * Permet de Mutli-caster a tous les clients
	 * 
	 * @param message le message à diffuser
	 * @throws CanalException 
	 */
	private void envoyerATous(String message) throws CanalException
	{   
	    synchronized(clients)
	    {
    	    for (Entry<Integer, JoueurDistant> joueur : clients.entrySet())
                joueur.getValue().envoyerSurCanalMAJ(message);
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
        catch (CanalException e)
        {
            canalErreur(e);
        }
        
        return message;
    }

    @Override
    public void partieInitialisee()
    {
        try
        {
            envoyerATous(Protocole.construireMsgPartieChangementEtat(PARTIE_INITIALISEE));
        } 
        catch (CanalException e)
        {
            canalErreur(e);
        }
    }
    
    protected synchronized static void log(String msg)
    {
        if(verbeux)
            System.out.println("[SERVEUR] "+ msg);
    }

    public void stopper()
    {
        port.liberer();
        
        try
        {
            envoyerATous(Protocole.construireMsgPartieChangementEtat(PARTIE_STOPPEE));
        } 
        catch (CanalException e)
        { 
            canalErreur(e);
        } 
    }
    
    private void canalErreur(Exception e)
    {
        //
        port.liberer();
        System.out.println("ServeurJeu.canalErreur");
        
        // envoi si possible...
        try
        {
            envoyerATous(Protocole.construireMsgPartieChangementEtat(PARTIE_STOPPEE));
        } 
        catch (CanalException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        //e.printStackTrace();   
    }

    // TODO peut faire mieux
    public ArrayList<Joueur> getJoueurs()
    { 
        return jeuServeur.getJoueurs();
    }

    public int getIdCreateur()
    {
        return createur.getId();
    }
}
