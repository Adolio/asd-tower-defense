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
public class Fumee extends Animation
{
	// constantes statiques
    private static final long serialVersionUID         = 1L;
	private static final Image[] IMAGES = new Image[4];
	
	// attributs
	private float alpha = 1.0f;
	private int indiceAnim = 0;
	
	private int largeur = 40;
	
	
	static
    { 
	    for(int i=0;i<IMAGES.length;i++)
	        IMAGES[i] = Toolkit.getDefaultToolkit().getImage("img/animations/fumee/"+i+".png");
    }
	
	/**
	 * Constructeur de l'animation
	 * 
	 * @param largeur largeur de l'anim
	 * @param hauteur hauteur de l'anim
	 */
	public Fumee(int x, int y)
	{
		super(x, y);
		
		hauteur = HAUTEUR_SOL;
	}

	@Override
	public void dessiner(Graphics2D g2)
	{
	    // style
	    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    
	    // dessin
	    Image i = IMAGES[indiceAnim];
	    
		g2.drawImage(i,x-largeur/2,y-largeur/2,largeur,largeur,null);
		
		// retabli la transparence
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
	}

    @Override
    public void animer(long tempsPasse)
    {
        if(indiceAnim < IMAGES.length-1)
        {
            indiceAnim++;
            alpha -= (1.f / IMAGES.length) / 2.0;
        }
        else
            estTerminee = true;
    }
}
