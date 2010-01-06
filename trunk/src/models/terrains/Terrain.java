package models.terrains;

import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.*;
import models.animations.Animation;
import models.creatures.*;
import models.maillage.*;
import models.outils.GestionnaireSons;
import models.outils.Son;
import models.tours.Tour;

/**
 * Classe de gestion d'un terrain de jeu.
 * <p>
 * Cette classe contient tous les elements contenu sur le terrain, c'est a dire
 * :<br>
 * - Les tours
 * <p>
 * - Les creatures : la liste des creatures et les vagues de creatures
 * <p>
 * - Les maillages : Il y a deux types de maillages, un pour les creatures
 * terrestres ou l'emplacement des tours a une influence sur le chemin des
 * creatures et un autre pour les creatures aerienne ou l'emplacement des tours
 * n'a aucune influence sur le chemin des creatures (elles passent par dessus
 * les tours)
 * <p>
 * - Les murs propres au terrain : simples zones inaccessibles
 * <p>
 * - Les zones de depart et arrivee des creatures<br>
 * <p>
 * - L'image de fond du terrain
 * <p>
 * <p>
 * Plusieurs methodes sont mises a disposition par cette classe pour gerer les
 * elements qu'elle contient.
 * <p>
 * De plus, cette classe est abstraite, elle ne peut pas etre instanciee en tant
 * que telle mais doit etre heritee.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Tour
 * @see Creature
 * @see Maillage
 */
public abstract class Terrain
{

	/**
	 * nom de la zone de jeu
	 */
	private final String NOM;

	/**
	 * nombre de vies au debut de la partie
	 */
	private final int NB_VIES_INITIALES;

	/**
	 * nombre de pieces d'or au debut de la partie
	 */
	private final int NB_PIECES_OR_INITIALES;

	/**
	 * Taille du terrain
	 */
	private final int LARGEUR, // en pixels
			HAUTEUR; // en pixels

	/**
	 * precision du maillage, espace entre deux noeuds
	 */
	private final int PRECISION_MAILLAGE = 10; // pixels

	/**
	 * Le maillage permet de definir les chemins des creatures sur le terrain.
	 * Ici, pour les creatures terriennes avec prise en compte de la position
	 * des tours.
	 * 
	 * @see Maillage
	 */
	private final Maillage MAILLAGE_TERRESTRE;

	/**
	 * Les creatures volantes n'ont pas besoins d'une maillage mais uniquement
	 * du chemin le plus court entre la zone de depart et la zone d'arrivee
	 */
	ArrayList<Point> cheminAerien;

	/**
	 * Les creatures sont implentees aleatoirement dans la zone de depart
	 * lorsqu'une vague des creatures est sollicitee par le joueur.
	 */
	private final Rectangle ZONE_DEPART;

	/**
	 * Les creatures doivent ce rendre dans la zone d'arrivee en utilisant le
	 * chemin le plus cours fourni par le maillage.
	 */
	private final Rectangle ZONE_ARRIVEE;

	/**
	 * Image de fond du terrain. <br>
	 * Note : les murs du terrain sont normalement lies a cette image
	 */
	private final Image IMAGE_DE_FOND;

	/**
	 * Les tours sont posees sur le terrain et permettent de tuer les creatures.
	 * 
	 * @see Tour
	 */
	private Vector<Tour> tours;

	/**
	 * Les creatures de deplacent sur le terrain d'une zone de depart a une zone
	 * d'arrivee.
	 * 
	 * @see Creature
	 */
	private Vector<Creature> creatures;

	/**
	 * Les murs sont utilises pour empecher le joueur de construire des tours
	 * dans certaines zones. Les creatures ne peuvent egalement pas si rendre.
	 * En fait, les murs reflettent les zones de la carte non accessible. Les
	 * murs ne sont pas affiches. Ils sont simplement utilises pour les controls
	 * d'acces de la carte.
	 */
	protected ArrayList<Rectangle> murs = new ArrayList<Rectangle>();

	/**
	 * Liste des animations visible sur le terrain
	 */
	private Vector<Animation> animations = new Vector<Animation>();

	/**
	 * Gestion des vagues de creatures. C'est le joueur que decident le moment
	 * ou il veut lancer une vague de creatures. Une fois que toutes les vagues
	 * de creatures ont ete detruites, le jeu est considere comme termine.
	 */
	protected int indiceVagueCourante;

	/**
	 * musique d'ambiance du terrain
	 */
	protected File fichierMusiqueDAmbiance;

	
	/**
	 * Constructeur du terrain.
	 * 
	 * @param largeur
	 *            la largeur en pixels du terrain (utilisé pour le maillage)
	 * @param hauteur
	 *            la hauteur en pixels du terrain (utilisé pour le maillage)
	 * @param nbPiecesOrInitiales
	 *            le nom de piece d'or en debut de partie
	 * @param positionMaillageX
	 *            position du point 0 du maillage
	 * @param positionMaillageY
	 *            position du point 0 du maillage
	 * @param largeurMaillage
	 *            largeur du maillage en pixel
	 * @param hauteurMaillage
	 *            hauteur du maillage en pixel
	 * @param imageDeFond
	 *            le chemin jusqu'a l'image de fond
	 * @param nom
	 *            nom de la zone de jeu
	 * @param zoneDepart
	 *            la zone de depart des creatures
	 * @param zoneArrivee
	 *            la zone d'arrivee des creatures
	 */
	public Terrain(int largeur, int hauteur, int nbPiecesOrInitiales,
			int nbViesInitiales, int positionMaillageX, int positionMaillageY,
			int largeurMaillage, int hauteurMaillage, Image imageDeFond,
			String nom, Rectangle zoneDepart, Rectangle zoneArrivee)
	{
		LARGEUR = largeur;
		HAUTEUR = hauteur;
		ZONE_DEPART = zoneDepart;
		ZONE_ARRIVEE = zoneArrivee;
		NB_PIECES_OR_INITIALES = nbPiecesOrInitiales;
		NB_VIES_INITIALES = nbViesInitiales;
		IMAGE_DE_FOND = imageDeFond;
		NOM = nom;

		// creation des deux maillages
		MAILLAGE_TERRESTRE = new Maillage(largeurMaillage, hauteurMaillage,
				PRECISION_MAILLAGE, positionMaillageX, positionMaillageY);

		// creation des collections
		tours = new Vector<Tour>();
		creatures = new Vector<Creature>();
	}

	/**
	 * Methode qui permet de recuperer la vague de creatures suivantes
	 * 
	 * @return la vague de creatures suivante
	 */
	abstract VagueDeCreatures getVagueDeCreaturesSuivante();

	// ------------------------------
	// -- GETTER / SETTER BASIQUES --
	// ------------------------------

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

	/**
	 * Permet de recuperer le nombre de pieces initial
	 * 
	 * @return le nombre de pieces initiales
	 */
	public int getNbPiecesOrInitiales()
	{
		return NB_PIECES_OR_INITIALES;
	}

	/**
	 * Permet de recuperer le nombre de vie du joueur en debut de partie
	 * 
	 * @return le nombre de vie du joueur en debut de partie
	 */
	public int getNbViesInitiales()
	{
		return NB_VIES_INITIALES;
	}

	/**
	 * Permet de recuperer le numero de la vague courante
	 * 
	 * @return le numero de la vague courante
	 */
	public int getNumVagueCourante()
	{
		return indiceVagueCourante;
	}

	/**
	 * Permet de recuperer le nom du terrain
	 * 
	 * @return le nom du terrain
	 */
	public String getNom()
	{
		return NOM;
	}

	/**
	 * Permet de recuperer la description vague suivante
	 * 
	 * @return la description de la vague suivante
	 */
	public String getDescriptionVagueSuivante()
	{
		String descriptionVague = getVagueDeCreaturesSuivante()
				.getDescription();

		// s'il y a une description, on la retourne
		if (!descriptionVague.isEmpty())
			return descriptionVague;

		// sinon on genere une description
		return getVagueDeCreaturesSuivante().toString();
	}

	// ----------------------
	// -- GESTION DES MURS --
	// ----------------------

	/**
	 * Permet d'ajouter un mur sur le terrain.
	 * 
	 * @param mur
	 *            le mur ajouter
	 */
	public void ajouterMur(Rectangle mur)
	{
		// c'est bien un mur valide ?
		if (mur == null)
			throw new IllegalArgumentException("Mur nul");

		// desactive la zone dans le maillage qui correspond au mur
		MAILLAGE_TERRESTRE.desactiverZone(mur);
		// MAILLAGE_AERIEN.desactiverZone(mur);

		// ajout du mur
		murs.add(mur);

		/*
		 * Recalculation du chemin des créatures volantes
		 */
		try
		{
			cheminAerien = MAILLAGE_TERRESTRE.plusCourtChemin(
			        (int)ZONE_DEPART.getCenterX(),
			        (int)ZONE_DEPART.getCenterY(), 
			        (int)ZONE_ARRIVEE.getCenterX(), 
			        (int)ZONE_ARRIVEE.getCenterY());
			
		} catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PathNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ---------------------------
	// -- GESTION DES CREATURES --
	// ---------------------------

	/**
	 * Permet de recuperer la liste des creatures sur le terrain.
	 * 
	 * @return la liste des creatures
	 */
	public Vector<Creature> getCreatures()
	{
		return creatures;
	}

	/**
	 * Permet d'ajouter une creature.
	 * 
	 * @param creature
	 *            la creature a ajouter
	 */
	synchronized public void ajouterCreature(Creature creature)
	{
		if (creature == null)
			throw new IllegalArgumentException("Creature nulle");

		creatures.add(creature);
	}

	/**
	 * Permet de supprimer une creature
	 * 
	 * @param creature
	 *            la creature a supprimer
	 */
	synchronized public void supprimerCreature(Creature creature)
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
	public Vector<Tour> getTours()
	{
		return tours;
	}

	/**
	 * Permet d'ajouter une tour sur le terrain.
	 * 
	 * @param tour
	 *            la tour a ajouter
	 * @throws Exception
	 *             si la zone n'est pas accessible (occupee)
	 * @throws Exception
	 *             si la tour bloque empeche tous chemins
	 */
	public void ajouterTour(Tour tour) throws Exception
	{
		// c'est bien une tour valide ?
		if (tour == null)
			throw new IllegalArgumentException("Tour nulle");

		// si elle peut pas etre posee
		if (!laTourPeutEtrePosee(tour))
			throw new Exception("Pose impossible : Zone non accessible");

		// si elle bloque le chemin de A vers B
		if (laTourBloqueraLeChemin(tour))
			throw new Exception("Pose impossible : Chemin bloqué");

		// desactive la zone dans le maillage qui correspond a la tour
		desactiverZone(tour, true);

		// ajout de la tour
		tours.add(tour);
		tour.setTerrain(this);
	}

	/**
	 * Permet de supprimer une tour du terrain.
	 * 
	 * @param tour
	 *            la tour a supprimer
	 */
	public void supprimerTour(Tour tour)
	{
		// c'est bien une tour valide ?
		if (tour == null)
			throw new IllegalArgumentException("Tour nulle");

		// arret du thread
		tour.arreter();

		// suppression de la tour
		tours.remove(tour);

		// reactive la zone dans le maillage qui correspond a la tour
		activerZone(tour, true);
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
		if (tour == null)
			return false;

		// il n'y a pas deja une tour
		synchronized (tours)
		{
			for (Tour tourCourante : tours)
				if (tour.intersects(tourCourante))
					return false;
		}

		// il n'y a pas un mur
		synchronized (murs)
		{
			for (Rectangle mur : murs)
				if (tour.intersects(mur))
					return false;
		}

		// il n'y a pas deja une creature
		synchronized (creatures)
		{
			for (Rectangle creature : creatures)
				if (tour.intersects(creature))
					return false;
		}

		// il n'y a pas la zone de depart ou d'arrivee
		if (tour.intersects(ZONE_DEPART) || tour.intersects(ZONE_ARRIVEE))
			return false;

		// rien empeche la tour d'etre posee
		return true;
	}

	/**
	 * Permet de savoir si apres la pose d'une tour en parametre le chemin
	 * deviendra bloque ?
	 * 
	 * @param tour
	 *            la tour a testee si elle bloquera le chemin
	 * @return true si elle le bloquera lors de la pose, false sinon
	 */
	public boolean laTourBloqueraLeChemin(Tour tour)
	{
		// c'est une tour valide ?
		if (tour == null)
			return false;

	 
		
		
		// si l'on construit la tour, il existe toujours un chemin
		desactiverZone(tour, false);
		
		try
		{
    		// calcul du chemin et attente une exception 
		    // PathNotFoundException s'il y a un probleme
		    getCheminLePlusCourt((int) ZONE_DEPART.getCenterX(),
    				(int) ZONE_DEPART.getCenterY(),
    				(int) ZONE_ARRIVEE.getCenterX(), (int) ZONE_ARRIVEE
    						.getCenterY(), Creature.TYPE_TERRIENNE);
    		
			// il existe un chemin, donc elle ne bloque pas.
			activerZone(tour, false); // on reactive la zone
			return false;
    	
		}
		catch(PathNotFoundException e)
	    {
		    // il n'existe pas de chemin, donc elle bloque le chemin.
	        activerZone(tour, false); // on reactive la zone
	        return true;
	    }
	}

	// -------------------------
	// -- GESTION DU MAILLAGE --
	// -------------------------

	/**
	 * Permet d'activer ou reactiver un zone rectangulaire du maillage.
	 * 
	 * @param zone
	 *            la zone rectangulaire a activer
	 * @param miseAJourDesCheminsDesCreatures
	 *            faut-il mettre a jour les chemins des creatures ?
	 */
	private void activerZone(Rectangle zone,
			boolean miseAJourDesCheminsDesCreatures)
	{
		// activation de la zone
		MAILLAGE_TERRESTRE.activerZone(zone);

		// mise a jour des chemins si necessaire
		if (miseAJourDesCheminsDesCreatures)
			miseAJourDesCheminsDesCreatures();
	}

	/**
	 * Permet de desactiver un zone rectangulaire du maillage.
	 * 
	 * @param zone
	 *            la zone rectangulaire a desactiver
	 * @param miseAJourDesCheminsDesCreatures
	 *            faut-il mettre a jour les chemins des creatures ?
	 */
	private void desactiverZone(Rectangle zone,
			boolean miseAJourDesCheminsDesCreatures)
	{
		// desactivation de la zone
		MAILLAGE_TERRESTRE.desactiverZone(zone);

		// mise a jour des chemins si necessaire
		if (miseAJourDesCheminsDesCreatures)
			miseAJourDesCheminsDesCreatures();
	}

	/**
	 * Permet de mettre a jour les chemins des creatures lors de la modification
	 * du maillage. 
	 */
	synchronized private void miseAJourDesCheminsDesCreatures()
	{
		// Il ne doit pas y avoir de modifications sur la collection
		// durant le parcours.
		synchronized (creatures)
		{
			// mise a jour de tous les chemins
			for (Creature creature : creatures)
			{
				// les tours n'affecte que le chemin des creatures terriennes
				if (creature.getType() == Creature.TYPE_TERRIENNE)
                    try
                    {
                        creature.setChemin(getCheminLePlusCourt((int) creature
                        		.getCenterX(), (int) creature.getCenterY(),
                        		(int) ZONE_ARRIVEE.getCenterX(), (int) ZONE_ARRIVEE
                        				.getCenterY(), creature.getType()));
                    } catch (IllegalArgumentException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (PathNotFoundException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
			}
		}
	}

	/**
	 * Permet de lancer la vague de creatures suivantes
	 * 
	 * @param edc
	 *            ecouteur de creature fourni a chacune des creatures
	 */
	public void lancerVagueSuivante(EcouteurDeVague edv, EcouteurDeCreature edc)
	{
		// lancement de la vague
		getVagueDeCreaturesSuivante().lancerVague(this, edv, edc);
		indiceVagueCourante++;
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
	 *         <b>null si aucun chemin ne relie les deux points</b>.
	 * @throws PathNotFoundException 
	 * @throws IllegalArgumentException 
	 * @see Maillage
	 */
	public ArrayList<Point> getCheminLePlusCourt(int xDepart, int yDepart,
			int xArrivee, int yArrivee, int typeCreature) throws IllegalArgumentException, PathNotFoundException
	{
	
		if (typeCreature == Creature.TYPE_TERRIENNE)
			return MAILLAGE_TERRESTRE.plusCourtChemin(xDepart, yDepart,
					xArrivee, yArrivee);

		return cheminAerien;
	}

	/**
	 * Permet de recuperer la liste des arcs actifs du maillage terrestre.
	 * 
	 * @return une collection de java.awt.geom.Line2D representant les arcs
	 *         actifs du maillage
	 */
	public ArrayList<Line2D> getArcsActifs()
	{
		return MAILLAGE_TERRESTRE.getArcs();
	}

	/**
	 * Permet de recuperer les noeuds du maillage terrestre
	 * 
	 * @return Une collection de noeuds
	 */
	public ArrayList<Noeud> getNoeuds()
	{
		return MAILLAGE_TERRESTRE.getNoeuds();
	}

	// -------------
	// -- MUSIQUE --
	// -------------

	/**
	 * Permet de demarrer la lecture de la musique d'ambiance
	 */
	public void demarrerMusiqueDAmbiance()
	{
		if (fichierMusiqueDAmbiance != null)
		{
			Son musiqueDAmbiance = new Son(fichierMusiqueDAmbiance);
		    
		    GestionnaireSons.ajouterSon(musiqueDAmbiance);
		    musiqueDAmbiance.lire(0); // lecture infinie
		}
	}

	/**
	 * Permet d'arreter la lecture de la musique d'ambiance
	 */
	public void arreterMusiqueDAmbiance()
	{
		if (fichierMusiqueDAmbiance != null)
		    GestionnaireSons.arreterTousLesSons(fichierMusiqueDAmbiance);
	}

	// ----------------
	// -- ANIMATIONS --
	// ----------------
	/**
	 * Permet d'ajouter une animation
	 * 
	 * @param animation
	 *            l'aniatio a ajouter
	 */
	public void ajouterAnimation(Animation animation)
	{
		animations.add(animation);
	}

	/**
	 * Permet de recuperer les animations
	 * 
	 * @return la collection d'animations
	 */
	public Vector<Animation> getAnimations()
	{
		return animations;
	}

	/**
	 * Permet de generer une vague en fonction de son indice de vague courante
	 * 
	 * Cette methode permet d'eviter de gerer les vagues pour chaque terrain.
	 * Mias rien n'empeche au developpeur de terrain de gerer lui-meme les
	 * vagues qu'il veut envoye.
	 * 
	 * @return la vague generee
	 */
	public VagueDeCreatures genererVagueStandard()
	{
		int noVague = indiceVagueCourante + 1;
		int uniteVague = noVague % 10;

		// 5 normales
		if (uniteVague == 1)
			return new VagueDeCreatures(5, new Smiley(fSante(noVague), 6, 20),
					1000, false);
		// 10 normales
		else if (uniteVague == 2)
			return new VagueDeCreatures(10, new Pokey(fSante(noVague), 6, 20),
					1000, false);
		// 10 volantes
		else if (uniteVague == 3)
			return new VagueDeCreatures(10, new Boo(fSante(noVague), 10, 20),
					1000, false);
		// 10 resistantes
		else if (uniteVague == 4)
			return new VagueDeCreatures(10, new CarapaceKoopa((int) (fSante(noVague)*1.5), 8,
					10), 2000, false);
		// 10 rapides
		else if (uniteVague == 5)
			return new VagueDeCreatures(10, new Smiley((int) (fSante(noVague) * 0.8), 8, 30),
					500, false);
		// 20 normales
		else if (uniteVague == 6)
			return new VagueDeCreatures(10, new Smiley(fSante(noVague), 6, 20),
					1000, false);
		// 15 resistantes
		else if (uniteVague == 7)
			return new VagueDeCreatures(15, new Thwomp((int) (fSante(noVague)*1.5), 8, 10),
					2000, false);
		// 10 volantes
		else if (uniteVague == 8)
			return new VagueDeCreatures(10, new Boo(fSante(noVague), 10, 20),
					1000, false);
		// 5 pre-boss
		else if (uniteVague == 9)
			return new VagueDeCreatures(3, new PetiteFlame(fSante(noVague) * 5,
					noVague * 2, 10), 2000, false);
		// boss
		else
			return new VagueDeCreatures(1, new GrandeFlame(fSante(noVague) * 50,
					noVague * 5, 10), 2000, false);
	}

    /**
     * Permet de calculer la sante d'une creer de facon exponentielle
     * pour rendre le jeu de plus en plus dur.
     * 
     * @param noVague le numero de la vague
     * 
     * @return la valeur de la sante
     */
    private int fSante(int noVague)
    {
        return (int) (1.0/50.0 * noVague * noVague * noVague * noVague 
                      + 0.25 * noVague + 100);
    }
	
	/**
	 * Permet de stope tous les threads des elements du terrain
	 */
    public void arreterTout()
    {
        // arret de toutes les tours
        synchronized (tours)
        {
            for(Tour tour : tours)
                tour.arreter();
            
            tours.clear();
        }
        
        // arret de toutes les creatures
        synchronized (creatures)
        {
            for(Creature creature : creatures)
                creature.arreter();
            
            creatures.clear();
        }
        
        // arret de toutes les creatures
        synchronized (animations)
        {
            for(Animation animation : animations)
                animation.arreter();
            
            animations.clear();
        }  
    }  
}