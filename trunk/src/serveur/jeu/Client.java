package serveur.jeu;

import org.json.JSONException;
import org.json.JSONObject;

import reseau.Canal;

public class Client implements Runnable
{
	private Thread thread;
	private Canal canal;

	public Client(String ID, Canal canal)
	{
		System.out.println("Nouveau client ID " + ID);
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run()
	{
		// Message du client;
		String str;
		// Envoit de la version du serveur au client
		canal.envoyerString(ServeurJeu.VERSION);
		while(true){
			try
			{
				// Récéption du message du client
				synchronized (canal)
				{
					str = canal.recevoirString();
				}
				parse(str);
			} catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void envoyer(String msg){
		synchronized (canal)
		{
			canal.envoyerString(msg);
		}
	}
	
	private void parse(String str) throws JSONException{
		// Interprétation de la chaine JSON
		JSONObject json = new JSONObject(str);
		// Extraction du type du message
		int type = json.getInt("TYPE");
		switch(type){
		default:
			break;
		}
	}
}
