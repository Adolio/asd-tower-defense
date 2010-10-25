package serveur.enregistrement;

import java.io.IOException;
import java.util.ArrayList;

import reseau.*;

/**
 * 
 * @author lazhar
 */
public class SEInscription
{
   
   private static ArrayList<Enregistrement> jeuxEnregistres = new ArrayList<Enregistrement>();
   private Port port;
   private boolean debug;
   private CanalTCP canal;
   
   /**
    * 
    * @param port
    * @param debug
    */
   public SEInscription(Port port, boolean debug)
   {
      this.port = port;
      this.debug = debug;
   }
   
   /**
    * 
    */
   public void lancer()
   {
      try
      {
         port.reserver();
         
         while (true)
         {
            if(debug)
                System.out.println("\n+ Connexion d'un client...");
            
            // Fonction bloquante qui attend que quelqu'un se connecte
            creerCanal();
            
            (new Thread(new SEConnexion(canal))).start();
         }
      } 
      catch (IOException e)
      {
         System.err.println("Serveur d'enregistrement déjà lancé !");
      }
   }
   
   /**
    * 
    */
   private void creerCanal()
   {
      try{
         canal = new CanalTCP(port);
      } 
      catch (CanalException ce){
        
          System.err.println("\tProbleme de connexion : " + ce.getMessage());
      }
   }
   
   /**
    * 
    * @param e
    * @return
    */
   public static synchronized boolean ajouterEnregistrement(Enregistrement e)
   {
      // TODO : surcharger les contains() pour pas que ce soit les réf. qui sont
      // comparées
      if (!jeuxEnregistres.contains(e))
      {
         jeuxEnregistres.add(e);
         
         System.out.println("Nb d'enreg. : " + jeuxEnregistres.size());
         
         return true;
      }
      return false;
   }
   
   /**
    * Permet de supprimer un enregistrement
    * 
    * @param e l'enregistrement a supprimer
    */
   public static synchronized void enleverEnregistrement(Enregistrement e)
   {
      jeuxEnregistres.remove(e);
   }
   
   /**
    * Permet de recuperer le nombre d'enregistrements
    * 
    * @return le nombre d'enregistrements
    */
   public static synchronized int getNombreEnregistrements()
   {
      return jeuxEnregistres.size();
   }
   
   /**
    * Permet de recuperer les jeux enregistres
    * 
    * @return les jeux enregistres
    */
   public static synchronized ArrayList<Enregistrement> getJeuxEnregistres()
   {
      return jeuxEnregistres;
   }
}
