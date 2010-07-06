package vues;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import models.jeu.Jeu;
import models.terrains.Terrain;

/**
 * Formlaire de choix des préférences du Terrain.
 * 
 * Utilisé dans la fenêtre d'édition de Terrain
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | juillet 2010
 * @since jdk1.6.0_16
 * @see Terrain
 */
public class Panel_OptionsTerrain extends JPanel implements ActionListener, DocumentListener
{
    private static final long serialVersionUID = 1L;
    
    private JTextField tfDescription = new JTextField();
    
    private JTextField tfNbPiecesOrInit = new JTextField();
    private JTextField tfNbViesInit = new JTextField();

    private JComboBox cbModeDeJeu = new JComboBox();
    
    private JTextField tfLargeurM = new JTextField();
    private JTextField tfHauteurM = new JTextField();
    
    private JTextField tfLargeurT = new JTextField();
    private JTextField tfHauteurT = new JTextField();
    
    private JCheckBox cbAfficherMurs = new JCheckBox();
    
    private JButton bImageDeFond    = new JButton("Parcourir...");
    private JButton bCouleurDeFond  = new JButton();
    private JButton bCouleurDesMurs = new JButton();
    
    private final JFileChooser fcImageDeFond = new JFileChooser();
    private Jeu jeu;
    
    private Panel_Table pForm = new Panel_Table(); 
    
    private Dimension dim = new Dimension(120,25);
    
    
    
    public Panel_OptionsTerrain(Jeu jeu)
    {
        this.jeu = jeu;
        setOpaque(false);

        //--------------------------------
        //-- CONSTRUCTION DU FORMULAIRE --
        //--------------------------------
        int ligne = 0;
        
        pForm.setOpaque(false);
        
        // mode de jeu
        cbModeDeJeu.addItem("Solo");
        cbModeDeJeu.addItem("Versus");
        //cbModeDeJeu.addItem(ModeDeJeu.MODE_COOP); // TODO
        pForm.add(new JLabel("Mode de jeu"),0,ligne);
        cbModeDeJeu.setPreferredSize(dim);
        cbModeDeJeu.addActionListener(this);
        pForm.add(cbModeDeJeu,1,ligne);
        ligne++;
             
        // Taille Terrain
        pForm.add(new JLabel("Taille du terrain"),0,ligne);
        tfLargeurT.setText(Integer.toString(jeu.getTerrain().getLargeur()));
        tfHauteurT.setText(Integer.toString(jeu.getTerrain().getHauteur()));
        tfLargeurT.setPreferredSize(new Dimension(40,25));
        tfHauteurT.setPreferredSize(new Dimension(40,25));
        
        tfLargeurT.getDocument().addDocumentListener(this);
        tfHauteurT.getDocument().addDocumentListener(this);
        
        JPanel pTailleT = new JPanel();
        pTailleT.setOpaque(false);
        pTailleT.add(new JLabel("l:"));
        pTailleT.add(tfLargeurT);
        pTailleT.add(new JLabel("h:"));
        pTailleT.add(tfHauteurT);
        pForm.add(pTailleT,1,ligne);
        ligne++;
        
        // Description du terrain
        pForm.add(new JLabel("Description"),0,ligne);
        tfDescription.setPreferredSize(dim);
        tfDescription.setText(jeu.getTerrain().getBrefDescription());
        
        tfDescription.addActionListener(this);
        tfDescription.getDocument().addDocumentListener(this);
        pForm.add(tfDescription,1,ligne);
        ligne++;
        
        // Nombre de pieces d'or initiales
        pForm.add(new JLabel("Nb pièces d'or init."),0,ligne);
        tfNbPiecesOrInit.setPreferredSize(dim);
        tfNbPiecesOrInit.setText(jeu.getTerrain().getNbPiecesOrInitiales()+"");
        
        tfNbPiecesOrInit.addActionListener(this);
        tfNbPiecesOrInit.getDocument().addDocumentListener(this);
        pForm.add(tfNbPiecesOrInit,1,ligne);
        ligne++;

        // Nombre de vies initiales
        pForm.add(new JLabel("Nb vies init."),0,ligne);
        tfNbViesInit.setPreferredSize(dim);
        tfNbViesInit.setText(jeu.getTerrain().getNbViesInitiales()+"");
        
        tfNbViesInit.addActionListener(this);
        tfNbViesInit.getDocument().addDocumentListener(this);
        pForm.add(tfNbViesInit,1,ligne);
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
        bImageDeFond.setPreferredSize(dim);
        bImageDeFond.addActionListener(this);
        pForm.add(bImageDeFond,1,ligne);
        ligne++;
          
        // Afficher les murs
        pForm.add(new JLabel("Murs visiblent par default"),0,ligne);
        cbAfficherMurs.addActionListener(this);
        pForm.add(cbAfficherMurs,1,ligne);
        ligne++;
        
        // Taille Maillage
        /*
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
        */

        add(pForm);
    }
    
    public void actionPerformed(ActionEvent e) 
    {

        if (e.getSource() == cbModeDeJeu) 
        {
            jeu.getTerrain().setModeDeJeu(cbModeDeJeu.getSelectedIndex());
        }
        else if (e.getSource() == bImageDeFond) 
        {
            if (fcImageDeFond.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
               
                File file = fcImageDeFond.getSelectedFile();
                
                jeu.getTerrain().setImageDeFond(Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath()));
                
                //This is where a real application would open the file.
                System.out.println("Opening: " + file.getName() + ".\n");
            } 
            else {
                System.out.println("Open command cancelled by user.\n");
            }
       } 
       else if (e.getSource() == cbAfficherMurs) 
       {
           jeu.getTerrain().setAfficherMurs(cbAfficherMurs.isSelected());
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

    public void miseAJour()
    {
        Terrain t = jeu.getTerrain();
         
        cbModeDeJeu.setSelectedIndex(t.getMode());
        
        tfDescription.setText(t.getBrefDescription());
        tfNbPiecesOrInit.setText(t.getNbPiecesOrInitiales()+"");
        tfNbViesInit.setText(t.getNbViesInitiales()+"");
        
        bCouleurDeFond.setBackground(t.getCouleurDeFond());
        bCouleurDesMurs.setBackground(t.getCouleurMurs());
        
        cbAfficherMurs.setSelected(t.getAfficherMurs());
        
        tfLargeurT.setText(t.getLargeur()+"");
        tfHauteurT.setText(t.getHauteur()+"");
        
        // FIXME Maillage !
        tfLargeurM.setText(t.getLargeur()+"");
        tfHauteurM.setText(t.getHauteur()+""); 
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {}

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        changementChamp(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        changementChamp(e);
    }
    
    public void changementChamp(DocumentEvent e)
    {
        if(e.getDocument() == tfDescription.getDocument())
        {
            jeu.getTerrain().setBrefDescription(tfDescription.getText());
        }
        else if(e.getDocument() == tfNbViesInit.getDocument())
        {
            try
            {
                int nbViesInitiales = Integer.parseInt(tfNbViesInit.getText());
                
                if(nbViesInitiales > 0)
                {
                    jeu.getTerrain().setNbViesInitiales(nbViesInitiales);
                    tfNbViesInit.setBorder(new LineBorder(Color.BLACK));
                }
                else
                    throw new Exception();
                
            }
            catch(Exception e1)
            {
                tfNbViesInit.setBorder(new LineBorder(Color.RED));
            }
        }
        else if(e.getDocument() == tfNbPiecesOrInit.getDocument())
        {
            try
            {
                int piecesOr = Integer.parseInt(tfNbPiecesOrInit.getText());
                
                if(piecesOr > 0)
                {
                    jeu.getTerrain().setNbPiecesOrInitiales(piecesOr);
                    tfNbPiecesOrInit.setBorder(new LineBorder(Color.BLACK));
                }
                else
                    throw new Exception();
                
            }
            catch(Exception e1)
            {
                tfNbPiecesOrInit.setBorder(new LineBorder(Color.RED));
            }
        }
        else if(e.getDocument() == tfLargeurT.getDocument())
        {
            try
            {
                int largeur = Integer.parseInt(tfLargeurT.getText());
                
                if(largeur > 0)   
                {
                    jeu.getTerrain().setLargeur(largeur);
                    tfLargeurT.setBorder(new LineBorder(Color.BLACK));
                }
                else
                    throw new Exception();
            }
            catch(Exception e1)
            {
                tfLargeurT.setBorder(new LineBorder(Color.RED));
                /*SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        tfLargeurT.setText(jeu.getTerrain().getLargeur()+"");
                    }
                });*/
            }
        }
        else if(e.getDocument() == tfHauteurT.getDocument())
        {
            try
            {
                int hauteur = Integer.parseInt(tfHauteurT.getText());
                
                if(hauteur > 0)  
                {
                    jeu.getTerrain().setHauteur(hauteur);
                    tfHauteurT.setBorder(new LineBorder(Color.BLACK));
                }
                else
                    throw new Exception();
            }
            catch(Exception e1)
            {
                tfHauteurT.setBorder(new LineBorder(Color.RED));
                /*
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        tfHauteurT.setText(jeu.getTerrain().getHauteur()+"");
                    }
                });
                */
            }
        }
    }
    
}
