package vues;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

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
    private JComboBox[] cbNbCreatures = new JComboBox[creatures.length];
    
    public Panel_CreationVague(final Jeu jeu, final Joueur cible, 
            final EcouteurDeLanceurDeVagues edlv)                          
    {
        super(new BorderLayout());
        
        this.jeu = jeu;
        setBackground(LookInterface.COULEUR_DE_FOND);
        
        
        JLabel titre = new JLabel("Lancement des creatures");
        titre.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        add(titre,BorderLayout.NORTH);
        
        tb.setBackground(LookInterface.COULEUR_DE_FOND_2);
        tb.add(new JLabel("Créature"),0,0);
        tb.add(new JLabel("Prix"),1,0);
        tb.add(new JLabel("Revenu"),2,0);
        
        for(int i=0;i < creatures.length;i++)
        {
            final Creature creature = creatures[i];
            
            ImageIcon image = new ImageIcon(creature.getImage());

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
            
            cbNbCreatures.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                   miseAJour();
                }
            });
            
            JPanel p = new JPanel(new FlowLayout());
            p.setOpaque(false);
            p.add(new JLabel(image));
            p.add(new JLabel(" x "));
            p.add(cbNbCreatures);
            tb.add(p,0,i+1);    
            
            tb.add(new JLabel(""+creature.getNbPiecesDOr()),1,i+1);    
            tb.add(new JLabel(""+creature.getNbPiecesDOr() * 
                    GestionnaireDeRevenu.POURCENTAGE_NB_PIECES_OR_CREATURE),2,i+1); 
               
            JButton bLancer = new JButton("Lancer");
            bLancers[i] = bLancer;
            tb.add(bLancer,3,i+1);
            bLancer.setBackground(LookInterface.COULEUR_BOUTON);
            bLancer.setForeground(GestionnaireDesPolices.COULEUR_TXT_BOUTON);
            
            bLancer.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    VagueDeCreatures vague = new VagueDeCreatures(Integer.parseInt((String) cbNbCreatures.getSelectedItem()), creature, 500, true);
                    
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
           
           int nbCreatures = Integer.parseInt((String) cbNbCreatures[i].getSelectedItem());
           
           bouton.setEnabled(nbPiecesDOr >= creatures[i].getNbPiecesDOr() * nbCreatures);
       }
    }
}
