package models.jeu;

import models.creatures.Creature;
import models.creatures.VagueDeCreatures;

public interface EcouteurDeJeu
{
    
    // ETAT DU JEU
    
    /**
     * Permet d'informer l'écouteur que la partie est terminee
     */
    public void partieTerminee();
    
    /**
     * Permet d'informer l'écouteur que le joueur a gagné une étoile
     */
    public void etoileGagnee();
    
    
    
    // VAGUES
    
    /**
     * Permet d'informer l'écouteur du lancement de vague termine
     */
    public void vagueEntierementLancee(VagueDeCreatures vague);

    
    
    
    // CREATURES
    
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

}
