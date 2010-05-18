package models.animations;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import models.outils.Outils;

/**
 * Classe de gestion de l'animation d'une tache de sang
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 * @since jdk1.6.0_16
 */
public class TacheDeSang extends Animation
{
	// constantes statiques
    private static final long serialVersionUID         = 1L;
	private static final Image[] TACHES = new Image[2];
	private static final long DUREE_DE_VIE = 2000;
	private long tempsPasse = 0;
	
	// attributs
	private float alpha = 1.0f;
	private int indiceAnim = 0;
    private Image image;
	
	static
    { 
	    for(int i=0;i<TACHES.length;i++)
	        TACHES[i] = Toolkit.getDefaultToolkit().getImage("img/animations/tachesDeSang/"+i+".png");
    }
	
	/**
	 * Constructeur de l'animation
	 * 
	 * @param largeur largeur de l'anim
	 * @param hauteur hauteur de l'anim
	 */
	public TacheDeSang(int centerX, int centerY)
	{
		super(centerX + Outils.tirerNombrePseudoAleatoire(-2, 2), centerY +  Outils.tirerNombrePseudoAleatoire(-2, 2));
		
		hauteur = Animation.HAUTEUR_SOL;
		
		image = TACHES[Outils.tirerNombrePseudoAleatoire(0, TACHES.length-1)];
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
        
        if(this.tempsPasse > DUREE_DE_VIE)
            estTerminee = true;
        else
            alpha = 1.f - (float) this.tempsPasse / (float) DUREE_DE_VIE;
    }
}
