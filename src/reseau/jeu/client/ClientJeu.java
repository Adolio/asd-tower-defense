package reseau.jeu.client;

import java.net.ConnectException;
import reseau.CanalTCP;
import reseau.CanalException;
import reseau.jeu.serveur.ConstantesServeurJeu;
import models.animations.GainDePiecesOr;
import models.creatures.*;
import models.jeu.Jeu_Client;
import models.joueurs.Joueur;
import models.tours.*;
import exceptions.*;
import org.json.*;

/**
 * Classe de gestion d'un client de communication réseau.
 * 
 * @author Romain Poulain
 * @author Da Campo Aurélien
 */
public class ClientJeu implements ConstantesServeurJeu, IDTours, IDCreatures, Runnable{
	
    /**
     * Joueur traité
     */
    private Joueur joueur;
    
    /**
     * Canal de ping-pong envoie / reception
     */
	private CanalTCP canal1;
	
	/**
	 * Canal d'écoute du serveur
	 */
	private CanalTCP canal2;
	
	/**
	 * Le jeu du client
	 */
	private Jeu_Client jeu;
	
	/**
	 * Mode verbeux
	 */
    private final boolean verbeux = true;
    
    /**
     * Constructeur
     * 
     * @param jeu
     * @param IPServeur
     * @param portServeur
     * @param pseudo
     * @throws ConnectException
     * @throws CanalException
     */
	public ClientJeu(Jeu_Client jeu, 
	                 String IPServeur, 
	                 int portServeur, 
	                 Joueur joueur) 
	                 throws ConnectException, CanalException 
	{
		this.jeu      = jeu;
		this.joueur   = joueur;
		
		// création du canal 1 (Requête / réponse)
		canal1 = new CanalTCP(IPServeur, portServeur, true);
		
		// demande de connexion au serveur (canal 1)
		canal1.envoyerString(joueur.getPseudo());
		
		// le serveur nous retourne notre identificateur
		joueur.setId(canal1.recevoirInt());
		
		// reception de la version du serveur
		String version = canal1.recevoirString();
		log("Version du jeu : "+version);
		
		// reception du port du canal 2
		int portCanal2 = canal1.recevoirInt();
		
		// création du canal 2 (Reception asynchrone)
		canal2 = new CanalTCP(IPServeur, portCanal2, true);

		// lancement de la tache d'écoute du canal 2
		(new Thread(this)).start();
	}
	
	/**
	 * Envoyer un message chat
	 * 
	 * TODO controler les betises de l'expediteur (guillemets, etc..)
	 * 
	 * @param message le message
	 * @param cible le joueur visé
	 */
	public void envoyerMessage(String message, int cible)
	{
		try 
		{
			JSONObject json = new JSONObject();
			json.put("TYPE", MSG);
			JSONObject content = new JSONObject();
			content.put("CIBLE", A_TOUS);
			content.put("MESSAGE", "foo bar");
			json.put("CONTENU", content);
			
			canal1.envoyerString(json.toString());
		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * TODO lorsque le joueur se casse...
	 * 
	 * @param etat
	 */
	public void envoyerEtatJoueur(int etat){
		try
		{
			JSONObject json = new JSONObject();
			json.put("TYPE", JOUEUR_ETAT);
			json.put("ETAT", etat);
			
			canal1.envoyerString(json.toString());
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet d'envoyer une vague de créatures
	 * 
	 * @param nbCreatures le nombre de créatures
	 * @param typeCreature le type des créatures
	 * @throws ArgentInsuffisantException 
	 */
	public void envoyerVague(VagueDeCreatures vague) throws ArgentInsuffisantException
	{
		try
		{
			JSONObject json = new JSONObject();
			json.put("TYPE", VAGUE);
			json.put("TYPE_CREATURE", Creature.getTypeCreature(vague.getNouvelleCreature()));
			json.put("NB_CREATURES", vague.getNbCreatures());
			
			canal1.envoyerString(json.toString());
			
			// attente de la réponse
            String resultat = canal1.recevoirString();
            JSONObject resultatJSON = new JSONObject(resultat);
            switch(resultatJSON.getInt("STATUS"))
            {
                case ARGENT_INSUFFISANT :
                    throw new ArgentInsuffisantException("Pas assez d'argent");
            }	
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet d'envoyer l'etat de la partie
	 * 
	 * TODO necessaire ?
	 * 
	 * @param etat
	 */
	public void envoyerEtatPartie(int etat)
	{
	    try 
		{
			JSONObject json = new JSONObject();
			json.put("TYPE", PARTIE_ETAT);
			json.put("ETAT", etat);
			
			canal1.envoyerString(json.toString());
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet de demander au serveur de jeu la pose d'une tour
	 * 
	 * @param tour la tour a poser
	 * 
	 * @throws ArgentInsuffisantException si pas assez d'argent
	 * @throws ZoneInaccessibleException si la pose est impossible 
	 */
	public void demanderCreationTour(Tour tour) 
	    throws ArgentInsuffisantException, ZoneInaccessibleException
	{
		try 
		{
		    // envoye de la requete d'ajout
		    JSONObject json = new JSONObject();
			json.put("TYPE", TOUR_AJOUT);
			json.put("X", tour.x);
			json.put("Y", tour.y);
			json.put("TYPE_TOUR", Tour.getTypeDeTour(tour));
			
            log("Envoye d'une demande de pose d'une tour");
			
			canal1.envoyerString(json.toString());
			
			// attente de la réponse
			String resultat = canal1.recevoirString();
			JSONObject resultatJSON = new JSONObject(resultat);
			switch(resultatJSON.getInt("STATUS"))
			{
			    case ARGENT_INSUFFISANT :
			        throw new ArgentInsuffisantException("Pas assez d'argent");
			    case ZONE_INACCESSIBLE :
                    throw new ZoneInaccessibleException("Zone non accessible");
			    case CHEMIN_BLOQUE :
                    throw new ZoneInaccessibleException("La tour bloque le chemin");
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet de demander au serveur de jeu d'améliorer une tour
	 * 
	 * @param tour la tour
	 * @throws ArgentInsuffisantException
	 * @throws ActionNonAutoriseeException 
	 */
	public void demanderAmeliorationTour(Tour tour) throws ArgentInsuffisantException, ActionNonAutoriseeException
	{
		try 
		{
		    // envoye de la requete de vente
		    JSONObject json = new JSONObject();
			json.put("TYPE", TOUR_AMELIORATION);
			json.put("ID_TOWER", tour.getId());
			
			log("Envoye d'une demande de vente d'une tour");
                
			canal1.envoyerString(json.toString());
			
			String resultat = canal1.recevoirString(); // pas d'erreur possible...

			JSONObject resultatJSON = new JSONObject(resultat);
            switch(resultatJSON.getInt("STATUS"))
            {
                case ARGENT_INSUFFISANT :
                    throw new ArgentInsuffisantException("Pas assez d'argent");
                case ACTION_NON_AUTORISEE :
                    throw new ActionNonAutoriseeException("Vous n'êtes pas propriétaire");
            }
			
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * Permet de demander au serveur de jeu la vente d'une tour
     * 
     * @param tour la tour a vendre
	 * @throws ActionNonAutoriseeException 
     */
	public void demanderVenteTour(Tour tour) throws ActionNonAutoriseeException
	{
		try 
		{
			// demande
		    JSONObject json = new JSONObject();
			json.put("TYPE", TOUR_SUPRESSION);
			json.put("ID_TOWER", tour.getId());
			canal1.envoyerString(json.toString());
			
			// reponse
			String resultat = canal1.recevoirString();
            JSONObject resultatJSON = new JSONObject(resultat);
            switch(resultatJSON.getInt("STATUS"))
            {
                case ACTION_NON_AUTORISEE :
                    throw new ActionNonAutoriseeException("Vous n'êtes pas propriétaire");
            }
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
     * Analyse d'un message d'ajout d'une tour
     * 
     * @param message le message
     * @throws TypeDeTourInvalideException 
     */
	private void receptionAjoutTour(JSONObject message) 
	    throws TypeDeTourInvalideException
	{
	   
	    log("Réception d'un objet de type : Tour.");
	    
	    Tour tour = null;

	    // création de la tour en fonction de son type
        try
        {
            switch(message.getInt("TYPE_TOUR"))
            {
                case TOUR_ARCHER : 
                    tour = new TourArcher();
                    break;
                case TOUR_AA : 
                    tour = new TourAntiAerienne();
                    break;
                case TOUR_CANON :
                    tour = new TourCanon();
                    break;
                case TOUR_D_AIR :
                    tour = new TourDAir();
                    break;
                case TOUR_DE_FEU : 
                    tour = new TourDeFeu();
                    break;
                case TOUR_DE_GLACE : 
                    tour = new TourDeGlace();
                    break;
                case TOUR_ELECTRIQUE :
                    tour = new TourElectrique();
                    break;
                default : 
                    throw new TypeDeTourInvalideException("Le type " 
                            + message.getString("TYPE_TOUR") + " est invalide");
                
            }
            
            // initialisation des tours
            tour.x = message.getInt("X");
            tour.y = message.getInt("Y");
            tour.setId(message.getInt("ID_TOUR"));
            
            jeu.poserTourDirect(tour);  
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
	}

	/**
	 * Tache de gestion du canal de réception des messages asynchrones
	 */
	public void run() 
	{
	    JSONObject resultat;
	    
	    while(true)
	    {
	    
            try 
            {
                log("Attente d'un String sur le canal 2...");
                
                resultat = new JSONObject(canal2.recevoirString());
                
                log("Canal 2 recoit : "+resultat);
                
                switch(resultat.getInt("TYPE"))
                {
                    case TOUR_AJOUT :
                        receptionAjoutTour(resultat);
                        break;
                    
                    case TOUR_AMELIORATION :
                        receptionAmeliorationTour(resultat);
                        break;    
                        
                    case TOUR_SUPRESSION :
                        receptionVendreTour(resultat);
                        break;
                        
                    case CREATURE_AJOUT :    
                        receptionAjoutCreature(resultat);
                        break;
   
                    case CREATURE_ETAT :    
                        receptionEtatCreature(resultat);
                        break;
                        
                    case CREATURE_SUPPRESSION :    
                        receptionSuppressionCreature(resultat);
                        break;  
  
                    default :
                        log("Réception d'un objet de type : Inconnu.");      
                }
            } 
            catch (CanalException e) {
                e.printStackTrace();
            } 
            catch (JSONException e) {
                e.printStackTrace();
            } 
            catch (TypeDeTourInvalideException e) {
                e.printStackTrace();
            }
	    }
	}

	/**
     * Analyse d'un message d'amélioration d'une tour
     * 
     * @param message le message
     */
    private void receptionAmeliorationTour(JSONObject message)
    {
        log("Réception de l'amélioration d'une tour");
        
        try
        {
            int idTour = message.getInt("ID_TOUR");
            jeu.ameliorerTourDirect(idTour);
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Analyse d'un message de vente d'une tour
     * 
     * @param message le message
     */
    private void receptionVendreTour(JSONObject message)
    {
        log("Réception de la suppression d'une tour");
        
        try
        {
            int idTour = message.getInt("ID_TOUR");
            jeu.supprimerTourDirect(idTour);
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Analyse d'un message d'info des équipes
     * 
     * TODO
     * 
     * @param message le message
     */
    private void receptionInfoEquipes(JSONObject message)
    {
        log("Réception des etats des equipes");

    }
    
    /**
     * Analyse d'un message d'ajout d'une créature
     * 
     * @param message le message
     */
    private void receptionAjoutCreature(JSONObject message)
    {
        log("Réception d'un l'ajout d'une créature.");
        
        try
        {
            int id = message.getInt("ID_CREATURE");
            int typeCreature = message.getInt("TYPE_CREATURE");
            
            
            int x = message.getInt("X");
            int y = message.getInt("Y");
            long santeMax = message.getInt("SANTE_MAX"); // FIXME ATTENTION getLong attendu
            int nbPiecesDOr = message.getInt("NB_PIECES_OR");
            double vitesse = message.getDouble("VITESSE");
            
            Creature creature;
            
            switch(typeCreature)
            {
                case AIGLE :
                    creature = new Aigle(santeMax, nbPiecesDOr, vitesse);
                    break;
                case ARAIGNEE : 
                    creature = new Araignee(santeMax, nbPiecesDOr, vitesse);
                    break;
                case ELEPHANT :
                    creature = new Elephant(santeMax, nbPiecesDOr, vitesse);
                    break;
                case GRANDE_ARAIGNEE : 
                    creature = new GrandeAraignee(santeMax, nbPiecesDOr, vitesse);
                    break;
                case MOUTON :
                    creature = new Mouton(santeMax, nbPiecesDOr, vitesse);
                    break;
                case PIGEON : 
                    creature = new Pigeon(santeMax, nbPiecesDOr, vitesse);
                    break;
                case RHINOCEROS :
                    creature = new Rhinoceros(santeMax, nbPiecesDOr, vitesse);
                    break;
                default :
                    return; // TODO erreur
            }
            
            creature.setId(id);
            creature.setX(x);
            creature.setY(y);
            
            jeu.ajouterCreatureDirect(creature);
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        } 
    }
    
    /**
     * Analyse d'un message d'état d'une créature
     * 
     * @param message le message
     */
    private void receptionEtatCreature(JSONObject message)
    {
        try {
            
            int id = message.getInt("ID_CREATURE");
            int x = message.getInt("X");
            int y = message.getInt("Y");
            int sante = message.getInt("SANTE");
            double angle = message.getDouble("ANGLE");
            
            Creature creature = jeu.getCreature(id);
            
            // Elle peut avoir été détruite entre-temps.
            if(creature != null)
            {
                creature.x = x;
                creature.y = y;
                creature.setSante(sante);
                creature.setAngle(angle);
            }
        } 
        catch (JSONException e){
            e.printStackTrace();
        }
    }
    
    /**
     * Analyse d'un message de suppression d'une creature
     * 
     * @param message le message
     */
    private void receptionSuppressionCreature(JSONObject message)
    {
        try {
            
            int id = message.getInt("ID_CREATURE");
            
            // TODO A REVOIR... pas sur que ca doit ici.
            Creature creature = jeu.getCreature(id);
            
            if(creature != null)
                jeu.ajouterAnimation(new GainDePiecesOr((int)creature.getCenterX(),
                                                    (int)creature.getCenterY(),
                                                    creature.getNbPiecesDOr()));
            
            jeu.supprimerCreatureDirect(id); 
        } 
        catch (JSONException e){
            e.printStackTrace();
        }
        
    }
    
    /**
     * Permet d'afficher des message log
     * 
     * @param msg le message
     */
    public void log(String msg)
    {
        if(verbeux)
            System.out.println("[CLIENT][JOUEUR "+joueur.getId()+"] "+msg);
    }
    
}
