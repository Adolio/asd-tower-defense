package models.creatures;
import java.awt.Point;
import java.util.ArrayList;

public abstract class Creature
{
	protected int x;
	protected int y;
	private ArrayList<Point> chemin;
	private int type;
	private int sante;
	private int santeMax;
	private int gainPieceDOr;
	private double angle;
	
	public Creature(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public ArrayList<Point> getChemin()
	{
		return chemin;
	}

	public int getSante()
	{
		return sante;
	}

	public int getSanteMax()
	{
		return santeMax;
	}

	public void setAngle(double angle)
	{
		this.angle = angle;
	}

	public double getAngle()
	{
		return angle;
	}
	
	abstract public Creature copier();

	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
}
