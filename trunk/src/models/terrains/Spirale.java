package models.terrains;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

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
public class Spirale extends Terrain
{
    public final static Image IMAGE_DE_FOND;
    public final static Image IMAGE_MENU;
    
    static
    {
        IMAGE_MENU    = Toolkit.getDefaultToolkit().getImage(
                                              "img/cartes/menu_principal/spirale.png");
    	IMAGE_DE_FOND = Toolkit.getDefaultToolkit().getImage("img/cartes/spirale.png");
    }
	
	/**
	 * Constructeur du terrain dans le desert
	 */
	public Spirale()
	{
		super(480, 500, 100, 
			  0,0,540,500,IMAGE_DE_FOND, 
			  new Rectangle(500,40,20,80),
			  new Rectangle(320,280,20,60));
		
		/* definition des murs du labyrinthe :
		 
		 		 1
		|------------------
		|
		|           5
		|	 |-------------
		|	 |	   9	  |
	  2 |	6|  -----|	  |
		|	 |		 | 8  | 4
		|    |-------|	  |
		|		 7		  |
		|-----------------|
				3
		*/
	
		ajouterMur(new Rectangle(20,0,460,20)); 	// 1
		ajouterMur(new Rectangle(0,0,20,500));	 	// 2	
		ajouterMur(new Rectangle(20,480,440,20)); 	// 3
		ajouterMur(new Rectangle(460,140,20,360));  // 4
		ajouterMur(new Rectangle(140,140,340,20));  // 5
		ajouterMur(new Rectangle(120,140,20,240));	// 6
		ajouterMur(new Rectangle(140,360,200,20));	// 7
		ajouterMur(new Rectangle(340,240,20,140));	// 8
		ajouterMur(new Rectangle(240,240,100,20));	// 9
		
		// configuration des vagues de creatures
		ajouterVague(new VagueDeCreatures(5, new Creature1(100,4,10),"Creatures terrestres faibles"));
		ajouterVague(new VagueDeCreatures(10, new Creature1(100,4,10),"Creature terrestres faibles"));
		ajouterVague(new VagueDeCreatures(5, new Creature1(300,10,10),"Creature terrestres moyennes"));
		ajouterVague(new VagueDeCreatures(20, new Creature1(100,4,30),"Creature terrestres rapides"));
		ajouterVague(new VagueDeCreatures(10, new Creature1(600,20,20),"Creature terrestres resistantes"));
		ajouterVague(new VagueDeCreatures(1, new Creature1(10000,40,10),"Boss : Très résistant"));
	}
}
