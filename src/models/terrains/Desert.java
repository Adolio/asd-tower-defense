package models.terrains;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import models.jeu.Jeu;
import models.joueurs.EmplacementJoueur;
import models.joueurs.Equipe;

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
    private static final long serialVersionUID = 1L;
    public final static Image IMAGE_DE_FOND;
	public final static Image IMAGE_MENU;
	public final static String NOM = "Desert";
	
    static
    {
        IMAGE_MENU = Toolkit.getDefaultToolkit().getImage(
                                              "img/cartes/menu_principal/objectif.png");
    	IMAGE_DE_FOND = Toolkit.getDefaultToolkit().getImage("img/cartes/objectif.png");
    }
	
	/**
	 * Constructeur du terrain dans le desert
	 */
	public Desert(Jeu jeu)
	{
		super(  jeu,
		        500,      // largeur
                500,      // hauteur
                140,    // nbPiecesOrInitiales
                20,       // nbViesInitiales
                0,      // positionMaillageX
                0,        // positionMaillageY
                540,      // largeurMaillage
                500,      // hauteurMaillage
                new Color(161,72,0), // couleur de fond
                new Color(150,150,150), // couleur des murs
                IMAGE_DE_FOND, // imageDeFond
                NOM       // nom
          );
		
		
		// Création des équipes
		Equipe e = new Equipe("Les rouges",Color.RED);
        e.ajouterZoneDepartCreatures(new Rectangle(510,40,20,60));
        e.setZoneArriveeCreatures(new Rectangle(0,400,20,60));
        e.ajouterEmplacementJoueur(new EmplacementJoueur(new Rectangle(0,0,500,500)));
        equipes.add(e);

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
}
