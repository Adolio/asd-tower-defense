package vues;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
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
	//private static final ImageIcon I_AMELIORER = new ImageIcon("img/icones/upgrade.png");
    //private static final ImageIcon I_VENDRE    = new ImageIcon("img/icones/sale.png");
    private static final String TXT_AMELIORER  = "Améliorer";
    private static final String TXT_VENDRE     = "Vendre";
    private static final String TXT_PRIX_ACHAT = "Prix d'achat";
    private static final String TXT_PRIX_TOTAL = "Valeur,Prix";
    private static final Dimension DIMENSION_PANEL = new Dimension(280, 300);
    private static final Dimension DIMENSION_DESCRIPTION = new Dimension(240,120);
    
	// membres graphiques
	private JLabel lNom 			 = new JLabel();
	private JLabel lDegats 			 = new JLabel();
	private JLabel lRayonPortee      = new JLabel();
	private JLabel lPrix 			 = new JLabel();
	private JLabel lTitrePrix 		 = new JLabel();
	private JLabel lCadenceTir	     = new JLabel();
	private JLabel lDPS              = new JLabel();
	private JTextArea taDescrition 	 = new JTextArea();
	private JScrollPane spDescription;
	private JPanel pCaracteristiques = new JPanel(new GridBagLayout());
	private JButton bAmeliorer       = new JButton(TXT_AMELIORER);//I_AMELIORER);
    private JButton bVendre          = new JButton(TXT_VENDRE);//I_VENDRE);
	
    private JLabel lPrixLvlS         = new JLabel();
    private JLabel lTitreLvl         = new JLabel();
    private JLabel lTitreLvlS        = new JLabel();
    private JLabel lDegatsLvlS       = new JLabel();
    private JLabel lRayonPorteeLvlS  = new JLabel();
    private JLabel lCadenceTirLvlS   = new JLabel();
    private JLabel lDPSLvlS          = new JLabel();
      
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
		setOpaque(false);
		
		setPreferredSize(DIMENSION_PANEL);
		//setBorder(BORDURE);
		//setBackground(LookInterface.COULEUR_DE_FOND);
		
		
		this.edpt = edpt;

		
		
		//JLabel lblTitre = new JLabel("Tour selectionnee");
		//lblTitre.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
		//add(lblTitre,BorderLayout.NORTH);

		JPanel pConteneurCaract = new JPanel();
		pConteneurCaract.setOpaque(false);
		int nbChamp = 0;
		pCaracteristiques.setOpaque(false);
		
		
		// champ nom
		lNom.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
		ajouterChamp(pCaracteristiques, lNom, 0, nbChamp++, 3);
		
		// champ description
        taDescrition.setEditable(false);
        taDescrition.setLineWrap(true);
        taDescrition.setWrapStyleWord(true);
        taDescrition.setBackground(LookInterface.COULEUR_DE_FOND);
        //taDescrition.setBorder(BORDURE_DESCRIPTION);
        taDescrition.setFont(GestionnaireDesPolices.POLICE_VALEUR_CHAMP);
        spDescription = new JScrollPane(taDescrition);
        spDescription.setPreferredSize(DIMENSION_DESCRIPTION);
        ajouterChamp(pCaracteristiques, spDescription, 0, nbChamp++, 3);
		
		lTitreLvl.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
		lTitreLvlS.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
		ajouterChamp(pCaracteristiques, lTitreLvl, 1, nbChamp, 1);
		ajouterChamp(pCaracteristiques, lTitreLvlS, 2, nbChamp++, 1);
		
		// champ prix
		ajouterChamp(pCaracteristiques, lTitrePrix, 0, nbChamp, 1);
		lPrix.setFont(GestionnaireDesPolices.POLICE_VALEUR_CHAMP);
		lPrixLvlS.setFont(GestionnaireDesPolices.POLICE_VALEUR_CHAMP);
		ajouterChamp(pCaracteristiques, lPrix, 1, nbChamp, 1);
        ajouterChamp(pCaracteristiques, lPrixLvlS, 2, nbChamp++, 1);
		
		// champ degats
		ajouterChamp(pCaracteristiques, new JLabel("Dégâts"), 0, nbChamp, 1);
		lDegats.setFont(GestionnaireDesPolices.POLICE_VALEUR_CHAMP);
		lDegatsLvlS.setFont(GestionnaireDesPolices.POLICE_VALEUR_CHAMP);
		ajouterChamp(pCaracteristiques, lDegats, 1, nbChamp, 1);
		ajouterChamp(pCaracteristiques, lDegatsLvlS, 2, nbChamp++, 1);
		
		// champ rayon de portee
		ajouterChamp(pCaracteristiques, new JLabel("Portée"), 0, nbChamp, 1);
		lRayonPortee.setFont(GestionnaireDesPolices.POLICE_VALEUR_CHAMP);
		lRayonPorteeLvlS.setFont(GestionnaireDesPolices.POLICE_VALEUR_CHAMP);
		ajouterChamp(pCaracteristiques, lRayonPortee, 1, nbChamp, 1);
		ajouterChamp(pCaracteristiques, lRayonPorteeLvlS, 2, nbChamp++, 1);
		
		// champ cadence de tir
		ajouterChamp(pCaracteristiques, new JLabel("Tirs / sec."), 0, nbChamp, 1);
		lCadenceTir.setFont(GestionnaireDesPolices.POLICE_VALEUR_CHAMP);
		lCadenceTirLvlS.setFont(GestionnaireDesPolices.POLICE_VALEUR_CHAMP);
		ajouterChamp(pCaracteristiques, lCadenceTir, 1, nbChamp, 1);
		ajouterChamp(pCaracteristiques, lCadenceTirLvlS, 2, nbChamp++, 1);
		
		// champ DPS : dégats par seconde
		ajouterChamp(pCaracteristiques, new JLabel("DPS"), 0, nbChamp, 1);
		lDPS.setFont(GestionnaireDesPolices.POLICE_VALEUR_CHAMP);
		lDPSLvlS.setFont(GestionnaireDesPolices.POLICE_VALEUR_CHAMP);
        ajouterChamp(pCaracteristiques, lDPS, 1, nbChamp, 1);
        ajouterChamp(pCaracteristiques, lDPSLvlS, 2, nbChamp++, 1);
		
		// les boutons
        ajouterChamp(pCaracteristiques, bVendre, 0, nbChamp, 2);
        ajouterChamp(pCaracteristiques, bAmeliorer, 2, nbChamp++, 1);
        
        Font f = new Font("", Font.BOLD, 9);
        
        bAmeliorer.setFont(f);
        bVendre.setFont(f);
        bAmeliorer.setPreferredSize(new Dimension(50,30));
        bVendre.setPreferredSize(new Dimension(50,30));
        GestionnaireDesPolices.setStyle(bAmeliorer);
        GestionnaireDesPolices.setStyle(bVendre);
        bAmeliorer.addActionListener(this);
        bVendre.addActionListener(this);
		
	
		pConteneurCaract.add(pCaracteristiques,BorderLayout.CENTER);
		
		JPanel pConteneurCaraEtBoutons = new JPanel(new BorderLayout());
		pConteneurCaraEtBoutons.setPreferredSize(new Dimension(260,160));
		pConteneurCaraEtBoutons.setBackground(LookInterface.COULEUR_DE_FOND_2);
		pConteneurCaraEtBoutons.add(pConteneurCaract,BorderLayout.NORTH);
		//pConteneurCaraEtBoutons.add(pBoutons,BorderLayout.SOUTH);
		
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
	private GridBagConstraints gbc = new GridBagConstraints();
	public void ajouterChamp(JPanel panel, Component composant, int gridx, int gridy, int gridwidth)
	{
		gbc.fill    = GridBagConstraints.HORIZONTAL;
		gbc.insets  = new Insets(1, 8, 1, 8);
		gbc.gridx 	= gridx;
		gbc.gridy 	= gridy;
		gbc.gridwidth = gridwidth;
		
		panel.add(composant,gbc);
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
		    lNom.setIcon(new ImageIcon(tour.getIcone()));
		    lNom.setForeground(tour.getCouleurDeFond());
            lNom.setText(tour.getNom()+" lvl. "+tour.getNiveau()+" ["+tour.getTexteType()+"]");
            lTitreLvl.setText("lvl. "+tour.getNiveau());
            lDegats.setText(tour.getDegats()+"");
			lRayonPortee.setText(String.format("%.1f", tour.getRayonPortee()));
			lCadenceTir.setText(String.format("%.1f", tour.getCadenceTir()));
			lDPS.setText(String.format("%.1f", tour.getCadenceTir()*tour.getDegats()));
			taDescrition.setText(tour.getDescription());
			
			// reset des scroll bars
			repaint();
			JScrollBar verticalScrollBar = spDescription.getVerticalScrollBar();
		    JScrollBar horizontalScrollBar = spDescription.getHorizontalScrollBar();
		    verticalScrollBar.setValue(verticalScrollBar.getMinimum());
		    horizontalScrollBar.setValue(horizontalScrollBar.getMinimum());
		    
		    updateUI();
		    
			// Améliorations
			if(tour.peutEncoreEtreAmelioree())
			{
			    lTitreLvlS.setText("lvl. "+(tour.getNiveau()+1));
			    lPrixLvlS.setText(tour.getPrixAchat()+"");
			    lDegatsLvlS.setText(tour.getDegatsLvlSuivant()+"");
			    lRayonPorteeLvlS.setText(String.format("%.1f",tour.getRayonPorteeLvlSuivant()));
			    lCadenceTirLvlS.setText(String.format("%.1f",tour.getCadenceTirLvlSuivant()));
			    lDPSLvlS.setText(String.format("%.1f", tour.getCadenceTirLvlSuivant()*tour.getDegatsLvlSuivant()));
			}
			else
			{
			    lTitreLvlS.setText("");
			    lPrixLvlS.setText("");
			    lDegatsLvlS.setText("");
                lRayonPorteeLvlS.setText("");
                lCadenceTirLvlS.setText("");
                lDPSLvlS.setText("");
                
			}
			
			// tour selectionnee pour information
			if(mode == 0)
			{
				// adaptation des champs
				lTitrePrix.setText(TXT_PRIX_TOTAL);
				lPrix.setText(tour.getPrixTotal()+"");

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
    					bAmeliorer.setText(TXT_AMELIORER+"["+tour.getPrixAchat()+"]");
    				}
    				else
    				{
    					bAmeliorer.setText("[niveau max]");
    					bAmeliorer.setEnabled(false);
    				}
				}
				
				bVendre.setText(TXT_VENDRE+"["+tour.getPrixDeVente()+"]");
	
				bVendre.setVisible(true);
				bAmeliorer.setVisible(true);
			}
			// tour selectionnee pour achat
			else if(mode == 1)
			{
				// adaptation des champs
				lTitrePrix.setText(TXT_PRIX_ACHAT);
				lPrix.setText(tour.getPrixAchat()+"");
				
				// adaptation des boutons
				bVendre.setVisible(false);
                bAmeliorer.setVisible(false);
			}
			
			// sauvegarde de la tour pour les operations
			this.tour = tour;
			
			pCaracteristiques.setVisible(true);
		}
		// mode sans tour selectionnee
		else
		{
			pCaracteristiques.setVisible(false);
			bVendre.setVisible(false);
            bAmeliorer.setVisible(false);
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
