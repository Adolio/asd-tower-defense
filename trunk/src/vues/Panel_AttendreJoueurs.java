package vues;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import models.jeu.Jeu;
import models.joueurs.EmplacementJoueur;
import models.joueurs.Equipe;
import models.joueurs.Joueur;
import reseau.Canal;
import serveur.enregistrement.RequeteEnregistrement;

@SuppressWarnings("serial")
public class Panel_AttendreJoueurs extends JPanel implements ActionListener
{
    private final int MARGES_PANEL = 40;
    private final boolean ADMIN;
    private JFrame parent;
    private JButton bDemarrerMaintenant = new JButton("Démarrer maintenant");
    private JLabel lblEtat = new JLabel();
    private JButton bDeconnecter = new JButton("Se Deconnecter");

    private Jeu jeu;
    private Canal canalServeurEnregistrement;
    private Canal canalServeurJeu;
    private Panel_EmplacementsTerrain pEmplacementsTerrain;
    private JLabel lEtat = new JLabel();
    private JLabel lIPs = new JLabel();
    private Joueur joueur;
    
    public Panel_AttendreJoueurs(JFrame parent, Canal canal, boolean admin, Joueur joueur)
    {
        this.parent = parent;
        this.ADMIN = admin;
        this.joueur = joueur;
        
        if (admin)
            this.canalServeurEnregistrement = canal;
        else
            this.canalServeurJeu = canal;

        initialiserForm();
    }

    public Panel_AttendreJoueurs(JFrame parent, Jeu jeu, Joueur joueur)
    {
        this.parent = parent;
        this.ADMIN = true;
        this.jeu = jeu;
        this.joueur = joueur;

        initialiserForm();
    }

    private void initialiserForm()
    {
        // initialisation
        setLayout(new BorderLayout());

        parent.setTitle("Attendre des joueurs");
        setBorder(new EmptyBorder(new Insets(MARGES_PANEL, MARGES_PANEL,
                MARGES_PANEL, MARGES_PANEL)));
        setBackground(LookInterface.COULEUR_DE_FOND);

        // ---------
        // -- TOP --
        // ---------
        JPanel pTop = new JPanel(new BorderLayout());
        pTop.setOpaque(false);

        JLabel lblTitre = new JLabel("ATTENTE DE JOUEURS...");
        lblTitre.setFont(GestionnaireDesPolices.POLICE_TITRE);
        pTop.add(lblTitre, BorderLayout.NORTH);

        add(pTop, BorderLayout.NORTH);

        // ------------
        // -- CENTER --
        // ------------
        if (jeu != null)
        {
            ArrayList<Joueur> joueurs = jeu.getJoueurs();
            ArrayList<Equipe> equipes = jeu.getEquipes();

            int maxJoueurs = jeu.getTerrain().getNbJoueursMax();

            Panel_GridBag pJoueurs = new Panel_GridBag(new Insets(2, 2, 2, 2));
            pJoueurs.setBackground(LookInterface.COULEUR_DE_FOND);
            pJoueurs.setPreferredSize(new Dimension(350, 150));

            for (int i = 0; i < maxJoueurs; i++)
            {

                /*JLabel lNo = new JLabel((i + 1) + ". ");
                lNo.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
                pJoueurs.add(lNo, 0, i, 1);*/

                // joueur trouvé
                if (i < joueurs.size())
                {
                    final Joueur joueur = joueurs.get(i);
                    final Equipe equipe = joueur.getEquipe();
                    final JComboBox cbEmplacements = new JComboBox();
                    final JComboBox cbEquipes = new JComboBox();

                    final JLabel lPseudo = new JLabel(joueur.getPseudo());
                    lPseudo.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
                    pJoueurs.add(lPseudo, 1, i, 1);
                    lPseudo.setForeground(joueur.getEquipe().getCouleur());

                    if(ADMIN || this.joueur == joueur)
                    {
                        // Liste des équipes
      
                        // Remplissage
                        for (int j = 0; j < equipes.size(); j++)
                        {
                            Equipe tmpEquipe = equipes.get(j);
    
                            // ajout de l'equipe
                            cbEquipes.addItem(tmpEquipe);
    
                            if (joueur.getEquipe() == tmpEquipe)
                                cbEquipes.setSelectedIndex(j);
                        }
    
                        // Action de la liste des équipes
                        cbEquipes.addActionListener(new ActionListener()
                        {
                            @Override
                            public void actionPerformed(ActionEvent e)
                            {
                                try
                                {
                                    lEtat.setText("");
                                    
                                    EmplacementJoueur old = joueur.getEmplacement();
                                    
                                    Equipe equipe = (Equipe) cbEquipes
                                            .getSelectedItem();
                                    
                                    equipe.ajouterJoueur(joueur);
                                    
                                    // mise a jour de la liste des emplacements
                                    remplirCombo(cbEmplacements,joueur);
    
                                    lPseudo.setForeground(joueur.getEquipe().getCouleur());
                                } 
                                catch (IllegalArgumentException iae)
                                {
                                    // on reselectionne l'ancienne sélection
                                    cbEquipes.setSelectedItem(joueur.getEquipe());
     
                                    lEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
                                    lEtat.setText(iae.getMessage());
                                }
    
                                pEmplacementsTerrain.repaint();
                            }
                        });
    
                        pJoueurs.add(cbEquipes, 2, i, 1);
    
                        // TODO
                        remplirCombo(cbEmplacements,joueur);
    
                        // Action de la liste des emplacements
                        cbEmplacements.addActionListener(new ActionListener()
                        {
                            
    
                            @Override
                            public void actionPerformed(ActionEvent e)
                            {
                                try
                                {
                                    lEtat.setText("");
                                    
                                    if(cbEmplacements.getSelectedIndex() != -1)
                                    {
                                        EmplacementJoueur ej = joueur.getEquipe().getEmplacementsJoueur().get(cbEmplacements.getSelectedIndex());
                                        
                                        joueur.setEmplacementJoueur(ej);
                                        pEmplacementsTerrain.repaint();
                                    }
                                } 
                                catch (IllegalArgumentException iae)
                                {
                                    // on reselectionne l'ancienne sélection
                                    for(int i=0;i<cbEmplacements.getItemCount();i++)
                                        if(joueur.getEmplacement().toString().equals(cbEmplacements.getItemAt(i)))
                                            cbEmplacements.setSelectedIndex(i);
                                    
                                    lEtat.setForeground(GestionnaireDesPolices.COULEUR_ERREUR);
                                    lEtat.setText(iae.getMessage());
                                }
                            }
                        });
    
                        // ajout de l'emplacement
                        pJoueurs.add(cbEmplacements, 3, i, 1);
                    }
                } 
                else// personne
                {
                    JLabel lInconnu = new JLabel("???");
                    lInconnu.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
                    pJoueurs.add(lInconnu, 1, i, 1);
                }
            }

            JPanel pCenter = new JPanel(new BorderLayout());
            pCenter.setOpaque(false);

            pEmplacementsTerrain = new Panel_EmplacementsTerrain(jeu.getTerrain());
           
            
            JScrollPane spEmplacement = new JScrollPane(pEmplacementsTerrain);
            spEmplacement.setOpaque(false);
            spEmplacement.setBorder(null);

            //spEmplacement.setPreferredSize(new Dimension(350, 350));
            pCenter.add(spEmplacement, BorderLayout.EAST);

            
            JPanel pJoueursEtat = new JPanel(new BorderLayout());
            pJoueursEtat.setOpaque(false);
            pJoueursEtat.add(lEtat, BorderLayout.CENTER);
            
     
            
            JPanel pTmp = new JPanel(new BorderLayout());
            pTmp.setOpaque(false);

            JScrollPane js = new JScrollPane(pJoueurs);
            js.setOpaque(false);
            js.setBorder(null);

            pTmp.add(js, BorderLayout.NORTH);
            
            pJoueursEtat.add(pTmp, BorderLayout.NORTH);
            
            pCenter.add(pJoueursEtat, BorderLayout.WEST);

            add(pCenter, BorderLayout.CENTER);
        }

        // ------------
        // -- BOTTOM --
        // ------------
        JPanel pBottom = new JPanel(new BorderLayout());
        pBottom.setOpaque(false);

        // bouton démarrer
        if (ADMIN)
        {
            bDemarrerMaintenant.setPreferredSize(new Dimension(100, 50));
            pBottom.add(bDemarrerMaintenant, BorderLayout.EAST);
            bDemarrerMaintenant.addActionListener(this);
        }

        String s = "Vos IPs : ";
 
        try
        {
            for (NetworkInterface netint : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress inetAddress : Collections.list(netint.getInetAddresses())) {
                   if (!inetAddress.toString().contains(":") && !inetAddress.toString().contains("127.0.0.1"))
                   {
                      s += "[" + inetAddress.toString().substring(1) + "] ou ";
                   }
                }
             }
        } 
        catch (SocketException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        lIPs.setText(s);
        pBottom.add(lIPs, BorderLayout.CENTER);
        
        
        bDeconnecter.addActionListener(this);
        pBottom.add(bDeconnecter, BorderLayout.WEST);

        if (ADMIN)
            if (canalServeurEnregistrement == null)
            {
                lblEtat.setForeground(GestionnaireDesPolices.COULEUR_INFO);
                lblEtat
                        .setText("La connexion avec le serveur central à échouée, "
                                + "votre serveur n'apparaitra pas dans la liste des serveurs");
            } else
            {
                lblEtat.setForeground(GestionnaireDesPolices.COULEUR_SUCCES);
                lblEtat
                        .setText("La connexion avec le serveur central à réussie");
            }

        pBottom.add(lblEtat, BorderLayout.SOUTH);

        add(pBottom, BorderLayout.SOUTH);
    }

    private void remplirCombo(JComboBox cbEmplacements, Joueur joueur)
    {

        synchronized(cbEmplacements)
        {
            
            // vidage
            while(cbEmplacements.getItemCount() > 0)
            {
                System.out.println("**"+cbEmplacements.getItemCount());
                cbEmplacements.removeItemAt(0); 
            }
            
            System.out.println("**"+joueur.getEmplacement());
            
            // Emplacements de l'equipe
            Equipe equipe = joueur.getEquipe();
            
            
            for (int j = 0; j < equipe.getEmplacementsJoueur().size(); j++)
            {
                System.out.println(joueur.getEmplacement());
                
                EmplacementJoueur ej = equipe.getEmplacementsJoueur().get(j);
    
                cbEmplacements.addItem(ej.toString());
    
                if (joueur.getEmplacement() == ej)
                    cbEmplacements.setSelectedIndex(j);
            }
            
            // si seulement 1 zone, on efface le combobox
            cbEmplacements.setVisible(cbEmplacements.getItemCount() > 1);
            
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();

        if (src == bDemarrerMaintenant)
        {
            if (ADMIN)
            {
                if (canalServeurEnregistrement != null)
                {
                    canalServeurEnregistrement
                            .envoyerString(RequeteEnregistrement.DESENREGISTRER);
                    canalServeurEnregistrement
                            .envoyerString(RequeteEnregistrement.STOP);
                }
            }

            // parent.getContentPane().removeAll();
            // parent.getContentPane().add(new Panel_JeuMulti(parent, new
            // Jeu(),new Joueur(new Equipe()), BorderLayout.CENTER);
            // parent.getContentPane().validate();
        } else if (src == bDeconnecter)
        {
            if (ADMIN)
            {
                if (canalServeurEnregistrement != null)
                {
                    canalServeurEnregistrement
                            .envoyerString(RequeteEnregistrement.DESENREGISTRER);
                    canalServeurEnregistrement
                            .envoyerString(RequeteEnregistrement.STOP);
                }
            } else if (canalServeurJeu != null)
            {
                canalServeurJeu.envoyerString(RequeteEnregistrement.STOP);
            }

            parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_MenuPrincipal(parent),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();
        }
    }
}
