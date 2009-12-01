package models.terrain;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Vector;

import models.jeu.Jeu;

public class Terrain
{
	private final int LARGEUR, // en pixels
				      HAUTEUR; // en pixels
	
	private final  Image IMAGE_DE_FOND;
	
	/**
	 * Les murs sont utilises pour empecher le joueur de construire des tours dans 
	 * certaines zones. Les ennemis ne peuvent egalement pas si rendre.
	 * En fait, les murs reflettent les zones de la carte non accessible.
	 * Les murs ne sont pas affiches. Ils sont simplement utilises pour les
	 * controle d'acces de la carte.
	 */
	protected Vector<Rectangle> murs = new Vector<Rectangle>();
	
	private Jeu jeu;
	
	public Terrain(int largeur, int hauteur, String imageDeFond)
	{
		LARGEUR 		= largeur;
		HAUTEUR 		= hauteur;
	
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
	
	public void ajouterMur(Rectangle mur)
	{
		murs.add(mur);
		
		// TODO adaptation du maillage
		// jeu.getMaillage().desactiverNoeuds(mur); // recoit un Rectangle
	}

	public Vector<Rectangle> getMurs()
	{
		return murs;
	}
}