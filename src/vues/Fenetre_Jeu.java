package vues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import models.tours.Tour;
import models.jeu.Jeu;

/**
 * Fenetre princiale du jeu. 
 * 
 * Elle permet voir le jeu et d'interagir avec en posant des tours sur le terrain 
 * et de les gérer. Elle fournie aussi de quoi gérer les vagues d'ennemis.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see JFrame
 * @see ActionListener
 */
public class Fenetre_Jeu extends JFrame implements ActionListener, 
													EcouteurDeSelectionDeTour
{
	private static final long serialVersionUID = 1L;
	
	public final static String FENETRE_TITRE = "ASD - Tower Defense";
	
	//---------------------------
	//-- declaration des menus --
	//---------------------------
	private final JMenuBar 	menuPrincipal 	= new JMenuBar();
	private final JMenu 	menuFichier 	= new JMenu("Fichier");
	private final JMenu 	menuEdition 	= new JMenu("Edition");
	private final JMenu 	menuAide 		= new JMenu("Aide");
	private final JMenuItem itemAPropos	    = new JMenuItem("A propos");
	private final JMenuItem itemQuitter	    = new JMenuItem("Quitter");

	//----------------------------
	//-- declaration des panels --
	//----------------------------
	/**
	 * panel contenant le terrain de jeu
	 */
	Panel_Terrain panelTerrain;
	/**
	 * panel contenant le menu d'interaction
	 */
	Panel_MenuInteraction panelMenuInteraction;
	
	/**
	 * Constructeur de la fenêtre. Creer et affiche la fenetre.
	 * 
	 * @param jeu le jeu a gerer
	 */
	public Fenetre_Jeu(Jeu jeu)
	{
		//-------------------------------
		//-- preferances de le fenetre --
		//-------------------------------
		super(FENETRE_TITRE);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//--------------------
		//-- menu principal --
		//--------------------
		// menu Fichier
		menuFichier.add(itemQuitter);
		menuPrincipal.add(menuFichier);
		
		// menu Edition
		menuPrincipal.add(menuEdition);
		
		// menu A propos
		menuAide.add(itemAPropos);
		menuPrincipal.add(menuAide);
		
		// ajout des ecouteurs
		itemQuitter.addActionListener(this);
		itemAPropos.addActionListener(this);
		
		// ajout du menu
		setJMenuBar(menuPrincipal); 
		
		//----------------------------------------
		//-- panel du jeu et menu d'interaction --
		//----------------------------------------
		// creation des panels
		panelTerrain = new Panel_Terrain(jeu);
		// pour savoir quand un tour est selectionnee
		panelTerrain.modifierEcouteurDeSelectionDeTour(this); 
		panelMenuInteraction = new Panel_MenuInteraction(panelTerrain,jeu);
		
		// ajout des panels
		add(panelTerrain,BorderLayout.WEST);
		add(panelMenuInteraction,BorderLayout.EAST);
		
		//---------------------------------------
		//-- dernieres propietes de la fenetre --
		//---------------------------------------
		pack(); // adapte la taille de la fenetre a son contenu
		setVisible(true); // tu es toute belle, affiche toi !
	}

	/**
	 * Gestion des événements des divers éléments de la 
	 * fenêtre (menu, bouttons, etc.).
	 * 
	 * @param ae l'événement associé à une action
	 */
	public void actionPerformed(ActionEvent ae)
	{
		Object source = ae.getSource();
		
		if(source == itemQuitter)
			System.exit(0); // Fermeture correcte du logiciel
		else if(source == itemAPropos)
			new Fenetre_APropos(); // ouverture de la fenetre "A propos"
	}

	public void tourSelectionnee(Tour tour)
	{
		panelMenuInteraction.setTourSelectionnee(tour);
	}
}
