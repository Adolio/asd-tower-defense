package models.creatures;
import java.awt.Point;
import java.util.ArrayList;

public class Creature
{
	private int x, y;
	private ArrayList<Point> chemin;
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
}
