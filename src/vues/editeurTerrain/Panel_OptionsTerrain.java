package vues.editeurTerrain;

import i18n.Langue;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import models.jeu.Jeu;
import models.jeu.ModeDeJeu;
import models.outils.GestionnaireSons;
import models.outils.Son;
import models.terrains.Terrain;
import vues.GestionnaireDesPolices;
import vues.commun.Panel_Table;

/**
 * Formlaire de choix des preferences du Terrain.
 * 
 * Utilisé dans la fenêtre d'édition de Terrain
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | juillet 2010
 * @since jdk1.6.0_16
 * @see Terrain
 */
public class Panel_OptionsTerrain extends JPanel implements ActionListener,
        DocumentListener, ChangeListener {
    private static final long serialVersionUID = 1L;
    private static final ImageIcon I_DEL = new ImageIcon(
            "img/icones/delete.png");
    private static final ImageIcon I_PLAY = new ImageIcon(
            "img/icones/control_play_blue.png");
    
    
    private static final Object EXTENTION_MP3 = ".mp3";
    private static final String DOSSIER_MUSIQUES = "snd";

    // ----------------------------
    // -- Elements du formulaire --
    // ----------------------------
    private JTextField tfBreveDescription = new JTextField();
    private JTextField tfNbPiecesOrInit = new JTextField();
    private JTextField tfNbViesInit = new JTextField();
    private JComboBox cbModeDeJeu = new JComboBox();
    private JTextField tfLargeurM = new JTextField();
    private JTextField tfHauteurM = new JTextField();
    private JTextField tfLargeurT = new JTextField();
    private JTextField tfHauteurT = new JTextField();
    private JSlider sOpaciteMurs = new JSlider(0, 100);
    private JButton bImageDeFond = new JButton(
            Langue.getTexte(Langue.ID_TXT_BTN_PARCOURIR) + "...");
    private JButton bSupprImageDeFond = new JButton(I_DEL);
    private JButton bCouleurDeFond = new JButton();
    private JButton bCouleurDesMurs = new JButton();
    private final JFileChooser fcImageDeFond = new JFileChooser();
    private JComboBox cbMusique = new JComboBox();
    private JButton bJouerMusique = new JButton(I_PLAY);
    
    private Border bordsParDefaut = tfNbViesInit.getBorder();
    
    /**
     * Le jeu a editer
     */
    private Jeu jeu;

    /**
     * Formulaire (affichage clef / champ)
     */
    private Panel_Table pForm = new Panel_Table(new Insets(1, 5, 1, 5));

    /**
     * Dimension des elements de droite
     */
    private Dimension dim = new Dimension(150, 25);
    
    /**
     * Le son courant (en lecture)
     */
    private Son sonCourant;


    /**
     * Constructeur
     * 
     * @param jeu
     */
    public Panel_OptionsTerrain(Jeu jeu) {
        this.jeu = jeu;
        setOpaque(false);

        // --------------------------------
        // -- CONSTRUCTION DU FORMULAIRE --
        // --------------------------------
        int ligne = 0;

        pForm.setOpaque(false);

        // mode de jeu
        cbModeDeJeu.addItem(ModeDeJeu.getNomMode(ModeDeJeu.MODE_SOLO));
        cbModeDeJeu.addItem(ModeDeJeu.getNomMode(ModeDeJeu.MODE_VERSUS));
        // TODO
        // cbModeDeJeu.addItem(ModeDeJeu.getNomMode(ModeDeJeu.MODE_COOP));
        

        JLabel lMode = new JLabel(Langue.getTexte(Langue.ID_TXT_MODE));
        lMode.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
        pForm.add(lMode, 0, ligne);
        cbModeDeJeu.setPreferredSize(dim);
        cbModeDeJeu.addActionListener(this);
        pForm.add(cbModeDeJeu, 1, ligne);
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
        pForm.add(lLargeur, 0, ligne);
        pForm.add(tfLargeurT, 1, ligne);
        ligne++;

        // TODO translate
        JLabel lHauteur = new JLabel("Height");
        lHauteur.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
        pForm.add(lHauteur, 0, ligne);
        pForm.add(tfHauteurT, 1, ligne);
        ligne++;

        // Description du terrain
        JLabel lDescription = new JLabel(
                Langue.getTexte(Langue.ID_TXT_DESCRIPTION));
        lDescription.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
        pForm.add(lDescription, 0, ligne);
        tfBreveDescription.setPreferredSize(dim);
        tfBreveDescription.setText(jeu.getTerrain().getBreveDescription());

        tfBreveDescription.addActionListener(this);
        tfBreveDescription.getDocument().addDocumentListener(this);
        pForm.add(tfBreveDescription, 1, ligne);
        ligne++;

        // Nombre de pieces d'or initiales
        JLabel lNbPiecesOrInit = new JLabel(
                Langue.getTexte(Langue.ID_TXT_NB_PIECES_OR_INIT));
        lNbPiecesOrInit.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
        pForm.add(lNbPiecesOrInit, 0, ligne);
        tfNbPiecesOrInit.setPreferredSize(dim);
        tfNbPiecesOrInit
                .setText(jeu.getTerrain().getNbPiecesOrInitiales() + "");

        tfNbPiecesOrInit.addActionListener(this);
        tfNbPiecesOrInit.getDocument().addDocumentListener(this);
        pForm.add(tfNbPiecesOrInit, 1, ligne);
        ligne++;

        // Nombre de vies initiales
        JLabel lNbViesInit = new JLabel(
                Langue.getTexte(Langue.ID_TXT_NB_VIES_INIT));
        lNbViesInit.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
        pForm.add(lNbViesInit, 0, ligne);
        tfNbViesInit.setPreferredSize(dim);
        tfNbViesInit.setText(jeu.getTerrain().getNbViesInitiales() + "");

        tfNbViesInit.addActionListener(this);
        tfNbViesInit.getDocument().addDocumentListener(this);
        pForm.add(tfNbViesInit, 1, ligne);
        ligne++;

        // Couleur de fond
        JLabel lCouleurDeFond = new JLabel(
                Langue.getTexte(Langue.ID_TXT_COULEUR_DE_FOND));
        lCouleurDeFond.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
        pForm.add(lCouleurDeFond, 0, ligne);
        bCouleurDeFond.setPreferredSize(dim);
        bCouleurDeFond.addActionListener(this);
        bCouleurDeFond.setBackground(jeu.getTerrain().getCouleurDeFond());
        pForm.add(bCouleurDeFond, 1, ligne);
        ligne++;

        // Image de fond
        JLabel lImageDeFond = new JLabel(
                Langue.getTexte(Langue.ID_TXT_IMAGE_DE_FOND));
        lImageDeFond.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
        pForm.add(lImageDeFond, 0, ligne);
        bImageDeFond.setPreferredSize(dim);
        bImageDeFond.addActionListener(this);
        bSupprImageDeFond.addActionListener(this);
        pForm.add(bImageDeFond, 1, ligne);
        pForm.add(bSupprImageDeFond, 2, ligne);
        ligne++;

        // Couleur des murs
        JLabel lCouleurDesMurs = new JLabel(
                Langue.getTexte(Langue.ID_TXT_COULEUR_MURS));
        lCouleurDesMurs.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
        pForm.add(lCouleurDesMurs, 0, ligne);
        bCouleurDesMurs.setPreferredSize(dim);
        bCouleurDesMurs.addActionListener(this);
        bCouleurDesMurs.setBackground(jeu.getTerrain().getCouleurMurs());
        pForm.add(bCouleurDesMurs, 1, ligne);
        ligne++;

        // Afficher les murs
        JLabel lAlphaMurs = new JLabel(
                Langue.getTexte(Langue.ID_TXT_MURS_VISIBLES_PAR_DEF));
        lAlphaMurs.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
        pForm.add(lAlphaMurs, 0, ligne);
        sOpaciteMurs.addChangeListener(this);
        sOpaciteMurs.setMajorTickSpacing(20);
        sOpaciteMurs.setMinorTickSpacing(10);
        sOpaciteMurs.setPaintTicks(true);
        sOpaciteMurs.setPaintLabels(true);
        sOpaciteMurs.setPreferredSize(new Dimension(dim.width, 50));
        sOpaciteMurs.setValue((int) jeu.getTerrain().getOpaciteMurs() * 100);
        pForm.add(sOpaciteMurs, 1, ligne);
        ligne++;

        // Musiques
        JLabel lMusique = new JLabel("Ambient Music");
        lMusique.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
        pForm.add(lMusique, 0, ligne);
        
        // importation des fichiers ou dossier selectionnes
        cbMusique.addItem("None");
        File[] fichiers = new File(DOSSIER_MUSIQUES).listFiles();
        for (int i = 0; i < fichiers.length; i++)
           importerRecucivement(fichiers[i]);
        
        cbMusique.setPreferredSize(dim);
        cbMusique.addActionListener(this);
        pForm.add(cbMusique, 1, ligne);

        
        
        bJouerMusique.addActionListener(this);
        pForm.add(bJouerMusique, 2, ligne);

        ligne++;

        // Taille Maillage
        /*
         * pForm.add(new JLabel("Taille maillage"),0,ligne);
         * tfLargeurM.setText(Integer.toString(jeu.getTerrain().getLargeur()));
         * tfHauteurM.setText(Integer.toString(jeu.getTerrain().getHauteur()));
         * tfLargeurM.setPreferredSize(new Dimension(40,25));
         * tfHauteurM.setPreferredSize(new Dimension(40,25)); JPanel pTailleM =
         * new JPanel(); pTailleM.add(new JLabel("l:"));
         * pTailleM.add(tfLargeurM); pTailleM.add(new JLabel("h:"));
         * pTailleM.add(tfHauteurM); pForm.add(pTailleM,1,ligne); ligne++;
         */

        add(pForm);
    }

    @SuppressWarnings("serial")
    private void importerRecucivement(File f) {
       
        if (f.isDirectory()) {
            // parcours recurcivement les sous répertoires
            File[] ssDossiers = f.listFiles();
            for (int i = 0; i < ssDossiers.length; i++)
               importerRecucivement(ssDossiers[i]);
         } else {
            String extension = "";
            int dotPos = f.getName().lastIndexOf(".");

            if (dotPos != -1) {
               extension = f.getName().substring(dotPos);

               if (extension.equals(EXTENTION_MP3))
               {
                   // classe anonyme pour surcharger la methode toString
                   // afin de n'afficher que le nom du fichier dans la liste
                   // deroulante...
                   cbMusique.addItem(new File(f.getPath())
                   {
                       public String toString()
                       {
                           return this.getName();
                       }
                   });
               }
            }
         } 
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == cbModeDeJeu)
            jeu.getTerrain().setModeDeJeu(cbModeDeJeu.getSelectedIndex());
        else if (src == bImageDeFond) {
            if (fcImageDeFond.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fcImageDeFond.getSelectedFile();
                jeu.getTerrain().setImageDeFond(
                        Toolkit.getDefaultToolkit().getImage(
                                file.getAbsolutePath()));
            }
        } else if (src == bSupprImageDeFond) {
            jeu.getTerrain().setImageDeFond(null);
        } else if (src == bCouleurDeFond) {
            Color couleur = JColorChooser.showDialog(null, Langue
                    .getTexte(Langue.ID_TXT_COULEUR_DE_FOND), jeu.getTerrain()
                    .getCouleurDeFond());

            if (couleur != null) {
                jeu.getTerrain().setCouleurDeFond(couleur);
                bCouleurDeFond.setBackground(couleur);
            }
        } else if (src == bCouleurDesMurs) {
            Color couleur = JColorChooser.showDialog(null, Langue
                    .getTexte(Langue.ID_TXT_COULEUR_MURS), jeu.getTerrain()
                    .getCouleurDeFond());

            if (couleur != null) {
                jeu.getTerrain().setCouleurMurs(couleur);
                bCouleurDesMurs.setBackground(couleur);
            }
        } else if(src == cbMusique) {
            if(cbMusique.getSelectedItem().equals("None")){
                jeu.getTerrain().setFichierMusiqueDAmbiance(null);
            }
            else
            {
               File fMusique = (File) cbMusique.getSelectedItem();
               
               if(fMusique.exists())
                   jeu.getTerrain().setFichierMusiqueDAmbiance(fMusique);
            }
        } else if(src == bJouerMusique) {
            
            if(sonCourant != null)
                sonCourant.arreter();
            
            if(!cbMusique.getSelectedItem().equals("None")){
                   
                sonCourant = new Son((File)cbMusique.getSelectedItem());
                GestionnaireSons.ajouterSon(sonCourant);
                sonCourant.lire();
            }
        }
    }

    public void miseAJour() {
        Terrain t = jeu.getTerrain();

        cbModeDeJeu.setSelectedIndex(t.getMode());

        tfBreveDescription.setText(t.getBreveDescription());
        tfNbPiecesOrInit.setText(t.getNbPiecesOrInitiales() + "");
        tfNbViesInit.setText(t.getNbViesInitiales() + "");

        bCouleurDeFond.setBackground(t.getCouleurDeFond());
        bCouleurDesMurs.setBackground(t.getCouleurMurs());

        sOpaciteMurs.setValue((int) t.getOpaciteMurs() * 100);

        tfLargeurT.setText(t.getLargeur() + "");
        tfHauteurT.setText(t.getHauteur() + "");

        // FIXME Maillage !
        tfLargeurM.setText(t.getLargeur() + "");
        tfHauteurM.setText(t.getHauteur() + "");
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        changementChamp(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        changementChamp(e);
    }

    public void changementChamp(DocumentEvent e) {
        if (e.getDocument() == tfBreveDescription.getDocument()) {
            jeu.getTerrain().setBreveDescription(tfBreveDescription.getText());
        } else if (e.getDocument() == tfNbViesInit.getDocument()) {
            try {
                int nbViesInitiales = Integer.parseInt(tfNbViesInit.getText());

                if (nbViesInitiales > 0) {
                    jeu.getTerrain().setNbViesInitiales(nbViesInitiales);
                    
                    //System.out.println(tfNbViesInit.getBorder());
                    tfNbViesInit.setBorder(bordsParDefaut);
                    
                } else
                    throw new Exception();

            } catch (Exception e1) {
                tfNbViesInit.setBorder(new LineBorder(Color.RED));
            }
        } else if (e.getDocument() == tfNbPiecesOrInit.getDocument()) {
            try {
                int piecesOr = Integer.parseInt(tfNbPiecesOrInit.getText());

                if (piecesOr > 0) {
                    jeu.getTerrain().setNbPiecesOrInitiales(piecesOr);
                    tfNbPiecesOrInit.setBorder(bordsParDefaut);
                } else
                    throw new Exception();

            } catch (Exception e1) {
                tfNbPiecesOrInit.setBorder(new LineBorder(Color.RED));
            }
        } else if (e.getDocument() == tfLargeurT.getDocument()) {
            try {
                int largeur = Integer.parseInt(tfLargeurT.getText());

                if (largeur > 0) {
                    jeu.getTerrain().setLargeur(largeur);
                    tfLargeurT.setBorder(bordsParDefaut);
                } else
                    throw new Exception();
            } catch (Exception e1) {
                tfLargeurT.setBorder(new LineBorder(Color.RED));
                /*
                 * SwingUtilities.invokeLater(new Runnable() { public void run()
                 * { tfLargeurT.setText(jeu.getTerrain().getLargeur()+""); } });
                 */
            }
        } else if (e.getDocument() == tfHauteurT.getDocument()) {
            try {
                int hauteur = Integer.parseInt(tfHauteurT.getText());

                if (hauteur > 0) {
                    jeu.getTerrain().setHauteur(hauteur);
                    tfHauteurT.setBorder(bordsParDefaut);
                } else
                    throw new Exception();
            } catch (Exception e1) {
                tfHauteurT.setBorder(new LineBorder(Color.RED));
                /*
                 * SwingUtilities.invokeLater(new Runnable() { public void run()
                 * { tfHauteurT.setText(jeu.getTerrain().getHauteur()+""); } });
                 */
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent arg0) {
        jeu.getTerrain().setOpaciteMurs(
                (float) (sOpaciteMurs.getValue() / 100.0));
    }

}
