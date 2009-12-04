package models.creatures;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class Creature extends Rectangle implements Runnable
{
	private static final long serialVersionUID = 1L;
	private ArrayList<Point> chemin;
	private int type;
	private int sante;
	private int santeMax;
	private int gainPieceDOr;
	private double angle;
	private Thread thread;
	protected boolean enJeu;
	private double vitesse = 10;
	
	public Creature(int x, int y, int largeur, int hauteur)
	{
		super(x,y,largeur,hauteur);
	}

	public synchronized ArrayList<Point> getChemin()
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
	
	public void demarrer()
	{
		thread = new Thread(this);
		thread.start();
		enJeu = true;
	}
	
	public void avancerSurChemin()
	{
		ArrayList<Point> chemin = getChemin();
		
		if(chemin.size() > 0)
		{
			Point p = chemin.get(0);
			
			if(x > p.getX()) 	  x--;
			else if(x < p.getX()) x++;
			
			if(y > p.getY()) 	  y--;
			else if(y < p.getY()) y++;
			
			if(x == p.getX() && y == p.getY())
				chemin.remove(0);
		}
	}
	
	public void run()
	{
		while(enJeu)
		{
			avancerSurChemin();
			
			try{
				Thread.sleep((long) (1.0/vitesse * 1000.0));
			} 
			catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}
