package vues;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import models.outils.MeilleursScores;

/**
 * Fenetre qui permet d'ajouter un score au fichier des meilleurs scores.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 17 decembre 2009
 * @since jdk1.6.0_16
 * @see MeilleursScores
 */
public class Fenetre_AjoutScore extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private JButton bOk             = new JButton("OK");
    private JTextField tfNomJoueur  = new JTextField();
    private int score;
    private JPanel pFormulaire;
    private String nomFichier;
    private String nomTerrain;
    
    /**
     * Constructeur de la fenetre
     * @param score le score a ajouter
     */
    public Fenetre_AjoutScore(int score, String nomTerrain)
    {
        // preferences de la fenetre
        super("Score");
        setLayout(new BorderLayout());
        
        // init attributs membres
        this.score      = score;
        this.nomTerrain = nomTerrain;
        this.nomFichier = "donnees/"+nomTerrain+".ms";
        
        
        // constructeur du formulaire
        pFormulaire = new JPanel(new GridLayout(0,2));
        pFormulaire.setBorder(new EmptyBorder(20,20,20,20));
        pFormulaire.add(new JLabel("Nom du joueur : "));
        pFormulaire.add(tfNomJoueur);
        pFormulaire.add(new JPanel());
        pFormulaire.add(bOk);
        bOk.addActionListener(this);
        
        JLabel lTitreForm = new JLabel("Ajoutez votre score");
        lTitreForm.setFont(new Font("",Font.BOLD,14));
        getContentPane().add(lTitreForm,BorderLayout.NORTH);
        getContentPane().add(pFormulaire,BorderLayout.CENTER);
        
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
