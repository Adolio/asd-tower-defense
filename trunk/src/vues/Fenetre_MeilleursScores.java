package vues;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;

import models.outils.MeilleursScores;
import models.outils.Score;

public class Fenetre_MeilleursScores extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 1L;
    private MeilleursScores ms;
    private JButton bFermer = new JButton("Fermer");
    private static String[] columnNames = {
                                            "Nom du joueur",
                                            "Score",
                                            "Date"
                                           };
    
    
    public Fenetre_MeilleursScores(String nomTerrain)
    {
        super("Meilleurs scores");
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
        
        
        JLabel lTitreForm = new JLabel("Meilleurs scores ["+nomTerrain+"]");
        lTitreForm.setFont(new Font("",Font.BOLD,14));
        getContentPane().add(lTitreForm,BorderLayout.NORTH);
        getContentPane().add(tableScore,BorderLayout.CENTER);
        getContentPane().add(bFermer,BorderLayout.SOUTH);
        
        bFermer.addActionListener(this);
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    
    public void actionPerformed(ActionEvent e)
    {
       dispose();
    }
}
