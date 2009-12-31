package models.terrains;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

import models.creatures.CarapaceKoopa;
import models.creatures.GrandeFlame;
import models.creatures.PetiteFlame;
import models.creatures.Smiley;
import models.creatures.Nuage;
import models.creatures.VagueDeCreatures;

/**
 * Classe de gestion d'un terrain en spiral.
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
public class Spiral extends Terrain
{
    public final static Image IMAGE_DE_FOND;
    public final static Image IMAGE_MENU;
    public final static String NOM = "Spiral";
    
    public static final VagueDeCreatures[] vagues = 
    {
        new VagueDeCreatures(30, new PetiteFlame(100,4,300),50,false),
        new VagueDeCreatures(10, new Smiley(100,4,10),2000,false),
        new VagueDeCreatures(5, new CarapaceKoopa(300,10,10),2000,false),
        new VagueDeCreatures(10, new Nuage(100,6,10),2000,false),
        new VagueDeCreatures(20, new Smiley(100,4,30),500,false),
        new VagueDeCreatures(10, new PetiteFlame(600,20,20),1000,false),
        new VagueDeCreatures(1, new GrandeFlame(10000,40,10),2000,false,"Boss")
    };
    
    
    static
    {
        IMAGE_MENU    = Toolkit.getDefaultToolkit().getImage(
                                              "img/cartes/menu_principal/spirale.png");
    	IMAGE_DE_FOND = Toolkit.getDefaultToolkit().getImage("img/cartes/spirale.png");
    }
	
	/**
	 * Constructeur du terrain dans le desert
	 */
	public Spiral()
	{
		super(  480,  // largeur
                500,  // hauteur
                100,  // nbPiecesOrInitiales
                20,   // nbViesInitiales
                0,    // positionMaillageX
                0,    // positionMaillageY
                540,  // largeurMaillage
                500,  // hauteurMaillage
                IMAGE_DE_FOND, // imageDeFond
                NOM,  // nom
                new Rectangle(500,40,20,80),   // zoneDepart
                new Rectangle(320,280,20,60)   // zoneArrivee
          );
		
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
	}
	
    /**
     * Permet de recuperer la vague suivante
     * @return la vague suivante
     */
    VagueDeCreatures getVagueDeCreaturesSuivante()
	{
    	if (indiceVagueCourante < vagues.length)
    		return vagues[indiceVagueCourante];
    	else
    		return new VagueDeCreatures(10, 
    			new Smiley(indiceVagueCourante*100,indiceVagueCourante,indiceVagueCourante)
    		,2000,false);
	}
}
