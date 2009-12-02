package models.terrains;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;

import models.creatures.Creature;
import models.jeu.Jeu;
import models.maillage.Maillage;
import models.tours.Tour;

public class Terrain
{
	
	/**
	 * Les tours sont les elements principals du jeu.
	 * @see Tour
	 */
	private ArrayList<Tour> tours;
	
	private ArrayList<Creature> creatures;
	
	
	private final int LARGEUR, // en pixels
				      HAUTEUR; // en pixels
	
	private final  Image IMAGE_DE_FOND;
	
	private final Rectangle ZONE_DEPART;
	private final Rectangle ZONE_ARRIVEE;
	
	/**
	 * Les murs sont utilises pour empecher le joueur de construire des tours dans 
	 * certaines zones. Les ennemis ne peuvent egalement pas si rendre.
	 * En fait, les murs reflettent les zones de la carte non accessible.
	 * Les murs ne sont pas affiches. Ils sont simplement utilises pour les
	 * controle d'acces de la carte.
	 */
	protected ArrayList<Rectangle> murs = new ArrayList<Rectangle>();
	
	private Jeu jeu;
	private final Maillage maillage;
	
	public Terrain(int largeur, int hauteur, String imageDeFond, 
				   Rectangle zoneDepart, Rectangle zoneArrivee)
	{
		LARGEUR 		= largeur;
		HAUTEUR 		= hauteur;
		maillage 		= new Maillage(largeur, hauteur);
		tours 			= new ArrayList<Tour>();
		creatures		= new ArrayList<Creature>();
		ZONE_DEPART 	= zoneDepart;
		ZONE_ARRIVEE 	= zoneArrivee;
		
		// chargement de l'image
		if (imageDeFond != null)
			IMAGE_DE_FOND = Toolkit.getDefaultToolkit().getImage(imageDeFond);
		else
			IMAGE_DE_FOND = null;
	}
	
	public void setJeu(Jeu jeu)
	{
		this.jeu = jeu;
	}
	
	public int getLargeur()
	{
		return LARGEUR;
	}

	public int getHauteur()
	{
		return HAUTEUR;
	}

	public Image getImageDeFond()
	{
		return IMAGE_DE_FOND;
	}
	
	
	/**
	 * Permet de recuperer la liste des tours
	 * @return la liste des tours
	 */
	public ArrayList<Tour> getTours()
	{
		return tours;
	}
	
	/**
	 * Permet d'ajouter une tour
	 * @param tour la tour a ajouter
	 */
	public void ajouterTour(Tour tour)
	{
		tours.add(tour);
	}
	
	/**
	 * Permet de supprimer une tour
	 * @param tour la tour a supprimer
	 */
	public void supprimerTour(Tour tour)
	{
		tours.remove(tour);
	}
	
	
	/**
	 * Permet d'ajouter un mur
	 * @param mur le mur ajouter
	 */
	public void ajouterMur(Rectangle mur)
	{
		murs.add(mur);
		
		// TODO adaptation du maillage
		// maillage.desactiverNoeuds(mur); // recoit un Rectangle
	}
	
	/**
	 * Permet de recuperer la liste des tours
	 * @return la liste des tours
	 */
	public ArrayList<Creature> getCreature()
	{
		return creatures;
	}
	
	/**
	 * Permet d'ajouter une creature
	 * @param tour la tour a ajouter
	 */
	public void ajouterCreature(Creature creature)
	{
		creatures.add(creature);
	}
	
	/**
	 * Permet de supprimer une creature
	 * @param tour la tour a supprimer
	 */
	public void supprimerCreature(Creature creature)
	{
		creatures.remove(creature);
	}
	
	
	public boolean laTourPeutEtrePosee(Tour tour)
	{
		// il n'y a pas déjà une tour
		for(Tour tourCourante : tours)
			if(tour.intersects(tourCourante))
				return false;
		
		// il n'y a pas un mur
		for(Rectangle mur : murs)
			if(tour.intersects(mur))
				return false;
		
		// TODO il n'y a pas déjà un ennemi
		return true;
	}
	
	public ArrayList<Point> getCheminLePlusCourt(int xDepart, int yDepart, 
											  int xArriver, int yArriver)
	{
		// TODO appeler la bonne methode du maillage
		//return maillage.cheminPlusCourt(xDepart,yDepart,
		//								xArriver,yArriver);
		
		return new ArrayList<Point>();
	}

	public Rectangle getZoneDepart()
	{
		return ZONE_DEPART;
	}
	
	public Rectangle getZoneArrivee()
	{
		return ZONE_ARRIVEE;
	}
}