package models.animations;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import models.jeu.Jeu;
import models.outils.Outils;

/**
 * Classe de gestion d'un Nuage
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 * @since jdk1.6.0_16
 */
public class Nuage extends Animation
{
	// constantes statiques
    private static final long serialVersionUID         = 1L;
	private static final Image[] IMAGES                = new Image[3];

	// attributs
	private double vitesse;
	private Image IMAGE;
	private float alpha = 1.0f;
	private Jeu jeu;
	private int largeur = 40;
	
	static
    { 
	    for(int i=0;i<IMAGES.length;i++)
            IMAGES[i] = Toolkit.getDefaultToolkit().getImage("img/animations/nuages/"+i+".png");
    }
	
	/**
	 * Constructeur de l'animation
	 * 
	 * @param largeur largeur de l'anim
	 * @param hauteur hauteur de l'anim
	 */
	public Nuage(Jeu jeu)
	{
		super(Outils.tirerNombrePseudoAleatoire(-500,-300), Outils.tirerNombrePseudoAleatoire(-100, jeu.getTerrain().getHauteur()));
	    
		IMAGE = IMAGES[Outils.tirerNombrePseudoAleatoire(0, 2)];
		vitesse = Outils.tirerNombrePseudoAleatoire(2, 10) / 100.0;
		
	    this.jeu = jeu;
		
		hauteur = HAUTEUR_AIR;
	}

	@Override
	public void dessiner(Graphics2D g2)
	{
	    // style
	    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    
	    // dessin
		g2.drawImage(IMAGE,x,y,null);
		
		// retabli la transparence
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
	}

    @Override
    public void animer(long tempsPasse)
    {
        this.x += tempsPasse * vitesse;
        
        if(this.x > jeu.getTerrain().getLargeur() + 100)
            estTerminee = true;
    }
}
