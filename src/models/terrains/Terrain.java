package models.terrains;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import models.creatures.Creature;
import models.maillage.Maillage;
import models.maillage.Noeud;
import models.maillage.PathNotFoundException;
import models.tours.Tour;

/**
 * Classe de gestion d'un terrain du jeu.
 * 
 * Cette classe est abstraite, elle ne peut pas etre instanciee en tant que
 * telle mais doit etre heritee.
 * <p>
 * Cette classe contient tous les elements contenu sur le terrain, c'est a dire
 * :<br>
 * - Les tours<br>
 * - Les creatures<br>
 * - Le maillage<br>
 * - Les murs propres au terrain (simples zones inaccessibles)<br>
 * - Les zones de depart et arrivee des creatures<br>
 * - L'image de fond du terrain
 * <p>
 * Plusieurs methodes sont mises a disposition par cette classe pour gerer les
 * elements qu'elle contient.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Maillage
 */
public abstract class Terrain
{
	/**
	 * Les tours sont posees sur le terrain et permettent de tuer les creatures.
	 * 
	 * @see Tour
	 */
	private ArrayList<Tour> tours;

	/**
	 * Les creatures de deplacent sur le terrain d'une zone de depart a une zone
	 * d'arrivee.
	 * 
	 * @see Creature
	 */
	private ArrayList<Creature> creatures;

	/**
	 * Les murs sont utilises pour empecher le joueur de construire des tours
	 * dans certaines zones. Les creatures ne peuvent egalement pas si rendre.
	 * En fait, les murs reflettent les zones de la carte non accessible. Les
	 * murs ne sont pas affiches. Ils sont simplement utilises pour les controle
	 * d'acces de la carte.
	 */
	protected ArrayList<Rectangle> murs = new ArrayList<Rectangle>();

	/**
	 * Taille du terrain
	 */
	private final int LARGEUR, // en pixels
			HAUTEUR; // en pixels

	/**
	 * Le maillage permet de definire les chemins des cratures sur le terrain.
	 * 
	 * @see Maillage
	 */
	private final int PRECISION_MAILLAGE = 10; // espace entre deux noeuds en
												// pixels
	private final Maillage maillage;

	/**
	 * Zones de depart et d'arrivee des creatures. Les creatures sont implentees
	 * aleatoirement dans la zone de départ lorsqu'une vague des creatures est
	 * sollicitee par le joueur. Ces dernieres doivent alors ce rendre dans la
	 * zone d'arrivee en utilisant le chemin le plus cour fourni par le
	 * maillage.
	 */
	private final Rectangle ZONE_DEPART;
	private final Rectangle ZONE_ARRIVEE;

	/**
	 * Image de fond du terrain. <br/>
	 * Note : les murs du terrain sont directement lies a cette image
	 */
	private final Image IMAGE_DE_FOND;

	/**
	 * Constructeur du terrain.
	 * 
	 * @param largeur
	 *            la largeur en pixels du terrain (utilisé pour le maillage)
	 * @param hauteur
	 *            la hauteur en pixels du terrain (utilisé pour le maillage)
	 * @param imageDeFond
	 *            le chemin jusqu'a l'image de fond
	 * @param zoneDepart
	 *            la zone de depart des creatures
	 * @param zoneArrivee
	 *            la zone d'arrivee des creatures
	 */
	public Terrain(int largeur, int hauteur, String imageDeFond,
			Rectangle zoneDepart, Rectangle zoneArrivee)
	{
		LARGEUR = largeur;
		HAUTEUR = hauteur;
		maillage = new Maillage(largeur, hauteur, PRECISION_MAILLAGE);
		tours = new ArrayList<Tour>();
		creatures = new ArrayList<Creature>();
		ZONE_DEPART = zoneDepart;
		ZONE_ARRIVEE = zoneArrivee;

		// chargement de l'image
		if (imageDeFond != null)
			IMAGE_DE_FOND = Toolkit.getDefaultToolkit().getImage(imageDeFond);
		else
			IMAGE_DE_FOND = null;
	}

	// ----------------------------------------------------------
	// -- GETTER / SETTER BASIQUES --
	// ----------------------------------------------------------

	/**
	 * Permet de recuperer la largeur du terrain.
	 * 
	 * @return la largeur du terrain
	 */
	public int getLargeur()
	{
		return LARGEUR;
	}

	/**
	 * Permet de recuperer la hauteur du terrain.
	 * 
	 * @return la hauteur du terrain
	 */
	public int getHauteur()
	{
		return HAUTEUR;
	}

	/**
	 * Permet de recuperer l'image de fond du terrain.
	 * 
	 * @return l'image de fond du terrain
	 */
	public Image getImageDeFond()
	{
		return IMAGE_DE_FOND;
	}

	/**
	 * Permet de recuperer la zone de depart des creatures.
	 * 
	 * @return la zone de depart des creatures
	 */
	public Rectangle getZoneDepart()
	{
		return ZONE_DEPART;
	}

	/**
	 * Permet de recuperer la zone d'arrivee des creatures.
	 * 
	 * @return la zone d'arrivee des creatures
	 */
	public Rectangle getZoneArrivee()
	{
		return ZONE_ARRIVEE;
	}

	// ----------------------------------------------------------
	// -- GESTION DES MURS --
	// ----------------------------------------------------------

	/**
	 * Permet d'ajouter un mur sur le terrain.
	 * 
	 * @param mur
	 *            le mur ajouter
	 */
	public void ajouterMur(Rectangle mur)
	{
		// c'est bien un mur valide ?
		if (mur != null)
		{
			// desactive la zone dans le maillage qui correspond au mur
			maillage.desactiverZone(mur);

			// ajout du mur
			murs.add(mur);
		}
	}

	// ----------------------------------------------------------
	// -- GESTION DES CREATURES --
	// ----------------------------------------------------------

	/**
	 * Permet de recuperer la liste des creatures sur le terrain.
	 * 
	 * @return la liste des creatures
	 */
	public ArrayList<Creature> getCreatures()
	{
		return creatures;
	}

	/**
	 * Permet d'ajouter une creature.
	 * 
	 * @param creature
	 *            la creature a ajouter
	 */
	public void ajouterCreature(Creature creature)
	{
		if (creature != null)
			creatures.add(creature);
	}

	/**
	 * Permet de supprimer une creature
	 * 
	 * @param creature
	 *            la creature a supprimer
	 */
	public void supprimerCreature(Creature creature)
	{
		if (creature != null)
			creatures.remove(creature);
	}

	// ----------------------------------------------------------
	// -- GESTION DES TOURS --
	// ----------------------------------------------------------

	/**
	 * Permet de recuperer la liste des tours.
	 * 
	 * @return la liste des tours
	 */
	public ArrayList<Tour> getTours()
	{
		return tours;
	}

	/**
	 * Permet d'ajouter une tour sur le terrain.
	 * 
	 * @param tour
	 *            la tour a ajouter
	 */
	public boolean ajouterTour(Tour tour)
	{
		// c'est bien une tour valide ?
		if (tour != null)
		{
			if (!laTourPeutEtrePosee(tour))
				return false;

			// desactive la zone dans le maillage qui correspond a la tour
			desactiverZone(tour);

			// ajout de la tour
			tours.add(tour);
			tour.setTerrain(this);

			return true;
		}
		return false;
	}

	/**
	 * Permet de supprimer une tour du terrain.
	 * 
	 * @param tour
	 *            la tour a supprimer
	 */
	public void supprimerTour(Tour tour)
	{
		// c'est bien un tour valide ?
		if (tour != null)
		{
			// arret du thread
			tour.arreter();

			// suppression de la tour
			tours.remove(tour);

			// reactive la zone dans le maillage qui correspond a la tour
			activerZone(tour);
		}
	}

	/**
	 * Permet de savoir si une tour peut etre posee a un certain endroit sur le
	 * terrain.
	 * 
	 * @param tour
	 *            la tour a posee
	 * @return true si la tour peut etre posee, false sinon
	 */
	public boolean laTourPeutEtrePosee(Tour tour)
	{
		// c'est une tour valide ?
		if (tour != null)
		{
			// il n'y a pas deja une tour
			for (Tour tourCourante : tours)
				if (tour.intersects(tourCourante))
					return false;

			// il n'y a pas un mur
			for (Rectangle mur : murs)
				if (tour.intersects(mur))
					return false;

			// il n'y a pas deja une creature
			for (Rectangle creature : creatures)
				if (tour.intersects(creature))
					return false;

			// rien empeche la tour d'etre posee
			return true;
		}

		return false;
	}

	// ----------------------------------------------------------
	// -- GESTION DU MAILLAGE --
	// ----------------------------------------------------------

	/**
	 * Permet d'activer ou reactiver un zone rectangulaire du maillage.
	 * 
	 * @param zone
	 *            la zone rectangulaire a activer
	 */
	private void activerZone(Rectangle zone)
	{
		maillage.activerZone(zone);

		miseAJourDesCheminsDesCreatures();
	}

	/**
	 * Permet de desactiver un zone rectangulaire du maillage.
	 * 
	 * @param zone
	 *            la zone rectangulaire a desactiver
	 */
	private void desactiverZone(Rectangle zone)
	{
		maillage.desactiverZone(zone);

		miseAJourDesCheminsDesCreatures();
	}

	/**
	 * Permet de mettre a jour les chemins des creatures lors de la modification
	 * du maillage.
	 */
	private void miseAJourDesCheminsDesCreatures()
	{
		synchronized (this)
		{
			// mise a jour de tous les chemins
			for (Creature creature : creatures)
				creature.setChemin(getCheminLePlusCourt(creature.x, creature.y,
						(int) ZONE_ARRIVEE.getCenterX(), (int) ZONE_ARRIVEE
								.getCenterY()));
		}
	}

	/**
	 * Permet de recuperer le chemin le plus court entre deux points sur le
	 * terrain.
	 * 
	 * Cette methode fait appel au maillage pour decouvrir ce chemin
	 * 
	 * @param xDepart
	 *            la position x du point de depart
	 * @param yDepart
	 *            la position y du point de depart
	 * @param xArrivee
	 *            la position x du point d'arrivee
	 * @param yArrivee
	 *            la position y du point d'arrivee
	 * @return le chemin sous la forme d'un ArrayList de java.awt.Point ou
	 *         <b>null si aucun chemin ne reli les deux points</b>.
	 * @see Maillage
	 */
	public ArrayList<Point> getCheminLePlusCourt(int xDepart, int yDepart,
			int xArrivee, int yArrivee)
	{
		try
		{
			return maillage.plusCourtChemin(xDepart, yDepart, xArrivee,
					yArrivee);
		}
		// les points sont hors du maillage
		catch (IllegalArgumentException e)
		{
			return null;
		}
		// aucun chemin ne reli les deux points
		catch (PathNotFoundException e)
		{
			return null;
		}
	}

	/**
	 * Permet de recuperer la liste des arcs actifs du maillage.
	 * 
	 * @return une collection de java.awt.geom.Line2D representant les arcs
	 *         actifs du maillage
	 */
	public ArrayList<Line2D> getArcsActifs()
	{
		return maillage.getArcsActifs();
	}
}