package vues;

import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import models.outils.*;

/**
 * Fenetre de gestion de l'affichage des meilleurs scores
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 17 decembre 2009
 * @since jdk1.6.0_16
 */
public class Fenetre_MeilleursScores extends JDialog
{
    // constantes statiques
    private static final long serialVersionUID  = 1L;
    private static final ImageIcon I_FENETRE    = new ImageIcon("img/icones/star.png");

    // membre graphiques
    private JButton bFermer = new JButton("Fermer");
    
    // autres membres
    private MeilleursScores ms;
   
    /**
     * Constructeur de la fenetre des meilleurs scores d'un terrain avec une 
     * boite de dialogue comme parent.
     * 
     * @param nomTerrain le nom du terrain dont on veut voir les meilleurs scores
     * @param parent le Dialog parent
     */
    public Fenetre_MeilleursScores(String nomTerrain, Dialog parent)
    {
        // preference de la fenetre
        super(parent,"Les "+MeilleursScores.NOMBRE_MAX_SCORES+" Meilleurs scores",true);
        
        construire(nomTerrain);
    }
    
    /**
     * Constructeur de la fenetre des meilleurs scores d'un terrain avec une 
     * fenetre comme parent.
     * 
     * @param nomTerrain le nom du terrain dont on veut voir les meilleurs scores
     * @param parent la Frame parent
     */
    public Fenetre_MeilleursScores(String nomTerrain, Frame parent)
    {
        super(parent,"Les "+MeilleursScores.NOMBRE_MAX_SCORES+" Meilleurs scores",true);
    
        construire(nomTerrain);
    }

    /**
     * Permet de construire le contenu de la fenetre.
     * 
     * @param nomTerrain le nom du terrain
     */
    @SuppressWarnings("serial")
    private void construire(String nomTerrain)
    { 
        setIconImage(I_FENETRE.getImage());
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel pFormulaire = new JPanel(new BorderLayout());
        pFormulaire.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        pFormulaire.setBackground(LookInterface.COULEUR_DE_FOND);
        
        //------------------------------------
        //-- creation de la table de scores --
        //------------------------------------
        DefaultTableModel model = new DefaultTableModel();
        
        // nom de colonnes
        model.addColumn("Joueur");
        model.addColumn("Score");
        model.addColumn("Date");
        
        // création de la table avec boquage des editions
        JTable tbScores = new JTable(model)
        {
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return false; // toujours désactivé
            }
        };

        tbScores.setEnabled(false);
        tbScores.setCellSelectionEnabled(true);
       
        // taille des colonnes
        tbScores.getColumnModel().getColumn(0).setPreferredWidth(100);
        tbScores.getColumnModel().getColumn(1).setPreferredWidth(50);
        tbScores.getColumnModel().getColumn(2).setPreferredWidth(120);
  
        
        ms = new MeilleursScores("donnees/"+nomTerrain+".ms");
        
        for(Score score : ms.getScores())
        {
            Object[] obj = new Object[] { score.getNomJoueur(), score.getValeur()+"",
                    DateFormat.getInstance().format(score.getDate()) };
            
            model.addRow(obj);
        }
         
        JLabel lTitreForm = new JLabel(nomTerrain);
        lTitreForm.setFont(GestionnaireDesPolices.POLICE_TITRE);
        pFormulaire.add(lTitreForm,BorderLayout.NORTH);
        pFormulaire.add(new JScrollPane(tbScores),BorderLayout.CENTER);
        pFormulaire.add(bFermer,BorderLayout.SOUTH);
        getContentPane().add(pFormulaire,BorderLayout.CENTER);
         
        getRootPane().setDefaultButton(bFermer); // def button
        GestionnaireDesPolices.setStyle(bFermer);
        bFermer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                dispose();
            }
        });
        
        // derniers parametres de la fenetre
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
