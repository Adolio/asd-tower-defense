package models.attaques;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Vector;
import models.creatures.Creature;
import models.jeu.Jeu;
import models.outils.MeilleursScores;
import models.tours.Tour;

/**
 * Attaque Rafale de Vent
 * 
 * Cette classe est une animation qui dessine un suffle partant d'une
 * tour vers une creature.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.1 | 4 mai 2010
 * @since jdk1.6.0_16
 * @see MeilleursScores
 */
public class RafaleDeVent extends Attaque
{
    // constantes finales
    private static final long serialVersionUID = 1L;
    private static final int DIAMETRE_BOULE = 20;
    private static final Image IMAGE_BOULE;
    
    private ArrayList<Creature> creaturesTouchees = new ArrayList<Creature>();
    
    /**
     * Distance max de la rafale
     */
    private final double MAX_DISTANCE;

    // attributs membres
    /**
     * distance de la source
     */
    private int distanceDeLaSource = 0;
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
        tx.translate((int) x + DIAMETRE_BOULE / 2, (int) y + DIAMETRE_BOULE / 2);
        tx.rotate(ANGLE);
        
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
                distanceDeLaSource += 5;
 
                // pourcentage de transparence
                alpha = (float) (1.f - distanceDeLaSource / MAX_DISTANCE);
                
                // calcul
                x = (int) (Math.cos(ANGLE) * distanceDeLaSource + attaquant.getCenterX()); // x
                y = (int) (Math.sin(ANGLE) * distanceDeLaSource + attaquant.getCenterY()); // y

                // blessures des créatures en contact pas encore touchées
                Vector<Creature> creatures = jeu.getGestionnaireCreatures().getCreaturesQuiIntersectent((int)x,(int)y,DIAMETRE_BOULE);
                Creature creature;
                for(int i=0;i<creatures.size();i++)
                {
                    creature = creatures.get(i);
                    
                    if(!creaturesTouchees.contains(creature))
                    {
                        creature.blesser((long)(degats * alpha), attaquant.getPrioprietaire());
                        creaturesTouchees.add(creature);
                    }
                }
            }
        }
    }
}
