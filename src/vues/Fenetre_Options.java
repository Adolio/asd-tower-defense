package vues;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;

import models.outils.GestionnaireSons;

import outils.Configuration;
import vues.commun.Panel_Table;

public class Fenetre_Options extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private static class Panel_OptionsReseau extends JPanel
    {
        private static final long serialVersionUID = 1L;
        private Panel_Table pFormulaire = new Panel_Table();
        private JTextField tfIP_SE = new JTextField(Configuration.getIpSE());
        
        public Panel_OptionsReseau()
        {
            setBackground(LookInterface.COULEUR_DE_FOND_PRI);
            
            pFormulaire.setOpaque(false);
            pFormulaire.add(new JLabel("IP Serveur d'enregistrement :"),0,0);
            
            tfIP_SE.setPreferredSize(new Dimension(100,25));
            pFormulaire.add(tfIP_SE,1,0);
           
            // TODO check
            tfIP_SE.getDocument().addDocumentListener(new DocumentListener()
            { 
                @Override
                public void removeUpdate(DocumentEvent arg0)
                {
                    Configuration.setIpSE(tfIP_SE.getText());
                }
                
                @Override
                public void insertUpdate(DocumentEvent arg0)
                {
                    Configuration.setIpSE(tfIP_SE.getText());
                }
                
                @Override
                public void changedUpdate(DocumentEvent arg0)
                {
                    Configuration.setIpSE(tfIP_SE.getText());
                }
            });
            
            add(pFormulaire); 
        }
    }
    
    
    private static class Panel_OptionsJeu extends JPanel
    {
        private static final long serialVersionUID = 1L;
        private Panel_Table pFormulaire = new Panel_Table();
        private JTextField tfPseudoJoueur = new JTextField(Configuration.getPseudoJoueur());
       
        public Panel_OptionsJeu()
        {
            setBackground(LookInterface.COULEUR_DE_FOND_PRI);
            
            pFormulaire.setOpaque(false);
            pFormulaire.add(new JLabel("Pseudo"),0,0);
            
            tfPseudoJoueur.setPreferredSize(new Dimension(100,25));
            pFormulaire.add(tfPseudoJoueur,1,0);
            
            tfPseudoJoueur.getDocument().addDocumentListener(new DocumentListener()
            { 
                @Override
                public void removeUpdate(DocumentEvent arg0)
                {
                    Configuration.setPseudoJoueur(tfPseudoJoueur.getText());
                }
                
                @Override
                public void insertUpdate(DocumentEvent arg0)
                {
                    Configuration.setPseudoJoueur(tfPseudoJoueur.getText());
                }
                
                @Override
                public void changedUpdate(DocumentEvent arg0)
                {
                    Configuration.setPseudoJoueur(tfPseudoJoueur.getText());
                }
            });
            
            
            add(pFormulaire); 
        }
    }
    
    
    private static class Panel_OptionsCommandes extends JPanel 
                                                implements 
                                                ActionListener, 
                                                KeyListener
    {
        
        private static class BoutonKeyCode extends JButton
        {
            private static final long serialVersionUID = 1L;
            private int keyCode;
            private final String PROPRIETE;
            
            
            public BoutonKeyCode(String propriete) 
            {   
                PROPRIETE = propriete;
                
                
                int keyCode2 = Integer.parseInt(Configuration.getProprety(PROPRIETE));
                
                setKeyCode(keyCode2);
            }
             
            public void setKeyCode(int keyCode) 
            {
                this.keyCode = keyCode;
                
                Configuration.setProperty(PROPRIETE,keyCode+"");
                
                super.setText(KeyEvent.getKeyText(keyCode));
            }
            
            public int getKeyCode()
            {
                return keyCode;
            }  
        }
        
        
        private static final long serialVersionUID = 1L;
        private Panel_Table pFormulaire = new Panel_Table();
        
        private BoutonKeyCode lDeplHaut = new BoutonKeyCode(Configuration.DEPL_HAUT);
        private BoutonKeyCode lDeplBas = new BoutonKeyCode(Configuration.DEPL_BAS);
        private BoutonKeyCode lDeplDroite = new BoutonKeyCode(Configuration.DEPL_DROITE);
        private BoutonKeyCode lDeplGauche = new BoutonKeyCode(Configuration.DEPL_GAUCHE);
        private BoutonKeyCode lLancerVagueSuivante = new BoutonKeyCode(Configuration.LANCER_VAGUE);
        private BoutonKeyCode lVendre = new BoutonKeyCode(Configuration.VENDRE_TOUR);
        private BoutonKeyCode lAmeliorer = new BoutonKeyCode(Configuration.AMELIO_TOUR);
        private BoutonKeyCode lPause = new BoutonKeyCode(Configuration.PAUSE);
        private BoutonKeyCode lSuivre = new BoutonKeyCode(Configuration.SUIVRE_CREATURE);
        private BoutonKeyCode lAugmenterVitesseJeu = new BoutonKeyCode(Configuration.AUG_VIT_JEU);
        private BoutonKeyCode lDiminuerVitesseJeu = new BoutonKeyCode(Configuration.DIM_VIT_JEU);
        
        private JLabel lZoom = new JLabel("Roulette");
        private ArrayList<BoutonKeyCode> boutons = new ArrayList<BoutonKeyCode>();
        private boolean attenteTouche;
        private BoutonKeyCode boutonCourant;
       
        public Panel_OptionsCommandes()
        {  
            setBackground(LookInterface.COULEUR_DE_FOND_PRI);
            
            boutons.add(lDeplHaut);
            boutons.add(lDeplGauche);
            boutons.add(lDeplBas);
            boutons.add(lDeplDroite);
            boutons.add(lLancerVagueSuivante);
            boutons.add(lVendre);
            boutons.add(lAmeliorer);
            boutons.add(lPause);
            boutons.add(lSuivre);
            boutons.add(lAugmenterVitesseJeu);
            boutons.add(lDiminuerVitesseJeu);
            
            for(JButton b : boutons)
            {
                b.addActionListener(this);
                b.addKeyListener(this);
                // désactive l'autovalidation par la touche ESPACE
                b.getInputMap().put(KeyStroke.getKeyStroke("SPACE"),
                "doNothing");
            }
            
            int i=0;  
            pFormulaire.setOpaque(false);
            pFormulaire.add(new JLabel("Déplacement haut"),0,i++);
            pFormulaire.add(new JLabel("Déplacement gauche"),0,i++);
            pFormulaire.add(new JLabel("Déplacement bas"),0,i++);
            pFormulaire.add(new JLabel("Déplacement droite"),0,i++);
            pFormulaire.add(new JLabel("Lancer la vague suivante"),0,i++);
            pFormulaire.add(new JLabel("Améliorer la tour sélectionnée"),0,i++);
            pFormulaire.add(new JLabel("Vendre la tour sélectionnée"),0,i++);
            pFormulaire.add(new JLabel("Mettre le jeu en pause"),0,i++);
            pFormulaire.add(new JLabel("Suivre la créature sélectionnée"),0,i++);
            pFormulaire.add(new JLabel("Augmenter la vitesse du jeu"),0,i++);
            pFormulaire.add(new JLabel("Diminuer la vitesse du jeu"),0,i++);
            pFormulaire.add(new JLabel("Zoom"),0,i++);
            
            
            i = 0;
            pFormulaire.add(lDeplHaut,1,i++);
            pFormulaire.add(lDeplGauche,1,i++);
            pFormulaire.add(lDeplBas,1,i++);
            pFormulaire.add(lDeplDroite,1,i++);
            pFormulaire.add(lLancerVagueSuivante,1,i++);
            pFormulaire.add(lAmeliorer,1,i++);
            pFormulaire.add(lVendre,1,i++);
            pFormulaire.add(lPause,1,i++);
            pFormulaire.add(lSuivre,1,i++);
            pFormulaire.add(lAugmenterVitesseJeu,1,i++);
            pFormulaire.add(lDiminuerVitesseJeu,1,i++);
            pFormulaire.add(lZoom,1,i++);
            
            // Style
            for(BoutonKeyCode b : boutons)
                b.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
            
            lZoom.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
            
            
            add(pFormulaire); 
            
            pFormulaire.setFocusable(true);
            pFormulaire.addKeyListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            attenteTouche = true;
            boutonCourant = (BoutonKeyCode) e.getSource();
              
            // désactivation
            for(BoutonKeyCode b : boutons)
                if(b != boutonCourant)
                    b.setEnabled(false);
        }

        @Override
        public void keyPressed(KeyEvent e){}

        @Override
        public void keyReleased(KeyEvent e)
        {
            if(attenteTouche)
            {
                for(BoutonKeyCode b : boutons)
                {
                    b.setEnabled(true);
                    
                    if(b.getKeyCode() == e.getKeyCode())
                        b.setKeyCode(boutonCourant.getKeyCode());
                }
                
                boutonCourant.setKeyCode(e.getKeyCode());
                
                attenteTouche = false;
            }
        }

        @Override
        public void keyTyped(KeyEvent e){}
    }
 
    
    private static class Panel_OptionsSon extends JPanel implements ActionListener, ChangeListener
    {
        private static final long serialVersionUID = 1L;
        private Panel_Table pFormulaire = new Panel_Table();
        private JButton bSonActif = new JButton("oui");
        private JSlider sVolumeSon = new JSlider(0,100); // %
        
        public Panel_OptionsSon()
        {
            setBackground(LookInterface.COULEUR_DE_FOND_PRI);
            
            pFormulaire.setOpaque(false);
            
            pFormulaire.add(new JLabel("Actif ?"),0,0);
            pFormulaire.add(bSonActif,1,0);
            
            sVolumeSon.setValue(GestionnaireSons.getVolumeSysteme());
            pFormulaire.add(new JLabel("Volume"),0,1);
            pFormulaire.add(sVolumeSon,1,1);

            
            bSonActif.addActionListener(this);
            sVolumeSon.addChangeListener(this);
            
            if(GestionnaireSons.isVolumeMute())
                bSonActif.setText("non");
            else
                bSonActif.setText("oui");

            add(pFormulaire); 
        }

        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            if(GestionnaireSons.isVolumeMute())
            {
                bSonActif.setText("oui");
                GestionnaireSons.setVolumeMute(false);
            }
            else
            {
                bSonActif.setText("non");
                GestionnaireSons.setVolumeMute(true);
            }
        }

        @Override
        public void stateChanged(ChangeEvent arg0)
        {
            GestionnaireSons.setVolumeSysteme(sVolumeSon.getValue());
        }
    }
    
    private static class Panel_OptionsStyle extends JPanel implements ActionListener
    {
        private static final long serialVersionUID = 1L;
        private Panel_Table pFormulaire = new Panel_Table();
        private JButton bCouleurDeFond_Pri = new JButton();
        private JButton bCouleurTexte_Pri = new JButton();
        
        private JButton bCouleurDeFond_Sec = new JButton();
        private JButton bCouleurTexte_Sec = new JButton();
        
        private JButton bCouleurDeFond_Boutons = new JButton();
        private JButton bCouleurTexte_Boutons = new JButton();
        
        private JButton bReinitialiser = new JButton("Réinitialiser");
        
        
        public Panel_OptionsStyle()
        {
            setBackground(LookInterface.COULEUR_DE_FOND_PRI);
            
            int ln = 0;
            
            pFormulaire.setOpaque(false);
            
            bCouleurDeFond_Pri.setPreferredSize(new Dimension(50,50));
            bCouleurDeFond_Pri.setBackground(LookInterface.COULEUR_DE_FOND_PRI);
            pFormulaire.add(new JLabel("Couleur de fond primaire"),0,ln);
            pFormulaire.add(bCouleurDeFond_Pri,1,ln++);

            bCouleurTexte_Pri.setPreferredSize(new Dimension(50,50));
            bCouleurTexte_Pri.setBackground(LookInterface.COULEUR_TEXTE_PRI);
            pFormulaire.add(new JLabel("Couleur du texte primaire"),0,ln);
            pFormulaire.add(bCouleurTexte_Pri,1,ln++);
            
            bCouleurDeFond_Sec.setPreferredSize(new Dimension(50,50));
            bCouleurDeFond_Sec.setBackground(LookInterface.COULEUR_DE_FOND_SEC);
            pFormulaire.add(new JLabel("Couleur de fond secondaire"),0,ln);
            pFormulaire.add(bCouleurDeFond_Sec,1,ln++);
  
            bCouleurTexte_Sec.setPreferredSize(new Dimension(50,50));
            bCouleurTexte_Sec.setBackground(LookInterface.COULEUR_TEXTE_SEC);
            pFormulaire.add(new JLabel("Couleur du texte secondaire"),0,ln);
            pFormulaire.add(bCouleurTexte_Sec,1,ln++);
            
            bCouleurDeFond_Boutons.setPreferredSize(new Dimension(50,50));
            bCouleurDeFond_Boutons.setBackground(LookInterface.COULEUR_DE_FOND_BTN);
            pFormulaire.add(new JLabel("Couleur de fond des boutons"),0,ln);
            pFormulaire.add(bCouleurDeFond_Boutons,1,ln++);

            bCouleurTexte_Boutons.setPreferredSize(new Dimension(50,50));
            bCouleurTexte_Boutons.setBackground(LookInterface.COULEUR_TEXTE_BTN);
            pFormulaire.add(new JLabel("Couleur du texte des boutons"),0,ln);
            pFormulaire.add(bCouleurTexte_Boutons,1,ln++);
            
            
            
            
            pFormulaire.add(bReinitialiser,1,ln++);
            
            bCouleurDeFond_Pri.addActionListener(this);
            bCouleurTexte_Pri.addActionListener(this);
            bCouleurDeFond_Sec.addActionListener(this);
            bCouleurTexte_Sec.addActionListener(this);
            bCouleurDeFond_Boutons.addActionListener(this);
            bCouleurTexte_Boutons.addActionListener(this);
            bReinitialiser.addActionListener(this);
            
            add(pFormulaire); 
        }

        @Override
        public void actionPerformed(ActionEvent ae)
        {
            Object src = ae.getSource();
            
            if(src == bCouleurDeFond_Pri)
            {
                Color couleur = JColorChooser.showDialog(null,
                        "Couleur de fond primaire",LookInterface.COULEUR_DE_FOND_PRI);
                  
                if(couleur != null)
                {
                    LookInterface.COULEUR_DE_FOND_PRI = couleur;
                    bCouleurDeFond_Pri.setBackground(couleur);
                    Configuration.setProperty(Configuration.COULEUR_DE_FOND_P, couleur.getRGB()+"");
                }
            }
            else if(src == bCouleurTexte_Pri)
            {
                Color couleur = JColorChooser.showDialog(null,
                        "Couleur texte primaire",LookInterface.COULEUR_TEXTE_PRI);
                  
                if(couleur != null)
                {
                    LookInterface.COULEUR_TEXTE_PRI = couleur;
                    bCouleurTexte_Pri.setBackground(couleur);
                    Configuration.setProperty(Configuration.COULEUR_TEXTE_P, couleur.getRGB()+"");
                }
            }   
            else if(src == bCouleurDeFond_Sec)
            {
                Color couleur = JColorChooser.showDialog(null,
                        "Couleur de fond secondaire",LookInterface.COULEUR_DE_FOND_SEC);
                  
                if(couleur != null)
                {
                    LookInterface.COULEUR_DE_FOND_SEC = couleur;
                    bCouleurDeFond_Sec.setBackground(couleur);
                    Configuration.setProperty(Configuration.COULEUR_DE_FOND_S, couleur.getRGB()+"");
                }
            }
            else if(src == bCouleurTexte_Sec)
            {
                Color couleur = JColorChooser.showDialog(null,
                        "Couleur texte secondaire",LookInterface.COULEUR_TEXTE_SEC);
                  
                if(couleur != null)
                {
                    LookInterface.COULEUR_TEXTE_SEC = couleur;
                    bCouleurTexte_Sec.setBackground(couleur);
                    Configuration.setProperty(Configuration.COULEUR_TEXTE_S, couleur.getRGB()+"");
                }
            }
            else if(src == bCouleurDeFond_Boutons)
            {
                Color couleur = JColorChooser.showDialog(null,
                        "Couleur de fond des boutons",LookInterface.COULEUR_DE_FOND_BTN);
                  
                if(couleur != null)
                {
                    LookInterface.COULEUR_DE_FOND_BTN = couleur;
                    bCouleurDeFond_Boutons.setBackground(couleur);
                    Configuration.setProperty(Configuration.COULEUR_DE_FOND_B, couleur.getRGB()+"");
                }
            } 
            else if(src == bCouleurTexte_Boutons)
            {
                Color couleur = JColorChooser.showDialog(null,
                        "Couleur du texte des boutons",LookInterface.COULEUR_TEXTE_BTN);
                  
                if(couleur != null)
                {
                    LookInterface.COULEUR_TEXTE_BTN = couleur;
                    bCouleurTexte_Boutons.setBackground(couleur);
                    Configuration.setProperty(Configuration.COULEUR_TEXTE_B, couleur.getRGB()+"");
                } 
            }
            else if(src == bReinitialiser)
            {
                // COULEUR_DE_FOND_PRI
                LookInterface.COULEUR_DE_FOND_PRI = LookInterface.DEF_COULEUR_DE_FOND_PRI;
                bCouleurDeFond_Pri.setBackground(LookInterface.COULEUR_DE_FOND_PRI);
                Configuration.setProperty(Configuration.COULEUR_DE_FOND_P, LookInterface.COULEUR_DE_FOND_PRI.getRGB()+"");
                
                // COULEUR_TEXTE_PRI
                LookInterface.COULEUR_TEXTE_PRI = LookInterface.DEF_COULEUR_TEXTE_PRI;
                bCouleurTexte_Pri.setBackground(LookInterface.COULEUR_TEXTE_PRI);
                Configuration.setProperty(Configuration.COULEUR_TEXTE_P, LookInterface.COULEUR_TEXTE_PRI.getRGB()+"");   
             
                // COULEUR_DE_FOND_SEC
                LookInterface.COULEUR_DE_FOND_SEC = LookInterface.DEF_COULEUR_DE_FOND_SEC;
                bCouleurDeFond_Sec.setBackground(LookInterface.COULEUR_DE_FOND_SEC);
                Configuration.setProperty(Configuration.COULEUR_DE_FOND_S, LookInterface.COULEUR_DE_FOND_SEC.getRGB()+"");
                
                // COULEUR_TEXTE_SEC
                LookInterface.COULEUR_TEXTE_SEC = LookInterface.DEF_COULEUR_TEXTE_SEC;
                bCouleurTexte_Sec.setBackground(LookInterface.COULEUR_TEXTE_SEC);
                Configuration.setProperty(Configuration.COULEUR_TEXTE_S, LookInterface.COULEUR_TEXTE_SEC.getRGB()+"");   

                // COULEUR_DE_FOND_BTN
                LookInterface.COULEUR_DE_FOND_BTN = LookInterface.DEF_COULEUR_DE_FOND_BTN;
                bCouleurDeFond_Boutons.setBackground(LookInterface.COULEUR_DE_FOND_BTN);
                Configuration.setProperty(Configuration.COULEUR_DE_FOND_B, LookInterface.COULEUR_DE_FOND_BTN.getRGB()+"");  
            
                // COULEUR_TEXTE_BTN
                LookInterface.COULEUR_TEXTE_BTN = LookInterface.DEF_COULEUR_TEXTE_BTN;
                bCouleurTexte_Boutons.setBackground(LookInterface.COULEUR_TEXTE_BTN);
                Configuration.setProperty(Configuration.COULEUR_TEXTE_B, LookInterface.COULEUR_TEXTE_BTN.getRGB()+"");      
            }  
        }
    }
    
    
    private static final ImageIcon I_FENETRE = new ImageIcon("img/icones/wrench.png");
    private static final ImageIcon I_JOUEUR = new ImageIcon("img/icones/user_red.png");
    private static final ImageIcon I_CMD = new ImageIcon("img/icones/keyboard.png");
    private static final ImageIcon I_SON = new ImageIcon("img/icones/sound.png");
    private static final ImageIcon I_RESEAU = new ImageIcon("img/icones/connect.png");
    private static final ImageIcon I_STYLE = new ImageIcon("img/icones/palette.png");
    
    private JTabbedPane onglets;
    private JButton bFermer = new JButton("Fermer");

    public Fenetre_Options()
    {
        super("Options");
        setIconImage(I_FENETRE.getImage());
        setLayout(new BorderLayout());
        setBackground(LookInterface.COULEUR_DE_FOND_PRI);
        
        // titre
        JPanel pTop = new JPanel(new BorderLayout());
        pTop.setBackground(LookInterface.COULEUR_DE_FOND_SEC);
        pTop.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitre = new JLabel("OPTIONS");
        lblTitre.setForeground(LookInterface.COULEUR_TEXTE_PRI);
        lblTitre.setFont(GestionnaireDesPolices.POLICE_TITRE);
        
        pTop.add(lblTitre, BorderLayout.NORTH);
        add(pTop, BorderLayout.NORTH);
        
        // onglets
        
        // Background
        onglets = new JTabbedPane();
 
        UIManager.put("TabbedPane.tabAreaBackground", LookInterface.COULEUR_DE_FOND_PRI);
        //SwingUtilities.updateComponentTreeUI(onglets);
        
        onglets.setFocusable(false); // pour keylistener dans option commande
        onglets.setOpaque(true);
        onglets.setBackground(LookInterface.COULEUR_DE_FOND_SEC);
        Panel_OptionsJeu panelOptionsJeu = new Panel_OptionsJeu(); 
        Panel_OptionsReseau panelOptionsReseau = new Panel_OptionsReseau();
        Panel_OptionsCommandes panelOptionsCommandes = new Panel_OptionsCommandes();
        Panel_OptionsSon panelOptionsSon = new Panel_OptionsSon();
        Panel_OptionsStyle panelOptionsStyle = new Panel_OptionsStyle();
        
        onglets.addTab("Joueur  ", I_JOUEUR, panelOptionsJeu);
        onglets.addTab("Commandes  ", I_CMD, new JScrollPane(panelOptionsCommandes));
        onglets.addTab("Son  ", I_SON, panelOptionsSon);
        onglets.addTab("Réseau  ", I_RESEAU, panelOptionsReseau);
        onglets.addTab("Style  ", I_STYLE, new JScrollPane(panelOptionsStyle));
        
        
        add(onglets,BorderLayout.CENTER);
        
        // boutons
        bFermer.addActionListener(this);
        
        JPanel pBoutons = new JPanel();
        pBoutons.setBackground(LookInterface.COULEUR_DE_FOND_SEC);
        
        //pBoutons.add(bValider);
        pBoutons.add(bFermer);
        add(pBoutons,BorderLayout.SOUTH);
        
        setBounds(0, 0, 400, 500);
        setLocationRelativeTo(null);
        setVisible(true);  
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        dispose();
    }
}
