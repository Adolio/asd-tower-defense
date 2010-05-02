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
    private static final String TITRE_FORM          = "Partie terminee !";
    
    // membrea graphiques
    private JButton bOk             = new JButton("OK");
    private JButton bAnnuler        = new JButton("Fermer");
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
        setResizable(false);
        getContentPane().setBackground(LookInterface.COULEUR_DE_FOND);
        
        
        // init attributs membres
        this.score      = score;
        this.nomTerrain = nomTerrain;
        this.nomFichier = "donnees/"+nomTerrain+".ms";
        
        // constructeur du formulaire
        pFormulaire = new JPanel(new GridLayout(0,2));
        pFormulaire.setOpaque(false);
        pFormulaire.setBorder(new EmptyBorder(20,20,20,20));
        
        pFormulaire.add(new JLabel("Score obtenu : "));
        pFormulaire.add(new JLabel(score+""));
        
        pFormulaire.add(new JLabel("Votre nom : "));
        pFormulaire.add(tfNomJoueur);
        pFormulaire.add(bAnnuler);
        bAnnuler.addActionListener(this);
        pFormulaire.add(bOk);
        bOk.addActionListener(this);
        
        
        JPanel conteneurTitre = new JPanel(new FlowLayout());
        conteneurTitre.setOpaque(false);
        JLabel lTitreForm = new JLabel(TITRE_FORM);
        lTitreForm.setFont(GestionnaireDesPolices.POLICE_TITRE);
        conteneurTitre.add(lTitreForm);
        
        getContentPane().add(conteneurTitre,BorderLayout.NORTH);
        getContentPane().add(pFormulaire,BorderLayout.SOUTH);
        
        // derniers parametres de la fenetre
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Gestionnaire des evenements. 
     * <p>
     * Cette methode est appelee en cas d'evenement
     * sur un objet ecouteur de ActionListener
     * 
     * @param ae l'evenement associe
     */
    public void actionPerformed(ActionEvent ae)
    {
        Object source = ae.getSource();
        
        if(source == bOk)
        {
            // le nom n'est pas vide
            if(!tfNomJoueur.getText().isEmpty())
            {
                // ajout du nouveau score
                MeilleursScores ms = new MeilleursScores(nomFichier);
                ms.ajouterMeilleurScore(tfNomJoueur.getText(), score);
                
                dispose(); // fermeture
                
                // ouverture de la fenetre des meilleurs scores
                new Fenetre_MeilleursScores(nomTerrain,this);
            } 
        }
        else
        {
            dispose(); // fermeture
        }
    }
}
