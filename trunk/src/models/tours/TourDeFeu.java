package models.tours;

import java.awt.Color;

import models.jeu.Jeu;

public class TourDeFeu extends Tour
{
	private static final long serialVersionUID = 1L;
	public static final Color COULEUR = Color.RED;
	public TourDeFeu(Jeu jeu)
	{
		super(0, 				// x
			  0, 				// y
			  20, 				// largeur
			  20, 				// hauteur
			  COULEUR,			// couleur de fond
			  "Tour de feu",	// nom
			  10,				// prix achat
			  5,				// degats
			  50);				// rayon de portee
		
		this.jeu = jeu;
		description = "La tour de feu est une tour \nqui fait beaucoup degats";
	}
	
	public void ameliorer()
	{
		// le prix total est ajouté du prix d'achat de la tour
		prixTotal += prixAchat;
		
		prixAchat 	+= 10;
		degats    	*= 2;
		rayonPortee += 50;
		
		/*
		switch(niveau)
		{
			case 1 :
				prixAchat 	= 20;
				degats 		= 20;
				rayonPortee = 200;
				break;
			case 2 :
				prixAchat 	= 40;
				degats 		= 40;
				rayonPortee = 300;
				break;
		}
		*/
		
		niveau++;
	}
}
