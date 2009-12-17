package models.outils;

import java.io.*;
import javazoom.jl.player.Player;

/**
 * Fichier : Musique.java
 * <p>
 * Encodage : UTF-8
 * <p>
 * Cette classe permet de lire une musique selon un fichier donné.
 * <p>
 * Remarques : <br>
 * Les musiques sont "threadées", c'est-à-dire qu'on peut sans autre lire plusieurs
 * fois la même musique dans le code.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 17 déc. 2009
 * @since jdk1.6.0_16
 */
public class Musique
{
   
   // Le fichier musique (.mp3, ...) qu'on veut lire.
   private final File FICHIER;
   
   /**
    * Ce constructeur permet de construire un objet de type Musique en fonction du
    * nom du fichier donné.
    * 
    * @param nomFichier
    *        Le nom du fichier musique (.mp3, ...)
    */
   public Musique (String nomFichier)
   {
      FICHIER = new File(nomFichier);
   }
   
   /**
    * Cette méthode permet de lire la musique courante, en fonction du nombre de
    * répétitions donné. Si <tt>nombreRepetitions</tt> vaut 0, on répète la musique $
    * l'infini, sinon on répète la musique <tt>nombreRepetitions</tt> fois.
    * 
    * @param nombreRepetitions
    *        Le nombre de fois que la musique doit être jouée. Si ce paramètre vaut
    *        0, la musique est jouée à l'infini (ne s'arrête jamais).
    */
   public void lire (int nombreRepetitions)
   {
      try
      {
         // Création de l'objet Son associé à cette musique.
         Son son = new Son(FICHIER, nombreRepetitions);
         // Appelle la méthode run() de la classe Son (lit la musique).
         son.start();
      }
      catch (Exception erreur) // Il y a eu une erreur liée au son créé.
      {
         erreur.printStackTrace();
      }
   }
   
   /**
    * Fichier : Son.java
    * <p>
    * Encodage : UTF-8
    * <p>
    * Cette classe interne privée permet de représenter du son (flux).
    * <p>
    * Remarques : <br>
    * Cette classe est "threadée", elle permet de lire plusieurs fois la même musique
    * en concurrence.
    * 
    * @author Pierre-Dominique Putallaz
    * @author Aurélien Da Campo
    * @author Lazhar Farjallah
    * @version 17 déc. 2009
    * @since jdk1.6.0_16
    */
   private final class Son extends Thread
   {
      
      // Le player de son.
      private Player player;
      // Le flux d'entrée lié au fichier à jouer.
      private FileInputStream fluxEntreeFichier;
      // Le nombre de fois qu'on veut répéter le son.
      private int nombreRepetitions;
      // Le fichier ou est stocké la musique à lire.
      private File fichier;
      
      /**
       * Ce constructeur permet de créer un objet Son en fonction d'un fichier donné
       * ainsi que d'un nombre de répétitions donné.
       * 
       * @param fichier
       *        Le fichier à lire.
       * @param repetitions
       *        Le nombre de fois que la musique doit être jouée. Si cette valeur
       *        vaut 0, la musique est répétée à l'infini.
       */
      private Son (File fichier, int repetitions)
      {
         this.fichier = fichier;
         // Si repetitions vaut 0, alors on répète un nombre de fois pseudo-infini la
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
               // Création du flux d'entrée en fonction du fichier.
               fluxEntreeFichier = new FileInputStream(fichier);
               // Initialisation de l'objet player en fonction du flux.
               player = new Player(fluxEntreeFichier);
               // Lecture de la musique.
               player.play();
            }
         }
         catch (Exception erreur) // Erreur liée à la lecture du son.
         {
            erreur.printStackTrace();
         }
         
      }
      
   }
   
}
