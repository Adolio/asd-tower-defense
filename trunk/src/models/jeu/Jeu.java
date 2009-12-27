package models.jeu;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Vector;

import models.creatures.Creature;
import models.creatures.EcouteurDeCreature;
import models.maillage.Noeud;
import models.terrains.Terrain;
import models.tours.Tour;

/**
 * Classe de gestion du jeu.
 * 
 * Cette classe contient les objets important de la partie comme :
 * <br>
 * - Le terrain qui contient les tours et les creatures
 * <br>
 * - Les informations du jeu (score, pieces d'or, etc.)
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Tour
 */
public class Jeu
{
	private static final String VERSION = "v1.0 | janvier 2010";

    /**
	 * Score courant du joueur
	 */
	private int score;
	
	/**
	 * Vies restantes du joueur. 
	 * <br>
	 * Note : Lorsque un ennemi atteint la zone d'arrive, le nombre de vies est
	 * decremente.
	 */
	private int viesRestantes 	= 20;
	
	/**
	 * Nombre de pieces d'or du joueur.
	 * <br>
	 * Cette variable fluctue en fonction des creatures tuees et de 
	 * l'achat et vente des tours.
	 */
	private int nbPiecesOr;
	

	/**
	 * Le terrain de jeu que contient tous les elements principaux :
	 * - Les tours
	 * - Les creatures
	 * - Le maillage
	 */
	private Terrain terrain;
	
	/**
	 * Constructeur du jeu.
	 * 
	 * @param terrain le terrain dans lequel on va jouer
	 */
	public Jeu(Terrain terrain)
	{
		this.terrain = terrain;
		nbPiecesOr = terrain.getNbPiecesOrInitial();
	}
	
	/**
	 * Permet de tenter de poser une tour sur le terrain de jeu.
	 * 
	 * @param tour la tour a poser
	 * @return true si operation realisee avec succes, false sinon 
	 * @throws Exception si pas assez d'argent
	 */
	public void poserTour(Tour tour) throws Exception
	{
		// suffisemment d'argent ?
		if(!laTourPeutEtreAchetee(tour))
		    throw new Exception("Pose impossible : Pas assez d'argent");
		
	    // ajout de la tour dans le terrain de jeu
		terrain.ajouterTour(tour);
		
		// la tour est mise en jeu
		tour.demarrer();
		
		// debit des pieces d'or
		nbPiecesOr -= tour.getPrixAchat();
	}

	/**
	 * permet de savoir si une tour peut etre achetee.
	 * @param tour la tour a achetee
	 * @return true si le joueur a assez de pieces d'or, false sinon
	 */
	public boolean laTourPeutEtreAchetee(Tour tour)
	{
	    if(tour != null)
			return (nbPiecesOr - tour.getPrixAchat()) >= 0;
		
		return false;
	}

	/**
	 * Permet de savoir si la tour peut etre posee.
	 * @param tour la tour a posee
	 * @return true si c'est possible et false sinon
	 */
	public boolean laTourPeutEtrePosee(Tour tour)
	{
		// la tour est valide ?
		if(tour == null)
			return false;
		
		// elle peut etre posee sur le terrain
		if(!terrain.laTourPeutEtrePosee(tour))
			return false;
		
		// et le joueur a assez d'argent
		return laTourPeutEtreAchetee(tour);
	}

	/**
	 * Permet de vendre une tour.
	 * 
	 * @param tour la tour a vendre
	 */
	public void vendreTour(Tour tour)
	{
		// supprime la tour
		terrain.supprimerTour(tour);
		
		// credit des pieces d'or
		nbPiecesOr += tour.getPrixDeVente();
	}
	
	/**
	 * Permet d'ameliorer une tour.
	 * 
	 * @param tour la tour a ameliorer
	 * @return vrai si operation realisee avec succes, sinon faux 
	 * @throws Exception 
	 */
	public void ameliorerTour(Tour tour) throws Exception
	{
	    if(!tour.peutEncoreEtreAmelioree())
            throw new Exception("Amélioration impossible : Niveau max atteint");
	    
	    if(nbPiecesOr < tour.getPrixAchat())
		    throw new Exception("Amélioration impossible : Pas assez d'argent");

		// debit des pieces d'or
		nbPiecesOr -= tour.getPrixAchat();
		
		// amelioration de la tour
		tour.ameliorer();
	}

	/**
	 * Permet de recuperer le nombre de piece d'or du joueur.
	 * @return le nombre de piece d'or du joueur
	 */
	public  int getNbPiecesOr()
	{
		return nbPiecesOr;
	}

	/**
	 * Permet de recuperer le nombre de vies restantes du joueur.
	 * @return le nombre de vies restantes du joueur
	 */
	public int getNbViesRestantes()
	{
		return viesRestantes;
	}

	/**
	 * Permet de recuperer l'image de fond du terrain.
	 * @return l'image de fond du terrain
	 */
	public Image getImageDeFondTerrain()
	{
		return terrain.getImageDeFond();
	}

	/**
	 * Permet de recuperer les tours sur le terrain.
	 * @return les tours du terrain.
	 */
	public Vector<Tour> getTours()
	{
		return terrain.getTours();
	}
	
	/**
	 * Permet de recuperer les creatures sur le terrain.
	 * @return les creatures du terrain.
	 */
	public Vector<Creature> getCreatures()
	{
		return terrain.getCreatures();
	}
	
	/**
	 * Permet de lancer une nouvelle vague de creatures.
	 */
	public void lancerVagueSuivante(EcouteurDeCreature edc)
	{
		terrain.lancerVagueSuivante(edc);
	}

	/**
	 * Permet de recuperer la liste des arcs actifs du maillage.
	 * @return une collection de java.awt.geom.Line2D representant les
	 * 		   arcs actifs du maillage
	 */
	public ArrayList<Line2D> getArcsActifs()
	{
		return terrain.getArcsActifs();
	}

	
	/**
	 * methode regissant de l'interface EcouteurDeCreature
	 * 
	 * Permet d'etre informe lorsqu'une creature a ete tuee.
	 */
	public void creatureTuee(Creature creature)
	{
		terrain.supprimerCreature(creature);
		
		// gain de pieces d'or
		nbPiecesOr 	+= creature.getNbPiecesDOr();
		
		// augmentation du score
		score 		+= creature.getNbPiecesDOr();
	}
	
	public Rectangle getZoneDepart()
	{
		return terrain.getZoneDepart();
	}
	
	public Rectangle getZoneArrivee()
	{
		return terrain.getZoneArrivee();
	}

	public ArrayList<Noeud> getNoeuds()
	{
		return terrain.getNoeuds();
	}

	public int getHauteurTerrain()
	{
		return terrain.getHauteur();
	}

	public int getLargeurTerrain()
	{
		return terrain.getLargeur();
	}

	public void perdreUneVie()
	{
		viesRestantes--;		
	}

	public boolean estPerdu()
	{
		return viesRestantes <= 0;
	}
	
	public int getNumVagueCourante()
	{
		return terrain.getNumVagueCourante();
	}
	
	public static String getVersion()
	{
	    return "heig-vd - ASD2 :: Tower Defense "+VERSION;
	}

    public int getScore()
    {
        return score;
    }

    public String getNomTerrain()
    {
        return terrain.getNom();
    }

    public String getDescriptionVagueCourante()
    {
        return terrain.getDescriptionVagueCourante();
    }

    public void creatureArriveeEnZoneArrivee(Creature creature)
    {
        terrain.supprimerCreature(creature);
    }
}
