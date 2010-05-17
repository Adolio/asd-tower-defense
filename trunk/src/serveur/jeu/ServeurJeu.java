package serveur.jeu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
		while(true){
			log("écoute sur le port "+_port);
			canal = new Canal(port,true);
			log("Récéption de "+canal.getIpClient());
			log("Récéption : "+canal.recevoirString());
			log("Envoit de PONG");
			canal.envoyerString("PONG");
			log("Fermeture de la connexion");
			canal.fermer();
		}
	}

	public void log(String msg)
	{
		System.out.print("[SERVEUR] ");
		System.out.println(msg);
	}

}
