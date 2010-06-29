package models.animations;

import java.awt.*;

/**
 * Classe de gestion d'une animation
 * <p>
 * Cette classe est utilisee pour afficher des animations
 * <p>
 * Elle est abstraite et doit etre heritee pour etre ensuite instanciee.
 * 
 * @author Aurelien Da Campo
 * @version 1.1 | mai 2010
 * @since jdk1.6.0_16
 */
public abstract class Animation extends Point
{
    public static final int HAUTEUR_SOL = 0;
    public static final int HAUTEUR_AIR = 1;
    
    private static final long serialVersionUID = 1L;
	protected boolean estTerminee;
    protected int hauteur = HAUTEUR_AIR;
    
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
	 * Permet de dessiner l'animation.
	 * 
	 * @param g2 le Graphics2D pour dessiner
	 */
	abstract public void dessiner(Graphics2D g2);

	/**
     * Permet d'animer l'animation
     */
    abstract public void animer(long tempsPasse);
	
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
     
    /**
     * Permet de recuperer la hauteur de l'animation
     * 
     * @return la hauteur
     */
    public int getHauteur()
    {
        return hauteur;
    }
}
