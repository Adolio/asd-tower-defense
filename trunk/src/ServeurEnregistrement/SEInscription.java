package ServeurEnregistrement;

import java.util.ArrayList;

import Reseau.*;

/**
 * 
 * @author lazhar
 *
 */
public class SEInscription {
   
   private static ArrayList<Enregistrement> jeuxEnregistres = new ArrayList<Enregistrement>();
   private Port port;
   private boolean avecLog;
   private Canal canal;
   
   /**
    * 
    * @param port
    * @param avecLog
    */
   public SEInscription(Port port, boolean avecLog) {
      this.port = port;
      this.avecLog = avecLog;
   }
   
   /**
    * 
    */
   public void lancer() {
      port.reserver();
      while (true) {
         System.out.println("\n+ Un nouveau thread du Serveur d'enregistrement va demarrer...");
         creerCanal();
         (new Thread(new SEConnexion(canal))).start();
      }
   }
   
   /**
    * 
    */
   private void creerCanal() {
      try {
         canal = new Canal(port, avecLog);
      } catch (CanalException ce) {
         System.out.println("\tProbleme de connexion : " + ce.getMessage());
      }
   }
   
   /**
    * 
    * @param e
    * @return
    */
   public static synchronized boolean ajouterEnregistrement(Enregistrement e) {
      if (!jeuxEnregistres.contains(e)) {
         jeuxEnregistres.add(e);
         return true;
      }
      return false;
   }
   
   /**
    * 
    * @param e
    */
   public static synchronized void enleverEnregistrement(Enregistrement e) {
      jeuxEnregistres.remove(e);
   }
   
   /**
    * 
    * @return
    */
   public static synchronized int getNombreEnregistrements() {
      return jeuxEnregistres.size();
   }
   
   /**
    * 
    * @return
    */
   public static synchronized ArrayList<Enregistrement> getJeuxEnregistres() {
      return jeuxEnregistres;
   }
}
