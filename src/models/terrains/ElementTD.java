package models.terrains;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

import models.creatures.*;
import models.outils.Musique;

/**
 * Classe de gestion du fameux terrain Element TD repris de chez Blizzard.
 * 
 * Cette classe herite de la classe Terrain de base.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 13 decembre 2009
 * @since jdk1.6.0_16
 * @see Terrain
 */
public class ElementTD extends Terrain
{
    public final static Image IMAGE_DE_FOND;
    public final static Image IMAGE_MENU;
    public final static Musique MUSIQUE_DE_FOND;
    public final static String NOM = "ElementTD";
    
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
        MUSIQUE_DE_FOND = new Musique("snd/blizzard/Human_I_(Fanfare).mp3");
        
        IMAGE_MENU    = Toolkit.getDefaultToolkit().getImage(
                                          "img/cartes/menu_principal/elementTD.png");
    	IMAGE_DE_FOND = Toolkit.getDefaultToolkit().getImage(
    	                                                 "img/cartes/elementTD.png");
    }
	
    /**
     * Constructeur d'un terrain ElementTD selon la celebre map de Blizzard.
     */
    public ElementTD () 
    {
        super(  480,  // largeur
                500,  // hauteur
                100,  // nbPiecesOrInitiales
                20,   // nbViesInitiales
                0,    // positionMaillageX
                0,    // positionMaillageY
                480,  // largeurMaillage
                540,  // hauteurMaillage
                IMAGE_DE_FOND, // imageDeFond
                NOM,  // nom
                new Rectangle(110, 0, 80, 20),  // zoneDepart
                new Rectangle(230, 0, 80, 20)   // zoneArrivee
          );
 
        musiqueDAmbiance = MUSIQUE_DE_FOND;
        
        /*
         * Définition des murs du labyrinthe.
         */
        ajouterMur(new Rectangle(20, 0, 80, 20));
        ajouterMur(new Rectangle(0, 0, 20, 500));
        ajouterMur(new Rectangle(20, 480, 440, 20));
        ajouterMur(new Rectangle(460, 0, 20, 500));
        ajouterMur(new Rectangle(320, 0, 140, 20));
        
        ajouterMur(new Rectangle(200, -40, 20, 140));
        
        ajouterMur(new Rectangle(120, 100, 240, 20));
        ajouterMur(new Rectangle(120, 120, 20, 20));
        ajouterMur(new Rectangle(340, 120, 20, 260));
        ajouterMur(new Rectangle(120, 360, 220, 20));
        ajouterMur(new Rectangle(20, 240, 220, 20));
        ajouterMur(new Rectangle(220, 220, 20, 20)); 
    }

    /**
     * Permet de recuperer la vague suivante
     * @return la vague suivante
     */
    VagueDeCreatures getVagueDeCreaturesSuivante()
	{
        // TODO
        int indiceVagueCourante = this.indiceVagueCourante + 1;
           
        /*if (indiceVagueCourante < vagues.length)
    		return vagues[indiceVagueCourante];
    	else*/
    	{
    	    //int n = tirerNombrePseudoAleatoire(0,10)
    	    int uniteVague = indiceVagueCourante % 10;
    	   
    	    // 5 normales
    	    if(uniteVague == 1)
        	    return new VagueDeCreatures(5, 
        			new Smiley(indiceVagueCourante*100,6,10),2000,false);
    	    
    	    // 10 normales
    	    else if(uniteVague == 2)
                return new VagueDeCreatures(10, 
                        new Pokey(indiceVagueCourante*100,6,10),2000,false);
    	    
    	    // 10 volantes
            else if(uniteVague == 3)
                return new VagueDeCreatures(10,
                        new Boo(indiceVagueCourante*100,10,20),1000,false);
    	    
    	    // 10 resistantes
    	    else if(uniteVague == 4)
                return new VagueDeCreatures(10, 
                        new CarapaceKoopa(indiceVagueCourante*150,8,10),2000,false);
    	    
    	    // 10 rapides
    	    else if(uniteVague == 5)
                return new VagueDeCreatures(10, 
                        new Smiley(indiceVagueCourante*80,8,30),500,false);

    	    // 20 normales
    	    else if(uniteVague == 6)
                return new VagueDeCreatures(10, 
                        new Smiley(indiceVagueCourante*100,6,10),2000,false);
    	    
    	    // 15 resistantes
    	    else if(uniteVague == 7)
                return new VagueDeCreatures(15, 
                        new Thwomp(indiceVagueCourante*150,8,10),2000,false);
    	    
    	    // 10 volantes
            else if(uniteVague == 8)
                return new VagueDeCreatures(10,
                        new Boo(indiceVagueCourante*100,10,20),1000,false);
    	    
    	    // 5 pre-boss
    	    else if(uniteVague == 9)
                return new VagueDeCreatures(3, 
                        new PetiteFlame(indiceVagueCourante*200,indiceVagueCourante,10),
                        2000,false);
    	    
    	    // boss
    	    else
                return new VagueDeCreatures(1, 
                        new GrandeFlame(indiceVagueCourante*500,indiceVagueCourante*5,10)
                    ,2000,false);
    	}
	}
    
}
