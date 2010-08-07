package vues.editeurTerrain;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import vues.GestionnaireDesPolices;
import vues.commun.Panel_Table;
import models.jeu.Jeu;
import models.joueurs.EmplacementJoueur;
import models.joueurs.Equipe;
import models.terrains.Terrain;

/**
 * Fenêtre de création d'équipes.
 * 
 * TODO commenter tout le fichier
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | juillet 2010
 * @since jdk1.6.0_16
 * @see Terrain
 */
public class Panel_CreationEquipes extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private static final ImageIcon I_AJOUTER_EQUIPE = new ImageIcon("img/icones/flag_add.gif");
    private static final ImageIcon I_SUPPRIMER = new ImageIcon("img/icones/delete.png");
    private static final ImageIcon I_COULEURS = new ImageIcon("img/icones/color_swatch.png");
    private static final ImageIcon I_ZONE_EDITION = new ImageIcon("img/icones/shape_square_edit.png");

    private JButton bCreerEquipe = new JButton("Créer une équipe",I_AJOUTER_EQUIPE);
    private Jeu jeu;
    private int idEquipe = 1;
    private Panel_Table pTabEquipes = new Panel_Table();   
    
    private int idEJ;
    private Panel_CreationTerrain panelCreationTerrain;
    
    public Panel_CreationEquipes(Jeu jeu,Panel_CreationTerrain panelCreationTerrain)
    {
        super(new BorderLayout());
        
        this.jeu = jeu;
        this.panelCreationTerrain = panelCreationTerrain;
        
        setOpaque(false);
        
        //add(new JLabel("Equipes!"));
        GestionnaireDesPolices.setStyle(bCreerEquipe);
        bCreerEquipe.addActionListener(this);
        
        JPanel pTmp = new JPanel();
        pTmp.setOpaque(false);
        pTmp.add(bCreerEquipe);
        add(pTmp,BorderLayout.NORTH);
        
        JPanel pEquipe = new JPanel(new BorderLayout());
        pEquipe.setOpaque(false);
        pTabEquipes.setOpaque(false);
        pEquipe.add(pTabEquipes,BorderLayout.NORTH);
        add(pEquipe,BorderLayout.CENTER);
       
        construirePanelEquipes();
        
        setPreferredSize(new Dimension(400,200));
    }

    private void construirePanelEquipes()
    {
        pTabEquipes.removeAll();

        int ligne=0;
        
        idEJ = 1;
        
        for(final Equipe equipe : jeu.getEquipes())
        {
            // nom de l'équipe
            final JTextField lNomEquipe = new JTextField(equipe.getNom());
            lNomEquipe.setForeground(equipe.getCouleur());
            lNomEquipe.setMinimumSize(new Dimension(80,25));
            lNomEquipe.setPreferredSize(new Dimension(80,25));
            
            
            pTabEquipes.add(lNomEquipe,0,ligne);
            
            
            lNomEquipe.getDocument().addDocumentListener(new DocumentListener()
            {
                @Override
                public void removeUpdate(DocumentEvent e)
                {
                    equipe.setNom(lNomEquipe.getText());
                }
                
                @Override
                public void insertUpdate(DocumentEvent e)
                {
                    equipe.setNom(lNomEquipe.getText());
                }
                
                @Override
                public void changedUpdate(DocumentEvent e)
                {
                    equipe.setNom(lNomEquipe.getText());
                }
            });
            
            // Couleur
            final JButton bCouleur = new JButton(I_COULEURS);
            GestionnaireDesPolices.setStyle(bCouleur);
            pTabEquipes.add(bCouleur,1,ligne);
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
            final JButton bZoneDepart = new JButton("ZD",I_ZONE_EDITION);
            GestionnaireDesPolices.setStyle(bZoneDepart);
            pTabEquipes.add(bZoneDepart,2,ligne);
            
            bZoneDepart.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    panelCreationTerrain.setRecEnTraitement(equipe.getZoneDepartCreatures(0));
                }
            });
            
            
            //ligne++;
            
            // Zone d'arrivé
            
            final JButton bZoneArrive = new JButton("ZA",I_ZONE_EDITION);
            GestionnaireDesPolices.setStyle(bZoneArrive);
            pTabEquipes.add(bZoneArrive,3,ligne);
            
            bZoneArrive.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    panelCreationTerrain.setRecEnTraitement(equipe.getZoneArriveeCreatures());
                }
            });

            // Zone d'arrivée
            final JButton bNouvelEmplacement = new JButton("E++");
            GestionnaireDesPolices.setStyle(bNouvelEmplacement);
            pTabEquipes.add(bNouvelEmplacement,4,ligne);
            
            bNouvelEmplacement.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    equipe.ajouterEmplacementJoueur(new EmplacementJoueur(idEJ++, new Rectangle(0,0,jeu.getTerrain().getLargeur(),jeu.getTerrain().getHauteur())));
                
                    construirePanelEquipes();
                }
            });
             
            // Suppression
            final JButton bSupprimerEquipe = new JButton(I_SUPPRIMER);
            GestionnaireDesPolices.setStyle(bSupprimerEquipe);
            pTabEquipes.add(bSupprimerEquipe,5,ligne);
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
                final JLabel lNomEmplacement = new JLabel("Emplacement "+ej.getId());
                lNomEmplacement.setForeground(ej.getCouleur());
                
                pTabEquipes.add(lNomEmplacement,1,ligne);
                
                if(ej.getId() >= idEJ)
                    idEJ = ej.getId() + 1;
                
                // Selection
                final JButton bSelectionnerEmplacement = new JButton(I_ZONE_EDITION);
                GestionnaireDesPolices.setStyle(bSelectionnerEmplacement);
                pTabEquipes.add(bSelectionnerEmplacement,3,ligne);
                bSelectionnerEmplacement.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        panelCreationTerrain.setRecEnTraitement(ej.getZoneDeConstruction());
                    } 
                });
                 
                // Couleur
                final JButton bCouleurEmplacement = new JButton(I_COULEURS);
                GestionnaireDesPolices.setStyle(bCouleurEmplacement);
                pTabEquipes.add(bCouleurEmplacement,2,ligne);
                bCouleurEmplacement.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        Color couleur = JColorChooser.showDialog(
                                null,
                                "Couleur de l'emplacement "+ej.getId(),ej.getCouleur());
                          
                        if(couleur != null)
                        {
                            ej.setCouleur(couleur);
                            lNomEmplacement.setForeground(couleur);
                        } 
                    } 
                });
                
                // Suppression
                final JButton bSupprimerEmplacement = new JButton(I_SUPPRIMER);
                GestionnaireDesPolices.setStyle(bSupprimerEmplacement);
                pTabEquipes.add(bSupprimerEmplacement,5,ligne);
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

        pTabEquipes.repaint();
        pTabEquipes.revalidate();
        pTabEquipes.validate();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        
        if(src == bCreerEquipe)
        {
            Equipe equipe = new Equipe(idEquipe, "Equipe "+idEquipe, Color.BLACK);
            
            jeu.ajouterEquipe(equipe);
            
            equipe.ajouterZoneDepart(new Rectangle(40,40,40,40));
            equipe.setZoneArriveeCreatures(new Rectangle(140,140,40,40));
            
            idEquipe++;
            
            construirePanelEquipes(); 
        }
    }

    public void miseAJour()
    {
        construirePanelEquipes();
    }
}
