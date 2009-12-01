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
	
	protected Vector<Rectangle> murs = new Vector<Rectangle>();
	
	private Jeu jeu;
	
	public Terrain(int largeur, int hauteur, String imageDeFond)
	{
		LARGEUR 		= largeur;
		HAUTEUR 		= hauteur;
	
		if (imageDeFond != null)
			IMAGE_DE_FOND = Toolkit.getDefaultToolkit().getImage(getClass().getResource(imageDeFond));
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
		// maillage.desactiverNoeuds(mur); // recoit un Rectangle
	}
	
}