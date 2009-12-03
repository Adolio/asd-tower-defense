package models.creatures;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class Creature extends Rectangle
{
	private ArrayList<Point> chemin;
	private int type;
	private int sante;
	private int santeMax;
	private int gainPieceDOr;
	private double angle;
	
	public Creature(int x, int y, int largeur, int hauteur)
	{
		super(x,y,largeur,hauteur);
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

	public void setChemin(ArrayList<Point> chemin)
	{
		this.chemin = chemin;
	}
}
