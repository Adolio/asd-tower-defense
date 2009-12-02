package models.jeu;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import models.creatures.Creature;
import models.terrains.Terrain;
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
		this.terrain = terrain;
		terrain.setJeu(this);
		
		// TODO effacer ca !!!
		lancerVague();
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
			terrain.ajouterTour(tour);
			
			return true;
		}
		
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
		// elle peut etre posee sur le terrain
		if(!terrain.laTourPeutEtrePosee(tour))
			return false;
		
		// et le joueur a assez d'argent
		return nbPiecesOr - tour.getPrixAchat() >= 0;
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
		terrain.supprimerTour(tour);
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

	
	public  int getNbPiecesOr()
	{
		return nbPiecesOr;
	}

	public void ajouterEcouteurOperationSurTour(EcouteurOperationSurTour eops)
	{
		this.eops = eops;
	}

	public int getNbViesRestantes()
	{
		return viesRestantes;
	}

	public Image getImageDeFondTerrain()
	{
		return terrain.getImageDeFond();
	}

	public ArrayList<Tour> getTours()
	{
		return terrain.getTours();
	}
	
	public ArrayList<Creature> getCreatures()
	{
		return terrain.getCreature();
	}
	
	public void lancerVague()
	{
		for(int i=0;i<20;i++)
		{
			Rectangle depart = terrain.getZoneDepart();
			
			int x = (int)(Math.random() * (depart.getWidth() + 1));
			int y = (int)(Math.random() * (depart.getHeight() + 1));
			
			terrain.ajouterCreature(new Creature(
					(int)depart.getX()+x,
					(int)depart.getY()+y));
		}
	}
	
}
