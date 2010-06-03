package vues;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import outils.myTimer;
import models.jeu.Jeu;

// TODO comment
public class Panel_InfosJoueurEtPartie extends JPanel
{
    // constantes finales
    private static final long serialVersionUID      = 1L;
    private static final ImageIcon I_PIECES         = new ImageIcon("img/icones/coins.png");
    private static final ImageIcon I_VIES           = new ImageIcon("img/icones/heart.png");
    private static final ImageIcon I_ETOILE         = new ImageIcon("img/icones/star.png");
    private static final ImageIcon I_TEMPS          = new ImageIcon("img/icones/time.png");
    private static final ImageIcon I_SCORE          = new ImageIcon("img/icones/cup.png");
    private static final ImageIcon I_REVENU         = new ImageIcon("img/icones/income.png");

    private JLabel lTimer                   = new JLabel();
    private JLabel lTitreTimer              = new JLabel(I_TEMPS);
    private JLabel lScore                   = new JLabel();
    private JLabel lTitreScore              = new JLabel(I_SCORE);
    private JLabel lVies                    = new JLabel();
    private JLabel lTitreVies               = new JLabel(I_VIES);
    private JLabel lNbPiecesOr              = new JLabel();
    private JLabel lTitrePiecesOr           = new JLabel(I_PIECES);
    private JLabel lEtoiles                 = new JLabel();
    private JLabel lTitreEtoiles            = new JLabel(I_ETOILE);
    private JLabel lRevenu                  = new JLabel();
    private JLabel lTitreRevenu             = new JLabel(I_REVENU);
    
    // autres membres
    private Jeu jeu;
    
    public Panel_InfosJoueurEtPartie(Jeu jeu, final myTimer timer)
    {
        this.jeu = jeu;
        
        setBackground(LookInterface.COULEUR_DE_FOND);
        
        timer.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                lTimer.setText(timer.toString());
            }
        });
        
        JPanel pGlobalInfo = new JPanel();
        pGlobalInfo.setOpaque(false);
        
        //timer
        pGlobalInfo.add(lTitreTimer);
        pGlobalInfo.add(lTimer);
        lTimer.setText("00.00.00");
        lTimer.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
            
        // etoiles gagnées
        pGlobalInfo.add(lTitreEtoiles);
        pGlobalInfo.add(lEtoiles);
        lEtoiles.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        miseAJourNbEtoiles();
        
        // revenu
        pGlobalInfo.add(lTitreRevenu);
        pGlobalInfo.add(lRevenu);
        lRevenu.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        miseAJourRevenu();
        
        //------------------------------------------
        //-- panel des donnees du joueur          --
        //-- (score, nb pieces or, vies restante) --
        //------------------------------------------
        JPanel pJoueur = new JPanel();
        pJoueur.setOpaque(false);
        
        // score
        pJoueur.add(lTitreScore);
        lTitreScore.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        pJoueur.add(lScore);
        lScore.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        miseAJourScore();
        
        // pieces d'or
        pJoueur.add(lTitrePiecesOr);
        pJoueur.add(lNbPiecesOr);
        lNbPiecesOr.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        miseAJourNbPiecesOr();
        
        // vies restantes
        pJoueur.add(lTitreVies);
        pJoueur.add(lVies);
        lVies.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        miseAJourNbViesRestantes();
            
        JPanel pToursEtJoueur = new JPanel(new BorderLayout());
        pToursEtJoueur.setOpaque(false);

        JPanel pAlignADroite = new JPanel(new BorderLayout());
        pAlignADroite.setOpaque(false);
        pAlignADroite.add(pGlobalInfo,BorderLayout.EAST);
        pToursEtJoueur.add(pAlignADroite,BorderLayout.NORTH);
        
        JPanel pAlignADroite2 = new JPanel(new BorderLayout());
        pAlignADroite2.setOpaque(false);
        pAlignADroite2.add(pJoueur,BorderLayout.EAST);
        pToursEtJoueur.add(pAlignADroite2,BorderLayout.CENTER);
        
        add(pToursEtJoueur,BorderLayout.NORTH); 
    }
    
    /**
     * Permet de demander une mise a jour du nombre de vies restantes du joueur
     */
    private void miseAJourNbViesRestantes()
    {
        lVies.setText(String.format("%02d",jeu.getJoueurPrincipal().getEquipe().getNbViesRestantes()));
    }
    
    /**
     * Permet de demander une mise a jour du nombre de vies restantes du joueur
     */
    private void miseAJourRevenu()
    {
        lRevenu.setText(String.format("%02.2f",jeu.getJoueurPrincipal().getRevenu()));
    }

    /**
     * Permet de demander une mise a jour du nombre d'étoiles gagnées
     */
    private void miseAJourNbEtoiles()
    {
        lEtoiles.setText(String.format("%02d",jeu.getJoueurPrincipal().getNbEtoiles()));
    }
    
    /**
     * Permet de demander une mise a jour du score du joueur
     */
    private void miseAJourScore()
    {
        lScore.setText(String.format("%06d",jeu.getJoueurPrincipal().getScore()));
        miseAJourNbEtoiles();
    }
    
    /**
     * Permet de demander une mise a jour du nombre de pieces du joueur
     */
    private void miseAJourNbPiecesOr()
    {
        int nbPiecesOr = jeu.getJoueurPrincipal().getNbPiecesDOr();
        lNbPiecesOr.setText(String.format("%04d",nbPiecesOr));
    }
    
    public void miseAJour()
    {
        miseAJourNbViesRestantes();
        miseAJourScore();
        miseAJourNbPiecesOr();
        miseAJourNbEtoiles(); 
        miseAJourRevenu();
    }
}
