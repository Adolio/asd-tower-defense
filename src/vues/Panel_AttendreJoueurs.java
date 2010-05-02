package vues;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import models.jeu.Jeu;
import models.joueurs.EmplacementJoueur;
import models.joueurs.Equipe;
import models.joueurs.Joueur;

import reseau.Canal;
import serveur.enregistrement.RequeteEnregistrement;

@SuppressWarnings("serial")
public class Panel_AttendreJoueurs extends JPanel implements ActionListener
{
    private final int MARGES_PANEL = 40;
    private final boolean ADMIN;
    private JFrame parent;
    private JButton bDemarrerMaintenant = new JButton("Démarrer maintenant");
    private JLabel lblEtat = new JLabel();
    private JButton bDeconnecter = new JButton("Se Deconnecter");
    
    private Jeu jeu;  
    private Canal canalServeurEnregistrement;
    private Canal canalServeurJeu;
    
    @SuppressWarnings("serial")
    public Panel_AttendreJoueurs(JFrame parent, Canal canal, boolean admin)
    {
        this.parent = parent;
        this.ADMIN  = admin;
        
        if(admin)
            this.canalServeurEnregistrement = canal;
        else
            this.canalServeurJeu = canal;
        
        initialiserForm();
    }
    
    @SuppressWarnings("serial")
    public Panel_AttendreJoueurs(JFrame parent, Jeu jeu)
    {
        this.parent = parent;
        this.ADMIN  = true;
        this.jeu    = jeu; 
         
        initialiserForm();
    }
    
    @SuppressWarnings("serial")
    public Panel_AttendreJoueurs(JFrame parent)
    {
        this.parent = parent;
        this.ADMIN  = false;
        
        initialiserForm();
    }

    private void initialiserForm()
    {
        // initialisation
        setLayout(new BorderLayout());
        
        parent.setTitle("Attendre des joueurs");
        setBorder(new EmptyBorder(new Insets(MARGES_PANEL, MARGES_PANEL,
                MARGES_PANEL, MARGES_PANEL)));
        setBackground(LookInterface.COULEUR_DE_FOND);
        
        
        //---------
        //-- TOP --
        //---------
        JPanel pTop = new JPanel(new BorderLayout());
        pTop.setOpaque(false);
        
        JLabel lblTitre = new JLabel("ATTENTE DE JOUEURS...");
        lblTitre.setFont(GestionnaireDesPolices.POLICE_TITRE);
        pTop.add(lblTitre, BorderLayout.NORTH);
        
        add(pTop, BorderLayout.NORTH);

        
        //------------
        //-- CENTER --
        //------------
        
        if(jeu != null)
        {
            
            ArrayList<Joueur> joueurs = jeu.getJoueurs();
            ArrayList<Equipe> equipes  = jeu.getEquipes();
            
            
            int maxJoueurs = jeu.getTerrain().getNbJoueursMax();
            
            JPanel pCenter = new JPanel(new GridLayout(maxJoueurs, 1));
            pCenter.setOpaque(false);
            
            for(int i=0;i<maxJoueurs;i++)
            {
                JPanel pJoueur = new JPanel();
                pJoueur.setOpaque(false);
                
                pJoueur.add(new JLabel((i+1)+". "));
                
                // joueur trouvé
                if(i < joueurs.size()) 
                {
                     Joueur joueur = joueurs.get(i);
                     pJoueur.add(new JLabel(joueur.getPseudo()));
                     
                     // Equipe
                     JComboBox cbEquipes = new JComboBox();
                     
                     
                     for(int j=0;j<equipes.size();j++)
                     {
                         Equipe e2 = equipes.get(j);
                         
                         // ajout de l'equipe
                         cbEquipes.addItem(e2.getNom());
                         
                         if(joueur.getEquipe() == e2)
                             cbEquipes.setSelectedIndex(j); 
                     }
                     
                     pJoueur.add(cbEquipes);
                     
                     // Emplacement
                     JComboBox cbEmplacements = new JComboBox();
                     Equipe equipe = joueur.getEquipe();
                     
                     for(int j=0;j<equipe.getEmplacementsJoueur().size();j++)
                     {
                         cbEmplacements.addItem("Zone "+(j+1));
                         
                         if(joueur.getEmplacement() == equipe.getEmplacementsJoueur().get(j))
                             cbEmplacements.setSelectedIndex(j);
                     }
                      
                     pJoueur.add(cbEmplacements);
                }
                else  // personne
                {
                    pJoueur.add(new JLabel("???"));
                }
                pCenter.add(pJoueur);
            }
            
            /*
            JPanel pCenter = new JPanel(new GridLayout(10, 1));
            
            ArrayList<Equipe> equipes = jeu.getEquipes();
            
            
            for(Equipe e : equipes)
            {
                ArrayList<Joueur> joueurs = e.getJoueurs();
                
                
                for(Joueur j : joueurs)
                {
                    JPanel pJoueur = new JPanel();
                    
                    pJoueur.add(new JLabel(j.getPseudo()));
                    
                    // Equipe
                    JComboBox cbEquipes = new JComboBox();
                    
                    
                    for(int i=0;i<equipes.size();i++)
                    {
                        Equipe e2 = equipes.get(i);
                        
                        // ajout de l'equipe
                        cbEquipes.addItem(e2.getNom());
                        
                        if(j.getEquipe() == e2)
                            cbEquipes.setSelectedIndex(i); 
                    }
                    
                    pJoueur.add(cbEquipes);
                    
                    // Emplacement
                    JComboBox cbEmplacements = new JComboBox();
                    
                    for(int i=0;i<e.getEmplacementsJoueur().size();i++)
                    {
                        cbEmplacements.addItem("Zone "+(i+1));
                        
                        if(j.getEmplacement() == e.getEmplacementsJoueur().get(i))
                            cbEmplacements.setSelectedIndex(i);
                    }
                     
                    pJoueur.add(cbEmplacements);
                    
                    
                    pCenter.add(pJoueur);
                }
            }
            */
            /*for(Equipe e : jeu.getEquipes())
            {
                ArrayList<EmplacementJoueur> joueurs = e.getEmplacementsJoueur();
                
                JLabel lblEquipe = new JLabel(e.getNom());
                lblEquipe.setForeground(e.getCouleur());
                pCenter.add(lblEquipe);
                
                for(EmplacementJoueur ej : joueurs)
                {
                    pCenter.add(new JLabel("e"));
                }
            }*/
            
            add(pCenter,BorderLayout.CENTER);
        }
        
        
        
        //------------
        //-- BOTTOM --
        //------------
        JPanel pBottom = new JPanel(new BorderLayout());
        pBottom.setOpaque(false);
        
        // bouton démarrer
        if(ADMIN)
        {
            bDemarrerMaintenant.setPreferredSize(new Dimension(100,50));
            pBottom.add(bDemarrerMaintenant,BorderLayout.EAST);
            bDemarrerMaintenant.addActionListener(this);
        }
       
        bDeconnecter.addActionListener(this);
        pBottom.add(bDeconnecter,BorderLayout.WEST);
        
         
        if(ADMIN)
            if(canalServeurEnregistrement == null)
            {
                lblEtat.setForeground(GestionnaireDesPolices.COULEUR_INFO);
                lblEtat.setText("La connexion avec le serveur central à échouée, " +
                		"votre serveur n'apparaitra pas dans la liste des serveurs");
            }
            else
            {
                lblEtat.setForeground(GestionnaireDesPolices.COULEUR_SUCCES);
                lblEtat.setText("La connexion avec le serveur central à réussie");
            }
        
        pBottom.add(lblEtat,BorderLayout.SOUTH);
        
        
        add(pBottom, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        
        if(src == bDemarrerMaintenant)
        {
            if(ADMIN)
            {
                if(canalServeurEnregistrement != null)
                {
                    canalServeurEnregistrement.envoyerString(RequeteEnregistrement.DESENREGISTRER);
                    canalServeurEnregistrement.envoyerString(RequeteEnregistrement.STOP); 
                }
            }

            //parent.getContentPane().removeAll();
            //parent.getContentPane().add(new Panel_JeuMulti(parent, new Jeu(),new Joueur(new Equipe()), BorderLayout.CENTER); 
            //parent.getContentPane().validate();
        }
        else if(src == bDeconnecter)
        {
            if(ADMIN)
            {
                if(canalServeurEnregistrement != null)
                {
                    canalServeurEnregistrement.envoyerString(RequeteEnregistrement.DESENREGISTRER);
                    canalServeurEnregistrement.envoyerString(RequeteEnregistrement.STOP); 
                }
            }
            else if(canalServeurJeu != null)
            {   
                canalServeurJeu.envoyerString(RequeteEnregistrement.STOP);         
            }
            
            parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_MenuPrincipal(parent), BorderLayout.CENTER); 
            parent.getContentPane().validate();
        }
    }
}
