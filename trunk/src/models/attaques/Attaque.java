/*
  Copyright (C) 2010 Aurelien Da Campo

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/

package models.attaques;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Enumeration;
import models.animations.*;
import models.creatures.Creature;
import models.jeu.Jeu;
import models.tours.Tour;

/**
 * Classe de gestion d'une attaque venant d'une tour en direction d'une creature.
 * 
 * @author Aurelien Da Campo
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
    
    protected Jeu jeu;
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
    public Attaque(int x, int y, Jeu jeu, Tour attaquant, Creature cible)
    {
        super(x, y);
        this.jeu        = jeu;
        this.attaquant  = attaquant;
        this.cible      = cible;
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
            if(cible.peutEtreAttaquee(attaquant))
                cible.blesser(degats,attaquant.getPrioprietaire());
            
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
        
        Enumeration<Creature> eCreatures = jeu.getCreatures().elements();
        Creature tmpCreature;
        while(eCreatures.hasMoreElements())
        {
            tmpCreature = eCreatures.nextElement();
            
            if(tmpCreature.peutEtreAttaquee(attaquant))
            {
                
                // si la creature est dans le splash
                distanceImpact = Point.distance(tmpCreature.getCenterX(), 
                                                tmpCreature.getCenterY(), 
                                                impact.x, 
                                                impact.y);
                
                if(distanceImpact <= rayonImpact)
                {
                    // calcul des degats en fonction de la distance de l'impact
                    degatsFinal = (long) (degats - (distanceImpact / rayonImpact * degats));
                    tmpCreature.blesser(degatsFinal, attaquant.getPrioprietaire());
                    
                    a.add(tmpCreature);
                }
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
