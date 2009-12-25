package models.tours;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import models.creatures.Creature;
import models.outils.Musique;

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
public class TourCanon extends Tour
{
	private static final long serialVersionUID = 1L;
	public static final Color COULEUR;
	public static final Image IMAGE;
	public static final Image ICONE;
	public static final int NIVEAU_MAX = 4;
	public static final Musique SON_CANON;
    private static final double PORTEE_DEGATS_ZONE = 50.0;
	
	static
	{
	    SON_CANON = new Musique("snd/canon.mp3");
	    COULEUR = new Color(64,64,64);
		IMAGE 	= Toolkit.getDefaultToolkit().getImage("img/tours/basic_tower_2.png");
		ICONE   = Toolkit.getDefaultToolkit().getImage("img/tours/icone_tourCanon.png");
	}
	
	public TourCanon()
	{
		super(0, 				// x
			  0, 				// y
			  20, 				// largeur
		      20, 				// hauteur
			  COULEUR,			// couleur de fond
			  "Canon",	// nom
			  20,				// prix achat
			  15,				// degats
			  40,				// rayon de portee
			  1,
			  Tour.TYPE_TERRESTRE,
			  IMAGE,
			  ICONE);				
		
		description = "Le tour canon est une tour avec de bons dégâts mais " +
					  "lente. De plus, elle n'attaque que " +
					  "les créatures terrestres";
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
		// terrain.ajouteTire(new bouleDeGlace(this,creature));
        //SON_CANON.lire(1);
	    
	    // degats de zone
	    int degatsFinal;
	    double distanceImpact;

	    for(int i=0;i < terrain.getCreatures().size();i++)
	    {
	        Creature tmpCreature = terrain.getCreatures().get(i);
	        
	        // si la creature est dans le splash
	        distanceImpact = Point.distance(tmpCreature.x, tmpCreature.y, creature.x, creature.y);
	        if(distanceImpact <= PORTEE_DEGATS_ZONE)
	        {
	            // calcul des degats en fonction de la distance de l'impact
	            degatsFinal = (int) (degats - (distanceImpact / PORTEE_DEGATS_ZONE * degats));
	            tmpCreature.blesser(degatsFinal);
	        }
	    }
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
