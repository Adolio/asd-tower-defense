package models.jeu;

import reseau.jeu.client.ClientJeu;
import models.creatures.VagueDeCreatures;
import models.joueurs.Joueur;
import models.tours.Tour;

public class Jeu_Client extends Jeu
{
    ClientJeu clientJeu;
    
    public Jeu_Client(Joueur joueur)
    {
        clientJeu = new ClientJeu("127.0.0.1",2357, joueur.getPseudo());
    }

    @Override
    public void poserTour(Tour tour)
    {
        clientJeu.demanderCreationTour((int) tour.getX(),(int) tour.getY(), 1);
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
}
