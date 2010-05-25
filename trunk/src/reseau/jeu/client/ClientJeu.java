package reseau.jeu.client;

import java.net.ConnectException;
import reseau.CanalTCP;
import reseau.CanalException;
import reseau.jeu.serveur.ConstantesServeurJeu;
import models.jeu.Jeu_Client;
import models.tours.*;
import exceptions.*;
import org.json.*;

public class ClientJeu implements ConstantesServeurJeu, IDTours, Runnable{
	
    private int ID;
	private CanalTCP canal1;
	private CanalTCP canal2;
	private Jeu_Client jeu;
    private final boolean DEBUG = true;
    
	public ClientJeu(Jeu_Client jeu, 
	                 String IPServeur, 
	                 int portServeur, 
	                 String pseudo) 
	                 throws ConnectException, CanalException 
	{
		this.jeu = jeu;

		// création du canal 1 (Requête / réponse)
		canal1 = new CanalTCP(IPServeur, portServeur, true);
		
		// demande de connexion au serveur (canal 1)
		canal1.envoyerString(pseudo);
		
		// le serveur nous retourne notre identificateur
		ID = canal1.recevoirInt();
		
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
	
	
	//TODO controler les betises de l'expediteur (guillemets, etc..)
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
	
	
	public void envoyerEtatJoueur(int etat){
		try
		{
			JSONObject json = new JSONObject();
			json.put("TYPE", JOUEUR);
			json.put("ETAT", etat);
			
			canal1.envoyerString(json.toString());
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	public void envoyerVague(int nbCreature, int typeCreature)
	{
		try
		{
			JSONObject json = new JSONObject();
			json.put("TYPE", VAGUE);
			json.put("TYPE_WAVE", typeCreature);
			json.put("SIZE_WAVE", nbCreature);
			
			canal1.envoyerString(json.toString());
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void envoyerEtatPartie(int etat)
	{
	    try 
		{
			JSONObject json = new JSONObject();
			json.put("TYPE", PARTIE);
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
	 * @param 
	 * @throws ArgentInsuffisantException
	 */
	public void demanderAmeliorationTour(Tour tour) throws ArgentInsuffisantException
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
            }
			
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	

	public void venteTour(Tour tour)
	{
		try 
		{
			JSONObject json = new JSONObject();
			json.put("TYPE", TOUR_SUPRESSION);
			json.put("ID_TOWER", tour.getId());
			canal1.envoyerString(json.toString());
			
			canal1.recevoirString(); // pas d'erreur possible...
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * initialise et pose la tour
	 * 
	 * @param tourInfo le JSON des infos de la tour
	 * @throws JSONException 
	 * @throws TypeDeTourInvalideException 
	 */
	private void receptionTour(JSONObject tourInfo) 
	    throws TypeDeTourInvalideException, JSONException
	{
	   
	    log("Réception d'un objet de type : Tour.");
	    
	    Tour tour = null;

	    // création de la tour en fonction de son type
        switch(tourInfo.getInt("TYPE_TOUR"))
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
                        + tourInfo.getString("TYPE_TOUR") + " est invalide");
            
        }
        
        // initialisation des tours
        tour.x = tourInfo.getInt("X");
        tour.y = tourInfo.getInt("Y");
        tour.setId(tourInfo.getInt("ID_TOUR"));
        
        jeu.poserTourDirect(tour);
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
                
                switch(resultat.getInt("TYPE"))
                {
                    case TOUR_AJOUT :
                        receptionTour(resultat);
                        break;
                    
                    case TOUR_AMELIORATION :
                        receptionAmeliorationTour(resultat);
                        break;    
                        
                    case TOUR_SUPRESSION :
                        receptionVendreTour(resultat);
                        break;
                        
                    case CREATURE :    
                        receptionCreature(resultat);
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

    private void receptionAmeliorationTour(JSONObject resultat)
    {
        log("Réception de l'amélioration d'une tour");
        
        try
        {
            int idTour = resultat.getInt("ID_TOUR");
            jeu.ameliorerTourDirect(idTour);
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    private void receptionVendreTour(JSONObject resultat)
    {
        log("Réception de la suppression d'une tour");
        
        try
        {
            int idTour = resultat.getInt("ID_TOUR");
            jeu.supprimerTourDirect(idTour);
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void receptionCreature(JSONObject resultat)
    {
        log("Réception d'un objet de type : Créature.");
    }
    
    
    public void log(String msg)
    {
        if(DEBUG)
            System.out.println("[CLIENT][JOUEUR "+ID+"] "+msg);
    }
    
}
