package models.jeu;

import java.net.ConnectException;

import reseau.CanalException;
import reseau.jeu.client.ClientJeu;
import reseau.jeu.serveur.ServeurJeu;
import models.creatures.VagueDeCreatures;
import models.joueurs.Joueur;
import models.tours.IDTours;
import models.tours.Tour;

public class Jeu_Client extends Jeu
{
    private ClientJeu clientJeu;
    private Joueur joueur;
    
    public Jeu_Client(Joueur joueur)
    {
        this.joueur = joueur;
    }

    @Override
    public void poserTour(Tour tour)
    {
        clientJeu.demanderCreationTour((int) tour.getX(),(int) tour.getY(),IDTours.TOUR_ARCHER);
    }

    @Override
    public void vendreTour(Tour tour)
    {
        clientJeu.venteTour(tour.getId());
    }

    @Override
    public void ameliorerTour(Tour tour)
    {
        clientJeu.demanderAmeliorationTour(tour.getId());
    }

    @Override
    public void lancerVague(VagueDeCreatures vague)
    {
        // TODO [CONTACT SERVEUR]
        //clientJeu.envoyerVague(vague);
    }
    
    public void poserTourDirect(Tour tour)
    {
        gestionnaireTours.ajouterTour(tour);
    }

    public boolean connexionAvecLeServeur(String IP, int port) 
    throws ConnectException, CanalException
    {
        clientJeu = new ClientJeu(this, IP, port, joueur.getPseudo());
        return true;
    }
}
