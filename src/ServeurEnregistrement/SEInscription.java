package ServeurEnregistrement;

import java.util.ArrayList;

import Reseau.*;

/**
 * 
 * @author lazhar
 *
 */
public class SEInscription {
   
   private ArrayList<Enregistrement> jeuxEnregistres = new ArrayList<Enregistrement>();
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
         creerCanal();
         (new Thread(new SEConnexion(canal, this))).start();
      }
   }
   
   /**
    * 
    */
   private void creerCanal() {
      try {
         System.out.println("Serveur d'enregistrement démarré.");
         canal = new Canal(port, avecLog);
      } catch (CanalException ce) {
         System.out.println("\tProblème de connexion : " + ce.getMessage());
      }
   }
   
   /**
    * 
    * @param e
    * @return
    */
   public boolean ajouterEnregistrement(Enregistrement e) {
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
   public void enleverEnregistrement(Enregistrement e) {
      jeuxEnregistres.remove(e);
   }
   
   /**
    * 
    * @return
    */
   public int getNombreEnregistrements() {
      return jeuxEnregistres.size();
   }
   
   /**
    * 
    * @return
    */
   public ArrayList<Enregistrement> getJeuxEnregistres() {
      return jeuxEnregistres;
   }
}
