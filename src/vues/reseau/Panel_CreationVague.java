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

package vues.reseau;

import i18n.Langue;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import vues.GestionnaireDesPolices;
import vues.LookInterface;
import vues.commun.Panel_Table;

import exceptions.ArgentInsuffisantException;
import models.creatures.*;
import models.jeu.Jeu;
import models.joueurs.GestionnaireDeRevenu;
import models.joueurs.Joueur;

/**
 * Panel de création d'une vague de créature
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | juin 2010
 * @since jdk1.6.0_16
 */
public class Panel_CreationVague extends JPanel
{
    private static final long serialVersionUID = 1L;
    private Panel_Table tb = new Panel_Table();
   
    private Creature[] creatures = new Creature[]
    {
        TypeDeCreature.getCreature(0,1,true),
        TypeDeCreature.getCreature(1,1,true),
        TypeDeCreature.getCreature(2,1,true),
        TypeDeCreature.getCreature(3,1,true),
        TypeDeCreature.getCreature(4,1,true),
        TypeDeCreature.getCreature(5,1,true),
        TypeDeCreature.getCreature(6,1,true)
    };
    
    Jeu jeu;
    
    private JButton[] bLancers = new JButton[creatures.length];
    /*
    private JComboBox[] cbNbCreatures = new JComboBox[creatures.length];
    */
    public Panel_CreationVague(final Jeu jeu, final Joueur cible, 
            final EcouteurDeLanceurDeVagues edlv)                          
    {
        super(new BorderLayout());
        
        this.jeu = jeu;
        setBackground(LookInterface.COULEUR_DE_FOND_PRI);
        
        /*
        JLabel titre = new JLabel("Lancement des creatures");
        titre.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        add(titre,BorderLayout.NORTH);
        */
        
        tb.setBackground(LookInterface.COULEUR_DE_FOND_SEC);
        tb.add(new JLabel(Langue.getTexte(Langue.ID_TXT_CREATURE)),0,0);
        tb.add(new JLabel(Langue.getTexte(Langue.ID_TXT_PRIX)),1,0);
        tb.add(new JLabel(Langue.getTexte(Langue.ID_TXT_REVENU)),2,0);
        
        for(int i=0;i < creatures.length;i++)
        {
            final Creature creature = creatures[i];
            
            ImageIcon image = new ImageIcon(creature.getImage());
            /*
            final JComboBox cbNbCreatures = new JComboBox();
            
            this.cbNbCreatures[i] = cbNbCreatures;
            cbNbCreatures.addItem("1");
            cbNbCreatures.addItem("2");
            cbNbCreatures.addItem("3");
            cbNbCreatures.addItem("4");
            cbNbCreatures.addItem("5");
            cbNbCreatures.addItem("10");
            cbNbCreatures.addItem("15");
            cbNbCreatures.addItem("20");
            cbNbCreatures.addItem("30");
            */
            /*
            cbNbCreatures.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                   miseAJour();
                }
            });
            */
            
            JPanel p = new JPanel(new FlowLayout());
            p.setOpaque(false);
            p.add(new JLabel(image));
            //p.add(new JLabel(" x "));
            //p.add(cbNbCreatures);
            tb.add(p,0,i+1);    
            
            tb.add(new JLabel(""+creature.getNbPiecesDOr()),1,i+1);    
            tb.add(new JLabel(""+creature.getNbPiecesDOr() * 
                    GestionnaireDeRevenu.POURCENTAGE_NB_PIECES_OR_CREATURE),2,i+1); 
               
            JButton bLancer = new JButton(Langue.getTexte(Langue.ID_TXT_BTN_LANCER));
            bLancers[i] = bLancer;
            tb.add(bLancer,3,i+1);
            
            GestionnaireDesPolices.setStyle(bLancer);
            
            bLancer.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    VagueDeCreatures vague = new VagueDeCreatures(1/*Integer.parseInt((String) cbNbCreatures.getSelectedItem())*/, creature, VagueDeCreatures.getTempsLancement(creature.getVitesseNormale()));
                    
                    try
                    {
                        edlv.lancerVague(vague);
                        miseAJour();
                    } 
                    catch (ArgentInsuffisantException e1)
                    {
                        edlv.erreurPasAssezDArgent();
                    } 
                }
            });
            
        }
        
        add(tb,BorderLayout.CENTER);
    }
    
    public void miseAJour()
    {
       double nbPiecesDOr = jeu.getJoueurPrincipal().getNbPiecesDOr();
       JButton bouton;
       for(int i=0; i < bLancers.length; i++)
       {
           bouton = bLancers[i];
           
           int nbCreatures = 1;//Integer.parseInt((String) cbNbCreatures[i].getSelectedItem());

           bouton.setEnabled(nbPiecesDOr >= creatures[i].getNbPiecesDOr() * nbCreatures);
       }
    }
}
