package vues;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import exceptions.AucunePlaceDisponibleException;
import exceptions.JeuEnCoursException;
import models.jeu.Jeu_Client;
import models.jeu.Jeu_Serveur;
import models.jeu.ModeDeJeu;
import models.joueurs.EmplacementJoueur;
import models.joueurs.Equipe;
import models.joueurs.Joueur;

@SuppressWarnings("serial")
public class Panel_AttendreJoueurs extends JPanel implements ActionListener
{
    private final int MARGES_PANEL = 40;
    private final boolean ADMIN;
    private JFrame parent;
    private JButton bDemarrerMaintenant = new JButton("Démarrer maintenant");
    private JLabel lblEtat = new JLabel();
    private JButton bDeconnecter = new JButton("Se Deconnecter");

    
    private JButton bTmpJConn = new JButton("...Un joueur se connect");
    
    private Panel_GridBag pJoueurs;
    private Jeu_Serveur jeuServeur;
    private Jeu_Client jeuClient;
    private Panel_EmplacementsTerrain pEmplacementsTerrain;
    private JLabel lEtat = new JLabel();
    private JLabel lIPs = new JLabel();
    private Joueur joueur;
    private int nbJoueurs = 0;
    
    /**
     * Constructeur de créateur de partie
     * 
     * @param parent la fenetre parent
     * @param jeu le jeu
     * @param joueur le joueur
     */
    public Panel_AttendreJoueurs(JFrame parent, Jeu_Serveur jeuServeur, Jeu_Client jeuClient, Joueur joueur)
    {
        this.parent     = parent;
        this.ADMIN      = true;
        this.jeuServeur = jeuServeur;
        this.jeuClient  = jeuClient;
        this.joueur     = joueur;

        initialiserForm();
    }

    /**
     * Constructeur du joueur qui rejoint
     * 
     * @param parent la fenetre parent
     * @param jeu le jeu du client
     * @param joueur le joueur
     */
    public Panel_AttendreJoueurs(JFrame parent, Jeu_Client jeu, Joueur joueur)
    {
        this.parent     = parent;
        this.ADMIN      = false;
        this.jeuClient  = jeu;
        this.joueur     = joueur;
        
        initialiserForm();
    }

    /**
     * Permet d'initialiser le formulaire
     */
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
        if (jeuServeur != null)
        {
            contruireEmplacementsJoueur();

            JPanel pCenter = new JPanel(new BorderLayout());
            pCenter.setOpaque(false);

            pEmplacementsTerrain = new Panel_EmplacementsTerrain(jeuServeur.getTerrain(),300,300);
           
            
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
            pTmp.add(bTmpJConn, BorderLayout.SOUTH);
            bTmpJConn.addActionListener(this);
            
            
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
            GestionnaireDesPolices.setStyle(bDemarrerMaintenant);
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
            e.printStackTrace();
        }
        lIPs.setText(s);
        pBottom.add(lIPs, BorderLayout.CENTER);
        
        
        bDeconnecter.addActionListener(this);
        bDeconnecter.setPreferredSize(new Dimension(120, 50));
        GestionnaireDesPolices.setStyle(bDeconnecter);
        pBottom.add(bDeconnecter, BorderLayout.WEST);

        if (ADMIN)
            if (jeuServeur.getEnregistrementReussie())
            {
                lblEtat.setForeground(GestionnaireDesPolices.COULEUR_SUCCES);
                lblEtat.setText("La connexion avec le serveur central à réussie"); 
            } 
            else
            {
                lblEtat.setForeground(GestionnaireDesPolices.COULEUR_INFO);
                lblEtat.setText("La connexion avec le serveur central à échouée, "+
                                "votre serveur n'apparaitra pas dans la liste " +
                               	"des serveurs");
            }

        pBottom.add(lblEtat, BorderLayout.SOUTH);

        add(pBottom, BorderLayout.SOUTH);
    }

    /**
     * Permte de remplir la combobox
     * 
     * @param cbEmplacements la combobox
     * @param joueur pour pré-sélection
     */
    private void remplirCombo(JComboBox cbEmplacements, Joueur joueur)
    {
        synchronized(cbEmplacements)
        {  
            // vidage
            cbEmplacements.removeAllItems();

            // Emplacements de l'equipe
            Equipe equipe = joueur.getEquipe();
            for (int j = 0; j < equipe.getEmplacementsJoueur().size(); j++)
            {
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
                jeuServeur.desenregistrerSurSE();
            
            
            
            // FIXME INFO VENANT DU SERVEUR
            //this.joueur = new Joueur("toto");
            joueur.setEquipe(new Equipe("ahah",Color.BLACK));
            joueur.setEmplacementJoueur(new EmplacementJoueur(new Rectangle(0,0,200,200)));

            jeuClient.initialiser(joueur);
            jeuServeur.initialiser(joueur);
            
           
            
            
            
            switch(jeuServeur.getTerrain().getMode())
            {
                case ModeDeJeu.MODE_VERSUS :
                    new Fenetre_JeuVersus(jeuClient);
                    break;
                
                case ModeDeJeu.MODE_COOP :
                    new Fenetre_JeuVersus(jeuClient); // FIXME
                    break;
            }
            parent.dispose();
            
        }
        else if (src == bDeconnecter)
        {
            if (ADMIN)
            {
                jeuServeur.desenregistrerSurSE();
                jeuServeur.stopperServeurDeJeu();
            }

            // retour
            parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_MenuPrincipal(parent),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();
        }
        else if(src == bTmpJConn)
        {  
            Joueur j = new Joueur("J"+this.nbJoueurs);
            
            try
            {
                jeuServeur.ajouterJoueur(j);
            } 
            catch (JeuEnCoursException e1)
            {
                e1.printStackTrace();
            } 
            catch (AucunePlaceDisponibleException e1)
            {
                e1.printStackTrace();
            }
            
            ajouterJoueur(j);
            
            jeuServeur.miseAJourSE();
        }
    }

    private void ajouterJoueur(final Joueur joueur)
    {
        final JComboBox cbEmplacements = new JComboBox();
        final JComboBox cbEquipes = new JComboBox();

        // styles
        GestionnaireDesPolices.setStyle(cbEmplacements);
        GestionnaireDesPolices.setStyle(cbEquipes);
        
        final JLabel lPseudo = new JLabel(joueur.getPseudo());
        lPseudo.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        pJoueurs.add(lPseudo, 1, nbJoueurs, 1);
        lPseudo.setForeground(joueur.getEquipe().getCouleur());

        ArrayList<Equipe> equipes = jeuServeur.getEquipes();
        
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

            pJoueurs.add(cbEquipes, 2, nbJoueurs, 1);

            // remplissage de la combobox
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
            pJoueurs.add(cbEmplacements, 3, nbJoueurs, 1);
            
            nbJoueurs++;
        }
    }
    
    public void contruireEmplacementsJoueur()
    {
        ArrayList<Joueur> joueurs = jeuServeur.getJoueurs();
        

        int maxJoueurs = jeuServeur.getTerrain().getNbJoueursMax();

        pJoueurs = new Panel_GridBag(new Insets(2, 2, 2, 2));
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
                // TODO
                ajouterJoueur(joueurs.get(i));
            } 
            else// personne
            {
                JLabel lInconnu = new JLabel("???");
                lInconnu.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
                pJoueurs.add(lInconnu, 1, i, 1);
            }
        }
    }
    
}
