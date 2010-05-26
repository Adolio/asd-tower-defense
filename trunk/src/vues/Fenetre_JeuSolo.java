package vues;

import models.animations.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import exceptions.ActionNonAutoriseeException;
import outils.myTimer;
import models.outils.GestionnaireSons;
import models.tours.Tour;
import models.creatures.*;
import models.jeu.EcouteurDeJeu;
import models.jeu.Jeu;
import models.joueurs.Joueur;

/**
 * Fenetre princiale du jeu 1 joueur. 
 * 
 * Elle permet voir le jeu et d'interagir avec en posant des tours sur le terrain 
 * et de les gerer. Elle fournit aussi de quoi gerer les vagues d'ennemis.
 * 
 * @author Aurelien Da Campo
 * @version 1.1 | 17 mai 2010
 * @since jdk1.6.0_16
 * @see JFrame
 * @see ActionListener
 */
public class Fenetre_JeuSolo extends JFrame implements ActionListener, 
                                                    EcouteurDeJeu, 
                                                    EcouteurDePanelTerrain,
                                                    WindowListener,
                                                    KeyListener
{
	// constantes statiques
    private final int MARGES_PANEL = 20;
    private static final long serialVersionUID = 1L;
    private static final ImageIcon I_RETOUR = new ImageIcon("img/icones/arrow_undo.png");
	private static final ImageIcon I_QUITTER = new ImageIcon("img/icones/door_out.png");
	private static final ImageIcon I_AIDE = new ImageIcon("img/icones/help.png");
	private static final ImageIcon I_REGLES = new ImageIcon("img/icones/script.png");
	private static final ImageIcon I_ACTIF = new ImageIcon("img/icones/tick.png");
	private static final ImageIcon I_INACTIF = null;
	private static final ImageIcon I_FENETRE = new ImageIcon("img/icones/icone_pgm.png");
	private static final ImageIcon I_SON_ACTIF = new ImageIcon("img/icones/sound.png");
	private static final String FENETRE_TITRE = "ASD - Tower Defense";
    private static final String TXT_VAGUE_SUIVANTE  = "Lancer la vague";
	private static final int VOLUME_PAR_DEFAUT = 20;
	
	//---------------------------
	//-- declaration des menus --
	//---------------------------
	private final JMenuBar 	menuPrincipal 	= new JMenuBar();
	private final JMenu 	menuFichier 	= new JMenu("Fichier");
	private final JMenu 	menuEdition 	= new JMenu("Edition");
	private final JMenu     menuJeu         = new JMenu("Jeu");
	private final JMenu     menuSon         = new JMenu("Son");
	private final JMenu 	menuAide 		= new JMenu("Aide");
	private final JMenuItem itemRegles      = new JMenuItem("Règles du jeu...",I_REGLES);
	private final JMenuItem itemAPropos	    = new JMenuItem("A propos...",I_AIDE);

	private final JMenuItem itemPause
        = new JMenuItem("Pause"); 
	private final JMenuItem itemActiverDesactiverSon 
	    = new JMenuItem("Activer / Désactiver",I_SON_ACTIF); 
	private final JMenuItem itemAfficherMaillage	    
		= new JMenuItem("Activer / Désactiver le mode debug");
	private final JMenuItem itemAfficherRayonsPortee	    
		= new JMenuItem("Activer / Désactiver l'affichage des rayons de portée");
	private final JMenuItem itemQuitter	    
	    = new JMenuItem("Quitter",I_QUITTER);
	private final JMenuItem itemRetourMenu  
	    = new JMenuItem("Retour vers le menu principal",I_RETOUR);
	
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

	/**
	 * bouton pour lancer la vagues suivante
	 */
    private JButton bLancerVagueSuivante = new JButton(TXT_VAGUE_SUIVANTE 
                                                       + " [niveau 1]");
    /**
     * Console d'affichages des vagues suivantes
     */
    private JEditorPane taConsole  = new JEditorPane("text/html","");
    
    /**
     * Formulaire principale de la fenêtre
     */
    private JPanel pFormulaire = new JPanel(new BorderLayout());
    
    /**
     * Timer pour gérer le temps de jeu
     */
    private myTimer timer = new myTimer(1000,null);
    
    /**
     * Lien vers le jeu
     */
	private Jeu jeu;
	
	/**
	 * Référence vers le joueur de la partie
	 */
	private Joueur joueur;
	
	/**
	 * Permet de savoir si la prochaine vague peut etre lancée
	 */
    private boolean vaguePeutEtreLancee = true;
    private boolean demandeDEnregistrementDuScoreEffectuee;

	/**
	 * Constructeur de la fenetre. Creer et affiche la fenetre.
	 * 
	 * @param jeu le jeu a gerer
	 */
	public Fenetre_JeuSolo(Jeu jeu)
	{
	    this.jeu = jeu;
        this.joueur = jeu.getJoueurPrincipal();
        
	    //-------------------------------
		//-- preferences de le fenetre --
		//-------------------------------
		setTitle(FENETRE_TITRE);
		setIconImage(I_FENETRE.getImage());
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		getContentPane().setBackground(LookInterface.COULEUR_DE_FOND);
		
		pFormulaire.setOpaque(false);
		pFormulaire.setBorder(new EmptyBorder(new Insets(MARGES_PANEL, MARGES_PANEL,
                MARGES_PANEL, MARGES_PANEL)));
		
		//--------------------
		//-- menu principal --
		//--------------------
		// menu Fichier
		menuFichier.add(itemRetourMenu);
		menuFichier.add(itemQuitter);
		menuPrincipal.add(menuFichier);
		
		// menu Edition
		itemPause.setAccelerator(KeyStroke.getKeyStroke('P'));
		menuEdition.add(itemAfficherMaillage);
		menuEdition.add(itemAfficherRayonsPortee);
		menuPrincipal.add(menuEdition);
		
		// menu Jeu
		menuJeu.add(itemPause);
		menuPrincipal.add(menuJeu);
		
		// menu Son
		menuSon.add(itemActiverDesactiverSon);
		menuPrincipal.add(menuSon);

		// menu Aide
		menuAide.add(itemRegles);
		menuAide.add(itemAPropos);
		menuPrincipal.add(menuAide);
		
		// ajout des ecouteurs
		itemRetourMenu.addActionListener(this);
		itemQuitter.addActionListener(this);
		itemPause.addActionListener(this);
		itemAfficherMaillage.addActionListener(this);
		itemAfficherRayonsPortee.addActionListener(this);
		itemActiverDesactiverSon.addActionListener(this);
		itemRegles.addActionListener(this);
		itemAPropos.addActionListener(this);
		
		
		// ajout du menu
		setJMenuBar(menuPrincipal); 
		
		//--------------------
        //-- vague suivante --
        //--------------------
        JPanel pVagueSuivante = new JPanel(new BorderLayout());
        pVagueSuivante.setOpaque(false);
        ajouterInfoVagueSuivanteDansConsole();

        // style du champ de description de la vague suivante 
        taConsole.setFont(GestionnaireDesPolices.POLICE_CONSOLE);

        taConsole.setEditable(false);
        JScrollPane scrollConsole = new JScrollPane(taConsole);
        scrollConsole.setPreferredSize(new Dimension(jeu.getTerrain().getLargeur(),50));
        pVagueSuivante.add(scrollConsole,BorderLayout.WEST);
        
        // bouton
        GestionnaireDesPolices.setStyle(bLancerVagueSuivante);
        bLancerVagueSuivante.addActionListener(this);
        pVagueSuivante.add(bLancerVagueSuivante,BorderLayout.CENTER);
            
        pFormulaire.add(pVagueSuivante,BorderLayout.SOUTH);
		

		//----------------------------------------
		//-- panel du jeu et menu d'interaction --
		//----------------------------------------
		// creation des panels
         
		JPanel conteneurTerrain = new JPanel(new BorderLayout());
		conteneurTerrain.setBorder(new LineBorder(Color.BLACK,4));
		panelTerrain = new Panel_Terrain(jeu, this, joueur);
		panelTerrain.addKeyListener(this);
		//conteneurTerrain.setBorder(new EmptyBorder(new Insets(10, 10,10, 10)));
		conteneurTerrain.setOpaque(false);
		
		conteneurTerrain.add(panelTerrain,BorderLayout.NORTH);
		panelMenuInteraction = new Panel_MenuInteraction(this,joueur,timer);
		timer.start();
		
		
		panelInfoTour = panelMenuInteraction.getPanelInfoTour();
		panelInfoCreature = panelMenuInteraction.getPanelInfoCreature();
		
		
		// ajout des panels
		JPanel pMarge = new JPanel(new BorderLayout());
        pMarge.setBorder(new EmptyBorder(10, 10, 10, 10));
        pMarge.setOpaque(false);
		pMarge.add(conteneurTerrain);
		pFormulaire.add(pMarge,BorderLayout.WEST);
		pFormulaire.add(panelMenuInteraction,BorderLayout.EAST);
		
		// on demarre la musique au dernier moment
		jeu.getTerrain().demarrerMusiqueDAmbiance();
		
		add(pFormulaire,BorderLayout.CENTER);
		
		
	    //----------------------
        //-- demarrage du jeu --
        //----------------------
		jeu.setJoueurPrincipal(joueur);
		jeu.setEcouteurDeJeu(this);
        jeu.demarrer();
        
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
		
		if (source == itemActiverDesactiverSon) 
		   if (GestionnaireSons.isVolumeMute()) 
		   {
		      GestionnaireSons.setVolumeMute(false);
		      GestionnaireSons.setVolumeSysteme(VOLUME_PAR_DEFAUT);
		   }
		   else
		   {
		       GestionnaireSons.setVolumeMute(true); 
		   }

		// quitter
		else if(source == itemQuitter)
			quitter();
		
		// retour au menu principal
		else if(source == itemRetourMenu)
		    demanderRetourAuMenuPrincipal();  
		
		// règles
		else if(source == itemRegles)
		    new Fenetre_HTML("Règles du jeu", new File("donnees/regles/regles.html"), this);

		// a propos
		else if(source == itemAPropos)
			new Fenetre_HTML("A propos",new File("aPropos/aPropos.html"),this);
		
		// basculer affichage du maillage
		else if(source == itemAfficherMaillage)
			if(panelTerrain.basculerAffichageMaillage())
			   itemAfficherMaillage.setIcon(I_ACTIF);
			else
			    itemAfficherMaillage.setIcon(I_INACTIF);
		
		else if(source == itemPause) 
		    activerDesactiverLaPause();

		// basculer affichage des rayons de portee
		else if(source == itemAfficherRayonsPortee)
		    if(panelTerrain.basculerAffichageRayonPortee())
		        itemAfficherRayonsPortee.setIcon(I_ACTIF);
		    else
		        itemAfficherRayonsPortee.setIcon(I_INACTIF);
		
		else if(source == bLancerVagueSuivante)
		{
		    if(!joueur.aPerdu())
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
	        demanderEnregistrementDuScore();
	        
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
	        demanderEnregistrementDuScore();
	        
	        retourAuMenuPrincipal();
        }
    }
	
	/**
	 * Permet de demander à l'utilisateur s'il veut sauver son score
	 */
	private void demanderEnregistrementDuScore()
	{
	    // si le joueur a un score > 0 et que le score n'a pas été déjà sauvé
	    if(joueur.getScore() > 0 && !demandeDEnregistrementDuScoreEffectuee)
        {
            
	        demandeDEnregistrementDuScoreEffectuee = true;
	        
	        if(JOptionPane.showConfirmDialog(this, 
                    "Voulez vous sauver votre score ?", 
                    "Sauver ?", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION)
            {
                new Fenetre_PartieTerminee(this, joueur.getScore(), timer.getTime() / 1000, jeu.getTerrain().getNom()); 
            }
        }
	}

    /**
	 * Permet de retourner au menu principal
	 */
	private void retourAuMenuPrincipal()
    {
	    GestionnaireSons.arreterTousLesSons();
	    
	    if(!jeu.estTermine())
	        jeu.terminer();
        
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
            
            Tour nouvelleTour = tour.getCopieOriginale();
            nouvelleTour.setProprietaire(tour.getPrioprietaire());
            setTourAAcheter(nouvelleTour);
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
        try
        {
            jeu.vendreTour(tour);
            
            panelInfoTour.effacerTour();
            panelMenuInteraction.miseAJourNbPiecesOr();
            panelTerrain.setTourSelectionnee(null);
            
            jeu.ajouterAnimation(
                    new GainDePiecesOr((int)tour.getCenterX(),(int)tour.getCenterY(), 
                            tour.getPrixDeVente())
                    );
            
        } 
        catch (ActionNonAutoriseeException e)
        {
            e.printStackTrace();
        }
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
        panelMenuInteraction.setCreatureSelectionnee(creature);
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
	        jeu.lancerVagueSuivante(joueur.getEquipe());
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
        ajouterTexteHTMLDansConsole("["+(jeu.getTerrain().getNumVagueCourante()+1)+"] Vague suivante : "+jeu.getTerrain().getDescriptionVagueSuivante()+"<br />");
        
        bLancerVagueSuivante.setText(TXT_VAGUE_SUIVANTE + " [niveau "+(jeu.getTerrain().getNumVagueCourante()+1)+"]");
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
	    // on efface la creature des panels d'information
        if(creature == panelTerrain.getCreatureSelectionnee())
        {
            panelInfoCreature.effacerCreature();
            panelTerrain.setCreatureSelectionnee(null);
        }
            
        panelMenuInteraction.miseAJourNbPiecesOr();
        panelMenuInteraction.miseAJourScore();

        // TODO A METTRE OU PAS
        //jeu.ajouterAnimation(new Disparition((int) creature.getCenterX(), (int) creature.getCenterY(), creature.getImage(), 400));
        
        jeu.ajouterAnimation(new GainDePiecesOr((int)creature.getCenterX(),
                (int)creature.getCenterY() - 2,
                creature.getNbPiecesDOr()));	
	}

	@Override
	public void creatureArriveeEnZoneArrivee(Creature creature)
	{
	    // creation de l'animation de blessure du joueur
        jeu.ajouterAnimation(new PerteVie(jeu.getTerrain().getLargeur(),jeu.getTerrain().getHauteur())) ;

	    // mise a jour des infos
        panelMenuInteraction.miseAJourNbViesRestantes();
        
        // si c'est la creature selectionnee
        if(panelTerrain.getCreatureSelectionnee() == creature)
        {
            panelInfoCreature.setCreature(null);
            panelTerrain.setCreatureSelectionnee(null);
        }
	}

    @Override
    public void vagueEntierementLancee(VagueDeCreatures vagueDeCreatures)
    {
        bLancerVagueSuivante.setEnabled(true);
        vaguePeutEtreLancee = true;
    }
 
    /**
     * Permet de mettre a jour les infos du jeu
     */
    public void miseAJourInfoJeu()
    {
        panelMenuInteraction.miseAJourNbPiecesOr();
        panelMenuInteraction.miseAJourNbViesRestantes();
        panelMenuInteraction.miseAJourScore();
    }

    @Override
    public void windowActivated(WindowEvent e)
    {}

    @Override
    public void windowClosed(WindowEvent e)
    {}

    @Override
    public void windowClosing(WindowEvent e)
    {
       quitter(); 
    }

    @Override
    public void windowDeactivated(WindowEvent e){}

    @Override
    public void windowDeiconified(WindowEvent e){}

    @Override
    public void windowIconified(WindowEvent e){}

    @Override
    public void windowOpened(WindowEvent e){}

    // TODO [DEBUG] a effacer
    /**
     * (pour debug) Permet d'ajouter des pieces d'or
     * 
     * @param nbPiecesDOr le nombre de piece d'or a ajouter
     */
    public void ajouterPiecesDOr(int nbPiecesDOr)
    {
        joueur.setNbPiecesDOr(joueur.getNbPiecesDOr() + nbPiecesDOr); 
        
        miseAJourInfoJeu();
    }

    @Override
    public void keyPressed(KeyEvent ke)
    {
        // TODO [DEBUG] enlever pour version finale
        // raccourci de gain d'argent (debug)
        if(ke.getKeyChar() == 'm' || ke.getKeyChar() == 'M')
        {
            ajouterPiecesDOr(1000);
            
            //jeu.getGestionnaireAnimations().ajouterAnimation(new GainEtoile(jeu.getTerrain().getLargeur(),jeu.getTerrain().getHauteur())) ;  
            //jeu.getGestionnaireAnimations().ajouterAnimation(new PerteVie(jeu.getTerrain().getLargeur(),jeu.getTerrain().getHauteur())) ;
        }
        // TODO [DEBUG] enlever pour version finale
        // raccourci de gain d'argent (debug)
        else if(ke.getKeyChar() == 'l' || ke.getKeyChar() == 'L')
        {
            jeu.lancerVagueSuivante(joueur.getEquipe());
            ajouterInfoVagueSuivanteDansConsole();
        }
    }

    @Override
    public void keyReleased(KeyEvent ke)
    {
        // PAUSE
        if(ke.getKeyChar() == 'p' || ke.getKeyChar() == 'P')
            activerDesactiverLaPause();  
        // raccourci lancer vague suivante
        else if(Character.isSpaceChar(ke.getKeyChar())) 
            if(!jeu.estEnPause())
                lancerVagueSuivante();
    }

    /**
     * Permet de mettre le jeu en pause.
     */
    private void activerDesactiverLaPause()
    {
        boolean enPause = jeu.togglePause();
        
        if(enPause)
            timer.pause();
        else
            timer.play();
        
        // inhibation
        panelMenuInteraction.setPause(enPause);
        
        bLancerVagueSuivante.setEnabled(!enPause);
    }

    @Override
    public void keyTyped(KeyEvent e)
    {}

    @Override
    public void partieTerminee()
    {
        panelMenuInteraction.partieTerminee();
        
        // le bouton lancer vague suivante devient un retour au menu
        bLancerVagueSuivante.setEnabled(true);
        vaguePeutEtreLancee = false;
        bLancerVagueSuivante.setText("Retour au menu");
        bLancerVagueSuivante.setIcon(I_RETOUR);

        // si le joueur a un score > 0 et que le score n'a pas été déjà sauvé
        if(joueur.getScore() > 0 && !demandeDEnregistrementDuScoreEffectuee)
        {
            demandeDEnregistrementDuScoreEffectuee = true;
            new Fenetre_PartieTerminee(this, joueur.getScore(), timer.getTime() / 1000, jeu.getTerrain().getNom()); 
        }
    }
    
    @Override
    public void etoileGagnee()
    {
        jeu.ajouterAnimation(new GainEtoile(jeu.getTerrain().getLargeur(),jeu.getTerrain().getHauteur())) ;   
    }

    @Override
    public void tourAmelioree(Tour tour){}

    @Override
    public void tourPosee(Tour tour){}

    @Override
    public void tourVendue(Tour tour){}

    @Override
    public void animationAjoutee(Animation animation){}

    @Override
    public void animationTerminee(Animation animation){}

    @Override
    public void creatureAjoutee(Creature creature){}

    @Override
    public void joueurAjoute(Joueur joueur){}

    @Override
    public void partieDemarree(){}
}
