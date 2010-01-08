package models.maillage;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.SimpleWeightedGraph;

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
	/*
	 * Constantes
	 */
	/**
	 * La largeur en pixel de chaque maille, ou noeud
	 */
	private final int LARGEUR_NOEUD;
	/**
	 * Le poid d'un arc diagonal
	 */
	private final int POIDS_DIAGO;
	/**
	 * La demi-distance entre un point et un autre
	 */
	private final int DEMI_NOEUD;
	/**
	 * La largeur en pixel totale du maillage (axe des x)
	 */
	private final int LARGEUR_EN_PIXELS;
	/**
	 * La hauteur en pixel totale du maillage (axe des y)
	 */
	private final int HAUTEUR_EN_PIXELS;
	/**
	 * Les dimensions en maille (ou noeuds) du maillage
	 */
	private final int NOMBRE_NOEUDS_X, NOMBRE_NOEUDS_Y;

	/*
	 * Attributs
	 */
	/**
	 * Le graphe
	 */
	private SimpleWeightedGraph<Noeud, Arc> graphe = new SimpleWeightedGraph<Noeud, Arc>(
			new GenerateurDArcs());;
	/**
	 * Le tableau des noeuds : Noeud[x][y]
	 */
	private Noeud[][] noeuds;
	/**
	 * Le decalage de base.
	 */
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
	 * @param xOffset
	 *            Le décalage en x du maillage, en pixels.
	 * @param yOffset
	 *            Le décalage en y du maillage, en pixels.
	 * @throws IllegalArgumentException
	 *             Levé si les dimensions ne correspondent pas.
	 */
	public Maillage(final int largeurPixels, final int hauteurPixels,
			final int largeurDuNoeud, int xOffset, int yOffset)
			throws IllegalArgumentException
	{
		// Assignation de la largeur du noeud (ou de la maille).
		LARGEUR_NOEUD = largeurDuNoeud;

		// Calcule une fois pour toute la distance diagonale
		POIDS_DIAGO = (int) Math.sqrt(2 * LARGEUR_NOEUD * LARGEUR_NOEUD);

		// Largeur du demi noeud
		DEMI_NOEUD = LARGEUR_NOEUD / 2;

		// Assignation de la dimension en pixel unitaire du maillage
		LARGEUR_EN_PIXELS = largeurPixels;
		HAUTEUR_EN_PIXELS = hauteurPixels;

		// Conversion en dimension en maille.
		NOMBRE_NOEUDS_X = (largeurPixels / LARGEUR_NOEUD);
		NOMBRE_NOEUDS_Y = (hauteurPixels / LARGEUR_NOEUD);

		// Les offsets du décalage
		this.xOffset = xOffset;
		this.yOffset = yOffset;

		// Initialisation du champs de noeuds
		noeuds = new Noeud[NOMBRE_NOEUDS_X][NOMBRE_NOEUDS_Y];

		// Construction du graphe
		construireGraphe();
	}

	/**
	 * 
	 * @param largeurPixels
	 * @param hauteurPixels
	 * @param largeurDuNoeud
	 * @throws IllegalArgumentException
	 */
	public Maillage(final int largeurPixels, final int hauteurPixels,
			final int largeurDuNoeud) throws IllegalArgumentException
	{
		this(largeurPixels, hauteurPixels, largeurDuNoeud, 0, 0);
	}

	/**
	 * Méthode public pour calculer le chemin le plus court d'un point à un
	 * autre. Les points sont données en pixels exactes dans le champs.
	 * 
	 * @param xDepart
	 *            La coordonnée x du point de départ.
	 * @param yDepart
	 *            La coordonnée y du point de départ.
	 * @param xArrivee
	 *            La coordonnée x du point d'arrivée.
	 * @param yArrivee
	 *            La coordonnée y du point d'arrivée.
	 * @return Une liste de points qui représente le chemin à parcourir.
	 * @throws PathNotFoundException
	 *             Levé si aucun chemin n'est trouvé.
	 * @throws IllegalArgumentException
	 *             Levé si les coordonnées ne sont pas dans le champs.
	 */
	public synchronized ArrayList<Point> plusCourtChemin(int xDepart,
			int yDepart, int xArrivee, int yArrivee)
			throws PathNotFoundException, IllegalArgumentException
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
		GraphPath<Noeud, Arc> dijkstraChemin;
		try
		{
			dijkstraChemin = (new DijkstraShortestPath<Noeud, Arc>(
					graphe,
					noeudContenantLePoint(xDepart - xOffset, yDepart - yOffset),
					noeudContenantLePoint(xArrivee - xOffset, yArrivee
							- yOffset))).getPath();
		} catch (IllegalArgumentException e)
		{
			// Mise à null du chemin en cas d'erreur
			return null;
		}
		/*
		 * S'il n'y a pas de chemin
		 */
		if (dijkstraChemin == null)
			throw new PathNotFoundException("Le chemin n'existe pas!");

		// Retourne l'ArrayList des points.
		return new ArrayList<Point>(Graphs.getPathVertexList(dijkstraChemin));
	}

	/**
	 * Active une zone rectangulaire dans le champs.
	 * 
	 * @param rectangle
	 *            La zone a activer
	 * @throws IllegalArgumentException
	 *             Si la zone à activer est hors champs.
	 */
	synchronized public void activerZone(Rectangle rectangle)
			throws IllegalArgumentException
	{
		zoneActive(rectangle, true);
	}

	/**
	 * Désactive une zone dans le champs
	 * 
	 * @param rectangle
	 *            la zone a désactiver.
	 * @throws IllegalArgumentException
	 *             Levé si le rectangle est hors champs.
	 */
	synchronized public void desactiverZone(Rectangle rectangle)
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
				+ LARGEUR_NOEUD + " pixels\n" + "Nombre de noeuds    : "
				+ graphe.vertexSet().size() + "\n" + "Nombre d'arcs       : "
				+ graphe.edgeSet().size() + "\nDécalage           : X="
				+ xOffset + " Y=" + yOffset;
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
	 * Retourne la liste complète des noeuds du maillage.
	 * 
	 * @return Les noeuds du maillage.
	 */
	public ArrayList<Noeud> getNoeuds()
	{
		ArrayList<Noeud> points = new ArrayList<Noeud>();
		for (Noeud[] ligne : noeuds)
			for (Noeud noeud : ligne)
				points.add(noeud);
		return points;
	}

	/**
	 * Retourne la liste complète des arcs du maillage.
	 * 
	 * @return La liste complète des arcs du maillage.
	 */
	public ArrayList<Line2D> getArcs()
	{
		ArrayList<Line2D> retour = new ArrayList<Line2D>();
		for (Arc edge : graphe.edgeSet())
			retour.add(edge.toLine2D());
		return retour;
	}

	/**
	 * Méthode de service pour activer ou désactiver une zone.
	 * 
	 * @param rectangle
	 *            La zone concernée.
	 * @param active
	 *            True s'il faut l'activer, False s'il faut la désactiver.
	 * @throws IllegalArgumentException
	 *             Levé si la zone est hors champs.
	 */
	private void zoneActive(final Rectangle rectangle, final boolean active)
			throws IllegalArgumentException
	{
		// Vérification de la validité du rectangle
		rectangleEstDansLeTerrain(rectangle);

		/*
		 * Pour chaque noeuds on vérifie s'il intersect avec la zone concernée.
		 */
		for (Noeud[] ligne : noeuds)
			for (Noeud noeud : ligne)
			{
				if (rectangle.intersects(new Rectangle(noeud.x - DEMI_NOEUD,
						noeud.y - DEMI_NOEUD, LARGEUR_NOEUD, LARGEUR_NOEUD)))
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
	 *            Le noeud dont on active les arcs.
	 */
	private void activer(Noeud noeud)
	{
		// Vérifie si le noeud n'est pas null
		if (noeud == null)
			throw new IllegalArgumentException(
					"Le noeud passé en paramêtre est null");
		// Activation du noeud
		noeud.setActif(true);
		// Replanter le noeud dans le graphe, s'il n'est pas déjà présent
		graphe.addVertex(noeud);

		/*
		 * Ajouter les arcs manquants
		 */
		int[] xy = Noeud.coordonnee(noeud, xOffset, yOffset);
		int x, y;
		Noeud cible;
		Arc arc;
		for (int i = -1; i <= 1; i++)
		{
			x = xy[0] + i;
			// Si le noeud n'est pas hors cadre
			if (x < 0 || x >= noeuds.length)
				continue;
			for (int j = -1; j <= 1; j++)
			{
				y = xy[1] + j;
				// Si le noeud n'est pas hors cadre
				if (y < 0 || y >= noeuds[x].length)
					continue;
				/*
				 * Extraction de la cible
				 */
				cible = noeuds[x][y];
				if (cible == null)
					throw new IllegalArgumentException(
							"Le noeud ciblé ne peut pas être nul");
				// Si le noeud cible n'est pas actif ou s'il s'agit du noeud
				// courant
				if (!cible.isActif() || cible.equals(noeud))
					continue;
				// Ajout du noeud à l'ensemble. La méthode test si le noeud est
				// déjà présent
				graphe.addVertex(cible);
				// Calcul du nouvel arc
				arc = graphe.addEdge(noeud, cible);
				// Ajout du poids à l'arc
				graphe.setEdgeWeight(arc,
						(Math.abs(i) != Math.abs(j)) ? LARGEUR_NOEUD
								: POIDS_DIAGO);
			}
		}
	}

	/**
	 * Désactive l'ensemble des arcs du noeud.
	 * 
	 * @param noeud
	 *            Le noeud dont on désactive les arcs.
	 */
	private void desactiver(Noeud noeud)
	{
		// Désactivation du noeud
		noeud.setActif(false);
		// Supprimer le noeud ainsi que tous les arcs relatifs
		graphe.removeVertex(noeud);
	}

	private void construireGraphe()
	{
		/*
		 * Ajouter les noeuds au graphe.
		 */
		for (int x = 0; x < NOMBRE_NOEUDS_X; x++)
			for (int y = 0; y < NOMBRE_NOEUDS_Y; y++)
				noeuds[x][y] = new Noeud((x * LARGEUR_NOEUD) + xOffset,
						(y * LARGEUR_NOEUD) + yOffset, LARGEUR_NOEUD);

		/*
		 * Active tout les noeuds (et calcul les vertex)
		 */
		for (Noeud[] ligne : noeuds)
			for (Noeud noeud : ligne)
				activer(noeud);
	}

	/**
	 * Méthode de service pour faire la relation Point -> Noeud.
	 * 
	 * Retourne le noeud actif le plus proche de la position x,y.
	 * 
	 * @param p
	 *            Le point à chercher
	 * @return Le noeud correspondant.
	 */
	private Noeud noeudContenantLePoint(int x, int y)
	{
		return noeuds[Noeud.pixelANodale(x, LARGEUR_NOEUD)][Noeud.pixelANodale(
				y, LARGEUR_NOEUD)];
	}

	/**
	 * Méthode de service pour tester si le rectangle passé en paramètre est
	 * dans le champs.
	 * 
	 * @param rectangle
	 *            Le rectangle à tester.
	 * @throws IllegalArgumentException
	 *             Levé si le rectangle est hors chamos.
	 */
	private void rectangleEstDansLeTerrain(Rectangle rectangle)
			throws IllegalArgumentException
	{
		if (rectangle.getX() + rectangle.getWidth() > LARGEUR_EN_PIXELS)
			throw new IllegalArgumentException("Largeur hors cadre");
		if (rectangle.getY() + rectangle.getHeight() > HAUTEUR_EN_PIXELS)
			throw new IllegalArgumentException("Hauteur hors cadre");
	}
}
