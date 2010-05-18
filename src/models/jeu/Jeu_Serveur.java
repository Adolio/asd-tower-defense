package models.jeu;

import models.creatures.VagueDeCreatures;
import models.tours.Tour;

public class Jeu_Serveur extends Jeu
{

    @Override
    public void poserTour(Tour tour) throws Exception
    {
        // c'est bien une tour valide ?
        if (tour == null)
            throw new IllegalArgumentException("Tour nulle");

        // suffisemment d'argent ?
        if(!laTourPeutEtreAchetee(tour))    
            throw new NoMoneyException("Pose impossible : Pas assez d'argent");
        
        // si elle peut pas etre posee
        if (!laTourPeutEtrePosee(tour))
            throw new Exception("Pose impossible : Zone non accessible");

        // si elle bloque le chemin de A vers B
        if (terrain.laTourBloqueraLeChemin(tour))
            throw new Exception("Pose impossible : Chemin bloqué");

        // desactive la zone dans le maillage qui correspond a la tour
        terrain.desactiverZone(tour, true);

        // ajout de la tour
        gestionnaireTours.ajouterTour(tour);
        
        // mise a jour du jeu de la tour
        tour.setJeu(this);
        
        // mise en jeu de la tour
        tour.mettreEnJeu();
        
        // debit des pieces d'or
        tour.getPrioprietaire().setNbPiecesDOr(
                tour.getPrioprietaire().getNbPiecesDOr() - tour.getPrixAchat()); 
    
    
        // TODO [CONTACT CLIENTS]
    
    }

    @Override
    public void vendreTour(Tour tour)
    {
        // supprime la tour
        gestionnaireTours.supprimerTour(tour);
        
        // debit des pieces d'or
        tour.getPrioprietaire().setNbPiecesDOr(
                tour.getPrioprietaire().getNbPiecesDOr() + tour.getPrixDeVente());
    
        // TODO [CONTACT CLIENTS]
    }

    @Override
    public void ameliorerTour(Tour tour) throws Exception
    {
        if(!tour.peutEncoreEtreAmelioree())
            throw new Exception("Amélioration impossible : Niveau max atteint");
        
        if(tour.getPrioprietaire().getNbPiecesDOr() < tour.getPrixAchat())
            throw new Exception("Amélioration impossible : Pas assez d'argent");

        // debit des pieces d'or
        tour.getPrioprietaire().setNbPiecesDOr(tour.getPrioprietaire().getNbPiecesDOr() - tour.getPrixAchat());
     
        // amelioration de la tour
        tour.ameliorer();
        
        // TODO [CONTACT CLIENTS]
    }

    @Override
    public void lancerVague(VagueDeCreatures vague)
    { 
        vague.lancerVague(this, joueur.getEquipe(), this, this);
        
        // TODO [CONTACT CLIENTS]
    }
}
