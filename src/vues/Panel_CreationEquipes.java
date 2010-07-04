package vues;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import models.jeu.Jeu;
import models.joueurs.EmplacementJoueur;
import models.joueurs.Equipe;

public class Panel_CreationEquipes extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private static final ImageIcon I_AJOUTER_EQUIPE = new ImageIcon("img/icones/flag_add.gif");
    private static final ImageIcon I_SUPPRIMER = new ImageIcon("img/icones/delete.png");
    private static final ImageIcon I_COULEURS = new ImageIcon("img/icones/color_swatch.png");
    
    private JButton bCreerEquipe = new JButton("Créer une équipe",I_AJOUTER_EQUIPE);
    private Jeu jeu;
    private int idCourant = 1;
    private Panel_Table pEquipes = new Panel_Table();   
    
    public Panel_CreationEquipes(Jeu jeu)
    {
        super(new BorderLayout());
        
        this.jeu = jeu;
        
        //add(new JLabel("Equipes!"));
        bCreerEquipe.addActionListener(this);
        add(bCreerEquipe,BorderLayout.NORTH);
        
        add(pEquipes,BorderLayout.CENTER);
        construirePanelEquipes();
        
        setPreferredSize(new Dimension(400,200));
    }

    private void construirePanelEquipes()
    {
        pEquipes.removeAll();
        
        int ligne=0;
        
        for(final Equipe equipe : jeu.getEquipes())
        {
            
            // nom de l'équipe
            final JTextField lNomEquipe = new JTextField(equipe.getNom());
            lNomEquipe.setForeground(equipe.getCouleur());
            lNomEquipe.setPreferredSize(new Dimension(120,30));
            pEquipes.add(lNomEquipe,0,ligne);
            
            // Couleur
            final JButton bCouleur = new JButton(I_COULEURS);
            pEquipes.add(bCouleur,1,ligne);
            bCouleur.addActionListener(new ActionListener()
            {
                
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    
                    Color couleur = JColorChooser.showDialog(
                            null,
                            "Couleur de l'équipe "+equipe.getNom(),equipe.getCouleur());
                      
                    if(couleur != null)
                    {
                        equipe.setCouleur(couleur);
                        lNomEquipe.setForeground(couleur);
                    }
                } 
            });
            
            
            //ligne++;
            
            // Zone de départ
            final JButton bZoneDepart = new JButton("ZD");
            pEquipes.add(bZoneDepart,2,ligne);
            
            bZoneDepart.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    // TODO panelCreationTerrain.setRecEnTraitement(equipe.getZoneDepart(0));
                    
                }
            });
            
            
            //ligne++;
            
            // Zone d'arrivé
            
            final JButton bZoneArrive = new JButton("ZA");
            pEquipes.add(bZoneArrive,3,ligne);
            
            bZoneArrive.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    // TODO panelCreationTerrain.setRecEnTraitement(equipe.getZoneArrivee());
                }
            });
            
            
            // Zone d'arrivé
            
            final JButton bNouvelEmplacement = new JButton("+E");
            pEquipes.add(bNouvelEmplacement,4,ligne);
            
            bNouvelEmplacement.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    // FIXME id
                    equipe.ajouterEmplacementJoueur(new EmplacementJoueur(1, new Rectangle(0,0,jeu.getTerrain().getLargeur(),jeu.getTerrain().getHauteur())));
                
                    construirePanelEquipes();
                }
            });
             
            
            
            
            
            // Suppression
            final JButton bSupprimerEquipe = new JButton(I_SUPPRIMER);
            pEquipes.add(bSupprimerEquipe,5,ligne);
            bSupprimerEquipe.addActionListener(new ActionListener()
            {
                
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    jeu.supprimerEquipe(equipe);
                    
                    construirePanelEquipes();
                } 
            });
            
            
            ligne++;
            
            
            
            for(final EmplacementJoueur ej : equipe.getEmplacementsJoueur())
            {
                pEquipes.add(new JLabel("Emplacement "+ej.getId()),1,ligne);
                
                
                // Suppression
                final JButton bSupprimerEmplacement = new JButton(I_SUPPRIMER);
                pEquipes.add(bSupprimerEmplacement,5,ligne);
                bSupprimerEmplacement.addActionListener(new ActionListener()
                {
                    
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        equipe.supprimerEmplacement(ej);
                        
                        construirePanelEquipes();
                    } 
                });
                
                
                ligne++;
            }   
            
            
            
            
        }
        
        
        
        
        
        
        
        
        
        
        
        pEquipes.repaint();
        pEquipes.revalidate();
        pEquipes.validate();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        
        if(src == bCreerEquipe)
        {
            Equipe equipe = new Equipe(idCourant, "Equipe "+idCourant, Color.BLACK);
            
            jeu.ajouterEquipe(equipe);
            
            equipe.setZoneDepart(new Rectangle(40,40,120,40));
            equipe.setZoneArriveeCreatures(new Rectangle(140,140,120,40));
            
            idCourant++;
            
            construirePanelEquipes(); 
        }
        
    }

    public void miseAJour()
    {
        construirePanelEquipes();
    }
}
