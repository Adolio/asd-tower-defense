package models.jeu;

import models.animations.Animation;
import models.creatures.Creature;
import models.creatures.VagueDeCreatures;
import models.joueurs.Joueur;
import models.tours.Tour;

public interface EcouteurDeJeu
{
    
    // ETAT DU JEU
    
    /**
     * Permet d'informer l'écouteur que la partie a démarrée
     */
    public void partieDemarree();
    
    /**
     * Permet d'informer l'écouteur que la partie est terminee
     */
    public void partieTerminee();
    
    /**
     * Permet d'informer l'écouteur que le joueur a gagné une étoile
     */
    public void etoileGagnee();
    
    
    // JOUEURS
    
    /**
     * Permet d'informer l'écouteur qu'un joueur a rejoint la partie
     */
    public void joueurAjoute(Joueur joueur);
    
    
    // TOURS
    
    /**
     * Permet d'informer l'écouteur qu'une tour à été posée
     */
    public void tourPosee(Tour tour);
    
    /**
     * Permet d'informer l'écouteur qu'une tour à été vendue
     */
    public void tourVendue(Tour tour);
    
    /**
     * Permet d'informer l'écouteur qu'une tour à été améliorée
     */
    public void tourAmelioree(Tour tour);
    
    
    // VAGUES
    
    /**
     * Permet d'informer l'écouteur du lancement de vague termine
     */
    public void vagueEntierementLancee(VagueDeCreatures vague);

    
    // CREATURES
    
    /**
     * Permet d'informer l'écouteur qu'une créature à été blessée
     */
    public void creatureAjoutee(Creature creature);
    
    
    /**
     * Permet d'informer l'écouteur qu'une créature à été blessée
     */
    public void creatureBlessee(Creature creature);

    /**
     * Permet d'informer l'écouteur de la mort d'une créature
     */
    public void creatureTuee(Creature creature);
    
    /**
     * Permet d'informer l'écouteur l'arrivée d'une créature
     */
    public void creatureArriveeEnZoneArrivee(Creature creature);
    
    
    // ANIMATIONS
    
    /**
     * Permet d'informer l'écouteur qu'une animation à été ajoutée
     */
    public void animationAjoutee(Animation animation);
    
    /**
     * Permet d'informer l'écouteur qu'une animation à été terminée
     */
    public void animationTerminee(Animation animation);
}
