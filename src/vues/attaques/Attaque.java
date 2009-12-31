package vues.attaques;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Enumeration;

import vues.animations.*;
import models.creatures.Creature;
import models.terrains.Terrain;
import models.tours.Tour;

abstract public class Attaque extends Animation
{
    private static final long serialVersionUID = 1L;
    protected Creature cible;
    protected Tour attaquant;

    protected int degats;
    protected double rayonImpact;
    private double coeffRalentissement;
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
    
    /**
     * Permet d'effectuer toutes les operations necessaire pour blesser la creature
     * en fonction de la valeur des attributs.
     */
    public void attaquerCible()
    {
        // si l'attaque ralenti
        if(coeffRalentissement > 0.0)
            cible.setCoeffRalentissement(coeffRalentissement);
        
        // s'il y a un rayon d'impact
        if(rayonImpact > 0.0)
            blesserCreaturesDansZoneImpact();
        else
            cible.blesser(degats);
    }
    
    /**
     * Permet de blesser des creatures dans une zone d'impact
     * 
     * @param impact le point d'impact de l'attaque
     * @param rayonImpact le rayon d'impact faisant des degats
     */
    synchronized public void blesserCreaturesDansZoneImpact()
    {
        // degats de zone
        Point impact = new Point((int) cible.getCenterX(), (int) cible.getCenterY());
        int degatsFinal;
        double distanceImpact;
        
        Enumeration<Creature> eCreatures = terrain.getCreatures().elements();
        Creature tmpCreature;
        while(eCreatures.hasMoreElements())
        {
            tmpCreature = eCreatures.nextElement();
            
            // si la creature est dans le splash
            distanceImpact = Point.distance(tmpCreature.getCenterX(), 
                                            tmpCreature.getCenterY(), 
                                            impact.x, 
                                            impact.y);
            
            if(distanceImpact <= rayonImpact)
            {
                // calcul des degats en fonction de la distance de l'impact
                degatsFinal = (int) (degats - (distanceImpact / rayonImpact * degats));
                tmpCreature.blesser(degatsFinal);
            }
        }
        
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
