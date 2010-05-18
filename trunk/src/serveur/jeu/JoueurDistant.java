package serveur.jeu;

import java.net.SocketException;

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
	private int etat = EN_JEU;
	// Message du client;
	private String str = "";
	
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
		// Envoi de la version du serveur au client
		canal.envoyerString(ServeurJeu.VERSION);
		while (true)
		{
			try
			{
				finalStateMachin();
			} catch (JSONException e)
			{
				log("ERROR : récéption inconnue \"" + str+"\"");
			} catch (CanalException e)
			{
				log("ERROR : une erreur est survenue durant la connexion");
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

		switch (etat)
		{
		case VALIDATION:
			// Envoi de la version du serveur au client
			send(ServeurJeu.VERSION);
			// Attente du pseudo du joueur
			String pseudo = getMessage();
			// Passage en état EN_JEU
			etat = EN_ATTENTE;
			break;
		case EN_ATTENTE:
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
		int code;
		switch (type)
		{
		// Récéption d'un message texte
		case MSG:
			log("Message reçu de " + ID);
			// Extraction du message
			JSONObject message = json.getJSONObject("CONTENT");
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
				// On envoit un message à un client en particulier
				serveur.direAuClient(ID, cible, text);
			}
			break;
		// Changement d'état d'un joueur
		case PLAYER:
			// Récupération du nouvel état
			int nouvelEtat = json.getInt("ETAT");
			// Appelle de la fonction de gestion des états et récupération du code d'état
			code = serveur.changementEtatJoueur(ID, nouvelEtat);
			// Réponse du code d'état au client
			repondreEtat(PLAYER,code);
			break;
		// Action sur une vague
		case WAVE:
			// Récupération du type de vague
			int typeVague = json.getInt("TYPE_WAVE");
			// Demande de lancement d'une vague
			code  = serveur.lancerVague(typeVague);
			// Retour au client de l'information
			repondreEtat(WAVE,code);
			break;
		// Changement d'état d'une partie
		case PLAY:
			// Récupération du nouvel état
			int nouvelEtatPartie = json.getInt("ETAT");
			// Envoi de l'information au serveur principal
			code = serveur.changementEtatPartie(nouvelEtatPartie);
			// Retour du code au client
			repondreEtat(PLAY,code);
			break;
		// Requête de création d'une tour
		case TOWER:
			// Extraction des coordonnées
			int x = json.getInt("X");
			int y = json.getInt("X");
			// Extraction du type de tour
			int typeTour = json.getInt("TYPE");
			// Demande d'ajout au serveur
			code = serveur.poserTour(ID, typeTour, x, y);
			// Retour au client du code
			repondreEtat(TOWER,code);
			break;
		// Requête d'amélioration d'une tour
		case TOWER_UP:
			// Récupération de la tour cible
			int tourCible = json.getInt("ID_TOUR");
			// Demande au serveur de l'opération
			code = serveur.ameliorerTour(tourCible);
			// Retour au client de code
			repondreEtat(TOWER_UP,code);
			break;
		// Requete de suppresion d'une tour
		case TOWER_DEL:
			// Récupération de la tour cible
			int tourCibleDel = json.getInt("ID_TOUR");
			// Demande au serveur de l'opération
			code = serveur.supprimerTour(tourCibleDel);
			// Retour au client de code
			repondreEtat(TOWER_UP,code);
			break;
		default:
			log("ERROR action inconnue : " + type);
			// Signaler au client qu'il envoi quelque chose d'incorecte
			repondreEtat(ERROR,ERROR);
			break;
		}
	}

	/**
	 * Répond du client un code d'état pour une réponse donnée
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
			// Envoit de la structure à travers le réseau
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
			// Envoit de la structure à travers le réseau
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
			message.put("ID_Player", IDFrom);
			message.put("message", contenu);
			// Envoit de la structure à travers le réseau
			send(message.toString());
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public void afficherObjet(int ID_Objet, int x, int y, int etat){
		// Message JSON
		JSONObject message = new JSONObject();
		try
		{
			// Construction de la structure JSON
			message.put("TYPE", OBJECT);
			message.put("OBJECT", ID_Objet);
			message.put("ETAT", etat);
			message.put("X",x);
			message.put("Y",y);
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
		// FIXME
		return "[CLIENT]\n" + "ID : " + ID + "\n" + "Pseudo : " + "unknown" + "\n"
				+ "IP : " + canal.getIpClient() + "\n" + "Etat : "
				+ nomEtat(etat);
	}

	private void log(String msg)
	{
		ServeurJeu.log("[CLIENT " + ID + "] " + msg);
	}

	private void desenregistrement()
	{
		serveur.supprimerJoueur(ID);
	}
	
	/**
	 * Met le client en pause
	 */
	public void mettreEnPause(){
		etat = EN_PAUSE;
	}
	
	/**
	 * Reprend la partie
	 */
	public void reprendre(){
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
		case QUITTE:
			return "Quitte la partie";
		default:
			return "<BAD>";
		}
	}
}
