package vues;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import models.creatures.*;
import models.jeu.*;
import models.tours.*;

/**
 * Panel de gestion des interactions avec le joueur.
 * <p>
 * Ce panel contient 4 zones :
 * <p>
 * - Information du l'etat du jeu (score, pieces d'or, etc)<br>
 * - Choix des tours pour ajout<br>
 * - Information sur une tour selectionnee<br>
 * - Action sur le jeu (lancer une nouvelle vague)
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see JPanel
 * @see ActionListener
 */
public class Panel_MenuInteraction extends JPanel implements ActionListener															 
{
	// constantes finales
    private static final long serialVersionUID      = 1L;
	private static final ImageIcon I_PIECES         = new ImageIcon("img/icones/coins.png");
	private static final ImageIcon I_VIES 	        = new ImageIcon("img/icones/heart.png");
	
	// membres graphiques
	private JButton bTourArcher 			= new JButton(new ImageIcon(TourArcher.ICONE));
	private JButton bTourCanon 				= new JButton(new ImageIcon(TourCanon.ICONE));
	private JButton bTourAntiAerienne 		= new JButton(new ImageIcon(TourAntiAerienne.ICONE));
	private JButton bTourDeGlace            = new JButton(new ImageIcon(TourDeGlace.ICONE));
    private JButton bTourDeFeu              = new JButton(new ImageIcon(TourDeFeu.ICONE));
    private JButton bTourDeTerre            = new JButton(new ImageIcon(TourDeTerre.ICONE));
	
	private JLabel lScore 				    = new JLabel();
	private JLabel lTitreScore 			    = new JLabel("Score :");
	private JLabel lVies 					= new JLabel();
	private JLabel lTitreVies 				= new JLabel(I_VIES);
	private JLabel lNbPiecesOr 				= new JLabel();
	private JLabel lTitrePiecesOr 			= new JLabel(I_PIECES);
		
	// autres membres
	private Fenetre_Jeu fenJeu;
	private Jeu jeu;

	// panels internes
	private Panel_InfoTour panelInfoTour;
	private Panel_InfoCreature panelInfoCreature = new Panel_InfoCreature();
	
	/**
	 * Constructeur du panel d'interaction
	 * 
	 * @param jeu le jeu avec lequel on interagit (le model)
	 * @param fenJeu la fenetre de jeu
	 */
	public Panel_MenuInteraction(Jeu jeu, Fenetre_Jeu fenJeu)
	{
		super(new BorderLayout());
        
		// sauvegarde des attributs membres
		this.fenJeu   = fenJeu;
		this.jeu      = jeu;
		
		//---------------------
		//-- panel des tours --
		//---------------------
		JPanel pTours = new JPanel();
		
		bTourArcher.addActionListener(this);
		bTourArcher.setBorder(new EmptyBorder(5,5,5,5));
		pTours.add(bTourArcher);
		
		bTourCanon.addActionListener(this);
		bTourCanon.setBorder(new EmptyBorder(5,5,5,5));
		pTours.add(bTourCanon);
		
		bTourAntiAerienne.addActionListener(this);
		bTourAntiAerienne.setBorder(new EmptyBorder(5,5,5,5));
		pTours.add(bTourAntiAerienne);
		
		bTourDeGlace.addActionListener(this);
		bTourDeGlace.setBorder(new EmptyBorder(5,5,5,5));
        pTours.add(bTourDeGlace);

        bTourDeFeu.addActionListener(this);
        bTourDeFeu.setBorder(new EmptyBorder(5,5,5,5));
        pTours.add(bTourDeFeu);
        
        bTourDeTerre.addActionListener(this);
        bTourDeTerre.setBorder(new EmptyBorder(5,5,5,5));
        pTours.add(bTourDeTerre);
        
		//------------------------------------------
        //-- panel des donnees du joueur          --
		//-- (score, nb pieces or, vies restante) --
        //------------------------------------------
		JPanel pJoueur = new JPanel();
		
		// score
		pJoueur.add(lTitreScore);
		pJoueur.add(lScore);
		miseAJourScore();
		
		// pieces d'or
		pJoueur.add(lTitrePiecesOr);
		pJoueur.add(lNbPiecesOr);
		miseAJourNbPiecesOr();
		
		// vies restantes
		pJoueur.add(lTitreVies);
		pJoueur.add(lVies);
		miseAJourNbViesRestantes();
		
		JPanel pToursEtJoueur = new JPanel(new BorderLayout());
		
		pToursEtJoueur.add(pJoueur,BorderLayout.NORTH);
		pToursEtJoueur.add(pTours,BorderLayout.SOUTH);
		
	    add(pToursEtJoueur,BorderLayout.NORTH);
		
		//---------------------------
        //-- info tour et creature --
        //---------------------------
	    JPanel pInfos = new JPanel(new BorderLayout());
	    
		panelInfoTour = new Panel_InfoTour(fenJeu);
		fenJeu.setPanelInfoTour(panelInfoTour);
		fenJeu.setPanelInfoCreature(panelInfoCreature);
		
        pInfos.add(panelInfoTour,BorderLayout.NORTH);
        pInfos.add(panelInfoCreature,BorderLayout.CENTER);
        
        add(pInfos,BorderLayout.CENTER);
	}
	
	/**
	 * Permet d'avertir le composants contenu dans le menu qu'une tour 
	 * a ete selectionnee
	 * @param tour la tour selectionnee
	 * @param mode le mode de selection
	 */
	public void setTourSelectionnee(Tour tour, int mode)
	{
		panelInfoTour.setTour(tour, mode);
	}
	
	/**
     * Permet d'avertir le composants contenu dans le menu qu'une creature 
     * a ete selectionnee
     * @param creature la creature selectionnee
     */
	public void setCreatureSelectionnee(Creature creature)
    {
        panelInfoCreature.setCreature(creature);
    }

	/**
	 * Gestion des événements des divers éléments du 
	 * panel (menu, bouttons, etc.).
	 * 
	 * @param ae l'événement associé à une action
	 */
	public void actionPerformed(ActionEvent ae)
	{
		Object source = ae.getSource();
		
		if(source == bTourArcher)
			fenJeu.setTourAAcheter(new TourArcher());
		else if(source == bTourCanon)
			fenJeu.setTourAAcheter(new TourCanon());
		else if(source == bTourAntiAerienne)
			fenJeu.setTourAAcheter(new TourAntiAerienne());
		else if(source == bTourDeGlace)
		    fenJeu.setTourAAcheter(new TourDeGlace());
		else if(source == bTourDeFeu)
		    fenJeu.setTourAAcheter(new TourDeFeu());
		else if(source == bTourDeTerre)
            fenJeu.setTourAAcheter(new TourDeTerre());
		
	}
	
	/**
	 * Permet de demander une mise a jour du nombre de pieces du joueur
	 */
	public void miseAJourNbPiecesOr()
	{
		int nbPiecesOr = jeu.getNbPiecesOr();
	    
		bTourArcher.setEnabled(nbPiecesOr >= TourArcher.PRIX_ACHAT);
	    bTourCanon.setEnabled(nbPiecesOr >= TourCanon.PRIX_ACHAT);
	    bTourAntiAerienne.setEnabled(nbPiecesOr >= TourAntiAerienne.PRIX_ACHAT);
	    bTourDeGlace.setEnabled(nbPiecesOr >= TourDeGlace.PRIX_ACHAT);
	    bTourDeFeu.setEnabled(nbPiecesOr >= TourDeFeu.PRIX_ACHAT);
	    bTourDeTerre.setEnabled(nbPiecesOr >= TourDeTerre.PRIX_ACHAT);
	    
	    lNbPiecesOr.setText(nbPiecesOr+"");
	}

	/**
     * Permet de demander une mise a jour du nombre de vies restantes du joueur
     */
	public void miseAJourNbViesRestantes()
	{
		lVies.setText(jeu.getNbViesRestantes()+"");
	}
	
	/**
     * Permet de demander une mise a jour du score du joueur
     */
	public void miseAJourScore()
    {
        lScore.setText(jeu.getScore()+"");
    }



    /**
     * Permet d'informer le panel que la partie est terminee
     */
    public void partieTerminee()
    {
        // informe le panel des tours
        panelInfoTour.partieTerminee();
        
        // desactivation des tours
        bTourArcher.setEnabled(false);
        bTourCanon.setEnabled(false);
        bTourAntiAerienne.setEnabled(false);
        bTourDeGlace.setEnabled(false);
        bTourDeFeu.setEnabled(false);
        bTourDeTerre.setEnabled(false);
    }
}
