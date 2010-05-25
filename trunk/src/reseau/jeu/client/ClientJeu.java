package reseau.jeu.client;

import java.net.ConnectException;

import reseau.CanalTCP;
import reseau.CanalException;
import reseau.jeu.serveur.ConstantesServeurJeu;

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
public class ClientJeu implements ConstantesServeurJeu {
	private int ID;
	private CanalTCP canal1;
	private CanalTCP canal2;
	
	
	/*
	 * FIXME (DE AURELIEN) NON L'ID DU JOUEUR JE NE LE CONNAIS PAS ENCORE
	 * C'EST A TOI DE LE DEMANDER AU SERVEUR (IL DOIT TE LE RETOURNER LORSQUE TU LUI
	 * DEMANDE DE REJOINDRE SA PARTIE...)
	 * 
	 * IP_SERVEUR SERA UN PARAMETRE...
	 * PORT_SERVEUR SERA UN PARAMETRE...
	 */
	public ClientJeu(String IPServeur, int portServeur, String pseudo) {
		int port2;
		try
		{
			canal1 = new CanalTCP(IPServeur, portServeur, true);
			canal1.envoyerString(pseudo);
			ID = canal1.recevoirInt();
			canal1.recevoirString();
			port2 = canal1.recevoirInt();
			canal2 = new CanalTCP(IPServeur, port2, true);
			
			
			//TODO Recevoir l'id! :D
		} catch (ConnectException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CanalException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println("Creation du client");
		ClientJeu client = new ClientJeu("localhost", 2357, "Tux");
		for(int i = 0; i < 5; i++){
			
			
			System.out.println("Envoi de PING");
			client.envoyerMessage("PING!", TO_ALL);
//			
//			System.out.println("Récéption : "+canal.recevoirString());
//			System.out.println("Fermeture");
//			canal.fermer();

		}
	}
	
	//TODO controler les betises de l'expediteur (guillemets, etc..)
	public void envoyerMessage(String message, int cible){
		try {
			JSONObject json = new JSONObject();
			json.put("TYPE", MSG);
			JSONObject content = new JSONObject();
			content.put("CIBLE", TO_ALL);
			content.put("MESSAGE", "foo bar");
			json.put("CONTENU", content);
			
			canal1.envoyerString(json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	//TODO ID reelement necessaire? 
	// (DE AURELIEN) ... NON CAR LE SERVEUR CONNAIT LE CLIENT AVEC LEQUEL IL COMMUNIQUE
	public void envoyerEtatJoueur(int etat){
		try{
			JSONObject json = new JSONObject();
			json.put("TYPE", PLAYER);
			json.put("ETAT", etat);
			
			canal1.envoyerString(json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//TODO revoir le nom des parametres
	// (DE AURELIEN) PARAMETRES :
	// int nbCreatures
	// int typeCreature
	public void envoyerVague(int nbCreature, int typeCreature){
		try{
			JSONObject json = new JSONObject();
			json.put("TYPE", WAVE);
			json.put("TYPE_WAVE", typeCreature);
			json.put("SIZE_WAVE", nbCreature);
			
			canal1.envoyerString(json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void envoyerEtatPartie(int etat){
		try {
			JSONObject json = new JSONObject();
			//TODO GAME au lieu de PLAY?
			json.put("TYPE", PLAY);
			json.put("ETAT", etat);
			
			canal1.envoyerString(json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void demanderCreationTour(int x, int y, int type){
		try {
			JSONObject json = new JSONObject();
			json.put("TYPE", TOWER);
			json.put("X", x);
			json.put("Y", y);
			//TODO regarder pour le doublon
			json.put("SORT", type);
			
			canal1.envoyerString(json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void demanderAmeliorationTour(int idTour){
		try {
			JSONObject json = new JSONObject();
			json.put("TYPE", TOWER_UP);
			json.put("ID_TOWER", idTour);
			
			canal1.envoyerString(json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//TODO suppressionTour ou venteTour?
	// (DE AURELIEN) ... VENTE CAR ON REGAGNE DE LA TUNE COTE SERVEUR!
	// PAR CONTRE TU RECEVERA UNE SUPPRESSION DE TOUR DE LA PART SERVEUR.
	public void venteTour(int idTour){
		try {
			JSONObject json = new JSONObject();
			//TODO TOWER_SELL au lieu de TOWER_DEL?
			// (DE AURELIEN) ... EFFECTIVEMENT! MAIS TOWER_DEL EN RETOUR DU SERVEUR
			json.put("TYPE", TOWER_DEL);
			json.put("ID_TOWER", idTour);
			
			canal1.envoyerString(json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//TODO transformer en thread
	public void receptionMessages(){
		JSONObject mes;
		try {
			mes = new JSONObject(canal1.recevoirString());
			
			switch(mes.getInt("TYPE")){
				case TOUR :
					break;
				default :
						
			}
		} catch (CanalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
