package vues.editeurTerrain;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import vues.EcouteurDePanelCreationTerrain;
import vues.EcouteurDePanelTerrain;
import vues.Fenetre_JeuSolo;
import vues.LookInterface;
import vues.Panel_OptionsTerrain;
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
public class Fenetre_CreationTerrain extends    JFrame 
                                     implements EcouteurDePanelTerrain, 
                                                EcouteurDePanelCreationTerrain, 
                                                ActionListener
{
    private static final long serialVersionUID = 1L;
    private static final ImageIcon I_FENETRE = new ImageIcon("img/icones/map_edit.png");
    private static final ImageIcon I_MAIN = new ImageIcon("img/icones/hand.png");
    private static final ImageIcon I_MURS = new ImageIcon("img/icones/shape_square_edit.png");
    private static final ImageIcon I_TESTER = new ImageIcon("img/icones/cog.png");
    private static final ImageIcon I_ENREGISTRER = new ImageIcon("img/icones/disk.png");
    private static final ImageIcon I_ENREGISTRER_SOUS = new ImageIcon("img/icones/disk_multiple.png");
    
    private static final ImageIcon I_NOUVEAU = new ImageIcon("img/icones/page_white_star.png");
    private static final ImageIcon I_OUVRIR = new ImageIcon("img/icones/folder_explore.png");
    private static final ImageIcon I_SUPPRIMER = new ImageIcon("img/icones/shape_square_delete.png");
    private static final ImageIcon I_QUITTER = new ImageIcon("img/icones/door_out.png");
    
    
    
    
    private JButton bMain           = new JButton(I_MAIN);
    private JButton bMurs           = new JButton(I_MURS);
    private JButton bNouveau        = new JButton(I_NOUVEAU);
    private JButton bOuvrir         = new JButton(I_OUVRIR);
    private JButton bEnregistrer    = new JButton(I_ENREGISTRER);
    private JButton bSupprimer      = new JButton(I_SUPPRIMER);
    private JButton bTester         = new JButton(I_TESTER);
    
    
    private final Insets INSETS     = new Insets(5, 5, 5, 5);
    private final Color C_BTN_SEL   = Color.BLUE;
     
    private Panel_CreationTerrain panelCreationTerrain;
    private Panel_OptionsTerrain panelOptionsTerrain;
    private Panel_CreationEquipes panelCreationEquipes;
    
    private final JMenuBar  menuPrincipal   = new JMenuBar();
    private final JMenu     menuFichier     = new JMenu("Fichier");
    private final JMenu     menuEdition     = new JMenu("Edition");
    private final JMenu     menuAide        = new JMenu("Aide");
    
    private final JMenuItem itemNouveau      = new JMenuItem("Nouveau",I_NOUVEAU);
    private final JMenuItem itemOuvrir      = new JMenuItem("Ouvrir...",I_OUVRIR);
    private final JMenuItem itemEnregistrer = new JMenuItem("Enregistrer",I_ENREGISTRER);
    private final JMenuItem itemEnregistrerSous = new JMenuItem("Enregistrer sous...",I_ENREGISTRER_SOUS);
    private final JMenuItem itemQuitter      = new JMenuItem("Quitter",I_QUITTER);
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
        super("ASD - Tower Defense - Editeur de terrain");
        setIconImage(I_FENETRE.getImage());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(LookInterface.COULEUR_DE_FOND_PRI);
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
        itemNouveau.addActionListener(this);
        itemOuvrir.addActionListener(this);
        itemEnregistrer.addActionListener(this);
        itemEnregistrerSous.addActionListener(this);
        itemQuitter.addActionListener(this);
        itemTester.addActionListener(this);
 
        // Raccourcis clavier
        itemNouveau.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, Event.CTRL_MASK));
        itemOuvrir.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, Event.CTRL_MASK));
        itemEnregistrer.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, Event.CTRL_MASK));
        itemQuitter.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_W, Event.CTRL_MASK));
        itemTester.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_E, Event.CTRL_MASK));
        
        menuFichier.add(itemNouveau);
        menuFichier.add(itemOuvrir);
        menuFichier.add(itemEnregistrer);
        menuFichier.add(itemEnregistrerSous);
        menuFichier.addSeparator();
        menuFichier.add(itemQuitter);
        menuEdition.add(itemTester);
        menuPrincipal.add(menuFichier);
        menuPrincipal.add(menuEdition);
        menuPrincipal.add(menuAide);
        
        setJMenuBar(menuPrincipal);
        
        // barre d'outils
        JToolBar tbPrincipale = new JToolBar();
        
        tbPrincipale.add(bNouveau);
        tbPrincipale.add(bOuvrir);
        tbPrincipale.add(bEnregistrer);
        tbPrincipale.addSeparator();
        tbPrincipale.add(bMain);
        tbPrincipale.add(bMurs);
        tbPrincipale.addSeparator();
        tbPrincipale.add(bSupprimer);
        tbPrincipale.addSeparator();
        tbPrincipale.add(bTester);
        
        bNouveau.addActionListener(this);
        bOuvrir.addActionListener(this);
        bEnregistrer.addActionListener(this);
        bMain.addActionListener(this);
        bMurs.addActionListener(this);
        bSupprimer.addActionListener(this);
        bTester.addActionListener(this);
 
        bNouveau.setToolTipText("Nouveau");
        bOuvrir.setToolTipText("Ouvrir...");
        bEnregistrer.setToolTipText("Enregistrer");
        bMain.setToolTipText("Déplacements");
        bMurs.setToolTipText("Edition de zone");
        bSupprimer.setToolTipText("Supprimer la zone sélectionnée");
        bTester.setToolTipText("Tester");
        
        add(tbPrincipale,BorderLayout.NORTH);
        
        // Onglets de droits
        JTabbedPane panelOnglets = new JTabbedPane();
        
        // Background
        UIManager.put("TabbedPane.tabAreaBackground", LookInterface.COULEUR_DE_FOND_PRI);
        //SwingUtilities.updateComponentTreeUI(panelSelectionEtVague);
     
        panelOnglets.setOpaque(true);
        //panelOnglets.setPreferredSize(new Dimension(300,420));
        panelOnglets.setBackground(LookInterface.COULEUR_DE_FOND_SEC);
        
        panelCreationTerrain = new Panel_CreationTerrain(jeu,this);
        panelOptionsTerrain = new Panel_OptionsTerrain(jeu);
        panelCreationEquipes = new Panel_CreationEquipes(jeu, panelCreationTerrain);
        
        panelOnglets.add("Propriétés",panelOptionsTerrain );
        panelOnglets.add("Equipes",panelCreationEquipes);
        
        JPanel p = new JPanel(new BorderLayout());
        p.add(panelOnglets,BorderLayout.CENTER);
        add(p,BorderLayout.EAST);
        
        panelCreationTerrain.setEcouteurDeCreationTerrain(this);
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
        
        if(src == bNouveau || src == itemNouveau)
        {
            nouveauTerrain();
        }
        else if(src == bOuvrir || src == itemOuvrir)
        {
            ouvrirTerrain();
        }
        else if(src == bEnregistrer || src == itemEnregistrer)
        {
            enregistrerTerrain();
        }
        else if(src == itemEnregistrerSous)
        {
            enregistrerTerrainSous();
        }  
        else if(src == itemQuitter)
        {
            System.exit(0);
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
        else if(src == bSupprimer)
        {
            jeu.getTerrain().supprimerMur(panelCreationTerrain.getRecEnTraitement());
            panelCreationTerrain.deselectionnerRecEnTraitement(); 
        }
        else if(src == bTester || src == itemTester)
        {
            tester();
        }
    }

    private void nouveauTerrain()
    {
        Terrain t = new Terrain(jeu);
        jeu.setTerrain(t);
        
        fichierCourant = null;
        
        panelCreationTerrain.deselectionnerRecEnTraitement();
        panelOptionsTerrain.miseAJour();
        panelCreationEquipes.miseAJour();
        
        lblEtat.setForeground(LookInterface.COULEUR_SUCCES);
        lblEtat.setText("Fichier chargé");
    }

    private void enregistrerTerrainSous()
    {
        // Save as...
        if (fcSauver.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) 
        {
            fichierCourant = fcSauver.getSelectedFile();
            
            // rename if extension not wrote
            if(!fichierCourant.getName().toLowerCase().endsWith("."+Terrain.EXTENSION_FICHIER))
                fichierCourant = new File(fichierCourant.getAbsolutePath()+"."+Terrain.EXTENSION_FICHIER);
        
            try
            {
                jeu.getTerrain().setLargeurMaillage(jeu.getTerrain().getLargeur());
                jeu.getTerrain().setHauteurMaillage(jeu.getTerrain().getHauteur());
                
                Terrain.serialiser(jeu.getTerrain(),fichierCourant/*new File("maps/"+jeu.getTerrain().getNom()+"."+Terrain.EXTENSION_FICHIER)*/);
            } 
            catch (IOException e)
            {
                lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
                lblEtat.setText("Erreur lors de la sauvegarde!");
            }
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
            lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
            lblEtat.setText("Fichier invalide");
        } 
        catch (IOException e1)
        {
            lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
            lblEtat.setText("Fichier invalide");
        } 
        catch (ClassNotFoundException e1)
        {
            lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
            lblEtat.setText("Fichier invalide");
        }
        catch (JeuEnCoursException e1)
        {
            lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
            lblEtat.setText("Le jeu est en cours!");
        } 
        catch (AucunePlaceDisponibleException e1)
        {
            lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
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
                lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
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
                
                panelCreationTerrain.deselectionnerRecEnTraitement();
                panelOptionsTerrain.miseAJour();
                panelCreationEquipes.miseAJour();
                
                lblEtat.setForeground(LookInterface.COULEUR_SUCCES);
                lblEtat.setText("Fichier chargé");
            } 
            catch (ClassCastException e1)
            {
                lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
                lblEtat.setText("Fichier invalide");
            } 
            catch (IOException e1)
            {
                lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
                lblEtat.setText("Fichier invalide");
            } 
            catch (ClassNotFoundException e1)
            {
                lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
                lblEtat.setText("Fichier invalide");
            }
        }
    }

    @Override
    public void zoneModifiee(Rectangle zone)
    {
        lblEtat.setForeground(Color.BLACK);
        lblEtat.setText("x:"+zone.x+" y:"+zone.y+" l:"+zone.width+" h:"+zone.height); 
    }

    @Override
    public void zoneSelectionnee(Rectangle zone)
    {
        lblEtat.setForeground(Color.BLACK);
        lblEtat.setText("x:"+zone.x+" y:"+zone.y+" l:"+zone.width+" h:"+zone.height);
    }
}
