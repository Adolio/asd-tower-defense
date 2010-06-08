package reseau.jeu.client;

import models.joueurs.Equipe;
import models.joueurs.Joueur;

/**
 * Interface permettant de mettre en oeuvre le pattern Observable/ Observé pour la
 * classe ClientJeu.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 * @see ClientJeu
 */
public interface EcouteurDeClientJeu
{
    /**
     * Permet d'informer l'ecouteur que le joueur à été initialisé
     */
    public void joueurInitialise();
    
    /**
     * Permet d'informer l'ecouteur que les joueurs ont été mis à jour
     */
    public void joueursMisAJour();
    
    /**
     * Message recu
     */
    public void messageRecu(String message, Joueur auteur);

    /**
     * Permet d'informer l'ecouteur de la deconnexion d'un joueur
     */
    public void joueurDeconnecte(Joueur joueur);

    /**
     * Permet d'informer l'ecouteur qu'une equipe perdue
     */
    public void receptionEquipeAPerdue(Equipe equipe);
}
