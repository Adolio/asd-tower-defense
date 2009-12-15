package models.terrains;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

import models.creatures.CarapaceKoopa;
import models.creatures.Creature1;
import models.creatures.VagueDeCreatures;

/**
 * Classe de gestion d'un terrain dans le desert.
 * 
 * Cette classe herite de la classe Terrain de base.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Terrain
 */
public class Objectif extends Terrain
{
	public final static Image IMAGE_DE_FOND;
	public final static Image IMAGE_MENU;
    
    static
    {
        IMAGE_MENU = Toolkit.getDefaultToolkit().getImage(
                                              "img/cartes/menu_principal/objectif.png");
    	IMAGE_DE_FOND = Toolkit.getDefaultToolkit().getImage("img/cartes/objectif.png");
    }
	
	/**
	 * Constructeur du terrain dans le desert
	 */
	public Objectif()
	{
		super(500, 500, 200, 
			  -10,0,560,500,
			  IMAGE_DE_FOND, 
			  new Rectangle(520,40,20,60),
			  new Rectangle(0,400,20,60));
		
		// murs entourant le terrain
		ajouterMur(new Rectangle(0,0,20,380)); 		// gauche
		ajouterMur(new Rectangle(0,0,500,20)); 		// haut
		ajouterMur(new Rectangle(0,480,500,20)); 	// bas
		ajouterMur(new Rectangle(480,120,20,380)); 	// droit
		
		// murs formant les coins
		// haut - gauche
		ajouterMur(new Rectangle(120,120,60,20));
		ajouterMur(new Rectangle(120,120,20,60));
		
		// haut - droite
		ajouterMur(new Rectangle(320,120,60,20));
		ajouterMur(new Rectangle(360,120,20,60));
		
		// bas - gauche
		ajouterMur(new Rectangle(120,320,20,60));
		ajouterMur(new Rectangle(120,360,60,20));
		
		// bas - droite
		ajouterMur(new Rectangle(360,320,20,60));
		ajouterMur(new Rectangle(320,360,60,20));
		
		
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
