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
 * Cette classe permet de représenter différentes opérations sur le maillage
 * (graphe), utilisé dans notre jeu. Elle implémente différentes opérations,
 * notamment la désactivation de zone et surtout le calcule du chemin le plus
 * cours d'une maille à une autre.
 * <p>
 * Remarques : -
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 30 nov. 2009
 * @since jdk1.6.0_16
 */
public class Maillage
{
	// Pour représenter un poids d'un arc non praticable
	private final int DESACTIVE;
	// La largeur en pixel de chaque maille, ou noeud
	private final int LARGEUR_NOEUD;

	// TODO comment
	private final int POIDS_DIAGO;

	private final int DEMI_NOEUD;
	// La largeur en pixel totale du maillage (axe des x)
	private final int LARGEUR_EN_PIXELS;
	// La hauteur en pixel totale du maillage (axe des y)
	private final int HAUTEUR_EN_PIXELS;
	// Les dimensions en maille (ou noeuds) du maillage
	private final int NOMBRE_NOEUDS_X, NOMBRE_NOEUDS_Y;
	// Le graphe
	private SimpleWeightedGraph<Noeud, Arc> graphe;
	// Le tableau des noeuds : Noeud[x][y]
	private Noeud[][] noeuds;

	// Le decalage de base.
	private int xOffset, yOffset;

	/**
	 * Un maillage dynamique représentant une aire de jeu.
	 * 
	 * @param largeurPixels
	 *            Largeur en pixel de la zone.
	 * @param hauteurPixels
	 *            Hauteur en pixel de la zone.
	 * @param largeurDuNoeud
	 *            La largeur en pixel de chaque maille.
	 * @throws IllegalArgumentException
	 *             Levé si les dimensions ne correspondent pas.
	 */
	public Maillage(final int largeurPixels, final int hauteurPixels,
			final int largeurDuNoeud, int xOffset, int yOffset)
			throws IllegalArgumentException
	{
		// Assignation de la largeur du noeud (ou de la maille).
		LARGEUR_NOEUD = largeurDuNoeud;

		// TODO comment
		POIDS_DIAGO = (int) Math.sqrt(2 * LARGEUR_NOEUD * LARGEUR_NOEUD);

		// Largeur du demi noeud
		DEMI_NOEUD = LARGEUR_NOEUD / 2;

		// Assignation de la dimension en pixel unitaire du maillage
		LARGEUR_EN_PIXELS = largeurPixels;
		HAUTEUR_EN_PIXELS = hauteurPixels;

		// Conversion en dimension en maille.
		NOMBRE_NOEUDS_X = (largeurPixels / LARGEUR_NOEUD);
		NOMBRE_NOEUDS_Y = (hauteurPixels / LARGEUR_NOEUD);

		// Calcul du poids maximum pouvant avoir le graphe
		DESACTIVE = 8 * NOMBRE_NOEUDS_X * NOMBRE_NOEUDS_Y * LARGEUR_NOEUD + 1;

		// Les offsets du décalage
		this.xOffset = xOffset;
		this.yOffset = yOffset;

		// Initialisation du champs de noeuds
		noeuds = new Noeud[NOMBRE_NOEUDS_X][NOMBRE_NOEUDS_Y];

		// Construction du graphe
		graphe = construireGraphe();
	}
	
	/**
	 * @param largeurPixels
	 * @param hauteurPixels
	 * @param largeurDuNoeud
	 * @throws IllegalArgumentException
	 */
	public Maillage(final int largeurPixels, final int hauteurPixels,
			final int largeurDuNoeud)
			throws IllegalArgumentException
	{
		this(largeurPixels,hauteurPixels,largeurDuNoeud,0,0);
	}

	/**
	 * @param xDepart
	 * @param yDepart
	 * @param xArrivee
	 * @param yArrivee
	 * @return
	 * @throws PathNotFoundException
	 */
	public ArrayList<Point> plusCourtChemin(int xDepart, int yDepart,
			int xArrivee, int yArrivee) throws PathNotFoundException
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

		/*
		 * Calcul par Dijkstra du chemin le plus cours d'un point à un autre.
		 */
		GraphPath<Noeud, Arc> dijkstraChemin = (new DijkstraShortestPath<Noeud, Arc>(
				graphe, noeudAExact(pointA(xDepart, yDepart)),
				noeudAExact(pointA(xArrivee, yArrivee)))).getPath();

		/*
		 * S'il n'y a pas de chemin
		 */
		if (dijkstraChemin == null)
			throw new PathNotFoundException("Le chemin n'existe pas!");
		if (dijkstraChemin.getWeight() >= DESACTIVE)
			throw new PathNotFoundException("Il n'existe aucun chemin valide.");

		return new ArrayList<Point>(Graphs.getPathVertexList(dijkstraChemin));
	}

	/**
	 * Active UNE ZONE --> IDEPENDANT DES TOURS !!!
	 * 
	 * @param rectangle
	 *            la zone a activer
	 * @throws IllegalArgumentException
	 */
	public void activerZone(Rectangle rectangle)
			throws IllegalArgumentException
	{
		zoneActive(rectangle, true);
	}

	/**
	 * DESACTIVE UNE ZONE --> IDEPENDANT DES TOURS !!!
	 * 
	 * @param rectangle
	 *            la zone a désactiver, coordonnées en pixels unitaire
	 * @throws IllegalArgumentException
	 *             Levé si le rectangle est hors champs.
	 */
	public void desactiverZone(Rectangle rectangle)
			throws IllegalArgumentException
	{
		zoneActive(rectangle, false);
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
				"Nombre de noeuds    : " + graphe.vertexSet().size() + "\n"
				+ "Nombre d'arcs       : " + graphe.edgeSet().size();
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

	public ArrayList<Noeud> getNoeuds()
	{
		ArrayList<Noeud> points = new ArrayList<Noeud>();

		for (Noeud[] ligne : noeuds)
			for (Noeud noeud : ligne)
				points.add(noeud);

		return points;
	}

	public ArrayList<Line2D> getArcsActifs()
	{
		ArrayList<Line2D> retour = new ArrayList<Line2D>();

		for (Arc edge : graphe.edgeSet())
			if (graphe.getEdgeWeight(edge) != DESACTIVE)
				retour.add(edge.toLine2D());

		return retour;
	}

	public ArrayList<Line2D> getArcs()
	{
		ArrayList<Line2D> retour = new ArrayList<Line2D>();

		for (Arc edge : graphe.edgeSet())
			retour.add(edge.toLine2D());

		return retour;
	}

	/**
	 * @param rectangle
	 * @param active
	 * @throws IllegalArgumentException
	 */
	private void zoneActive(final Rectangle rectangle, final boolean active)
			throws IllegalArgumentException
	{
		/*
		 * Vérification de la validité du rectangle
		 */
		rectangleEstDansLeTerrain(rectangle);

		/*
		 * for (int i = pixelToNoeud(rectangle.x); i < pixelToNoeud(rectangle.x
		 * + rectangle.width); ++i) for (int j = pixelToNoeud(rectangle.y); j <
		 * pixelToNoeud(rectangle.y + rectangle.height); ++j) if (active)
		 * activer(noeuds[i][j]); else desactiver(noeuds[i][j]);
		 */
		// Correction des arcs diagonaux
		// TODO
		for (Noeud[] ligne : noeuds)
			for (Noeud noeud : ligne)
			{
				Rectangle rec = new Rectangle(noeud.x - DEMI_NOEUD, noeud.y
						- DEMI_NOEUD, LARGEUR_NOEUD, LARGEUR_NOEUD);
				if (rectangle.intersects(rec))
					if (active)
						activer(noeud);
					else
						desactiver(noeud);
			}
	}

	/**
	 * Active l'ensemble des arcs d'un noeud.
	 * 
	 * @param noeud
	 *            Le noeud ton on active les arcs.
	 */
	private void activer(Noeud noeud)
	{
		noeud.setActif(true);
		for (Arc edge : graphe.edgesOf(noeud))
			if (edge.getArrivee().isActif() && edge.getDepart().isActif())
				graphe.setEdgeWeight(edge, LARGEUR_NOEUD);
	}

	/**
	 * Désactive l'ensemble des arcs du noeud.
	 * 
	 * @param noeud
	 *            Le noeud dont on désactive les arcs.
	 */
	private void desactiver(Noeud noeud)
	{
		noeud.setActif(false);
		for (Arc edge : graphe.edgesOf(noeud))
			graphe.setEdgeWeight(edge, DESACTIVE);
	}

	private SimpleWeightedGraph<Noeud, Arc> construireGraphe()
	{
		SimpleWeightedGraph<Noeud, Arc> graphe = new SimpleWeightedGraph<Noeud, Arc>(
				new GenerateurDArcs());
		/*
		 * Ajouter les noeuds au graphe.
		 */
		for (int x = 0; x < NOMBRE_NOEUDS_X; x++)
		{
			for (int y = 0; y < NOMBRE_NOEUDS_Y; y++)
			{
				// Nouveau noeud avec sa position x,y en pixel
				noeuds[x][y] = new Noeud( // Calcul des points
						(x) * LARGEUR_NOEUD + DEMI_NOEUD + xOffset, // Pixels en x
						(y) * LARGEUR_NOEUD + DEMI_NOEUD + yOffset);// Pixels en y

				graphe.addVertex(noeuds[x][y]);
			}
		}

		/*
		 * Ajouter les arcs horizontaux.
		 */
		for (int y = 0; y < NOMBRE_NOEUDS_Y; y++)
		{
			for (int x = 0; x < NOMBRE_NOEUDS_X - 1; x++)
			{
				graphe.setEdgeWeight(graphe.addEdge(noeuds[x][y], // Source
						noeuds[x + 1][y]), // Arrivée
						LARGEUR_NOEUD); // Poids, en fait la distance

			}
		}

		/*
		 * Ajouter les arcs verticaux.
		 */
		for (int y = 0; y < NOMBRE_NOEUDS_Y - 1; y++)
		{
			for (int x = 0; x < NOMBRE_NOEUDS_X; x++)
			{
				graphe.setEdgeWeight(graphe.addEdge(noeuds[x][y], // Source
						noeuds[x][y + 1]), // Arrivée
						LARGEUR_NOEUD); // Poids
			}
		}

		/*
		 * Ajouter les arcs diagonaux descendants.
		 */
		for (int x = 0; x < NOMBRE_NOEUDS_X - 1; x++)
		{
			for (int y = 0; y < NOMBRE_NOEUDS_Y - 1; y++)
			{
				graphe.setEdgeWeight(graphe.addEdge(noeuds[x][y], // Source
						noeuds[x + 1][y + 1]), // Arrivée
						POIDS_DIAGO); // Poids
			}
		}

		/*
		 * Ajouter les arcs diagonaux ascendants.
		 */
		for (int x = 0; x < NOMBRE_NOEUDS_X - 1; x++)
		{
			for (int y = 1; y < NOMBRE_NOEUDS_Y; y++)
			{
				graphe.setEdgeWeight(graphe.addEdge(noeuds[x][y], // Source
						noeuds[x + 1][y - 1]), // Arrivée
						POIDS_DIAGO); // Poids
			}
		}

		return graphe;
	}

	private Noeud noeudAExact(Point p)
	{
		return noeuds[pixelToNoeud(p.x)][pixelToNoeud(p.y)];
	}

	private Point pointA(int x, int y)
	{
		return new Point( // Calcul du point
				x - (x % LARGEUR_NOEUD) + DEMI_NOEUD, // Coord x
				y - (y % LARGEUR_NOEUD) + DEMI_NOEUD); // Coord y
	}

	private int pixelToNoeud(int n)
	{
		return (n - DEMI_NOEUD) / LARGEUR_NOEUD;
	}

	private void rectangleEstDansLeTerrain(Rectangle rectangle)
			throws IllegalArgumentException
	{
		if (rectangle.getX() < 0 || rectangle.getY() < 0)
			throw new IllegalArgumentException("Origine trop petite");

		if (rectangle.getX() + rectangle.getWidth() > LARGEUR_EN_PIXELS)
			throw new IllegalArgumentException("Largeur hors cadre");

		if (rectangle.getY() + rectangle.getHeight() > HAUTEUR_EN_PIXELS)
			throw new IllegalArgumentException("Hauteur hors cadre");
	}
}
