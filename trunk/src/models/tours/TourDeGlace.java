package models.tours;

import i18n.Langue;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import models.attaques.BouleDeGlace;
import models.creatures.Creature;


/**
 * Classe de gestion d'une tour de glace.
 * <p>
 * La tour de glace est une tour qui est rapide et qui ralenti les creatures.
 * Cette tour attaque tous types de creatures
 * 
 * @author Aurélien Da Campo
 * @version 1.1 | mai 2010
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
    private static final double COEFF_RALENTISSMENT = 0.4; // perd 40% de sa vitesse
    
    public static final int PRIX_ACHAT = 50;
    private static final String DESCRIPTION = Langue.getTexte(Langue.ID_TXT_DESC_TOUR_GLACE);

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
              "Glace",          // nom
              PRIX_ACHAT,       // prix achat
              20,               // degats
              50,               // rayon de portee
              2.0,                // cadence de tir (tirs / sec.)
              Tour.TYPE_TERRESTRE_ET_AIR, // type
              IMAGE,            // image sur terrain
              ICONE);           // icone pour bouton      
    
        description = DESCRIPTION;
    }
    
    public void ameliorer()
    {
        if(peutEncoreEtreAmelioree())
        {
            // le prix total est ajouté du prix d'achat de la tour
            prixTotal   += prixAchat;
            
            // augmentation du prix du prochain niveau
            prixAchat   *= 2;
            
            // augmentation des degats
            degats      = getDegatsLvlSuivant();
            
            // augmentation du rayon de portee
            rayonPortee = getRayonPorteeLvlSuivant();
            
            // raccourcissement du temps de preparation du tire
            setCadenceTir(getCadenceTirLvlSuivant());
        
            niveau++;
        }
    }

    public void tirer(Creature creature)
    {
        
        jeu.ajouterAnimation(new BouleDeGlace(jeu,this,creature,degats,
                                                  COEFF_RALENTISSMENT));
    }

    public Tour getCopieOriginale()
    {
        return new TourDeGlace();
    }

    public boolean peutEncoreEtreAmelioree()
    {
        return niveau < NIVEAU_MAX;
    }
    
    @Override
    public double getCadenceTirLvlSuivant()
    {
        return getCadenceTir() * 1.2;
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
