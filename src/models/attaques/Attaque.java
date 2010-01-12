package models.attaques;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Enumeration;
import models.animations.*;
import models.creatures.Creature;
import models.terrains.Terrain;
import models.tours.Tour;

/**
 * Classe de gestion d'une attaque venant d'une tour en direction d'une creature.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 30 janvier 2009
 * @since jdk1.6.0_16
 * @see Tour
 * @see Creature
 */
abstract public class Attaque extends Animation
{
    private static final long serialVersionUID = 1L;
    protected Creature cible;
    protected Tour attaquant;

    protected long degats;
    protected double rayonImpact;
    protected double coeffRalentissement;
    
    protected Terrain terrain;
    private ArrayList<EcouteurDAttaque> ecouteursDAttaque = new ArrayList<EcouteurDAttaque>();
    
    /**
     * Constructeur de l'attaque
     * 
     * @param x position initiale x
     * @param y position initiale y
     * @param terrain le terrain sur lequel est l'attaque
     * @param attaquant la tour attaquante
     * @param cible la creature attaquee
     */
    public Attaque(int x, int y, Terrain terrain, Tour attaquant, Creature cible)
    {
        super(x, y);
        this.attaquant  = attaquant;
        this.cible      = cible;
        this.terrain    = terrain;
    }
    
    /**
     * Permet d'effectuer toutes les operations necessaire pour blesser la creature
     * en fonction de la valeur des attributs.
     */
    public ArrayList<Creature> attaquerCibles()
    {
        // s'il y a un rayon d'impact
        if(rayonImpact > 0.0)
            return blesserCreaturesDansZoneImpact();
        else
        {
            cible.blesser(degats);
            
            ArrayList<Creature> a = new ArrayList<Creature>();
            a.add(cible); 
            return a;
        }
    }
    
    /**
     * Permet de blesser des creatures dans une zone d'impact
     * 
     * @param impact le point d'impact de l'attaque
     * @param rayonImpact le rayon d'impact faisant des degats
     */
    synchronized public ArrayList<Creature> blesserCreaturesDansZoneImpact()
    {
        // degats de zone
        ArrayList<Creature> a = new ArrayList<Creature>();
        
        Point impact = new Point((int) cible.getCenterX(), (int) cible.getCenterY());
        long degatsFinal;
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
                degatsFinal = (long) (degats - (distanceImpact / rayonImpact * degats));
                tmpCreature.blesser(degatsFinal);
                
                a.add(tmpCreature);
            }
        }
        
        return a;
    }
    
    /**
     * Permet d'ajouter un ecouteur d'attaque
     * 
     * @param ea l'ecouteur d'attaque
     */
    public void ajouterEcouteurAttaque(EcouteurDAttaque ea)
    {
        ecouteursDAttaque.add(ea);
    }
    
    /**
     * Permet d'informer tous les ecouteurs d'attaque que l'attaque est 
     * terminee
     */
    protected void informerEcouteurAttaqueTerminee()
    {
        for(EcouteurDAttaque ea : ecouteursDAttaque)
            ea.attaqueTerminee(attaquant, cible);
    }
}
