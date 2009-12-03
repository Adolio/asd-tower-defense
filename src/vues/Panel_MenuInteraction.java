package vues;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import models.jeu.Jeu;
import models.tours.Tour;
import models.tours.TourDeFeu;
import models.tours.TourDeGlace;

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
	private static final long serialVersionUID = 1L;
	private JButton bTourFeu 		= new JButton("Feu");
	private JButton bTourGlace 		= new JButton("Glace");
	private static final ImageIcon I_PIECES = new ImageIcon("img/icones/coins.png");
	private static final ImageIcon I_VIES = new ImageIcon("img/icones/heart.png");
	//private JLabel lScore 			= new JLabel();
	//private JLabel lTitreScore 		= new JLabel("Score :");
	private JLabel lVies 			= new JLabel();
	private JLabel lTitreVies 		= new JLabel(I_VIES);
	private JLabel lNbPiecesOr 		= new JLabel();
	private JLabel lTitrePiecesOr 	= new JLabel(I_PIECES);
	private JButton bLancerVagueSuivante = new JButton("Vague suivante");
	
	private Fenetre_Jeu fenJeu;
	private Jeu jeu;
	private Panel_InfoTour panelInfoTour;

	public Panel_MenuInteraction(Jeu jeu, Fenetre_Jeu fenJeu)
	{
		super(new BorderLayout());
		
		this.fenJeu = fenJeu;
		this.jeu = jeu;
		setBackground(Color.BLACK);
		
		JPanel pTours = new JPanel();
		
		bTourFeu.setBackground(TourDeFeu.COULEUR);
		bTourFeu.addActionListener(this);
		pTours.add(bTourFeu);
		
		bTourGlace.setBackground(TourDeGlace.COULEUR);
		bTourGlace.addActionListener(this);
		pTours.add(bTourGlace);
		
		//pTours.add(lTitreScore);
		//pTours.add(lScore);
	
		pTours.add(lTitrePiecesOr);
		lNbPiecesOr.setText(jeu.getNbPiecesOr()+"");
		pTours.add(lNbPiecesOr);
		
		
		pTours.add(lTitreVies);
		lVies.setText(jeu.getNbViesRestantes()+"");
		pTours.add(lVies);
		
		panelInfoTour = new Panel_InfoTour(fenJeu);
		fenJeu.setPanelInfoTour(panelInfoTour);
		
		add(pTours,BorderLayout.NORTH);
		add(panelInfoTour,BorderLayout.CENTER);
		add(bLancerVagueSuivante,BorderLayout.SOUTH);
		bLancerVagueSuivante.addActionListener(this);
	}
	
	public void setTourSelectionnee(Tour tour, int mode)
	{
		panelInfoTour.setTour(tour, mode);
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
		
		if(source == bTourFeu)
		{
			TourDeFeu tourDeFeu = new TourDeFeu();
			fenJeu.setTourAAcheter(tourDeFeu);
		}
		else if(source == bTourGlace)
		{
			TourDeGlace tourDeGlace = new TourDeGlace();
			fenJeu.setTourAAcheter(tourDeGlace);
		}
		else if(source == bLancerVagueSuivante)
		{
			fenJeu.lancerVagueSuivante();
		}
	}

	public void setNbPiecesOr(int nbPiecesOr)
	{
		lNbPiecesOr.setText(nbPiecesOr+"");
	}
	
	public void ameliorerTour(Tour tour)
	{
		jeu.ameliorerTour(tour);
	}

	public void vendreTour(Tour tour)
	{
		jeu.vendreTour(tour);
		fenJeu.objetSelectionnee(null);
		panelInfoTour.effacerTour();
	}

	public void miseAJourNbPiecesOr(int nbPiecesOr)
	{
		lNbPiecesOr.setText(nbPiecesOr+"");
	}
}
