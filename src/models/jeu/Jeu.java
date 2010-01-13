package models.jeu;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Vector;
import models.animations.*;
import models.creatures.Creature;
import models.creatures.EcouteurDeCreature;
import models.creatures.EcouteurDeVague;
import models.maillage.Noeud;
import models.terrains.Terrain;
import models.tours.Tour;

/**
 * Classe de gestion du jeu.
 * 
 * Cette classe contient :
 * <br>
 * - Le terrain qui contient les tours et les creatures
 * <br>
 * - Les informations du jeu et du joueur (score, pieces d'or, etc.)
 * <p>
 * Elle sert principalement de classe d'encapsulation du terrain.
 * Elle gere egalement tout ce qui concerne les pieces d'or du joueur 
 * (achat, amelioration et vente des tours)
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Tour
 */
public class Jeu
{
	/**
	 * version du jeu
	 */
    private static final String VERSION 
        = "heig-vd - ASD2 :: Tower Defense v1.0 | janvier 2010";

    /**
	 * score courant du joueur. Cette valeur equivaux a la somme 
	 * de toutes les pieces d'or amasse par le joueur durant la partie.
	 */
	private int score;
	
	/**
	 * vies restantes du joueur. 
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
		this.terrain  = terrain;
		nbPiecesOr    = terrain.getNbPiecesOrInitiales();
		viesRestantes = terrain.getNbViesInitiales();
	}
	
    /**
     * Permet de recuperer la version du jeu.
     * 
     * @return la version du jeu.
     */
    public static String getVersion()
    {
        return VERSION;
    }
	
	/**
     * Permet de recuperer le nombre de piece d'or du joueur.
     * @return le nombre de piece d'or du joueur
     */
    public int getNbPiecesOr()
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
     * Permet de recuperer la zone de depart du terrain
     * 
     * @return la zone de depart du terrain
     */
    public Rectangle getZoneDepart()
    {
        return terrain.getZoneDepart();
    }
    
    /**
     * Permet de recuperer la zone d'arriver du terrain
     * 
     * @return la zone de d'arriver du terrain
     */
    public Rectangle getZoneArrivee()
    {
        return terrain.getZoneArrivee();
    }

    /**
     * Permet de recuperer les noeuds du maillage terrien
     * 
     * @return les noeuds du maillage terrien
     */
    public ArrayList<Noeud> getNoeuds()
    {
        return terrain.getNoeuds();
    }
    
    /**
     * Permet de recuperer la liste des arcs actifs du maillage terrien.
     * 
     * @return une collection de java.awt.geom.Line2D representant les
     *         arcs actifs du maillage
     */
    public ArrayList<Line2D> getArcsActifs()
    {
        return terrain.getArcsActifs();
    }

    /**
     * Permet de recuperer la hauteur du terrain
     * 
     * @return la hauteur du terrain
     */
    public int getHauteurTerrain()
    {
        return terrain.getHauteur();
    }

    /**
     * Permet de recuperer la largeur du terrain
     * 
     * @return la largeur du terrain
     */
    public int getLargeurTerrain()
    {
        return terrain.getLargeur();
    }
   
    /**
     * Permet de recuperer le numero de la vague courante
     * 
     * @return le numero de la vague courante
     */
    public int getNumVagueCourante()
    {
        return terrain.getNumVagueCourante();
    }

    /**
     * Permet de recuperer le score du joueur
     * 
     * @return le score du joueur
     */
    public int getScore()
    {
        return score;
    }

    /**
     * Permet de recuperer le nom du terrain
     * 
     * @return le nom du terrain
     */
    public String getNomTerrain()
    {
        return terrain.getNom();
    }

    /**
     * Permet de recuperer la description de la vague suivante
     * 
     * @return la description de la vague suivante
     */
    public String getDescriptionVagueSuivante()
    {
        return terrain.getDescriptionVagueSuivante();
    }
    
    /**
     * Permet de savoir si le joueur a perdu et donc si la partie est terminee
     * 
     * @return true s'il a perdu, false sinon
     */
    public boolean partieEstPerdu()
    {
        return viesRestantes <= 0;
    }
    
    
	/**
	 * Permet de poser une tour sur le terrain de jeu.
	 * 
	 * @param tour la tour a poser
	 * @throws Exception si pas assez d'argent
	 * @throws Exception si la zone n'est pas accessible (occupee)
     * @throws Exception si la tour bloque empeche tous chemins
	 */
	public void poserTour(Tour tour) throws Exception
	{
		// suffisemment d'argent ?
		if(!laTourPeutEtreAchetee(tour))
		    throw new Exception("Pose impossible : Pas assez d'argent");
		
	    // ajout de la tour dans le terrain de jeu
		terrain.ajouterTour(tour);
		
		// la tour est mise en jeu
		tour.mettreEnJeu();
		
		// debit des pieces d'or
		nbPiecesOr -= tour.getPrixAchat();
	}

	/**
	 * Permet de savoir si une tour peut etre achetee.
	 * 
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
	 * 
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
	 * @throws Exception si pas assez d'argent 
	 * @throws Exception si niveau max de la tour atteint
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
	 * Permet de lancer une nouvelle vague de creatures.
	 */
	public void lancerVagueSuivante(EcouteurDeVague edv,EcouteurDeCreature edc)
	{
		terrain.lancerVagueSuivante(edv, edc);
	}

	/**
	 * Permet d'etre informe lorsqu'une creature a ete tuee.
	 * 
	 * @param la creature qui a ete tuee
	 */
	public void creatureTuee(Creature creature)
	{
		// gain de pieces d'or
		nbPiecesOr 	+= creature.getNbPiecesDOr();
		
		// augmentation du score
		score 		+= creature.getNbPiecesDOr();
	}

    /**
     * Permet d'etre informe lorsqu'une creature est arrivee en zone d'arrivee.
     * 
     * @param creature qui est arrivee en zone d'arrivee
     */
    public void creatureArriveeEnZoneArrivee(Creature creature)
    {
        // perd une vie
        viesRestantes--;
    }
    
    /**
     * Permet de demarrer la lecture de la musique d'ambiance du terrain
     */
    public void demarrerMusiqueDAmbianceDuTerrain()
    {
        terrain.demarrerMusiqueDAmbiance();
    }

    /**
     * Permet d'arreter la lecture de la musique d'ambiance du terrain
     */
    public void arreterMusiqueDAmbianceDuTerrain()
    {
        terrain.arreterMusiqueDAmbiance();
    }

    /**
     * Permet d'ajouter une animation
     * 
     * @param animation l'aniatio a ajouter
     */
    public void ajouterAnimation(Animation animation)
    {
        terrain.ajouterAnimation(animation);
    }

    /**
     * Permet de terminer la partie en cours
     */
    public void terminerLaPartie()
    {
        terrain.arreterTout();
    }

    /**
     * Permet de recuperer le gestionnaire d'animations
     * 
     * @return le gestionnaire d'animations
     */
    public GestionnaireAnimations getGestionnaireAnimations()
    {
        return terrain.getGestionnaireAnimations();    
    }

    // TODO [DEBUG] a effacer
    /**
     * (pour debug) Permet d'ajouter des pieces d'or
     * 
     * @param nbPiecesDOr le nombre de piece d'or a ajouter
     */
    public void ajouterPiecesDOr(int nbPiecesDOr)
    {
        this.nbPiecesOr += nbPiecesDOr;
    }
}
