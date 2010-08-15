package models.attaques;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Vector;
import models.creatures.Creature;
import models.jeu.Jeu;
import models.tours.Tour;

/**
 * Attaque Rafale de Vent
 * 
 * Cette classe est une animation qui dessine un suffle partant d'une
 * tour vers une creature.
 * 
 * @author Aurelien Da Campo
 * @version 1.1 | 4 mai 2010
 * @since jdk1.6.0_16
 */
public class RafaleDeVent extends Attaque
{
    // constantes finales
    private static final long serialVersionUID = 1L;
    private static final int DIAMETRE_BOULE = 20;
    private static final Image IMAGE_BOULE;
    
    private ArrayList<Creature> creaturesTouchees = new ArrayList<Creature>();
    
    /**
     * Vitesse
     */
    private double vitesse = 0.1; // px / ms
    /**
     * Distance max de la rafale
     */
    private final double MAX_DISTANCE;

    // attributs membres
    /**
     * distance de la source
     */
    private double distanceDeLaSource = 0;
    private final double ANGLE;
    private float alpha;
    
    static
    {
        IMAGE_BOULE = Toolkit.getDefaultToolkit().getImage(
                "img/animations/attaques/rafaleDeVent.png");
    }

    /**
     * Constructeur de l'attaque
     * 
     * @param terrain le terrain sur lequel l'attaque est lancee
     * @param attaquant la tour attaquante
     * @param cible la creature visee
     */
    public RafaleDeVent(Jeu jeu, Tour attaquant, Creature cible, long degats)
    {
        super((int) attaquant.getCenterX(), (int) attaquant.getCenterY(), jeu,
                attaquant, cible);

        this.degats = degats;

        ANGLE = Math.atan2(cible.getCenterY() - attaquant.getCenterY(), 
                cible.getCenterX() - attaquant.getCenterX());
        
        MAX_DISTANCE = attaquant.getRayonPortee();
    
    }
    

    @Override
    public void dessiner(Graphics2D g2)
    { 
        // rotation des créatures
        AffineTransform tx = new AffineTransform();
        tx.translate((int) x, (int) y);
        tx.rotate(ANGLE);
        tx.translate((int) -DIAMETRE_BOULE / 2.0, (int) -DIAMETRE_BOULE / 2.0);

        // dessin de la boule de feu
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.drawImage(IMAGE_BOULE, tx, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));  
    }

    @Override
    public void animer(long tempsPasse)
    {
        if(!estTerminee)
        {
            if(distanceDeLaSource >= MAX_DISTANCE)
            {
                informerEcouteurAttaqueTerminee();
                estTerminee = true;
            }
            else
            {
                // la fleche avance
                distanceDeLaSource += tempsPasse * vitesse;
 
                // pourcentage de transparence
                alpha = (float) (1.f - distanceDeLaSource / MAX_DISTANCE);
                
                if(alpha < 0.0)
                    alpha = 0.0f;
                
                // calcul
                x = (int) (Math.cos(ANGLE) * distanceDeLaSource + attaquant.getCenterX()); // x
                y = (int) (Math.sin(ANGLE) * distanceDeLaSource + attaquant.getCenterY()); // y

                
                Vector<Creature> creatures = jeu.getCreaturesQuiIntersectent((int)x,(int)y,DIAMETRE_BOULE);
                Creature creature;
                for(int i=0;i<creatures.size();i++)
                {
                    creature = creatures.get(i);
                    
                    // blessures des créatures volantes en contact pas encore touchées
                    if(creature.getType() == Creature.TYPE_AERIENNE && !creaturesTouchees.contains(creature))
                    {
                        creature.blesser((long)(degats * alpha), attaquant.getPrioprietaire());
                        creaturesTouchees.add(creature);
                    }
                }
            }
        }
    }
}
