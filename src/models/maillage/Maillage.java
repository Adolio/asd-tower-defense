package models.maillage;
import java.util.Vector;
import java.awt.*;
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
    * LA CONSTRUCTION SE FAIT SIMPLEMENT AVEC UNE LARGEUR ET UNE HAUTEUR
    * 
    * DANS CE RECTANGLE TU MAILLE COMME TU VEUX...
    * 
    * @param largeurPixels
    * @param hauteurPixels
    * @param tours
    * @param murs
    * @throws IllegalArgumentException
    */
   public Maillage (int largeurPixels, int hauteurPixels, 
		   			Vector<Rectangle> zonesDesactivees) 
   					throws IllegalArgumentException
   {
      this.largeurPixels = largeurPixels;
      this.hauteurPixels = hauteurPixels;
      graphe = construireGraphe(zonesDesactivees);
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
    * DESACTIVE UNE ZONE --> IDEPENDANT DES TOURS !!!
    * 
    * @param rectangle la zone a desactiver
    * @throws IllegalArgumentException
    */
   public void activerNoeuds (Rectangle rectangle) throws IllegalArgumentException
   {
      // TODO : activer les noeuds dans la zone donnee.
   }
   
   /**
    * DESACTIVE UNE ZONE --> IDEPENDANT DES TOURS !!!
    * 
    * @param rectangle la zone a desactiver
    * @throws IllegalArgumentException
    */
   public void desactiverNoeuds (Rectangle rectangle) throws IllegalArgumentException
   {
      // TODO : desactiver les noeuds dans la zone donnee.
   }
   
   /**
    * @param tours
    * @param murs
    * @return
    */
   private static DefaultDirectedWeightedGraph<Noeud, DefaultEdge> 
   	construireGraphe (Vector<Rectangle> zoneDesactivee)
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
