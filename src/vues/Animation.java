package vues;

import java.awt.*;

/**
 * Classe de gestion d'une animation
 * <p>
 * Cette classe est utilisee pour afficher des animations
 * <p>
 * Elle est abstraite et doit etre heritee pour etre ensuite instanciee.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 10 decembre 2009
 * @since jdk1.6.0_16
 */
public abstract class Animation extends Point
{
	private static final long serialVersionUID = 1L;
	protected boolean estTerminee;
	
	/**
	 * Constructeur de l'animation.
	 * 
	 * @param x position initiale x
	 * @param y position initiale y
	 */
	public Animation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Force les fils a creer une methode de dessin
	 * @param g2
	 */
	abstract public void dessiner(Graphics2D g2);

	/**
	 * Permet de savoir si l'animation est terminee
	 * 
	 * Lorsqu'une animation est terminee, elle sera detruite.
	 * 
	 * @return true si elle est terminee et false sinon
	 */
    public boolean estTerminee()
    {
        return estTerminee;
    }
}
