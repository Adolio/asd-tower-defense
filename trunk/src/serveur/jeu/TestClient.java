package serveur.jeu;

import java.net.ConnectException;

import org.json.JSONException;
import org.json.JSONObject;

import reseau.Canal;
import reseau.CanalException;

public class TestClient implements ConstantesServeurJeu
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			Canal canal = new Canal("localhost",2357,false);
			System.out.println("Envoi de l'ID");
			canal.envoyerString("12");
			System.out.println("Récéption : "+canal.recevoirString());
			JSONObject json = new JSONObject();
			json.put("TYPE", MSG);
			JSONObject content = new JSONObject();
			content.put("CIBLE", TO_ALL);
			content.put("MESSAGE","foo bar");
			json.put("CONTENT", content);
			canal.envoyerString(json.toString());
			System.out.println("Récéption : "+canal.recevoirString());
			System.out.println("Fermeture");
			canal.fermer();
		} catch (ConnectException e)
		{
			e.printStackTrace();
		} catch (CanalException e)
		{
			e.printStackTrace();
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
