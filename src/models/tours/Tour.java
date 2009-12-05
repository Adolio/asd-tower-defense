package models.tours;

import java.awt.*;
import models.creatures.Creature;
import models.jeu.Jeu;

/**
 * Classe de gestion d'une tour
 * 
 * Cette classe est abstraite et doit etre heritee pour etre ensuite instanciee.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 */
public abstract class Tour extends Rectangle implements Runnable
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * couleur de la tour
	 */
	private Color couleurDeFond;
	
	/**
	 * nom de la tour
	 */
	private String nom;
	
	/**
	 * texte descriptif de la tour
	 */
	protected String description;
	
	/**
	 * texte descriptif de la tour
	 */
	protected int degats;
	
	/**
	 * niveau actuelle de la tour
	 */
	protected int niveau = 1; 
	
	/**
	 * prix achat de la tour ou de son amelioration
	 */
	protected int prixAchat;
	
	/**
	 * prix total de la tour en compant toutes les ameliorations
	 */
	protected int prixTotal; 

	/**
	 * coefficient de prix de vente en pourcentage du prix total de la tour
	 */
	private double coeffPrixVente = 0.5;
	
	/**
	 * rayon de portee de la tour
	 */
	protected double rayonPortee = 100;
	
	protected Jeu jeu;
	private Thread thread;
	private boolean enJeu;
	
	/**
	 * Constructeur de la tour
	 * 
	 * @param x
	 * @param y
	 * @param largeur
	 * @param hauteur
	 * @param couleurDeFond
	 * @param nom
	 * @param prixAchat
	 */
	public Tour(int x,int y, 
				int largeur, 
				int hauteur, 
				Color couleurDeFond, 
				String nom, 
				int prixAchat,
				int degats,
				double rayonPortee)
	{
		this.x = x;
		this.y = y;
		
		width  = largeur;
		height = hauteur;
		
		this.nom = nom;
		this.couleurDeFond 	= couleurDeFond;
		this.prixAchat 		= prixAchat;
		prixTotal 			= prixAchat;
		this.degats			= degats;
		this.rayonPortee	= rayonPortee;
	}
	
	public abstract void ameliorer();
	
	public Color getCouleurDeFond()
	{
		return couleurDeFond;
	}
	
	public String getNom()
	{
		return nom;
	}
	
	public int getXi()
	{
		return x;
	}
	
	public int getYi()
	{
		return y;
	}

	public String getDescription()
	{
		return description;
	}

	public int getPrixAchat()
	{
		return prixAchat;
	}
	
	public double getRayonPortee()
	{
		return rayonPortee;
	}
	
	public double getCoeffPrixVente()
	{
		return coeffPrixVente;
	}
	
	public int getPrixDeVente()
	{
		return (int) (prixTotal * coeffPrixVente);
	}

	/*
	public String toString()
	{
		return x+" "+y+" "+width;
	}*/

	public int getPrixTotal()
	{
		return prixTotal;
	}
	
	public void demarrer()
	{
		thread = new Thread(this);
		
		thread.start();
	}
	
	public void run()
	{
		enJeu = true;
		
		while(enJeu)
		{
			Creature creature = getCreatureLaPlusProcheEtAPortee();
			
			if(creature != null)
			{
				try{
					Thread.sleep(200);
				} 
				catch (InterruptedException e){
					e.printStackTrace();
				}
				creature.blesser(degats);
			}
			else
			{
				try{
					Thread.sleep(50);
				} 
				catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	private Creature getCreatureLaPlusProcheEtAPortee()
	{
		Creature creatureLaPlusProche 	= null;
		double distancePlusProche 		= 0;
		double distance 				= 0;
		
		for(Creature creature : jeu.getCreatures())
		{
			distance = getDistance(creature);
			
			// est a portee ?
			if(distance <= rayonPortee)
			{
				// la creature a plus proche que la creature 
				// actuelle plus proche
				if(creatureLaPlusProche == null ||
				distance < distancePlusProche)
				{	
					creatureLaPlusProche = creature;
					distancePlusProche 	 = distance;
				}
			}
		}
		
		return creatureLaPlusProche;
	}
	
	private double getDistance(Creature creature)
	{
		return Point.distance(x, y, creature.x, creature.y);
	}

	public void arreter()
	{
		enJeu = false;
	}
}
