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

import org.jgrapht.graph.DefaultWeightedEdge;

public class Arc extends DefaultWeightedEdge
{
	Point depart, arrivee;
	public Arc(Point depart, Point arrivee)
	{
		this.depart = depart;
		this.arrivee = arrivee;
	}

	public Line2D toLine2D()
	{
		return new Line2D()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.geom.Line2D#getP1()
			 */
			@Override
			public Point2D getP1()
			{
				return depart;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.geom.Line2D#getP2()
			 */
			@Override
			public Point2D getP2()
			{
				return arrivee;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.geom.Line2D#getX1()
			 */
			@Override
			public double getX1()
			{
				return depart.x;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.geom.Line2D#getX2()
			 */
			@Override
			public double getX2()
			{
				return arrivee.x;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.geom.Line2D#getY1()
			 */
			@Override
			public double getY1()
			{
				return depart.y;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.geom.Line2D#getY2()
			 */
			@Override
			public double getY2()
			{
				return arrivee.y;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.geom.Line2D#setLine(double, double, double, double)
			 */
			@Override
			public void setLine(double x1, double y1, double x2, double y2)
			{
				// TODO Auto-generated method stub

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.Shape#getBounds2D()
			 */
			@Override
			public Rectangle2D getBounds2D()
			{
				return null;
			}
		};
	}
}
