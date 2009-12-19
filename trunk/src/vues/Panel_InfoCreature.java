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
	
	// membres
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
	//private static final Border BORDURE = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	private static final Border BORDURE = BorderFactory.createTitledBorder("Creature");
	
	/**
	 * Constructeur du panel
	 */
	public Panel_InfoCreature()
	{
		// construction du panel
		super(new BorderLayout());
		setBorder(BORDURE);
		setPreferredSize(DIMENSION_PANEL);
		
		pConteneur = new JPanel(new GridLayout(0,2));
		
	
		
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.setPreferredSize(new Dimension(140,50));
		p.add(lImage);
		lNom.setFont(new Font("", Font.BOLD, 14));
		p.add(lNom);
		p.setBorder(new EmptyBorder(-5,-5,-5,-5));
		
		
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
	
	public void effacerCreature()
	{
		setCreature(null);
	}
	
	/**
	 * Changement de la creature
	 * 
	 * Met à jour le panel pour afficher les bonnes informations
	 * 
	 * @param creature La creature a afficher
	 */
	public void setCreature(Creature creature)
	{
		// creature ou pas ?
		if(creature != null)
		{
		    pConteneur.setVisible(true);
			
			lImage.setIcon(new ImageIcon(creature.getImage()));
			lNom.setText(creature.getNom());
			lTitreType.setText(" ["+creature.getNomType()+"]");
			lSante.setText(" : "+creature.getSante()+" / "+creature.getSanteMax());
			lVitesse.setText(" : "+String.format(" %.1f",creature.getVitesse()));
			lGain.setText(" : "+creature.getNbPiecesDOr());
		}
		// mode sans creature selectionnee
		else
		    pConteneur.setVisible(false);
	}
}
