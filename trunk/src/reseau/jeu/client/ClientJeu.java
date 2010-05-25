package reseau.jeu.client;

import java.net.ConnectException;

import reseau.CanalTCP;
import reseau.CanalException;
import reseau.jeu.serveur.ConstantesServeurJeu;
import models.jeu.Jeu_Client;
import models.tours.*;
import exceptions.*;

import org.json.*;

/* 
 * DES IDEES : (DE AURELIEN)
 * 
 * 1) THREAD D'ECOUTE ET NOTIFICATIONS
 * 
 * TU DEVRAS SUREMENT CREER UN THREAD DEDIE A L'ECOUTE DU SERVEUR QUI NE FAIT QUE 
 * D'ATTENDRE DES INFOS DU SERVEUR. 
 * 
 * UN FOIS UNE INFO RECUPEREE, TU TROUVE LA BONNE BRANCHE DANS UN GROS 
 * SWITCH ET LA TU VAS NOTIFIER TES ECOUTEURS (MOI J'AI BESOINS DE T'ECOUTER POUR
 * ETRE AU COURANT QUAND QQCHOSE CE PASSE...)
 * 
 * 
 * 2) PENSER QUE LE SERVEUR DOIT TE RETOURNER L'ID DE LA TOUR SI
 * ELLE EST POSABLE... ON EN A BESOINS POUR LA SUITE.
 */
public class ClientJeu implements ConstantesServeurJeu, IDTours, Runnable{
	private int ID;
	private CanalTCP canal1;
	private CanalTCP canal2;
	private Jeu_Client jeu;
    private final boolean DEBUG = true;
	
	/*
	 * FIXME (DE AURELIEN) NON L'ID DU JOUEUR JE NE LE CONNAIS PAS ENCORE
	 * C'EST A TOI DE LE DEMANDER AU SERVEUR (IL DOIT TE LE RETOURNER LORSQUE TU LUI
	 * DEMANDE DE REJOINDRE SA PARTIE...)
	 * 
	 * IP_SERVEUR SERA UN PARAMETRE...
	 * PORT_SERVEUR SERA UN PARAMETRE...
	 */
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
	
	
	//TODO ID reelement necessaire? 
	// (DE AURELIEN) ... NON CAR LE SERVEUR CONNAIT LE CLIENT AVEC LEQUEL IL COMMUNIQUE
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
	
	
	public void envoyerVague(int nbCreature, int typeCreature){
		try{
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
	
	public void envoyerEtatPartie(int etat){
		try 
		{
			JSONObject json = new JSONObject();
			//TODO GAME au lieu de PLAY?
			json.put("TYPE", PARTIE);
			json.put("ETAT", etat);
			
			canal1.envoyerString(json.toString());
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void demanderCreationTour(int x, int y, int type) 
	throws NoMoneyException, BadPosException
	{
		try 
		{
			// envoye de la requete d'ajout
		    JSONObject json = new JSONObject();
			json.put("TYPE", TOUR_AJOUT);
			json.put("X", x);
			json.put("Y", y);
			//TODO regarder pour le doublon
			json.put("SORT", type);
			
		    if(DEBUG)
                afficherMessage("Envoye d'une demande de pose d'une tour");
			
			canal1.envoyerString(json.toString());
			
			// attente de la réponse
			String resultat = canal1.recevoirString();
			JSONObject resultatJSON = new JSONObject(resultat);
			switch(resultatJSON.getInt("STATUS"))
			{
			    case PAS_ARGENT :
			        throw new NoMoneyException("Pas assez d'argent");
			    case ZONE_INACCESSIBLE :
                    throw new BadPosException("Zone non accessible");
			    case CHEMIN_BLOQUE :
                    throw new BadPosException("La tour bloque le chemin");
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void demanderAmeliorationTour(int idTour){
		try 
		{
		    // envoye de la requete de vente
		    JSONObject json = new JSONObject();
			json.put("TYPE", TOUR_AMELIORATION);
			json.put("ID_TOWER", idTour);
			
			if(DEBUG)
			    afficherMessage("Envoye d'une demande de vente d'une tour");
                
			canal1.envoyerString(json.toString());
			
			canal1.recevoirString(); // pas d'erreur possible...

		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	// TODO suppressionTour ou venteTour?
	// (DE AURELIEN) ... VENTE CAR ON REGAGNE DE LA TUNE COTE SERVEUR!
	// PAR CONTRE TU RECEVERA UNE SUPPRESSION DE TOUR DE LA PART SERVEUR.
	public void venteTour(int idTour)
	{
		try 
		{
			JSONObject json = new JSONObject();
			//TODO TOWER_SELL au lieu de TOWER_DEL?
			// (DE AURELIEN) ... EFFECTIVEMENT! MAIS TOWER_DEL EN RETOUR DU SERVEUR
			json.put("TYPE", TOUR_SUPRESSION);
			json.put("ID_TOWER", idTour);
			
			canal1.envoyerString(json.toString());
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
	    if(DEBUG)
	        afficherMessage("Réception d'un objet de type : Tour.");
	    
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
                        + tourInfo.getString("SORT") + " est invalide");
            
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
        try 
        {
            if(DEBUG)
                System.out.print("[CLIENT][JOUEUR "+ID+"] Réception d'un objet de type :");
            
            resultat = new JSONObject(canal2.recevoirString());
            
            switch(resultat.getInt("TYPE"))
            {
                case TOUR_AJOUT :
                    receptionTour(resultat);
                    break;
                
                case TOUR_SUPRESSION :
                    receptionVendreTour(resultat);
                    break;
                    
                case CREATURE :    
                    receptionCreature(resultat);
                    break;
                    
                default :
                    if(DEBUG)
                        afficherMessage("Réception d'un objet de type : Inconnu.");
                        
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

    private void receptionVendreTour(JSONObject resultat)
    {
        if(DEBUG)
            afficherMessage("Réception de la suppression d'une tour");
        
        try
        {
            int idTour = resultat.getInt("ID_TOWER");
            jeu.supprimerTourDirect(idTour);
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void receptionCreature(JSONObject resultat)
    {
        if(DEBUG)
            afficherMessage("Réception d'un objet de type : Créature.");
    }
    
    
    public void afficherMessage(String msg)
    {
        System.out.println("[CLIENT][JOUEUR "+ID+"] "+msg);
    }
    
}
