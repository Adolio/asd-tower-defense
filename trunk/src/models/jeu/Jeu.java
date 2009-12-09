package models.jeu;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import models.creatures.Creature;
import models.creatures.Creature1;
import models.creatures.EcouteurDeCreature;
import models.creatures.VagueDeCreatures;
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
public class Jeu implements EcouteurDeCreature
{
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
	private int nbPiecesOr 		= 1000;
	
	/**
	 * Gestion des vagues de creatures.
	 * C'est le joueur que decident le moment ou il veut lancer une vague de
	 * creatures. Une fois que toutes les vagues de creatures ont ete detruites,
	 * le jeu est considere comme termine.
	 */
	private int indiceVagueCourante;
	private VagueDeCreatures[] vagues = {
										new VagueDeCreatures(5, new Creature1(50,2)),
										new VagueDeCreatures(10, new Creature1(50,2)),
										new VagueDeCreatures(5, new Creature1(200,4)),
										new VagueDeCreatures(20, new Creature1(100,4)),
										new VagueDeCreatures(10, new Creature1(300,30)),
										new VagueDeCreatures(1, new Creature1(2000,100))
										};
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
	}
	
	/**
	 * Permet de tenter de poser une tour sur le terrain de jeu.
	 * 
	 * @param tour la tour a poser
	 * @return true si operation realisee avec succes, false sinon 
	 */
	public boolean poserTour(Tour tour)
	{
		// suffisemment d'argent ?
		if(laTourPeutEtreAchetee(tour))
		{
			// ajout de la tour dans le terrain de jeu
			if(terrain.ajouterTour(tour))
			{
				// la tour est mise en jeu
				tour.demarrer();
				
				// debit des pieces d'or
				nbPiecesOr -= tour.getPrixAchat();
				
				return true;
			}
		}
		
		return false;
	}

	/**
	 * permet de savoir si une tour peut etre achetee.
	 * @param tour la tour a achetee
	 * @return true si le joueur a assez de pieces d'or, false sinon
	 */
	public boolean laTourPeutEtreAchetee(Tour tour)
	{
		if(tour != null)
			return nbPiecesOr - tour.getPrixAchat() >= 0;
		
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
	 */
	public boolean ameliorerTour(Tour tour)
	{
		if(nbPiecesOr >= tour.getPrixAchat())
		{
			// debit des pieces d'or
			nbPiecesOr -= tour.getPrixAchat();
			
			// amelioration de la tour
			tour.ameliorer();
			
			return true;
		}
		
		return false;
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
	public ArrayList<Tour> getTours()
	{
		return terrain.getTours();
	}
	
	/**
	 * Permet de recuperer les creatures sur le terrain.
	 * @return les creatures du terrain.
	 */
	public ArrayList<Creature> getCreatures()
	{
		return terrain.getCreatures();
	}
	
	/**
	 * Permet de lancer une nouvelle vague de creatures.
	 */
	public void lancerVagueSuivante()
	{
		// il reste encore des vagues de creatures
		if(indiceVagueCourante < vagues.length)
		{
			// recuperation de la vague
			VagueDeCreatures vague = vagues[indiceVagueCourante];
			
			// creation des creatures de la vague
			for(int i=0;i<vague.getNbCreatures();i++)
			{
				// recuperation des zones
				Rectangle depart  = terrain.getZoneDepart();
				Rectangle arrivee = terrain.getZoneArrivee();
				
				// calcul de la position aleatoire de la creature
				// dans la zone de depart
				int xDepart = (int)(Math.random() * (depart.getWidth() 
								 + 1) + depart.getX());
				int yDepart = (int)(Math.random() * (depart.getHeight() 
								 + 1) + depart.getY());
				
				// creation d'une nouvelle instance de la creature
				// et affectation de diverses proprietes
				Creature creature = vague.getNouvelleCreature();
				creature.setX(xDepart);
				creature.setY(yDepart);
				creature.ajouterEcouteurDeCreature(this);
				creature.setChemin(terrain.getCheminLePlusCourt(
										xDepart, 
										yDepart, 
										(int) arrivee.getCenterX(), 
										(int) arrivee.getCenterY()));
				
				terrain.ajouterCreature(creature);
				creature.demarrer();
			}
			
			indiceVagueCourante++;
		}
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
	 * Permet d'etre informe lorsqu'une creature subie des degats.
	 */
	public void creatureBlessee(Creature creature)
	{
	
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
		score 		+= creature.getNbPiecesDOr(); 
		
		// TODO mise a jour des vues
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
}
