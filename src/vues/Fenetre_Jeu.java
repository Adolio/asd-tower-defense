package vues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import models.tours.Tour;
import models.creatures.Creature;
import models.creatures.EcouteurDeCreature;
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
public class Fenetre_Jeu extends JFrame implements ActionListener, EcouteurDeCreature
{
	private static final long serialVersionUID = 1L;
	
	private static final ImageIcon I_QUITTER = new ImageIcon("img/icones/door_out.png");
	private static final ImageIcon I_AIDE = new ImageIcon("img/icones/help.png");
	private static final ImageIcon I_ACTIF = new ImageIcon("img/icones/tick.png");
	private static final ImageIcon I_INACTIF = null;
	
	public final static String FENETRE_TITRE = "ASD - Tower Defense";
	
	//---------------------------
	//-- declaration des menus --
	//---------------------------
	private final JMenuBar 	menuPrincipal 	= new JMenuBar();
	private final JMenu 	menuFichier 	= new JMenu("Fichier");
	private final JMenu 	menuEdition 	= new JMenu("Edition");
	private final JMenu 	menuAide 		= new JMenu("Aide");
	private final JMenuItem itemAPropos	    = new JMenuItem("A propos",I_AIDE);
	private final JMenuItem itemAfficherMaillage	    
		= new JMenuItem("activer / desactiver elements de gestion invisibles");
	private final JMenuItem itemAfficherRayonsPortee	    
		= new JMenuItem("activer / desactiver affichage des rayons de portee");
	private final JMenuItem itemQuitter	    = new JMenuItem("Quitter",I_QUITTER);

	//----------------------------
	//-- declaration des panels --
	//----------------------------
	/**
	 * panel contenant le terrain de jeu
	 */
	private Panel_Terrain panelTerrain;
	/**
	 * panel contenant le menu d'interaction
	 */
	private Panel_MenuInteraction panelMenuInteraction;
	
	
	private Panel_InfoTour panelInfoTour;
	private Panel_InfoCreature panelInfoCreature;
	private Jeu jeu;

	
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
		this.jeu = jeu;
		
		//--------------------
		//-- menu principal --
		//--------------------
		// menu Fichier
		menuFichier.add(itemQuitter);
		menuPrincipal.add(menuFichier);
		
		// menu Edition
		menuEdition.add(itemAfficherMaillage);
		menuEdition.add(itemAfficherRayonsPortee);
		menuPrincipal.add(menuEdition);
		
		// menu A propos
		menuAide.add(itemAPropos);
		menuPrincipal.add(menuAide);
		
		// ajout des ecouteurs
		itemQuitter.addActionListener(this);
		itemAfficherMaillage.addActionListener(this);
		itemAfficherRayonsPortee.addActionListener(this);
		itemAPropos.addActionListener(this);
		
		
		// ajout du menu
		setJMenuBar(menuPrincipal); 
		
		//----------------------------------------
		//-- panel du jeu et menu d'interaction --
		//----------------------------------------
		// creation des panels
		JPanel conteneurTerrain = new JPanel(new BorderLayout());
		panelTerrain = new Panel_Terrain(jeu, this);
		conteneurTerrain.add(panelTerrain,BorderLayout.NORTH);
		panelMenuInteraction = new Panel_MenuInteraction(jeu,this);
		
		// ajout des panels
		add(conteneurTerrain,BorderLayout.WEST);
		add(panelMenuInteraction,BorderLayout.EAST);
		
		//---------------------------------------
		//-- dernieres propietes de la fenetre --
		//---------------------------------------
		pack(); // adapte la taille de la fenetre a son contenu
		setVisible(true); // tu es toute belle, affiche toi !
		setLocationRelativeTo(null); // centrage de la fenetre
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
		else if(source == itemAfficherMaillage)
			if(panelTerrain.toggleAfficherMaillage())
			   itemAfficherMaillage.setIcon(I_ACTIF);
			else
			    itemAfficherMaillage.setIcon(I_INACTIF);
		else if(source == itemAfficherRayonsPortee)
		    if(panelTerrain.toggleAfficherRayonPortee())
		        itemAfficherRayonsPortee.setIcon(I_ACTIF);
		    else
		        itemAfficherRayonsPortee.setIcon(I_INACTIF);
	}

	public void acheterTour(Tour tour)
	{
		if(jeu.poserTour(tour))
		{
			panelTerrain.deselectionner();
			panelMenuInteraction.miseAJourNbPiecesOr();
			
			setTourAAcheter(tour.getCopieOriginale());
			panelInfoTour.setTour(tour, Panel_InfoTour.MODE_ACHAT);
		}
	}
	
	public void ameliorerTour(Tour tour)
	{
		if(jeu.ameliorerTour(tour))
		{
			panelMenuInteraction.miseAJourNbPiecesOr();
			panelInfoTour.setTour(tour, Panel_InfoTour.MODE_SELECTION);
		}
	}
	
	// TODO
	public void tourSelectionnee(Tour tour,int mode)
	{
		panelMenuInteraction.setTourSelectionnee(tour,mode);
	}

	// TODO
	public void setTourAAcheter(Tour tour)
	{
		panelTerrain.setTourAAjouter(tour);
		panelInfoTour.setTour(tour, Panel_InfoTour.MODE_ACHAT);
	}

	// TODO
	public void objetSelectionnee(Object object)
	{
	    panelTerrain.setTourSelectionnee((Tour)object);
	}
	
	// TODO
	public void setPanelInfoTour(Panel_InfoTour panelInfoTour)
	{
		this.panelInfoTour = panelInfoTour;
	}
	
	// TODO
    public void setPanelInfoCreature(Panel_InfoCreature panelInfoCreature)
    {
        this.panelInfoCreature = panelInfoCreature;
    }

	// TODO
	public void vendreTour(Tour tour)
	{
		jeu.vendreTour(tour);
		panelInfoTour.effacerTour();
		panelMenuInteraction.setNbPiecesOr(jeu.getNbPiecesOr());
		panelTerrain.setTourSelectionnee(null);
	}
	
	// TODO
	public void lancerVagueSuivante()
	{
	    jeu.lancerVagueSuivante(this);
	    
	    panelMenuInteraction.miseAJourInfoVagueSuivante();
	}

	/**
	 * methode regissant de l'interface EcouteurDeCreature
	 * 
	 * Permet d'etre informe lorsqu'une creature subie des degats.
	 */
	public void creatureBlessee(Creature creature)
	{

	}

	/**
	 * methode regissant de l'interface EcouteurDeCreature
	 * 
	 * Permet d'etre informe lorsqu'une creature a ete tuee.
	 */
	public void creatureTuee(Creature creature)
	{
		jeu.creatureTuee(creature);

		panelMenuInteraction.setNbPiecesOr(jeu.getNbPiecesOr());
		
		panelTerrain.addAnimation(
				new GainDePiecesOr((int)creature.getCenterX(),
								   (int)creature.getCenterY() - 2,
								   creature.getNbPiecesDOr()));
	}

	// TODO
	public void estArriveeEnZoneArrivee(Creature creature)
	{
		if(!jeu.estPerdu())
		{
			jeu.perdreUneVie();
		
			panelMenuInteraction.miseAJourNbViesRestantes();
			
			if(jeu.estPerdu())
			{
			    panelMenuInteraction.partieTerminee();
			    
			    new Fenetre_PartieTerminee(this, jeu.getScore(), jeu.getNomTerrain());
			}
		}
	}

    public void creatureSelectionnee(Creature creature)
    {
        panelInfoCreature.setCreature(creature);
    }
}
