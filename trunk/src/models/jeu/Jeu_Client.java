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
 * Classe de représentation d'un client pouvant se connecter à un serveur de jeu.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 * 
 * @see ClientJeu
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
            clientJeu.demanderPoseTour(tour);
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
    ActionNonAutoriseeException, NiveauMaxAtteintException, JoueurHorsJeu
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
    
    /**
     * Permet d'établir la connexion avec le serveur.
     * 
     * @param IP l'IP du serveur distant
     * @param port le port du serveur distant
     * 
     * @throws ConnectException
     * @throws CanalException
     * @throws AucunEmplacementDisponibleException
     */
    public void connexionAvecLeServeur(String IP, int port) 
        throws ConnectException, CanalException, AucunEmplacementDisponibleException
    {
        clientJeu.etablirConnexion(IP, port);
    }

    /**
     * Permet de poser une tour directement (sans contrôle)
     * 
     * @param tour la tour
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
     * @param tour la tour
     */
    public void supprimerTourDirect(Tour tour)
    {
        if(tour != null)
            gestionnaireTours.supprimerTour(tour);
    }

    /**
     * Permet d'améliorer une tour directement (sans contrôle)
     * 
     * @param tour la tour
     */
    public void ameliorerTourDirect(Tour tour)
    {
        if(tour != null)
            tour.ameliorer();
    }

    /**
     * Permet d'ajouter une créature directement (sans contrôle)
     * 
     * @param creature la creature
     */
    public void ajouterCreatureDirect(Creature creature)
    {
        if(creature != null)
            gestionnaireCreatures.ajouterCreature(creature);
    }

    /**
     * Permet de supprimer une créature directement (sans contrôle)
     * 
     * @param creature la creature
     */
    public void supprimerCreatureDirect(Creature creature)
    {
        if(creature != null)
            gestionnaireCreatures.supprimerCreature(creature);
    }

    /**
     * Permet de demander un changement d'equipe
     * 
     * @param joueur le joueur qui veut changer
     * @param equipe l'equipe dans laquelle il veut aller
     * @throws AucunEmplacementDisponibleException
     */
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
    
    /**
     * Permet d'annoncer une erreur du canal
     * 
     * @param e l'exception
     */
    private void erreurCanal(Exception e)
    {
        System.err.println("Jeu_Client.erreurCanal()");
        e.printStackTrace();
    }

    /**
     * Permet de vider les équipes
     */
    public void viderEquipes()
    {
        synchronized (equipes)
        {
            // vide toutes les equipes
            for(Equipe e : equipes)
                e.vider(); 
        }
    }

    /**
     * Permet de modifier l'écouteur du client jeu
     * 
     * @param edcj l'écouteur du client jeu
     */
    public void setEcouteurDeClientJeu(
            EcouteurDeClientJeu edcj)
    {
        clientJeu.setEcouteurDeClientJeu(edcj);
    }

    /**
     * Permet d'annoncer le serveur que le joueur souhaite se déconnecter
     * @throws CanalException
     */
    public void annoncerDeconnexion() throws CanalException
    {
        clientJeu.annoncerDeconnexion();
    }

    /**
     * Permet d'envoyer un message chat
     * 
     * @param message le message
     * @param cible la cible
     * @throws CanalException
     * @throws MessageChatInvalide 
     */
    public void envoyerMsgChat(String message, int cible) throws CanalException, MessageChatInvalide
    {
        clientJeu.envoyerMessage(message, cible);
    } 
}
