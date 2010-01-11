package models.animations;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import models.outils.MeilleursScores;

/**
 * Classe de gestion de l'animation d'un gain de pieces d'or
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 22 decembre 2009
 * @since jdk1.6.0_16
 * @see MeilleursScores
 */
public class GainDePiecesOr extends Animation
{
	// constantes statiques
    private static final long serialVersionUID         = 1L;
	private static final Color COULEUR_GAIN_PIECES_OR  = Color.GREEN;
	private static final float ETAPE_ALPHA             = .01f;
	
	// attributs
	private int nbPiecesOr;
	private float alpha = 1.0f;
	
	/**
	 * Constructeur de l'animation
	 * 
	 * @param x position initiale x
	 * @param y position initiale y
	 * @param nbPiecesOr nombre de pieces d'or gagne
	 */
	public GainDePiecesOr(int x, int y, int nbPiecesOr)
	{
		super(x, y);
		this.nbPiecesOr = nbPiecesOr;
	}

	@Override
	public void dessiner(Graphics2D g2)
	{
	    // style
	    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    g2.setColor(COULEUR_GAIN_PIECES_OR);
	    
	    // dessin
		g2.drawString("+"+nbPiecesOr,x,y);
		
		// retabli la transparence
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
	}

    @Override
    public void animer()
    {
        // diminution de la transparence
        alpha -= ETAPE_ALPHA;
        
        // si invisible
        if (alpha <= .0f)
        {
            alpha       = .0f;
            estTerminee = true;
            return;
        }
        
        // l'animation monte
        y--;
    }
}
