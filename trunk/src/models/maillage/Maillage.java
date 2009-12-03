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
    private int largeurPixels;
    private int hauteurPixels;
    private ArrayList<Noeud> noeuds;
    private SimpleWeightedGraph<Noeud, DefaultWeightedEdge> graphe;
    
    /**
     * @param largeurPixels
     * @param hauteurPixels
     * @param tours
     * @param murs
     * @throws IllegalArgumentException
     */
    public Maillage(int largeurPixels,
                    int hauteurPixels)
    throws IllegalArgumentException
    {
        this.largeurPixels = largeurPixels;
        this.hauteurPixels = hauteurPixels;
        graphe = construireGraphe();
    }
    
    /**
     * Getter pour le champ <tt>largeurPixels</tt>
     * 
     * @return La valeur du champ <tt>largeurPixels</tt>
     */
    public int getLargeurPixels()
    {
        return largeurPixels;
    }
    
    /**
     * Getter pour le champ <tt>hauteurPixels</tt>
     * 
     * @return La valeur du champ <tt>hauteurPixels</tt>
     */
    public int getHauteurPixels()
    {
        return hauteurPixels;
    }
    
    /**
     * Setter pour le champ <tt>largeurPixels</tt>
     * 
     * @param largeurPixels
     *            La valeur qu'on veut attribuer au champ <tt>largeurPixels</tt>
     */
    public void setLargeurPixels(int largeurPixels)
    {
        this.largeurPixels = largeurPixels;
    }
    
    /**
     * Setter pour le champ <tt>hauteurPixels</tt>
     * 
     * @param hauteurPixels
     *            La valeur qu'on veut attribuer au champ <tt>hauteurPixels</tt>
     */
    public void setHauteurPixels(int hauteurPixels)
    {
        this.hauteurPixels = hauteurPixels;
    }
    
    /**
     * DESACTIVE UNE ZONE --> IDEPENDANT DES TOURS !!!
     * 
     * @param rectangle
     *            la zone a activer
     * @throws IllegalArgumentException
     */
    public void activerNoeuds(Rectangle rectangle) throws IllegalArgumentException
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
    public void desactiverZone(Rectangle rectangle) throws IllegalArgumentException
    {
        // TODO : désactiver les noeuds dans la zone donnée.
        // Pour cela, une idée serait de mettre simplement le poids des noeuds
        // concernées à une valeur pseudo infinie.

		for (int i = indexOfNoeudAExact(pointA(rectangle.x, rectangle.y)); i <= rectangle.width
				/ LARGEUR_NOEUD; i += LARGEUR_NOEUD)
			for (int j = i; j <= rectangle.height / LARGEUR_NOEUD; j += (largeurPixels / LARGEUR_NOEUD))
				desactiver(noeuds.get(j));
    }
    
    /**
     * Désactive l'ensemble des arcs du noeud.
     * @param noeud Le noeud dont on désactive les arcs.
     */
    private void desactiver(Noeud noeud){
    	for(DefaultWeightedEdge edge : graphe.edgesOf(noeud)){
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
                                            int xArrivee, int yArrivee) throws Exception {

		/*
		 * Test des arguments
		 */
		if (xDepart >= largeurPixels || xArrivee >= largeurPixels
				|| xDepart < 0 || xArrivee < 0)
			throw new IllegalArgumentException("Valeur invalide en x");
		if (yDepart >= hauteurPixels || yArrivee >= hauteurPixels || yDepart < 0
				|| yArrivee < 0)
			throw new IllegalArgumentException("Valeur invalide en y");
    	
    	ArrayList<Point> chemin = new ArrayList<Point>();
        DijkstraShortestPath<Noeud, DefaultWeightedEdge> dijkstra =
            new DijkstraShortestPath<Noeud, DefaultWeightedEdge>
                (graphe,
                 noeudAExact(pointA(xDepart, yDepart)),
                 noeudAExact(pointA(xArrivee, yArrivee)));
        GraphPath<Noeud, DefaultWeightedEdge> dijkstraChemin = dijkstra.getPath();
        
        if(dijkstraChemin == null)
            throw new Exception("Le chemin n'existe pas!");
        
        for (Noeud noeud : Graphs.getPathVertexList(dijkstraChemin)) {
            chemin.add(new Point(noeud.getX(), noeud.getY()));
        }
        
        return chemin;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Largeur du maillage : " + largeurPixels + " pixels\n" +
               "Hauteur du maillage : " + hauteurPixels + " pixels\n" +
               "Représentation      : 1 noeud = " + LARGEUR_NOEUD + "x" +
                                      LARGEUR_NOEUD + " pixels\n" +
               //graphe.toString() + "\n" +
               "nombre de noeuds    : " + graphe.vertexSet().size() + "\n" +
               "nombre d'arcs       : " + graphe.edgeSet().size();
    }
    
    /**
     * @param tours
     * @param murs
     * @return
     */
    private SimpleWeightedGraph<Noeud,DefaultWeightedEdge> construireGraphe()
    {
    	SimpleWeightedGraph<Noeud, DefaultWeightedEdge> graphe =
            new SimpleWeightedGraph<Noeud, DefaultWeightedEdge>
                                                        (DefaultWeightedEdge.class);
        noeuds = new ArrayList<Noeud>();
        
        /*
         * Ajouter les noeuds au graphe.
         */
        for (int y=0; y<hauteurPixels; y+=LARGEUR_NOEUD) {
            for (int x=0; x<largeurPixels; x+=LARGEUR_NOEUD) {
                noeuds.add(new Noeud(x + (LARGEUR_NOEUD / 2),
                                     y + (LARGEUR_NOEUD / 2)));
                graphe.addVertex(noeuds.get(noeuds.size() - 1));
            }
        }
        
        /*
         * Ajouter les arcs horizontaux.
         */
        for (int y=0; y<hauteurPixels / LARGEUR_NOEUD; y++) {
            for (int x=0; x<largeurPixels / LARGEUR_NOEUD - 1; x++) {
                graphe.setEdgeWeight(
                    graphe.addEdge(
                        noeuds.get(x + y * largeurPixels / LARGEUR_NOEUD),
                        noeuds.get(x + y * (largeurPixels / LARGEUR_NOEUD) + 1)
                    ),
                    (double)LARGEUR_NOEUD
                );
            }
        }
        
        /*
         * Ajouter les arcs verticaux.
         */
        for (int y=0; y<hauteurPixels / LARGEUR_NOEUD - 1; y++) {
            for (int x=0; x<largeurPixels / LARGEUR_NOEUD; x++) {
                graphe.setEdgeWeight(
                    graphe.addEdge(
                        noeuds.get(x + y * largeurPixels / LARGEUR_NOEUD),
                        noeuds.get(x + (y + 1) * (largeurPixels / LARGEUR_NOEUD))
                    ),
                    (double)LARGEUR_NOEUD
                );
            }
        }
        
        /*
         * Ajouter les arcs diagonaux descendants.
         */
        for (int x=0; x<largeurPixels / LARGEUR_NOEUD - 1; x++) {
            for (int y=0; y<hauteurPixels / LARGEUR_NOEUD - 1; y++) {
                graphe.setEdgeWeight(
                        graphe.addEdge(
                            noeuds.get(x + y * largeurPixels / LARGEUR_NOEUD),
                            noeuds.get(x + (y+1)*(largeurPixels / LARGEUR_NOEUD) + 1)
                        ),
                        Math.sqrt(2.0 * Math.pow((double)LARGEUR_NOEUD, 2.0))
                );
            }
        }
        
        /*
         * Ajouter les arcs diagonaux ascendants.
         */
        for (int x=0; x<largeurPixels / LARGEUR_NOEUD - 1; x++) {
            for (int y=1; y<hauteurPixels / LARGEUR_NOEUD; y++) {
                graphe.setEdgeWeight(
                        graphe.addEdge(
                            noeuds.get(x + y * largeurPixels / LARGEUR_NOEUD),
                            noeuds.get(x + (y-1)*(largeurPixels / LARGEUR_NOEUD) + 1)
                        ),
                        Math.sqrt(2.0 * Math.pow((double)LARGEUR_NOEUD, 2.0))
                );
            }
        }
        
        return graphe;
    }
    
    /**
     * 
     * @param p
     * @return
     */
    private Noeud noeudAExact(Point p) {
        return noeuds.get(indexOfNoeudAExact(p));
    }
    
	private int indexOfNoeudAExact(Point p)
	{
		return (p.y - 1) / LARGEUR_NOEUD * (largeurPixels / LARGEUR_NOEUD)
				+ (p.x - 1) / LARGEUR_NOEUD;
	}
    
    /**
     * 
     * @param x
     * @param y
     * @return
     */
	private Point pointA(int x, int y)
	{
		return new Point(
				x - (x % LARGEUR_NOEUD) + (LARGEUR_NOEUD / 2), 
				y - (y % LARGEUR_NOEUD) + (LARGEUR_NOEUD / 2));
	}

    /**
     * @return
     */
    public ArrayList<Point> getNoeuds()
    {
        ArrayList<Point> points = new ArrayList<Point>();
        
        for (Noeud noeud : noeuds) {
            points.add(new Point(noeud.getX(), noeud.getX()));
        }
        return points;
    }
    
    public ArrayList<Line2D> getArcsActifs(){
    	ArrayList<Line2D> retour = new ArrayList<Line2D>();
    	
    	for(DefaultWeightedEdge edge : graphe.edgeSet()){
    		if(graphe.getEdgeWeight(edge) != DESACTIVE) 
    			{
    			int x_depart = graphe.getEdgeSource(edge).getX();
    			int y_depart = graphe.getEdgeSource(edge).getY();
    			int x_arrivee = graphe.getEdgeTarget(edge).getX();
    			int y_arrivee = graphe.getEdgeTarget(edge).getY();
    			
    			retour.add(new Line2D(){});
    			
    			}
    	}
    	
    	return retour;
    }
    
    public ArrayList<Line2D> getArcs()
    {
        // TODO
        // retourner tous les arcs du maillage
        return null;
    }
    
}
