package models.tours;

import java.awt.Color;

import models.jeu.Jeu;
import models.terrains.Terrain;

public class TourDeGlace extends Tour
{
	private static final long serialVersionUID = 1L;
	public static final Color COULEUR = Color.BLUE;
	
	public TourDeGlace(Jeu jeu)
	{
		super(0, 				// x
			  0, 				// y
			  20, 				// largeur
		      20, 				// hauteur
			  COULEUR,			// couleur de fond
			  "Tour de glace",	// nom
			  20,				// prix achat
			  40,				// degats
			  40);				// rayon de portee
		
		this.jeu = jeu;
		description = "La tour de glace est une \ntour rapide qui ralenti les ennemis";
	}
	
	public void ameliorer()
	{
		// le prix total est ajouté du prix d'achat de la tour
		prixTotal += prixAchat;
		
		prixAchat 	+= 10;
		degats    	*= 2;
		rayonPortee += 50;
		
		/*switch(niveau)
		{
			case 1 :
				prixAchat 	= 20;
				degats 		= 20;
				break;
			case 2 :
				prixAchat 	= 40;
				degats 		= 40;
				break;
		}*/
		
		niveau++;
	}
}
