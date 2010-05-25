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
	public ClientJeu(Jeu_Client jeu, String IPServeur, int portServeur, String pseudo) throws ConnectException, CanalException {
		int port2;
		this.jeu = jeu;

		canal1 = new CanalTCP(IPServeur, portServeur, true);
		
		// demande de connexion au serveur (canal 1)
		canal1.envoyerString(pseudo);
		
		// le serveur nous retourne notre identificateur
		ID = canal1.recevoirInt();
		
		// 
		canal1.recevoirString();
		port2 = canal1.recevoirInt();
		canal2 = new CanalTCP(IPServeur, port2, true);

		(new Thread(this)).start();
		
		//TODO Recevoir l'id! :D
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println("Creation du client");
		//ClientJeu client = new ClientJeu("localhost", 2357, "Tux");
		for(int i = 0; i < 5; i++){
			
			
			System.out.println("Envoi de PING");
		//T	client.envoyerMessage("PING!", TO_ALL);
//			
//			System.out.println("Récéption : "+canal.recevoirString());
//			System.out.println("Fermeture");
//			canal.fermer();

		}
	}
	
	//TODO controler les betises de l'expediteur (guillemets, etc..)
	public void envoyerMessage(String message, int cible){
		try 
		{
			JSONObject json = new JSONObject();
			json.put("TYPE", MSG);
			JSONObject content = new JSONObject();
			content.put("CIBLE", TO_ALL);
			content.put("MESSAGE", "foo bar");
			json.put("CONTENU", content);
			
			canal1.envoyerString(json.toString());
		} 
		catch (JSONException e){
			e.printStackTrace();
		}
		
	}
	
	
	//TODO ID reelement necessaire? 
	// (DE AURELIEN) ... NON CAR LE SERVEUR CONNAIT LE CLIENT AVEC LEQUEL IL COMMUNIQUE
	public void envoyerEtatJoueur(int etat){
		try
		{
			JSONObject json = new JSONObject();
			json.put("TYPE", PLAYER);
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
			json.put("TYPE", WAVE);
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
			json.put("TYPE", PLAY);
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
			json.put("TYPE", TOWER);
			json.put("X", x);
			json.put("Y", y);
			//TODO regarder pour le doublon
			json.put("SORT", type);
			
			if(DEBUG)
			    System.out.println("[CLIENT][JOUEUR "+ID+"] Envoye d'une demande de tour");
			
			canal1.envoyerString(json.toString());
			
			// attente de la réponse
			String resultat = canal1.recevoirString();
			JSONObject resultatJSON = new JSONObject(resultat);
			switch(resultatJSON.getInt("STATUS"))
			{
			    case PAS_ARGENT :
			        throw new NoMoneyException("Pas assez d'argent");
			    case MAUVAISE_POS :
                    throw new BadPosException("Zone non accessible");
			    case CHEM_BLOQUE :
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
			json.put("TYPE", TOWER_UP);
			json.put("ID_TOWER", idTour);
			
			if(DEBUG)
                System.out.println("[CLIENT][JOUEUR "+ID+"] Envoye d'une demande de vente d'une tour");

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
			json.put("TYPE", TOWER_DEL);
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
            System.out.println("Tour");
	    
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
        
        tour.x = tourInfo.getInt("X");
        tour.y = tourInfo.getInt("Y");
        
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
                case TOUR :
                    receptionTour(resultat);
                    break;
                
                case CREATURE :    
                    receptionCreature(resultat);
                    break;
                    
                default :
                    if(DEBUG)
                        System.out.println("Inconnu");
                        
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

    private void receptionCreature(JSONObject resultat)
    {
        if(DEBUG)
            System.out.println("Creature");
    }
}
