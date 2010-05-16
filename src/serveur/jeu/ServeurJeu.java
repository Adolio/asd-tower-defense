package serveur.jeu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Cette classe contiendra le serveur de jeu sur lequel se connecteront tout les
 * cliens.
 * 
 * @author Pierre-Do
 * 
 */
public class ServeurJeu
{
	private static int port = 2357;
	private ServerSocket serverSocket;

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
	public ServeurJeu(int port) throws IOException
	{
		serverSocket = new ServerSocket(port);
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
		try
		{
			while (true)
			{
				// Attente d'une connexion avec un éventuel client
				Socket socket = serverSocket.accept();
				new GestionnaireDeConnection(socket);
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
