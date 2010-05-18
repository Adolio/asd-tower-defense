package models.animations;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * Classe de gestion de l'animation d'une perte de vie
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | 22 decembre 2009
 * @since jdk1.6.0_16
 */
public class PerteVie extends Animation
{
	// constantes statiques
    private static final long serialVersionUID         = 1L;
	private static final float ETAPE_ALPHA             = .1f;
	private static final Image BLESSURE;
	
	// attributs
	private float alpha = 1.0f;
	private int largeur;
	private int hauteur;
	
	static
    {
        BLESSURE = Toolkit.getDefaultToolkit().getImage("img/animations/perteVie.png");
    }
	
	/**
	 * Constructeur de l'animation
	 * 
	 * @param largeur largeur de l'anim
	 * @param hauteur hauteur de l'anim
	 */
	public PerteVie(int largeur, int hauteur)
	{
		super(0, 0);
		this.largeur = largeur;
		this.hauteur = hauteur;
	}

	@Override
	public void dessiner(Graphics2D g2)
	{
	    // style
	    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    
	    // dessin
		g2.drawImage(BLESSURE,x,y,largeur,hauteur,null);
		
		// retabli la transparence
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
	}

    @Override
    public void animer(long tempsPasse)
    {
        if(alpha >= ETAPE_ALPHA)
            alpha -= ETAPE_ALPHA;
       
        if(alpha < ETAPE_ALPHA)
            estTerminee = true;
    }
}
