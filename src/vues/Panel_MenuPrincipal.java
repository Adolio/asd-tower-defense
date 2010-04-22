package vues;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import models.jeu.Jeu;

public class Panel_MenuPrincipal extends JPanel implements ActionListener
{
    // constantes statiques
    private static final long serialVersionUID = 1L;
    private static final Color COULEUR_DE_FOND = new Color(50, 50, 50);
    private static final ImageIcon IMAGE_MENU = new ImageIcon(
            "img/tours/towers.png");
    private static final Color COULEUR_TEXTE_VERSION = new Color(200, 200, 200);

    // elements du formulaire
    private JLabel version;

    private JButton bPartieSolo = new JButton("Partie Solo");
    private JButton bRejoindrePartieMulti = new JButton("Rejoindre");
    private JButton bCreerPartieMulti = new JButton("Créer");
    private JButton bQuitter = new JButton("Quitter");

    private JFrame parent;

    /**
     * Constructeur de la fenetre du menu principal
     */
    public Panel_MenuPrincipal(JFrame parent)
    {
        super(new BorderLayout());

        this.parent = parent;

        // -------------------------------
        // -- preferances de le fenetre --
        // -------------------------------
        parent.setTitle("Menu principal - ASD Tower Defense");

        // ---------------------------
        // -- element du formulaire --
        // ---------------------------

        setBackground(COULEUR_DE_FOND);
        add(new JLabel(IMAGE_MENU), BorderLayout.NORTH);

        JPanel pAbsolu = new JPanel(null); // layout absolu
        pAbsolu.setPreferredSize(new Dimension(0, 160));
        pAbsolu.setBackground(COULEUR_DE_FOND);

        // partie solo
        bPartieSolo.addActionListener(this);
        bPartieSolo.setBounds(50, 20, 100, 30);
        pAbsolu.add(bPartieSolo);

        // partie multijoueurs
        bRejoindrePartieMulti.setBounds(50, 60, 100, 30);
        bRejoindrePartieMulti.addActionListener(this);
        pAbsolu.add(bRejoindrePartieMulti);

        bCreerPartieMulti.setBounds(160, 60, 100, 30);
        bCreerPartieMulti.addActionListener(this);
        pAbsolu.add(bCreerPartieMulti);

        // quitter
        bQuitter.addActionListener(this);
        bQuitter.setBounds(480, 20, 100, 30);
        pAbsolu.add(bQuitter);

        add(pAbsolu, BorderLayout.CENTER);

        version = new JLabel(Jeu.getVersion());
        version.setForeground(COULEUR_TEXTE_VERSION);
        add(version, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object source = ae.getSource();

        // quitter
        if (source == bQuitter)
            System.exit(0); // Fermeture correcte du logiciel

        else if (source == bPartieSolo)
        {
            parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_ModeSolo(parent),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();
        } else if (source == bRejoindrePartieMulti)
        {
            parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_RejoindrePartieMulti(parent),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();
        } else if (source == bCreerPartieMulti)
        {
            parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_CreerPartieMulti(parent),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();
        }
    }
}