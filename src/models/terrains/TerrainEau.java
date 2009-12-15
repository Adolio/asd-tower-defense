package models.terrains;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

import models.creatures.*;

/**
 * Classe de gestion du terrain TerrainEau.
 * 
 * Cette classe hérite de la classe Terrain de base.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 14 décembre 2009
 * @since jdk1.6.0_16
 * @see Terrain
 */
public class TerrainEau extends Terrain
{
    
	public final static Image IMAGE_DE_FOND;
	public final static Image IMAGE_MENU;
    
    static
    {
        IMAGE_MENU    = Toolkit.getDefaultToolkit().getImage(
                                              "img/cartes/menu_principal/water.png");
    	IMAGE_DE_FOND = Toolkit.getDefaultToolkit().getImage("img/cartes/water.png");
    }
	
    /**
     * Constructeur d'un terrain TerrainEau.
     */
    public TerrainEau () {
        super(500, 500, 100, 
              0, 0, 540, 500, IMAGE_DE_FOND, 
              new Rectangle(0, 30, 20, 80),
              new Rectangle(480, 390, 20, 80)
        );
    
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
        
        /*
         * Configuration des vagues de créatures.
         */
        ajouterVague(new VagueDeCreatures(
                5, new Creature1(100,4,10),"Creatures terrestres faibles"));
        ajouterVague(new VagueDeCreatures(
                10, new Creature1(100,4,10),"Creature terrestres faibles"));
        ajouterVague(new VagueDeCreatures(
                5, new CarapaceKoopa(300,10,10),"Creature terrestres moyennes"));
        ajouterVague(new VagueDeCreatures(
                20, new Creature1(100,4,30),"Creature terrestres rapides"));
        ajouterVague(new VagueDeCreatures(
                10, new Creature1(600,20,20),"Creature terrestres resistantes"));
        ajouterVague(new VagueDeCreatures(
                1, new CarapaceKoopa(10000,40,10),"Boss : Très résistant"));
    }
    
}
