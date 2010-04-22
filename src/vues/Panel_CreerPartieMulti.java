package vues;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class Panel_CreerPartieMulti extends JPanel implements ActionListener
{
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
    private JTextField tfNomServeur = new JTextField();

    private JLabel lblEquipeAleatoire = new JLabel("Equipe aléatoire :");
    private JCheckBox cbEquipeAleatoire = new JCheckBox();

    private JButton bAnnuler = new JButton("Annuler");

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

            // connexion réussie
            parent.getContentPane().removeAll();
            parent.getContentPane().add(
                    new Panel_AttendreJoueurs(parent, false),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();

        } else if (src == bAnnuler)
        {
            parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_MenuPrincipal(parent),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();
        }
    }
}
