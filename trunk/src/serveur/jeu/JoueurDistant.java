package serveur.jeu;

import org.json.JSONException;
import org.json.JSONObject;
import reseau.CanalTCP;
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
	private final static int PARTIE_TERMINEE = 5;

	// Variables d'instance
	private Thread thread;
	// Canal de dialogue commun
	private CanalTCP canal;
	// Canal de mise à jour
	private CanalTCP canal_update;
	private int ID;
	private ServeurJeu serveur;
	private int etat = VALIDATION;
	// Message du client;
	private String str = "";
	/**
	 *  Niveau d'affichage des messages
	 */
	public static int verboseMode = 0;

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
	protected JoueurDistant(int ID, CanalTCP canal, ServeurJeu serveur)
	{
		this.canal = canal;
		this.ID = ID;
		this.serveur = serveur;

		log("Nouveau client");
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
		while (true)
		{
			try
			{
				finalStateMachin();
			} catch (JSONException e)
			{
				log("ERROR : récéption inconnue \"" + str + "\"");
				e.printStackTrace();
			} catch (CanalException e)
			{
				ServeurJeu.log("ERROR : une erreur est survenue durant la connexion");
				ServeurJeu.log("ERROR : Déconexion du client "+ID);
				desenregistrement();
				return;
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
			str = canal.recevoirString();
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

		log("Etat : " + nomEtat(etat));
		switch (etat)
		{
		case VALIDATION:
			// Envoi de la version du serveur au client
			send(ServeurJeu.VERSION);
			// Passage en état EN_JEU
			etat = EN_ATTENTE;
			break;
		case EN_ATTENTE:
			// Le thread se met en attente sur le serveur de jeu
			/*
			 * try // FIXME { serveur.wait(); } catch (InterruptedException e) {
			 * e.printStackTrace(); }
			 */
			// Début de la partie
			etat = EN_JEU;
			break;
		case EN_JEU:
			// Interprétation de la chaine
			parse(getMessage());
			break;
		case EN_PAUSE:
			// La partie continue
			etat = EN_JEU;
			break;
		case PARTIE_TERMINEE:
			// TODO

			break;
		case QUITTE:
			log("Terminaison de la liaison avec le client");
			// On clos la liaison avec le client
			couperLeCanal();
			// On supprime le joueur distant de la liste des clients
			desenregistrement();
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
	public synchronized void send(final String msg)
	{
			log("Envoi du String " + msg);
			canal.envoyerString(msg);
	}

	public void send(final int msg)
	{
		synchronized (canal)
		{
			log("Envoi de l'int " + msg);
			canal.envoyerInt(msg);
		}
	}

	private void parse(String str) throws JSONException
	{
		// Interprétation de la chaine JSON
		JSONObject json = new JSONObject(str);
		// Extraction du type du message
		int type = json.getInt("TYPE");
		log("Récéption d'un message de type " + type);
		int code;
		switch (type)
		{
		// Récéption d'un message texte
		case MSG:
			log("Message reçu de " + ID);
			// Extraction du message
			JSONObject message = json.getJSONObject("CONTENU");
			// Extraction de la cible du message
			int cible = message.getInt("CIBLE");
			log("Message pour " + cible);
			// Extraction du texte du message
			String text = message.getString("MESSAGE");
			log("Texte : " + text);
			if (cible == TO_ALL)
			{
				// On broadcast le message à tous les clients
				serveur.direATous(ID, text);
			} else
			{
				// On envoi un message à un client en particulier
				serveur.direAuClient(ID, cible, text);
			}
			break;
		// Changement d'état d'un joueur
		case PLAYER:
			// Récupération du nouvel état
			int nouvelEtat = json.getInt("ETAT");
			// Le le nouvelEtat n'est pas une commanque QUITTER
			if (nouvelEtat == QUITTER)
			{
				couperLeCanal();
				desenregistrement();
			} else
			{
				// Appelle de la fonction de gestion des états et récupération
				// du
				// code d'état
				code = serveur.changementEtatJoueur(ID, nouvelEtat);
				// Réponse du code d'état au client
				repondreEtat(PLAYER, code);
			}
			break;
		// Action sur une vague
		case WAVE:
			// Récupération du type de vague
			int typeVague = json.getInt("TYPE_WAVE");
			// Demande de lancement d'une vague
			code = serveur.lancerVague(ID, typeVague);
			// Retour au client de l'information
			repondreEtat(WAVE, code);
			break;
		// Changement d'état d'une partie
		case PLAY:
			// Récupération du nouvel état
			int nouvelEtatPartie = json.getInt("ETAT");
			// Envoi de l'information au serveur principal
			code = serveur.changementEtatPartie(ID, nouvelEtatPartie);
			// Retour du code au client
			repondreEtat(PLAY, code);
			break;
		// Requête de création d'une tour
		case TOWER:
			// Extraction des coordonnées
			int x = json.getInt("X");
			int y = json.getInt("Y");
			// Extraction du type de tour
			int typeTour = json.getInt("TYPE");
			// Demande d'ajout au serveur
			code = serveur.poserTour(ID, typeTour, x, y);
			// Retour au client du code
			repondreEtat(TOWER, code);
			break;
		// Requête d'amélioration d'une tour
		case TOWER_UP:
			// Récupération de la tour cible
			int tourCible = json.getInt("ID_TOWER");
			// Demande au serveur de l'opération
			code = serveur.ameliorerTour(ID, tourCible);
			// Retour au client de code
			repondreEtat(TOWER_UP, code);
			break;
		// Requete de suppresion d'une tour
		case TOWER_DEL:
			// Récupération de la tour cible
			int tourCibleDel = json.getInt("ID_TOWER");
			// Demande au serveur de l'opération
			code = serveur.supprimerTour(ID, tourCibleDel);
			// Retour au client de code
			repondreEtat(TOWER_UP, code);
			break;
		default:
			log("ERROR action inconnue : " + type);
			// Signaler au client qu'il envoi quelque chose d'incorecte
			repondreEtat(ERROR, ERROR);
			break;
		}
	}

	/**
	 * Répond du client un code d'état pour une réponse donnée
	 * 
	 * @param player
	 * @param code
	 */
	private void repondreEtat(int type, int code)
	{
		// Message JSON
		JSONObject message = new JSONObject();
		try
		{
			// Construction de la structure JSON
			message.put("TYPE", type);
			message.put("STATUS", code);
			// Envoi de la structure à travers le réseau
			send(message.toString());
		} catch (JSONException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Signal un changement d'état d'un joueur quelconque au joueur géré par le
	 * module.
	 * 
	 * @param pseudoFrom
	 * @param nouvelEtat
	 */
	public void signalerNouvelEtat(String pseudoFrom, int nouvelEtat)
	{
		// Message JSON
		JSONObject message = new JSONObject();
		try
		{
			// Construction de la structure JSON
			message.put("TYPE", PLAYER);
			message.put("PSEUDO", pseudoFrom);
			message.put("ETAT", nouvelEtat);
			// Envoi de la structure à travers le réseau
			send(message.toString());
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Envoi un message texte au joueur géré par le module.
	 * 
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
			message.put("PSEUDO", IDFrom);
			message.put("MESSAGE", contenu);
			// Envoi de la structure à travers le réseau
			send(message.toString());
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	public void afficherObjet(int ID_Objet, int x, int y, int etat)
	{
		// Message JSON
		JSONObject message = new JSONObject();
		try
		{
			// Construction de la structure JSON
			message.put("TYPE", OBJECT);
			message.put("OBJECT", ID_Objet);
			message.put("ETAT", etat);
			message.put("X", x);
			message.put("Y", y);
			// Envoi de la structure à travers le réseau
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
		// FIXME
		return "[CLIENT]\n" + "ID : " + ID + "\n" + "Pseudo : " + "unknown"
				+ "\n" + "IP : " + canal.getIpClient() + "\n" + "Etat : "
				+ nomEtat(etat);
	}

	private void log(String msg)
	{
		if (verboseMode > 0)
			ServeurJeu.log("[JOUEUR " + ID + "]" + msg);
	}

	private void desenregistrement()
	{
		log("Suppression du joueur "+ID);
		serveur.supprimerJoueur(ID);
	}

	/**
	 * Met le client en pause
	 */
	public void mettreEnPause()
	{
		etat = EN_PAUSE;
	}

	/**
	 * Reprend la partie
	 */
	public void lancerPartie()
	{
		// Mise en place de l'état de la partie
		etat = EN_JEU;
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
		case PARTIE_TERMINEE:
			return "Partie terminée";
		case QUITTE:
			return "Quitte la partie";
		default:
			return "<BAD>";
		}
	}

	public void partieTerminee()
	{
		etat = PARTIE_TERMINEE;
	}
}
