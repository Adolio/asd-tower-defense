package vues.editeurTerrain;

import i18n.Langue;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;
import vues.GestionnaireDesPolices;
import vues.commun.Panel_Table;
import models.jeu.Jeu;
import models.jeu.ModeDeJeu;
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
public class Panel_OptionsTerrain extends JPanel implements ActionListener, DocumentListener, ChangeListener
{
    private static final long serialVersionUID = 1L;
    private static final ImageIcon I_DEL = new ImageIcon("img/icones/delete.png");
    
    // ----------------------------
    // -- Elements du formulaire --
    // ----------------------------
    private JTextField tfDescription = new JTextField();
    private JTextField tfNbPiecesOrInit = new JTextField();
    private JTextField tfNbViesInit = new JTextField();
    private JComboBox cbModeDeJeu = new JComboBox(); 
    private JTextField tfLargeurM = new JTextField();
    private JTextField tfHauteurM = new JTextField();
    private JTextField tfLargeurT = new JTextField();
    private JTextField tfHauteurT = new JTextField(); 
    private JSlider sOpaciteMurs    = new JSlider(0,100);
    private JButton bImageDeFond    = new JButton(Langue.getTexte(Langue.ID_TXT_BTN_PARCOURIR)+"...");
    private  JButton bSupprImageDeFond = new JButton(I_DEL);
    private JButton bCouleurDeFond  = new JButton();
    private JButton bCouleurDesMurs = new JButton();
    private final JFileChooser fcImageDeFond = new JFileChooser();
    
    /**
     * Le jeu a editer
     */
    private Jeu jeu;
    
    /**
     * Formulaire (affichage clef / champ)
     */
    private Panel_Table pForm = new Panel_Table(new Insets(1,5,1,5)); 
    
    /**
     * Dimension des elements de droite
     */
    private Dimension dim = new Dimension(150,25);

    /**
     * Constructeur
     * 
     * @param jeu
     */
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
        cbModeDeJeu.addItem(ModeDeJeu.getNomMode(ModeDeJeu.MODE_SOLO));
        cbModeDeJeu.addItem(ModeDeJeu.getNomMode(ModeDeJeu.MODE_VERSUS));
        //cbModeDeJeu.addItem(ModeDeJeu.getNomMode(ModeDeJeu.MODE_COOP)); // TODO
        
        JLabel lMode = new JLabel(Langue.getTexte(Langue.ID_TXT_MODE));
        lMode.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
        pForm.add(lMode,0,ligne);
        cbModeDeJeu.setPreferredSize(dim);
        cbModeDeJeu.addActionListener(this);
        pForm.add(cbModeDeJeu,1,ligne);
        ligne++;
             
        // Taille du terrain
        tfLargeurT.setText(Integer.toString(jeu.getTerrain().getLargeur()));
        tfHauteurT.setText(Integer.toString(jeu.getTerrain().getHauteur()));
        tfLargeurT.setPreferredSize(dim);
        tfHauteurT.setPreferredSize(dim);
        tfLargeurT.getDocument().addDocumentListener(this);
        tfHauteurT.getDocument().addDocumentListener(this);
        
        // TODO translate
        JLabel lLargeur = new JLabel("Width");
        lLargeur.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
        pForm.add(lLargeur,0,ligne);
        pForm.add(tfLargeurT,1,ligne);
        ligne++;

        // TODO translate
        JLabel lHauteur = new JLabel("Height");
        lHauteur.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
        pForm.add(lHauteur,0,ligne);
        pForm.add(tfHauteurT,1,ligne);
        ligne++;
        
        
        // Description du terrain
        pForm.add(new JLabel(Langue.getTexte(Langue.ID_TXT_DESCRIPTION)),0,ligne);
        tfDescription.setPreferredSize(dim);
        tfDescription.setText(jeu.getTerrain().getBrefDescription());
        
        tfDescription.addActionListener(this);
        tfDescription.getDocument().addDocumentListener(this);
        pForm.add(tfDescription,1,ligne);
        ligne++;
        
        
        // Nombre de pieces d'or initiales
        pForm.add(new JLabel(Langue.getTexte(Langue.ID_TXT_NB_PIECES_OR_INIT)),0,ligne);
        tfNbPiecesOrInit.setPreferredSize(dim);
        tfNbPiecesOrInit.setText(jeu.getTerrain().getNbPiecesOrInitiales()+"");
        
        tfNbPiecesOrInit.addActionListener(this);
        tfNbPiecesOrInit.getDocument().addDocumentListener(this);
        pForm.add(tfNbPiecesOrInit,1,ligne);
        ligne++;

        
        // Nombre de vies initiales
        pForm.add(new JLabel(Langue.getTexte(Langue.ID_TXT_NB_VIES_INIT)),0,ligne);
        tfNbViesInit.setPreferredSize(dim);
        tfNbViesInit.setText(jeu.getTerrain().getNbViesInitiales()+"");
        
        tfNbViesInit.addActionListener(this);
        tfNbViesInit.getDocument().addDocumentListener(this);
        pForm.add(tfNbViesInit,1,ligne);
        ligne++;
        
        
        // Couleur de fond
        pForm.add(new JLabel(Langue.getTexte(Langue.ID_TXT_COULEUR_DE_FOND)),0,ligne);
        bCouleurDeFond.setPreferredSize(dim);
        bCouleurDeFond.addActionListener(this);
        bCouleurDeFond.setBackground(jeu.getTerrain().getCouleurDeFond());
        pForm.add(bCouleurDeFond,1,ligne);
        ligne++;
        
        
        // Couleur des murs
        pForm.add(new JLabel(Langue.getTexte(Langue.ID_TXT_COULEUR_MURS)),0,ligne);
        bCouleurDesMurs.setPreferredSize(dim);
        bCouleurDesMurs.addActionListener(this);
        bCouleurDesMurs.setBackground(jeu.getTerrain().getCouleurMurs());
        pForm.add(bCouleurDesMurs,1,ligne);
        ligne++;
        
        
        // Image de fond
        pForm.add(new JLabel(Langue.getTexte(Langue.ID_TXT_IMAGE_DE_FOND)),0,ligne);
        bImageDeFond.setPreferredSize(dim);
        bImageDeFond.addActionListener(this);
        bSupprImageDeFond.addActionListener(this);
        pForm.add(bImageDeFond,1,ligne);
        pForm.add(bSupprImageDeFond,2,ligne);
        ligne++;

        
        // Afficher les murs
        pForm.add(new JLabel(Langue.getTexte(Langue.ID_TXT_MURS_VISIBLES_PAR_DEF)),0,ligne);
        sOpaciteMurs.addChangeListener(this);
        sOpaciteMurs.setMajorTickSpacing(20);
        sOpaciteMurs.setMinorTickSpacing(10);
        sOpaciteMurs.setPaintTicks(true);
        sOpaciteMurs.setPaintLabels(true);
        sOpaciteMurs.setPreferredSize(new Dimension(dim.width,50));
        sOpaciteMurs.setValue((int)jeu.getTerrain().getOpaciteMurs()*100);
        pForm.add(sOpaciteMurs,1,ligne);
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
            jeu.getTerrain().setModeDeJeu(cbModeDeJeu.getSelectedIndex());
        else if (e.getSource() == bImageDeFond) 
            if (fcImageDeFond.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
            {
                File file = fcImageDeFond.getSelectedFile();
                jeu.getTerrain().setImageDeFond(Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath()));
            } 
       else if (e.getSource() == bSupprImageDeFond) 
           jeu.getTerrain().setImageDeFond(null); 
       else if (e.getSource() == bCouleurDeFond)
       {
           Color couleur = JColorChooser.showDialog(null,
                   Langue.getTexte(Langue.ID_TXT_COULEUR_DE_FOND),jeu.getTerrain().getCouleurDeFond());
             
           if(couleur != null)
           {
               jeu.getTerrain().setCouleurDeFond(couleur);
               bCouleurDeFond.setBackground(couleur);
           } 
       }
       else if (e.getSource() == bCouleurDesMurs)
       {
           Color couleur = JColorChooser.showDialog(null,
                   Langue.getTexte(Langue.ID_TXT_COULEUR_MURS),jeu.getTerrain().getCouleurDeFond());
             
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
        
        sOpaciteMurs.setValue((int)t.getOpaciteMurs()*100);
        
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

    @Override
    public void stateChanged(ChangeEvent arg0)
    {
        jeu.getTerrain().setOpaciteMurs((float)(sOpaciteMurs.getValue()/100.0));
    }
    
}
