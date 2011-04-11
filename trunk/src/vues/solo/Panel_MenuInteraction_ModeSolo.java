/*
  Copyright (C) 2010 Aurelien Da Campo
  
  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/

package vues.solo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import vues.LookInterface;
import vues.commun.EcouteurDePanelTerrain;
import vues.commun.Panel_AjoutTour;
import vues.commun.Panel_InfoCreature;
import vues.commun.Panel_InfoTour;
import vues.commun.Panel_InfoVagues;
import vues.commun.Panel_InfosJoueurEtPartie;
import vues.commun.Panel_Selection;
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
    private Panel_InfoVagues panelInfoVagues;

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
		
	    pSelection = new Panel_Selection(jeu, edpt);
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

    public Panel_InfoVagues getPanelInfoVagues() {
        return panelInfoVagues;
    }
}
