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
	// Constantes pour les états de la connexion avec le joueur distant

	private final static int EN_ATTENTE = 0;
	private final static int VALIDATION = 1;
	private final static int EN_JEU = 2;
	private final static int EN_PAUSE = 3;
	private final static int QUITTE = 4;

	// Variables d'instance
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
				finalStateMachin();
			} catch (JSONException e)
			{
				log("ERROR : récéption inconnue " + str);
			} catch (CanalException e)
			{
				log("ERROR : une erreur est survenue durant la connexion");
			}
		}
	}

	/**
	 * Ecoute un message du client.
	 * 
	 * @return Une chaine de caractère.
	 */
	public String getMessage()
	{
		// Récéption du message du client
		synchronized (canal)
		{
			String str = canal.recevoirString();
			log("Récéption de " + str);
			return str;
		}
	}

	/**
	 * Machine d'état du client
	 * 
	 * @throws JSONException
	 *             Erreur levée si la chaine de caractère reçu du serveur n'est
	 *             pas un format JSON
	 */
	private void finalStateMachin() throws JSONException
	{
		String str = "";
		log("Passage dans l'état : " + nomEtat(etat));

		switch (etat)
		{
		case VALIDATION:
			// Envoi de la version du serveur au client
			send(ServeurJeu.VERSION);
			// Attente du pseudo du joueur
			pseudo = getMessage();
			// Passage en état EN_JEU
			etat = EN_JEU;
			break;
		case EN_ATTENTE:
			break;
		case EN_JEU:
			// Interprétation de la chaine
			str = getMessage();
			parse(str);
			break;
		case EN_PAUSE:
			break;
		case QUITTE:
			log("Terminaison de la liaison avec le client");
			break;
		default:
			break;
		}

	}

	/**
	 * Envoi un message au client
	 * 
	 * @param msg
	 *            Le message à envoyer.
	 */
	public void send(final String msg)
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
			send(message.toString());
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
				+ "IP : " + canal.getIpClient() + "\n" + "Etat : "
				+ nomEtat(etat);
	}

	private void log(String msg)
	{
		ServeurJeu.log("[CLIENT " + ID + "] " + msg);
	}

	/**
	 * Donne une représentation sous forme de chaine de caractère de l'état
	 * actuel du client
	 * 
	 * @param etat
	 *            L'état actuel du client
	 * @return Une chaine de caractère
	 */
	public static String nomEtat(int etat)
	{
		switch (etat)
		{
		case EN_ATTENTE:
			return "En attente";
		case EN_JEU:
			return "En jeu";
		case EN_PAUSE:
			return "En pause";
		case VALIDATION:
			return "Validation";
		case QUITTE:
			return "Quitte la partie";
		default:
			return "<BAD>";
		}
	}
}
