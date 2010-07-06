package models.terrains;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;

import models.jeu.Jeu;
import models.jeu.ModeDeJeu;
import models.joueurs.EmplacementJoueur;
import models.joueurs.Equipe;

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
    private static final long serialVersionUID = 1L;
    public final static Image IMAGE_DE_FOND;
    public final static Image IMAGE_MENU;
    public final static File  FICHIER_MUSIQUE_DE_FOND;
    public final static String NOM = "Spiral";
 
    static
    {
        FICHIER_MUSIQUE_DE_FOND = new File("snd/Defnael/Tork.mp3");
        
        IMAGE_MENU    = Toolkit.getDefaultToolkit().getImage(
                                              "img/cartes/menu_principal/spirale.png");
    	IMAGE_DE_FOND = Toolkit.getDefaultToolkit().getImage("img/cartes/spirale.png");
    }
	
	/**
	 * Constructeur du terrain dans le desert
	 */
	public Spiral(Jeu jeu)
	{
		super(  jeu,
		        480,  // largeur
                500,  // hauteur
                100,  // nbPiecesOrInitiales
                20,   // nbViesInitiales
                0,    // positionMaillageX
                0,    // positionMaillageY
                540,  // largeurMaillage
                500,  // hauteurMaillage
                ModeDeJeu.MODE_SOLO, // mode de jeu
                new Color(150,150,150), // couleur de fond
                new Color(140,120,75),  // couleur des murs
                IMAGE_DE_FOND, // imageDeFond
                NOM  // nom
          );
		
		// Création des équipes
		Equipe e = new Equipe(1,"Equipe par defaut",Color.BLACK);
        e.ajouterZoneDepartCreatures(new Rectangle(500,40,20,80));
        e.setZoneArriveeCreatures(new Rectangle(300,290,40,40));
        e.ajouterEmplacementJoueur(new EmplacementJoueur(1,new Rectangle(0,0,480,500)));
        equipes.add(e);
		
        fichierMusiqueDAmbiance = FICHIER_MUSIQUE_DE_FOND;
        
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
		ajouterMur(new Rectangle(460,140,80,360));  // 4
		ajouterMur(new Rectangle(140,140,340,20));  // 5
		ajouterMur(new Rectangle(120,140,20,240));	// 6
		ajouterMur(new Rectangle(140,360,200,20));	// 7
		ajouterMur(new Rectangle(340,240,20,140));	// 8
		ajouterMur(new Rectangle(240,240,100,20));	// 9
	}
}
