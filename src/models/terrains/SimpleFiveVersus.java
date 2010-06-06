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
public class SimpleFiveVersus extends Terrain
{
    private static final long serialVersionUID = 1L;
    public final static Image IMAGE_DE_FOND;
    public final static Image IMAGE_MENU;
    public final static File  FICHIER_MUSIQUE_DE_FOND;
    public final static String NOM = "Simple Versus de 2 à 5 joueurs";

    static
    {
        FICHIER_MUSIQUE_DE_FOND = new File("snd/Filippo Vicarelli/The War Begins.mp3");
        
        IMAGE_MENU    = Toolkit.getDefaultToolkit().getImage(
                                          "img/cartes/menu_principal/elementTD.png");
    	IMAGE_DE_FOND = Toolkit.getDefaultToolkit().getImage(
    	                                                 "img/cartes/simpleFiveVersus.png");
    }
	
    /**
     * Constructeur d'un terrain ElementTD selon la celebre map de Blizzard.
     */
    public SimpleFiveVersus (Jeu jeu) 
    {
        super(  jeu,
                520,  // largeur
                500,  // hauteur
                100,  // nbPiecesOrInitiales
                20,   // nbViesInitiales
                0,    // positionMaillageX
                0,    // positionMaillageY
                520,  // largeurMaillage
                500,  // hauteurMaillage
                ModeDeJeu.MODE_VERSUS, // mode de jeu
                new Color(197,148,90), // couleur de fond
                new Color(91,123,43),  // couleur des murs
                IMAGE_DE_FOND, // imageDeFond
                NOM  // nom
          );
 
        
        // Création des équipes
        Equipe e1 = new Equipe(1,"Les Rouges",Color.RED);
        e1.ajouterZoneDepartCreatures(new Rectangle(20, 0, 80, 20));
        e1.setZoneArriveeCreatures(new Rectangle(20, 480, 80, 20));
        e1.ajouterEmplacementJoueur(new EmplacementJoueur(1,new Rectangle(20,0,80,500),Color.RED));
        equipes.add(e1);
        
        Equipe e2 = new Equipe(2,"Les Bleus",Color.BLUE);
        e2.ajouterZoneDepartCreatures(new Rectangle(120, 0, 80, 20));
        e2.setZoneArriveeCreatures(new Rectangle(120, 480, 80, 20));
        e2.ajouterEmplacementJoueur(new EmplacementJoueur(2,new Rectangle(120,0,80,500),Color.BLUE));
        equipes.add(e2);
        
        Equipe e3 = new Equipe(3,"Les Verts",Color.GREEN);
        e3.ajouterZoneDepartCreatures(new Rectangle(220, 0, 80, 20));
        e3.setZoneArriveeCreatures(new Rectangle(220, 480, 80, 20));
        e3.ajouterEmplacementJoueur(new EmplacementJoueur(3,new Rectangle(220,0,80,500),Color.GREEN));
        equipes.add(e3);
        
        Equipe e4 = new Equipe(4,"Les Jaunes",Color.YELLOW);
        e4.ajouterZoneDepartCreatures(new Rectangle(320, 0, 80, 20));
        e4.setZoneArriveeCreatures(new Rectangle(320, 480, 80, 20));
        e4.ajouterEmplacementJoueur(new EmplacementJoueur(4,new Rectangle(320,0,80,500),Color.YELLOW));
        equipes.add(e4);
        
        Equipe e5 = new Equipe(5,"Les Noirs",Color.BLACK);
        e5.ajouterZoneDepartCreatures(new Rectangle(420, 0, 80, 20));
        e5.setZoneArriveeCreatures(new Rectangle(420, 480, 80, 20));
        e5.ajouterEmplacementJoueur(new EmplacementJoueur(5,new Rectangle(420,0,80,500),Color.BLACK));
        equipes.add(e5);
        
        fichierMusiqueDAmbiance = FICHIER_MUSIQUE_DE_FOND;
        
        /*
         * Définition des murs.
         */
        ajouterMur(new Rectangle(0, 0, 20, 500));
        
        ajouterMur(new Rectangle(100, 0, 20, 500));
        ajouterMur(new Rectangle(200, 0, 20, 500));
        ajouterMur(new Rectangle(300, 0, 20, 500));
        ajouterMur(new Rectangle(400, 0, 20, 500));
        ajouterMur(new Rectangle(500, 0, 20, 500));
    }
}
