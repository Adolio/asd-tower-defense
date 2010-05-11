package vues;

import java.awt.BorderLayout;

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
		
		Panel_InfoCreature pInfoTour = new Panel_InfoCreature();
		add(pInfoTour,BorderLayout.NORTH);
		pInfoTour.setVisible(false);
		
		Panel_InfoTour pInfoCreature = new Panel_InfoTour(edpt);
        add(pInfoCreature,BorderLayout.SOUTH);
        pInfoCreature.setVisible(false);
	}
	
	/**
     * Permet d'informer de le panel que la partie est terminee.
     * 
     * Bloque tous les boutons.
     */
    public void partieTerminee()
    {
        
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
		// Tour
	    if(Tour.class.isInstance(selection))
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
    }
}
