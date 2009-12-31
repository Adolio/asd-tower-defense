package vues;

import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import javax.swing.*;
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
    private static final Font POLICE_TITRE      = new Font("",Font.BOLD,14);
    private static String[] columnNames         = {
                                                    "Nom du joueur",
                                                    "Score",
                                                    "Date"
                                                   };
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
        
        contruire(nomTerrain);
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
    
        contruire(nomTerrain);
    }

    /**
     * Permet de construire le contenu de la fenetre.
     * 
     * @param nomTerrain le nom du terrain
     */
    private void contruire(String nomTerrain)
    {
        setLayout(new BorderLayout());
        setResizable(false);
 
        // creation de la table de scores
        int i = 0;
        String[][] data = new String[MeilleursScores.NOMBRE_MAX_SCORES][3];
        ms = new MeilleursScores("donnees/"+nomTerrain+".ms");
        
        for(Score score : ms.getScores())
        {
            data[i][0] = score.getNomJoueur();
            data[i][1] = score.getValeur()+"";
            data[i][2] = DateFormat.getInstance().format(score.getDate());
            i++;
        }
        
        JTable tableScore = new JTable(data,columnNames);
        tableScore.setEnabled(false);
        tableScore.setCellSelectionEnabled(true);
       
        // taille des colonnes
        tableScore.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableScore.getColumnModel().getColumn(1).setPreferredWidth(50);
        tableScore.getColumnModel().getColumn(2).setPreferredWidth(120);
        
        
        JLabel lTitreForm = new JLabel("Les "+MeilleursScores.NOMBRE_MAX_SCORES+" Meilleurs scores [Terrain : "+nomTerrain+"]");
        lTitreForm.setFont(POLICE_TITRE);
        getContentPane().add(lTitreForm,BorderLayout.NORTH);
        getContentPane().add(tableScore,BorderLayout.CENTER);
        getContentPane().add(bFermer,BorderLayout.SOUTH);
        
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
