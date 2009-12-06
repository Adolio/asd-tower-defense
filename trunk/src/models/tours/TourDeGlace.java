package models.tours;

import java.awt.Color;
import models.creatures.Creature;

/**
 * Classe de gestion d'une tour de glace.
 * Cette classe derive de Tour.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Tour
 */
public class TourDeGlace extends Tour
{
	private static final long serialVersionUID = 1L;
	public static final Color COULEUR = Color.BLUE;
	
	public TourDeGlace()
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
		
		description = "La tour de glace est une \ntour rapide qui ralenti les ennemis";
	}
	
	public void ameliorer()
	{
		// le prix total est ajouté du prix d'achat de la tour
		prixTotal 	+= prixAchat;
		
		prixAchat 	+= 10;
		degats    	*= 2;
		rayonPortee += rayonPortee * 0.5;
		
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
	
	public void tirer(Creature creature)
	{
		// terrain.ajouteTire(new bouleDeGlace(this,creature));
		creature.blesser(degats);
	}
}
