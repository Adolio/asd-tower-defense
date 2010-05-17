package models.tours;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import models.attaques.Fleche;
import models.creatures.Creature;

/**
 * Classe de gestion d'une tour anti aerienne.
 * <p>
 * La tour anti aerienne est une tour qui est tres performante, 
 * mais elle n'attaque que les creatures volantes. 
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Tour
 */
public class TourAntiAerienne extends Tour
{
	private static final long serialVersionUID = 1L;
	public static final Color COULEUR;
	public static final Image IMAGE;
	public static final Image ICONE;
	public static final int NIVEAU_MAX = 5;
	public static final int PRIX_ACHAT = 30;
	public static final String DESCRIPTION = 
    	"La tour anti-aérienne est une tour qui est très performante," +
        " mais elle n'attaque que les créatures volantes. ";
	
	static
	{
	    COULEUR = new Color(30,0,170);
		IMAGE 	= Toolkit.getDefaultToolkit().getImage("img/tours/tourAntiAerienne.png");
		ICONE   = Toolkit.getDefaultToolkit().getImage("img/tours/icone_tourAntiAerienne.png");
	}
	
	/**
     * Constructeur de la tour.
     */
	public TourAntiAerienne()
	{
		super(0, 				// x
			  0, 				// y
			  20, 				// largeur
			  20, 				// hauteur
			  COULEUR,			// couleur de fond
			  "Anti Aérienne",	// nom
			  PRIX_ACHAT,		// prix achat
			  40,				// degats
			  50,				// rayon de portee
			  3,                // cadence de tir (tirs / sec.)
			  Tour.TYPE_AIR,    // type
			  IMAGE,            // image sur terrain
			  ICONE);		    // icone pour bouton
	
		description = DESCRIPTION;
	}
	
	public void ameliorer()
	{
	    if(peutEncoreEtreAmelioree())
        {
			// le prix total est ajouté du prix d'achat de la tour
			prixTotal 	+= prixAchat;
			
			// augmentation du prix du prochain niveau
			prixAchat 	*= 2;
			
			// augmentation des degats
			degats    	= getDegatsLvlSuivant();
			
			// augmentation du rayon de portee
			rayonPortee = getRayonPorteeLvlSuivant();
			
			// raccourcissement du temps de preparation du tire
			cadenceTir	= getCadenceTirLvlSuivant();
		
			niveau++;
		}
	}

	public void tirer(Creature creature)
	{
	    jeu.ajouterAnimation(
	            new Fleche(jeu,this,creature,degats));
	}

	public Tour getCopieOriginale()
	{
		return new TourAntiAerienne();
	}

	public boolean peutEncoreEtreAmelioree()
	{
		return niveau < NIVEAU_MAX;
	}

    @Override
    public double getCadenceTirLvlSuivant()
    {
        return cadenceTir * 1.2;
    }

    @Override
    public long getDegatsLvlSuivant()
    {
        return (long) (degats * 1.5);
    }

    @Override
    public double getRayonPorteeLvlSuivant()
    {
        return rayonPortee + 10;
    }
}
