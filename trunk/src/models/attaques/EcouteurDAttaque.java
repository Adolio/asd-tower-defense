package models.attaques;

import models.creatures.Creature;
import models.tours.Tour;

public interface EcouteurDAttaque
{
    public void attaqueTerminee(Tour attaquant, Creature cible);
}
