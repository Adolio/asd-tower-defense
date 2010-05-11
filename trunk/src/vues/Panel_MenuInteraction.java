package vues;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import outils.myTimer;
import models.creatures.*;
import models.joueurs.Joueur;
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
	private static final ImageIcon I_ETOILE         = new ImageIcon("img/icones/star.png");
	private static final ImageIcon I_TEMPS         = new ImageIcon("img/icones/time.png");
	private static final ImageIcon I_SCORE         = new ImageIcon("img/icones/cup.png");
	
	// membres graphiques
	private JButton bTourArcher 			= new JButton(new ImageIcon(TourArcher.ICONE));
	private JButton bTourCanon 				= new JButton(new ImageIcon(TourCanon.ICONE));
	private JButton bTourAntiAerienne 		= new JButton(new ImageIcon(TourAntiAerienne.ICONE));
	private JButton bTourDeGlace            = new JButton(new ImageIcon(TourDeGlace.ICONE));
    private JButton bTourDeFeu              = new JButton(new ImageIcon(TourDeFeu.ICONE));
    private JButton bTourDAir               = new JButton(new ImageIcon(TourDAir.ICONE));
    private JButton bTourDeTerre            = new JButton(new ImageIcon(TourDeTerre.ICONE));
    private JButton bTourElectrique         = new JButton(new ImageIcon(TourElectrique.ICONE));
    private ArrayList<JButton> boutonsTours = new ArrayList<JButton>();
    
    private JLabel lTimer                   = new JLabel();
    private JLabel lTitreTimer              = new JLabel(I_TEMPS);
	private JLabel lScore 				    = new JLabel();
	private JLabel lTitreScore 			    = new JLabel(I_SCORE);
	private JLabel lVies 					= new JLabel();
	private JLabel lTitreVies 				= new JLabel(I_VIES);
	private JLabel lNbPiecesOr 				= new JLabel();
	private JLabel lTitrePiecesOr 			= new JLabel(I_PIECES);
	private JLabel lEtoiles                 = new JLabel();
	private JLabel lTitreEtoiles            = new JLabel(I_ETOILE);
		
	// autres membres
	private EcouteurDePanelTerrain edpt;
	private Joueur joueur;
	
	// panels internes
	//private Panel_InfoTour panelInfoTour;
	//private Panel_InfoCreature panelInfoCreature = new Panel_InfoCreature();
    
	private Panel_Selection pSelection;

	/**
	 * Constructeur du panel d'interaction
	 * 
	 * @param jeu le jeu avec lequel on interagit (le model)
	 * @param fenJeu la fenetre de jeu
	 */
	public Panel_MenuInteraction(EcouteurDePanelTerrain edpt,Joueur joueur, final myTimer timer)
	{
		super(new BorderLayout());
        
		this.edpt     = edpt;
		this.joueur   = joueur;
		setBackground(LookInterface.COULEUR_DE_FOND);
		
		timer.addActionListener(new ActionListener()
	    {
	        @Override
	        public void actionPerformed(ActionEvent e)
	        {
	            lTimer.setText(timer.toString());
	        }
	    });

		//---------------------
		//-- panel des tours --
		//---------------------
		JPanel pTours = new JPanel(new GridLayout(2,0));
		pTours.setOpaque(false);
		//pTours.setPreferredSize(new Dimension(200,80));
		
		boutonsTours.add(bTourArcher);
		bTourArcher.setToolTipText("Prix : "+TourArcher.PRIX_ACHAT);
		
        boutonsTours.add(bTourCanon);
        bTourCanon.setToolTipText("Prix : "+TourCanon.PRIX_ACHAT);
        
        boutonsTours.add(bTourAntiAerienne);
        bTourAntiAerienne.setToolTipText("Prix : "+TourAntiAerienne.PRIX_ACHAT);
        
        boutonsTours.add(bTourDeGlace);
        bTourDeGlace.setToolTipText("Prix : "+TourDeGlace.PRIX_ACHAT);
        
        boutonsTours.add(bTourElectrique);
        bTourElectrique.setToolTipText("Prix : "+TourElectrique.PRIX_ACHAT);
        
        boutonsTours.add(bTourDeFeu);
        bTourDeFeu.setToolTipText("Prix : "+TourDeFeu.PRIX_ACHAT);
        
        boutonsTours.add(bTourDAir);
        bTourDAir.setToolTipText("Prix : "+TourDAir.PRIX_ACHAT);
        
        boutonsTours.add(bTourDeTerre);
        bTourDeTerre.setToolTipText("Prix : "+TourDeTerre.PRIX_ACHAT);
        
		for(JButton bTour : boutonsTours)
        {
            bTour.addActionListener(this);
            bTour.setBorder(new EmptyBorder(5,5,5,5));
            GestionnaireDesPolices.setStyle(bTour);
            pTours.add(bTour);
        }
         
        JPanel pGlobalInfo = new JPanel();
        pGlobalInfo.setOpaque(false);
        
        //timer
        pGlobalInfo.add(lTitreTimer);
        pGlobalInfo.add(lTimer);
        lTimer.setText("00.00.00");
        lTimer.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
            
        // etoiles gagnées
        pGlobalInfo.add(lTitreEtoiles);
        pGlobalInfo.add(lEtoiles);
        lEtoiles.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        miseAJourNbEtoiles();
        

		//------------------------------------------
        //-- panel des donnees du joueur          --
		//-- (score, nb pieces or, vies restante) --
        //------------------------------------------
		JPanel pJoueur = new JPanel();
		pJoueur.setOpaque(false);
		
		
		// score
		pJoueur.add(lTitreScore);
        lTitreScore.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        pJoueur.add(lScore);
        lScore.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        miseAJourScore();
		
		// pieces d'or
		pJoueur.add(lTitrePiecesOr);
		pJoueur.add(lNbPiecesOr);
		lNbPiecesOr.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
		miseAJourNbPiecesOr();
		
		// vies restantes
		pJoueur.add(lTitreVies);
		pJoueur.add(lVies);
		lVies.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
		miseAJourNbViesRestantes();
		
		
		
		
		
		JPanel pToursEtJoueur = new JPanel(new BorderLayout());
		pToursEtJoueur.setOpaque(false);
		
		
		
		JPanel pAlignADroite = new JPanel(new BorderLayout());
        pAlignADroite.setOpaque(false);
        pAlignADroite.add(pGlobalInfo,BorderLayout.EAST);
		pToursEtJoueur.add(pAlignADroite,BorderLayout.NORTH);
		
		JPanel pAlignADroite2 = new JPanel(new BorderLayout());
		pAlignADroite2.setOpaque(false);
		pAlignADroite2.add(pJoueur,BorderLayout.EAST);
		pToursEtJoueur.add(pAlignADroite2,BorderLayout.CENTER);
		
		pToursEtJoueur.add(pTours,BorderLayout.SOUTH);
		
	    add(pToursEtJoueur,BorderLayout.NORTH);
		
		//---------------------------
        //-- info tour et creature --
        //---------------------------
	    pSelection = new Panel_Selection(edpt);
      
	    //JPanel pInfos = new JPanel(new BorderLayout());
	    //pInfos.setOpaque(false);
	    
		//panelInfoTour = new Panel_InfoTour(edpt);
		
        //pInfos.add(panelInfoTour,BorderLayout.NORTH);
        //pInfos.add(panelInfoCreature,BorderLayout.CENTER);
        
        add(pSelection,BorderLayout.CENTER);
	}
	
	/**
	 * Permet d'avertir le composants contenu dans le menu qu'une tour 
	 * a ete selectionnee
	 * @param tour la tour selectionnee
	 * @param mode le mode de selection
	 */
	public void setTourSelectionnee(Tour tour, int mode)
	{
		pSelection.setSelection(tour, mode);
	}
	
	/**
     * Permet d'avertir le composants contenu dans le menu qu'une creature 
     * a ete selectionnee
     * @param creature la creature selectionnee
     */
	public void setCreatureSelectionnee(Creature creature)
    {
        pSelection.setSelection(creature, 0);
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
		Tour tour = null;
		
		if(source == bTourArcher)
		    tour = new TourArcher();
		else if(source == bTourCanon)
		    tour = new TourCanon();
		else if(source == bTourAntiAerienne)
		    tour = new TourAntiAerienne();
		else if(source == bTourDeGlace)
		    tour = new TourDeGlace();
		else if(source == bTourDeFeu)
		    tour = new TourDeFeu();
		else if(source == bTourDAir)
		    tour = new TourDAir();
		else if(source == bTourDeTerre)
		    tour = new TourDeTerre();
		else if(source == bTourElectrique)
		    tour = new TourElectrique();
		else
		    return;
		
		tour.setProprietaire(joueur);
		
		pSelection.setSelection(tour,Panel_InfoTour.MODE_ACHAT);
		edpt.setTourAAcheter(tour);
	}
	
	
	/**
	 * Permet de demander une mise a jour du nombre de pieces du joueur
	 */
	public void miseAJourNbPiecesOr()
	{
		int nbPiecesOr = joueur.getNbPiecesDOr();
	    
		bTourArcher.setEnabled(nbPiecesOr >= TourArcher.PRIX_ACHAT);
	    bTourCanon.setEnabled(nbPiecesOr >= TourCanon.PRIX_ACHAT);
	    bTourAntiAerienne.setEnabled(nbPiecesOr >= TourAntiAerienne.PRIX_ACHAT);
	    bTourDeGlace.setEnabled(nbPiecesOr >= TourDeGlace.PRIX_ACHAT);
	    bTourElectrique.setEnabled(nbPiecesOr >= TourElectrique.PRIX_ACHAT);
	    bTourDeFeu.setEnabled(nbPiecesOr >= TourDeFeu.PRIX_ACHAT);
	    bTourDAir.setEnabled(nbPiecesOr >= TourDeFeu.PRIX_ACHAT);
	    bTourDeTerre.setEnabled(nbPiecesOr >= TourDeTerre.PRIX_ACHAT);
	    
	    lNbPiecesOr.setText(String.format("%04d",nbPiecesOr));
	}

	/**
     * Permet de demander une mise a jour du nombre de vies restantes du joueur
     */
	public void miseAJourNbViesRestantes()
	{
		lVies.setText(String.format("%02d",joueur.getEquipe().getNbViesRestantes()));
	}
	
	/**
     * Permet de demander une mise a jour du nombre d'étoiles gagnées
     */
    public void miseAJourNbEtoiles()
    {
        lEtoiles.setText(String.format("%02d",joueur.getNbEtoiles()));
    }
	
	/**
     * Permet de demander une mise a jour du score du joueur
     */
	public void miseAJourScore()
    {
        lScore.setText(String.format("%06d",joueur.getScore()));
        miseAJourNbEtoiles();
    }

    /**
     * Permet d'informer le panel que la partie est terminee
     */
    public void partieTerminee()
    {
        // informe le panel des tours
        //panelInfoTour.partieTerminee();
        pSelection.partieTerminee();
        
        // desactivation des tours
        for(JButton bTour : boutonsTours)
            bTour.setEnabled(false); 
    }

    /**
     * Permet de recuperer le panel d'info tour
     * @return le panel d'info tour
     */
    public Panel_InfoTour getPanelInfoTour()
    {
        //return panelInfoTour;
        return pSelection.getPanelInfoTour();
    }
    
    /**
     * Permet de recuperer le panel d'info créature
     * @return le panel d'info créature
     */
    public Panel_InfoCreature getPanelInfoCreature()
    {
        //return panelInfoCreature;
        return pSelection.getPanelInfoCreature();
    }

    /**
     * Permet d'indiquer que le jeu est en pause ou non.
     * 
     * @param enPause si le jeu est en pause.
     */
    public void setPause(boolean enPause)
    {
        pSelection.setPause(enPause);

        // desactivation des tours
        if(enPause)
            for(JButton bTour : boutonsTours)
                bTour.setEnabled(false);
        else
            miseAJourNbPiecesOr();
    }
}
