package models.creatures;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class Creature extends Rectangle implements Runnable
{
	private static final long serialVersionUID = 1L;
	public static final int TYPE_TERRIENNE 	= 0;
	public static final int TYPE_VOLANTE 	= 1;
	
	
	private ArrayList<Point> chemin;
	private int indiceCourantChemin;
	
	private int type = TYPE_TERRIENNE;
	private int sante;
	private int santeMax;
	private int gainPiecesDOr;
	private double angle;
	private Thread thread;
	protected boolean enJeu;
	private double vitesse = 10;
	private boolean mort = false;
	private ArrayList<EcouteurDeCreature> ecouteursDeCreature;
	
	
	public Creature(int x, int y, int largeur, int hauteur, 
					int santeMax, int gainPiecesDOr)
	{
		super(x,y,largeur,hauteur);
		
		this.gainPiecesDOr = gainPiecesDOr;
		sante = santeMax;
		ecouteursDeCreature = new ArrayList<EcouteurDeCreature>();
	}

	public synchronized ArrayList<Point> getChemin()
	{
		return chemin;
	}

	public int getType()
	{
		return type;
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
	
	public int getGainPiecesDOr()
	{
		return gainPiecesDOr;
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
		indiceCourantChemin = 0;
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
		
		if(chemin != null && indiceCourantChemin < chemin.size())
		{
			Point p = chemin.get(indiceCourantChemin);
			
			if(x > p.getX()) 	  x--;
			else if(x < p.getX()) x++;
			
			if(y > p.getY()) 	  y--;
			else if(y < p.getY()) y++;
			
			if(x == p.getX() && y == p.getY())
				indiceCourantChemin++;
		}
	}
	
	public void run()
	{
		while(enJeu && !mort)
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

	public void blesser(int degats)
	{
		sante -= degats;
		
		if(sante <= 0)
			mourrir();
	}
	
	private void mourrir()
	{
		mort = true;
		
		// supprimer de la collection de creature
		for( EcouteurDeCreature edc : ecouteursDeCreature)
			edc.creatureTuee(this);
	}

	public void ajouterEcouteurDeCreature(EcouteurDeCreature edc)
	{
		ecouteursDeCreature.add(edc);
	}
}
