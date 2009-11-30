package models.maillage;

import java.net.URL;
import java.util.Vector;
import java.awt.*;
import models.tours.Tour;
import org.jgrapht.*;
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
   
   private int largeurPixels;
   private int hauteurPixels;
   private DefaultDirectedWeightedGraph<Noeud, DefaultEdge> graphe;
   
   /**
    * @param largeurPixels
    * @param hauteurPixels
    * @param tours
    * @param murs
    * @throws IllegalArgumentException
    */
   public Maillage (int largeurPixels, int hauteurPixels, Vector<Rectangle> tours,
         Vector<Rectangle> murs) throws IllegalArgumentException
   {
      this.largeurPixels = largeurPixels;
      this.hauteurPixels = hauteurPixels;
      graphe = construireGraphe(tours, murs);
   }
   
   /**
    * Getter pour le champ <tt>largeurPixels</tt>
    * 
    * @return La valeur du champ <tt>largeurPixels</tt>
    */
   public int getLargeurPixels ()
   {
      return largeurPixels;
   }
   
   /**
    * Getter pour le champ <tt>hauteurPixels</tt>
    * 
    * @return La valeur du champ <tt>hauteurPixels</tt>
    */
   public int getHauteurPixels ()
   {
      return hauteurPixels;
   }
   
   /**
    * Setter pour le champ <tt>largeurPixels</tt>
    * 
    * @param largeurPixels
    *        La valeur qu'on veut attribuer au champ <tt>largeurPixels</tt>
    */
   public void setLargeurPixels (int largeurPixels)
   {
      this.largeurPixels = largeurPixels;
   }
   
   /**
    * Setter pour le champ <tt>hauteurPixels</tt>
    * 
    * @param hauteurPixels
    *        La valeur qu'on veut attribuer au champ <tt>hauteurPixels</tt>
    */
   public void setHauteurPixels (int hauteurPixels)
   {
      this.hauteurPixels = hauteurPixels;
   }
   
   /**
    * @param tour
    * @throws IllegalArgumentException
    */
   public void activerNoeud (Rectangle tour) throws IllegalArgumentException
   {
      // TODO : activer le noeud correspondant à la tour donnée.
   }
   
   /**
    * @param tours
    * @param murs
    * @return
    */
   private static DefaultDirectedWeightedGraph<Noeud, DefaultEdge> construireGraphe (
         Vector<Rectangle> tours, Vector<Rectangle> murs)
         throws IllegalArgumentException
   {
      DefaultDirectedWeightedGraph<Noeud, DefaultEdge> graphe =
          new DefaultDirectedWeightedGraph<Noeud, DefaultEdge>(DefaultEdge.class);
      
      /*
       * TODO : construire le graphe de base en fonction des tours et des murs.
       * graphe.setEdgeWeight( graphe.addEdge( new Noeud(1, 2, false, false), new
       * Noeud(3, 7, false, false) ), 3.5 );
       */

      return graphe;
      
   }
   
}
