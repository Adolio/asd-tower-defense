package serveur.jeu;

import org.json.JSONException;
import org.json.JSONObject;
import reseau.Canal;
import reseau.CanalException;

/**
 * Une classe qui modélise coté serveur un joueur.
 * 
 * @author Pierre-Do
 */
public class JoueurDistant implements Runnable, ConstantesServeurJeu
{
	private Thread thread;
	private Canal canal;
	private int ID;
	private ServeurJeu serveur;
	private String pseudo = "unknown";
	private int etat = 0;

	/**
	 * Crée un lien avec un joueur distant.
	 * 
	 * @param ID
	 *            L'ID associée au joueur.
	 * @param canal
	 *            Le canal de communication.
	 * @param serveur
	 *            Le serveur de jeu associé au joueur.
	 */
	protected JoueurDistant(int ID, Canal canal, ServeurJeu serveur)
	{
		log("Nouveau client");

		this.canal = canal;
		this.ID = ID;
		this.serveur = serveur;

		thread = new Thread(this);
		thread.start();
	}

	@Override
	protected void finalize() throws Throwable
	{
		// En cas de déréférencement de l'objet, on aura tendance à couper le
		// canal de communication
		couperLeCanal();
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

	/**
	 * Envoi un message au client
	 * 
	 * @param msg
	 *            Le message à envoyer.
	 */
	public void envoyer(final String msg)
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

	/**
	 * Termine la liaison avec le client
	 */
	public void couperLeCanal()
	{
		canal.fermer();
	}

	@Override
	public String toString()
	{
		return "[CLIENT]\n" + "ID : " + ID + "\n" + "Pseudo : " + pseudo + "\n"
				+ "IP : " + canal.getIpClient() + "\n" + "Etat : " + etat;
	}

	private void log(String msg)
	{
		ServeurJeu.log("[CLIENT " + ID + "] " + msg);
	}
}
