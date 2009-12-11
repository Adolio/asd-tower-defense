package vues;

import java.awt.Graphics2D;
import java.awt.Point;

public abstract class Animation extends Point
{
	private static final long serialVersionUID = 1L;

	public Animation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	abstract public void dessiner(Graphics2D g2);
}
