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
 * C'est dans ce panel que le joueur peut ameliorer une tour ou la vendre.
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
    private static final Dimension DIMENSION_PANEL = new Dimension(280, 300);
    private static final Border BORDURE        = BorderFactory.createTitledBorder("Tour  sélectionnée");
    private static final Dimension DIMENSION_DESCRIPTION = new Dimension(250,80);
    private static final Border BORDURE_DESCRIPTION = new EmptyBorder(10,10,10,10);
    //private static final Border BORDURE_DESCRIPTION = new LineBorder(Color.DARK_GRAY,1,true);
    
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
	private EcouteurDePanelTerrain edpt;
    private boolean partieTerminee;
    private boolean enPause;
	
	/**
	 * Constructeur du panel
	 * 
	 * @param la fenetre de jeu parent
	 */
	public Panel_InfoTour(EcouteurDePanelTerrain edpt)
	{
		// construction du panel
		super(new BorderLayout());
		setPreferredSize(DIMENSION_PANEL);
		//setBorder(BORDURE);
		setBackground(LookInterface.COULEUR_DE_FOND);
		
		
		this.edpt = edpt;

		
		add(new JLabel("Tour sélectionnée"),BorderLayout.NORTH);

		JPanel pConteneurCaract = new JPanel();
		pConteneurCaract.setOpaque(false);
		int nbChamp = 0;
		pCaracteristiques.setOpaque(false);
		
		
		// champ nom
		lNom.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
		ajouterChamp(pCaracteristiques, lNom, 0, nbChamp++, 2);
		
		// champ prix
		ajouterChamp(pCaracteristiques, lTitrePrix, 0, nbChamp, 1);
		lPrix.setFont(GestionnaireDesPolices.POLICE_VALEUR_CHAMP);
		ajouterChamp(pCaracteristiques, lPrix, 1, nbChamp++, 1);
		
		// champ degats
		ajouterChamp(pCaracteristiques, new JLabel("Degats"), 0, nbChamp, 1);
		lDegats.setFont(GestionnaireDesPolices.POLICE_VALEUR_CHAMP);
		ajouterChamp(pCaracteristiques, lDegats, 1, nbChamp++, 1);
	
		// champ rayon de portee
		ajouterChamp(pCaracteristiques, new JLabel("Rayon de portee"), 0, nbChamp, 1);
		lRayonPortee.setFont(GestionnaireDesPolices.POLICE_VALEUR_CHAMP);
		ajouterChamp(pCaracteristiques, lRayonPortee, 1, nbChamp++, 1);
		
		// champ cadence de tir
		ajouterChamp(pCaracteristiques, new JLabel("Cadence de tir"), 0, nbChamp, 1);
		lCadenceTir.setFont(GestionnaireDesPolices.POLICE_VALEUR_CHAMP);
		ajouterChamp(pCaracteristiques, lCadenceTir, 1, nbChamp++, 1);

		// champ type de tir
		ajouterChamp(pCaracteristiques, new JLabel("Type de tir"), 0, nbChamp, 1);
		lType.setFont(GestionnaireDesPolices.POLICE_VALEUR_CHAMP);
        ajouterChamp(pCaracteristiques, lType, 1, nbChamp++, 1);

		// champ description
		taDescrition.setPreferredSize(DIMENSION_DESCRIPTION);
		taDescrition.setEditable(false);
		taDescrition.setLineWrap(true);
		taDescrition.setWrapStyleWord(true);
		taDescrition.setBackground(LookInterface.COULEUR_DE_FOND);
		taDescrition.setBorder(BORDURE_DESCRIPTION);
		ajouterChamp(pCaracteristiques, taDescrition, 0, nbChamp++, 2);
	
		pConteneurCaract.add(pCaracteristiques,BorderLayout.CENTER);
		
		// les boutons
		pBoutons.setOpaque(false);
		pBoutons.add(bAmeliorer);
		pBoutons.add(bVendre);
		bAmeliorer.addActionListener(this);
		bVendre.addActionListener(this);
		
		
		JPanel pConteneurCaraEtBoutons = new JPanel(new BorderLayout());
		pConteneurCaraEtBoutons.setPreferredSize(new Dimension(260,160));
		pConteneurCaraEtBoutons.setBackground(LookInterface.COULEUR_DE_FOND_2);
		pConteneurCaraEtBoutons.add(pConteneurCaract,BorderLayout.NORTH);
		pConteneurCaraEtBoutons.add(pBoutons,BorderLayout.SOUTH);
		
		add(pConteneurCaraEtBoutons,BorderLayout.WEST);
	
		// initialisation a vide
		effacerTour();
	}
	
	/**
	 * Permet d'ajouter un champ dans un GridBagLayout
	 * 
	 * @param panel le GridBagLayout
	 * @param composant le composant a ajouter
	 * @param gridx position x dans la grille
	 * @param gridy position y dans la grille
	 * @param gridwidth largeur de la grille
	 */
	public void ajouterChamp(JPanel panel, Component composant, int gridx, int gridy, int gridwidth)
	{
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill 		= GridBagConstraints.HORIZONTAL;
		c.gridx 	= gridx;
		c.gridy 	= gridy;
		c.gridwidth = gridwidth;
		
		panel.add(composant,c);
	}
	
	/**
	 * Permet de deselectionner la tour affichee
	 */
	public void effacerTour()
	{
		setTour(null, 0);
	}
	
	/**
     * Permet d'informer de le panel que la partie est terminee.
     * 
     * Bloque tous les boutons.
     */
    public void partieTerminee()
    {
        bAmeliorer.setEnabled(false);
        bVendre.setEnabled(false);
        partieTerminee = true;
    }
	
	/**
	 * Permet de changer de tour
	 * 
	 * Met a jour le panel pour afficher les bonnes informations
	 * 
	 * @param tour La tour a gerer
	 */
	public void setTour(Tour tour, int mode)
	{
		// tour ou pas ?
		if(tour != null)
		{
			// mise a jour des caracteristiques
		    lDegats.setText(" : "+tour.getDegats());
			lRayonPortee.setText(" : "+String.format("%.1f", tour.getRayonPortee()));
			taDescrition.setText(tour.getDescription());
			lNom.setForeground(tour.getCouleurDeFond());
			lNom.setText(tour.getNom());
			lCadenceTir.setText(" : "+String.format("%.1f", tour.getCadenceTir())+" tirs / sec.");
			lType.setText(" : "+tour.getTexteType());
			
			// tour selectionnee pour information
			if(mode == 0)
			{
				// adaptation des champs
				lTitrePrix.setText(TXT_PRIX_TOTAL);
				lPrix.setText(" : "+tour.getPrixTotal());

				if(!partieTerminee)
				{
    				if(enPause)
    				{
    				    bAmeliorer.setEnabled(false);
    				    bVendre.setEnabled(false);
    				}
				    // adaptation des boutons
    				else if(tour.peutEncoreEtreAmelioree())
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
				pBoutons.setVisible(true);
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
			
			pCaracteristiques.setVisible(true);
		}
		// mode sans tour selectionnee
		else
		{
			pCaracteristiques.setVisible(false);
			pBoutons.setVisible(false);
		}
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
		
		if(source == bAmeliorer)
			edpt.ameliorerTour(tour);
		
		else if(source == bVendre)
			edpt.vendreTour(tour);
	}

    public void setPause(boolean enPause)
    {
        this.enPause = enPause;
        
        bAmeliorer.setEnabled(!enPause);
        bVendre.setEnabled(!enPause);
    }
}
