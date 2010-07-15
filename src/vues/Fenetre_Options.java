package vues;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import outils.Configuration;

public class Fenetre_Options extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private static class Panel_OptionsReseau extends JPanel implements ActionListener
    {
        private static final long serialVersionUID = 1L;
        private Panel_Table pFormulaire = new Panel_Table();
        private JTextField tfIP_SE = new JTextField(Configuration.getIpSE());
        
        public Panel_OptionsReseau()
        {
            setBackground(LookInterface.COULEUR_DE_FOND);
            
            pFormulaire.setOpaque(false);
            pFormulaire.add(new JLabel("IP Serveur d'enregistrement :"),0,0);
            
            tfIP_SE.setPreferredSize(new Dimension(100,25));
            pFormulaire.add(tfIP_SE,1,0);
           
            add(pFormulaire); 
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            // TODO controle !!!!
            
            Configuration.setIpSE(tfIP_SE.getText());
        }
    }
    
    
    private static class Panel_OptionsJeu extends JPanel implements ActionListener
    {
        private static final long serialVersionUID = 1L;
        private Panel_Table pFormulaire = new Panel_Table();
        private JTextField tfPseudoJoueur = new JTextField(Configuration.getPseudoJoueur());
       
        public Panel_OptionsJeu()
        {
            setBackground(LookInterface.COULEUR_DE_FOND);
            
            pFormulaire.setOpaque(false);
            pFormulaire.add(new JLabel("Pseudo"),0,0);
            
            tfPseudoJoueur.setPreferredSize(new Dimension(100,25));
            pFormulaire.add(tfPseudoJoueur,1,0);
            
            add(pFormulaire); 
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            // TODO controle !!!!
            
            Configuration.setPseudoJoueur(tfPseudoJoueur.getText());
        }
    }
    
    
    private static class Panel_OptionsCommandes extends JPanel implements ActionListener
    {
        private static final long serialVersionUID = 1L;
        private static final Dimension TAILLE_TF = new Dimension(30,30);
        
        private Panel_Table pFormulaire = new Panel_Table();
        
        private JLabel lDeplHaut = new JLabel((Configuration.getDeplHaut()+"").toUpperCase());
        private JLabel lDeplBas = new JLabel((Configuration.getDeplBas()+"").toUpperCase());
        private JLabel lDeplDroite = new JLabel((Configuration.getDeplDroite()+"").toUpperCase());
        private JLabel lDeplGauche = new JLabel((Configuration.getDeplGauche()+"").toUpperCase());
        private JLabel lVendre = new JLabel((Configuration.getCmdVendre()+"").toUpperCase());
        private JLabel lAmeliorer = new JLabel((Configuration.getCmdAmeliorer()+"").toUpperCase());
        private JLabel lPause = new JLabel("P");
        private JLabel lSuivre = new JLabel("F");

        public Panel_OptionsCommandes()
        {
            setBackground(LookInterface.COULEUR_DE_FOND);
            
            int i=0;   
            pFormulaire.setOpaque(false);
            pFormulaire.add(new JLabel("Depl. haut"),0,i++);
            pFormulaire.add(new JLabel("Depl. droite"),0,i++);
            pFormulaire.add(new JLabel("Depl. bas"),0,i++);
            pFormulaire.add(new JLabel("Depl. gauche"),0,i++);
            pFormulaire.add(new JLabel("Améliorer Tour"),0,i++);
            pFormulaire.add(new JLabel("Vendre Tour"),0,i++);
            pFormulaire.add(new JLabel("Pause"),0,i++);
            pFormulaire.add(new JLabel("Suivre Créature"),0,i++);
            
            i = 0;
            pFormulaire.add(lDeplHaut,1,i++);
            pFormulaire.add(lDeplDroite,1,i++);
            pFormulaire.add(lDeplBas,1,i++);
            pFormulaire.add(lDeplGauche,1,i++);
            pFormulaire.add(lAmeliorer,1,i++);
            pFormulaire.add(lVendre,1,i++);
            pFormulaire.add(lPause,1,i++);
            pFormulaire.add(lSuivre,1,i++);
            
            lDeplHaut.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
            lDeplBas.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
            lDeplDroite.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
            lDeplGauche.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
            lVendre.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
            lAmeliorer.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
            lPause.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
            lSuivre.setFont(GestionnaireDesPolices.POLICE_TITRE_CHAMP);
            
            /*
            tfDeplHaut.setPreferredSize(TAILLE_TF);
            tfDeplBas.setPreferredSize(TAILLE_TF);
            tfDeplDroite.setPreferredSize(TAILLE_TF);
            tfDeplGauche.setPreferredSize(TAILLE_TF);
            tfVendre.setPreferredSize(TAILLE_TF);
            tfAmeliorer.setPreferredSize(TAILLE_TF);
            */
            
            add(pFormulaire); 
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            // TODO controle !!!!
            
            Configuration.setPseudoJoueur(lDeplHaut.getText());
        }
    }
    
    
    
    
    private static final ImageIcon I_FENETRE = new ImageIcon(
    "img/icones/wrench.png");
    
    JTabbedPane onglets;
    
    private JButton bValider = new JButton("Valider");
    private JButton bFermer = new JButton("Fermer");
    
    
    
    public Fenetre_Options()
    {
        super("Options");
        setIconImage(I_FENETRE.getImage());
        setLayout(new BorderLayout());
        setBackground(LookInterface.COULEUR_DE_FOND);
        
        
        // titre
        JPanel pTop = new JPanel(new BorderLayout());
        pTop.setBackground(LookInterface.COULEUR_DE_FOND_2);
        pTop.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitre = new JLabel("OPTIONS");
        lblTitre.setFont(GestionnaireDesPolices.POLICE_TITRE);
        
        pTop.add(lblTitre, BorderLayout.NORTH);
        add(pTop, BorderLayout.NORTH);
        
        // onglets
        
        // Background
        onglets = new JTabbedPane();
        
        
        
        UIManager.put("TabbedPane.tabAreaBackground", LookInterface.COULEUR_DE_FOND);
        //SwingUtilities.updateComponentTreeUI(onglets);
        
        onglets.setOpaque(true);
        onglets.setBackground(LookInterface.COULEUR_DE_FOND_2);
        Panel_OptionsJeu panelOptionsJeu = new Panel_OptionsJeu(); 
        Panel_OptionsReseau panelOptionsReseau = new Panel_OptionsReseau();
        Panel_OptionsCommandes panelOptionsCommandes = new Panel_OptionsCommandes();
        onglets.add("Jeu", panelOptionsJeu);
        onglets.add("Commandes", new JScrollPane(panelOptionsCommandes));
        onglets.add("Réseau", panelOptionsReseau);
        add(onglets,BorderLayout.CENTER);
        
        // boutons
        bValider.addActionListener(panelOptionsJeu);
        bValider.addActionListener(panelOptionsReseau);
        bFermer.addActionListener(this);
        
        JPanel pBoutons = new JPanel();
        pBoutons.setBackground(LookInterface.COULEUR_DE_FOND_2);
        
        //GestionnaireDesPolices.setStyle(bValider);
        //GestionnaireDesPolices.setStyle(bFermer);
        
        pBoutons.add(bValider);
        pBoutons.add(bFermer);
        add(pBoutons,BorderLayout.SOUTH);
        
        
        setBounds(0, 0, 400, 300);
        setLocationRelativeTo(null);
        setVisible(true);  
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        dispose();
    }
}
