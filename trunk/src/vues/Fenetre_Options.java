package vues;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import outils.Configuration;

public class Fenetre_Options extends JFrame implements ActionListener
{
    private static class Panel_OptionsReseau extends JPanel implements ActionListener
    {
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
        onglets.add("Jeu", panelOptionsJeu);
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
