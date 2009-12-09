package models.tours;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

import models.creatures.Creature;

/**
 * Classe de gestion d'une tour de feu.
 * Cette classe derive de Tour.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Tour
 */
public class TourDeFeu extends Tour
{
	private static final long serialVersionUID = 1L;
	public static final Color COULEUR;
	public static final Image IMAGE;
	
	static
	{
		COULEUR = Color.RED;
		IMAGE 	= Toolkit.getDefaultToolkit().getImage("img/tours/basic_tower_1.png");
	}
	
	public TourDeFeu()
	{
		super(0, 				// x
			  0, 				// y
			  20, 				// largeur
			  20, 				// hauteur
			  COULEUR,			// couleur de fond
			  "Tour de feu",	// nom
			  10,				// prix achat
			  5,				// degats
			  50,				// rayon de portee
			  IMAGE);				
	
		description = "La tour de feu est une tour \nqui fait beaucoup degats," +
					  " mais elle est très lente. " +
					  "Elle fait egalement des degats de zone.";
	}
	
	public void ameliorer()
	{
		// le prix total est ajouté du prix d'achat de la tour
		prixTotal 	+= prixAchat;
		
		prixAchat 	+= 10;
		degats    	*= 2;
		rayonPortee += 50;
		
		niveau++;
	}

	public void tirer(Creature creature)
	{
		// terrain.ajouteTire(new bouleDeFeu(this,creature));
		creature.blesser(degats);
	}

	public Tour getCopieOriginale()
	{
		return new TourDeFeu();
	}
}
