package vues;

import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;

import javax.swing.*;

import models.jeu.Jeu;
import models.joueurs.Equipe;
import models.joueurs.Joueur;
import models.outils.MeilleursScores;
import models.outils.Outils;
import models.outils.Score;
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
public class Panel_ModeSolo extends JPanel implements ActionListener, Runnable
{
	// constantes statiques
    private static final long serialVersionUID 	= 1L;
	private static final ImageIcon icoQUITTER 	= new ImageIcon("img/icones/door_out.png");
	private static final ImageIcon icoAIDE 		= new ImageIcon("img/icones/help.png");
	private static final ImageIcon icoSCORE      = new ImageIcon("img/icones/star.png");
	private static final int IMAGE_MENU_LARGEUR = 120;
	private static final int IMAGE_MENU_HAUTEUR = 120;
    private static final Color COULEUR_DE_FOND  = new Color(0,110,0);
    private static final ImageIcon IMAGE_MENU   = new ImageIcon("img/tours/towers.png");
	private static final ImageIcon icoCADENAS      = new ImageIcon("img/icones/lock.png");
    
    
    
	// elements du formulaire
	private final JMenuBar 	menuPrincipal 		= new JMenuBar();
	private final JMenu 	menuFichier 		= new JMenu("Fichier");
	private final JMenu     menuMeilleursScore  = new JMenu("Scores");
	private final JMenu 	menuAide 			= new JMenu("Aide");
	private final JMenuItem itemAPropos	    	= new JMenuItem("A propos",icoAIDE);
	private final JMenuItem itemQuitter	   		= new JMenuItem("Quitter",icoQUITTER);
	
	private final JMenuItem itemMSElementTD     = new JMenuItem(ElementTD.NOM);
    private final JMenuItem itemMSSpiral        = new JMenuItem(Spiral.NOM);
    private final JMenuItem itemMSDesert        = new JMenuItem(Desert.NOM);
    private final JMenuItem itemMSWaterWorld    = new JMenuItem(WaterWorld.NOM);
	
	private final JButton[] boutonsTerrains     = new JButton[4]; 

	private JProgressBar chargementTerrain;
	private Thread thread;
    private boolean chargementTermine;
	private JFrame parent;
	
	private JButton bAnnuler = new JButton("Annuler");
	
	/**
	 * Constructeur de la fenetre du menu principal
	 */
	public Panel_ModeSolo(JFrame parent)
	{
		//-------------------------------
		//-- preferances de le fenetre --
		//-------------------------------
	    this.parent = parent;
	    setLayout(new BorderLayout());
		parent.setTitle("Menu principal - Tower Defense");

		//--------------------
		//-- menu principal --
		//--------------------
		// menu Fichier
		menuMeilleursScore.setIcon(icoSCORE);
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
		parent.setJMenuBar(menuPrincipal);
		
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

		
		
		
		String[] nomTerrains = new String[]{"ElementTD","Spiral","Desert","WaterWorld"};
	    Score[] scoresMax    = new Score[4];
	    MeilleursScores ms;
	    int sommeEtoiles = 0;
	    
	    for(int i=0; i < nomTerrains.length; i++)
	    {  
	        ms = new MeilleursScores("donnees/"+nomTerrains[i]+".ms");
	        
	        if(ms.getScores().size() > 0)
	        {
	            Score score = ms.getScores().get(0); 
	            sommeEtoiles += score.getNbEtoiles();
	            scoresMax[i] = score;
	        }
	        else
	            scoresMax[i] = new Score(" ", 0);
	    }

	    
		// ajout des boutons au panel et ajout des ecouteurs
		JPanel pBoutonsTerrains = new JPanel(new FlowLayout());
		
		for(int i=0; i < boutonsTerrains.length; i++)
		{
		    JButton bouton = boutonsTerrains[i];
		    
		    JPanel p = new JPanel(new BorderLayout());
	
		    bouton.addActionListener(this);
		    p.add(bouton,BorderLayout.NORTH);
		    
		    // recuperation du meilleur score
		    Score score = scoresMax[i];
		    
		   
		    p.add(new Panel_Etoiles(score),BorderLayout.CENTER);
		    
		    String txt = " ";
		    
		    if(score.getValeur() > 0)
		        txt = score.getNomJoueur()+" - "+score.getValeur()+"";
		    
		    
	
		    p.add(new JLabel(txt,0),BorderLayout.SOUTH);
		    
		    if(i == 1 && sommeEtoiles < 1)
		    {
		        bouton.setEnabled(false);
		        
		        p.add(new JLabel("1 étoile min.",icoCADENAS,0),BorderLayout.SOUTH);
		    }
		        
		    if(i == 2 && sommeEtoiles < 3)
		    {
                bouton.setEnabled(false);
		        p.add(new JLabel("3 étoiles min.",icoCADENAS,0),BorderLayout.SOUTH);
		    }
		    
		    if(i == 3 && sommeEtoiles < 7)
		    {
		        bouton.setEnabled(false);
		        p.add(new JLabel("7 étoiles min.",icoCADENAS,0),BorderLayout.SOUTH);
		    }

		    pBoutonsTerrains.add(p);
		}
		
		setBackground(COULEUR_DE_FOND);
		add(new JLabel(IMAGE_MENU),BorderLayout.NORTH);
		
		JPanel p = new JPanel(new BorderLayout());
		
		p.add(pBoutonsTerrains,BorderLayout.CENTER);
		
		
		bAnnuler.addActionListener(this);
        p.add(bAnnuler,BorderLayout.SOUTH);

		
		add(p,BorderLayout.CENTER);
	
		
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
			new Fenetre_APropos(parent); // ouverture de la fenetre "A propos"
		
		// les terrains
		else if(source == boutonsTerrains[0])
		{
		    actionnerBarreDeChargement();
		    
		    Jeu jeu = new Jeu();
		    jeu.setTerrain(new ElementTD(jeu));
		    Equipe equipe = jeu.getEquipe(0); // les equipes sont créer par le terrain
            Joueur joueur = new Joueur(equipe);
            equipe.ajouterJoueur(joueur);
            jeu.initialiser();
            new Fenetre_JeuSolo(jeu,joueur);
			
			chargementTermine = true;
			parent.dispose();
		}
		else if(source == boutonsTerrains[1])
		{
		    actionnerBarreDeChargement();

		    Jeu jeu = new Jeu();
            jeu.setTerrain(new Spiral(jeu));
            Equipe equipe = jeu.getEquipe(0); // les equipes sont créer par le terrain
            Joueur joueur = new Joueur(equipe);
            equipe.ajouterJoueur(joueur);
            jeu.initialiser();
            new Fenetre_JeuSolo(jeu,joueur);
		    
		    chargementTermine = true;
		    parent.dispose();
		}
		else if(source == boutonsTerrains[2])
		{
		    actionnerBarreDeChargement();
		     
		    Jeu jeu = new Jeu();
            jeu.setTerrain(new Desert(jeu));
            Equipe equipe = jeu.getEquipe(0); // les equipes sont créer par le terrain
            Joueur joueur = new Joueur(equipe);
            equipe.ajouterJoueur(joueur);
            jeu.initialiser();
            new Fenetre_JeuSolo(jeu,joueur);

		    chargementTermine = true;
		    parent.dispose();
		}
		else if(source == boutonsTerrains[3])
		{
		    actionnerBarreDeChargement();
		    
		    Jeu jeu = new Jeu();
            jeu.setTerrain(new WaterWorld(jeu));
            Equipe equipe = jeu.getEquipe(0); // les equipes sont créer par le terrain
            Joueur joueur = new Joueur(equipe);
            equipe.ajouterJoueur(joueur);
            jeu.initialiser();
            new Fenetre_JeuSolo(jeu,joueur);
		    
		    chargementTermine = true;
		    parent.dispose();
		}
		else if(source == itemMSElementTD)
		    new Fenetre_MeilleursScores(ElementTD.NOM, parent);
		else if(source == itemMSSpiral)
            new Fenetre_MeilleursScores(Spiral.NOM, parent);
		else if(source == itemMSDesert)
            new Fenetre_MeilleursScores(Desert.NOM, parent);
		else if(source == itemMSWaterWorld)
            new Fenetre_MeilleursScores(WaterWorld.NOM, parent);
		else if(source == bAnnuler)
		{
		    parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_MenuPrincipal(parent),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();
		}
		    
	}

    synchronized private void actionnerBarreDeChargement()
    {  
        thread = new Thread(this);
        thread.start();   
    }

    @Override
    public void run()
    {
       /* version.setText("   CHARGEMENT DE LA CARTE");
        version.setForeground(Color.BLACK);*/
        chargementTerrain = new JProgressBar();
        add(chargementTerrain,BorderLayout.SOUTH);
        validate();
        
        int pourcent = 0;
        
        while(!chargementTermine)
        {
            pourcent = (pourcent+2)%100;
     
            chargementTerrain.setValue(pourcent);
            chargementTerrain.paintImmediately(0,0,1000,200);
            
            try{
                Thread.sleep(10);
            } 
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
