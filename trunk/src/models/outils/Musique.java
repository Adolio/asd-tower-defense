package models.outils;

import java.io.*;
import java.util.*;

import javax.sound.sampled.*;

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
   // L'objet de type Son à lire.
   private Son son;
   // Pour avoir une trace de tous les objets Son lances et threades.
   private ArrayList<Son> listeSons = new ArrayList<Son>();
   // Controle d'entree pour le volume.
   private static Control ctrlIn;
   
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
         son = new Son(FICHIER, nombreRepetitions);
         // Ajout de l'objet son courant à la liste des sons.
         listeSons.add(son);
         // Appelle la methode run() de la classe Son (lit la musique).
         son.start();
      }
      catch (Exception erreur) // Il y a eu une erreur liee au son cree.
      {
         erreur.printStackTrace();
      }
   }
   
   /**
    * Cette methode permet d'arreter toutes les occurrences de cette musique qui sont
    * en train d'etre lues. En bref, elle stoppe tous les threads en train de lire
    * cette musique.
    */
   public void arreter ()
   {
      // Pour chaque occurrence de la musique en lecture.
      for (Son sonCourant : listeSons)
      {
         // Chaque occurrence est stoppée.
         sonCourant.interrupt();
      }
   }
   
   /**
    * Cette methode permet de determiner si toutes les occurrences de lecture de la
    * musique courante sont terminees ou non.
    * 
    * @return True si toutes les occurrences sont terminees, false sinon.
    */
   public boolean estTerminee ()
   {
      // Pour chaque occurrence de la musique en lecture.
      for (Son sonCourant : listeSons)
      {
         if (!sonCourant.arret) // Le son courant n'est pas a l'arret.
         {
            // Un son non arrete a ete detecte. On retourne faux.
            return false;
         }
      }
      // Tous les sons sont bien arretes. On retourne vrai.
      return true;
   }
   
   /**
    * Cette methode permet de savoir (en pourcentage) quel est le volume actuel du
    * systeme.
    * 
    * @return Le pourcentage du volume du systeme actuel.
    */
   public static int getVolume ()
   {
      return 100 * (int) (((FloatControl) ctrlIn).getValue() / ((FloatControl) ctrlIn)
            .getMaximum());
   }
   
   /**
    * Cette methode permet de regler le volume du systeme en fonction d'un
    * pourcentage donne.
    * 
    * @param volumePourcent
    *        Le pourcentage du volume qu'on veut appliquer.
    */
   public static void setVolume (int volumePourcent)
   {
      // Pour les ports de sortie audio.
      Port lineOut;
      
      try
      {
         // Le systeme possede une sortie LINE_OUT.
         if (AudioSystem.isLineSupported(Port.Info.LINE_OUT))
         {
            lineOut = (Port) AudioSystem.getLine(Port.Info.LINE_OUT);
            // On ouvre ce port.
            lineOut.open();
         }
         // Le systeme possede une sortie HEADPHONE.
         else if (AudioSystem.isLineSupported(Port.Info.HEADPHONE))
         {
            lineOut = (Port) AudioSystem.getLine(Port.Info.HEADPHONE);
            // On ouvre ce port.
            lineOut.open();
         }
         // Le systeme possede une sortie SPEAKER.
         else if (AudioSystem.isLineSupported(Port.Info.SPEAKER))
         {
            lineOut = (Port) AudioSystem.getLine(Port.Info.SPEAKER);
            // On ouvre ce port.
            lineOut.open();
         }
         // Le systeme ne possede pas de sortie audio (triste!).
         else
         {
            System.out.println("Impossible d'avoir une sortie audio!");
            return;
         }
         
         // On recupere le controle d'entree du volume.
         ctrlIn = lineOut.getControl(FloatControl.Type.VOLUME);
         // On change le volume du systeme avec celui donne en parametre.
         ((FloatControl) ctrlIn).setValue(volumePourcent / 100.0f);
      }
      catch (Exception erreur)  // Une erreur liee au volume est survenue.
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
   private static final class Son extends Thread
   {
      
      // Le player de son.
      private Player player;
      // Le flux d'entree lie au fichier a jouer.
      private FileInputStream fluxEntreeFichier;
      // Le nombre de fois qu'on veut repeter le son.
      private int nombreRepetitions;
      // Le fichier ou est stocke la musique a lire.
      private File fichier;
      // Pour savoir si le son actuel est à l'arret.
      private boolean arret = false;
      
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
            for (int i = 0; !arret && i < nombreRepetitions; ++i)
            {
               // Creation du flux d'entree en fonction du fichier.
               fluxEntreeFichier = new FileInputStream(fichier);
               // Initialisation de l'objet player en fonction du flux.
               player = new Player(fluxEntreeFichier);
               // Lecture de la musique.
               player.play();
            }
            // On arrete le thread courant car le son est termine.
            interrupt();
         }
         catch (Exception erreur) // Erreur liee a la lecture du son.
         {
            erreur.printStackTrace();
         }
      }
      
      /**
       * Permet d'interrompre un thread.
       * 
       * @see Thread#interrupt()
       */
      public void interrupt ()
      {
         // On passe en mode arret.
         arret = true;
         // On ferme le lecteur de son.
         player.close();
      }
      
   }
   
}
