package vues;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

import models.jeu.Jeu;

public class Panel_OptionsTerrain extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private JTextField tfNomTerrain = new JTextField();
    
    private JTextField tfLargeurM = new JTextField();
    private JTextField tfHauteurM = new JTextField();
    
    private JTextField tfLargeurT = new JTextField();
    private JTextField tfHauteurT = new JTextField();
    
    
    
    private JButton openButton = new JButton("Parcourir...");
    private JButton bCouleurDeFond = new JButton();
    private JButton bCouleurDesMurs = new JButton();
    
    private final JFileChooser fc = new JFileChooser();
    private Jeu jeu;
    
    private Panel_Table pForm = new Panel_Table(); 
    
    private Dimension dim = new Dimension(120,25);
    
    
    
    public Panel_OptionsTerrain(Jeu jeu)
    {
        this.jeu = jeu;
        
        int ligne = 0;
        
        // Nom du terrain
        pForm.add(new JLabel("Nom du terrain"),0,ligne);
        tfNomTerrain.setPreferredSize(dim);
        tfNomTerrain.setText(jeu.getTerrain().getNom());
        pForm.add(tfNomTerrain,1,ligne);
        ligne++;
        
        // Couleur de fond
        pForm.add(new JLabel("Couleur de Fond"),0,ligne);
        bCouleurDeFond.setPreferredSize(new Dimension(25,25));
        bCouleurDeFond.addActionListener(this);
        bCouleurDeFond.setBackground(jeu.getTerrain().getCouleurDeFond());
        pForm.add(bCouleurDeFond,1,ligne);
        ligne++;
        
        // Couleur des murs
        pForm.add(new JLabel("Couleur des murs"),0,ligne);
        bCouleurDesMurs.setPreferredSize(new Dimension(25,25));
        bCouleurDesMurs.addActionListener(this);
        bCouleurDesMurs.setBackground(jeu.getTerrain().getCouleurMurs());
        pForm.add(bCouleurDesMurs,1,ligne);
        ligne++;
        
        
        // Image de fond
        pForm.add(new JLabel("Image de fond"),0,ligne);
        openButton.setPreferredSize(dim);
        openButton.addActionListener(this);
        pForm.add(openButton,1,ligne);
        ligne++;
        
        // Taille Terrain
        pForm.add(new JLabel("Taille du terrain"),0,ligne);
        tfLargeurT.setText(Integer.toString(jeu.getTerrain().getLargeur()));
        tfHauteurT.setText(Integer.toString(jeu.getTerrain().getHauteur()));
        tfLargeurT.setPreferredSize(new Dimension(40,25));
        tfHauteurT.setPreferredSize(new Dimension(40,25));
        JPanel pTailleT = new JPanel();
        pTailleT.add(new JLabel("l:"));
        pTailleT.add(tfLargeurT);
        pTailleT.add(new JLabel("h:"));
        pTailleT.add(tfHauteurT);
        pForm.add(pTailleT,1,ligne);
        ligne++;
        
        // Taille Maillage
        pForm.add(new JLabel("Taille maillage"),0,ligne);
        tfLargeurM.setText(Integer.toString(jeu.getTerrain().getLargeur()));
        tfHauteurM.setText(Integer.toString(jeu.getTerrain().getHauteur()));
        tfLargeurM.setPreferredSize(new Dimension(40,25));
        tfHauteurM.setPreferredSize(new Dimension(40,25));
        JPanel pTailleM = new JPanel();
        pTailleM.add(new JLabel("l:"));
        pTailleM.add(tfLargeurM);
        pTailleM.add(new JLabel("h:"));
        pTailleM.add(tfHauteurM);
        pForm.add(pTailleM,1,ligne);
        ligne++;
        

        
        add(pForm);
        setPreferredSize(new Dimension(200,200));
    }
    
    public void actionPerformed(ActionEvent e) {
        //Handle open button action.
        if (e.getSource() == openButton) 
        {
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
               
                File file = fc.getSelectedFile();
                
                jeu.getTerrain().setImageDeFond(Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath()));
                
                //This is where a real application would open the file.
                System.out.println("Opening: " + file.getName() + ".\n");
            } 
            else {
                System.out.println("Open command cancelled by user.\n");
            }
       } 
       else if (e.getSource() == bCouleurDeFond)
       {
           Color couleur = JColorChooser.showDialog(null,
                   "Couleur de fond du terrain ",jeu.getTerrain().getCouleurDeFond());
             
           if(couleur != null)
           {
               jeu.getTerrain().setCouleurDeFond(couleur);
               bCouleurDeFond.setBackground(couleur);
           } 
       }
       else if (e.getSource() == bCouleurDesMurs)
       {
           Color couleur = JColorChooser.showDialog(null,
                   "Couleur de fond du terrain ",jeu.getTerrain().getCouleurDeFond());
             
           if(couleur != null)
           {
               jeu.getTerrain().setCouleurMurs(couleur);
               bCouleurDesMurs.setBackground(couleur);
           } 
       }
        
    }

    
}
