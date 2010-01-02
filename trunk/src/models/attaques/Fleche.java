package models.attaques;

import java.awt.Color;
import java.awt.Graphics2D;

import models.creatures.Creature;
import models.outils.MeilleursScores;
import models.outils.Musique;
import models.terrains.Terrain;
import models.tours.Tour;

/**
 * Attaque de fleche.
 * 
 * Cette classe est une animation qui dessine une fleche partant d'une tour
 * vers une creature.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 30 decembre 2009
 * @since jdk1.6.0_16
 * @see MeilleursScores
 */
public class Fleche extends Attaque implements Runnable
{
    // constantes finales
    private static final long serialVersionUID = 1L;
    private static final int LONGUEUR_FLECHE   = 10;
    private static final Color COULEUR_FLECHE  = new Color(128,0,0);
    public static final Musique SON_FLECHE;
    
    // attributs membres
    /**
     * distance entre la tete de la fleche et la tour
     */
    private int distanceTeteTour = 0;
    
    /**
     * position de la tete de la fleche
     */
    private double xTete, yTete;
    
    /**
     * position de la queue de la fleche
     */
    private double xQueue, yQueue;
    
    static
    {
        SON_FLECHE = new Musique("snd/arc.mp3");
    }

    /**
     * Constructeur de l'attaque
     * 
     * @param terrain le terrain sur lequel l'attaque est lancee
     * @param attaquant la tour attaquante
     * @param cible la creature visee
     */
    public Fleche(Terrain terrain, Tour attaquant, Creature cible, int degats)
    {
        super((int) attaquant.getCenterX(),(int) attaquant.getCenterY(), terrain, attaquant, cible);
        
        this.degats = degats;
        
        Thread thread = new Thread(this);
        thread.start();
        
        // TODO
        //SON_FLECHE.lire(1);
    }

    @Override
    public void dessiner(Graphics2D g2)
    {
        double xAttaquant = attaquant.getCenterX();
        double yAttaquant = attaquant.getCenterY();
         
        // calcul de l'angle entre la cible et la tour
        // /!\ Math.atan2(y,x) /!\
        double angle = Math.atan2(
                        cible.getCenterY() - yAttaquant,
                        cible.getCenterX() - xAttaquant); 
        
        // calcul de la tete et de la queue de la fleche
        xTete = Math.cos(angle)*distanceTeteTour + xAttaquant; // x
        yTete = Math.sin(angle)*distanceTeteTour + yAttaquant; // y
        
        xQueue = Math.cos(angle)*(distanceTeteTour - LONGUEUR_FLECHE) + xAttaquant; // x
        yQueue = Math.sin(angle)*(distanceTeteTour - LONGUEUR_FLECHE) + yAttaquant; // y
        
        // dessin de la fleche
        g2.setColor(COULEUR_FLECHE);
        g2.drawLine((int) xTete,(int) yTete,(int) xQueue,(int) yQueue);
    }

    @Override
    public void run()
    {
       // si la creature meurt on arrete l'attaque
       while(!cible.estMorte())
       {    
           // la fleche avance
           distanceTeteTour += 5;
           
           // calcul de la distance max de parcours de la fleche
           double diffX       = cible.getCenterX() - attaquant.getCenterX();
           double diffY       = cible.getCenterY() - attaquant.getCenterY();  
           double distanceMax = Math.sqrt(diffX * diffX + diffY * diffY);
           
           // si cette distance est atteinte ou depassee, l'attaque est terminee
           if (distanceTeteTour >= distanceMax)
           {
               informerEcouteurAttaqueTerminee();
               estTerminee = true;
               
               attaquerCible();
               
               break;
           }

           // on endors le thread
           try{
                Thread.sleep(50);
           } 
           catch (InterruptedException e){
                e.printStackTrace();
           }
       }
       
       estTerminee = true;
    }
}
