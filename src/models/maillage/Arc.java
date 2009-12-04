/**
 * Fichier : Line.java
 * 
 * <p> But : 
 * <p> Remarques : 
 * 
 * @author Pierre-Dominique Putallaz
 * @version 4 déc. 2009
 * @since jdk1.6.0_16 
 */
package models.maillage;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Arc extends Line2D
{
	private int x1,x2,y1,y2;
	
	public Arc(int xDepart,int yDepart,int xArrivee,int yArrivee) {
		super();
		x1 = xDepart;
		x2 = xArrivee;
		y1 = yDepart;
		y2 = yArrivee;
		
	}

	/* (non-Javadoc)
	 * @see java.awt.geom.Line2D#getP1()
	 */
	@Override
	public Point2D getP1()
	{
		return new Point(x1, y1);
	}

	/* (non-Javadoc)
	 * @see java.awt.geom.Line2D#getP2()
	 */
	@Override
	public Point2D getP2()
	{
		return new Point(x2,y2);
	}

	/* (non-Javadoc)
	 * @see java.awt.geom.Line2D#getX1()
	 */
	@Override
	public double getX1()
	{
		return x1;
	}

	/* (non-Javadoc)
	 * @see java.awt.geom.Line2D#getX2()
	 */
	@Override
	public double getX2()
	{
		return x2;
	}

	/* (non-Javadoc)
	 * @see java.awt.geom.Line2D#getY1()
	 */
	@Override
	public double getY1()
	{
		return y1;
	}

	/* (non-Javadoc)
	 * @see java.awt.geom.Line2D#getY2()
	 */
	@Override
	public double getY2()
	{
		return y2;
	}

	/* (non-Javadoc)
	 * @see java.awt.geom.Line2D#setLine(double, double, double, double)
	 */
	@Override
	public void setLine(double x1, double y1, double x2, double y2)
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.Shape#getBounds2D()
	 */
	@Override
	public Rectangle2D getBounds2D()
	{
		return new Rectangle(x1, y1, x2-x1, y2-y1);
	}

}
