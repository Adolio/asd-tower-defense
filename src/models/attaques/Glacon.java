package models.attaques;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import models.creatures.Creature;
import models.jeu.Jeu;
import models.tours.Tour;

/**
 * Attaque glacon.
 * 
 * Permet de gérer le ralentissement d'une creature
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | 30 decembre 2009
 * @since jdk1.6.0_16
 */
public class Glacon extends Attaque
{
	// constantes statiques
    private static final long serialVersionUID  = 1L;
    private static final Image IMAGE;
    private final long DUREE_RALENTISSEMENT;
    private long tempsPasse = 0L;
    
    static
    {
        IMAGE = Toolkit.getDefaultToolkit().getImage("img/animations/attaques/glacon.png");
    }
    
	/**
	 * Constructeur de l'animation
	 * 
	 * @param x position initiale x
	 * @param y position initiale y
	 * @param nbPiecesOr nombre de pieces d'or gagne
	 */
	public Glacon(Jeu jeu, Tour attaquant, Creature cible, double coeffRalentissement,  long dureeRalentissement)
	{
		super((int)attaquant.getX(), (int) attaquant.getY(), jeu, attaquant, cible);
		
		cible.setCoeffRalentissement(coeffRalentissement);
		
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
    public void animer(long tempsPasse)
    { 
        this.tempsPasse += tempsPasse;
        
        if(cible.estMorte())
            estTerminee = true;
        else if(this.tempsPasse > DUREE_RALENTISSEMENT)
        {
            cible.setCoeffRalentissement(0.0);
            estTerminee = true;
        }
    }
}
