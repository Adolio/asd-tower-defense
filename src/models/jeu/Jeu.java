package models.jeu;

import java.awt.Rectangle;
import java.util.Vector;

import models.terrain.Terrain;
import models.tours.Tour;
import vues.EcouteurOperationSurTour;

/**
 * Classe de gestion du jeu.
 * 
 * Cette classe contient les objets important de la partie comme :
 * <br>
 * - La liste des tours sur le terrain
 * <br>
 * - La liste des ennemis sur le terrain
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
	/**
	 * Les tours sont les elements principals du jeu.
	 * @see Tour
	 */
	private Vector<Tour> tours;
	
	/**
	 * Score courant du joueur
	 */
	private int score;
	
	/**
	 * Vies restantes du joueur. 
	 * <br>
	 * Lorsque un ennemi atteint la zone d'arrive, le nombre de vies est alors
	 * decremente.
	 */
	private int viesRestantes 	= 20;
	
	/**
	 * Nombre de pieces d'or du joueur.
	 * <br>
	 * Cette variable fluctue en fonction des ennemis tue et de 
	 * l'achat et vente des tours.
	 */
	private int nbPiecesOr 		= 100;
	
	// TODO commenter et controler
	private EcouteurOperationSurTour eops;

	private Terrain terrain;
	
	/**
	 * Constructeur de la partie
	 */
	public Jeu(Terrain terrain)
	{
		tours 		 = new Vector<Tour>();
		this.terrain = terrain;
		terrain.setJeu(this);
	}
	
	/**
	 * Permet de recuperer la liste des tours
	 * @return la liste des tours
	 */
	public Vector<Tour> getTours()
	{
		return tours;
	}
	
	/**
	 * Permet de tanter de poser une tour sur le terrain de jeu.
	 * 
	 * @param tour la tour a poser
	 * @return vrai si operation realisee avec succes, sinon faux 
	 */
	public boolean poserTour(Tour tour)
	{
		if(laTourPeutEtrePosee(tour))
		{
			// TODO adaptation du maillage
			// maillage.desactiverNoeuds(tour); // recoit un Rectangle
			
			// debit des pieces d'or
			nbPiecesOr -= tour.getPrixAchat();

			// ajout de la tour dans le terrain de jeu
			tours.add(tour);
			
			return true;
		}
		
		return false;
	}

	/**
	 * Permet de vendre une tour
	 * 
	 * @param tour la tour a vendre
	 */
	public void vendreTour(Tour tour)
	{
		// TODO redonne des pieces d'or
		nbPiecesOr += tour.getPrixDeVente();
		
		// TODO adaptation du maillage
		// maillage.activerNoeuds(tour); // recoit un Rectangle
		
		// supprime la tour
		tours.remove(tour);
	}
	
	/**
	 * Permet d'ameliorer une tour
	 * 
	 * @param tour la tour a ameliorer
	 * @return vrai si operation realisee avec succes, sinon faux 
	 */
	public boolean ameliorerTour(Tour tour)
	{
		if(laTourPeutEtrePosee(tour))
		{
			// debit des pieces d'or
			nbPiecesOr -= tour.getPrixAchat();
			
			// amelioration de la tour
			tour.ameliorer();
			
			return true;
		}
		
		return false;
	}
	
	public boolean laTourPeutEtrePosee(Tour tour)
	{
		// il n'y a pas déjà une tour
		for(Tour tourCourante : tours)
			if(tour.intersects(tourCourante))
				return false;
		
		// il n'y a pas un mur
		for(Rectangle mur : terrain.getMurs())
			if(tour.intersects(mur))
				return false;
		
		// TODO il n'y a pas déjà un ennemi
		
		
		// le joueur a assez d'argent
		return nbPiecesOr - tour.getPrixAchat() >= 0;
	}
	
	public  int getNbPiecesOr()
	{
		return nbPiecesOr;
	}

	public void ajouterEcouteurOperationSurTour(EcouteurOperationSurTour eops)
	{
		this.eops = eops;
	}

	public Terrain getTerrain()
	{
		return terrain;
	}
}
