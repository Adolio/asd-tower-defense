package vues;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import models.tours.*;

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
	// constantes statiques
    public static final int MODE_SELECTION 	   = 0;
	public static final int MODE_ACHAT 		   = 1;
	private static final long serialVersionUID = 1L;
	private static final ImageIcon I_AMELIORER = new ImageIcon("img/icones/hammer.png");
    private static final ImageIcon I_VENDRE    = new ImageIcon("img/icones/coins_add.png");
    private static final String TXT_AMELIORER  = "Ameliorer";
    private static final String TXT_VENDRE     = "Vendre";
    private static final String TXT_PRIX_ACHAT = "Prix d'achat";
    private static final String TXT_PRIX_TOTAL = "Prix total";
    private static final Dimension DIMENSION_PANEL = new Dimension(280, 250);
    private static final Border BORDURE        = BorderFactory.createTitledBorder("Tour");
    
	// membres graphiques
	private JLabel lNom 			 = new JLabel();
	private JLabel lDegats 			 = new JLabel();
	private JLabel lRayonPortee      = new JLabel();
	private JLabel lPrix 			 = new JLabel();
	private JLabel lTitrePrix 		 = new JLabel();
	private JLabel lCadenceTir	     = new JLabel();
	private JTextArea taDescrition 	 = new JTextArea();
	private JPanel pBoutons 		 = new JPanel(new FlowLayout());
	private JPanel pCaracteristiques = new JPanel(new GridBagLayout());
	private JLabel lType             = new JLabel();
	private JButton bAmeliorer       = new JButton(TXT_AMELIORER,I_AMELIORER);
    private JButton bVendre          = new JButton(TXT_VENDRE,I_VENDRE);
	
    // autres membres
	private Tour tour;
	private Fenetre_Jeu fenJeu;
    private boolean partieTerminee;
	
	/**
	 * Constructeur du panel
	 * 
	 * @param la fenetre de jeu parent
	 */
	public Panel_InfoTour(Fenetre_Jeu fenJeu)
	{
		// construction du panel
		super(new BorderLayout());
		setPreferredSize(DIMENSION_PANEL);
		setBorder(BORDURE);

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
		taDescrition.setPreferredSize(new Dimension(250,60));
		taDescrition.setEditable(false);
		taDescrition.setLineWrap(true);
		taDescrition.setWrapStyleWord(true);
		taDescrition.setBackground(new Color(200,200,200));
		taDescrition.setBorder(new EmptyBorder(5,5,5,5));
		taDescrition.setBorder(new LineBorder(Color.DARK_GRAY,2,true));
		
		//lDescrition.setBackground(new Color(pCaracteristiques.getBackground().getRGB()));
		
		ajouterChamp(pCaracteristiques, taDescrition, 0, nbChamp++, 2);
	

		JPanel pConteneurCaract = new JPanel();
		pConteneurCaract.add(pCaracteristiques,BorderLayout.NORTH);
		
		
		
		// les boutons
		pBoutons.add(bAmeliorer);
		pBoutons.add(bVendre);
		bAmeliorer.addActionListener(this);
		bVendre.addActionListener(this);
		
		
		//add(lNom,BorderLayout.NORTH);
		JPanel p = new JPanel(new BorderLayout());
		p.add(pConteneurCaract,BorderLayout.NORTH);
		p.add(pBoutons,BorderLayout.SOUTH);
		
		add(p,BorderLayout.WEST);
	
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
			lRayonPortee.setText(" : "+String.format(" %.1f", tour.getRayonPortee()));
			taDescrition.setText(tour.getDescription());
			lNom.setForeground(tour.getCouleurDeFond());
			lNom.setText(tour.getNom());
			lCadenceTir.setText(" : "+String.format(" %.1f", tour.getCadenceTir())+" / sec.");
			lType.setText(" : "+tour.getTexteType());
			
			// tour selectionnee pour information
			if(mode == 0)
			{
				// adaptation des champs
				lTitrePrix.setText(TXT_PRIX_TOTAL);
				lPrix.setText(" : "+tour.getPrixTotal());
				
				pBoutons.setVisible(true);
				
				if(!partieTerminee)
				{
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

    public void partieTerminee()
    {
        bAmeliorer.setEnabled(false);
        bVendre.setEnabled(false);
        partieTerminee = true;
    }
}
