package models.tours;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

import models.creatures.Creature;
import models.outils.Musique;

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
public class TourArcher extends Tour
{
	private static final long serialVersionUID = 1L;
	public static final Color COULEUR;
	public static final Image IMAGE;
	public static final int NIVEAU_MAX = 5;
    public static final Musique SON_FLECHE;
    
    static
    {
        SON_FLECHE = new Musique("snd/arc.mp3");
		COULEUR = new Color(128,64,32);
		IMAGE 	= Toolkit.getDefaultToolkit().getImage("img/tours/basic_tower_1.png");
	}
	
    public TourArcher()
	{
	    super(0,                // x
			  0, 				// y
			  20, 				// largeur
			  20, 				// hauteur
			  COULEUR,			// couleur de fond
			  "Archer",	// nom
			  10,                // prix achat
			  5,                 // degats
			  50,                // rayon de portee
			  2,
			  Tour.TYPE_TERRESTRE_ET_AIR,
			  IMAGE);		
	
		description = "La tour d'archer est une tour qui est très rapide," +
					  " mais elle fait peu de dégâts. ";
	}
	
	public void ameliorer()
	{
		// que six niveau pour la tour
		if(niveau <= 6)
		{
			// le prix total est ajouté du prix d'achat de la tour
			prixTotal 	+= prixAchat;
			
			// augmentation du prix du prochain niveau
			prixAchat 	*= 2;
			
			// augmentation des degats
			degats    	*= 1.5;
			
			// augmentation du rayon de portee
			rayonPortee += 10;
			
			// raccourcissement du temps de preparation du tire
			cadenceTir	*= 1.2;
		
			niveau++;
		}
	}

	public void tirer(Creature creature)
	{
	    //SON_FLECHE.lire(1);
	    
	    // TODO
		// terrain.ajouteTire(new bouleDeFeu(this,creature));
		creature.blesser(degats);
	}

	public Tour getCopieOriginale()
	{
		return new TourArcher();
	}

	public boolean peutEncoreEtreAmelioree()
	{
		return niveau <= NIVEAU_MAX;
	}
}
