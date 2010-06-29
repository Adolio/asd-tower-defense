package vues;

import models.animations.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.*;
import exceptions.*;
import models.outils.GestionnaireSons;
import models.tours.Tour;
import models.creatures.*;
import models.jeu.*;
import models.joueurs.*;

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
    private final int MARGES_PANEL = 10;
    private static final long serialVersionUID = 1L;
    private static final ImageIcon I_REDEMARRER = new ImageIcon("img/icones/arrow_rotate_clockwise.png");
    private static final ImageIcon I_RETOUR = new ImageIcon("img/icones/application_home.png");
	private static final ImageIcon I_QUITTER = new ImageIcon("img/icones/door_out.png");
	private static final ImageIcon I_AIDE = new ImageIcon("img/icones/help.png");
	private static final ImageIcon I_REGLES = new ImageIcon("img/icones/script.png");
	private static final ImageIcon I_ACTIF = new ImageIcon("img/icones/tick.png");
	private static final ImageIcon I_INACTIF = null;
	private static final ImageIcon I_FENETRE = new ImageIcon("img/icones/icone_pgm.png");
	private static final ImageIcon I_SON_ACTIF = new ImageIcon("img/icones/sound.png");
	private static final ImageIcon I_VITESSE_JEU   = new ImageIcon("img/icones/clock_play.png");
	private static final ImageIcon I_PLEIN_ECRAN = new ImageIcon("img/icones/arrow_out.png");
	private static final ImageIcon I_RETRECIR = new ImageIcon("img/icones/arrow_in.png");
	private static final ImageIcon I_CENTRE = new ImageIcon("img/icones/target.png");
	private static final ImageIcon I_ZOOM = new ImageIcon("img/icones/magnifier_zoom_in.png");
	private static final ImageIcon I_DEZOOM = new ImageIcon("img/icones/magnifier_zoom_out.png");
	
	
	private static final String FENETRE_TITRE = "ASD - Tower Defense";
    private static final String TXT_VAGUE_SUIVANTE  = "Lancer la vague";
	private static final int VOLUME_PAR_DEFAUT = 20;
    private static final double VITESSE_JEU_MAX = 3.0;
    private static final double VITESSE_JEU_MIN = 1.0;
    
    
    
    
	//---------------------------
	//-- declaration des menus --
	//---------------------------
	private final JMenuBar 	menuPrincipal 	= new JMenuBar();
	private final JMenu 	menuFichier 	= new JMenu("Fichier");
	private final JMenu 	menuAffichage 	= new JMenu("Affichage");
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
		= new JMenuItem("Maillage");
	private final JMenuItem itemModeDebug       
        = new JMenuItem("Mode debug");
	private final JMenuItem itemAfficherRayonsPortee	    
		= new JMenuItem("Rayons de portée");
	private final JMenuItem itemQuitter	    
	    = new JMenuItem("Quitter",I_QUITTER);
	private final JMenuItem itemRetourMenu  
	    = new JMenuItem("Retour vers le menu principal",I_RETOUR);
	
	private final JMenuItem itemRedemarrer  
    = new JMenuItem("Redémarrer la partie",I_REDEMARRER);
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
	private Panel_MenuInteraction_ModeSolo panelMenuInteraction;
	
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
     * Lien vers le jeu
     */
	private Jeu jeu;
	
	/**
	 * Permet de savoir si la prochaine vague peut etre lancée
	 */
    private boolean vaguePeutEtreLancee = true;
    private boolean demandeDEnregistrementDuScoreEffectuee;

    
    // TODO
    private JButton bVitesseJeu = new JButton("x"+VITESSE_JEU_MIN);
    private JButton bPleinEcran = new JButton(I_PLEIN_ECRAN);
    private JButton bCentrer = new JButton(I_CENTRE);
    private JButton bZoomAvant = new JButton(I_ZOOM);
    private JButton bZoomArriere = new JButton(I_DEZOOM);
    
	/**
	 * Constructeur de la fenetre. Creer et affiche la fenetre.
	 * 
	 * @param jeu le jeu a gerer
	 */
	public Fenetre_JeuSolo(Jeu jeu)
	{
	    this.jeu = jeu;
        
	    //-------------------------------
		//-- preferences de le fenetre --
		//-------------------------------
		setTitle(FENETRE_TITRE);
		setIconImage(I_FENETRE.getImage());
		//setResizable(false);
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
		menuFichier.add(itemRedemarrer);
		menuFichier.add(itemRetourMenu);
		menuFichier.add(itemQuitter);
		menuPrincipal.add(menuFichier);
		
		// menu Edition
		itemPause.setAccelerator(KeyStroke.getKeyStroke('P'));
		menuAffichage.add(itemModeDebug);
		menuAffichage.add(itemAfficherMaillage);
		menuAffichage.add(itemAfficherRayonsPortee);
		menuPrincipal.add(menuAffichage);
		
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
		itemRedemarrer.addActionListener(this);
		itemRetourMenu.addActionListener(this);
		itemQuitter.addActionListener(this);
		itemPause.addActionListener(this);
		itemModeDebug.addActionListener(this);
		itemAfficherMaillage.addActionListener(this);
		itemAfficherRayonsPortee.addActionListener(this);
		itemActiverDesactiverSon.addActionListener(this);
		itemRegles.addActionListener(this);
		itemAPropos.addActionListener(this);
		
		
		// ajout du menu
		setJMenuBar(menuPrincipal); 
		
		
		JPanel pGauche = new JPanel(new BorderLayout());
		pGauche.setOpaque(false);
		
		
		
		JPanel boutonsHaut = new JPanel(new FlowLayout());
        boutonsHaut.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
        //boutonsHaut.setPreferredSize(new Dimension(400, 25));
        boutonsHaut.setOpaque(false);
        
        //Dimension dimBouton = new Dimension(51, 32);
        
        // zoom
        bZoomAvant.setToolTipText("Zoom Avant [Roulette de la souris]");
        bZoomArriere.setToolTipText("Zoom Arrière [Roulette de la souris]");
        GestionnaireDesPolices.setStyle(bZoomAvant);
        GestionnaireDesPolices.setStyle(bZoomArriere);
        //bZoomAvant.setPreferredSize(dimBouton);
        //bZoomArriere.setPreferredSize(dimBouton);
        boutonsHaut.add(bZoomAvant);
        boutonsHaut.add(bZoomArriere);
        bZoomAvant.addActionListener(this);
        bZoomArriere.addActionListener(this);
        
        // centrer
        bCentrer.setToolTipText("Centrer [Double-clique]");
        GestionnaireDesPolices.setStyle(bCentrer);
        //bCentrer.setPreferredSize(dimBouton);
        boutonsHaut.add(bCentrer);
        bCentrer.addActionListener(this);
        
        // maximisation / minimisation
        bPleinEcran.setToolTipText("Maximiser / Minimiser la fenêtre");
        GestionnaireDesPolices.setStyle(bPleinEcran);
        //bPleinEcran.setPreferredSize(dimBouton);
        boutonsHaut.add(bPleinEcran);
        bPleinEcran.addActionListener(this);
        
        
        // vitesse de jeu
        //boutonsHaut.add(new JLabel(I_VITESSE_JEU));
        bVitesseJeu.setIcon(I_VITESSE_JEU);
        bVitesseJeu.setText("x"+jeu.getCoeffVitesse());
        bVitesseJeu.setToolTipText("Vitesse du jeu");
        GestionnaireDesPolices.setStyle(bVitesseJeu);
        //bVitesseJeu.setPreferredSize(dimBouton);
        boutonsHaut.add(bVitesseJeu);
        bVitesseJeu.addActionListener(this);
        
        //boutonsHaut.add(sVitesseJeu);
        pGauche.add(boutonsHaut,BorderLayout.NORTH);
		
		//----------------------
		//-- panel du terrain --
		//----------------------
		// creation des panels
         
		JPanel pConteneurTerrain = new JPanel(new BorderLayout());
		pConteneurTerrain.setBorder(new LineBorder(Color.BLACK,4));
		panelTerrain = new Panel_Terrain(jeu, this);
		panelTerrain.addKeyListener(this);
		//conteneurTerrain.setBorder(new EmptyBorder(new Insets(10, 10,10, 10)));
		pConteneurTerrain.setOpaque(false);
		pConteneurTerrain.add(panelTerrain,BorderLayout.CENTER);
		
        JPanel pMarge = new JPanel(new BorderLayout());
        pMarge.setBorder(new EmptyBorder(MARGES_PANEL / 2, 0, MARGES_PANEL / 2, MARGES_PANEL / 2));
        pMarge.setOpaque(false);
        pMarge.add(pConteneurTerrain);
		
		pGauche.add(pMarge,BorderLayout.CENTER);
		
		//-------------
        //-- console --
        //-------------
        ajouterInfoVagueSuivanteDansConsole();

        // style du champ de description de la vague suivante 
        taConsole.setFont(GestionnaireDesPolices.POLICE_CONSOLE);

        taConsole.setEditable(false);
        JScrollPane scrollConsole = new JScrollPane(taConsole);
        scrollConsole.setPreferredSize(new Dimension(jeu.getTerrain().getLargeur(),50));

        pGauche.add(scrollConsole,BorderLayout.SOUTH);
		
        pFormulaire.add(pGauche,BorderLayout.CENTER);
        
        
        //--------------------
        //-- menu de droite --
        //--------------------

		panelMenuInteraction = new Panel_MenuInteraction_ModeSolo(this,jeu);
		panelInfoTour     = panelMenuInteraction.getPanelInfoTour();
		panelInfoCreature = panelMenuInteraction.getPanelInfoCreature();
		
	    // bouton de lancement de vague
        GestionnaireDesPolices.setStyle(bLancerVagueSuivante);
        bLancerVagueSuivante.addActionListener(this);
        panelMenuInteraction.add(bLancerVagueSuivante,BorderLayout.SOUTH);
        bLancerVagueSuivante.setPreferredSize(new Dimension(300,50));
        
		pFormulaire.add(panelMenuInteraction,BorderLayout.EAST);
		
		add(pFormulaire,BorderLayout.CENTER);
		
		
	    //----------------------
        //-- demarrage du jeu --
        //----------------------
		// on demarre la musique au dernier moment
        jeu.getTerrain().demarrerMusiqueDAmbiance();
		
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
			demanderQuitter();
		
		else if(source == itemRedemarrer)
            demanderRedemarrerPartie();
		
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
		
		// basculer affichage du maillage
        else if(source == itemModeDebug)
            if(panelTerrain.basculerModeDebug())
                itemModeDebug.setIcon(I_ACTIF);
            else
                itemModeDebug.setIcon(I_INACTIF);

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
		    if(!jeu.getJoueurPrincipal().aPerdu())
		    {
    		    lancerVagueSuivante();
    		    bLancerVagueSuivante.setEnabled(false);
		    }
		    else
		        retourAuMenuPrincipal();
		}
		
		else if(source == bVitesseJeu)
        {
		    if(jeu.getCoeffVitesse() >= VITESSE_JEU_MAX)
		        jeu.setCoeffVitesse(VITESSE_JEU_MIN);
		    else    
		        jeu.augmenterCoeffVitesse();
        }
		else if(source == bPleinEcran)
        {

		      if(getExtendedState() == JFrame.MAXIMIZED_BOTH) 
		      {
		          pack();
		          setLocationRelativeTo(null);
		          bPleinEcran.setIcon(I_PLEIN_ECRAN);
		      }
		      else
		      {
		          setExtendedState(JFrame.MAXIMIZED_BOTH);
		          bPleinEcran.setIcon(I_RETRECIR);
		      }
		      
		      panelTerrain.reinitialiserVue();
        }
		
		else if(source == bCentrer)
        {
		    panelTerrain.reinitialiserVue();
        }
		
		
		else if(source == bZoomAvant)
        {
            panelTerrain.zoomer(-1);
        }
		    
		else if(source == bZoomArriere)
        {
		    panelTerrain.zoomer(1);
        }
	}

	private void demanderRedemarrerPartie()
    {
	    if(JOptionPane.showConfirmDialog(this, 
                "Etes-vous sûr de vouloir arrêter cette partie ?", 
                "Vraiment redémarrer ?", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
        {
            demanderEnregistrementDuScore();
            
            //jeu.terminer();
            //jeu.detruire();
            
            jeu.reinitialiser();

            new Fenetre_JeuSolo(jeu);
            
            dispose();
        }
    }

    /**
	 * Permet de proposer au joueur s'il veut quitter le programme
	 */
	private void demanderQuitter()
    {
	    if(JOptionPane.showConfirmDialog(this, 
	            "Etes-vous sûr de vouloir quitter le jeu ?", 
	            "Vraiment quittez ?", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
	    {
	        //demanderEnregistrementDuScore();
	        
	        jeu.terminer();
            jeu.detruire();
	        
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
	        //demanderEnregistrementDuScore();
	        
	        jeu.terminer();
	        jeu.detruire();
	        
	        retourAuMenuPrincipal();
        }
    }
	
	/**
	 * Permet de demander à l'utilisateur s'il veut sauver son score
	 */
	private void demanderEnregistrementDuScore()
	{
	    // si le joueur a un score > 0 et que le score n'a pas été déjà sauvé
	    if(jeu.getJoueurPrincipal().getScore() > 0 && !demandeDEnregistrementDuScoreEffectuee)
        {
	        demandeDEnregistrementDuScoreEffectuee = true;
	        
	        if(JOptionPane.showConfirmDialog(this, 
                    "Voulez vous sauver votre score ?", 
                    "Sauver ?", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION)
            {
	            new Fenetre_PartieTerminee(this, jeu.getJoueurPrincipal().getScore(), jeu.getTimer().getTime() / 1000, jeu.getTerrain().getNom()); 
            }
        }
	}

    /**
	 * Permet de retourner au menu principal
	 */
	private void retourAuMenuPrincipal()
    {
	    GestionnaireSons.arreterTousLesSons();
         
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
        
        // reposition le curseur en fin 
        taConsole.setCaretPosition( taConsole.getDocument().getLength() - 1 );
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
	        jeu.lancerVagueSuivante(jeu.getJoueurPrincipal(), jeu.getJoueurPrincipal().getEquipe());
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
        ajouterTexteHTMLDansConsole("["+(jeu.getNumVagueCourante())+"] Vague suivante : "+jeu.getTerrain().getDescriptionVague(jeu.getNumVagueCourante())+"<br />");
        
        bLancerVagueSuivante.setText(TXT_VAGUE_SUIVANTE + " [niveau "+(jeu.getNumVagueCourante())+"]");
    }
	
    @Override
	public void creatureBlessee(Creature creature)
	{
	    panelInfoCreature.miseAJourInfosVariables();
	}

	@Override
	public void creatureTuee(Creature creature, Joueur tueur)
	{
	    // on efface la creature des panels d'information
        if(creature == panelTerrain.getCreatureSelectionnee())
        {
            panelInfoCreature.effacerCreature();
            panelTerrain.setCreatureSelectionnee(null);
        }

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
        panelMenuInteraction.miseAJourInfoJoueur();
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
       demanderQuitter(); 
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
        jeu.getJoueurPrincipal().setNbPiecesDOr(jeu.getJoueurPrincipal().getNbPiecesDOr() + nbPiecesDOr); 
        
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
        }
        // TODO [DEBUG] enlever pour version finale
        // raccourci de gain d'argent (debug)
        else if(ke.getKeyChar() == 'l' || ke.getKeyChar() == 'L')
        {
            jeu.lancerVagueSuivante(jeu.getJoueurPrincipal(), jeu.getJoueurPrincipal().getEquipe());
            ajouterInfoVagueSuivanteDansConsole();
        }
        else if(ke.getKeyChar() == '+')
        {
            jeu.augmenterCoeffVitesse();
        }
        else if(ke.getKeyChar() == '-')
        {
            jeu.diminuerCoeffVitesse();
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
        
        // inhibation
        panelMenuInteraction.setPause(enPause);
        
        bLancerVagueSuivante.setEnabled(!enPause);
    }

    @Override
    public void keyTyped(KeyEvent e)
    {}

    @Override
    public void partieTerminee(ResultatJeu resultatJeu)
    {
        panelMenuInteraction.partieTerminee();
        
        // le bouton lancer vague suivante devient un retour au menu
        bLancerVagueSuivante.setEnabled(true);
        vaguePeutEtreLancee = false;
        bLancerVagueSuivante.setText("Retour au menu");
        bLancerVagueSuivante.setIcon(I_RETOUR);

        
        demanderEnregistrementDuScore();
        /*
        // si le joueur a un score > 0 et que le score n'a pas été déjà sauvé
        if(jeu.getJoueurPrincipal().getScore() > 0 && !demandeDEnregistrementDuScoreEffectuee)
        {
            demandeDEnregistrementDuScoreEffectuee = true;
            new Fenetre_PartieTerminee(this, jeu.getJoueurPrincipal().getScore(), jeu.getTimer().getTime() / 1000, jeu.getTerrain().getNom()); 
        }*/
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

    @Override
    public void joueurMisAJour(Joueur joueur)
    {
        panelMenuInteraction.miseAJourInfoJoueur();
    }

    @Override
    public void partieInitialisee(){}

    @Override
    public void deselection()
    {
        panelInfoCreature.setCreature(null);
        panelInfoTour.setTour(null, 0);
    }

    public void receptionEquipeAPerdue(Equipe equipe){}

    @Override
    public void equipeAPerdue(Equipe equipe){}

    @Override
    public void coeffVitesseModifie(double coeffVitesse)
    {
        bVitesseJeu.setText("x"+coeffVitesse);
    }
}
