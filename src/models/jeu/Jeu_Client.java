package models.jeu;
import java.net.ConnectException;
import exceptions.*;
import reseau.CanalException;
import reseau.jeu.client.ClientJeu;
import models.creatures.VagueDeCreatures;
import models.joueurs.Joueur;
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
    public void poserTour(Tour tour) throws ArgentInsuffisantException, ZoneInaccessibleException
    {
        clientJeu.demanderCreationTour(tour);
    }

    @Override
    public void vendreTour(Tour tour)
    {
        clientJeu.venteTour(tour);
    }

    @Override
    public void ameliorerTour(Tour tour) throws ArgentInsuffisantException
    {
        clientJeu.demanderAmeliorationTour(tour);
    }

    @Override
    public void lancerVague(VagueDeCreatures vague)
    {
        clientJeu.envoyerVague(vague.getNbCreatures(), 1);
    }
    
    public boolean connexionAvecLeServeur(String IP, int port) 
    throws ConnectException, CanalException
    {
        clientJeu = new ClientJeu(this, IP, port, joueur.getPseudo());
        return true;
    }

    /**
     * Permet de poser une tour directement (sans contrôle)
     * 
     * @param idTour l'identificateur de la tour 
     */
    public void poserTourDirect(Tour tour)
    {
        gestionnaireTours.ajouterTour(tour);
    }
    
    /**
     * Permet de supprimer une tour directement (sans contrôle)
     * 
     * @param idTour l'identificateur de la tour 
     */
    public void supprimerTourDirect(int idTour)
    {
        Tour tour = gestionnaireTours.getTour(idTour);
        
        gestionnaireTours.supprimerTour(tour);
    }

    /**
     * Permet d'améliorer une tour directement (sans contrôle)
     * 
     * @param idTour l'identificateur de la tour 
     */
    public void ameliorerTourDirect(int idTour)
    {
        Tour tour = gestionnaireTours.getTour(idTour);

        if(tour != null)
            tour.ameliorer();
    }
}
