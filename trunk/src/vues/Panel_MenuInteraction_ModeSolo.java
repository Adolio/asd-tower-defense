package vues;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import models.creatures.*;
import models.jeu.Jeu;
import models.jeu.ModeDeJeu;
import models.tours.*;

/**
 * Panel de gestion des interactions avec le joueur.
 * <p>
 * Ce panel contient 3 zones :
 * <p>
 * - Information du l'etat du jeu (score, pieces d'or, etc)<br>
 * - Choix des tours pour ajout<br>
 * - Information sur une tour selectionnee<br>
 * 
 * @author Aurélien Da Campo
 * @version 1.1 | juin 2010
 * @since jdk1.6.0_16
 * @see JPanel
 * @see ActionListener
 */
public class Panel_MenuInteraction_ModeSolo extends JPanel										 
{
    private static final long serialVersionUID = 1L;
    
	// panels internes
	private Panel_InfosJoueurEtPartie pInfosJoueurEtPartie;
	private Panel_AjoutTour pAjoutTour;
	private Panel_Selection pSelection;

	/**
	 * Constructeur du panel d'interaction
	 * 
	 * @param jeu le jeu avec lequel on interagit (le model)
	 * @param fenJeu la fenetre de jeu
	 */
	public Panel_MenuInteraction_ModeSolo(EcouteurDePanelTerrain edpt,Jeu jeu)
	{
		super(new BorderLayout());
		setBackground(LookInterface.COULEUR_DE_FOND_PRI);
		
		//---------------------
		//-- panels internes --
		//---------------------
		
		JPanel pToursEtJoueur = new JPanel(new BorderLayout());
        pToursEtJoueur.setOpaque(false);
		
        pInfosJoueurEtPartie = new Panel_InfosJoueurEtPartie(jeu, ModeDeJeu.MODE_SOLO);
        pToursEtJoueur.add(pInfosJoueurEtPartie,BorderLayout.NORTH);
        
        pAjoutTour = new Panel_AjoutTour(jeu, edpt, 280, 80);
        pToursEtJoueur.add(pAjoutTour,BorderLayout.SOUTH);
		
	    add(pToursEtJoueur,BorderLayout.NORTH);
		
	    pSelection = new Panel_Selection(edpt);
        add(pSelection,BorderLayout.CENTER);

	}
	
	/**
	 * Permet d'avertir le composants contenu dans le menu qu'une tour 
	 * a ete selectionnee
	 * @param tour la tour selectionnee
	 * @param mode le mode de selection
	 */
	public void setTourSelectionnee(Tour tour, int mode)
	{
		pSelection.setSelection(tour, mode);
	}
	
	/**
     * Permet d'avertir le composants contenu dans le menu qu'une creature 
     * a ete selectionnee
     * @param creature la creature selectionnee
     */
	public void setCreatureSelectionnee(Creature creature)
    {
        pSelection.setSelection(creature, 0);
    }

    /**
     * Permet d'informer le panel que la partie est terminee
     */
    public void partieTerminee()
    {
        // informe le panel des tours
        pSelection.partieTerminee();
        pAjoutTour.partieTerminee();
        pInfosJoueurEtPartie.partieTerminee();
    }

    /**
     * Permet de recuperer le panel d'info tour
     * @return le panel d'info tour
     */
    public Panel_InfoTour getPanelInfoTour()
    {
        return pSelection.getPanelInfoTour();
    }
    
    /**
     * Permet de recuperer le panel d'info créature
     * @return le panel d'info créature
     */
    public Panel_InfoCreature getPanelInfoCreature()
    {
        return pSelection.getPanelInfoCreature();
    }

    /**
     * Permet d'indiquer que le jeu est en pause ou non.
     * 
     * @param enPause si le jeu est en pause.
     */
    public void setPause(boolean enPause)
    {
        pSelection.setPause(enPause);
        pAjoutTour.setPause(enPause);
        pInfosJoueurEtPartie.setPause(enPause);
    }

    public void miseAJourInfoJoueur()
    {
        pInfosJoueurEtPartie.miseAJour();
        pAjoutTour.miseAJour();
    }
}
