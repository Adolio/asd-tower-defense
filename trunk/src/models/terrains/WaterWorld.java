package models.terrains;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

import models.creatures.*;
import models.outils.Musique;

/**
 * Classe de gestion du terrain WaterWorld.
 * 
 * Cette classe herite de la classe Terrain de base.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 14 décembre 2009
 * @since jdk1.6.0_16
 * @see Terrain
 */
public class WaterWorld extends Terrain
{
    
	public static final Image IMAGE_DE_FOND;
	public static final Image IMAGE_MENU;
	public final static Musique MUSIQUE_DE_FOND;
	public final static String NOM = "WaterWorld";
	
    static
    {
        MUSIQUE_DE_FOND = new Musique("snd/blizzard/Human_II_(High Seas).mp3");
        
        IMAGE_MENU    = Toolkit.getDefaultToolkit().getImage(
                                              "img/cartes/menu_principal/water.png");
    	IMAGE_DE_FOND = Toolkit.getDefaultToolkit().getImage("img/cartes/water.png");
    }

    /**
     * Constructeur d'un terrain TerrainEau.
     */
    public WaterWorld () {
        super(500,  // largeur
              500,  // hauteur
              100,  // nbPiecesOrInitiales
              20,   // nbViesInitiales
              0,    // positionMaillageX
              0,    // positionMaillageY
              540,  // largeurMaillage
              500,  // hauteurMaillage
              IMAGE_DE_FOND, // imageDeFond
              NOM,  // nom
              new Rectangle(0, 30, 20, 80),     // zoneDepart
              new Rectangle(480, 390, 20, 80)   // zoneArrivee
        );

        musiqueDAmbiance = MUSIQUE_DE_FOND;
        
        /*
         * Définition des murs du terrain.
         */
        ajouterMur(new Rectangle(0, 0, 220, 20));
        ajouterMur(new Rectangle(220, 0, 60, 90));
        ajouterMur(new Rectangle(280, 0, 220, 20));
        ajouterMur(new Rectangle(480, 20, 20, 200));
        ajouterMur(new Rectangle(410, 220, 90, 60));
        ajouterMur(new Rectangle(480, 280, 20, 100));
        ajouterMur(new Rectangle(280, 480, 220, 20));
        ajouterMur(new Rectangle(220, 410, 60, 90));
        ajouterMur(new Rectangle(0, 480, 220, 20));
        ajouterMur(new Rectangle(0, 280, 20, 200));
        ajouterMur(new Rectangle(0, 220, 90, 60));
        ajouterMur(new Rectangle(0, 120, 20, 100));
        ajouterMur(new Rectangle(130, 220, 240, 60));
        ajouterMur(new Rectangle(220, 130, 60, 90));
        ajouterMur(new Rectangle(220, 280, 60, 90));
    }
    
    /**
     * Permet de recuperer la vague suivante
     * @return la vague suivante
     */
    VagueDeCreatures getVagueDeCreaturesSuivante()
	{
        return genererVagueStandard();
	}
}
