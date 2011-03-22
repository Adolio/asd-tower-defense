package vues.editeurTerrain;

import i18n.Langue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import models.jeu.Jeu;
import models.jeu.Jeu_Solo;
import models.jeu.ModeDeJeu;
import models.joueurs.Joueur;
import models.outils.GestionnaireSons;
import models.terrains.Terrain;
import outils.OutilsFichier;
import vues.GestionnaireDesPolices;
import vues.LookInterface;
import vues.Panel_MenuPrincipal;
import vues.commun.Panel_Terrain;
import vues.commun.TableCellRenderer_Image;
import vues.solo.Fenetre_JeuSolo;
import exceptions.*;

/**
 * Panel de création d'une partie réseau.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 */
@SuppressWarnings("serial")
public class Panel_PartiePersonnalisee extends JPanel implements ActionListener
{
    private static final int MARGES_PANEL = 40;
    private static final ImageIcon I_EDITEUR_T = new ImageIcon("img/icones/map_edit.png");
    private static final ImageIcon I_PLAY = new ImageIcon("img/icones/controller.png");

    private JFrame parent;
    
    // form
    private JLabel lblTitreTerrains = new JLabel(Langue.getTexte(Langue.ID_TITRE_CHOIX_TERRAIN));
    private JLabel lblEtat = new JLabel();
    
    private JButton bLancer = new JButton(Langue.getTexte(Langue.ID_TXT_BTN_DEMARRER),I_PLAY);
    private JButton bEditerCarte = new JButton("Edit this map",I_EDITEUR_T);
    private JButton bRetour = new JButton(Langue.getTexte(Langue.ID_TXT_BTN_RETOUR));
    private JButton bEditeurDeTerrain = new JButton(Langue.getTexte(Langue.ID_TXT_BTN_EDITEUR_DE_TERRAIN), I_EDITEUR_T);
    
    // terrains
    
    private ArrayList<File> fichiersTerrains = new ArrayList<File>();
    private ArrayList<Terrain> terrains = new ArrayList<Terrain>();
    private DefaultTableModel model = new DefaultTableModel();
    private JTable tbTerrains;
    
    private Jeu jeu = new Jeu_Solo();
    
    Panel_Terrain pEmplacementTerrain;
    
   
    /**
     * Constructeur
     * 
     * @param parent la fenetre parent
     */
    public Panel_PartiePersonnalisee(JFrame parent)
    {
        // initialisation
        super(new BorderLayout());
        
        
        jeu.setTerrain(new Terrain(jeu));
        pEmplacementTerrain = new Panel_Terrain(jeu,null);
        pEmplacementTerrain.setPreferredSize(new Dimension(300,300));
        pEmplacementTerrain.basculerAffichageFPS();
        pEmplacementTerrain.basculeraffichageZonesDepartArrivee();
        
        this.parent = parent;
        parent.setTitle(Langue.getTexte(Langue.ID_TITRE_PARTIE_PERSONNALISEES)+" - ASD Tower Defense");
        setBorder(new EmptyBorder(new Insets(MARGES_PANEL, MARGES_PANEL,
                MARGES_PANEL, MARGES_PANEL)));

        setBackground(LookInterface.COULEUR_DE_FOND_PRI);

        // ---------
        // -- TOP --
        // ---------
        JPanel pTop = new JPanel(new BorderLayout());

        pTop.setOpaque(false);

        JLabel lblTitre = new JLabel(Langue.getTexte(Langue.ID_TITRE_PARTIE_PERSONNALISEES));
        
        lblTitre.setFont(GestionnaireDesPolices.POLICE_TITRE);
        lblTitre.setForeground(LookInterface.COULEUR_TEXTE_PRI);
        pTop.add(lblTitre, BorderLayout.NORTH);

        add(pTop, BorderLayout.NORTH);

        // ------------
        // -- CENTER --
        // ------------
        JPanel pCentre = new JPanel(new GridBagLayout());
        pCentre.setBorder(new LineBorder(LookInterface.COULEUR_DE_FOND_SEC));
        pCentre.setOpaque(false);

        int ligne = 0;
        
        GridBagConstraints c = new GridBagConstraints();
        final int margesCellule = 15;
        c.insets = new Insets(margesCellule, margesCellule, margesCellule,
               margesCellule);
        c.anchor = GridBagConstraints.LINE_START;

        // -------------------------
        // -- Editeur de terrrain --
        // -------------------------
        c.gridx = 0;
        c.gridy = ligne;
        
        bEditeurDeTerrain.addActionListener(this);
        bEditeurDeTerrain.setPreferredSize(new Dimension(180, 50));
        GestionnaireDesPolices.setStyle(bEditeurDeTerrain);
        pCentre.add(bEditeurDeTerrain, c);
        
        ligne++;
        
        // --------------
        // -- terrains --
        // --------------
        c.gridx = 0;
        c.gridy = ligne;
        c.gridwidth = 4;
        
        JPanel pTerrains = new JPanel(new BorderLayout());
        pTerrains.setPreferredSize(new Dimension(650, 250));
        pTerrains.setOpaque(false);

        lblTitreTerrains.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        lblTitreTerrains.setForeground(LookInterface.COULEUR_TEXTE_PRI);
        pTerrains.add(lblTitreTerrains,BorderLayout.NORTH);
        
        // création de la table avec boquage des editions
        tbTerrains = new JTable(model)
        {
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return false; // toujours désactivé
            }
        };
        
        // evenement sur le changement de sélection
        tbTerrains.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent){
                
                if (listSelectionEvent.getValueIsAdjusting())
                    return;
                    
                ListSelectionModel lsm = (ListSelectionModel)listSelectionEvent.getSource();
                
                if (!lsm.isSelectionEmpty()) 
                {
                    int ligneSelectionnee = lsm.getMinSelectionIndex();
                    
                    jeu.setTerrain(terrains.get(ligneSelectionnee));
                    
                    pEmplacementTerrain.voirToutLeTerrain();
                    
                    //pEmplacementTerrain.setTerrain(terrains.get(ligneSelectionnee));
                }
            }});

        // Simple selection
        tbTerrains.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // nom de colonnes
        model.addColumn(Langue.getTexte(Langue.ID_TXT_DESCRIPTION));
        model.addColumn(Langue.getTexte(Langue.ID_TXT_APERCU));

        
        // Taille des colonnes
        tbTerrains.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  
        tbTerrains.getColumnModel().getColumn(0).setPreferredWidth(318);
        tbTerrains.getColumnModel().getColumn(1).setPreferredWidth(60);
         
        // propiete des
        tbTerrains.setRowHeight(60);
        
        tbTerrains.getColumnModel().getColumn(1).setCellRenderer(
                new TableCellRenderer_Image());

        // Chargement de toutes les maps
        File repertoireMaps = new File("maps/solo");
        File[] listFiles = repertoireMaps.listFiles();
        
        Terrain t;
        String extFichier;
        
        int i = 0;
        for (File f2 : listFiles)
        {
            extFichier = OutilsFichier.getExtension(f2);

            if (extFichier.equals(Terrain.EXTENSION_FICHIER))
            {
                try{
                    t = Terrain.charger(f2);

                    terrains.add(t);
                    fichiersTerrains.add(f2);
                    
                    Object[] obj = new Object[] { t.getBreveDescription(), t };
                    
                    model.addRow(obj);
                    
                    i++;
                } 
                catch (IOException e)
                {
                    lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
                    lblEtat.setText("Erreur lors du chargement des terrains");
                    e.printStackTrace();
                } 
                catch (ClassNotFoundException e)
                {
                    lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
                    lblEtat.setText("Erreur lors du chargement des terrains");
                    e.printStackTrace();
                } 
            }
        }
        
        // selection de la première map
        if(i > 0)
            tbTerrains.setRowSelectionInterval(0, 0);
        
       
        JScrollPane spTerrains =  new JScrollPane(tbTerrains);
        spTerrains.setPreferredSize(new Dimension(400,200));
        pTerrains.add(spTerrains, BorderLayout.WEST);
        
        
        JPanel pTmp = new JPanel(new BorderLayout());
        pTmp.setOpaque(false);
        pTmp.add(pEmplacementTerrain, BorderLayout.NORTH);

        pTerrains.add(pTmp, BorderLayout.CENTER);
        
        pCentre.add(pTerrains, c);

        
        // changement de ligne
        ligne++;
        
        // ajout du panel central
        add(pCentre, BorderLayout.CENTER);

        // ------------
        // -- BOTTOM --
        // ------------
        JPanel pBottom = new JPanel(new BorderLayout());
        pBottom.setOpaque(false);

        
        // bouton lancer
        bLancer.setPreferredSize(new Dimension(100, 50));
        GestionnaireDesPolices.setStyle(bLancer);
        bLancer.addActionListener(this);
        
        bEditerCarte.setPreferredSize(new Dimension(150, 50));
        GestionnaireDesPolices.setStyle(bEditerCarte);
        bEditerCarte.addActionListener(this);
        
        JPanel panelEst = new JPanel();
        panelEst.setOpaque(false);
        panelEst.add(bEditerCarte);
        panelEst.add(bLancer);
        pBottom.add(panelEst, BorderLayout.EAST);
        pBottom.add(lblEtat, BorderLayout.SOUTH);

        bRetour.addActionListener(this);
        GestionnaireDesPolices.setStyle(bRetour);
        bRetour.setPreferredSize(new Dimension(80,50));
        pBottom.add(bRetour, BorderLayout.WEST);

        add(pBottom, BorderLayout.SOUTH);
    }

    public String[] listFiles(String dir) throws Exception
    {
        return new File(dir).list();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();

        if (src == bLancer)
        {
            // Test des champs...
            lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
            
            if(tbTerrains.getSelectedRow() == -1)
            {
                lblEtat.setText("Veuillez sélectionner un terrain de jeu.");
                return;
            }
  
            //---------------------
            //-- Création du jeu --
            //---------------------
            
            Terrain terrain = terrains.get(tbTerrains.getSelectedRow());
            terrain.initialiser();
            
            if(terrain.getMode() == ModeDeJeu.MODE_SOLO)
            {
                Jeu_Solo jeu = new Jeu_Solo();

                jeu.setTerrain(terrain);
                terrain.setJeu(jeu);
                
                Joueur j = new Joueur("sans nom");
                jeu.setJoueurPrincipal(j);
                
                try
                {
                    jeu.ajouterJoueur(j);
                    
                    jeu.getTerrain().setLargeurMaillage(jeu.getTerrain().getLargeur());
                    jeu.getTerrain().setHauteurMaillage(jeu.getTerrain().getHauteur());
                    
                    jeu.getTerrain().initialiser();
                    jeu.initialiser();
                    
                    GestionnaireSons.arreterTousLesSons();
                    
                    new Fenetre_JeuSolo(jeu);
                    
                    parent.dispose();
                } 
                catch (JeuEnCoursException e1)
                {
                    lblEtat.setText("Jeu en cours ?!?");
                } 
                catch (AucunePlaceDisponibleException e1)
                {
                    lblEtat.setText("Aucune place disponible dans ce terrain.");
                } 
            }
        }
        else if(src == bEditerCarte)
        {
            Terrain terrain = terrains.get(tbTerrains.getSelectedRow());
            
            File fichierTerrain = fichiersTerrains.get(tbTerrains.getSelectedRow());

            terrain.initialiser();
            new Fenetre_CreationTerrain(terrain,fichierTerrain);
            
            GestionnaireSons.arreterTousLesSons();
            
            parent.dispose();
        }
        else if(src == bEditeurDeTerrain)
        {
            new Fenetre_CreationTerrain();
            
            GestionnaireSons.arreterTousLesSons();
            
            parent.dispose();
        }
        else if (src == bRetour)
        {
            parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_MenuPrincipal(parent),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();
        }
    }
}
