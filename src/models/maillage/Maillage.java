package models.maillage;

import java.util.*;
import java.awt.*;

import org.jgrapht.WeightedGraph;
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
    private final int LARGEUR_NOEUD = 2;
    private int largeurPixels;
    private int hauteurPixels;
    private ArrayList<Noeud> noeuds;
    private DefaultDirectedWeightedGraph<Noeud, DefaultEdge> graphe;
    
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
    public void desactiverNoeuds(Rectangle rectangle) throws IllegalArgumentException
    {
        // TODO : désactiver les noeuds dans la zone donnée.
        // Pour cela, une idée serait de mettre simplement le poids des noeuds
        // concernées à une valeur pseudo infinie.
    }
    
    /* (non-Javadoc)
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
    private DefaultDirectedWeightedGraph<Noeud,DefaultEdge> construireGraphe()
    {
        DefaultDirectedWeightedGraph<Noeud, DefaultEdge> graphe =
            new DefaultDirectedWeightedGraph<Noeud, DefaultEdge>(DefaultWeightedEdge.class);
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
        for (int y=0; y<hauteurPixels / LARGEUR_NOEUD; y+=1) {
            for (int x=0; x<largeurPixels / LARGEUR_NOEUD - 1; x+=1) {
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
        for (int y=0; y<hauteurPixels / LARGEUR_NOEUD - 1; y+=1) {
            for (int x=0; x<largeurPixels / LARGEUR_NOEUD; x+=1) {
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
        for (int x=0; x<largeurPixels / LARGEUR_NOEUD - 1; x+=1) {
            for (int y=0; y<hauteurPixels / LARGEUR_NOEUD - 1; y+=1) {
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
        for (int x=0; x<largeurPixels / LARGEUR_NOEUD - 1; x+=1) {
            for (int y=1; y<hauteurPixels / LARGEUR_NOEUD; y+=1) {
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
    
}
