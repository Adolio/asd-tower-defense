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
import models.tours.TourAntiAerienne;
import models.tours.TourArcher;
import models.tours.TourCanon;

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
	private JButton bTourArcher 			= new JButton(new ImageIcon(TourArcher.IMAGE));
	private JButton bTourCanon 				= new JButton(new ImageIcon(TourCanon.IMAGE));
	private JButton bTourAntiAerienne 		= new JButton(new ImageIcon(TourAntiAerienne.IMAGE));
	private static final ImageIcon I_PIECES = new ImageIcon("img/icones/coins.png");
	private static final ImageIcon I_VIES 	= new ImageIcon("img/icones/heart.png");
	//private JLabel lScore 				= new JLabel();
	//private JLabel lTitreScore 			= new JLabel("Score :");
	private JLabel lVies 					= new JLabel();
	private JLabel lTitreVies 				= new JLabel(I_VIES);
	private JLabel lNbPiecesOr 				= new JLabel();
	private JLabel lTitrePiecesOr 			= new JLabel(I_PIECES);
	private static final String TXT_VAGUE_SUIVANTE	= "Vague suivante";
	private JButton bLancerVagueSuivante = new JButton(TXT_VAGUE_SUIVANTE + " [niveau 1]");
	
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
		
		//bTourFeu.setBackground(TourDeFeu.COULEUR);
		bTourArcher.addActionListener(this);
		pTours.add(bTourArcher);
		
		//bTourGlace.setBackground(TourDeGlace.COULEUR);
		bTourCanon.addActionListener(this);
		pTours.add(bTourCanon);
		
		bTourAntiAerienne.addActionListener(this);
		pTours.add(bTourAntiAerienne);
		
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
		
		if(source == bTourArcher)
			fenJeu.setTourAAcheter(new TourArcher());
		else if(source == bTourCanon)
			fenJeu.setTourAAcheter(new TourCanon());
		else if(source == bTourAntiAerienne)
			fenJeu.setTourAAcheter(new TourAntiAerienne());
		else if(source == bLancerVagueSuivante)
		{
			fenJeu.lancerVagueSuivante();
			bLancerVagueSuivante.setText(TXT_VAGUE_SUIVANTE + " [niveau "+jeu.getNumVagueCourante()+"]");
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

	public void miseAJourNbPiecesOr()
	{
		lNbPiecesOr.setText(jeu.getNbPiecesOr()+"");
	}

	public void miseAJourNbViesRestantes()
	{
		lVies.setText(jeu.getNbViesRestantes()+"");
	}
}
