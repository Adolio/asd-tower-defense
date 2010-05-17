package vues;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import models.creatures.*;
import models.jeu.Jeu;
import models.joueurs.Joueur;

public class Panel_CreationVague extends JPanel
{
    private static final long serialVersionUID = 1L;
    private Panel_Table tb = new Panel_Table();
    
    private Creature[] creatures = new Creature[]
    {
        new Mouton(100, 10, 40.0),
        new Araignee(200, 20, 40.0),
        new Pigeon(300, 30, 40.0),
        new Rhinoceros(400, 40, 40.0),
        new Elephant(500, 50, 40.0)
    };
    
    public Panel_CreationVague(final Jeu jeu, final Joueur cible, final EcouteurDeLanceurDeVagues edlv)
                               
    {
        //setOpaque(false);
        //tb.setOpaque(false);
        setBackground(LookInterface.COULEUR_DE_FOND_2);
        tb.setOpaque(false);
        
        tb.add(new JLabel("Créature"),0,0); 
        tb.add(new JLabel("Prix"),2,0); 
        tb.add(new JLabel("Revenu"),3,0); 
        
        
        for(int i=0;i < creatures.length;i++)
        {
            final Creature creature = creatures[i];
            
            ImageIcon image = new ImageIcon(creature.getImage());

            final JComboBox cbNbCreatures = new JComboBox();
            cbNbCreatures.addItem("1");
            cbNbCreatures.addItem("2");
            cbNbCreatures.addItem("3");
            cbNbCreatures.addItem("4");
            cbNbCreatures.addItem("5");
            cbNbCreatures.addItem("10");
            cbNbCreatures.addItem("15");
            cbNbCreatures.addItem("20");
            cbNbCreatures.addItem("30");
            
            JPanel p = new JPanel(new FlowLayout());
            p.setOpaque(false);
            p.add(new JLabel(image));
            p.add(new JLabel(" x "));
            p.add(cbNbCreatures);
            tb.add(p,0,i+1);
            
            JButton bLancer = new JButton("Lancer");
            tb.add(bLancer,3,i+1);
            bLancer.setBackground(LookInterface.COULEUR_BOUTON);
            bLancer.setForeground(GestionnaireDesPolices.COULEUR_TXT_BOUTON);
            
            bLancer.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    VagueDeCreatures vague = new VagueDeCreatures(Integer.parseInt((String) cbNbCreatures.getSelectedItem()), creature, 500, true);
                    
                    edlv.lancerVague(vague); 
                }
            });
            
        }
        
        add(tb);
    }
}
