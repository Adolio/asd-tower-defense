package vues;

import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import javax.swing.*;
import models.outils.*;

// TODO
public class Fenetre_MeilleursScores extends JFrame implements ActionListener
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
     * Constructeur de la fenetre des meilleurs scores d'un terrain
     * 
     * @param nomTerrain le nom du terrain dont on veut voir les meilleurs scores
     */
    public Fenetre_MeilleursScores(String nomTerrain)
    {
        super("Les 10 Meilleurs scores");
        setLayout(new BorderLayout());
        setResizable(false);
 
        
        int i = 0;
        String[][] data = new String[10][3];
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
        
        
        JLabel lTitreForm = new JLabel("Les 10 Meilleurs scores [Terrain : "+nomTerrain+"]");
        lTitreForm.setFont(POLICE_TITRE);
        getContentPane().add(lTitreForm,BorderLayout.NORTH);
        getContentPane().add(tableScore,BorderLayout.CENTER);
        getContentPane().add(bFermer,BorderLayout.SOUTH);
        
        bFermer.addActionListener(this);
        
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
    public void actionPerformed(ActionEvent e)
    {
        // un seul bouton -> un seul choix
        dispose();
    }
}
