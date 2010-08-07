package vues.reseau;

import models.animations.*;
import vues.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.*;
import exceptions.*;
import outils.myTimer;
import reseau.CanalException;
import reseau.jeu.client.EcouteurDeClientJeu;
import reseau.jeu.serveur.ServeurJeu;
import vues.Fenetre_MenuPrincipal;
import vues.GestionnaireDesPolices;
import vues.LookInterface;
import vues.commun.EcouteurDePanelTerrain;
import vues.commun.Fenetre_HTML;
import vues.commun.Panel_AjoutTour;
import vues.commun.Panel_InfoTour;
import vues.commun.Panel_InfosJoueurEtPartie;
import vues.commun.Panel_Selection;
import vues.commun.Panel_Terrain;
import models.outils.GestionnaireSons;
import models.outils.Outils;
import models.tours.Tour;
import models.creatures.*;
import models.jeu.EcouteurDeJeu;
import models.jeu.Jeu_Client;
import models.jeu.ModeDeJeu;
import models.jeu.ResultatJeu;
import models.joueurs.Equipe;
import models.joueurs.Joueur;

/**
 * Fenetre princiale du jeu 1 joueur. 
 * 
 * Elle permet voir le jeu et d'interagir avec en posant des tours sur le terrain 
 * et de les gerer. Elle fournit aussi de quoi gerer les vagues d'ennemis.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | 17 mai 2010
 * @since jdk1.6.0_16
 * @see JFrame
 * @see ActionListener
 */
public class Fenetre_JeuVersus extends JFrame implements ActionListener, 
                                                    EcouteurDeJeu,
                                                    EcouteurDeLanceurDeVagues,
                                                    EcouteurDePanelTerrain,
                                                    EcouteurDeClientJeu,
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
	private static final ImageIcon I_ENVOYER_MSG = new ImageIcon("img/icones/msg_go.png"); 
	private static final String FENETRE_TITRE = "ASD - Tower Defense";
	private static final int VOLUME_PAR_DEFAUT = 20;
	private static final int LARGEUR_MENU_DROITE = 280;
	
	
	//---------------------------
	//-- declaration des menus --
	//---------------------------
	private final JMenuBar 	menuPrincipal 	= new JMenuBar();
	private final JMenu 	menuFichier 	= new JMenu("Fichier");
	private final JMenu 	menuEdition 	= new JMenu("Edition");
	private final JMenu     menuSon         = new JMenu("Son");
	private final JMenu 	menuAide 		= new JMenu("Aide");
	private final JMenuItem itemRegles      = new JMenuItem("Règles du jeu...",I_REGLES);
	private final JMenuItem itemAPropos	    = new JMenuItem("A propos...",I_AIDE);

	
	
	private final JMenuItem itemActiverDesactiverSon 
	    = new JMenuItem("Activer / Désactiver",I_SON_ACTIF); 
	private final JMenuItem itemAfficherRayonsPortee	    
		= new JMenuItem("Activer / Désactiver l'affichage des rayons de portée");
	private final JMenuItem itemAfficherZonesJoueurs       
    = new JMenuItem("Activer / Désactiver l'affichage des zones et nom de joueurs");
	
	private final JMenuItem itemQuitter	    
	    = new JMenuItem("Quitter",I_QUITTER);
	private final JMenuItem itemRetourMenu  
	    = new JMenuItem("Retour vers le menu principal",I_RETOUR);
	
	private JLabel lblEtat = new JLabel(" ");
	
	//----------------------------
	//-- declaration des panels --
	//----------------------------
	/**
	 * panel contenant le terrain de jeu
	 */
	private Panel_Terrain panelTerrain;
	
	// TODO commenter
	private Panel_InfosJoueurEtPartie panelInfoJoueurEtPartie;
	private Panel_Selection panelSelection;
	private Panel_AjoutTour panelAjoutTour;
	private JTabbedPane panelSelectionEtVague;
	
    /**
     * Console d'affichages
     */
    private JEditorPane taConsole = new JEditorPane("text/html","");
    private JScrollPane scrollConsole;
    private JTextField tfSaisieMsg = new JTextField();
    private JButton bEnvoyerMsg = new JButton(I_ENVOYER_MSG);
    
    
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
	private Jeu_Client jeu;
	
	/**
	 * Panel de cröation d'une vague
	 */
    private Panel_CreationVague panelCreationVague;

	/**
	 * Constructeur de la fenetre. Creer et affiche la fenetre.
	 * 
	 * @param jeu le jeu a gerer
	 */
	public Fenetre_JeuVersus(Jeu_Client jeu)
	{
	    this.jeu = jeu;
	    jeu.setEcouteurDeClientJeu(this);
	    

	    //-------------------------------
		//-- preferances de le fenetre --
		//-------------------------------
		setTitle(FENETRE_TITRE);
		setIconImage(I_FENETRE.getImage());
		//setResizable(false);
		//setPreferredSize(new Dimension(1024,768));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		getContentPane().setBackground(LookInterface.COULEUR_DE_FOND_PRI);
		
		pFormulaire.setOpaque(false);
		//pFormulaire.setBackground(LookInterface.COULEUR_DE_FOND_2);
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
		menuEdition.add(itemAfficherRayonsPortee);
		menuEdition.add(itemAfficherZonesJoueurs);
		menuPrincipal.add(menuEdition);
		
		// menu Jeu
		//menuPrincipal.add(menuJeu);
		
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
		itemAfficherRayonsPortee.addActionListener(this);
		itemAfficherZonesJoueurs.addActionListener(this);
		itemActiverDesactiverSon.addActionListener(this);
		itemRegles.addActionListener(this);
		itemAPropos.addActionListener(this);
		
		// ajout du menu
		setJMenuBar(menuPrincipal); 
		
		//------------------------
        //-- Eléments de gauche --
        //------------------------
		
		JPanel pGauche = new JPanel(new BorderLayout());
		pGauche.setOpaque(false);
		
		//-------------
        //-- Console --
        //-------------
		// style
        taConsole.setFont(GestionnaireDesPolices.POLICE_CONSOLE);
        taConsole.setEditable(false);
        
        JPanel pConsole = new JPanel(new BorderLayout());
        pConsole.setOpaque(false);
        
        scrollConsole = new JScrollPane(taConsole);
        scrollConsole.setPreferredSize(new Dimension(0,50));
        pConsole.add(scrollConsole,BorderLayout.NORTH);
       
        // Saisie et bouton envoyer
        bEnvoyerMsg.addActionListener(this);
        getRootPane().setDefaultButton(bEnvoyerMsg); // bouton par def.
        
        JPanel pSaisieMsgEtBEnvoyer = new JPanel(new BorderLayout());
        pSaisieMsgEtBEnvoyer.setOpaque(false);
        pSaisieMsgEtBEnvoyer.add(tfSaisieMsg,BorderLayout.CENTER);
        pSaisieMsgEtBEnvoyer.add(bEnvoyerMsg,BorderLayout.EAST);
        pConsole.add(pSaisieMsgEtBEnvoyer,BorderLayout.SOUTH);
        
        pGauche.add(pConsole,BorderLayout.SOUTH);
		
		//-------------
		//-- Terrain --
		//-------------
		
        // creation des panels
		JPanel conteneurTerrain = new JPanel(new BorderLayout());
		conteneurTerrain.setBorder(new LineBorder(Color.BLACK,4));
		panelTerrain = new Panel_Terrain(jeu, this);
		panelTerrain.addKeyListener(this);
		conteneurTerrain.setOpaque(false);
		conteneurTerrain.add(panelTerrain,BorderLayout.CENTER);

	    // ajout
        JPanel pMargeTerrain = new JPanel(new BorderLayout());
        pMargeTerrain.setBorder(new EmptyBorder(5, 5, 5, 5));
        pMargeTerrain.setOpaque(false);
        pMargeTerrain.add(conteneurTerrain);
        pGauche.add(pMargeTerrain,BorderLayout.CENTER);
		
        // affichage des znoes et joueurs
        if(panelTerrain.basculerAffichageZonesJoueurs())
            itemAfficherZonesJoueurs.setIcon(I_ACTIF);
        else
            itemAfficherZonesJoueurs.setIcon(I_INACTIF);
        
        
        pFormulaire.add(pGauche,BorderLayout.CENTER);
        
        //--------------------
        //-- Menu de droite --
        //--------------------
		
        // Info jeu et joueur
		panelInfoJoueurEtPartie = new Panel_InfosJoueurEtPartie(jeu, ModeDeJeu.MODE_VERSUS);
		
		// Ajout de tour
		panelAjoutTour = new Panel_AjoutTour(jeu, this, LARGEUR_MENU_DROITE, 80);
		
		// Selection (tour et créature)
        panelSelection = new Panel_Selection(this);
        
        // Conteneur en onglets
		panelSelectionEtVague = new JTabbedPane();
		
		// Background
        UIManager.put("TabbedPane.tabAreaBackground", LookInterface.COULEUR_DE_FOND_PRI);
        //SwingUtilities.updateComponentTreeUI(panelSelectionEtVague);
     
        panelSelectionEtVague.setOpaque(true);
		panelSelectionEtVague.setPreferredSize(new Dimension(LARGEUR_MENU_DROITE,420));
		panelSelectionEtVague.setBackground(LookInterface.COULEUR_DE_FOND_PRI);
        panelSelectionEtVague.add("Info séléction", panelSelection);
           
        // panel de création de vagues
        panelCreationVague = new Panel_CreationVague(jeu,jeu.getJoueurPrincipal(),this);
        JScrollPane jsCreationVague = new JScrollPane(panelCreationVague);
        jsCreationVague.setOpaque(false);
        jsCreationVague.setPreferredSize(new Dimension(LARGEUR_MENU_DROITE,300));
        panelSelectionEtVague.add("Lanceur de créatures", jsCreationVague);
        
		
		JPanel pN1 = new JPanel(new BorderLayout());
		pN1.setOpaque(false);

		pN1.add(panelInfoJoueurEtPartie,BorderLayout.NORTH);
		
		JPanel pN2 = new JPanel(new BorderLayout());
		pN2.setOpaque(false);
        pN2.add(panelAjoutTour,BorderLayout.NORTH);
		
        JPanel pN3 = new JPanel(new BorderLayout());
        pN3.setOpaque(false);
        
        JPanel pPourCentrer = new JPanel();
        pPourCentrer.setOpaque(false);
        pPourCentrer.add(lblEtat);
        
        pN3.add(pPourCentrer,BorderLayout.NORTH);
		 
        
        JPanel pN4 = new JPanel(new BorderLayout());
        pN4.setOpaque(false);
        pN4.add(panelSelectionEtVague,BorderLayout.NORTH);
        
        pN3.add(pN4,BorderLayout.CENTER);
        pN2.add(pN3,BorderLayout.CENTER);
        pN1.add(pN2,BorderLayout.CENTER);
        
       // pN2.add(lblEtat,BorderLayout.NORTH);
        
        
        
        
        /*
        JPanel ppp = new JPanel();
        ppp.setOpaque(false);
        ppp.add(lblEtat,BorderLayout.EAST);
        
        pN1.add(ppp,BorderLayout.SOUTH);*/
        
        
		pFormulaire.add(pN1,BorderLayout.EAST);
		add(pFormulaire,BorderLayout.CENTER);
		
	    //----------------------
        //-- demarrage du jeu --
        //----------------------
		// TODO faire un 5.. 4.. 3.. 2.. 1..
		
		// on demarre la musique au dernier moment
        jeu.getTerrain().demarrerMusiqueDAmbiance();
        
		jeu.demarrer();
		timer.start();
		jeu.setEcouteurDeJeu(this);
	
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

		// basculer affichage des rayons de portee
		else if(source == itemAfficherRayonsPortee)
		    if(panelTerrain.basculerAffichageRayonPortee())
		        itemAfficherRayonsPortee.setIcon(I_ACTIF);
		    else
		        itemAfficherRayonsPortee.setIcon(I_INACTIF);
		
		else if(source == itemAfficherZonesJoueurs)
            if(panelTerrain.basculerAffichageZonesJoueurs())
                itemAfficherZonesJoueurs.setIcon(I_ACTIF);
            else
                itemAfficherZonesJoueurs.setIcon(I_INACTIF);

		else if(source == bEnvoyerMsg)
		{
		    try{
                
		        // on envoie pas de chaines vides
		        if(!tfSaisieMsg.getText().trim().equals(""))
                {
                    try
                    {
                        jeu.envoyerMsgChat(tfSaisieMsg.getText(), ServeurJeu.A_TOUS);
                    
                        tfSaisieMsg.setText("");
                        tfSaisieMsg.requestFocus();
                    } 
                    catch (MessageChatInvalide e)
                    {
                        ajouterTexteHTMLDansConsole("<font color='red'>#Quotes ouvrantes et fermantes interdites</font> <br/>");
                    }
                    
                }
            } 
		    catch (CanalException e)
            {
                e.printStackTrace();
            }
		}   
	}

	private void deconnexionDuJoueur()
	{
	    // on envoie la deconnexion
        try
        {
            jeu.annoncerDeconnexion();
        } 
        catch (CanalException e)
        {
            e.printStackTrace();
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
	        deconnexionDuJoueur();
	        
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
	        retourAuMenuPrincipal();
        }
    }
	
    /**
	 * Permet de retourner au menu principal
	 */
	private void retourAuMenuPrincipal()
    {
	    deconnexionDuJoueur();
	    
	    GestionnaireSons.arreterTousLesSons();
        
	    jeu.terminer();
        jeu.detruire();
        
        dispose(); // destruction de la fenetre
        System.gc(); // passage du remasse miette
        new Fenetre_MenuPrincipal();  
    }

    @Override
	public void acheterTour(Tour tour)
	{
	    try
	    {
	        jeu.poserTour(tour);
	        
	        panelTerrain.toutDeselectionner();
	          
            Tour nouvelleTour = tour.getCopieOriginale();
            nouvelleTour.setProprietaire(tour.getPrioprietaire());
            setTourAAcheter(nouvelleTour);
            panelSelection.setSelection(tour, Panel_InfoTour.MODE_ACHAT);
            panelSelectionEtVague.setSelectedIndex(0);
            
            
            lblEtat.setForeground(LookInterface.COULEUR_SUCCES);
            lblEtat.setText("Tour posée");
	    }
	    catch(Exception e)
	    {
	        lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
	        lblEtat.setText(e.getMessage());
	    }
	}
	
	@Override
	public void ameliorerTour(Tour tour)
	{
	    try
        {
	        jeu.ameliorerTour(tour);
	          
	        panelSelection.setSelection(tour, Panel_InfoTour.MODE_SELECTION);
	        
	        lblEtat.setForeground(LookInterface.COULEUR_SUCCES);
            lblEtat.setText("Tour Améliorée");
        }
	    catch(Exception e)
        {
	        lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
	        lblEtat.setText(e.getMessage());
        }
	}
	
	@Override
    public void vendreTour(Tour tour)
    {
        try
        {
            jeu.vendreTour(tour);
            panelSelection.deselection();
            panelTerrain.setTourSelectionnee(null);
            
            jeu.ajouterAnimation(
                    new GainDePiecesOr((int)tour.getCenterX(),(int)tour.getCenterY(), 
                            tour.getPrixDeVente())
                    );
            
            lblEtat.setForeground(LookInterface.COULEUR_SUCCES);
            lblEtat.setText("Tour vendue");
        } 
        catch (ActionNonAutoriseeException e)
        {
            lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
            lblEtat.setText(e.getMessage());
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
    
	@Override
	public void tourSelectionnee(Tour tour,int mode)
	{
	    if(tour == null)
	        // selection de l'onglet création de vague
	        panelSelectionEtVague.setSelectedIndex(1);
	    else
	        // selection de l'onglet selection
	        panelSelectionEtVague.setSelectedIndex(0);
	    
	    panelSelection.setSelection(tour, mode);
	    lblEtat.setText(" ");
	}
	
	/**
     * Permet d'informer la fenetre qu'une creature a ete selectionnee
     * 
     * @param creature la creature selectionnee
     */
    public void creatureSelectionnee(Creature creature)
    {
        if(creature == null)
            // selection de l'onglet création de vague
            panelSelectionEtVague.setSelectedIndex(1);
        else
            // selection de l'onglet selection
            panelSelectionEtVague.setSelectedIndex(0);
        
        panelSelection.setSelection(creature, 0);
        lblEtat.setText(" ");
    }

	/**
     * Permet d'informer la fenetre qu'on change la tour a acheter
     * 
     * @param tour la nouvelle tour a acheter
     */
	public void setTourAAcheter(Tour tour)
	{
		panelTerrain.setTourAAjouter(tour);
		panelSelection.setSelection(tour, Panel_InfoTour.MODE_ACHAT);
		lblEtat.setText(" ");
	}

	
	
    @Override
	public void creatureBlessee(Creature creature)
	{
	    panelSelection.setSelection(creature, 0);
	}

	@Override
	public void creatureTuee(Creature creature,Joueur tueur)
	{
	    // on efface la creature des panels d'information
        if(creature == panelTerrain.getCreatureSelectionnee())
        {
            panelSelection.deselection();
            panelTerrain.setCreatureSelectionnee(null);
        }

        jeu.ajouterAnimation(new GainDePiecesOr((int)creature.getCenterX(),
                (int)creature.getCenterY() - 2,
                creature.getNbPiecesDOr()));
	}

	/**
     * methode regissant de l'interface EcouteurDeCreature
     * 
     * Permet d'etre informe lorsqu'une creature est arrivee en zone d'arrivee.
     */
	public void creatureArriveeEnZoneArrivee(Creature creature)
	{
	    // creation de l'animation de blessure du joueur
        jeu.ajouterAnimation(new PerteVie(jeu.getTerrain().getLargeur(),jeu.getTerrain().getHauteur())) ;

        // si c'est la creature selectionnee
        if(panelTerrain.getCreatureSelectionnee() == creature)
        {
            panelSelection.deselection();
            panelTerrain.setCreatureSelectionnee(null);
        }
	}

    @Override
    public void vagueEntierementLancee(VagueDeCreatures vagueDeCreatures)
    {}
 
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

    @Override
    public void keyPressed(KeyEvent ke){}

    @Override
    public void keyTyped(KeyEvent e){}
    
    @Override
    public void keyReleased(KeyEvent e){}

    @Override
    public void lancerVague(VagueDeCreatures vague) throws ArgentInsuffisantException
    {
        jeu.lancerVague(jeu.getJoueurPrincipal(), jeu.getEquipeSuivanteNonVide(jeu.getJoueurPrincipal().getEquipe()),vague);
    }

    @Override
    public void ajouterInfoVagueSuivanteDansConsole(){}

    @Override
    public void lancerVagueSuivante() {}

    @Override
    public void partieTerminee(ResultatJeu resultatJeu)
    {
        panelSelection.partieTerminee();
        panelAjoutTour.partieTerminee();
        
        Equipe equipeGagnante = resultatJeu.getEquipeGagnante();
        
        // FIXME continuer...
        if(equipeGagnante == null)
            new Dialog_Message (this, "Pas de gagant!", "Personne n'a gagné !");         
        else if(equipeGagnante == jeu.getJoueurPrincipal().getEquipe())
        {
            new Dialog_Message (this, "Gagné!", "Vous avez gagné :) Bravo!");
            ajouterTexteHTMLDansConsole("<b>Vous avez gagné !</b><br/>"); 
        }
        else
        {
            new Dialog_Message (this, "Résultat de la partie", 
                    " L'équipe \""+equipeGagnante.getNom()+"\" " +
                    "remporte la partie!");  
            
            String couleurHexa = Outils.ColorToHexa(equipeGagnante.getCouleur());
            ajouterTexteHTMLDansConsole("L'équipe \"<b><font color='#"+couleurHexa+"'>"+equipeGagnante.getNom()+"</font></b>\" remporte la partie!<br/>"); 
        }
    }

    @Override
    public void etoileGagnee(){}

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
        if(jeu.getJoueurPrincipal() == joueur)
        {
            panelCreationVague.miseAJour();
            panelAjoutTour.miseAJour();
            panelInfoJoueurEtPartie.miseAJour();
        }
    }

    @Override
    public void partieInitialisee(){}

    @Override
    public void erreurPasAssezDArgent()
    {
        lblEtat.setForeground(LookInterface.COULEUR_SUCCES);
        lblEtat.setText("Vague trop chère");
    }

    @Override
    public void miseAJourInfoJeu(){}

    @Override
    public void joueurInitialise(){}

    @Override
    public void joueursMisAJour(){}

    @Override
    public void messageRecu(String message, Joueur auteur)
    {
        String couleurHexa = Outils.ColorToHexa(auteur.getEquipe().getCouleur());
        
        ajouterTexteHTMLDansConsole("<b><font color='#"+couleurHexa+"'>"+auteur.getPseudo()+"</font></b> dit : "+message+" <br />");
    }

    @Override
    public void joueurDeconnecte(Joueur joueur)
    {
        ajouterTexteHTMLDansConsole("<font color='#FF0000'>#Déconnexion : <b>"+joueur.getPseudo()+"</b></font> est parti.<br />");
    }

    @Override
    public void deselection()
    {
        panelSelection.deselection();
        
        lblEtat.setText(" ");
    }

    @Override
    public void receptionEquipeAPerdue(Equipe equipe)
    {
        if(equipe == jeu.getJoueurPrincipal().getEquipe())
        {
            new Dialog_Message (this, "Perdu!", "Vous avez perdu :(");
            ajouterTexteHTMLDansConsole("<b>Vous avez perdu!</b><br />");
        }
        else
        {   
            String couleurHexa = Outils.ColorToHexa(equipe.getCouleur());
            ajouterTexteHTMLDansConsole("L'équipe \"<b><font color='#"+couleurHexa+"'>"+equipe.getNom()+"</font></b>\" a perdue!<br />");
        }
    }

    @Override
    public void equipeAPerdue(Equipe equipe)
    {
        // ca vient du jeu... on s'en fou, c'est les infos du serveur qui comptent.
    }

    @Override
    public void coeffVitesseModifie(double coeffVitesse)
    {
        // pas utilisé en mode multi
    }
}
