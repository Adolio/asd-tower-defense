package models.animations;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * Classe de gestion de l'animation d'une explosion
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 * @since jdk1.6.0_16
 */
public class Explosion extends Animation
{
	// constantes statiques
    private static final long serialVersionUID         = 1L;
	private static final Image[] EXPLOSION = new Image[4];
	
	// attributs
	private float alpha = 1.0f;
	private int indiceAnim = 0;
	
	static
    { 
	    for(int i=0;i<EXPLOSION.length;i++)
	        EXPLOSION[i] = Toolkit.getDefaultToolkit().getImage("img/animations/explosion/"+i+".gif");
    }
	
	/**
	 * Constructeur de l'animation
	 * 
	 * @param largeur largeur de l'anim
	 * @param hauteur hauteur de l'anim
	 */
	public Explosion(int x, int y)
	{
		super(x, y);
	}

	@Override
	public void dessiner(Graphics2D g2)
	{
	    // style
	    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    
	    // dessin
		g2.drawImage(EXPLOSION[indiceAnim],x,y,null);
		
		// retabli la transparence
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
	}

    @Override
    public void animer(long tempsPasse)
    {
        if(indiceAnim < EXPLOSION.length-1)
        {
            indiceAnim++;
            alpha -= (1.f / EXPLOSION.length) / 2.0;
        }
        else
            estTerminee = true;
    }
}
