package models.maillage;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import org.jgrapht.*;
import org.jgrapht.alg.*;
import org.jgrapht.graph.*;

/**
 * Fichier : Maillage.java
 * <p>
 * Encodage : UTF-8
 * <p>
 * Cette classe...
 * <p>
 * Remarques :
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 30 nov. 2009
 * @since jdk1.6.0_16
 */
public class Maillage
{

	private final int DESACTIVE = Integer.MAX_VALUE;
	private final int LARGEUR_NOEUD = 10;
	private final int LARGEUR_EN_PIXELS;
	private final int HAUTEUR_EN_PIXELS;
	private final int NOMBRE_NOEUDS_X, NOMBRE_NOEUDS_Y;
	private SimpleWeightedGraph<Noeud, DefaultWeightedEdge> graphe;

	// Le tableau des noeuds : Noeud[x][y]
	private Noeud[][] noeuds;

	public Maillage(int largeurPixels, int hauteurPixels)
			throws IllegalArgumentException
	{
		this.LARGEUR_EN_PIXELS = largeurPixels;
		this.HAUTEUR_EN_PIXELS = hauteurPixels;

		NOMBRE_NOEUDS_X = (largeurPixels / LARGEUR_NOEUD);
		NOMBRE_NOEUDS_Y = (hauteurPixels / LARGEUR_NOEUD);

		// Initialisation du champs de noeuds
		System.out.println("Champs de noeuds de " + NOMBRE_NOEUDS_X + " x "
				+ NOMBRE_NOEUDS_Y);
		noeuds = new Noeud[NOMBRE_NOEUDS_X][NOMBRE_NOEUDS_Y];

		graphe = construireGraphe();

	}

	/**
	 * Getter pour le champ <tt>largeurPixels</tt>
	 * 
	 * @return La valeur du champ <tt>largeurPixels</tt>
	 */
	public int getLargeurPixels()
	{
		return LARGEUR_EN_PIXELS;
	}

	/**
	 * Getter pour le champ <tt>hauteurPixels</tt>
	 * 
	 * @return La valeur du champ <tt>hauteurPixels</tt>
	 */
	public int getHauteurPixels()
	{

		return HAUTEUR_EN_PIXELS;
	}

	/**
	 * DESACTIVE UNE ZONE --> IDEPENDANT DES TOURS !!!
	 * 
	 * @param rectangle
	 *            la zone a activer
	 * @throws IllegalArgumentException
	 */
	public void activerNoeuds(Rectangle rectangle)
			throws IllegalArgumentException
	{
		// TODO : activer les noeuds dans la zone donnee.
		// Cela revient à mettre un poids sensé à chaque noeud concerné.
	}

	/**
	 * DESACTIVE UNE ZONE --> IDEPENDANT DES TOURS !!!
	 * 
	 * @param rectangle
	 *            la zone a désactiver
	 * @throws IllegalArgumentException
	 */
	public void desactiverZone(Rectangle rectangle)
			throws IllegalArgumentException
	{
		// TODO : désactiver les noeuds dans la zone donnée.
		// Pour cela, une idée serait de mettre simplement le poids des noeuds
		// concernées à une valeur pseudo infinie.

	}

	/**
	 * Désactive l'ensemble des arcs du noeud.
	 * 
	 * @param noeud
	 *            Le noeud dont on désactive les arcs.
	 */
	private void desactiver(Noeud noeud)
	{
		for (DefaultWeightedEdge edge : graphe.edgesOf(noeud))
		{
			graphe.setEdgeWeight(edge, DESACTIVE);
		}
	}

	/**
	 * @param xDepart
	 * @param yDepart
	 * @param xArrivee
	 * @param yArrivee
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Point> plusCourtChemin(int xDepart, int yDepart,
			int xArrivee, int yArrivee) throws Exception
	{
		/*
		 * Test des arguments
		 */
		if (xDepart >= LARGEUR_EN_PIXELS || xArrivee >= LARGEUR_EN_PIXELS
				|| xDepart < 0 || xArrivee < 0)
			throw new IllegalArgumentException("Valeur invalide en x");
		if (yDepart >= HAUTEUR_EN_PIXELS || yArrivee >= HAUTEUR_EN_PIXELS
				|| yDepart < 0 || yArrivee < 0)
			throw new IllegalArgumentException("Valeur invalide en y");

		ArrayList<Point> chemin = new ArrayList<Point>();
		DijkstraShortestPath<Noeud, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<Noeud, DefaultWeightedEdge>(
			graphe, noeudAExact(pointA(xDepart, yDepart)),
				noeudAExact(pointA(xArrivee, yArrivee)));
		GraphPath<Noeud, DefaultWeightedEdge> dijkstraChemin = dijkstra
				.getPath();

		if (dijkstraChemin == null)
			throw new Exception("Le chemin n'existe pas!");

		for (Noeud noeud : Graphs.getPathVertexList(dijkstraChemin))
		{
			chemin.add(new Point(noeud.getX(), noeud.getY()));
		}

		return chemin;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Largeur du maillage : " + LARGEUR_EN_PIXELS + " pixels\n"
				+ "Hauteur du maillage : " + HAUTEUR_EN_PIXELS + " pixels\n"
				+ "Représentation      : 1 noeud = " + LARGEUR_NOEUD + "x"
				+ LARGEUR_NOEUD + " pixels\n"
				+
				// graphe.toString() + "\n" +
				"nombre de noeuds    : " + graphe.vertexSet().size() + "\n"
				+ "nombre d'arcs       : " + graphe.edgeSet().size();
	}

	/**
	 * @param tours
	 * @param murs
	 * @return
	 */
	private SimpleWeightedGraph<Noeud, DefaultWeightedEdge> construireGraphe()
	{
		SimpleWeightedGraph<Noeud, DefaultWeightedEdge> graphe = new SimpleWeightedGraph<Noeud, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);
		/*
		 * Ajouter les noeuds au graphe.
		 */
		for (int x = 0; x < NOMBRE_NOEUDS_Y; x++)
		{
			for (int y = 0; y < NOMBRE_NOEUDS_X; y++)
			{
				noeuds[x][y] = (new Noeud(x * LARGEUR_NOEUD
						+ (LARGEUR_NOEUD / 2), y * LARGEUR_NOEUD
						+ (LARGEUR_NOEUD / 2)));

				// TODO : C'est quoi ce machin ?
				 graphe.addVertex(noeuds[x][y]);
			}

		}

		/*
		 * Ajouter les arcs horizontaux.
		 */
		for (int y = 0; y < NOMBRE_NOEUDS_X; y++)
		{
			for (int x = 0; x < NOMBRE_NOEUDS_Y - 1; x++)
			{
				graphe.setEdgeWeight(graphe.addEdge(noeuds[x][y], // Source
						noeuds[x + 1][y]), // Arrivée
						LARGEUR_NOEUD); // Poids, en fait la distance
				/*
				 * graphe.setEdgeWeight(graphe.addEdge(noeuds.get(x + y
				 * largeurPixels / LARGEUR_NOEUD), noeuds.get(x + y
				 * (largeurPixels / LARGEUR_NOEUD) + 1)), (double)
				 * LARGEUR_NOEUD);
				 */
			}
		}

		/*
		 * Ajouter les arcs verticaux.
		 */
		for (int y = 0; y < NOMBRE_NOEUDS_X - 1; y++)
		{
			for (int x = 0; x < NOMBRE_NOEUDS_Y; x++)
			{
				graphe.setEdgeWeight(graphe.addEdge(noeuds[x][y], // Source
						noeuds[x][y + 1]), // Arrivée
						LARGEUR_NOEUD); // Poids
				/*
				 * graphe.setEdgeWeight(graphe.addEdge(noeuds.get(x + y
				 * largeurPixels / LARGEUR_NOEUD), noeuds.get(x + (y + 1) *
				 * (largeurPixels / LARGEUR_NOEUD))), (double) LARGEUR_NOEUD);
				 */
			}
		}

		/*
		 * Ajouter les arcs diagonaux descendants.
		 */
		for (int x = 0; x < NOMBRE_NOEUDS_X - 1; x++)
		{
			for (int y = 0; y < NOMBRE_NOEUDS_Y - 1; y++)
			{
				// TODO
				graphe.setEdgeWeight(graphe.addEdge(noeuds[x][y], // Source
						noeuds[x + 1][y + 1]), // Arrivée
						LARGEUR_NOEUD); // Poids
				/*
				 * graphe.setEdgeWeight(graphe.addEdge(noeuds.get(x + y
				 * largeurPixels / LARGEUR_NOEUD), noeuds.get(x + (y + 1) *
				 * (largeurPixels / LARGEUR_NOEUD) + 1)), Math .sqrt(2.0 *
				 * Math.pow((double) LARGEUR_NOEUD, 2.0)));
				 */
			}
		}

		/*
		 * Ajouter les arcs diagonaux ascendants.
		 */
		for (int x = 1; x < NOMBRE_NOEUDS_X; x++)
		{
			for (int y = 1; y < NOMBRE_NOEUDS_Y; y++)
			{
				// TODO
				/*
				graphe.setEdgeWeight(graphe.addEdge(
						noeuds[x-1][y-1], // Source
						noeuds[x][y]), // Arrivée
						LARGEUR_NOEUD); // Poids
				 * graphe.setEdgeWeight(graphe.addEdge(noeuds.get(x + y
				 * LARGEUR_EN_PIXELS / LARGEUR_NOEUD), noeuds.get(x + (y - 1) *
				 * (LARGEUR_EN_PIXELS / LARGEUR_NOEUD) + 1)), Math .sqrt(2.0 *
				 * Math.pow((double) LARGEUR_NOEUD, 2.0)));
				 */
			}
		}

		return graphe;
	}

	private Noeud noeudAExact(Point p)
	{
		return noeuds[(p.x-5)/LARGEUR_NOEUD][(p.y-5)/LARGEUR_NOEUD];
	}
	
	private Point pointA(int x, int y)
	{
		return new Point(x - (x % LARGEUR_NOEUD) + (LARGEUR_NOEUD / 2), y
				- (y % LARGEUR_NOEUD) + (LARGEUR_NOEUD / 2));
	}

	public ArrayList<Point> getNoeuds()
	{
		ArrayList<Point> points = new ArrayList<Point>();

		for (Noeud[] ligne : noeuds)
			for (Noeud noeud : ligne)
				points.add(new Point(noeud.getX(), noeud.getX()));

		return points;
	}

	public ArrayList<Line2D> getArcsActifs()
	{
		ArrayList<Line2D> retour = new ArrayList<Line2D>();

		for (DefaultWeightedEdge edge : graphe.edgeSet())
			if (graphe.getEdgeWeight(edge) != DESACTIVE)
				retour.add(new Arc(graphe.getEdgeSource(edge).getX(), graphe
						.getEdgeSource(edge).getY(), graphe.getEdgeTarget(edge)
						.getX(), graphe.getEdgeTarget(edge).getY()));

		return retour;
	}

	public ArrayList<Line2D> getArcs()
	{
		ArrayList<Line2D> retour = new ArrayList<Line2D>();

		for (DefaultWeightedEdge edge : graphe.edgeSet())
			retour.add(new Arc(graphe.getEdgeSource(edge).getX(), graphe
					.getEdgeSource(edge).getY(), graphe.getEdgeTarget(edge)
					.getX(), graphe.getEdgeTarget(edge).getY()));

		return retour;
	}

}
