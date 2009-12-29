package models.outils;

import java.io.*;
import javazoom.jl.player.Player;

/**
 * Fichier : Musique.java
 * <p>
 * Encodage : UTF-8
 * <p>
 * Cette classe permet de lire une musique selon un fichier donne.
 * <p>
 * Remarques : <br>
 * Les musiques sont "threadees", c'est-a-dire qu'on peut sans autre lire plusieurs
 * fois la meme musique dans le code.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 17 dec. 2009
 * @since jdk1.6.0_16
 */
public class Musique
{
   
   // Le fichier musique (.mp3, ...) qu'on veut lire.
   private final File FICHIER;
   
   /**
    * Ce constructeur permet de construire un objet de type Musique en fonction du
    * nom du fichier donne.
    * 
    * @param nomFichier
    *        Le nom du fichier musique (.mp3, ...)
    */
   public Musique (String nomFichier)
   {
      FICHIER = new File(nomFichier);
   }
   
   /**
    * Cette methode permet de lire la musique courante, en fonction du nombre de
    * repetitions donne. Si <tt>nombreRepetitions</tt> vaut 0, on repete la musique $
    * l'infini, sinon on repete la musique <tt>nombreRepetitions</tt> fois.
    * 
    * @param nombreRepetitions
    *        Le nombre de fois que la musique doit etre jouee. Si ce parametre vaut
    *        0, la musique est jouee a l'infini (ne s'arrete jamais).
    */
   public void lire (int nombreRepetitions)
   {
      try
      {
         // Creation de l'objet Son associe a cette musique.
         Son son = new Son(FICHIER, nombreRepetitions);
         // Appelle la methode run() de la classe Son (lit la musique).
         son.start();
      }
      catch (Exception erreur) // Il y a eu une erreur liee au son cree.
      {
         erreur.printStackTrace();
      }
   }
   
   /**
    * Fichier : Son.java
    * <p>
    * Encodage : UTF-8
    * <p>
    * Cette classe interne privee permet de representer du son (flux).
    * <p>
    * Remarques : <br>
    * Cette classe est "threadee", elle permet de lire plusieurs fois la meme musique
    * en concurrence.
    * 
    * @author Pierre-Dominique Putallaz
    * @author Aurelien Da Campo
    * @author Lazhar Farjallah
    * @version 17 dec. 2009
    * @since jdk1.6.0_16
    */
   private final class Son extends Thread
   {
      
      // Le player de son.
      private Player player;
      // Le flux d'entree lie au fichier a jouer.
      private FileInputStream fluxEntreeFichier;
      // Le nombre de fois qu'on veut repeter le son.
      private int nombreRepetitions;
      // Le fichier ou est stocke la musique a lire.
      private File fichier;
      
      /**
       * Ce constructeur permet de creer un objet Son en fonction d'un fichier donne
       * ainsi que d'un nombre de repetitions donne.
       * 
       * @param fichier
       *        Le fichier a lire.
       * @param repetitions
       *        Le nombre de fois que la musique doit etre jouee. Si cette valeur
       *        vaut 0, la musique est repetee a l'infini.
       */
      private Son (File fichier, int repetitions)
      {
         this.fichier = fichier;
         // Si repetitions vaut 0, alors on repete un nombre de fois pseudo-infini la
         // lecture de la musique.
         nombreRepetitions = repetitions == 0 ? Integer.MAX_VALUE : repetitions;
      }
      
      /**
       * Permet de lancer le thread qui lit le son.
       * 
       * @see Thread#run()
       */
      @Override
      public void run ()
      {
         try
         {
            // On lit le nombre de fois voulu le son.
            for (int i = 0; i < nombreRepetitions; ++i)
            {
               // Creation du flux d'entree en fonction du fichier.
               fluxEntreeFichier = new FileInputStream(fichier);
               // Initialisation de l'objet player en fonction du flux.
               player = new Player(fluxEntreeFichier);
               // Lecture de la musique.
               player.play();
            }
         }
         catch (Exception erreur) // Erreur liee a la lecture du son.
         {
            erreur.printStackTrace();
         }
         
      }
      
   }
   
}
