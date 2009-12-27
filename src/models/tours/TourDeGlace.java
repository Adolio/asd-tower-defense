package models.tours;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
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
    public static final Color COULEUR;
    public static final Image IMAGE;
    public static final Image ICONE;
    public static final int NIVEAU_MAX = 5;
    
    static
    {
        COULEUR = new Color(0,200,200);
        IMAGE   = Toolkit.getDefaultToolkit().getImage("img/tours/tourDeGlace.png");
        ICONE   = Toolkit.getDefaultToolkit().getImage("img/tours/icone_tourDeGlace.png");
    }
    
    public TourDeGlace()
    {
        super(0,                // x
              0,                // y
              20,               // largeur
              20,               // hauteur
              COULEUR,          // couleur de fond
              "Glace", // nom
              50,                // prix achat
              50,                 // degats
              50,                // rayon de portee
              2,
              Tour.TYPE_TERRESTRE_ET_AIR,
              IMAGE,
              ICONE);       
    
        description = "La tour de glace est une tour qui est rapide," +
                      " qui ralenti les creatures. ";
    }
    
    public void ameliorer()
    {
        // que six niveau pour la tour
        if(niveau <= 6)
        {
            // le prix total est ajouté du prix d'achat de la tour
            prixTotal   += prixAchat;
            
            // augmentation du prix du prochain niveau
            prixAchat   *= 2;
            
            // augmentation des degats
            degats      *= 1.5;
            
            // augmentation du rayon de portee
            rayonPortee += 10;
            
            // raccourcissement du temps de preparation du tire
            cadenceTir  *= 1.2;
        
            niveau++;
        }
    }

    public void tirer(Creature creature)
    {
        creature.blesser(degats);
    }

    public Tour getCopieOriginale()
    {
        return new TourDeGlace();
    }

    public boolean peutEncoreEtreAmelioree()
    {
        return niveau <= NIVEAU_MAX;
    }
}
