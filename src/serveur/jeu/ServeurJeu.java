package serveur.jeu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Map.Entry;

import exceptions.AucunePlaceDisponibleException;
import exceptions.BadPosException;
import exceptions.JeuEnCoursException;
import exceptions.NoMoneyException;
import exceptions.PathBlockException;

import models.animations.Animation;
import models.creatures.Creature;
import models.creatures.VagueDeCreatures;
import models.jeu.EcouteurDeJeu;
import models.jeu.Jeu;
import models.joueurs.Joueur;
import models.tours.IDTours;
import models.tours.Tour;
import models.tours.TourAntiAerienne;
import models.tours.TourArcher;
import models.tours.TourBalistique;
import models.tours.TourCanon;
import models.tours.TourDAir;
import models.tours.TourDeFeu;
import models.tours.TourDeGlace;
import models.tours.TourElectrique;

import reseau.Canal;
import reseau.Port;

/**
 * Cette classe contiendra le serveur de jeu sur lequel se connecteront tout les
 * cliens.
 * 
 * @author Pierre-Do
 * 
 */
public class ServeurJeu extends Observable implements ConstantesServeurJeu,
		EcouteurDeJeu, IDTours, Runnable
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
	private static final boolean DEBUG = true;

	/**
	 * Liste des clients enregistrés sur le serveur
	 */
	private HashMap<Integer, JoueurDistant> clients = new HashMap<Integer, JoueurDistant>();

	/**
	 * Lien vers le module coté serveur du jeu
	 */
	private Jeu serveurJeu;

	/**
	 * Thead de rafraichissement pour les messages
	 */
	private Thread notifieur;
	private final static long ATTENTE = 1000;

	/**
	 * Méthode MAIN : entrée dans le programme en cas de lancement en standalone
	 * du serveur
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
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
	public ServeurJeu(Jeu serveurJeu) throws IOException
	{
		// Assignation du serveur
		this.serveurJeu = serveurJeu;
		// Réglage du niveau d'affichage des messages clients
		JoueurDistant.verboseMode = 0;
		// Réservation du port d'écoute
		Port port = new Port(_port);
		port.reserver();
		// Canal d'écoute
		Canal canal;
		// Lancement de l'horloge interne
		notifieur = new Thread(this);
		notifieur.start();
		// Boucle d'attente de connections
		while (true)
		{
			// On attend qu'un joueur se présente
			log("écoute sur le port " + _port);
			// Nouveau joueur !!
			canal = new Canal(port, DEBUG);
			// Log
			log("Récéption de " + canal.getIpClient());
			// Récéption du pseudo du joueur
			String pseudo = canal.recevoirString();
			// Création du joueur
			Joueur joueur = new Joueur(pseudo);
			try
			{
				// Ajout du joueur à l'ensemble des joueurs
				serveurJeu.ajouterJoueur(joueur);
				// Extraction de l'ID du joueur
				int IDClient = joueur.getId();
				// Envoi de l'ID du joueur au client
				canal.envoyerInt(joueur.getId());
				// Log
				log("Nouveau joueur ! ID : " + IDClient);
				// Enregistrement de l'ID et du canal dans la base interne
				enregistrerClient(IDClient, canal);
			} catch (JeuEnCoursException e)
			{
				e.printStackTrace();
			} catch (AucunePlaceDisponibleException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void enregistrerClient(int IDClient, Canal canal)
	{
		// On vérifie que l'ID passé en paramêtre soit bien unique
		if (clients.containsKey(IDClient))
		{
			log("ERROR : Le client " + IDClient + " est déjà dans la partie");
			// On déconnecte le client; // FIXME
			canal.fermer();
		} else
		{
			// On inscrit le joueur à la partie
			clients.put(IDClient, new JoueurDistant(IDClient, canal, this));
			setChanged();
			notifyObservers(clients);
		}
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

	protected synchronized static void log(String msg)
	{
		System.out.print("[SERVEUR]");
		System.out.println(msg);
	}

	private synchronized Joueur repererJoueur(int ID)
	{
		for (Joueur joueur : serveurJeu.getJoueurs())
		{
			if (joueur.getId() == ID)
				return joueur;
		}
		throw new IllegalArgumentException("ID " + ID + " non trouvé");
	}

	public synchronized void lancerPartie()
	{
		// Lancement du thread de rafraichisement
		Updater updater = new Updater(clients);
		addObserver(updater);
		// Signalisation aux clients que la partie à commencé
		for (Entry<Integer, JoueurDistant> joueur : clients.entrySet())
		{
			joueur.getValue().lancerPartie();
		}
	}

	/**************** NOTIFICATIONS **************/

	@Override
	public void creatureArriveeEnZoneArrivee(Creature creature)
	{
		setChanged();
	}

	@Override
	public void creatureBlessee(Creature creature)
	{
		setChanged();
	}

	@Override
	public void creatureTuee(Creature creature)
	{
		setChanged();
	}

	@Override
	public void etoileGagnee()
	{
		setChanged();
	}

	@Override
	public void partieTerminee()
	{
		for (Entry<Integer, JoueurDistant> joueur : clients.entrySet())
			joueur.getValue().partieTerminee();
	}

	@Override
	public void vagueEntierementLancee(VagueDeCreatures vague)
	{
		// Rien
	}

	@Override
	public void animationAjoutee(Animation animation)
	{
		setChanged();
	}

	@Override
	public void animationTerminee(Animation animation)
	{
		setChanged();
	}

	@Override
	public void creatureAjoutee(Creature creature)
	{
		setChanged();
	}

	@Override
	public void joueurAjoute(Joueur joueur)
	{
		// FIXME J'fais quoi ici moi ?
	}

	@Override
	public void partieDemarree()
	{
		// Notification aux joueurs que la partie débutte
		notifyAll();
	}

	@Override
	public void tourAmelioree(Tour tour)
	{
		setChanged();
	}

	@Override
	public void tourPosee(Tour tour)
	{
		setChanged();
	}

	@Override
	public void tourVendue(Tour tour)
	{
		setChanged();
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
		notifyObservers(clients);
	}

	/************************** ACTIONS DES JOUEURS ************************/

	/**
	 * 
	 * @param typeVague
	 * @return
	 */
	public synchronized int lancerVague(int IDPlayer, int typeVague)
	{
		log("Le joueur " + IDPlayer + " désire lancer une vague de type"
				+ typeVague);
		return 0;
	}

	/**
	 * 
	 * @param IDJoueur
	 * @param typeTour
	 * @param x
	 * @param y
	 * @return
	 */
	public synchronized int poserTour(int IDJoueur, int typeTour, int x, int y)
	{
		log("Le joueur " + IDJoueur + " veut poser une tour de type "
				+ typeTour);
		// Selection de la tour cible
		Tour tour = null;
		switch (typeTour)
		{
		// Tour d'archer
		case TOUR_ARCHER: // FIXME
			tour = new TourArcher();
			break;
		// Tour Anti Aerienne
		case TOUR_AA:
			tour = new TourAntiAerienne();
			break;
		// Tour balistique
		case TOUR_BALISTIQUE:
			tour = new TourBalistique();
			break;
		// Tour canon
		case TOUR_CANON:
			tour = new TourCanon();
			break;
		// Tour d'air
		case TOUR_D_AIR:
			tour = new TourDAir();
			break;
		// Tour de feu
		case TOUR_DE_FEU:
			tour = new TourDeFeu();
			break;
		// Tour de glace (à la fraise)
		case TOUR_DE_GLACE:
			tour = new TourDeGlace();
			break;
		// Bobine de Telsa
		case TOUR_ELECTRIQUE:
			tour = new TourElectrique();
			break;
		default:
			log("Tour " + typeTour + " inconnue.");
			return ERROR;
		}
		// Assignation des paramêtres
		tour.x = x;
		tour.y = y;
		// Assignation du propriétaire
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
		} catch (Exception e)
		{
			e.printStackTrace();
			return ERREUR;
		}
		return OK;
	}

	/**
	 * 
	 * @param IDPlayer
	 * @param nouvelEtat
	 * @return
	 */
	public synchronized int changementEtatJoueur(int IDPlayer, int nouvelEtat)
	{
		log("Le joueur " + IDPlayer + " désire passer en état " + nouvelEtat);
		return 0;
	}

	/**
	 * 
	 * @param nouvelEtatPartie
	 * @return
	 */
	public synchronized int changementEtatPartie(int IDPlayer, int nouvelEtat)
	{
		log("Le joueur " + IDPlayer + " désire passer la partie en état "
				+ nouvelEtat);

		switch (nouvelEtat)
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

	/**
	 * 
	 * @param tourCible
	 * @return
	 */
	public synchronized int ameliorerTour(int IDPlayer, int tourCible)
	{
		log("Le joueur " + IDPlayer + " désire améliorer la tour" + tourCible);
		return 0;
	}

	/**
	 * 
	 * @param tourCibleDel
	 * @return
	 */
	public synchronized int supprimerTour(int IDPlayer, int tourCible)
	{
		log("Le joueur " + IDPlayer + " désire supprimer la tour" + tourCible);
		return 0;
	}

	/**
	 * Envoi un message texte à l'ensemble des clients connectés.
	 * 
	 * @param IDFrom
	 *            L'ID de l'expéditeur.
	 * @param message
	 *            Le message à envoyer.
	 */
	public synchronized void direATous(int IDPlayer, String message)
	{
		log("Le joueur " + IDPlayer + " dit : " + message);
		for (Entry<Integer, JoueurDistant> joueur : clients.entrySet())
			joueur.getValue().envoyerMessageTexte(IDPlayer, message);
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
	public synchronized void direAuClient(int IDPlayer, int IDTo, String message)
	{
		log("Le joueur " + IDPlayer + " désire envoyer un message à " + IDTo
				+ "(" + message + ")");
		clients.get(IDTo).envoyerMessageTexte(IDPlayer, message);
	}

	public synchronized ArrayList<Creature> getCreatures()
	{
		return new ArrayList<Creature>(serveurJeu.getCreatures());
	}

	public synchronized ArrayList<Tour> getTours()
	{
		return new ArrayList<Tour>(serveurJeu.getTours());
	}

	public synchronized ArrayList<Animation> getAnimations()
	{
		return new ArrayList<Animation>();
	}

	@Override
	public void run()
	{
		while (true)
		{
			// Met à jour
			notifyObservers();
			// Attente un nombre donné de temps
			try
			{
				synchronized (this)
				{					
					wait(ATTENTE);
				}
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

	}
}
