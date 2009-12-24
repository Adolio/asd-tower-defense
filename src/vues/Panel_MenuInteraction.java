package vues;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
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
	private static final String TXT_VAGUE_SUIVANTE  = "lancer la vague";
	
	// membres graphiques
	private JButton bTourArcher 			= new JButton(new ImageIcon(TourArcher.ICONE));
	private JButton bTourCanon 				= new JButton(new ImageIcon(TourCanon.ICONE));
	private JButton bTourAntiAerienne 		= new JButton(new ImageIcon(TourAntiAerienne.ICONE));
	private JButton bLancerVagueSuivante    = new JButton(TXT_VAGUE_SUIVANTE + " [niveau 1]");
	private JLabel lScore 				    = new JLabel();
	private JLabel lTitreScore 			    = new JLabel("Score :");
	private JLabel lVies 					= new JLabel();
	private JLabel lTitreVies 				= new JLabel(I_VIES);
	private JLabel lNbPiecesOr 				= new JLabel();
	private JLabel lTitrePiecesOr 			= new JLabel(I_PIECES);
	private JTextArea taDescrVagueSuivante  = new JTextArea();;
	
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
		pTours.add(bTourArcher);
		
		bTourCanon.addActionListener(this);
		pTours.add(bTourCanon);
		
		bTourAntiAerienne.addActionListener(this);
		pTours.add(bTourAntiAerienne);
		
		//------------------------------------------
        //-- panel des donnees du joueur          --
		//-- (score, nb pieces or, vies restante) --
        //------------------------------------------
		// score
		pTours.add(lTitreScore);
		lScore.setText(jeu.getScore()+"");
		pTours.add(lScore);
	
		// pieces d'or
		pTours.add(lTitrePiecesOr);
		lNbPiecesOr.setText(jeu.getNbPiecesOr()+"");
		pTours.add(lNbPiecesOr);
		
		// vies restantes
		pTours.add(lTitreVies);
		lVies.setText(jeu.getNbViesRestantes()+"");
		pTours.add(lVies);

	    add(pTours,BorderLayout.NORTH);
		
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
        
		//--------------------
        //-- vague suivante --
        //--------------------
		JPanel pVagueSuivante = new JPanel(new BorderLayout());
		miseAJourInfoVagueSuivante();

		// style du champ de description de la vague suivante
		taDescrVagueSuivante.setFont(new Font("",Font.TRUETYPE_FONT,10));
		taDescrVagueSuivante.setPreferredSize(new Dimension(250,50));
		taDescrVagueSuivante.setEditable(false);
		taDescrVagueSuivante.setLineWrap(true);
		taDescrVagueSuivante.setWrapStyleWord(true);
		taDescrVagueSuivante.setBackground(new Color(200,200,200));
		pVagueSuivante.add(taDescrVagueSuivante,BorderLayout.NORTH);
		pVagueSuivante.setBorder(BorderFactory.createTitledBorder("Vagues suivante"));
		
		// bouton
		bLancerVagueSuivante.addActionListener(this);
		pVagueSuivante.add(bLancerVagueSuivante,BorderLayout.CENTER);
			
		add(pVagueSuivante,BorderLayout.SOUTH);
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
		else if(source == bLancerVagueSuivante)
			fenJeu.lancerVagueSuivante();
	}
	
	// TODO
	public void ameliorerTour(Tour tour)
	{
		jeu.ameliorerTour(tour);
	}

	// TODO
	public void vendreTour(Tour tour)
	{
		jeu.vendreTour(tour);
		fenJeu.objetSelectionnee(null);
		panelInfoTour.effacerTour();
	}

	// TODO
	public void miseAJourNbPiecesOr()
	{
		lNbPiecesOr.setText(jeu.getNbPiecesOr()+"");
	}

	// TODO
	public void miseAJourNbViesRestantes()
	{
		lVies.setText(jeu.getNbViesRestantes()+"");
	}
	
	//TODO
	public void miseAJourScore()
    {
        lScore.setText(jeu.getScore()+"");
    }

	// TODO
    public void miseAJourInfoVagueSuivante()
    {
        taDescrVagueSuivante.setText(jeu.getDescriptionVagueCourante());
        bLancerVagueSuivante.setText(TXT_VAGUE_SUIVANTE + " [niveau "+jeu.getNumVagueCourante()+"]");
    }

    // TODO
    public void partieTerminee()
    {
        bLancerVagueSuivante.setEnabled(false);
        panelInfoTour.partieTerminee();
        
        // TODO
        bTourArcher.setEnabled(false);
        bTourCanon.setEnabled(false);
        bTourAntiAerienne.setEnabled(false);
    }
}
