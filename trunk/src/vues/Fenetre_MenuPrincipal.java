package vues;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import models.jeu.Jeu;
import models.outils.Outils;
import models.terrains.ElementTD;
import models.terrains.Objectif;
import models.terrains.Spirale;
import models.terrains.TerrainEau;

/**
 * Fenetre du menu principal du jeu.
 * 
 * Affiche un menu permettant au joueur de choisir 
 * sur quel terrain il veut jouer.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 15 decembre 2009
 * @since jdk1.6.0_16
 */
public class Fenetre_MenuPrincipal extends JFrame implements ActionListener
{
	private static final long serialVersionUID 	= 1L;
	private static final ImageIcon I_QUITTER 	= new ImageIcon("img/icones/door_out.png");
	private static final ImageIcon I_AIDE 		= new ImageIcon("img/icones/help.png");
	private static final int IMAGE_MENU_LARGEUR = 120;
	private static final int IMAGE_MENU_HAUTEUR = 120;
	
	private final JMenuBar 	menuPrincipal 		= new JMenuBar();
	private final JMenu 	menuFichier 		= new JMenu("Fichier");
	private final JMenu 	menuAide 			= new JMenu("Aide");
	private final JMenuItem itemAPropos	    	= new JMenuItem("A propos",I_AIDE);
	private final JMenuItem itemQuitter	   		= new JMenuItem("Quitter",I_QUITTER);
	
	JButton[] boutonsTerrains = new JButton[4]; 

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
		tracker.addImage(Spirale.IMAGE_MENU, 1);
		tracker.addImage(Objectif.IMAGE_MENU, 2);
		tracker.addImage(TerrainEau.IMAGE_MENU, 3);
		
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
							Outils.redimentionner(Spirale.IMAGE_MENU,
									IMAGE_MENU_LARGEUR,IMAGE_MENU_HAUTEUR)));
		
		boutonsTerrains[2] = new JButton(new ImageIcon(
							Outils.redimentionner(Objectif.IMAGE_MENU,
									IMAGE_MENU_LARGEUR,IMAGE_MENU_HAUTEUR)));
		
		boutonsTerrains[3] = new JButton(new ImageIcon(
							Outils.redimentionner(TerrainEau.IMAGE_MENU,
									IMAGE_MENU_LARGEUR,IMAGE_MENU_HAUTEUR)));

		// ajout des boutons au panel et ajout des ecouteurs
		JPanel pBoutonsTerrains = new JPanel(new FlowLayout());
		for(JButton bouton : boutonsTerrains)
		{
			bouton.addActionListener(this);
			pBoutonsTerrains.add(bouton);
		}
		
		getContentPane().setBackground(new Color(0,110,0));
		getContentPane().setForeground(Color.WHITE);
		//pBoutonsTerrains.setBackground(Color.DARK_GRAY);
		getContentPane().add(new JLabel(new ImageIcon("img/tours/towers.png")),BorderLayout.NORTH);
		getContentPane().add(pBoutonsTerrains,BorderLayout.CENTER);
		JLabel version = new JLabel(Jeu.getVersion());
		version.setForeground(new Color(200,200,200));
		getContentPane().add(version,BorderLayout.SOUTH);
		
		// dernieres proprietes
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}


	/**
	 * gestion des evenements
	 */
	public void actionPerformed(ActionEvent ae)
	{
		Object source = ae.getSource();
		
		if(source == itemQuitter)
			System.exit(0); // Fermeture correcte du logiciel
		else if(source == itemAPropos)
			new Fenetre_APropos(); // ouverture de la fenetre "A propos"
		else if(source == boutonsTerrains[0])
		{
			new Fenetre_Jeu(new Jeu(new ElementTD()));
			dispose();
		}
		else if(source == boutonsTerrains[1])
		{
			new Fenetre_Jeu(new Jeu(new Spirale()));
			dispose();
		}
		else if(source == boutonsTerrains[2])
		{
			new Fenetre_Jeu(new Jeu(new Objectif()));
			dispose();
		}
		else if(source == boutonsTerrains[3])
		{
			new Fenetre_Jeu(new Jeu(new TerrainEau()));
			dispose();
		}
	}
}
