package models.outils;

import java.io.Serializable;
import java.util.Date;

/**
 * Fichier : Score.java
 * <p>
 * Encodage : UTF-8
 * <p>
 * Cette classe permet de représenter le score d'un joueur en fonction de son nom, de
 * son score obtenu ainsi que de la date d'obtention du score.
 * <p>
 * Remarques : <br>
 * Cette classe implémente deux interfaces : {@link Serializable} et
 * {@link Comparable}
 * 
 * @author Aurélien Da Campo
 * @author Pierre-Dominique Putallaz
 * @author Lazhar Farjallah
 * @version 16 déc. 2009
 * @since jdk1.6.0_16
 */
public class Score implements Serializable, Comparable<Score>
{
   
   // La version de sérialisation.
   private static final long serialVersionUID = 1L;
   // La valeur du score du joueur.
   private int valeur;
   // Le nom du joueur.
   private String nomJoueur;
   // La date à laquelle le score est créé.
   private Date date;
   
   /**
    * Ce constructeur permet de créer un objet de type Score en fonction d'un nom de
    * joueur ainsi qu'une valeur de score obtenu.
    * 
    * @param nomJoueur
    *        Le nom du joueur qui a obtenu un score.
    * @param valeur
    *        La valeur du score obtenue par le joueur.
    */
   public Score (String nomJoueur, int valeur)
   {
      this.valeur = valeur;
      this.nomJoueur = nomJoueur;
      // new Date() créé automatiquement la date courante.
      date = new Date();
   }
   
   /**
    * Ce constructeur permet de créer un score en fonction d'un autre score donné.
    * 
    * @param score
    *        Le score à partir duquel on veut créer un nouvel objet Score.
    */
   public Score (Score score)
   {
      valeur    = score.valeur;
      nomJoueur = score.nomJoueur;
      date      = score.date;
   }
   
   /**
    * Getter pour le champ <tt>valeur</tt>
    * 
    * @return La valeur du champ <tt>valeur</tt>
    */
   public int getValeur ()
   {
      return valeur;
   }
   
   /**
    * Getter pour le champ <tt>nomJoueur</tt>
    * 
    * @return La valeur du champ <tt>nomJoueur</tt>
    */
   public String getNomJoueur ()
   {
      return nomJoueur;
   }
   
   /**
    * Getter pour le champ <tt>date</tt>
    * 
    * @return La valeur du champ <tt>date</tt>
    */
   public Date getDate()
   {
      return new Date(date.getTime());
   }
   
   /**
    * Cette méthode permet d'appliquer une politique de comparaison entre objets de
    * type Score. Cette politique sera ensuite appliquée lors du tri d'une collection
    * quelconque contenant des objets de type Score. Ici, on applique la politique
    * suivante : on veut qu'un score plus élevé qu'un autre soit "plus grand" que
    * lui, c'est-à-dire qu'un score plus élevé est classé avant un score moins élevé.
    * En bref, on applique ici une politique de tri en ordre décroissant.
    * 
    * @see Comparable#compareTo(Object)
    */
   @Override
   public int compareTo (Score aComparer)
   {
      // Le score à comparer est plus élevé que le score courant.
      if (aComparer.valeur > valeur)
         return 1;
      // Le score à comparer équivaut le score courant.
      else if (aComparer.valeur == valeur)
         return 0;
      // Le score à comparer est plus petit que le score courant.
      else
         return -1;
   }
   
   /**
    * @see Object#toString()
    */
   @Override
   public String toString ()
   {
      return nomJoueur + " - " + valeur + " - " + date;
   }
   
}
