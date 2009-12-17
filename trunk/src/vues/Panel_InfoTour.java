package vues;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import models.outils.Outils;
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
	public static final int MODE_SELECTION 	= 0;
	public static final int MODE_ACHAT 		= 1;
	
	private static final long serialVersionUID = 1L;
	private Tour tour;
	
	private JLabel lNom 			= new JLabel();
	private JLabel lDegats 			= new JLabel();
	private JLabel lRayonPortee 			= new JLabel();
	private JLabel lPrix 			= new JLabel();
	private JLabel lTitrePrix 		= new JLabel();
	private JLabel lCadenceTir	= new JLabel();
	private JTextArea lDescrition 	= new JTextArea();
	private JPanel pBoutons 		= new JPanel(new FlowLayout());
	private JPanel pCaracteristiques= new JPanel(new GridBagLayout());
	private JLabel lType            = new JLabel();
	
	
	private static final ImageIcon I_AMELIORER = new ImageIcon("img/icones/hammer.png");
	private static final ImageIcon I_VENDRE = new ImageIcon("img/icones/coins_add.png");
	private static final String TXT_AMELIORER = "Ameliorer";
	private JButton bAmeliorer = new JButton(TXT_AMELIORER,I_AMELIORER);
	private static final String TXT_VENDRE = "Vendre";
	private static final String TXT_PRIX_ACHAT = "Prix d'achat";
	private static final String TXT_PRIX_TOTAL = "Prix total";
	private JButton bVendre = new JButton(TXT_VENDRE,I_VENDRE);
	private Fenetre_Jeu fenJeu;
	
	/**
	 * Constructeur du panel
	 */
	public Panel_InfoTour(Fenetre_Jeu fenJeu)
	{
		// construction du panel
		super(new BorderLayout());
		setPreferredSize(new Dimension(280, 0));

		

		
		
		
		this.fenJeu = fenJeu;
		
		Font f = new Font("Verdana", Font.BOLD, 12);
		
		int nbChamp = 0;
		
		// champ nom
		lNom.setFont(new Font("Arial",Font.BOLD,14));
		ajouterChamp(pCaracteristiques, lNom, 0, nbChamp++, 2);
		
		// champ prix
		ajouterChamp(pCaracteristiques, lTitrePrix, 0, nbChamp, 1);
		lPrix.setFont(f);
		ajouterChamp(pCaracteristiques, lPrix, 1, nbChamp++, 1);
		
		// champ degats
		ajouterChamp(pCaracteristiques, new JLabel("Degats"), 0, nbChamp, 1);
		lDegats.setFont(f);
		ajouterChamp(pCaracteristiques, lDegats, 1, nbChamp++, 1);
	
		// champ rayon de portee
		ajouterChamp(pCaracteristiques, new JLabel("Rayon de portee"), 0, nbChamp, 1);
		lRayonPortee.setFont(f);
		ajouterChamp(pCaracteristiques, lRayonPortee, 1, nbChamp++, 1);
		
		// champ cadence de tir
		ajouterChamp(pCaracteristiques, new JLabel("Cadence de tir"), 0, nbChamp, 1);
		lCadenceTir.setFont(f);
		ajouterChamp(pCaracteristiques, lCadenceTir, 1, nbChamp++, 1);

		// champ type de tir
		ajouterChamp(pCaracteristiques, new JLabel("Type de tir"), 0, nbChamp, 1);
		lType.setFont(f);
        ajouterChamp(pCaracteristiques, lType, 1, nbChamp++, 1);

		// champ description
		lDescrition.setPreferredSize(new Dimension(250,100));
		lDescrition.setEditable(false);
		lDescrition.setLineWrap(true);
		lDescrition.setWrapStyleWord(true);
		lDescrition.setBackground(new Color(200,200,200));
		lDescrition.setBorder(new EmptyBorder(5,5,5,5));
		
		lDescrition.setBorder(new LineBorder(Color.DARK_GRAY,2,true));
		
		//lDescrition.setBackground(new Color(pCaracteristiques.getBackground().getRGB()));
		
		ajouterChamp(pCaracteristiques, lDescrition, 0, nbChamp++, 2);
	

		JPanel pConteneurCaract = new JPanel();
		pConteneurCaract.add(pCaracteristiques,BorderLayout.NORTH);
		
		
		
		// les boutons
		pBoutons.add(bAmeliorer);
		pBoutons.add(bVendre);
		bAmeliorer.addActionListener(this);
		bVendre.addActionListener(this);
		
		
		//add(lNom,BorderLayout.NORTH);
		add(pConteneurCaract,BorderLayout.NORTH);
		add(pBoutons,BorderLayout.SOUTH);
	
		// initialisation a vide
		effacerTour();
	}
	
	public void ajouterChamp(JPanel panel, Component composant, int gridx, int gridy, int gridwidth)
	{
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill 		= GridBagConstraints.HORIZONTAL;
		c.gridx 	= gridx;
		c.gridy 	= gridy;
		c.gridwidth = gridwidth;
		
		panel.add(composant,c);
	}
	
	
	public void effacerTour()
	{
		setTour(null, 0);
	}
	
	/**
	 * Changement de tour
	 * 
	 * Met à jour le panel pour afficher les bonnes informations
	 * 
	 * @param tour La tour a gerer
	 */
	public void setTour(Tour tour, int mode)
	{
		// tour ou pas ?
		if(tour != null)
		{
			pCaracteristiques.setVisible(true);
			
			lDegats.setText(" : "+tour.getDegats());
			lRayonPortee.setText(" : "+Outils.arrondir(tour.getRayonPortee(),2));
			lDescrition.setText(tour.getDescription());
			lNom.setForeground(tour.getCouleurDeFond());
			lNom.setText(tour.getNom());
			lCadenceTir.setText(" : "+Outils.arrondir(tour.getCadenceTir(),2)+" / sec.");
			lType.setText(" : "+tour.getTexteType());
			
			// tour selectionnee pour information
			if(mode == 0)
			{
				// adaptation des champs
				lTitrePrix.setText(TXT_PRIX_TOTAL);
				lPrix.setText(" : "+tour.getPrixTotal());
				
				pBoutons.setVisible(true);
				
				// adaptation des boutons
				if(tour.peutEncoreEtreAmelioree())
				{
					bAmeliorer.setEnabled(true);
					bAmeliorer.setText(TXT_AMELIORER+" ["+tour.getPrixAchat()+"]");
				}
				else
				{
					bAmeliorer.setText("[niveau max]");
					bAmeliorer.setEnabled(false);
				}
				
				bVendre.setText(TXT_VENDRE+" ["+tour.getPrixDeVente()+"]");
			}
			// tour selectionnee pour achat
			else if(mode == 1)
			{
				// adaptation des champs
				lTitrePrix.setText(TXT_PRIX_ACHAT);
				lPrix.setText(" : "+tour.getPrixAchat());
				
				// adaptation des boutons
				pBoutons.setVisible(false);
			}
			
			// sauvegarde de la tour pour les operations
			this.tour = tour;
		}
		// mode sans tour selectionnee
		else
		{
			pCaracteristiques.setVisible(false);
			pBoutons.setVisible(false);
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
			fenJeu.ameliorerTour(tour);
		}
		else if(source == bVendre)
		{
			fenJeu.vendreTour(tour);
		}
	}
}
