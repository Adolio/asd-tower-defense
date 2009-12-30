package vues.attaques;

import java.awt.Point;
import java.util.ArrayList;
import vues.animations.*;
import models.creatures.Creature;
import models.terrains.Terrain;
import models.tours.Tour;

abstract public class Attaque extends Animation
{
    private static final long serialVersionUID = 1L;
    protected Creature cible;
    protected Tour attaquant;
    private int rayonImpact;
    private int degats;
    private double ralentissement;
    private Terrain terrain;
    
    private ArrayList<EcouteurDAttaque> ecouteursDAttaque = new ArrayList<EcouteurDAttaque>();
    
    // TODO
    public Attaque(int x, int y, Terrain terrain, Tour attaquant, Creature cible)
    {
        super(x, y);
        this.attaquant  = attaquant;
        this.cible      = cible;
        this.terrain    = terrain;
    }
    
    // TODO
    protected ArrayList<Creature> getCreaturesDansRayonDImpact()
    {
        ArrayList<Creature> creaturesDansRayonDImpact = new ArrayList<Creature>();
    
        // degats de zone
        double distanceImpact;
        
        // monopolisation de la collection des creatures
        synchronized (terrain.getCreatures())
        {
            for(Creature creature : terrain.getCreatures())
            {
                // si la creature est dans le splash
                distanceImpact = Point.distance(creature.getCenterX(), 
                                                creature.getCenterY(), 
                                                cible.x, 
                                                cible.y);
                
                if(distanceImpact <= rayonImpact)
                    creaturesDansRayonDImpact.add(creature);
            }
        }
    
        return creaturesDansRayonDImpact;
    }
    
    // TODO
    public void ajouterEcouteurAttaque(EcouteurDAttaque ea)
    {
        ecouteursDAttaque.add(ea);
    }
    
    // TODO
    protected void informerEcouteurAttaqueTerminee()
    {
        for(EcouteurDAttaque ea : ecouteursDAttaque)
            ea.attaqueTerminee(attaquant, cible);
    }
   
}
