package vues;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Fenetre du menu principal du jeu.
 * <p>
 * Affiche un menu permettant au joueur de choisir son mode de jeu.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 20 avril 2010
 * @since jdk1.6.0_16
 */
public class Fenetre_MenuPrincipal extends JFrame implements ActionListener
{
    // constantes statiques
    private static final long serialVersionUID = 1L;
    private static final ImageIcon I_QUITTER = new ImageIcon(
            "img/icones/door_out.png");
    private static final ImageIcon I_AIDE = new ImageIcon("img/icones/help.png");
    private static final ImageIcon I_FENETRE = new ImageIcon(
            "img/icones/icone_pgm.png");

    // elements du formulaire
    private final JMenuBar menuPrincipal = new JMenuBar();
    private final JMenu menuFichier = new JMenu("Fichier");
    private final JMenu menuAide = new JMenu("Aide");
    private final JMenuItem itemAPropos = new JMenuItem("A propos", I_AIDE);
    private final JMenuItem itemQuitter = new JMenuItem("Quitter", I_QUITTER);

    public static final int LARGEUR_FENETRE = 800;
    public static final int HAUTEUR_FENETRE = 600;
    
    /**
     * Constructeur de la fenetre du menu principal
     */
    public Fenetre_MenuPrincipal()
    {
        // -------------------------------
        // -- preferences de le fenetre --
        // -------------------------------
        //setSize(LARGEUR_FENETRE, HAUTEUR_FENETRE);
        setIconImage(I_FENETRE.getImage());
        // TODO uncomment
        // setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // --------------------
        // -- menu principal --
        // --------------------
        // menu Fichier
        menuFichier.add(itemQuitter);
        menuPrincipal.add(menuFichier);

        // menu A propos
        menuAide.add(itemAPropos);
        menuPrincipal.add(menuAide);

        // ajout des ecouteurs
        itemQuitter.addActionListener(this);
        itemAPropos.addActionListener(this);

        // ajout du menu
        setJMenuBar(menuPrincipal);

        // ---------------------
        // -- panel principal --
        // ---------------------
        getContentPane().add(new Panel_MenuPrincipal(this), BorderLayout.CENTER);

        // --------------------------
        // -- dernieres proprietes --
        // --------------------------
        getContentPane().setPreferredSize(new Dimension(800,600));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object source = ae.getSource();

        // quitter
        if (source == itemQuitter)
            System.exit(0); // Fermeture correcte du logiciel

        // a propos
        else if (source == itemAPropos)
            new Fenetre_APropos(this); // ouverture de la fenetre "A propos"
    }
}
