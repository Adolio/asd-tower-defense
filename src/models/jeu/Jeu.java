package models.jeu;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import models.creatures.Creature;
import models.creatures.Creature1;
import models.creatures.EcouteurDeCreature;
import models.creatures.VagueDeCreatures;
import models.terrains.Terrain;
import models.tours.Tour;

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
public class Jeu implements EcouteurDeCreature
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
	private int nbPiecesOr 		= 1000;
	
	private int vagueCourante;
	private VagueDeCreatures[] vagues = {
										new VagueDeCreatures(10, new Creature1()),
										new VagueDeCreatures(1, new Creature1()),
										new VagueDeCreatures(10, new Creature1())
										};

	private Terrain terrain;
	
	/**
	 * Constructeur de la partie
	 */
	public Jeu(Terrain terrain)
	{
		this.terrain = terrain;
		terrain.setJeu(this);
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
			// Désactive la zone sur le terrain qui correspont à la tour
			terrain.desactiverZone(tour);
			
			// debit des pieces d'or
			nbPiecesOr -= tour.getPrixAchat();

			// ajout de la tour dans le terrain de jeu
			terrain.ajouterTour(tour);
			
			// la tour est mise en jeu
			tour.demarrer();
			
			miseAJourDesChemins();			
			
			return true;
		}
		
		return false;
	}

	private void miseAJourDesChemins()
	{
		// mise a jour de tous les chemins
		for(Creature creature : terrain.getCreature())
			creature.setChemin(terrain.getChemin(creature.x, creature.y,
						       (int)terrain.getZoneArrivee().getCenterX(), 
						       (int)terrain.getZoneArrivee().getCenterY()));
		
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
		// supprime la tour
		terrain.supprimerTour(tour);
		
		
		
		// redonne les pieces d'or
		nbPiecesOr += tour.getPrixDeVente();
		
		// adaptation du maillage
		terrain.activerZone(tour);
		miseAJourDesChemins();
	}
	
	/**
	 * Permet d'ameliorer une tour
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

	
	public  int getNbPiecesOr()
	{
		return nbPiecesOr;
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
	
	public void lancerVagueSuivante()
	{
		if(vagueCourante < vagues.length)
		{
			VagueDeCreatures vague = vagues[vagueCourante];
			
			for(int i=0;i<vague.getNbDeCreatures();i++)
			{
				Rectangle depart = terrain.getZoneDepart();
				Rectangle arrivee = terrain.getZoneArrivee();
				
				int x = (int)(Math.random() * (depart.getWidth() + 1));
				int y = (int)(Math.random() * (depart.getHeight() + 1));
				
				Creature creature = vague.getNouvelleCreature();
				int xDepart = (int) (x+depart.getX());
				int yDepart = (int) (y+depart.getY());
				
				creature.setX(xDepart);
				creature.setY(yDepart);
				
				int xArrivee = (int) (arrivee.getX()+arrivee.getWidth()/2);
				int yArrivee = (int) (arrivee.getY()+arrivee.getHeight()/2);
				
				
				
				creature.ajouterEcouteurDeCreature(this);
				creature.setChemin(terrain.getChemin(xDepart, yDepart, xArrivee, yArrivee));
				terrain.ajouterCreature(creature);
				creature.demarrer();
			}
			
			vagueCourante++;
		}
	}

	public ArrayList<Point> getChemin(int xDepart, int yDepart, 
									  int xArrivee, int yArrivee)
	{
		return terrain.getChemin(xDepart,yDepart,xArrivee,yArrivee);
	}

	public ArrayList<Line2D> getArcsActifs()
	{
		return terrain.getArcsActifs();
	}


	public void creatureBlessee(Creature creature)
	{
	
	}

	public void creatureTuee(Creature creature)
	{
		terrain.supprimerCreature(creature);
		nbPiecesOr 	+= creature.getGainPiecesDOr();
		score 		+= creature.getGainPiecesDOr(); 
		
		// TODO mise a jour des vues
		
	}
}
