package models.tours;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import models.creatures.Creature;


/**
 * Classe de gestion d'une tour de feu.
 * <p>
 * La tour de feu est une tour qui est lente
 * mais qui fait de gros degats de zone.
 * Cette tour attaque tous types de creatures
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
    public static final Image ICONE;
    public static final int NIVEAU_MAX = 5;
    private static final double RAYON_IMPACT = 20.0;
    private static final String DESCRIPTION = 
        "La tour de feu est une tour qui est lente " +
        "mais qui fait de gros dégâts de zone. " +
        "Cette tour attaque tous types de creatures";
    
    static
    {
        COULEUR = new Color(220,0,0);
        IMAGE   = Toolkit.getDefaultToolkit().getImage("img/tours/tourDeFeu.png");
        ICONE   = Toolkit.getDefaultToolkit().getImage("img/tours/icone_tourDeFeu.png");
    }
    
    public TourDeFeu()
    {
        super(0,                // x
              0,                // y
              20,               // largeur
              20,               // hauteur
              COULEUR,          // couleur de fond
              "Feu",            // nom
              180,              // prix achat
              100,              // degats
              40,               // rayon de portee
              1,                // cadence de tir (tirs / sec.)
              Tour.TYPE_TERRESTRE_ET_AIR, // type
              IMAGE,            // image sur terrain
              ICONE);           // icone pour bouton
    
        description = DESCRIPTION;
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
        blesserCreaturesDansZoneImpact(new Point((int)creature.getCenterX(),
                                                 (int)creature.getCenterY())
                                       ,RAYON_IMPACT);
    }

    public Tour getCopieOriginale()
    {
        return new TourDeFeu();
    }

    public boolean peutEncoreEtreAmelioree()
    {
        return niveau <= NIVEAU_MAX;
    }
}
