package vues;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import models.outils.MeilleursScores;

/**
 * Fenetre qui informe le joueur que la partie est terminee (il a perdu).
 * 
 * Permet aussi d'ajouter un score au fichier des meilleurs scores.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 17 decembre 2009
 * @since jdk1.6.0_16
 * @see MeilleursScores
 */
public class Fenetre_PartieTerminee extends JDialog implements ActionListener
{
    // constantes statiques
    private static final long serialVersionUID      = 1L;
    private static final Dimension TAILLE_FENETRE   = new Dimension(300,170);
    private static final String TITRE_FORM          = "Partie terminée !";
    private static final Font POLICE_TITRE          = new Font("",Font.BOLD,14);
    
    // membrea graphiques
    private JButton bOk             = new JButton("OK");
    private JTextField tfNomJoueur  = new JTextField();
    private JPanel pFormulaire;
    private String nomFichier;
    private String nomTerrain;
    
    // autres membres
    private int score;
    
    /**
     * Constructeur de la fenetre
     * @param score le score a ajouter
     */
    public Fenetre_PartieTerminee(Frame fenParent,int score, String nomTerrain)
    {
        // preferences de la fenetre
        super(fenParent,"Partie Terminée.",true);
        setLayout(new BorderLayout());
        setPreferredSize(TAILLE_FENETRE);
        setResizable(false);
        
        // init attributs membres
        this.score      = score;
        this.nomTerrain = nomTerrain;
        this.nomFichier = "donnees/"+nomTerrain+".ms";
        
        // constructeur du formulaire
        pFormulaire = new JPanel(new GridLayout(0,2));
        pFormulaire.setBorder(new EmptyBorder(20,20,20,20));
        
        pFormulaire.add(new JLabel("Score obtenu : "));
        pFormulaire.add(new JLabel(score+""));
        
        pFormulaire.add(new JLabel("Votre nom : "));
        pFormulaire.add(tfNomJoueur);
        pFormulaire.add(new JPanel());
        pFormulaire.add(bOk);
        bOk.addActionListener(this);
        
        JPanel conteneurTitre = new JPanel(new FlowLayout());
        JLabel lTitreForm = new JLabel(TITRE_FORM);
        lTitreForm.setFont(POLICE_TITRE);
        conteneurTitre.add(lTitreForm);
        
        getContentPane().add(conteneurTitre,BorderLayout.NORTH);
        getContentPane().add(pFormulaire,BorderLayout.SOUTH);
        
        // derniers parametres de la fenetre
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Gestion des evenements
     * @param ae l'evenement associe
     */
    public void actionPerformed(ActionEvent ae)
    {
        // le nom n'est pas vide
        if(!tfNomJoueur.getText().isEmpty())
        {
            // ajout du nouveau score
            MeilleursScores ms = new MeilleursScores(nomFichier);
            ms.ajouterMeilleurScore(tfNomJoueur.getText(), score);
            
            dispose(); // fermeture
            
            // ouverture de la fenetre des meilleurs scores
            new Fenetre_MeilleursScores(nomTerrain);
        } 
    }
}
