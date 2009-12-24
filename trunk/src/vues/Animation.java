package vues;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public abstract class Animation extends Point
{
	private static final long serialVersionUID = 1L;
	protected ArrayList<EcouteurDAnimation> ecouteursDAnimation 
	    = new ArrayList<EcouteurDAnimation>();
	protected boolean estTerminee;
	
	public Animation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void ajouterEcouteurDAnimation(EcouteurDAnimation ea)
	{
	    ecouteursDAnimation.add(ea);
	}

	abstract public void dessiner(Graphics2D g2);

    public boolean estTerminee()
    {
        return estTerminee;
    }
}
