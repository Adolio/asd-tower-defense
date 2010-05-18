package client.jeu;

import java.net.ConnectException;

import reseau.Canal;
import reseau.CanalException;

import org.json.*;

public class ClientJeu {
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
			client.envoyerMessage("PING!", i);
//			
//			System.out.println("Récéption : "+canal.recevoirString());
//			System.out.println("Fermeture");
//			canal.fermer();

		}
	}
	
	//TODO controler les betises de l'expediteur (guillemets, etc..)
	public void envoyerMessage(String message, int cible){
		//TODO  remplacer par la constante du package
		String type = "MSG";
		
		//TODO utiiser org.json
		canal.envoyerString(
				"{" +
					"\"TYPE\" : " + type +
					"\"ID_Player\" : " + ID +
					"\"message\" : " +
					"{" +
						"\"cible\" : " + cible +
						"\"message\" : " + message +
					"}"+
				"}");
		
	}
}
