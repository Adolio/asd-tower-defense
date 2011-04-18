/*
  Copyright (C) 2010 Lazhar Farjallah

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package models.outils;

import java.io.*;
import java.util.*;
import javax.media.*;
import javax.media.format.*;

/**
 * Fichier : Son.java
 * <p>
 * Encodage : UTF-8
 * <p>
 * Cette classe interne privee permet de representer du son (flux).
 * <p>
 * Remarques : <br>
 * Cette classe ne permet plus de lire plusieurs fichiers son en concurrence.
 * Si on a besoin de ça, par exemple pour introduire les musiques d'effets, il faudra
 * développer une autre classe utilisant la librairie JLayer (jl1.0.1.jar), mais avec
 * le risque d'avoir des bugs comme auparavant.
 * 
 * @author Lazhar Farjallah
 * @version 17 dec. 2009
 * @since jdk1.6.0_16
 */
public class Son {
   // Le player de son.
   private static Player player;
   private static Format input1;
   private static Format input2;
   private static Format output;
   private static ControllerListener ControleurEcoute;

   // Le nombre de fois qu'on veut repeter le son.
   private int nombreRepetitions = 1;
   // Le fichier ou est stocke la musique a lire.
   private File fichier;
   // Pour savoir si le son actuel est à l'arret.
   private boolean arret = false;
   // Ecouteur de son
   private ArrayList<EcouteurDeSon> ecouteursDeSon = new ArrayList<EcouteurDeSon>();

   /**
    * Ce constructeur permet de creer un objet Son en fonction d'un fichier
    * donne ainsi que d'un nombre de repetitions donne.
    * 
    * @param fichier
    *           Le fichier a lire.
    * @param repetitions
    *           Le nombre de fois que la musique doit etre jouee. Si cette
    *           valeur vaut 0, la musique est repetee a l'infini.
    */
   public Son(File fichier) {
      this.fichier = fichier;
      input1 = new AudioFormat(AudioFormat.MPEGLAYER3);
      input2 = new AudioFormat(AudioFormat.MPEG);
      output = new AudioFormat(AudioFormat.LINEAR);
      PlugInManager.addPlugIn("com.sun.media.codec.audio.mp3.JavaDecoder",
            new Format[] { input1, input2 }, new Format[] { output },
            PlugInManager.CODEC);
   }

   /**
    * Cette methode permet de lire le son une fois.
    */
   public void lire() {
      jouer();
   }

   /**
    * Cette methode permet de lire la musique courante, en fonction du nombre de
    * repetitions donne. Si <tt>nombreRepetitions</tt> vaut 0, on repete la
    * musique $ l'infini, sinon on repete la musique <tt>nombreRepetitions</tt>
    * fois.
    * 
    * @param nombreRepetitions
    *           Le nombre de fois que la musique doit etre jouee. Si ce
    *           parametre vaut 0, la musique est jouee a l'infini (ne s'arrete
    *           jamais).
    */
   public void lire(int nombreRepetitions) {
      this.nombreRepetitions = nombreRepetitions == 0 ? Integer.MAX_VALUE
            : nombreRepetitions;
      jouer();
   }

   /**
    * Cette methode permet d'arreter toutes les occurrences de cette musique qui
    * sont en train d'etre lues. En bref, elle stoppe tous les threads en train
    * de lire cette musique.
    */
   public void arreter() {
      // On passe en mode arret.
      arret = true;
      // On ferme le lecteur de son.
      if (player != null) {
         player.removeControllerListener(ControleurEcoute);
         player.stop();
         player.close();
      }

      // informe les ecouteurs que le son est termine
      for (EcouteurDeSon ecouteurDeSon : ecouteursDeSon) {
         ecouteurDeSon.estTerminee(this);
      }
   }

   /**
    * Cette methode permet de determiner si toutes les occurrences de lecture de
    * la musique courante sont terminees ou non.
    * 
    * @return True si toutes les occurrences sont terminees, false sinon.
    */
   public boolean estTerminee() {
      return arret;
   }

   /**
    * Permet d'ajouter un ecouteur de son
    * 
    * @param eds
    *           l'ecouteur de son
    */
   public void ajouterEcouteurDeSon(EcouteurDeSon eds) {
      ecouteursDeSon.add(eds);
   }

   /**
    * Permet de recuperer le fichier du son
    * 
    * @return le fichier du son
    */
   public File getFichier() {
      return fichier;
   }

   private void jouer() {
      arret = false;
      try {
         if (player != null) {
            player.stop();
            player.close();
         }

         player = Manager.createPlayer(new MediaLocator(fichier.toURI().toURL()));
         ControleurEcoute = new ControllerListener() {
            int times = nombreRepetitions;
   
            @Override
            public void controllerUpdate(ControllerEvent arg0) {
               if (arg0 instanceof javax.media.EndOfMediaEvent) {
                  if (--times != 0 && !arret) {
                     jouer();
                     player.removeControllerListener(this);
                  }
               }
   
            }
         };
         player.addControllerListener(ControleurEcoute);
         player.start();
      }
      catch(Exception e) {
         e.printStackTrace();
      }
   }

}
