package serveur.jeu;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import reseau.Canal;
import reseau.Port;

/**
 * Cette classe contiendra le serveur de jeu sur lequel se connecteront tout les
 * cliens.
 * 
 * @author Pierre-Do
 * 
 */
public class ServeurJeu
{
	private static int _port = 2357;
	private static Port port;
	public static final String VERSION = "0.1";
	public static final boolean DEBUG = true;

	private HashMap<Integer, Joueur> clients = new HashMap<Integer, Joueur>();

	private static int IDClient = 0;

	/**
	 * Méthode MAIN : entrée dans le programme
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println("Lancement du serveur sur le port " + _port);
		try
		{
			ServeurJeu serveur = new ServeurJeu();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	public ServeurJeu() throws IOException
	{
		port = new Port(_port);
		port.reserver();
		Canal canal;
		while (true)
		{
			log("écoute sur le port " + _port);
			canal = new Canal(port, DEBUG);
			log("Récéption de " + canal.getIpClient());
			IDClient++;
			clients.put(IDClient, new Joueur(IDClient, canal, this));
		}
	}

	public void direATous(int IDFrom, String message)
	{
		for (Entry<Integer, Joueur> joueur : clients.entrySet())
			joueur.getValue().envoyerMessageTexte(IDFrom, message);
	}
	
	public void direAuClient(int IDFrom, int IDTo, String message){
		clients.get(IDTo).envoyerMessageTexte(IDFrom, message);
	}

	public static void log(String msg)
	{
		System.out.print("[SERVEUR] ");
		System.out.println(msg);
	}

}
