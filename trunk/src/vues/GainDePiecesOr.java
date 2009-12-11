package vues;

import java.awt.Color;
import java.awt.Graphics2D;

public class GainDePiecesOr extends Animation
{
	private static final long serialVersionUID = 1L;
	private static final Color COULEUR_GAIN_PIECES_OR = Color.GREEN;
	private int nbPiecesOr;
	
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
		g2.setColor(COULEUR_GAIN_PIECES_OR);
		g2.drawString("+"+nbPiecesOr,x,y);
		y--;
	}
}
