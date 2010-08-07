package vues.reseau;

import exceptions.ArgentInsuffisantException;
import models.creatures.VagueDeCreatures;


/**
 * Classe Observeur du lancer de vague de créatures
 * 
 * @author Aurélien Da Campo
 * @version 1.0 | juin 2010
 * @since jdk1.6.0_16
 */
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
