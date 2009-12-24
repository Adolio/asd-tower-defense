package vues;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class GainDePiecesOr extends Animation
{
	private static final long serialVersionUID = 1L;
	private static final Color COULEUR_GAIN_PIECES_OR = Color.GREEN;
	private static final float ETAPE_ALPHA = .01f;
	private int nbPiecesOr;
	private float alpha = 1.0f;
	int X_INITIAL, Y_INITIAL;
	
	public GainDePiecesOr(int x, int y, int nbPiecesOr)
	{
		super(x, y);
		
		X_INITIAL = x;
		Y_INITIAL = y;
		
		this.nbPiecesOr = nbPiecesOr;
	}

	public void dessiner(Graphics2D g2)
	{
	    
	    alpha -= ETAPE_ALPHA;
	    
	    if (alpha <= .0f)
        {
            alpha       = .0f;
            estTerminee = true;
        }
	    
	    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    g2.setColor(COULEUR_GAIN_PIECES_OR);
		g2.drawString("+"+nbPiecesOr,x,y);
		y--;
		
		
	}
}
