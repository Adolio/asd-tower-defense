package models.terrains;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import models.jeu.Jeu;
import models.jeu.ModeDeJeu;
import models.joueurs.EmplacementJoueur;
import models.joueurs.Equipe;

/**
 * Classe de gestion du fameux terrain Element TD en Versus 4 joueurs
 * 
 * Cette classe herite de la classe Terrain de base.
 * 
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | mai 2010
 * @since jdk1.6.0_16
 * @see Terrain
 */
public class ElementTD_Versus_4 extends Terrain
{
    private static final long serialVersionUID = 1L;
    public final static Image IMAGE_DE_FOND;
    public final static Image IMAGE_MENU;
    public final static File  FICHIER_MUSIQUE_DE_FOND;
    public final static String NOM = "Element TD Versus";

    static
    {
        FICHIER_MUSIQUE_DE_FOND = new File("snd/Filippo Vicarelli/The War Begins.mp3");
        
        IMAGE_MENU    = Toolkit.getDefaultToolkit().getImage(
                                          "img/cartes/menu_principal/elementTD.png");
    	IMAGE_DE_FOND = Toolkit.getDefaultToolkit().getImage(
    	                                                 "img/cartes/elementTDVersus4.jpg");
    }
	
    /**
     * Constructeur d'un terrain ElementTD selon la celebre map de Blizzard.
     */
    public ElementTD_Versus_4 (Jeu jeu) 
    {
        super(  jeu,
                960,  // largeur
                1000,  // hauteur
                100,  // nbPiecesOrInitiales
                20,   // nbViesInitiales
                0,    // positionMaillageX
                0,    // positionMaillageY
                960,  // largeurMaillage
                1000,  // hauteurMaillage
                ModeDeJeu.MODE_VERSUS, // mode de jeu
                new Color(197,148,90), // couleur de fond
                new Color(91,123,43),  // couleur des murs
                IMAGE_DE_FOND, // imageDeFond
                NOM  // nom
          );
 
        
        taillePanelTerrain = new Dimension(480,500);
        
        
        // Création des équipes
        Equipe e1 = new Equipe(1,"Les verts",Color.GREEN);
        e1.ajouterZoneDepartCreatures(new Rectangle(110, 0, 80, 20));
        e1.setZoneArriveeCreatures(new Rectangle(230, 0, 80, 20));
        e1.ajouterEmplacementJoueur(new EmplacementJoueur(1,new Rectangle(0,0,480,500),Color.GREEN));
        equipes.add(e1);
        
        Equipe e2 = new Equipe(2,"Les rouges",Color.RED);
        e2.ajouterZoneDepartCreatures(new Rectangle(590, 0, 80, 20));
        e2.setZoneArriveeCreatures(new Rectangle(710, 0, 80, 20));
        e2.ajouterEmplacementJoueur(new EmplacementJoueur(2,new Rectangle(480,0,480,500),Color.RED));
        equipes.add(e2);
        
        Equipe e3 = new Equipe(3,"Les bleus",Color.BLUE);
        e3.ajouterZoneDepartCreatures(new Rectangle(110, 500, 80, 20));
        e3.setZoneArriveeCreatures(new Rectangle(230, 500, 80, 20));
        e3.ajouterEmplacementJoueur(new EmplacementJoueur(3,new Rectangle(0,500,480,500),Color.BLUE));
        equipes.add(e3);
        
        Equipe e4 = new Equipe(4,"Les jaunes",Color.YELLOW);
        e4.ajouterZoneDepartCreatures(new Rectangle(590,500, 80, 20));
        e4.setZoneArriveeCreatures(new Rectangle(710, 500, 80, 20));
        e4.ajouterEmplacementJoueur(new EmplacementJoueur(4,new Rectangle(480,500,480,500),Color.YELLOW));
        equipes.add(e4);
        
        
        // musique
        fichierMusiqueDAmbiance = FICHIER_MUSIQUE_DE_FOND;
         
        // création des murs
        creerMurs(0,0);
        creerMurs(480,0);
        creerMurs(0,500);
        creerMurs(480,500);  
    }

    private void creerMurs(int decalageX, int decalageY)
    {
        /*
         * Définition des murs du labyrinthe.
         */
        ajouterMur(new Rectangle(20+decalageX, 0+decalageY, 80, 20));
        ajouterMur(new Rectangle(0+decalageX, 0+decalageY, 20, 500));
        ajouterMur(new Rectangle(20+decalageX, 480+decalageY, 440, 20));
        ajouterMur(new Rectangle(460+decalageX, 0+decalageY, 20, 500));
        ajouterMur(new Rectangle(320+decalageX, 0+decalageY, 140, 20));
        
        ajouterMur(new Rectangle(200+decalageX, -0+decalageY, 20, 100));
        
        ajouterMur(new Rectangle(120+decalageX, 100+decalageY, 240, 20));
        ajouterMur(new Rectangle(120+decalageX, 120+decalageY, 20, 20));
        ajouterMur(new Rectangle(340+decalageX, 120+decalageY, 20, 260));
        ajouterMur(new Rectangle(120+decalageX, 360+decalageY, 220, 20));
        ajouterMur(new Rectangle(20+decalageX, 240+decalageY, 220, 20));
        ajouterMur(new Rectangle(220+decalageX, 220+decalageY, 20, 20));
    }
}
