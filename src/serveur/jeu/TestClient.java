package serveur.jeu;

import java.net.ConnectException;

import reseau.Canal;
import reseau.CanalException;

public class TestClient
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			Canal canal = new Canal("localhost",2357,true);
			System.out.println("Envoi de PING");
			canal.envoyerString("PING");
			System.out.println("Récéption : "+canal.recevoirString());
			System.out.println("Fermeture");
			canal.fermer();
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

}
