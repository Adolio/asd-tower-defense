package models.attaques;

import java.awt.*;
import models.animations.Explosion;
import models.creatures.Creature;
import models.jeu.Jeu;
import models.tours.Tour;

/**
 * Attaque d'une boule de feu.
 * 
 * Cette classe est une animation qui dessine une boule de feu partant d'une
 * tour vers une creature.
 * 
 * @author Romain Poulain
 * @author Aurelien Da Campo
 * @version 1.1 | 4 mai 2010
 * @since jdk1.6.0_16
 */
public class BouleBalistique extends Attaque
{
    // constantes finales
    private static final long serialVersionUID = 1L;
    private static final int DIAMETRE_BOULE = 10;
    private static final Image IMAGE_BOULE;

    // attributs membres
    /**
     * distance entre la tete de la fleche et la tour
     */
    private double distanceCentreBoule = 0;

    /**
     * position de la tete de la fleche
     */
    private double xCentreBoule, yCentreBoule;

    static
    {
        IMAGE_BOULE = Toolkit.getDefaultToolkit().getImage(
                "img/tours/tourDeTerre.png");
    }

    /**
     * Constructeur de l'attaque
     * 
     * @param terrain le terrain sur lequel l'attaque est lancee
     * @param attaquant la tour attaquante
     * @param cible la creature visee
     */
    public BouleBalistique(Jeu jeu, Tour attaquant, Creature cible, long degats,
            double rayonImpact)
    {
        super((int) attaquant.getCenterX(), (int) attaquant.getCenterY(), jeu,
                attaquant, cible);

        this.degats = degats;
        this.rayonImpact = rayonImpact;
    }

    @Override
    public void dessiner(Graphics2D g2)
    {
        double xAttaquant = attaquant.getCenterX();
        double yAttaquant = attaquant.getCenterY();

        // calcul de l'angle entre la cible et la tour
        // /!\ Math.atan2(y,x) /!\
        double angle = Math.atan2(cible.getCenterY() - yAttaquant, cible
                .getCenterX()
                - xAttaquant);

        // calcul de la tete et de la queue de la fleche
        xCentreBoule = Math.cos(angle) * distanceCentreBoule + xAttaquant; // x
        yCentreBoule = Math.sin(angle) * distanceCentreBoule + yAttaquant; // y

        // dessin de la boule de feu
        g2.drawImage(IMAGE_BOULE, (int) xCentreBoule - DIAMETRE_BOULE / 2,
                (int) yCentreBoule - DIAMETRE_BOULE / 2, DIAMETRE_BOULE,
                DIAMETRE_BOULE, null);
    }

    @Override
    public void animer(long tempsPasse)
    {
        if(!estTerminee)
        {
            // la fleche avance
            distanceCentreBoule += tempsPasse / 10.0;
    
            // calcul de la distance max de parcours de la fleche
            double diffX = cible.getCenterX() - attaquant.getCenterX();
            double diffY = cible.getCenterY() - attaquant.getCenterY();
            double distanceMax = Math.sqrt(diffX * diffX + diffY * diffY);
    
            // si cette distance est atteinte ou depassee, l'attaque est
            // terminee
            if (distanceCentreBoule >= distanceMax)
            {
                informerEcouteurAttaqueTerminee();
                estTerminee = true;
    
                jeu.ajouterAnimation(
                        (new Explosion((int) xCentreBoule - DIAMETRE_BOULE,
                                (int) yCentreBoule - DIAMETRE_BOULE)));
    
                attaquerCibles();
                
                estTerminee = true;
            }
        }
    }
}
