package models.tours;

import i18n.Langue;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import models.attaques.ArcElectrique;
import models.creatures.Creature;


/**
 * Classe de gestion d'une tour électrique.
 * <p>
 * 
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @author Romain Poulain
 * @version 1.0 | 5 mai 2009
 * @since jdk1.6.0_16
 * @see Tour
 */
public class TourElectrique extends Tour
{
    private static final long serialVersionUID = 1L;
    public static final Color COULEUR;
    public static final Image IMAGE;
    public static final Image ICONE;
    public static final int NIVEAU_MAX = 5;
    public static final int PRIX_ACHAT = 120;
    private static final String DESCRIPTION = Langue.getTexte(Langue.ID_TXT_DESC_TOUR_ELECTRIQUE); 

    
    static
    {
        COULEUR = new Color(211,135,225);
        IMAGE   = Toolkit.getDefaultToolkit().getImage("img/tours/tourElectrique.png");
        ICONE   = Toolkit.getDefaultToolkit().getImage("img/tours/icone_tourElectrique.png");
    }
    
    public TourElectrique()
    {
        super(0,                // x
              0,                // y
              20,               // largeur
              20,               // hauteur
              COULEUR,          // couleur de fond
              Langue.getTexte(Langue.ID_TXT_NOM_TOUR_ELECTRIQUE),     // nom
              PRIX_ACHAT,       // prix achat
              120,              // degats
              70,               // rayon de portee
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
        jeu.ajouterAnimation(new ArcElectrique(jeu,this,creature,degats));
    }

    public Tour getCopieOriginale()
    {
        return new TourElectrique();
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
