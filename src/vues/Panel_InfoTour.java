package vues;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import models.tours.Tour;

/**
 * Classe de gestion d'affichage d'information d'une tour.
 * 
 * Le joueur pourra voir les proprietes d'une tour caracterisee par
 * sont prix, ses degats, son rayon de portee, etc.
 * 
 * C'est dans ce panel que le joueur peut améliorer une tour ou la vendre.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see JPanel
 * @see Tour
 */
public class Panel_InfoTour extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private Tour tour;
	private JLabel lNom;
	private JTextArea lDescrition;
	private static final String TXT_AMELIORER = "Ameliorer";
	private JButton bAmeliorer = new JButton(TXT_AMELIORER);
	private static final String TXT_VENDRE = "Vendre";
	private JButton bVendre = new JButton(TXT_VENDRE);
	private EcouteurOperationSurTour eost;
	
	/**
	 * Constructeur du panel
	 */
	public Panel_InfoTour()
	{
		// construction du panel
		super(new BorderLayout());
		
		setPreferredSize(new Dimension(250, 400));
		
		
		// TODO faire un system de formulaire (tableau)
		lNom = new JLabel();
		lDescrition = new JTextArea();
		lDescrition.setEditable(false);
		add(lNom,BorderLayout.NORTH);
		add(lDescrition,BorderLayout.CENTER);
		
		JPanel pBoutons = new JPanel(new FlowLayout());
		
		pBoutons.add(bAmeliorer);
		pBoutons.add(bVendre);
		
		bAmeliorer.addActionListener(this);
		bVendre.addActionListener(this);
		
		add(pBoutons,BorderLayout.SOUTH);
	
		// initialisation a vide
		setTour(null);
	}
	
	/**
	 * Changement de tour
	 * 
	 * Met à jour le panel pour afficher les bonnes informations
	 * 
	 * @param tour La tour a gerer
	 */
	public void setTour(Tour tour)
	{
		// mode avec une tour selectionnee
		if(tour != null)
		{
			// adaptation des champs
			lNom.setText(tour.getNom()+" ["+tour.getPrixAchat()+"]");
			lDescrition.setText(tour.getDescription());
			bAmeliorer.setVisible(true);
			bAmeliorer.setText(TXT_AMELIORER+" ["+tour.getPrixAchat()+"]");
			bVendre.setVisible(true);
			bVendre.setText(TXT_VENDRE+" ["+tour.getPrixDeVente()+"]");
			
			// sauvegarde de la tour pour les operations
			this.tour = tour;
		}
		// mode sans tour selectionnee
		else
		{
			lNom.setText("");
			lDescrition.setText("");
			bAmeliorer.setVisible(false);
			bVendre.setVisible(false);
		}
	}

	/**
	 * Methode de gestion des evenements
	 */
	public void actionPerformed(ActionEvent ae)
	{
		Object source = ae.getSource();
		
		if(source == bAmeliorer)
		{
			if(eost != null)
				eost.ameliorerTour(tour);
		}
		else if(source == bVendre)
			if(eost != null)
				eost.vendreTour(tour);
	}
	
	public void modifierEcouteurOperationSurTour(EcouteurOperationSurTour eost)
	{
		this.eost = eost;
	}
}
