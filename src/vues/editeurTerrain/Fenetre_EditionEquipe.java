package vues.editeurTerrain;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import vues.GestionnaireDesPolices;
import vues.LookInterface;
import vues.commun.Panel_Table;
import models.joueurs.Equipe;

public class Fenetre_EditionEquipe extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private final int MARGES_PANEL = 20;
    
    private final Equipe equipe;
    
    private final JLabel lTitre = new JLabel("Edition d'une equipe");
    
    private final JLabel lNomEquipe = new JLabel("Nom :");
    private final JLabel lCouleurEquipe = new JLabel("Couleur :");
    
    private final JTextField tfNomEquipe = new JTextField();
    private final JButton bCouleurEquipe = new JButton();
    
    private final JButton bOk = new JButton("OK");
    private final JButton bAnnuler = new JButton("Annuler");
    
    private Panel_CreationEquipes pce;
    
    
    public Fenetre_EditionEquipe(Panel_CreationEquipes pce, Equipe equipe)
    {
        super("Edition d'une équipe");
        
        this.pce = pce;
        this.equipe = equipe;
 
        getContentPane().setBackground(LookInterface.COULEUR_DE_FOND_SEC);

        Panel_Table p = new Panel_Table();
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(new Insets(MARGES_PANEL, MARGES_PANEL,
                MARGES_PANEL, MARGES_PANEL)));
        
        int ln=0;
        
        // Titre
        lTitre.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        lTitre.setForeground(LookInterface.COULEUR_TEXTE_PRI);
        lTitre.setBorder(new EmptyBorder(new Insets(0, 0, 10, 0)));
        p.add(lTitre,0,ln++,3,1);
        
        // Nom
        p.addFieldTitle(lNomEquipe,ln);
        tfNomEquipe.setText(equipe.getNom());
        tfNomEquipe.setPreferredSize(new Dimension(90,30));
        p.addField(tfNomEquipe,ln++);
        
        // Couleur
        p.addFieldTitle(lCouleurEquipe,ln);
        bCouleurEquipe.setBackground(equipe.getCouleur());
        bCouleurEquipe.addActionListener(this);
        bCouleurEquipe.setPreferredSize(new Dimension(90,30));
        p.addField(bCouleurEquipe,ln++);
        
        
        // Boutons
        JPanel pButton = new JPanel(new FlowLayout());
        pButton.setOpaque(false);
        
        bOk.addActionListener(this);
        GestionnaireDesPolices.setStyle(bOk);
        pButton.add(bOk);
        
        bAnnuler.addActionListener(this);
        GestionnaireDesPolices.setStyle(bAnnuler);
        pButton.add(bAnnuler);
        
        p.add(pButton,1,ln++);
        
        add(p,BorderLayout.CENTER);
        
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object obj = e.getSource();
        
        if(obj == bCouleurEquipe)
        {
            Color couleur = JColorChooser.showDialog(
                    null,"",equipe.getCouleur());
              
            if(couleur != null)
            {
                equipe.setCouleur(couleur);
                bCouleurEquipe.setBackground(couleur);
            }
        }
        else if(obj == bOk)
        {
            if(!tfNomEquipe.getText().trim().isEmpty())
            {
                equipe.setNom(tfNomEquipe.getText());
                
                pce.miseAJour();
                
                dispose();
            }
            else
            {
                // TODO Traduire
                JOptionPane.showConfirmDialog(this, "Nom vide !", "Erreur",JOptionPane.OK_OPTION,JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(obj == bAnnuler)
        {
            dispose();
        }
    }
}
