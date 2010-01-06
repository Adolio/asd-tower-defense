package models.attaques;

import java.awt.*;

import models.creatures.Creature;
import models.outils.MeilleursScores;
import models.terrains.Terrain;
import models.tours.Tour;

/**
 * Attaque glacon.
 * 
 * Permet de gerer le ralentissement d'une creature
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 30 decembre 2009
 * @since jdk1.6.0_16
 * @see MeilleursScores
 */
public class Glacon extends Attaque implements Runnable
{
    // constantes finales
    private static final long serialVersionUID  = 1L;
    private static final Image IMAGE;
    private long DUREE_RALENTISSEMENT;
    static
    {
        IMAGE   = Toolkit.getDefaultToolkit().getImage("img/attaques/glacon.png");
    }
 
    /**
     * Constructeur de l'attaque
     * 
     * @param terrain le terrain sur lequel l'attaque est lancee
     * @param attaquant la tour attaquante
     * @param cible la creature visee
     */
    public Glacon(Terrain terrain, Tour attaquant, Creature cible, long dureeRalentissement)
    {
        super((int) attaquant.getCenterX(),(int) attaquant.getCenterY(), terrain, attaquant, cible);
        
        DUREE_RALENTISSEMENT = dureeRalentissement;
        
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void dessiner(Graphics2D g2)
    {
        // dessin de la boule de feu
        g2.drawImage(IMAGE, 
                    (int) cible.getX(), 
                    (int) cible.getY(), 
                    (int) cible.getWidth(), 
                    (int) cible.getHeight(), null);
    }

    @Override
    public void run()
    {
        enJeu = true;
        
        // on endors le thread
        int etapeI = 100;
        for(double i=0.0;i < DUREE_RALENTISSEMENT && enJeu;i += etapeI)
        {
            if(cible.estMorte())
                break;
           
            try{
                 Thread.sleep(etapeI);
            } 
            catch (InterruptedException e){
                 e.printStackTrace();
            } 
        }
        
        // reinitialisation du relentissemeent
        cible.setCoeffRalentissement(0.0);
        
        estTerminee = true;
    }
}
