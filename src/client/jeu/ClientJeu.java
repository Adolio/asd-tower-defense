package client.jeu;

import java.net.ConnectException;
import serveur.jeu.ConstantesServeurJeu;

import reseau.Canal;
import reseau.CanalException;

import org.json.*;

public class ClientJeu implements ConstantesServeurJeu {
	final int ID;
	Canal canal;
	private final static String IP_SERVEUR = "localhost";
	
	public ClientJeu(int ID) {
		this.ID = ID;
		
		try
		{
			canal = new Canal(IP_SERVEUR, 2357, true);
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
		ClientJeu client = new ClientJeu(1);
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
			
			canal.envoyerString(json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	//TODO ID reelement necessaire?
	public void envoyerEtatJoueur(int etat){
		try{
			JSONObject json = new JSONObject();
			json.put("TYPE", PLAYER);
			json.put("ETAT", etat);
			
			canal.envoyerString(json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//TODO revoir le nom des parametres
	public int envoyerVague(int typeVague){
		try{
			JSONObject json = new JSONObject();
			json.put("TYPE", WAVE);
			json.put("TYPE_WAVE", typeVague);
			
			canal.envoyerString(json.toString());
			
			String reponse = canal.recevoirString();
			json = new JSONObject(reponse);
			return json.getInt("STATUS");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//TODO quel val retourner si erreur
		return -1;
	}
	
	public void envoyerEtatPartie(int etat){
		try {
			JSONObject json = new JSONObject();
			//TODO GAME au lieu de PLAY?
			json.put("TYPE", PLAY);
			json.put("ETAT", etat);
			
			canal.envoyerString(json.toString());
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
			
			canal.envoyerString(json.toString());
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
			
			canal.envoyerString(json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//TODO suppressionTour ou venteTour?
	public void venteTour(int idTour){
		try {
			JSONObject json = new JSONObject();
			//TODO TOWER_SELL au lieu de TOWER_DEL?
			json.put("TYPE", TOWER_DEL);
			json.put("ID_TOWER", idTour);
			
			canal.envoyerString(json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
