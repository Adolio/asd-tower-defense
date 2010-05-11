package vues;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;

import models.creatures.Creature;
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
public class Panel_Selection extends JPanel
{
	private boolean enPause;
	
	private Panel_InfoTour pInfoTour;
	private Panel_InfoCreature pInfoCreature;

    /**
	 * Constructeur du panel
	 * 
	 * @param la fenetre de jeu parent
	 */
	public Panel_Selection(EcouteurDePanelTerrain edpt)
	{
		super(new BorderLayout());
		
		setOpaque(false);
		
		setPreferredSize(new Dimension(280,0));
		//setBackground(LookInterface.COULEUR_DE_FOND_2);
		
		JLabel titre = new JLabel("Information sur la selection");
		titre.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
		add(titre,BorderLayout.NORTH);
		
		JPanel p = new JPanel(new FlowLayout());
		//p.setOpaque(false);
		p.setBackground(LookInterface.COULEUR_DE_FOND_2);
		
		
		pInfoTour = new Panel_InfoTour(edpt);
		p.add(pInfoTour);
		pInfoTour.setVisible(false);
		
	    pInfoCreature = new Panel_InfoCreature();
        p.add(pInfoCreature);
        pInfoCreature.setVisible(false);
        
        add(p,BorderLayout.CENTER);  
	}
	
	/**
     * Permet d'informer de le panel que la partie est terminee.
     * 
     * Bloque tous les boutons.
     */
    public void partieTerminee()
    {
        pInfoTour.partieTerminee();
    }
	
	/**
	 * Permet de changer de tour
	 * 
	 * Met a jour le panel pour afficher les bonnes informations
	 * 
	 * @param tour La tour a gerer
	 */
	public void setSelection(Object selection, int mode)
	{
	    if(selection == null)
	    {
	        pInfoCreature.setVisible(false);
	        pInfoTour.setVisible(false);
	    }
	    // Tour
	    else if(Tour.class.isInstance(selection))
		{
	        pInfoTour.setTour((Tour) selection, mode);
	        pInfoTour.setVisible(true);
	        pInfoCreature.setVisible(false);
		}
	    // Creature
		else if(Creature.class.isInstance(selection))
        {
		    pInfoCreature.setCreature((Creature) selection);
		    pInfoCreature.setVisible(true);
		    pInfoTour.setVisible(false);
        }
	}

    public void setPause(boolean enPause)
    {
        this.enPause = enPause;
        pInfoTour.setPause(enPause);
    }

    public Panel_InfoTour getPanelInfoTour()
    {
        return pInfoTour;
    }

    public Panel_InfoCreature getPanelInfoCreature()
    {
        return pInfoCreature;
    }
}
