package models.jeu;
import java.net.ConnectException;

import exceptions.*;
import reseau.CanalException;
import reseau.jeu.client.ClientJeu;
import models.creatures.Creature;
import models.creatures.VagueDeCreatures;
import models.joueurs.Equipe;
import models.joueurs.Joueur;
import models.tours.Tour;

public class Jeu_Client extends Jeu
{
    private ClientJeu clientJeu;
    
    public Jeu_Client(Joueur joueur)
    {
        setJoueurPrincipal(joueur);
    }

    @Override
    public void poserTour(Tour tour) throws ArgentInsuffisantException, ZoneInaccessibleException
    {
        try
        {
            clientJeu.demanderCreationTour(tour);
        } 
        catch (CanalException e)
        {
            erreurCanal(e);
        }
    }

    @Override
    public void vendreTour(Tour tour) throws ActionNonAutoriseeException
    {
        try
        {
            clientJeu.demanderVenteTour(tour);
        }
        catch (CanalException e)
        {
           erreurCanal(e);
        }
    }

    @Override
    public void ameliorerTour(Tour tour) throws ArgentInsuffisantException, 
    ActionNonAutoriseeException
    {
        try
        {
            clientJeu.demanderAmeliorationTour(tour);
        } 
        catch (CanalException e)
        {
            erreurCanal(e);
        }
    }

    @Override
    public void lancerVague(Equipe equipe, VagueDeCreatures vague) 
    {
        try
        {
            clientJeu.envoyerVague(vague);
        } 
        catch (ArgentInsuffisantException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        catch (CanalException e)
        {
            erreurCanal(e);
        }
    }
    
    public boolean connexionAvecLeServeur(String IP, int port) 
    throws ConnectException, CanalException, AucunEmplacementDisponibleException
    {
        clientJeu = new ClientJeu(this, IP, port, getJoueurPrincipal());
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

    public void ajouterCreatureDirect(Creature creature)
    {
        gestionnaireCreatures.ajouterCreature(creature);
    }

    public void supprimerCreatureDirect(int id)
    {
        // FIXME pas optimal
        Creature creature = gestionnaireCreatures.getCreature(id);
        gestionnaireCreatures.supprimerCreature(creature);
    }

    public void changerEquipe(Joueur joueur, Equipe equipe) throws AucunEmplacementDisponibleException
    {
        try
        {
            clientJeu.demanderChangementEquipe(equipe);
        } 
        catch (CanalException e)
        {
            erreurCanal(e);
        } 
    }
    
    private void erreurCanal(Exception e)
    {
        System.err.println("Jeu_Client.erreurCanal()");
        e.printStackTrace();
    }
}
