package models.attaques;

import java.awt.*;
import java.io.File;

import models.animations.Fumee;
import models.creatures.Creature;
import models.jeu.Jeu;
import models.tours.Tour;

/**
 * Attaque d'un boulet de canon.
 * 
 * Cette classe est une animation qui dessine un boulet de canon partant d'une tour
 * vers une creature.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | 30 decembre 2009
 * @since jdk1.6.0_16
 */
public class BouletDeCanon extends Attaque
{
    // constantes finales
    private static final long serialVersionUID  = 1L;
    private static final int DIAMETRE_BOULET    = 8;
    private static final Image IMAGE_BOULET;
    public static final File FICHIER_SON_BOULET   = new File("snd/effects/boulet.mp3");
    //private static final int MAX_SONS_BOULET      = 3;
    
    // attributs membres
    /**
     * Vitesse
     */
    private double vitesse = 0.2; // px / ms
    /**
     * distance entre la tete de la fleche et la tour
     */
    private double distanceCentreBoulet = 0;
    
    /**
     * position de la tete de la fleche
     */
    private double xCentreBoulet, yCentreBoulet;
    
    static
    {
        IMAGE_BOULET   = Toolkit.getDefaultToolkit().getImage("img/animations/attaques/bouletDeCanon.png");
    }
    
    
    /**
     * Constructeur de l'attaque
     * 
     * @param terrain le terrain sur lequel l'attaque est lancee
     * @param attaquant la tour attaquante
     * @param cible la creature visee
     */
    public BouletDeCanon(Jeu jeu, Tour attaquant, Creature cible, 
                        long degats, double rayonImpact)
    {
        super((int) attaquant.getCenterX(),(int) attaquant.getCenterY(), jeu, attaquant, cible);
        
        this.degats         = degats;
        this.rayonImpact    = rayonImpact;

        /*
        if(GestionnaireSons.getNbSonsEnLecture(FICHIER_SON_BOULET) < MAX_SONS_BOULET)
        {
            Son son = new Son(FICHIER_SON_BOULET);
            GestionnaireSons.ajouterSon(son);
            son.lire();
        }
        */
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
    public void animer(long tempsPasse)
    {
        if(!estTerminee)
        {
            // la fleche avance
            distanceCentreBoulet += tempsPasse * vitesse;
            
            // calcul de la distance max de parcours de la fleche
            double diffX       = cible.getCenterX() - attaquant.getCenterX();
            double diffY       = cible.getCenterY() - attaquant.getCenterY();  
            double distanceMax = Math.sqrt(diffX * diffX + diffY * diffY);
            
            // si cette distance est atteinte ou depassee, l'attaque est terminee
            if (distanceCentreBoulet >= distanceMax)
            {
                informerEcouteurAttaqueTerminee();
                estTerminee = true;
                attaquerCibles();
                
                jeu.ajouterAnimation(new Fumee((int) xCentreBoulet, (int) yCentreBoulet));

                estTerminee = true;
            }
        }
    }
}
