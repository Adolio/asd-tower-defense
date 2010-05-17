package vues;

import models.creatures.VagueDeCreatures;

public interface EcouteurDeLanceurDeVagues
{
    /**
     * Permet d'informer l'écouteur qu'une vague veut etre lancée
     * 
     * @param vague la vague a lancer
     */
    public void lancerVague(VagueDeCreatures vague);
}
