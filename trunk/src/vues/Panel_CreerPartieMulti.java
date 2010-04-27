package vues;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import javax.swing.border.*;
import org.json.JSONException;
import org.json.JSONObject;
import reseau.Canal;
import reseau.CanalException;
import serveur.enregistrement.CodeEnregistrement;
import serveur.enregistrement.RequeteEnregistrement;

@SuppressWarnings("serial")
public class Panel_CreerPartieMulti extends JPanel implements ActionListener
{
    private static final int NUMERO_PORT = 1234;
    // IP idael : "188.165.41.224";
    // IP lazahr : "10.192.51.161";
    private static final String IP_SE = "127.0.0.1";
    private final int MARGES_PANEL = 40;
    private final Dimension DEFAULT_DIMENTION_COMP = new Dimension(120, 25);

    private JFrame parent;
    private JLabel lblPseudo = new JLabel("Pseudo : ");
    private JTextField tfPseudo = new JTextField(10);
    private JButton bCreer = new JButton("Créer");
    private JLabel lblEtat = new JLabel();

    private JLabel lblNbJoueurs = new JLabel("Nb Joueurs :");
    private JComboBox cbNbJoueurs = new JComboBox();

    private JLabel lblMode = new JLabel("Mode de jeu :");
    private JComboBox cbMode = new JComboBox();

    private JLabel lblNomServeur = new JLabel("Nom du serveur :");
    private JTextField tfNomServeur = new JTextField("Serveur de test");

    private JLabel lblEquipeAleatoire = new JLabel("Equipe aléatoire :");
    private JCheckBox cbEquipeAleatoire = new JCheckBox();

    private JButton bAnnuler = new JButton("Annuler");

    private Canal canalServeurEnregistrement;
    
    
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

        // ---------
        // -- TOP --
        // ---------
        JPanel pTop = new JPanel(new BorderLayout());
        pTop.add(new JLabel("CREER UN PARTIE"), BorderLayout.NORTH);

        add(pTop, BorderLayout.NORTH);

        // ------------
        // -- CENTER --
        // ------------
        JPanel pCentre = new JPanel(new GridBagLayout());
        pCentre.setBorder(new LineBorder(Color.BLACK));

        GridBagConstraints c = new GridBagConstraints();
        final int margesCellule = 5;
        c.insets = new Insets(margesCellule, margesCellule, margesCellule,
                margesCellule);
        c.anchor = GridBagConstraints.LINE_START;

        c.gridx = 0;
        c.gridy = 0;
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
        c.gridy = 0;

        pCentre.add(cbNbJoueurs, c);

        // ----------
        // -- mode --
        // ----------

        c.gridx = 0;
        c.gridy = 1;

        pCentre.add(lblMode, c);

        c.gridx = 1;
        c.gridy = 1;

        cbMode.addItem("Versus");
        cbMode.addItem("Coopération");
        cbMode.setPreferredSize(DEFAULT_DIMENTION_COMP);

        pCentre.add(cbMode, c);

        // --------------------
        // -- nom du serveur --
        // --------------------

        c.gridx = 2;
        c.gridy = 0;

        pCentre.add(lblNomServeur, c);

        c.gridx = 3;
        c.gridy = 0;

        tfNomServeur.setPreferredSize(DEFAULT_DIMENTION_COMP);

        pCentre.add(tfNomServeur, c);

        // ----------------------
        // -- equipe aléatoire --
        // ----------------------

        c.gridx = 2;
        c.gridy = 1;

        pCentre.add(lblEquipeAleatoire, c);

        c.gridx = 3;
        c.gridy = 1;

        pCentre.add(cbEquipeAleatoire, c);

        // --------------
        // -- terrains --
        // --------------

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 4;

        JPanel pTerrains = new JPanel();
        pTerrains.setPreferredSize(new Dimension(600, 200));
        pTerrains.setBorder(new TitledBorder(
                "Terrains disponibles pour vos critères"));
        pCentre.add(new JScrollPane(pTerrains), c);

        // ajout du panel central
        add(pCentre, BorderLayout.CENTER);

        // ------------
        // -- BOTTOM --
        // ------------
        JPanel pBottom = new JPanel(new BorderLayout());

        // pseudo
        JPanel pPseudo = new JPanel();
        JPanel pTmp = new JPanel();

        pTmp.add(lblPseudo, BorderLayout.WEST);
        pTmp.add(tfPseudo, BorderLayout.EAST);
        pPseudo.add(pTmp, BorderLayout.EAST);
        pBottom.add(pPseudo, BorderLayout.CENTER);

        // bouton créer
        bCreer.setPreferredSize(new Dimension(100, 50));
        pBottom.add(bCreer, BorderLayout.EAST);
        bCreer.addActionListener(this);

        lblEtat.setForeground(Color.RED);
        pBottom.add(lblEtat, BorderLayout.SOUTH);

        bAnnuler.addActionListener(this);
        pBottom.add(bAnnuler, BorderLayout.WEST);

        add(pBottom, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();

        if (src == bCreer)
        {
            // TODO test des champs...
            
            
            
            
            // TODO connexion au serveur, demande de création de la partie...
            //---------------------------------------------------------------
            //-- Enregistrement du serveur sur le serveur d'enregistrement --
            //---------------------------------------------------------------
            try
            {
                // Information
                lblEtat.setForeground(Color.BLACK);
                lblEtat.setText("Enregistrement au serveur central...");

                // Création du canal avec le serveur d'enregistrement
                canalServeurEnregistrement = new Canal(IP_SE,NUMERO_PORT,true);
                
                // Création de la requete d'enregistrement
                String requete = RequeteEnregistrement.getRequeteEnregistrer(tfNomServeur.getText(), NUMERO_PORT, Integer.parseInt((String) 
                        cbNbJoueurs.getSelectedItem()),"TruiteTD",(String) cbMode.getSelectedItem());

                // Envoie de la requete
                canalServeurEnregistrement.envoyerString(requete);
                
                // Attente du résultat
                String resultat = canalServeurEnregistrement.recevoirString();
                
                try
                {
                    // Analyse de la réponse du serveur d'enregistrement
                    JSONObject jsonResultat = new JSONObject(resultat);
                    if(jsonResultat.getInt("status") == CodeEnregistrement.OK)
                    {
                        lblEtat.setForeground(Color.GREEN);
                        lblEtat.setText("Enregistrement au serveur central réussi!");
                    }
                    else
                    {
                        lblEtat.setForeground(Color.RED);
                        lblEtat.setText("Réponse du serveur central invalide!");
                    }
                } 
                catch (JSONException e1)
                {
                    lblEtat.setForeground(Color.RED);
                    lblEtat.setText("Réponse du serveur central invalide!");
                }
            } 
            catch (ConnectException e1)
            {
                lblEtat.setForeground(Color.RED);
                lblEtat.setText("Enregistrement au serveur central échoué!");
            } 
            catch (CanalException e1)
            {
                e1.printStackTrace();
            }
            
            // connexion réussie
            parent.getContentPane().removeAll();
            parent.getContentPane().add(
                    new Panel_AttendreJoueurs(parent, canalServeurEnregistrement),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();
            
        } 
        else if (src == bAnnuler)
        {
            parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_MenuPrincipal(parent),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();
            
            // fermeture du canal s'il est ouvert
            if(canalServeurEnregistrement != null)
            {
                try
                {
                    // désenregistrement du serveur
                    canalServeurEnregistrement.envoyerString(RequeteEnregistrement.DESENREGISTRER);
                    canalServeurEnregistrement.recevoirString();
                    
                    // fermeture propre du canal
                    canalServeurEnregistrement.envoyerString(RequeteEnregistrement.STOP);
                    canalServeurEnregistrement.recevoirString();
                }
                // il y a eu une erreur... on quitte tout de même
                catch(CanalException ce){}
                
                canalServeurEnregistrement.fermer();
            }
        }        
    }
}
