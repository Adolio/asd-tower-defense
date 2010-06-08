package models.outils;

import java.io.*;
import java.util.*;

/**
 * Fichier : MeilleursScores.java
 * <p>
 * Encodage : UTF-8
 * <p>
 * Cette classe permet de serialiser la liste des scores d'un jeu (enregistrer les
 * scores dans un fichier serialise). Lors de l'appel du constructeur, si le fichier
 * donne n'existe pas, on le cree. S'il existe deja, on charge ses donnees. Lors de
 * l'ajout d'un score, on sauve automatiquement les donnees dans le fichier
 * serialise. Les scores sont tries par ordre decroissant, du meilleur au moins bon.
 * Le maximum de scores stockes est de <tt>NOMBRE_MAX_SCORES</tt>.
 * <p>
 * Remarques : <br>
 * Cette classe ne peut pas être derivee.
 * 
 * @author Lazhar Farjallah
 * @version 16 dec. 2009
 * @since jdk1.6.0_16
 */
public final class MeilleursScores
{
   
   // Le nombre maximum de scores qu'on va stocker dans le fichier serialise.
   public final static int NOMBRE_MAX_SCORES = 100;
   // L'bjet permettant de representer le fichier serialise avec lequel on traite.
   private final File FICHIER;
   // Pour stocker la liste des scores.
   private ArrayList<Score> scores;
   
   /**
    * Ce constructeur permet de charger le fichier donne en parametre (et le cas
    * echeant de le creer automatiquement). Le fichier en question contiendra (si ce
    * n'est pas deja le cas) les donnees serialisees relatives au scores des joueurs.
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
    * Cette methode permet d'ajouter un meilleur score dans la liste des meilleurs
    * scores. Elle est totalement transparente, l'utilisateur ne se soucie de rien.
    * Il suffit simplement d'appeler cette methode sans se soucier du score qu'on
    * donne et le tour est joue. La methode fait des verifications automatiques
    * telles que le nombre maximum de scores stockes ainsi que le tri intrinseque des
    * scores stockes.
    * 
    * @param nomJoueur
    *        Le nom du joueur dont on veut enregistrer le meilleur score obtenu
    * @param valeurScore
    *        La valeur du meilleur score que le joueur a atteint.
    */
   public void ajouterMeilleurScore (String nomJoueur, int valeurScore, long dureePartie)
   {
      scores.add(new Score(nomJoueur, valeurScore, dureePartie));
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
    * Cette methode permet d'ouvrir le fichier serialise correspondant et de charger
    * ces donnees dans les objets de la classe. Si le fichier en question n'existe
    * pas, il est automatiquement cree.
    */
   @SuppressWarnings("unchecked")
   private void lireDonneesFichier ()
   {
      try
      {
         // Ouverture d'un flux d'entree depuis le fichier FICHIER.
         FileInputStream fluxEntreeFichier = new FileInputStream(FICHIER);
         // Creation d'un "flux objet" avec le flux d'entree.
         ObjectInputStream fluxEntreeObjet = new ObjectInputStream(fluxEntreeFichier);
         try
         {
            // Deserialisation : lecture de l'objet depuis le flux d'entree
            // (chargement des donnees du fichier).
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
         // Si le fichier n'existe pas, on le cree!
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
    * Cette methode permet de serialiser les donnees dans un fichier, c'est-a-dire de
    * sauver la liste des scores dans un fichier binaire.
    */
   private void sauverDonneesFichier ()
   {
      try
      {
         // Ouverture d'un flux de sortie vers le fichier FICHIER.
         FileOutputStream fluxSortieFichier = new FileOutputStream(FICHIER);
         
         // Creation d'un "flux objet" avec le flux fichier.
         ObjectOutputStream fluxSortieObjet = new ObjectOutputStream(fluxSortieFichier);
         try
         {
            // Serialisation : ecriture de l'objet dans le flux de sortie.
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
