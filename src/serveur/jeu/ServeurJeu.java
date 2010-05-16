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
		System.out.println("Lancement du serveur sur le port " + port);
		try
		{
			port = new Port(_port);
			ServeurJeu serveur = new ServeurJeu(port);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	public ServeurJeu(Port port) throws IOException
	{
		ecouter();
	}

	public void log(String msg)
	{
		System.out.print("[SERVEUR] ");
		System.out.println(msg);
	}

	public void ecouter()
	{
		log("Ecoute sur le port " + port);
		while (true)
		{
			// Attente d'une connexion avec un éventuel client
			log("Attente...");
			Canal canal = new Canal(port,true);
			log(canal.recevoirString());
		}
	}

	private class GestionnaireDeConnection implements Runnable
	{
		private Socket socket;
		ObjectInputStream ois;
		ObjectOutputStream oos;

		public GestionnaireDeConnection(Socket socket)
		{
			log("Nouvelle connection avec le client");
			log("Client : "+socket.getInetAddress());
			this.socket = socket;
			Thread t = new Thread(this);
			t.start();
		}

		@Override
		public void run()
		{
			try
			{
				while (true)
				{
					log("Attente de "+socket.getInetAddress());
					ois = new ObjectInputStream(socket.getInputStream());
					String msg = (String) ois.readObject();
					log("Reçu : " + msg);

					oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject("PONG");
					
					ois.close();
					oos.close();
					socket.close();

				}
				} catch (IOException e)
			{
				e.printStackTrace();
			} catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}
}
