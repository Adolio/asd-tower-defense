package reseau.jeu.client;

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
}
