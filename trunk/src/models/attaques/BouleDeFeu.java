package models.attaques;

import java.awt.*;

import models.creatures.Creature;
import models.outils.MeilleursScores;
import models.terrains.Terrain;
import models.tours.Tour;

/**
 * Attaque d'une boule de feu.
 * 
 * Cette classe est une animation qui dessine une boule de feu  partant d'une tour
 * vers une creature.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 30 decembre 2009
 * @since jdk1.6.0_16
 * @see MeilleursScores
 */
public class BouleDeFeu extends Attaque implements Runnable
{
    // constantes finales
    private static final long serialVersionUID  = 1L;
    private static final int DIAMETRE_BOULET    = 8;
    private static final Image IMAGE_BOULE;
    
    // attributs membres
    /**
     * distance entre la tete de la fleche et la tour
     */
    private int distanceCentreBoule = 0;
    
    /**
     * position de la tete de la fleche
     */
    private double xCentreBoule, yCentreBoule;
    
    static
    {
        IMAGE_BOULE   = Toolkit.getDefaultToolkit().getImage("img/attaques/bouleDeFeu.png");
    }
    
    
    /**
     * Constructeur de l'attaque
     * 
     * @param terrain le terrain sur lequel l'attaque est lancee
     * @param attaquant la tour attaquante
     * @param cible la creature visee
     */
    public BouleDeFeu(Terrain terrain, Tour attaquant, Creature cible, 
                      int degats, double rayonImpact)
    {
        super((int) attaquant.getCenterX(),(int) attaquant.getCenterY(), terrain, attaquant, cible);
        
        this.degats         = degats;
        this.rayonImpact    = rayonImpact;
        
        Thread thread = new Thread(this);
        thread.start();
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
        xCentreBoule = Math.cos(angle)*distanceCentreBoule + xAttaquant; // x
        yCentreBoule = Math.sin(angle)*distanceCentreBoule + yAttaquant; // y
             
        // dessin de la boule de feu
        g2.drawImage(IMAGE_BOULE, 
                    (int) xCentreBoule - DIAMETRE_BOULET / 2, 
                    (int) yCentreBoule - DIAMETRE_BOULET / 2, 
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
           distanceCentreBoule += 5;
           
           // calcul de la distance max de parcours de la fleche
           double diffX       = cible.getCenterX() - attaquant.getCenterX();
           double diffY       = cible.getCenterY() - attaquant.getCenterY();  
           double distanceMax = Math.sqrt(diffX * diffX + diffY * diffY);
           
           // si cette distance est atteinte ou depassee, l'attaque est terminee
           if (distanceCentreBoule >= distanceMax)
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
