package serveur.jeu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Map.Entry;

import models.jeu.BadPosException;
import models.jeu.Jeu_Serveur;
import models.jeu.NoMoneyException;
import models.jeu.PathBlockException;
import models.joueurs.Joueur;
import models.tours.Tour;
import models.tours.TourArcher;

import reseau.Canal;
import reseau.Port;

/**
 * Cette classe contiendra le serveur de jeu sur lequel se connecteront tout les
 * cliens.
 * 
 * @author Pierre-Do
 * 
 */
public class ServeurJeu extends Observable implements ConstantesServeurJeu
{
	/**
	 * La version courante du serveur
	 */
	public static final String VERSION = "0.1";

	/**
	 * Le port sur lequel le serveur écoute par defaut
	 */
	public final static int _port = 2357;

	/**
	 * Fanion pour le mode debug
	 */
	private static final boolean DEBUG = false;

	/**
	 * Liste des clients enregistrés sur le serveur
	 */
	private HashMap<Integer, JoueurDistant> clients = new HashMap<Integer, JoueurDistant>();

	/**
	 * Lien vers le module coté serveur du jeu
	 */
	private Jeu_Serveur serveurJeu;

	/**
	 * Méthode MAIN : entrée dans le programme en cas de lancement en standalone
	 * du serveur
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println("Lancement du serveur sur le port " + _port);
		try
		{
			// Création d'un serveur de jeu en standalone
			new ServeurJeu(null);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param serveurJeu
	 * @throws IOException
	 */
	public ServeurJeu(Jeu_Serveur serveurJeu) throws IOException
	{
		// Assignation du serveur
		this.serveurJeu = serveurJeu;
		// Réservation du port d'écoute
		Port port = new Port(_port);
		port.reserver();
		// Canal d'écoute
		Canal canal;
		// Boucle d'attente de connections
		while (true)
		{
			// On attend qu'un joueur se présente
			log("écoute sur le port " + _port);
			canal = new Canal(port, DEBUG);
			log("Récéption de " + canal.getIpClient());
			int IDClient = serveurJeu.getJoueurPrincipal().getId(); // FIXME
			// On inscris le joueur à la partie
			clients.put(IDClient, new JoueurDistant(IDClient, canal, this));
		}
	}

	/**
	 * Envoi un message texte à l'ensemble des clients connectés.
	 * 
	 * @param IDFrom
	 *            L'ID de l'expéditeur.
	 * @param message
	 *            Le message à envoyer.
	 */
	public synchronized void direATous(int IDFrom, String message)
	{
		log("[MESSAGE] " + IDFrom + " dit : " + message);
		for (Entry<Integer, JoueurDistant> joueur : clients.entrySet())
			joueur.getValue().envoyerMessageTexte(IDFrom, message);
	}

	/**
	 * Envoi un message texte à un client en particulier.
	 * 
	 * @param IDFrom
	 *            L'ID de l'expéditeur
	 * @param IDTo
	 *            L'ID du destinataire
	 * @param message
	 *            Le message à envoyer.
	 */
	public synchronized void direAuClient(int IDFrom, int IDTo, String message)
	{
		clients.get(IDTo).envoyerMessageTexte(IDFrom, message);
	}

	/**
	 * Affiche toutes les informations de tous les clients connectés.
	 */
	public synchronized void infos()
	{
		System.out.println("Serveur de jeu");
		System.out.println("Nombre de joueurs : " + clients.size());
		for (Entry<Integer, JoueurDistant> joueur : clients.entrySet())
			System.out.println(joueur.getValue());
	}

	/**
	 * Supprime un joueur de la partie
	 * 
	 * @param ID
	 *            l'ID du joueur à supprimer
	 */
	public synchronized void supprimerJoueur(int ID)
	{
		clients.remove(ID);
		// TODO
		setChanged();
		notifyObservers();
	}

	public synchronized int lancerVague(int typeVague)
	{
		// TODO
		return 0;
	}

	public synchronized int poserTour(int IDJoueur, int typeTour, int x, int y)
	{
		// Selection de la tour cible
		Tour tour = null;
		switch (typeTour)
		{
		case 0:
			tour = new TourArcher();
			break;
		default:
			log("Tour " + typeTour + " inconnue.");
			return ERROR;
		}
		// Assignation des paramêtres
		tour.x = x;
		tour.y = y;
		tour.setProprietaire(repererJoueur(IDJoueur));
		try
		{
			// Tentative de poser la tour
			serveurJeu.poserTour(tour);
		} catch (NoMoneyException e)
		{
			// Si pas assez d'argent on retourne le code d'erreur correspondant
			return NO_MONEY;
		} catch (BadPosException e)
		{
			// Mauvaise position
			return BAD_POS;
		} catch (PathBlockException e)
		{
			// Chemin bloqué.
			return PATH_BLOCK;
		}
		return 0;
	}

	public synchronized int changementEtatJoueur(int iD, int nouvelEtat)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public synchronized int changementEtatPartie(int nouvelEtatPartie)
	{
		switch (nouvelEtatPartie)
		{
		case EN_PAUSE:
			break;
		case EN_JEU:
			break;
		default:
			break;
		}
		return 0;
	}

	public synchronized int ameliorerTour(int tourCible)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public synchronized int supprimerTour(int tourCibleDel)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	protected synchronized static void log(String msg)
	{
		System.out.print("[SERVEUR] ");
		System.out.println(msg);
	}

	private Joueur repererJoueur(int ID)
	{
		for (Joueur joueur : serveurJeu.getJoueurs())
		{
			if (joueur.getId() == ID)
				return joueur;
		}
		return null;
	}
}
