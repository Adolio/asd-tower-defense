package vues;

import exceptions.ArgentInsuffisantException;
import models.creatures.VagueDeCreatures;

public interface EcouteurDeLanceurDeVagues
{
    /**
     * Permet d'informer l'écouteur qu'une vague veut etre lancée
     * 
     * @param vague la vague a lancer
     * @throws ArgentInsuffisantException 
     */
    public void lancerVague(VagueDeCreatures vague) throws ArgentInsuffisantException;

    /**
     * Permet d'informer l'écouteur qu'une vague coute trop chere
     */
    public void erreurPasAssezDArgent();

}
