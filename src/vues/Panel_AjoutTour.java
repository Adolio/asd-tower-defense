package vues;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import models.jeu.Jeu;
import models.tours.*;

// TODO comment
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
        
        setBackground(LookInterface.COULEUR_DE_FOND);
        
        //---------------------
        //-- panel des tours --
        //---------------------
        JPanel pTours = new JPanel(new GridLayout(2,0));
        pTours.setOpaque(false);
        pTours.setPreferredSize(new Dimension(largeur,hauteur));
        
        
        boutonsTours.add(bTourArcher);
        bTourArcher.setToolTipText("Prix : "+TourArcher.PRIX_ACHAT);
        
        boutonsTours.add(bTourCanon);
        bTourCanon.setToolTipText("Prix : "+TourCanon.PRIX_ACHAT);
        
        boutonsTours.add(bTourAntiAerienne);
        bTourAntiAerienne.setToolTipText("Prix : "+TourAntiAerienne.PRIX_ACHAT);
        
        boutonsTours.add(bTourDeGlace);
        bTourDeGlace.setToolTipText("Prix : "+TourDeGlace.PRIX_ACHAT);
        
        boutonsTours.add(bTourElectrique);
        bTourElectrique.setToolTipText("Prix : "+TourElectrique.PRIX_ACHAT);
        
        boutonsTours.add(bTourDeFeu);
        bTourDeFeu.setToolTipText("Prix : "+TourDeFeu.PRIX_ACHAT);
        
        boutonsTours.add(bTourDAir);
        bTourDAir.setToolTipText("Prix : "+TourDAir.PRIX_ACHAT);
        
        boutonsTours.add(bTourDeTerre);
        bTourDeTerre.setToolTipText("Prix : "+TourDeTerre.PRIX_ACHAT);
        
        for(JButton bTour : boutonsTours)
        {
            bTour.addActionListener(this);
            bTour.setBorder(new EmptyBorder(5,5,5,5));
            GestionnaireDesPolices.setStyle(bTour);
            pTours.add(bTour);
        }
        
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
        int nbPiecesOr = jeu.getJoueurPrincipal().getNbPiecesDOr();
        
        bTourArcher.setEnabled(nbPiecesOr >= TourArcher.PRIX_ACHAT);
        bTourCanon.setEnabled(nbPiecesOr >= TourCanon.PRIX_ACHAT);
        bTourAntiAerienne.setEnabled(nbPiecesOr >= TourAntiAerienne.PRIX_ACHAT);
        bTourDeGlace.setEnabled(nbPiecesOr >= TourDeGlace.PRIX_ACHAT);
        bTourElectrique.setEnabled(nbPiecesOr >= TourElectrique.PRIX_ACHAT);
        bTourDeFeu.setEnabled(nbPiecesOr >= TourDeFeu.PRIX_ACHAT);
        bTourDAir.setEnabled(nbPiecesOr >= TourDAir.PRIX_ACHAT);
        bTourDeTerre.setEnabled(nbPiecesOr >= TourDeTerre.PRIX_ACHAT);
    }
}
