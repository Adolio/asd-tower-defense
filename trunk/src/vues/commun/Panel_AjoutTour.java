package vues.commun;

import i18n.Langue;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import vues.GestionnaireDesPolices;
import vues.LookInterface;
import models.jeu.Jeu;
import models.tours.*;

/**
 * Panel de sélection d'une tour pour achat
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | juin 2010
 * @since jdk1.6.0_16
 */
public class Panel_AjoutTour extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    // membres graphiques
    private JButton bTourArcher             = new JButton(new ImageIcon(TourArcher.ICONE));
    private JButton bTourCanon              = new JButton(new ImageIcon(TourCanon.ICONE));
    private JButton bTourAntiAerienne       = new JButton(new ImageIcon(TourAntiAerienne.ICONE));
    private JButton bTourDeGlace            = new JButton(new ImageIcon(TourDeGlace.ICONE));
    private JButton bTourDeFeu              = new JButton(new ImageIcon(TourDeFeu.ICONE));
    private JButton bTourDAir               = new JButton(new ImageIcon(TourDAir.ICONE));
    private JButton bTourDeTerre            = new JButton(new ImageIcon(TourDeTerre.ICONE));
    private JButton bTourElectrique         = new JButton(new ImageIcon(TourElectrique.ICONE));
    private ArrayList<JButton> boutonsTours = new ArrayList<JButton>();
    private Jeu jeu;
    private EcouteurDePanelTerrain edpt;

    public Panel_AjoutTour(Jeu jeu, EcouteurDePanelTerrain edpt, int largeur, int hauteur)
    {  
        this.jeu = jeu;
        this.edpt = edpt;
        
        setBackground(LookInterface.COULEUR_DE_FOND_PRI);
        
        //---------------------
        //-- panel des tours --
        //---------------------
        JPanel pTours = new JPanel(new GridLayout(2,0));
        pTours.setOpaque(false);
        pTours.setPreferredSize(new Dimension(largeur,hauteur));
        
        
        String titrePrixAchat = Langue.getTexte(Langue.ID_TXT_PRIX_ACHAT);
        
        boutonsTours.add(bTourArcher);
        bTourArcher.setToolTipText(titrePrixAchat+" : "+TourArcher.PRIX_ACHAT);
        
        boutonsTours.add(bTourCanon);
        bTourCanon.setToolTipText(titrePrixAchat+" : "+TourCanon.PRIX_ACHAT);
        
        boutonsTours.add(bTourAntiAerienne);
        bTourAntiAerienne.setToolTipText(titrePrixAchat+" : "+TourAntiAerienne.PRIX_ACHAT);
        
        boutonsTours.add(bTourDeGlace);
        bTourDeGlace.setToolTipText(titrePrixAchat+" : "+TourDeGlace.PRIX_ACHAT);
        
        boutonsTours.add(bTourElectrique);
        bTourElectrique.setToolTipText(titrePrixAchat+" : "+TourElectrique.PRIX_ACHAT);
        
        boutonsTours.add(bTourDeFeu);
        bTourDeFeu.setToolTipText(titrePrixAchat+" : "+TourDeFeu.PRIX_ACHAT);
        
        boutonsTours.add(bTourDAir);
        bTourDAir.setToolTipText(titrePrixAchat+" : "+TourDAir.PRIX_ACHAT);
        
        boutonsTours.add(bTourDeTerre);
        bTourDeTerre.setToolTipText(titrePrixAchat+" : "+TourDeTerre.PRIX_ACHAT);
        
        for(JButton bTour : boutonsTours)
        {
            bTour.addActionListener(this);
            bTour.setBorder(new EmptyBorder(5,5,5,5));
            GestionnaireDesPolices.setStyle(bTour);
            pTours.add(bTour);
        }
        
        miseAJour();
        
        add(pTours,BorderLayout.CENTER);
    }

    /**
     * Gestion des événements des divers éléments du 
     * panel (menu, bouttons, etc.).
     * 
     * @param ae l'événement associé à une action
     */
    public void actionPerformed(ActionEvent ae)
    {
        Object source = ae.getSource();
        Tour tour = null;
        
        if(source == bTourArcher)
            tour = new TourArcher();
        else if(source == bTourCanon)
            tour = new TourCanon();
        else if(source == bTourAntiAerienne)
            tour = new TourAntiAerienne();
        else if(source == bTourDeGlace)
            tour = new TourDeGlace();
        else if(source == bTourDeFeu)
            tour = new TourDeFeu();
        else if(source == bTourDAir)
            tour = new TourDAir();
        else if(source == bTourDeTerre)
            tour = new TourDeTerre();
        else if(source == bTourElectrique)
            tour = new TourElectrique();
        else
            return;
        
        tour.setProprietaire(jeu.getJoueurPrincipal());
        
        edpt.tourSelectionnee(tour, Panel_InfoTour.MODE_ACHAT);
        edpt.setTourAAcheter(tour);
    }

    public void partieTerminee()
    {
        // desactivation des tours
        for(JButton bTour : boutonsTours)
            bTour.setEnabled(false); 
    }
    
    public void miseAJour()
    {
        double nbPiecesOr = jeu.getJoueurPrincipal().getNbPiecesDOr();
        
        bTourArcher.setEnabled(nbPiecesOr >= TourArcher.PRIX_ACHAT);
        bTourCanon.setEnabled(nbPiecesOr >= TourCanon.PRIX_ACHAT);
        bTourAntiAerienne.setEnabled(nbPiecesOr >= TourAntiAerienne.PRIX_ACHAT);
        bTourDeGlace.setEnabled(nbPiecesOr >= TourDeGlace.PRIX_ACHAT);
        bTourElectrique.setEnabled(nbPiecesOr >= TourElectrique.PRIX_ACHAT);
        bTourDeFeu.setEnabled(nbPiecesOr >= TourDeFeu.PRIX_ACHAT);
        bTourDAir.setEnabled(nbPiecesOr >= TourDAir.PRIX_ACHAT);
        bTourDeTerre.setEnabled(nbPiecesOr >= TourDeTerre.PRIX_ACHAT);
    }
    
    public void setPause(boolean pause)
    {
        if(pause)
            for(JButton bTour : boutonsTours)
                bTour.setEnabled(false);   
        else
            miseAJour();
    }
}
