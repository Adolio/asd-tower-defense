package vues;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class Panel_MenuInteraction extends JPanel implements ActionListener,
															 EcouteurOperationSurTour
{
	private static final long serialVersionUID = 1L;
	private JButton bTourFeu 		= new JButton("Feu");
	private JButton bTourGlace 		= new JButton("Glace");
	
	private JLabel lScore 			= new JLabel();
	private JLabel lTitreScore 		= new JLabel("Score :");
	private JLabel lNbPiecesOr 		= new JLabel();
	private JLabel lTitrePiecesOr 	= new JLabel("Pieces d'or :");
	
	
	
	private JButton bLancerVague = new JButton("Vague suivante");
	
	private Panel_Terrain panelTerrain;
	private Jeu jeu;
	private Panel_InfoTour pInfoTour;

	public Panel_MenuInteraction(Panel_Terrain panelTerrain, Jeu jeu)
	{
		super(new BorderLayout());
		
		this.panelTerrain = panelTerrain;
		this.jeu = jeu;
		jeu.ajouterEcouteurOperationSurTour(this);
		setBackground(Color.BLACK);
		
		JPanel pTours = new JPanel();
		
		bTourFeu.setBackground(TourDeFeu.COULEUR);
		bTourFeu.addActionListener(this);
		pTours.add(bTourFeu);
		
		bTourGlace.setBackground(TourDeGlace.COULEUR);
		bTourGlace.addActionListener(this);
		pTours.add(bTourGlace);
		
		JPanel pPied = new JPanel(new FlowLayout());
		
		add(lTitreScore);
		pPied.add(lScore);
		lNbPiecesOr.setText(jeu.getNbPiecesOr()+"");
		pPied.add(lTitrePiecesOr);
		pPied.add(lNbPiecesOr);
		
		pInfoTour = new Panel_InfoTour();
		pInfoTour.modifierEcouteurOperationSurTour(this);
		add(pTours,BorderLayout.NORTH);
		add(pInfoTour,BorderLayout.CENTER);
		add(bLancerVague,BorderLayout.SOUTH);
		add(pPied,BorderLayout.EAST);
		
	}
	
	public void setTourSelectionnee(Tour tour)
	{
		pInfoTour.setTour(tour);
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
			panelTerrain.setTourAAjouter(tourDeFeu);
			pInfoTour.setTour(tourDeFeu);
		}
		else if(source == bTourGlace)
		{
			TourDeGlace tourDeGlace = new TourDeGlace();
			panelTerrain.setTourAAjouter(tourDeGlace);
			pInfoTour.setTour(tourDeGlace);
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
		panelTerrain.setTourSelectionnee(null);
	}

	public void ajouterTour(Tour tour)
	{
		
	}

	public void tourAjoutee(Tour tour)
	{
	}
}
