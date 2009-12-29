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
public class Desert extends Terrain
{
	public final static Image IMAGE_DE_FOND;
	public final static Image IMAGE_MENU;
    
    public static final VagueDeCreatures[] vagues = 
    {
        new VagueDeCreatures(5, new Smiley(100,4,10),2000,false),
        new VagueDeCreatures(10, new Smiley(100,4,10),2000,false),
        new VagueDeCreatures(5, new CarapaceKoopa(300,10,10),2000,false),
        new VagueDeCreatures(10, new Nuage(100,6,10),2000,false),
        new VagueDeCreatures(20, new Smiley(100,4,30),500,false),
        new VagueDeCreatures(10, new PetiteFlame(600,20,20),1000,false),
        new VagueDeCreatures(1, new GrandeFlame(10000,40,10),2000,false,"Boss")
    };
	
    static
    {
        IMAGE_MENU = Toolkit.getDefaultToolkit().getImage(
                                              "img/cartes/menu_principal/objectif.png");
    	IMAGE_DE_FOND = Toolkit.getDefaultToolkit().getImage("img/cartes/objectif.png");
    }
	
	/**
	 * Constructeur du terrain dans le desert
	 */
	public Desert()
	{
		super(  500,      // largeur
                500,      // hauteur
                10000,    // nbPiecesOrInitiales
                20,       // nbViesInitiales
                -10,      // positionMaillageX
                0,        // positionMaillageY
                560,      // largeurMaillage
                500,      // hauteurMaillage
                IMAGE_DE_FOND, // imageDeFond
                "Desert",  // nom
                new Rectangle(520,40,20,60), // zoneDepart
                new Rectangle(0,400,20,60)   // zoneArrivee
          );
		
		
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
