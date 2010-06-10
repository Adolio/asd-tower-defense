package models.attaques;

import models.creatures.Creature;
import models.tours.Tour;

public interface EcouteurDAttaque
{
    /**
     * Permet d'informer l'écouteur de la fin d'une attaque
     */
    public void attaqueTerminee(Tour attaquant, Creature cible);
}
