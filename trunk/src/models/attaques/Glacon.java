package models.attaques;

import java.awt.*;
import java.util.Date;
import models.creatures.Creature;
import models.jeu.Jeu;
import models.outils.MeilleursScores;
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
public class Glacon extends Attaque
{
    // constantes finales
    private static final long serialVersionUID  = 1L;
    private static final Image IMAGE;
    private long DUREE_RALENTISSEMENT;
    private long tempsPasse;
    private Date date = new Date();
    private long tempsDernierPassage = date.getTime();
    
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
    public Glacon(Jeu jeu, Tour attaquant, Creature cible, long dureeRalentissement)
    {
        super((int) attaquant.getCenterX(),(int) attaquant.getCenterY(), jeu, attaquant, cible);
        
        DUREE_RALENTISSEMENT = dureeRalentissement;
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
    public void animer()
    {
        // recuperation du temps passe depuis le dernier appel
        date = new Date();
        tempsPasse += date.getTime() - tempsDernierPassage;
        tempsDernierPassage = date.getTime();
        
        // le temps est passe
        if(tempsPasse > DUREE_RALENTISSEMENT)
        {
            if(!cible.estMorte())
                // reinitialisation du ralentissemeent
                cible.setCoeffRalentissement(0.0);
            
            estTerminee = true;
        }
        else if(cible.estMorte())
            estTerminee = true;     
    }
}
