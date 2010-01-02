package models.tours;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import models.attaques.BouleDeTerre;
import models.creatures.Creature;

/**
 * Classe de gestion d'une tour de terre.
 * <p>
 * La tour de terre est une tour lente qui fait enormement de degats
 * et qui a une tres grande portee. Malheureusement, elle n'attaque
 * que les creatures terrestre.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Tour
 */
public class TourDeTerre extends Tour
{
    private static final long serialVersionUID = 1L;
    public static final Color COULEUR;
    public static final Image IMAGE;
    public static final Image ICONE;
    public static final int NIVEAU_MAX = 5;
    public static final int PRIX_ACHAT = 250;
    private static final double RAYON_IMPACT = 30;
    
    private static final String DESCRIPTION = 
        "La tour de terre est une tour lente qui fait enormement de degats" +
        " et qui a une très grande portée. Malheureusement, elle n'attaque " +
        "que les créatures terrestre.";
    
    static
    {
        COULEUR = new Color(128,0,0);
        IMAGE   = Toolkit.getDefaultToolkit().getImage("img/tours/tourDeTerre.png");
        ICONE   = Toolkit.getDefaultToolkit().getImage("img/tours/icone_tourDeTerre.png");
    }
    
    public TourDeTerre()
    {
        super(0,                // x
              0,                // y
              20,               // largeur
              20,               // hauteur
              COULEUR,          // couleur de fond
              "Terre",          // nom
              PRIX_ACHAT,       // prix achat
              200,              // degats
              150,              // rayon de portee
              0.5,              // cadence de tir (tirs / sec.)
              Tour.TYPE_TERRESTRE,// type
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
        terrain.ajouterAnimation(new BouleDeTerre(terrain,this,creature,degats,RAYON_IMPACT));
    }

    public Tour getCopieOriginale()
    {
        return new TourDeTerre();
    }

    public boolean peutEncoreEtreAmelioree()
    {
        return niveau <= NIVEAU_MAX;
    }
}
