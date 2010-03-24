package models.terrains;
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import models.creatures.*;

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
    public final static File  FICHIER_MUSIQUE_DE_FOND;
    public final static String NOM = "ElementTD";

    static
    {
        FICHIER_MUSIQUE_DE_FOND = new File("snd/blizzard/Human_I_(Fanfare).mp3");
        
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
                new Color(197,148,90), // couleur de fond
                new Color(91,123,43),  // couleur des murs
                IMAGE_DE_FOND, // imageDeFond
                NOM,  // nom
                new Rectangle(110, 0, 80, 20),  // zoneDepart
                new Rectangle(230, 0, 80, 20)   // zoneArrivee
          );
 
        fichierMusiqueDAmbiance = FICHIER_MUSIQUE_DE_FOND;
        
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
    public VagueDeCreatures getVagueDeCreaturesSuivante()
	{
        return VagueDeCreatures.genererVagueStandard(indiceVagueCourante);
	}
}
