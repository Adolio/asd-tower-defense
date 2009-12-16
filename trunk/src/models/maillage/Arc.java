package models.maillage;

import java.awt.geom.*;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * Fichier : Arc.java
 * 
 * <p>
 * But : Modélise un arc du graphe, en étendant un {@link DefaultWeightedEdge}.
 * <p>
 * Remarques : Pour avoir une {@link Line2D} de l'arc il faut utiliser la
 * méthode dédiée.
 * 
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @author Pierre-Dominique Putallaz
 * @version 4 déc. 2009
 * @since jdk1.6.0_16
 */
public class Arc extends DefaultWeightedEdge
{
	// Les deux noeuds de départ et d'arrivée.
	Noeud depart, arrivee;

	/**
	 * Construit un arc pondéré non orienté avec les noeuds de départs et
	 * d'arrivée.
	 * 
	 * @param depart
	 *            Le noeud de départ
	 * @param arrivee
	 *            Le noeud d'arrivée
	 */
	public Arc(Noeud depart, Noeud arrivee)
	{
		this.depart = depart;
		this.arrivee = arrivee;
	}

	/**
	 * Retourne une représentation sous forme de {@link Line2D} de l'arc.
	 * 
	 * @return une représentation sous forme de {@link Line2D} de l'arc.
	 */
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
				throw new IllegalArgumentException("Méthode non implémentée");

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.awt.Shape#getBounds2D()
			 */
			@Override
			public Rectangle2D getBounds2D()
			{
				throw new IllegalArgumentException("Méthode non implémentée");
			}
		};
	}

	/**
	 * Retourne le noeud de départ.
	 * 
	 * @return le noeud de départ.
	 */
	public Noeud getDepart()
	{
		return depart;
	}

	/**
	 * Retourne le noeud d'arrivée.
	 * @return le noeud d'arrivée.
	 */
	public Noeud getArrivee()
	{
		return arrivee;
	}
}
