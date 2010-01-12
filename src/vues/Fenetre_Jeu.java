package vues;

import models.animations.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import models.outils.GestionnaireSons;
import models.tours.Tour;
import models.creatures.Creature;
import models.creatures.EcouteurDeCreature;
import models.creatures.EcouteurDeVague;
import models.creatures.VagueDeCreatures;
import models.jeu.Jeu;

/**
 * Fenetre princiale du jeu. 
 * 
 * Elle permet voir le jeu et d'interagir avec en posant des tours sur le terrain 
 * et de les gerer. Elle fournie aussi de quoi gerer les vagues d'ennemis.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see JFrame
 * @see ActionListener
 */
public class Fenetre_Jeu extends JFrame implements ActionListener, 
                                                    EcouteurDeCreature, 
                                                    EcouteurDeVague
{
	// constantes statiques
   private static final long serialVersionUID = 1L;
	private static final ImageIcon I_QUITTER = new ImageIcon("img/icones/door_out.png");
	private static final ImageIcon I_AIDE = new ImageIcon("img/icones/help.png");
	private static final ImageIcon I_ACTIF = new ImageIcon("img/icones/tick.png");
	private static final ImageIcon I_INACTIF = null;
	private static final ImageIcon I_FENETRE = new ImageIcon("img/icones/icone_pgm.png");
	private static final String FENETRE_TITRE = "ASD - Tower Defense";
	private static final int VOLUME_PAR_DEFAUT = 25;
	
	//---------------------------
	//-- declaration des menus --
	//---------------------------
	private final JMenuBar 	menuPrincipal 	= new JMenuBar();
	private final JMenu 	menuFichier 	= new JMenu("Fichier");
	private final JMenu 	menuEdition 	= new JMenu("Edition");
	private final JMenu  menuSon    = new JMenu("Son");
	private final JMenu 	menuAide 		= new JMenu("Aide");
	private final JMenuItem itemActiverSon = new JMenuItem("activer");
	private final JMenuItem itemDesactiverSon = new JMenuItem("desactiver");
	private final JMenuItem itemAPropos	    = new JMenuItem("A propos",I_AIDE);
	private final JMenuItem itemAfficherMaillage	    
		= new JMenuItem("activer / desactiver elements de gestion invisibles");
	private final JMenuItem itemAfficherRayonsPortee	    
		= new JMenuItem("activer / desactiver affichage des rayons de portee");
	private final JMenuItem itemQuitter	    = new JMenuItem("Quitter",I_QUITTER);
	private final JMenuItem itemRetourMenu  = new JMenuItem("Retour vers le menu",I_QUITTER);
	
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
	
	/**
	 * panel pour afficher les caracteristiques d'une tour 
	 * et permet d'ameliorer ou de vendre la tour en question
	 */
	private Panel_InfoTour panelInfoTour;
	
	/**
	 * panel pour afficher les caracteristiques d'une creature
	 */
	private Panel_InfoCreature panelInfoCreature;

	
	private static final String TXT_VAGUE_SUIVANTE  = "Lancer la vague";
    private JButton bLancerVagueSuivante = new JButton(TXT_VAGUE_SUIVANTE 
                                                       + " [niveau 1]");
    private JEditorPane taConsole  = new JEditorPane("text/html","");
    
    
	// autre attribut
	private Jeu jeu;
    private boolean vaguePeutEtreLancee = true;

	
	/**
	 * Constructeur de la fenetre. Creer et affiche la fenetre.
	 * 
	 * @param jeu le jeu a gerer
	 */
	public Fenetre_Jeu(Jeu jeu)
	{
		//-------------------------------
		//-- preferances de le fenetre --
		//-------------------------------
		super(FENETRE_TITRE);
		setIconImage(I_FENETRE.getImage());
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.jeu = jeu;
		
		//--------------------
		//-- menu principal --
		//--------------------
		// menu Fichier
		menuFichier.add(itemRetourMenu);
		menuFichier.add(itemQuitter);
		menuPrincipal.add(menuFichier);
		
		// menu Edition
		menuEdition.add(itemAfficherMaillage);
		menuEdition.add(itemAfficherRayonsPortee);
		menuPrincipal.add(menuEdition);
		
		// menu Son
		menuSon.add(itemActiverSon);
		menuSon.add(itemDesactiverSon);
		menuPrincipal.add(menuSon);
		
		// menu Aide
		menuAide.add(itemAPropos);
		menuPrincipal.add(menuAide);
		
		// ajout des ecouteurs
		itemRetourMenu.addActionListener(this);
		itemQuitter.addActionListener(this);
		itemAfficherMaillage.addActionListener(this);
		itemAfficherRayonsPortee.addActionListener(this);
		itemActiverSon.addActionListener(this);
		itemDesactiverSon.addActionListener(this);
		itemAPropos.addActionListener(this);
		
		
		// ajout du menu
		setJMenuBar(menuPrincipal); 
		
	
		
		//--------------------
        //-- vague suivante --
        //--------------------
        JPanel pVagueSuivante = new JPanel(new BorderLayout());
        ajouterInfoVagueSuivanteDansConsole();

        // style du champ de description de la vague suivante
        taConsole.setFont(new Font("",Font.TRUETYPE_FONT,10));
        taConsole.setEditable(false);
        JScrollPane scrollConsole = new JScrollPane(taConsole);
        scrollConsole.setPreferredSize(new Dimension(jeu.getLargeurTerrain(),50));
        pVagueSuivante.add(scrollConsole,BorderLayout.WEST);
        
        // bouton
        bLancerVagueSuivante.addActionListener(this);
        pVagueSuivante.add(bLancerVagueSuivante,BorderLayout.CENTER);
            
        add(pVagueSuivante,BorderLayout.SOUTH);
		

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
		
		// on demarre la musique au dernier moment
		jeu.demarrerMusiqueDAmbianceDuTerrain();
		
		//---------------------------------------
		//-- dernieres propietes de la fenetre --
		//---------------------------------------
		pack(); // adapte la taille de la fenetre a son contenu
		setVisible(true); // tu es toute belle, affiche toi !
		setLocationRelativeTo(null); // centrage de la fenetre
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
		
		if (source == itemDesactiverSon) {
		   GestionnaireSons.setVolumeMute(true);
		}
		
		else if (source == itemActiverSon) {
		   if (GestionnaireSons.isVolumeMute()) {
		      GestionnaireSons.setVolumeMute(false);
		      GestionnaireSons.setVolumeSysteme(VOLUME_PAR_DEFAUT);
		   }
		}
		
		// quitter
		else if(source == itemQuitter)
			quitter();
		
		// retour au menu principal
		else if(source == itemRetourMenu)
		    demanderRetourAuMenuPrincipal();  
		    
		// a propos
		else if(source == itemAPropos)
			new Fenetre_APropos(this); // ouverture de la fenetre "A propos"
		
		// basculer affichage du maillage
		else if(source == itemAfficherMaillage)
			if(panelTerrain.basculerAffichageMaillage())
			   itemAfficherMaillage.setIcon(I_ACTIF);
			else
			    itemAfficherMaillage.setIcon(I_INACTIF);
		
		// basculer affichage des rayons de portee
		else if(source == itemAfficherRayonsPortee)
		    if(panelTerrain.basculerAffichageRayonPortee())
		        itemAfficherRayonsPortee.setIcon(I_ACTIF);
		    else
		        itemAfficherRayonsPortee.setIcon(I_INACTIF);
		
		else if(source == bLancerVagueSuivante)
		{
		    if(!jeu.partieEstPerdu())
		    {
    		    lancerVagueSuivante();
    		    bLancerVagueSuivante.setEnabled(false);
		    }
		    else
		        retourAuMenuPrincipal();
		}
	}

	/**
	 * Permet de proposer au joueur s'il veut quitter le programme
	 */
	private void quitter()
    {
	    if(JOptionPane.showConfirmDialog(this, 
	            "Etes-vous sûr de vouloir quitter le jeu ?", 
	            "Vraiment quittez ?", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
	    {
	        System.exit(0); // Fermeture correcte du logiciel
	    }
    }

	/**
     * Permet de demander pour retourner au menu principal
     */
	private void demanderRetourAuMenuPrincipal()
    {
	    if(JOptionPane.showConfirmDialog(this, 
	            "Etes-vous sûr de vouloir arrêter la partie ?", 
	            "Retour au menu", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
        {
            retourAuMenuPrincipal();
        }
    }
	
    /**
	 * Permet de retourner au menu principal
	 */
	private void retourAuMenuPrincipal()
    {
	    GestionnaireSons.arreterTousLesSons();
        jeu.terminerLaPartie();
        
        dispose(); // destruction de la fenetre
        System.gc(); // passage du remasse miette
        new Fenetre_MenuPrincipal();  
    }

    /**
	 * Permet d'informer la fenetre que le joueur veut acheter une tour
	 * 
	 * @param tour la tour voulue
	 */
	public void acheterTour(Tour tour)
	{
	    try
	    {
	        jeu.poserTour(tour);
	        
	        panelTerrain.toutDeselectionner();
            panelMenuInteraction.miseAJourNbPiecesOr();
            
            setTourAAcheter(tour.getCopieOriginale());
            panelInfoTour.setTour(tour, Panel_InfoTour.MODE_ACHAT);
	    }
	    catch(Exception e)
	    {
	        ajouterTexteHTMLDansConsole("<font color='red'>" 
	                               + e.getMessage()+"</font><br />");
	    }
	}
	
	/**
	 * Permet d'informer la fenetre que le joueur veut ameliorer une tour
	 * 
	 * @param tour la tour a ameliorer
	 */
	public void ameliorerTour(Tour tour)
	{
	    try
        {
	        jeu.ameliorerTour(tour);
	        
	        panelMenuInteraction.miseAJourNbPiecesOr();
            panelInfoTour.setTour(tour, Panel_InfoTour.MODE_SELECTION);
        }
	    catch(Exception e)
        {
	        ajouterTexteHTMLDansConsole("<font color='red'>" 
                    + e.getMessage()+"</font><br />");
        }
	}
	
	/**
     * Permet d'informer la fenetre que le joueur veut vendre une tour
     * 
     * @param tour la tour a ameliorer
     */
    public void vendreTour(Tour tour)
    {
        jeu.vendreTour(tour);
        panelInfoTour.effacerTour();
        panelMenuInteraction.miseAJourNbPiecesOr();
        panelTerrain.setTourSelectionnee(null);
    }
	
    /**
     * Permet d'ajouter du text HTML dans la console
     * 
     * @param texte le texte a ajouter
     */
    public void ajouterTexteHTMLDansConsole(String texte)
    {
        String s = taConsole.getText();
        taConsole.setText( s.substring(0,s.indexOf("</body>")) 
                           + texte + 
                           s.substring(s.indexOf("</body>")));
    }
    
    
	/**
	 * Permet d'informer la fenetre qu'une tour a ete selectionnee
	 * 
	 * @param tour la tour selectionnee
	 * @param mode le mode de selection
	 */
	public void tourSelectionnee(Tour tour,int mode)
	{
		panelMenuInteraction.setTourSelectionnee(tour,mode);
	}
	
	/**
     * Permet d'informer la fenetre qu'une creature a ete selectionnee
     * 
     * @param creature la creature selectionnee
     */
    public void creatureSelectionnee(Creature creature)
    {
        panelInfoCreature.setCreature(creature);
    }

	/**
     * Permet d'informer la fenetre qu'on change la tour a acheter
     * 
     * @param tour la nouvelle tour a acheter
     */
	public void setTourAAcheter(Tour tour)
	{
		panelTerrain.setTourAAjouter(tour);
		panelInfoTour.setTour(tour, Panel_InfoTour.MODE_ACHAT);
	}

	/**
     * Permet de mettre a jour la reference vers le panel d'information
     * d'une tour.
     * 
     * @param panelInfoTour le panel
     */
	public void setPanelInfoTour(Panel_InfoTour panelInfoTour)
	{
		this.panelInfoTour = panelInfoTour;
	}
	
	/**
     * Permet de mettre a jour la reference vers le panel d'information
     * d'une creature.
     * 
     * @param panelInfoCreature le panel
     */
    public void setPanelInfoCreature(Panel_InfoCreature panelInfoCreature)
    {
        this.panelInfoCreature = panelInfoCreature;
    }
	
	/**
	 * Permet d'informer la fenetre que le joueur veut lancer une vague de
	 * creatures.
	 */
	public void lancerVagueSuivante()
	{ 
	    if(vaguePeutEtreLancee)
	    {
	        jeu.lancerVagueSuivante(this,this);
	        ajouterInfoVagueSuivanteDansConsole();
	        bLancerVagueSuivante.setEnabled(false);
	        vaguePeutEtreLancee = false;
	    }
	}
	
	/**
     * Permet de demander une mise a jour des informations de la vague suivante
     */
    public void ajouterInfoVagueSuivanteDansConsole()
    {
        ajouterTexteHTMLDansConsole("["+(jeu.getNumVagueCourante()+1)+"] Vague suivante : "+jeu.getDescriptionVagueSuivante()+"<br />");
        
        bLancerVagueSuivante.setText(TXT_VAGUE_SUIVANTE + " [niveau "+(jeu.getNumVagueCourante()+1)+"]");
    }
	
	/**
	 * methode regissant de l'interface EcouteurDeCreature
	 * 
	 * Permet d'etre informe lorsqu'une creature subie des degats.
	 */
	public void creatureBlessee(Creature creature)
	{
	    panelInfoCreature.miseAJourInfosVariables();
	    
	}

	/**
	 * methode regissant de l'interface EcouteurDeCreature
	 * 
	 * Permet d'etre informe lorsqu'une creature a ete tuee.
	 */
	public void creatureTuee(Creature creature)
	{
		jeu.creatureTuee(creature);
		
		// on efface la creature des panels d'information
		if(creature == panelTerrain.getCreatureSelectionnee())
		{
		    panelInfoCreature.effacerCreature();
		    panelTerrain.setCreatureSelectionnee(null);
		}
		    
		panelMenuInteraction.miseAJourNbPiecesOr();
		panelMenuInteraction.miseAJourScore();

		jeu.ajouterAnimation(new GainDePiecesOr((int)creature.getCenterX(),
                (int)creature.getCenterY() - 2,
                creature.getNbPiecesDOr()));
				
	}

	/**
     * methode regissant de l'interface EcouteurDeCreature
     * 
     * Permet d'etre informe lorsqu'une creature est arrivee en zone d'arrivee.
     */
	public void estArriveeEnZoneArrivee(Creature creature)
	{
		// si pas encore perdu
	    if(!jeu.partieEstPerdu())
		{
	        // indication au jeu
            jeu.creatureArriveeEnZoneArrivee(creature);
	        
            // mise a jour des infos
	        panelMenuInteraction.miseAJourNbViesRestantes();
			
			// le joueur n'a plus de vie
			if(jeu.partieEstPerdu())
			{
			    jeu.terminerLaPartie();
			    
			    panelMenuInteraction.partieTerminee();
			    
			    // le bouton lancer vague suivante devient un retour au menu
			    bLancerVagueSuivante.setEnabled(true);
			    vaguePeutEtreLancee = false;
		        bLancerVagueSuivante.setText("Retour au menu");
		        bLancerVagueSuivante.setIcon(I_QUITTER);
		        
			    new Fenetre_PartieTerminee(this, jeu.getScore(), jeu.getNomTerrain());
			}
		}
	}

    @Override
    public void vagueEntierementLancee(VagueDeCreatures vagueDeCreatures)
    {
        bLancerVagueSuivante.setEnabled(true);
        vaguePeutEtreLancee = true;
    }
}
