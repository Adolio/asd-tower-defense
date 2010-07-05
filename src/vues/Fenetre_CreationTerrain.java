package vues;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import exceptions.*;
import models.creatures.Creature;
import models.jeu.Jeu;
import models.jeu.Jeu_Solo;
import models.joueurs.Joueur;
import models.outils.GestionnaireSons;
import models.terrains.Terrain;
import models.tours.Tour;

/**
 * Fenêtre de création et d'édition de terrain de jeu.
 * 
 * TODO compléter
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | juillet 2010
 * @since jdk1.6.0_16
 * @see Terrain
 */
public class Fenetre_CreationTerrain extends JFrame implements EcouteurDePanelTerrain, ActionListener
{
    private static final long serialVersionUID = 1L;
    private static final ImageIcon I_FENETRE = new ImageIcon("img/icones/icone_pgm.png");
    private static final ImageIcon I_MAIN = new ImageIcon("img/icones/hand.png");
    private static final ImageIcon I_MURS = new ImageIcon("img/icones/shape_square_edit.png");
    private static final ImageIcon I_TESTER = new ImageIcon("img/icones/cog.png");
    private static final ImageIcon I_ENREGISTRER = new ImageIcon("img/icones/disk.png");
    private static final ImageIcon I_OUVRIR = new ImageIcon("img/icones/folder_explore.png");
    
    private JButton bMain = new JButton(I_MAIN);
    private JButton bMurs = new JButton(I_MURS);
    private JButton bOuvrir = new JButton(I_OUVRIR);
    private JButton bEnregistrer = new JButton(I_ENREGISTRER);
    private JButton bTester = new JButton(I_TESTER);
    
    private final Insets INSETS = new Insets(5, 5, 5, 5);
    private final Color C_BTN_SEL = Color.BLUE;
    
    
    private Panel_CreationTerrain panelCreationTerrain;
    private Panel_OptionsTerrain panelOptionsTerrain;
    private Panel_CreationEquipes panelCreationEquipes;
    
    private final JMenuBar  menuPrincipal   = new JMenuBar();
    private final JMenu     menuFichier     = new JMenu("Fichier");
    private final JMenu     menuEdition     = new JMenu("Edition");
    private final JMenu     menuAide        = new JMenu("Aide");
    
    private final JMenuItem itemOuvrir      = new JMenuItem("Ouvrir...",I_OUVRIR);
    private final JMenuItem itemEnregistrer = new JMenuItem("Enregistrer",I_ENREGISTRER);
    private final JMenuItem itemTester      = new JMenuItem("Tester",I_TESTER);
    
    private JFileChooser fcOuvrir = new JFileChooser("./maps");
    private JFileChooser fcSauver = new JFileChooser("./maps");
    
    private Jeu jeu;
    private File fichierCourant;
    //private boolean sauve = false;
    
    private JLabel lblEtat = new JLabel("Prêt.");
    
    private static FileFilter filtreFichier = new FileFilter()
    {
        public String getDescription()
        {
            return "Terrain ."+Terrain.EXTENSION_FICHIER;
        }
        
        public boolean accept(File file)
        {
            if(file.isDirectory()) 
                 return true; 

            return file.getName().toLowerCase().endsWith("."+Terrain.EXTENSION_FICHIER);
        }
    };
    
    
    public Fenetre_CreationTerrain()
    {
        super("Editeur de terrain");
        setIconImage(I_FENETRE.getImage());
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        jeu = new Jeu_Solo();
        jeu.setTerrain(new Terrain(jeu));
        
        // selectionneur de fichiers
        fcSauver.addChoosableFileFilter(filtreFichier);   
        fcSauver.setDialogType(JFileChooser.SAVE_DIALOG);
        fcSauver.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fcOuvrir.addChoosableFileFilter(filtreFichier);   
        fcOuvrir.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        // menu
        itemOuvrir.addActionListener(this);
        itemEnregistrer.addActionListener(this);
        itemTester.addActionListener(this);
 
        menuFichier.add(itemOuvrir);
        menuFichier.add(itemEnregistrer);
        menuEdition.add(itemTester);
        menuPrincipal.add(menuFichier);
        menuPrincipal.add(menuEdition);
        menuPrincipal.add(menuAide);
        
        setJMenuBar(menuPrincipal);
        
        // barre d'outils
        JToolBar tbPrincipale = new JToolBar();
        
        tbPrincipale.add(bOuvrir);
        tbPrincipale.add(bEnregistrer);
        tbPrincipale.addSeparator();
        tbPrincipale.add(bMain);
        tbPrincipale.add(bMurs);
        tbPrincipale.addSeparator();
        tbPrincipale.add(bTester);
        
        bOuvrir.addActionListener(this);
        bEnregistrer.addActionListener(this);
        bMain.addActionListener(this);
        bMurs.addActionListener(this);
        bTester.addActionListener(this);
 
        add(tbPrincipale,BorderLayout.NORTH);
        
        // Onglets de droits
        JTabbedPane panelOnglets = new JTabbedPane();
        
        // Background
        UIManager.put("TabbedPane.tabAreaBackground", LookInterface.COULEUR_DE_FOND);
        //SwingUtilities.updateComponentTreeUI(panelSelectionEtVague);
     
        panelOnglets.setOpaque(true);
        //panelOnglets.setPreferredSize(new Dimension(300,420));
        panelOnglets.setBackground(LookInterface.COULEUR_DE_FOND);
        
        panelCreationTerrain = new Panel_CreationTerrain(jeu,this);
        panelOptionsTerrain = new Panel_OptionsTerrain(jeu);
        panelCreationEquipes = new Panel_CreationEquipes(jeu, panelCreationTerrain);
        
        panelOnglets.add("Propriétés",panelOptionsTerrain );
        panelOnglets.add("Equipes",panelCreationEquipes);
        
        JPanel p = new JPanel(new BorderLayout());
        p.add(panelOnglets,BorderLayout.CENTER);
        add(p,BorderLayout.EAST);
        
        
        panelCreationTerrain.basculeraffichageZonesDepartArrivee();
        add(panelCreationTerrain,BorderLayout.CENTER);
        
        add(lblEtat,BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // essaye de mettre le nouveau look and feel "Nimbus" fourni par Java
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels())
            if ("Nimbus".equals(laf.getName())) 
                try 
                {
                    UIManager.setLookAndFeel(laf.getClassName());
                } 
                catch (Exception e) 
                {
                    /* 
                     * On fait rien, c'est pas grave. 
                     * C'est juste le look and feel qui n'est pas installe.
                     */ 
                } 
        
        
        new Fenetre_CreationTerrain();
    }

    @Override
    public void acheterTour(Tour tour){}

    @Override
    public void ajouterInfoVagueSuivanteDansConsole(){}

    @Override
    public void ameliorerTour(Tour tour){}

    @Override
    public void creatureSelectionnee(Creature creature){}

    @Override
    public void deselection(){}

    @Override
    public void lancerVagueSuivante(){}

    @Override
    public void miseAJourInfoJeu(){}

    @Override
    public void setTourAAcheter(Tour tour){}

    @Override
    public void tourSelectionnee(Tour tour, int mode){}

    @Override
    public void vendreTour(Tour tour){}

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        
        
        if(src == bOuvrir || src == itemOuvrir)
        {
            ouvrirTerrain();
        }
        else if(src == bEnregistrer || src == itemEnregistrer)
        {
            enregistrerTerrain();
        }
        else if(src == bMain)
        {
            bMain.setBorder(new LineBorder(C_BTN_SEL,2));
            bMurs.setBorder(new EmptyBorder(INSETS));
            
            panelCreationTerrain.activerModeDeplacement();
        }
        else if(src == bMurs)
        {
            bMurs.setBorder(new LineBorder(C_BTN_SEL,2));
            bMain.setBorder(new EmptyBorder(INSETS));
            
            panelCreationTerrain.activerModeCreationMurs();
        }
        else if(src == bTester || src == itemTester)
        {
            tester();
        }
    }

    /**
     * Permet de tester le terrain courant
     */
    private void tester()
    {
        try
        {
            // sauvegarde et chargement du terrain
            enregistrerTerrain();
            
            // si la sauvegarde a joué (si première sauvegarde)
            if(fichierCourant != null)
            {
                Terrain t = Terrain.charger(fichierCourant);
                
                Jeu j = new Jeu_Solo();
                j.setTerrain(t);
                t.setJeu(j);
                
                Joueur joueur = new Joueur("Joueur Test");
                j.setJoueurPrincipal(joueur);
                j.ajouterJoueur(joueur);
     
                t.initialiser();
                j.initialiser();
                
                new Fenetre_JeuSolo(j)
                {
                    private static final long serialVersionUID = 35425L;

                    /**
                     * Permet de retourner au menu principal
                     */
                    protected void retourAuMenuPrincipal()
                    {
                        GestionnaireSons.arreterTousLesSons();
                         
                        dispose(); // destruction de la fenetre
                        System.gc(); // passage du remasse miette
                    }
                    
                    protected void quitter()
                    {
                        // FIXME fait planter l'éditeur après fermeture
                        //jeu.terminer();
                        //jeu.detruire();
                        dispose();
                    } 
                };
            }
        }
        catch (ClassCastException e1)
        {
            lblEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
            lblEtat.setText("Fichier invalide");
        } 
        catch (IOException e1)
        {
            lblEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
            lblEtat.setText("Fichier invalide");
        } 
        catch (ClassNotFoundException e1)
        {
            lblEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
            lblEtat.setText("Fichier invalide");
        }
        catch (JeuEnCoursException e1)
        {
            lblEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
            lblEtat.setText("Le jeu est en cours!");
        } 
        catch (AucunePlaceDisponibleException e1)
        {
            lblEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
            lblEtat.setText("Il n'y a aucun emplacement de joueur!");
        }
    }
    
    /**
     * Permet de sauvegarder le Terrain en traitement
     */
    private void enregistrerTerrain()
    {
        if(fichierCourant == null)
        {
            // Save as...
            if (fcSauver.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) 
            {
                fichierCourant = fcSauver.getSelectedFile();
                
                // rename if extension not wrote
                if(!fichierCourant.getName().toLowerCase().endsWith("."+Terrain.EXTENSION_FICHIER))
                    fichierCourant = new File(fichierCourant.getAbsolutePath()+"."+Terrain.EXTENSION_FICHIER);
            }
        }
        
        if(fichierCourant != null)
        {
            try
            {
                jeu.getTerrain().setLargeurMaillage(jeu.getTerrain().getLargeur());
                jeu.getTerrain().setHauteurMaillage(jeu.getTerrain().getHauteur());
                
                Terrain.serialiser(jeu.getTerrain(),fichierCourant/*new File("maps/"+jeu.getTerrain().getNom()+"."+Terrain.EXTENSION_FICHIER)*/);
            } 
            catch (IOException e)
            {
                lblEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
                lblEtat.setText("Erreur lors de la sauvegarde!");
            } 
        }
    }

    /**
     * Permet d'ouvir un Terrain sérialisé
     */
    private void ouvrirTerrain()
    {
        int returnVal = fcOuvrir.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fichier = fcOuvrir.getSelectedFile();
            
            try
            {
                Terrain t = Terrain.charger(fichier);
                
                jeu.setTerrain(t);
                t.setJeu(jeu);
                
                fichierCourant = fichier;
                
                panelOptionsTerrain.miseAJour();
                panelCreationEquipes.miseAJour();
                
                lblEtat.setForeground(GestionnaireDesPolices.COULEUR_SUCCES);
                lblEtat.setText("Fichier chargé");
                
            } 
            catch (ClassCastException e1)
            {
                lblEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
                lblEtat.setText("Fichier invalide");
            } 
            catch (IOException e1)
            {
                lblEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
                lblEtat.setText("Fichier invalide");
            } 
            catch (ClassNotFoundException e1)
            {
                lblEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
                lblEtat.setText("Fichier invalide");
            }
        }
    }
}
