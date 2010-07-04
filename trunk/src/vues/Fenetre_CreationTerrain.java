package vues;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import exceptions.*;
import models.creatures.Creature;
import models.jeu.Jeu;
import models.jeu.Jeu_Solo;
import models.joueurs.Joueur;
import models.terrains.Terrain;
import models.tours.Tour;

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
        super("Création de terrain");
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
        JToolBar jToolBar = new JToolBar();
        
        jToolBar.add(bOuvrir);
        jToolBar.add(bEnregistrer);
        jToolBar.addSeparator();
        jToolBar.add(bMain);
        jToolBar.add(bMurs);
        jToolBar.addSeparator();
        jToolBar.add(bTester);
        
        
        bOuvrir.addActionListener(this);
        bEnregistrer.addActionListener(this);
        bMain.addActionListener(this);
        bMurs.addActionListener(this);
        bTester.addActionListener(this);
        
        add(jToolBar,BorderLayout.NORTH);
        
        
        // Onglets de droits
        JTabbedPane panelOnglets = new JTabbedPane();
        
        // Background
        UIManager.put("TabbedPane.tabAreaBackground", LookInterface.COULEUR_DE_FOND);
        //SwingUtilities.updateComponentTreeUI(panelSelectionEtVague);
     
        panelOnglets.setOpaque(true);
        //panelOnglets.setPreferredSize(new Dimension(300,420));
        panelOnglets.setBackground(LookInterface.COULEUR_DE_FOND);
        
        
        panelOptionsTerrain = new Panel_OptionsTerrain(jeu);
        panelCreationEquipes = new Panel_CreationEquipes(jeu);
        
        panelOnglets.add("Options",panelOptionsTerrain );
        panelOnglets.add("Equipes",panelCreationEquipes);
        
        JPanel p = new JPanel(new BorderLayout());
        p.add(panelOnglets,BorderLayout.CENTER);
        add(p,BorderLayout.EAST);
        
        panelCreationTerrain = new Panel_CreationTerrain(jeu,this);
   
        panelCreationTerrain.basculerModeDebug();
        add(panelCreationTerrain,BorderLayout.CENTER);
        
        
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
        if(src == bEnregistrer || src == itemEnregistrer)
        {
            enregistrerTerrain();
        }
        else if(src == bMain)
        {
            panelCreationTerrain.activerModeDeplacement();
        }
        else if(src == bMurs)
        {
            panelCreationTerrain.activerModeCreationMurs();
        }
        else if(src == bTester || src == itemTester)
        {
            Joueur j = new Joueur("Test");
            jeu.setJoueurPrincipal(j);
            
            try
            {
                jeu.ajouterJoueur(j);
                
                jeu.getTerrain().setLargeurMaillage(jeu.getTerrain().getLargeur());
                jeu.getTerrain().setHauteurMaillage(jeu.getTerrain().getHauteur());
                
                jeu.getTerrain().initialiser();
                jeu.initialiser();
                
                new Fenetre_JeuSolo(jeu);
            } 
            catch (JeuEnCoursException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } 
            catch (AucunePlaceDisponibleException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } 
        }
    }

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
            Terrain.serialiser(jeu.getTerrain(),fichierCourant/*new File("maps/"+jeu.getTerrain().getNom()+"."+Terrain.EXTENSION_FICHIER)*/); 
        }
    }

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
                
            } 
            catch (ClassCastException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } 
            catch (IOException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } 
            catch (ClassNotFoundException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        
    }
}
