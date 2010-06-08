package models.tours;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import models.creatures.Creature;

/**
 * Classe de gestion d'une tour a canon.
 * <p>
 * Le tour canon est une tour lente avec de bons degats de zone. 
 * De plus, elle n'attaque que les creatures terrestres
 * 
 * @author Romain Poulain
 * @author Aurélien Da Campo
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Tour
 */
public class TourBalistique extends Tour
{
	private static final long serialVersionUID = 1L;
	public static final Color COULEUR;
	public static final Image IMAGE;
	public static final Image ICONE;
	public static final int NIVEAU_MAX = 5;
    public static final int PRIX_ACHAT = 15;
    private static final String DESCRIPTION = 
        "La tour canon est une tour avec de bons dégâts mais lente. " +
        "Cette dernière n'attaque que les créatures terrestres";
	
	static
	{
	    COULEUR = new Color(64,64,64);
		IMAGE 	= Toolkit.getDefaultToolkit().getImage("img/tours/tourCanon.png");
		ICONE   = Toolkit.getDefaultToolkit().getImage("img/tours/icone_tourCanon.png");
	}
	
	/**
     * Constructeur de la tour.
     */
	public TourBalistique()
	{
		super(0, 				// x
			  0, 				// y
			  20, 				// largeur
		      20, 				// hauteur
			  COULEUR,			// couleur de fond
			  "Canon",	        // nom
			  PRIX_ACHAT,		// prix achat
			  18,				// degats
			  40,				// rayon de portee
			  1,                // cadence de tir (tirs / sec.)
              Tour.TYPE_TERRESTRE, // type
              IMAGE,            // image sur terrain
              ICONE);           // icone pour bouton			
		
		description = DESCRIPTION;
	}

	public void ameliorer()
	{
        if(peutEncoreEtreAmelioree())
        {
    	    // le prix total est ajouté du prix d'achat de la tour
    		prixTotal 	+= prixAchat;
    		
    		prixAchat 	*= 2;	// + 100%
    		
    		// augmentation des degats
            degats      = getDegatsLvlSuivant();
            
            // augmentation du rayon de portee
            rayonPortee = getRayonPorteeLvlSuivant();
            
            // raccourcissement du temps de preparation du tire
            cadenceTir  = getCadenceTirLvlSuivant();
    		
    		niveau++;
        }
	}
	
	public void tirer(Creature creature)
	{
	    //jeu.getGestionnaireAnimations().ajouterAnimation(new BouleBalistique(jeu,this,creature,degats,RAYON_IMPACT));
	}

	public Tour getCopieOriginale()
	{
		return new TourBalistique();
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
