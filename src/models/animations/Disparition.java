package models.animations;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Classe de gestion de l'animation d'une tache de sang
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 * @since jdk1.6.0_16
 */
public class Disparition extends Animation
{
	// constantes statiques
    private static final long serialVersionUID = 1L;
	
	private final long DUREE_DE_VIE;
	
	// attributs
	private float alpha = 1.0f;
    private Image image;
    private long tempsPasse = 0;
    
	/**
	 * Constructeur de l'animation
	 * 
	 * @param largeur largeur de l'anim
	 * @param hauteur hauteur de l'anim
	 */
	public Disparition(int centerX, int centerY, Image image, long duree)
	{
	    super(centerX, centerY);
		
		this.image = image;
		this.DUREE_DE_VIE = duree;
	}

	@Override
	public void dessiner(Graphics2D g2)
	{
	    // style
	    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    
	    // dessin
		g2.drawImage(image,x-image.getWidth(null)/2,y-image.getHeight(null)/2,null);
		
		// retabli la transparence
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
	}

    @Override
    public void animer(long tempsPasse)
    {
        this.tempsPasse += tempsPasse;
        
        // temps de vie passé
        if(this.tempsPasse > DUREE_DE_VIE)
            estTerminee = true;
        else
            // fondu
            alpha = 1.f - (float) this.tempsPasse / (float) DUREE_DE_VIE;
    }
}
