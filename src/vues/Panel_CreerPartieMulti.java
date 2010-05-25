package vues;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import exceptions.AucunePlaceDisponibleException;
import exceptions.JeuEnCoursException;
import models.jeu.*;
import models.joueurs.Joueur;
import models.terrains.*;
import outils.*;

@SuppressWarnings("serial")
public class Panel_CreerPartieMulti extends JPanel implements ActionListener
{
    private final int MARGES_PANEL = 40;
    private final Dimension DEFAULT_DIMENTION_COMP = new Dimension(120, 25);

    private JFrame parent;

    // form
    private JLabel lblNomServeur = new JLabel("Nom du serveur :");
    private JTextField tfNomServeur = new JTextField("Serveur de test");
    private JLabel lblEquipeAleatoire = new JLabel("Equipes aleatoires :");
    private JCheckBox cbEquipeAleatoire = new JCheckBox();
    private JLabel lblTitreTerrains = new JLabel("Choisissez votre terrain");
    private JLabel lblEtat = new JLabel();
    
    private JLabel lblPseudo = new JLabel("Pseudo : ");
    private JTextField tfPseudo = new JTextField("Joueur", 10);
    private JButton bCreer = new JButton("Créer");
    private JButton bAnnuler = new JButton("Annuler");
    
    // terrains
    private ArrayList<Terrain> terrains = new ArrayList<Terrain>();
    private DefaultTableModel model = new DefaultTableModel();
    private JTable tbTerrains;
    Panel_EmplacementsTerrain pEmplacementTerrain = new Panel_EmplacementsTerrain(150, 150);
   
    /**
     * Constructeur
     * 
     * @param parent la fenetre parent
     */
    public Panel_CreerPartieMulti(JFrame parent)
    {
        // initialisation
        super(new BorderLayout());
        this.parent = parent;
        parent.setTitle("Créer une partie multijoueurs");
        setBorder(new EmptyBorder(new Insets(MARGES_PANEL, MARGES_PANEL,
                MARGES_PANEL, MARGES_PANEL)));

        setBackground(LookInterface.COULEUR_DE_FOND);

        // ---------
        // -- TOP --
        // ---------
        JPanel pTop = new JPanel(new BorderLayout());

        pTop.setOpaque(false);

        JLabel lblTitre = new JLabel("CREER UNE PARTIE");
        lblTitre.setFont(GestionnaireDesPolices.POLICE_TITRE);
        lblTitre.setForeground(GestionnaireDesPolices.COULEUR_TITRE);
        pTop.add(lblTitre, BorderLayout.NORTH);

        add(pTop, BorderLayout.NORTH);

        // ------------
        // -- CENTER --
        // ------------
        JPanel pCentre = new JPanel(new GridBagLayout());
        pCentre.setBorder(new LineBorder(Color.BLACK));
        pCentre.setOpaque(false);

        int ligne = 0;
        
        GridBagConstraints c = new GridBagConstraints();
        final int margesCellule = 15;
        c.insets = new Insets(margesCellule, margesCellule, margesCellule,
                margesCellule);
        c.anchor = GridBagConstraints.LINE_START;

 
        // --------------------
        // -- nom du serveur --
        // --------------------

        c.gridx = 0;
        c.gridy = ligne;

        lblNomServeur.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        lblNomServeur.setForeground(GestionnaireDesPolices.COULEUR_TXT_SUR_COULEUR_DE_FOND);
        pCentre.add(lblNomServeur, c);

        c.gridx = 1;
        c.gridy = ligne;

        tfNomServeur.setPreferredSize(DEFAULT_DIMENTION_COMP);

        pCentre.add(tfNomServeur, c);
        
        // ----------------------
        // -- equipe aléatoire --
        // ----------------------

        c.gridx = 2;
        c.gridy = ligne;

        lblEquipeAleatoire.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        lblEquipeAleatoire.setForeground(GestionnaireDesPolices.COULEUR_TXT_SUR_COULEUR_DE_FOND);
        pCentre.add(lblEquipeAleatoire, c);

        c.gridx = 3;
        c.gridy = ligne;

        pCentre.add(cbEquipeAleatoire, c);

        
        // changement de ligne
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
        lblTitreTerrains.setForeground(GestionnaireDesPolices.COULEUR_TXT_SUR_COULEUR_DE_FOND);
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
                    
                    pEmplacementTerrain.setTerrain(terrains.get(ligneSelectionnee));
                }
            }});
     
        
        

        // Simple selection
        tbTerrains.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // nom de colonnes
        model.addColumn("Nom");
        model.addColumn("Mode");
        model.addColumn("Joueurs");
        model.addColumn("Equipes");
        model.addColumn("Apercu");

        
        // Taille des colonnes
        tbTerrains.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  
        tbTerrains.getColumnModel().getColumn(0).setPreferredWidth(210);
        tbTerrains.getColumnModel().getColumn(1).setPreferredWidth(60);
        tbTerrains.getColumnModel().getColumn(2).setPreferredWidth(60);
        tbTerrains.getColumnModel().getColumn(3).setPreferredWidth(60);
        tbTerrains.getColumnModel().getColumn(4).setPreferredWidth(60);
         
        // propiete des
        tbTerrains.setRowHeight(60);
        
        tbTerrains.getColumnModel().getColumn(4).setCellRenderer(
                new TableCellRenderer_Image());

        // Chargement de toutes les maps
        File repertoireMaps = new File("maps/");
        File[] listFiles = repertoireMaps.listFiles();
        
        Terrain t;
        String extFichier;
        for (File f2 : listFiles)
        {
            extFichier = OutilsFichier.getExtension(f2);

            if (extFichier.equals(Terrain.EXTENSION_FICHIER))
            {
                t = Terrain.charger(f2);
                
                terrains.add(t);
                
                Object[] obj = new Object[] { t.getNom(), ModeDeJeu.getNomMode(t.getMode()), t.getNbJoueursMax(), 
                        t.getEquipesInitiales().size()+"", t.getImageDeFond() };
                
                model.addRow(obj);
            }
        }

        pTerrains.add(new JScrollPane(tbTerrains), BorderLayout.WEST);
        
        pEmplacementTerrain.setTerrain(new ElementTD_Coop(new Jeu_Solo()));
        
        JPanel pTmp = new JPanel(new BorderLayout());
        pTmp.setOpaque(false);
        pTmp.add(new JScrollPane(pEmplacementTerrain), BorderLayout.NORTH);

        pTerrains.add(pTmp, BorderLayout.EAST);
        
        pCentre.add(pTerrains, c);

        
        // changement de ligne
        ligne++;
        
        // TODO amélioration
        /*
        // ----------------
        // -- Nb Joueurs --
        // ----------------
        
        c.gridx = 0;
        c.gridy = ligne;
        
        lblNbJoueurs.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        lblNbJoueurs.setForeground(GestionnaireDesPolices.COULEUR_SOUS_TITRE);
        pCentre.add(lblNbJoueurs, c);

        cbNbJoueurs.addItem("2");
        cbNbJoueurs.addItem("3");
        cbNbJoueurs.addItem("4");
        cbNbJoueurs.addItem("5");
        cbNbJoueurs.addItem("6");
        cbNbJoueurs.addItem("7");
        cbNbJoueurs.addItem("8");
        cbNbJoueurs.setPreferredSize(DEFAULT_DIMENTION_COMP);

        c.gridx = 1;
        c.gridy = ligne;

        pCentre.add(cbNbJoueurs, c);

        // ----------
        // -- mode --
        // ----------

        c.gridx = 2;
        c.gridy = ligne;

        lblMode.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        lblMode.setForeground(GestionnaireDesPolices.COULEUR_SOUS_TITRE);
        pCentre.add(lblMode, c);

        c.gridx = 3;
        c.gridy = ligne;

        cbMode.addItem("Versus");
        cbMode.addItem("Coopération");
        cbMode.setPreferredSize(DEFAULT_DIMENTION_COMP);

        pCentre.add(cbMode, c);
        */
        
        // ajout du panel central
        add(pCentre, BorderLayout.CENTER);

        // ------------
        // -- BOTTOM --
        // ------------
        JPanel pBottom = new JPanel(new BorderLayout());
        pBottom.setOpaque(false);

        // pseudo
        JPanel pPseudo = new JPanel();
        pPseudo.setOpaque(false);

        JPanel pAlignementADroite = new JPanel();
        pAlignementADroite.setOpaque(false);

        lblPseudo.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        lblPseudo.setForeground(GestionnaireDesPolices.COULEUR_TXT_SUR_COULEUR_DE_FOND);
        pAlignementADroite.add(lblPseudo, BorderLayout.WEST);
        pAlignementADroite.add(tfPseudo, BorderLayout.EAST);
        pPseudo.add(pAlignementADroite, BorderLayout.EAST);
        pBottom.add(pPseudo, BorderLayout.CENTER);

        // bouton créer
        bCreer.setPreferredSize(new Dimension(100, 50));
        GestionnaireDesPolices.setStyle(bCreer);
        pBottom.add(bCreer, BorderLayout.EAST);
        bCreer.addActionListener(this);

        pBottom.add(lblEtat, BorderLayout.SOUTH);

        bAnnuler.addActionListener(this);
        GestionnaireDesPolices.setStyle(bAnnuler);
        pBottom.add(bAnnuler, BorderLayout.WEST);

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

        if (src == bCreer)
        {
            // Test des champs...
            lblEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
            
            if(tfNomServeur.getText().isEmpty())
            {
                lblEtat.setText("Veuillez saisir le nom de votre partie.");
                return;
            }
            
            if(tfPseudo.getText().isEmpty())
            {
                lblEtat.setText("Veuillez entrer votre pseudo.");
                return;
            }
            
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
            Jeu_Serveur jeu = new Jeu_Serveur();
            jeu.setTerrain(terrain);
            terrain.setJeu(jeu);
            
            // ajout du joueur dans le premier emplacement
            Joueur joueur1 = new Joueur(tfPseudo.getText());
            try
            {
                jeu.ajouterJoueur(joueur1);
            } 
            catch (JeuEnCoursException e1)
            {
                e1.printStackTrace();
            } 
            catch (AucunePlaceDisponibleException e1)
            {
                e1.printStackTrace();
            }
            
            /* TODO a effacer
            Joueur joueur2 = new Joueur("fictiveBoy");
            jeu.ajouterJoueur(joueur2);
            
            Joueur joueur3 = new Joueur("fictiveGirl");
            jeu.ajouterJoueur(joueur3);
            */
            
            // ---------------------------------------------------------------
            // -- Enregistrement du serveur sur le serveur d'enregistrement --
            // ---------------------------------------------------------------
        
            // Information
            lblEtat.setForeground(GestionnaireDesPolices.COULEUR_TEXTE);
            lblEtat.setText("Enregistrement au serveur central...");

            if (jeu.enregistrerSurSE(tfNomServeur.getText(),
                    terrain.getNbJoueursMax(),
                    (String) model.getValueAt(tbTerrains.getSelectedRow(), 0),
                    terrain.getMode()))
            {
                lblEtat.setForeground(GestionnaireDesPolices.COULEUR_SUCCES);
                lblEtat.setText("Enregistrement au serveur central réussi!");
            } 
            else
            {
                lblEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
                lblEtat.setText("Enregistrement au serveur central échoué!");
            }

            
            // --------------------------------
            // -- Création du serveur de jeu --
            // --------------------------------

            if(jeu.etablissementDuServeur())
            {
                // connexion réussie
                parent.getContentPane().removeAll();
                parent.getContentPane().add(
                        new Panel_AttendreJoueurs(parent, jeu, joueur1),
                        BorderLayout.CENTER);
                parent.getContentPane().validate();
            }

        } 
        else if (src == bAnnuler)
        {
            parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_MenuPrincipal(parent),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();
        }
    }
}
