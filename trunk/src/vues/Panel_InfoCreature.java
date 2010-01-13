package vues;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import models.creatures.*;

/**
 * Classe de gestion d'affichage d'information d'une creature.
 * 
 * Le joueur pourra voir les proprietes d'une creature caracterisee par
 * sa sante, son type (terrestre ou aerienne), sa vitesse, etc.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 18 decembre 2009
 * @since jdk1.6.0_16
 * @see Creature
 */
public class Panel_InfoCreature extends JPanel
{
    // constante statiques
    private static final long serialVersionUID = 1L;
    private static final ImageIcon I_SANTE     = new ImageIcon("img/icones/heart.png");
	private static final ImageIcon I_VITESSE   = new ImageIcon("img/icones/running_man.gif");
	private static final ImageIcon I_GAIN      = new ImageIcon("img/icones/coins_add.png");
	private static final Dimension DIMENSION_PANEL = new Dimension(280, 120);
	private static final Border BORDURE        = BorderFactory.createTitledBorder("Créature sélectionnée");
    private static final Dimension DIMENSION_IMAGE_ET_NOM = new Dimension(110,50);
    private static final Font POLICE_NOM       = new Font("", Font.BOLD, 14);
    private static final Border BORDURE_IMAGE_ET_NOM = new EmptyBorder(-5,-5,-5,-5);
    private static final Font POLICE_DONNEES = new Font("Verdana", Font.BOLD, 12);
    
	// attributs
	private JLabel lTitreType       = new JLabel("Type");
	private JLabel lTitreSante      = new JLabel("Santé",I_SANTE,JLabel.LEFT);
	private JLabel lTitreVitesse    = new JLabel("Vitesse",I_VITESSE,JLabel.LEFT);
	private JLabel lTitreGain       = new JLabel("Gain pièces",I_GAIN,JLabel.LEFT);
	private JLabel lSante           = new JLabel();
	private JLabel lVitesse         = new JLabel();
	private JLabel lGain            = new JLabel();
	private JLabel lImage           = new JLabel();
    private JLabel lNom             = new JLabel();
    private JPanel pConteneur;
    private Creature creature;
		
	/**
	 * Constructeur du panel
	 */
	public Panel_InfoCreature()
	{
		// construction du panel
		super(new BorderLayout());
		setBorder(BORDURE);
		setPreferredSize(DIMENSION_PANEL);
		
		lNom.setFont(POLICE_NOM);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.setPreferredSize(DIMENSION_IMAGE_ET_NOM);
		p.setBorder(BORDURE_IMAGE_ET_NOM);
		p.add(lImage);
		p.add(lNom);
		
		
		lSante.setFont(POLICE_DONNEES);
		lVitesse.setFont(POLICE_DONNEES);
		lGain.setFont(POLICE_DONNEES);
		
		pConteneur = new JPanel(new GridLayout(0,2));
		pConteneur.add(p);
		pConteneur.add(lTitreType);
		pConteneur.add(lTitreSante);
        pConteneur.add(lSante);
        pConteneur.add(lTitreVitesse);
        pConteneur.add(lVitesse);
        pConteneur.add(lTitreGain);
        pConteneur.add(lGain);
        add(pConteneur,BorderLayout.WEST);
        
        pConteneur.setVisible(false);
	}
	
	/**
	 * Permet d'effacer la creature
	 */
	public void effacerCreature()
	{
		setCreature(null);
	}
	
	/**
	 * Permet de changement la creature courante
	 * 
	 * Met a jour le panel pour afficher les bonnes informations
	 * 
	 * @param creature La creature a afficher
	 */
	public void setCreature(Creature creature)
	{
	    this.creature = creature;
	    
	    // creature ou pas ?
		if(creature != null)
		{
			// mise a jour des champs
		    lImage.setIcon(new ImageIcon(creature.getImage()));
			lNom.setText(creature.getNom());
			lTitreType.setText(" ["+creature.getNomType()+"]");
			lGain.setText(" : "+creature.getNbPiecesDOr());
			
			miseAJourInfosVariables();

			// affichage du panel
			pConteneur.setVisible(true);
		}
		// mode sans creature selectionnee
		else
		    pConteneur.setVisible(false);
	}

	/**
	 * Permet de mettre a jour les informations variable de la creature
	 */
    public void miseAJourInfosVariables()
    {
        if(creature != null)
        {
            lSante.setText(" : "+formaterSante(creature.getSante())+" / "+
                                 formaterSante(creature.getSanteMax()));

            // vitesse
            if(creature.getCoeffRalentissement() > 0.0)
            {
                lVitesse.setForeground(Color.BLUE);
                lVitesse.setText(" : "+String.format("%.1f",creature.getVitesseReelle())
                                +" (-"+(creature.getCoeffRalentissement() * 100.0)+"%)");
            }
            else
            {
                lVitesse.setForeground(Color.BLACK);
                lVitesse.setText(" : "+String.format("%.1f",creature.getVitesseNormale()));
            }
        }
    }
    
    static final long KILO  = 1000L;
    static final long MEGA  = 1000000L;
    static final long GIGA  = 1000000000L;
    static final long TERRA = 1000000000000L;
    
    /**
     * Permet de formatter la sante des creature afin de minimiser la place.
     * 
     * @param sante 
     * 
     * @return la sante sous la forme 5000 = 5M, 3000000 = 3G
     */
    private String formaterSante(long sante)
    { 
        long tmp;
        if(sante >= KILO && sante < MEGA)
        {    
            tmp = sante / KILO;
            return tmp + "." + ((sante % KILO) / (KILO / 10)) + "K"; // kilo
        }
        else if(sante >= MEGA && sante < GIGA)
        {    
            tmp = sante / MEGA;
            return tmp + "." + ((sante % MEGA) / (MEGA / 10)) + "M"; // mega
        }
        else if(sante >= GIGA && sante < TERRA)
        {
            tmp = sante / GIGA;
            return tmp + "." + ((sante % GIGA) / (GIGA / 10)) + "G"; // giga
        }
        else if(sante >= TERRA)
        {
            tmp = sante / TERRA;
            return tmp + "." + ((sante % TERRA) / (TERRA / 10)) + "T"; // giga
        }
        else
            return sante+"";
    }
}
