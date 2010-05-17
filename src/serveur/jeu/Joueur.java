package serveur.jeu;

import java.io.EOFException;

import org.json.JSONException;
import org.json.JSONObject;

import reseau.Canal;
import reseau.CanalException;

public class Joueur implements Runnable, ConstantesServeurJeu
{
	private Thread thread;
	private Canal canal;
	private int ID;
	private ServeurJeu serveur;
	private String pseudo = "unknown";

	public Joueur(int ID, Canal canal, ServeurJeu serveur)
	{
		log("Nouveau client");

		this.canal = canal;
		this.ID = ID;
		this.serveur = serveur;

		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run()
	{
		// Message du client;
		String str = "";
		// Envoi de la version du serveur au client
		canal.envoyerString(ServeurJeu.VERSION);
		while (true)
		{
			try
			{
				// Récéption du message du client
				synchronized (canal)
				{
					str = canal.recevoirString();
					log("Récéption de " + str);
				}
				// Interprétation de la chaine
				parse(str);
			} catch (JSONException e)
			{
				log("ERROR : récéption inconnue " + str);
			} catch (CanalException e)
			{
				log("ERROR : une erreur est survenue durant la connexion");
				break;
			}

			log("Terminaison de la liaison avec le client");
		}
	}

	public void envoyer(String msg)
	{
		synchronized (canal)
		{
			log("Envoi de " + msg);
			canal.envoyerString(msg);
		}
	}

	private void parse(String str) throws JSONException
	{
		// Interprétation de la chaine JSON
		JSONObject json = new JSONObject(str);
		// Extraction du type du message
		int type = json.getInt("TYPE");
		switch (type)
		{
		// Récéption d'un message texte
		case MSG:
			log("Message reçu de " + ID);
			// Extraction de l'ID du client
			int _ID = json.getInt("ID_Player");
			if (ID != _ID)
				log("Ooops, mauvaise ID du client");
			// Extraction du message
			JSONObject message = json.getJSONObject("message");
			// Extraction de la cible du message
			int cible = message.getInt("cible");
			log("Message pour " + cible);
			// Extraction du texte du message
			String text = message.getString("message");
			log("Texte : " + text);
			if (cible == TO_ALL)
			{
				// On broadcast le message à tous les clients
				serveur.direATous(ID, text);
			} else
			{
				// On envoit un message à un client en particulier
				serveur.direAuClient(ID, cible, text);
			}
			break;
		// Changement d'état d'un joueur
		case PLAYER:
			break;
		// Action sur une vague
		case WAVE:
			break;
		// Changement d'état d'une partie
		case PLAY:
			break;
		// Requête de création d'une tour
		case TOWER:
			break;
		// Requête d'amélioration d'une tour
		case TOWER_UP:
			break;
		// Requete de suppresion d'une tour
		case TOWER_DEL:
			break;
		default:
			log("ERROR action inconnue : " + type);
			break;
		}
	}

	/**
	 * @param IDFrom
	 * @param contenu
	 */
	public void envoyerMessageTexte(int IDFrom, String contenu)
	{
		// Message JSON
		JSONObject message = new JSONObject();
		try
		{
			// Construction de la structure JSON
			message.put("TYPE", MSG);
			message.put("ID_Player", IDFrom);
			message.put("message", contenu);
			// Envoit de la structure à travers le réseau
			envoyer(message.toString());
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	public void couperLeCanal()
	{
		canal.fermer();
	}

	public void log(String msg)
	{
		ServeurJeu.log("[CLIENT " + ID + "] " + msg);
	}
}
