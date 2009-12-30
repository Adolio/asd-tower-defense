package vues.attaques;

import java.awt.*;
import models.creatures.Creature;
import models.outils.MeilleursScores;
import models.outils.Musique;
import models.terrains.Terrain;
import models.tours.Tour;

/**
 * Attaque d'un boulet de canon.
 * 
 * Cette classe est une animation qui dessine un boulet de canon partant d'une tour
 * vers une creature.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 30 decembre 2009
 * @since jdk1.6.0_16
 * @see MeilleursScores
 */
public class BouletDeCanon extends Attaque implements Runnable
{
    // constantes finales
    private static final long serialVersionUID  = 1L;
    private static final int DIAMETRE_BOULET    = 8;
    private static final Image IMAGE_BOULET;
    public static final Musique SON_CANON;
    
    // attributs membres
    /**
     * distance entre la tete de la fleche et la tour
     */
    private int distanceCentreBoulet = 0;
    
    /**
     * position de la tete de la fleche
     */
    private double xCentreBoulet, yCentreBoulet;
    
    static
    {
        SON_CANON      = new Musique("snd/canon.mp3");
        IMAGE_BOULET   = Toolkit.getDefaultToolkit().getImage("img/attaques/bouletDeCanon.png");
    }
    
    
    /**
     * Constructeur de l'attaque
     * 
     * @param terrain le terrain sur lequel l'attaque est lancee
     * @param attaquant la tour attaquante
     * @param cible la creature visee
     */
    public BouletDeCanon(Terrain terrain, Tour attaquant, Creature cible)
    {
        super((int) attaquant.getCenterX(),(int) attaquant.getCenterY(), terrain, attaquant, cible);
        
        Thread thread = new Thread(this);
        thread.start();
        
        SON_CANON.lire(1);
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
        xCentreBoulet = Math.cos(angle)*distanceCentreBoulet + xAttaquant; // x
        yCentreBoulet = Math.sin(angle)*distanceCentreBoulet + yAttaquant; // y
             
        // dessin du boulet de canon
        g2.drawImage(IMAGE_BOULET, 
                    (int) xCentreBoulet - DIAMETRE_BOULET / 2, 
                    (int) yCentreBoulet - DIAMETRE_BOULET / 2, 
                    DIAMETRE_BOULET, 
                    DIAMETRE_BOULET, null);
    }

    @Override
    public void run()
    {
       // si la creature meurt on arrete l'attaque
       while(!cible.estMorte())
       {    
           // la fleche avance
           distanceCentreBoulet += 5;
           
           // calcul de la distance max de parcours de la fleche
           double diffX       = cible.getCenterX() - attaquant.getCenterX();
           double diffY       = cible.getCenterY() - attaquant.getCenterY();  
           double distanceMax = Math.sqrt(diffX * diffX + diffY * diffY);
           
           // si cette distance est atteinte ou depassee, l'attaque est terminee
           if (distanceCentreBoulet >= distanceMax)
           {
               informerEcouteurAttaqueTerminee();
               estTerminee = true;
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
