package models.outils;

import java.io.*;
import java.util.*;

/**
 * Fichier : MeilleursScores.java
 * <p>
 * Encodage : UTF-8
 * <p>
 * Cette classe permet de sérialiser la liste des scores d'un jeu (enregistrer les
 * scores dans un fichier sérialisé). Lors de l'appel du constructeur, si le fichier
 * donné n'existe pas, on le crée. S'il existe déjà, on charge ses données. Lors de
 * l'ajout d'un score, on sauve automatiquement les données dans le fichier
 * sérialisé. Les scores sont triés par ordre décroissant, du meilleur au moins bon.
 * Le maximum de scores stockés est de <tt>NOMBRE_MAX_SCORES</tt>.
 * <p>
 * Remarques : <br>
 * Cette classe ne peut pas être dérivée.
 * 
 * @author Aurélien Da Campo
 * @author Pierre-Dominique Putallaz
 * @author Lazhar Farjallah
 * @version 16 déc. 2009
 * @since jdk1.6.0_16
 */
public final class MeilleursScores
{
   
   // Le nombre maximum de scores qu'on va stocker dans le fichier sérialisé.
   public final static int NOMBRE_MAX_SCORES = 10;
   // L'bjet permettant de représenter le fichier sérialisé avec lequel on traite.
   private final File FICHIER;
   // Pour stocker la liste des scores.
   private ArrayList<Score> scores;
   
   /**
    * Ce constructeur permet de charger le fichier donné en paramètre (et le cas
    * échéant de le créer automatiquement). Le fichier en question contiendra (si ce
    * n'est pas déjà le cas) les données sérialisées relatives au scores des joueurs.
    * 
    * @param nomFichier
    *        Le nom du fichier avec lequel traiter.
    */
   public MeilleursScores (String nomFichier)
   {
      FICHIER = new File(nomFichier);
      lireDonneesFichier();
   }
   
   /**
    * Getter pour le champ <tt>scores</tt>
    * 
    * @return La valeur du champ <tt>scores</tt>
    */
   public ArrayList<Score> getScores ()
   {
      return new ArrayList<Score>(scores);
   }
   
   /**
    * Cette méthode permet d'ajouter un meilleur score dans la liste des meilleurs
    * scores. Elle est totalement transparente, l'utilisateur ne se soucie de rien.
    * Il suffit simplement d'appeler cette méthode sans se soucier du score qu'on
    * donne et le tour est joué. La méthode fait des vérifications automatiques
    * telles que le nombre maximum de scores stockés ainsi que le tri intrinsèque des
    * scores stockés.
    * 
    * @param nomJoueur
    *        Le nom du joueur dont on veut enregistrer le meilleur score obtenu
    * @param valeurScore
    *        La valeur du meilleur score que le joueur à atteint.
    */
   public void ajouterMeilleurScore (String nomJoueur, int valeurScore)
   {
      scores.add(new Score(nomJoueur, valeurScore));
      Collections.sort(scores);
      if (scores.size() > NOMBRE_MAX_SCORES)
      {
         scores.remove(NOMBRE_MAX_SCORES);
      }
      sauverDonneesFichier();
   }
   
   /**
    * @see Object#toString()
    */
   @Override
   public String toString ()
   {
      StringBuilder chaine = new StringBuilder();
      
      for (Score score : scores)
         chaine.append(score + "\n");
      
      return new String(chaine);
   }
   
   /**
    * Cette méthode permet d'ouvrir le fichier sérialisé correspondant et de charger
    * ces données dans les objets de la classe. Si le fichier en question n'existe
    * pas, il est automatiquement créé.
    */
   @SuppressWarnings("unchecked")
   private void lireDonneesFichier ()
   {
      try
      {
         // Ouverture d'un flux d'entrée depuis le fichier FICHIER.
         FileInputStream fluxEntreeFichier = new FileInputStream(FICHIER);
         // Création d'un "flux objet" avec le flux d'entrée.
         ObjectInputStream fluxEntreeObjet = new ObjectInputStream(fluxEntreeFichier);
         try
         {
            // Désérialisation : lecture de l'objet depuis le flux d'entrée
            // (chargement des données du fichier).
            scores = (ArrayList<Score>) fluxEntreeObjet.readObject();
         }
         finally
         {
            // On ferme les flux (important!).
            try
            {
               fluxEntreeObjet.close();
            }
            finally
            {
               fluxEntreeFichier.close();
            }
         }
      }
      catch (FileNotFoundException erreur) // Le fichier n'existe pas.
      {
         // Si le fichier n'existe pas, on le crée!
         scores = new ArrayList<Score>();
      }
      catch (IOException erreur) // Erreur sur l'ObjectInputStream.
      {
         erreur.printStackTrace();
      }
      catch (ClassNotFoundException erreur) // Erreur sur l'ObjectInputStream.
      {
         erreur.printStackTrace();
      }
   }
   
   /**
    * Cette méthode permet de sérialiser les données dans un fichier, c'est-à-dire de
    * sauver la liste des scores dans un fichier binaire.
    */
   private void sauverDonneesFichier ()
   {
      try
      {
         // Ouverture d'un flux de sortie vers le fichier FICHIER.
         FileOutputStream fluxSortieFichier = new FileOutputStream(FICHIER);
         
         // Création d'un "flux objet" avec le flux fichier.
         ObjectOutputStream fluxSortieObjet = new ObjectOutputStream(fluxSortieFichier);
         try
         {
            // Sérialisation : écriture de l'objet dans le flux de sortie.
            fluxSortieObjet.writeObject(scores);
            // On vide le tampon.
            fluxSortieObjet.flush();
         }
         finally
         {
            // Fermeture des flux (important!).
            try
            {
               fluxSortieObjet.close();
            }
            finally
            {
               fluxSortieFichier.close();
            }
         }
      }
      catch (IOException erreur) // Erreur sur l'ObjectOutputStream.
      {
         erreur.printStackTrace();
      }
   }
   
}
