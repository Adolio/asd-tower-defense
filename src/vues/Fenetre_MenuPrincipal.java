package vues;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import models.jeu.Jeu;
import models.outils.Outils;
import models.terrains.*;

/**
 * Fenetre du menu principal du jeu.
 * <p>
 * Affiche un menu permettant au joueur de choisir 
 * sur quel terrain il veut jouer.
 * <p>
 * Les boutons des terrains ont ete fait completement statiques 
 * pour gagner un temps precieux.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 15 decembre 2009
 * @since jdk1.6.0_16
 */
public class Fenetre_MenuPrincipal extends JFrame implements ActionListener
{
	// constantes statiques
    private static final long serialVersionUID 	= 1L;
	private static final ImageIcon I_QUITTER 	= new ImageIcon("img/icones/door_out.png");
	private static final ImageIcon I_AIDE 		= new ImageIcon("img/icones/help.png");
	private static final ImageIcon I_SCORE      = new ImageIcon("img/icones/star.png");
	private static final int IMAGE_MENU_LARGEUR = 120;
	private static final int IMAGE_MENU_HAUTEUR = 120;
    private static final Color COULEUR_DE_FOND  = new Color(0,110,0);
    private static final ImageIcon IMAGE_MENU   = new ImageIcon("img/tours/towers.png");
    private static final Color COULEUR_TEXTE_VERSION = new Color(200,200,200);
	
	// elements du formulaire
	private final JMenuBar 	menuPrincipal 		= new JMenuBar();
	private final JMenu 	menuFichier 		= new JMenu("Fichier");
	private final JMenu     menuMeilleursScore  = new JMenu("Scores");
	private final JMenu 	menuAide 			= new JMenu("Aide");
	private final JMenuItem itemAPropos	    	= new JMenuItem("A propos",I_AIDE);
	private final JMenuItem itemQuitter	   		= new JMenuItem("Quitter",I_QUITTER);
	
	private final JMenuItem itemMSElementTD     = new JMenuItem(ElementTD.NOM);
    private final JMenuItem itemMSSpiral        = new JMenuItem(Spiral.NOM);
    private final JMenuItem itemMSDesert        = new JMenuItem(Desert.NOM);
    private final JMenuItem itemMSWaterWorld    = new JMenuItem(WaterWorld.NOM);
	
	
	private final JButton[] boutonsTerrains     = new JButton[4]; 

	/**
	 * Constructeur de la fenetre du menu principal
	 */
	public Fenetre_MenuPrincipal()
	{
		//-------------------------------
		//-- preferances de le fenetre --
		//-------------------------------
		super("Menu principal - Tower Defense");
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//--------------------
		//-- menu principal --
		//--------------------
		// menu Fichier
		menuMeilleursScore.setIcon(I_SCORE);
		menuFichier.add(menuMeilleursScore);
		menuMeilleursScore.add(itemMSElementTD);
		menuMeilleursScore.add(itemMSSpiral);
		menuMeilleursScore.add(itemMSDesert);
		menuMeilleursScore.add(itemMSWaterWorld);
		
		itemMSElementTD.addActionListener(this);
		itemMSSpiral.addActionListener(this);
		itemMSDesert.addActionListener(this);
		itemMSWaterWorld.addActionListener(this);
		
		menuFichier.add(itemQuitter);
		menuPrincipal.add(menuFichier);
		
		// menu A propos
		menuAide.add(itemAPropos);
		menuPrincipal.add(menuAide);
		
		// ajout des ecouteurs
		itemQuitter.addActionListener(this);
		itemAPropos.addActionListener(this);
		
		// ajout du menu
		setJMenuBar(menuPrincipal); 
		
		//---------------------------
		//-- chargement des images --
		//---------------------------
		// attent que toutes les images soit complementements chargees
		MediaTracker tracker = new MediaTracker(this);
		tracker.addImage(ElementTD.IMAGE_MENU, 0);
		tracker.addImage(Spiral.IMAGE_MENU, 1);
		tracker.addImage(Desert.IMAGE_MENU, 2);
		tracker.addImage(WaterWorld.IMAGE_MENU, 3);
		
		try { 
			tracker.waitForAll(); 
		} 
		catch (InterruptedException e){ 
			e.printStackTrace(); 
		}
		
		// creation des boutons
		boutonsTerrains[0] = new JButton(new ImageIcon(
							Outils.redimentionner(ElementTD.IMAGE_MENU,
									IMAGE_MENU_LARGEUR,IMAGE_MENU_HAUTEUR)));
		
		boutonsTerrains[1] = new JButton(new ImageIcon(
							Outils.redimentionner(Spiral.IMAGE_MENU,
									IMAGE_MENU_LARGEUR,IMAGE_MENU_HAUTEUR)));
		
		boutonsTerrains[2] = new JButton(new ImageIcon(
							Outils.redimentionner(Desert.IMAGE_MENU,
									IMAGE_MENU_LARGEUR,IMAGE_MENU_HAUTEUR)));
		
		boutonsTerrains[3] = new JButton(new ImageIcon(
							Outils.redimentionner(WaterWorld.IMAGE_MENU,
									IMAGE_MENU_LARGEUR,IMAGE_MENU_HAUTEUR)));

		// ajout des boutons au panel et ajout des ecouteurs
		JPanel pBoutonsTerrains = new JPanel(new FlowLayout());
		for(JButton bouton : boutonsTerrains)
		{
			bouton.addActionListener(this);
			pBoutonsTerrains.add(bouton);
		}
		
		getContentPane().setBackground(COULEUR_DE_FOND);
		getContentPane().add(new JLabel(IMAGE_MENU),BorderLayout.NORTH);
		getContentPane().add(pBoutonsTerrains,BorderLayout.CENTER);
		JLabel version = new JLabel(Jeu.getVersion());
		version.setForeground(COULEUR_TEXTE_VERSION);
		getContentPane().add(version,BorderLayout.SOUTH);
		
		// dernieres proprietes
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

    /**
     * Gestionnaire des evenements. 
     * <p>
     * Cette methode est appelee en cas d'evenement
     * sur un objet ecouteur de ActionListener
     * 
     * @param ae l'evenement associe
     */
	public void actionPerformed(ActionEvent ae)
	{
		Object source = ae.getSource();
		
		// quitter
		if(source == itemQuitter)
			System.exit(0); // Fermeture correcte du logiciel
		
		// a propos
		else if(source == itemAPropos)
			new Fenetre_APropos(this); // ouverture de la fenetre "A propos"
		
		// les terrains
		else if(source == boutonsTerrains[0])
		{
			new Fenetre_Jeu(new Jeu(new ElementTD()));
			dispose();
		}
		else if(source == boutonsTerrains[1])
		{
			new Fenetre_Jeu(new Jeu(new Spiral()));
			dispose();
		}
		else if(source == boutonsTerrains[2])
		{
			new Fenetre_Jeu(new Jeu(new Desert()));
			dispose();
		}
		else if(source == boutonsTerrains[3])
		{
			new Fenetre_Jeu(new Jeu(new WaterWorld()));
			dispose();
		}
		else if(source == itemMSElementTD)
		    new Fenetre_MeilleursScores(ElementTD.NOM, this);
		else if(source == itemMSSpiral)
            new Fenetre_MeilleursScores(Spiral.NOM, this);
		else if(source == itemMSDesert)
            new Fenetre_MeilleursScores(Desert.NOM, this);
		else if(source == itemMSWaterWorld)
            new Fenetre_MeilleursScores(WaterWorld.NOM, this);
	}
}
