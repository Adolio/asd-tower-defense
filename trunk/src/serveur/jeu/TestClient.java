package serveur.jeu;

import java.net.ConnectException;

import org.json.JSONException;
import org.json.JSONObject;

import reseau.Canal;
import reseau.CanalException;

public class TestClient implements ConstantesServeurJeu
{
	public TestClient(int ID)
	{

		try
		{
			Canal canal = new Canal("localhost", 2357, true);

			// Envoi de l'ID du clietn
			canal.envoyerInt(ID);
			// Récéption de la version du serveur
			System.out.println("Récéption : " + canal.recevoirString());

			while (true)
			{
				JSONObject json = new JSONObject();
				json.put("TYPE", MSG);
				JSONObject content = new JSONObject();
				content.put("CIBLE", TO_ALL);
				content.put("MESSAGE", "foo bar");
				json.put("CONTENU", content);

				canal.envoyerString(json.toString());

				String rcp = canal.recevoirString();
				System.out.println("Récéption : " + rcp);
				json = new JSONObject(rcp);
				System.out.print("["+json.getString("PSEUDO")+"] ");
				System.out.println(json.getString("MESSAGE"));
				synchronized (this)
				{
					wait(1000);					
				}
			}
			// System.out.println("Fermeture");

			// canal.fermer();
		} catch (ConnectException e)
		{
			e.printStackTrace();
		} catch (CanalException e)
		{
			e.printStackTrace();
		} catch (JSONException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		int ID = 0;
		if (args.length > 0)
		{
			ID = Integer.parseInt(args[0]);
		}

		new TestClient(ID);
	}

}
