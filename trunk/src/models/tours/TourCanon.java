package models.tours;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import vues.attaques.BouletDeCanon;
import models.creatures.Creature;

/**
 * Classe de gestion d'une tour a canon.
 * <p>
 * Le tour canon est une tour lente avec de bons degats de zone. 
 * De plus, elle n'attaque que les creatures terrestres
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Tour
 */
public class TourCanon extends Tour
{
	private static final long serialVersionUID = 1L;
	public static final Color COULEUR;
	public static final Image IMAGE;
	public static final Image ICONE;
	public static final int NIVEAU_MAX = 4;
    private static final double RAYON_IMPACT = 30.0;
    private static final String DESCRIPTION = 
        "Le tour canon est une tour avec de bons dégâts mais lente. " +
        "De plus, elle n'attaque que les créatures terrestres";
	
	static
	{
	    COULEUR = new Color(64,64,64);
		IMAGE 	= Toolkit.getDefaultToolkit().getImage("img/tours/tourCanon.png");
		ICONE   = Toolkit.getDefaultToolkit().getImage("img/tours/icone_tourCanon.png");
	}
	
	/**
     * Constructeur de la tour.
     */
	public TourCanon()
	{
		super(0, 				// x
			  0, 				// y
			  20, 				// largeur
		      20, 				// hauteur
			  COULEUR,			// couleur de fond
			  "Canon",	        // nom
			  20,				// prix achat
			  15,				// degats
			  40,				// rayon de portee
			  1,                // cadence de tir (tirs / sec.)
              Tour.TYPE_TERRESTRE, // type
              IMAGE,            // image sur terrain
              ICONE);           // icone pour bouton			
		
		description = DESCRIPTION;
	}

	public void ameliorer()
	{
		// le prix total est ajouté du prix d'achat de la tour
		prixTotal 	+= prixAchat;
		
		prixAchat 	*= 2;	// + 100%
		degats    	*= 1.5; // + 50%
		rayonPortee *= 1.2; // + 20%
		cadenceTir	*= 1.2;
		
		niveau++;
	}
	
	public void tirer(Creature creature)
	{
	    terrain.ajouteAnimation(new BouletDeCanon(terrain,this,creature,degats,RAYON_IMPACT));
	}


	public Tour getCopieOriginale()
	{
		return new TourCanon();
	}
	
	public boolean peutEncoreEtreAmelioree()
	{
		return niveau <= NIVEAU_MAX;
	}
}
