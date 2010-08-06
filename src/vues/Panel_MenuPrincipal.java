package vues;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import models.jeu.Jeu;

/**
 * Panel du menu principal de l'application.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 */
public class Panel_MenuPrincipal extends JPanel implements ActionListener
{
    // constantes statiques
    private final int MARGES_PANEL = 40;
    private static final long serialVersionUID = 1L;
    private static final Image IMAGE_DE_FOND = Toolkit.getDefaultToolkit().getImage("img/interfaces/menuPrincipal.png");
    
    // elements du formulaire
    private JLabel version;

    private JButton bPartieSolo = new JButton("Solo");
    private JButton bRejoindrePartieMulti = new JButton("Rejoindre");
    private JButton bCreerPartieMulti = new JButton("Créer");
    private JButton bRegles = new JButton("Règles");
    private JButton bAPropos = new JButton("A propos");
    private JButton bOptions = new JButton("Options");
    private JButton bQuitter = new JButton("Quitter");
    private JButton bPartiePerso = new JButton("Vos parties");
    
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

        setBorder(new EmptyBorder(new Insets(MARGES_PANEL, MARGES_PANEL,
                MARGES_PANEL, MARGES_PANEL)));
        
        
        // attent que toutes les images soit complementements chargees
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(IMAGE_DE_FOND, 0);

        try { 
            tracker.waitForAll(); 
        } 
        catch (InterruptedException e){ 
            e.printStackTrace(); 
        }
        
        // ---------------------------
        // -- element du formulaire --
        // ---------------------------

        setBackground(LookInterface.COULEUR_DE_FOND_PRI);
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(1,280));
        
        add(p, BorderLayout.NORTH);

        
        JPanel pAbsolu = new JPanel(null); // layout absolu
        pAbsolu.setPreferredSize(new Dimension(0, 160));
        pAbsolu.setOpaque(false);

        // partie solo
        bPartieSolo.addActionListener(this);
        bPartieSolo.setBounds(50, 0, 100, 50);
        parent.getRootPane().setDefaultButton(bPartieSolo); // def button
        GestionnaireDesPolices.setStyle(bPartieSolo);  
        pAbsolu.add(bPartieSolo);
        
        bPartiePerso.addActionListener(this);
        bPartiePerso.setBounds(160, 0, 100, 50); 
        GestionnaireDesPolices.setStyle(bPartiePerso);  
        pAbsolu.add(bPartiePerso);
        
        // partie multijoueurs
        bRejoindrePartieMulti.setBounds(50, 100, 100, 50);
        bRejoindrePartieMulti.addActionListener(this);
        GestionnaireDesPolices.setStyle(bRejoindrePartieMulti);
        pAbsolu.add(bRejoindrePartieMulti);

        bCreerPartieMulti.setBounds(160, 100, 100, 50);
        bCreerPartieMulti.addActionListener(this);
        GestionnaireDesPolices.setStyle(bCreerPartieMulti);
        pAbsolu.add(bCreerPartieMulti);

        // Regles
        bRegles.addActionListener(this);
        bRegles.setBounds(555, 0, 100, 25);
        GestionnaireDesPolices.setStyle(bRegles);
        pAbsolu.add(bRegles);

        // A propos
        bAPropos.addActionListener(this);
        bAPropos.setBounds(555, 30, 100, 25);
        GestionnaireDesPolices.setStyle(bAPropos);
        pAbsolu.add(bAPropos);

        // Options
        bOptions.addActionListener(this);
        bOptions.setBounds(555, 60, 100, 25);
        GestionnaireDesPolices.setStyle(bOptions);
        pAbsolu.add(bOptions);
        
        // quitter
        bQuitter.addActionListener(this);
        bQuitter.setBounds(555, 100, 100, 50);
        GestionnaireDesPolices.setStyle(bQuitter);
        pAbsolu.add(bQuitter);

        add(pAbsolu, BorderLayout.CENTER);

        version = new JLabel(Jeu.getVersion());
        version.setForeground(LookInterface.COULEUR_TEXTE_PRI);
        add(version, BorderLayout.SOUTH);
    }
    
    public void paintComponent(Graphics g)
    {
        g.setColor(LookInterface.COULEUR_DE_FOND_PRI);
        g.fillRect(0, 0, 800, 600);
        
        g.drawImage(IMAGE_DE_FOND, 0, 0, null);
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        Object source = ae.getSource();

        if (source == bPartieSolo)
        {
            parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_ModeSolo(parent),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();
        } 
        else if (source == bPartiePerso)
        {
            parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_PartiePersonnalisee(parent),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();
        }   
        else if (source == bRejoindrePartieMulti)
        {
            parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_RejoindrePartieMulti(parent),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();
        } 
        else if (source == bCreerPartieMulti)
        {
            parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_CreerPartieMulti(parent),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();
        }
        else if(source == bRegles)
            new Fenetre_HTML("Règles du jeu", new File("donnees/regles/regles.html"), parent);
       
        else if(source == bAPropos)
            new Fenetre_HTML("A propos",new File("aPropos/aPropos.html"), parent);
        
        else if(source == bOptions)
            new Fenetre_Options();
        
        else if (source == bQuitter)
            System.exit(0); // Fermeture correcte du logiciel
    }
}