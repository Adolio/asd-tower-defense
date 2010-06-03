package models.jeu;
import java.net.ConnectException;
import exceptions.*;
import reseau.CanalException;
import reseau.jeu.client.ClientJeu;
import reseau.jeu.client.EcouteurDeClientJeu;
import models.creatures.*;
import models.joueurs.*;
import models.tours.Tour;

/**
 * Classe de représentation d'un client sur le serveur.
 * 
 * C'est cette classe qui recevera les messages venant du client.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 */
public class Jeu_Client extends Jeu
{
    private ClientJeu clientJeu;
    
    public Jeu_Client(Joueur joueur)
    {
        setJoueurPrincipal(joueur);
        
        clientJeu = new ClientJeu(this);
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
    public void lancerVague(Joueur joueur, Equipe equipe, VagueDeCreatures vague) throws ArgentInsuffisantException
    {
        try
        {
            clientJeu.envoyerVague(vague);
        } 
        catch (CanalException e)
        {
            erreurCanal(e);
        } 
    }
    
    public boolean connexionAvecLeServeur(String IP, int port) 
    throws ConnectException, CanalException, AucunEmplacementDisponibleException
    {
        clientJeu.etablirConnexion(IP, port);
        
        return true;
    }

    /**
     * Permet de poser une tour directement (sans contrôle)
     * 
     * @param idTour l'identificateur de la tour 
     */
    public void poserTourDirect(Tour tour)
    {
        if(tour != null)
        {
            tour.mettreEnJeu();
            tour.setJeu(this);
            
            gestionnaireTours.ajouterTour(tour);
        }
    }
    
    /**
     * Permet de supprimer une tour directement (sans contrôle)
     * 
     * @param idTour l'identificateur de la tour 
     */
    public void supprimerTourDirect(Tour tour)
    {
        if(tour != null)
            gestionnaireTours.supprimerTour(tour);
    }

    /**
     * Permet d'améliorer une tour directement (sans contrôle)
     * 
     * @param idTour l'identificateur de la tour 
     */
    public void ameliorerTourDirect(Tour tour)
    {
        if(tour != null)
            tour.ameliorer();
    }

    public void ajouterCreatureDirect(Creature creature)
    {
        if(creature != null)
            gestionnaireCreatures.ajouterCreature(creature);
    }

    public void supprimerCreatureDirect(Creature creature)
    {
        if(creature != null)
            gestionnaireCreatures.supprimerCreature(creature);
    }

    public void changerEquipe(Joueur joueur, Equipe equipe) throws AucunEmplacementDisponibleException
    {
        try
        {
            clientJeu.demanderChangementEquipe(joueur,equipe);
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

    public void viderEquipes()
    {
        synchronized (equipes)
        {
            // vide toutes les equipes
            for(Equipe e : equipes)
                e.vider(); 
        }
    }

    public void setEcouteurDeClientJeu(
            EcouteurDeClientJeu edcj)
    {
        clientJeu.setEcouteurDeClientJeu(edcj);
    }

    public void annoncerDeconnexion() throws CanalException
    {
        clientJeu.annoncerDeconnexion();
    }

    public void envoyerMsg(String message, int cible) throws CanalException
    {
        clientJeu.envoyerMessage(message, cible);
    }
}
