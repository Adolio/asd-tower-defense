package vues;

import models.creatures.Creature;
import models.tours.Tour;

/**
 * Interface permettant de mettre en oeuvre le pattern Observable/ Observé pour la
 * classe Panel_Terrain.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | avril 2010
 * @see Panel_Terrain
 */
public interface EcouteurDePanelTerrain
{
    /**
     * Permet d'informer l'ecouteur qu'une tour a ete selectionnee
     * 
     * @param tour la tour selectionnee
     * @param mode le mode de selection
     */
    public void tourSelectionnee(Tour tour,int mode);
    
    /**
     * Permet d'informer l'ecouteur qu'une creature a ete selectionnee
     * 
     * @param creature la creature selectionnee
     */
    public void creatureSelectionnee(Creature creature);
    
    /**
     * Permet d'informer l'ecouteur que le joueur veut acheter une tour
     * 
     * @param tour la tour voulue
     */
    public void acheterTour(Tour tour);
    
    /**
     * Permet d'informer l'ecouteur que le joueur veut vendre une tour
     * 
     * @param tour la tour a ameliorer
     */
    public void vendreTour(Tour tour);
    
    
    /**
     * Permet d'informer l'ecouteur que le joueur veut ameliorer une tour
     * 
     * @param tour la tour a ameliorer
     */
    public void ameliorerTour(Tour tour);

    
    /**
     * Permet de mettre a jour les infos du jeu
     */
    public void miseAJourInfoJeu();
    
    /**
     * Permet de demander une mise a jour des informations de la vague suivante
     */
    public void ajouterInfoVagueSuivanteDansConsole();
    
    
    /**
     * Permet d'informer la fenetre que le joueur veut lancer une vague de
     * creatures.
     */
    public void lancerVagueSuivante();
    
    
    /**
     * Permet d'informer la fenetre qu'on change la tour a acheter
     * 
     * @param tour la nouvelle tour a acheter
     */
    public void setTourAAcheter(Tour tour);
}
