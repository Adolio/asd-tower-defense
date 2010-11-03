package serveur.enregistrement;

import java.io.IOException;
import java.util.ArrayList;
import reseau.*;

/**
 * Classe de gestion d'un serveur d'enregistrement de parties reseaux
 * 
 * @author lazhar
 * @author Aurelien Da Campo
 */
public class SEInscription
{
   
   private static ArrayList<Enregistrement> jeuxEnregistres = new ArrayList<Enregistrement>();
   private Port port;
   private static final boolean debug = true;
   private CanalTCP canal;
   
   /**
    * Constructeur
    * 
    * @param port
    * @param debug
    */
   public SEInscription(Port port)
   {
      this.port = port;
   }
   
   /**
    * Permet de lancer le serveur
    * 
    * 1) reservation du port
    * 2) attente de connexion d'un client
    *   2.1) Creation d'une tache de traitement du client
    */
   public void lancer()
   {
      try
      {
         port.reserver();
         
         System.out.println("Le serveur d'enregistrement a bien ete lance.");
         System.out.println("Attente de connexions...");
         
         while (true)
         {
             // Fonction bloquante qui attend que quelqu'un se connecte
            creerCanal();
            
            if(debug)
                System.out.println("\n+ Connexion d'un client!");
            
            (new Thread(new SEConnexion(canal))).start();
         }
      } 
      catch (IOException e)
      {
         System.err.println("Serveur d'enregistrement deja lance !");
      }
   }
   
   /**
    * Permet de creer un canal
    * 
    * Methode bloquante sur l'arrive d'un client
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
    * Permet d'ajouter un serveur de jeu.
    * 
    * @param e l'enregistrement (serveur de jeu)
    * @return
    */
   public static synchronized boolean ajouterEnregistrement(Enregistrement e)
   {
      // TODO : surcharger les contains() pour pas que ce soit les réf. qui sont
      // comparées
      if (!jeuxEnregistres.contains(e))
      {
         jeuxEnregistres.add(e);
         
         if(debug)
             System.out.println("+ Ajout d'un enregistrement, nb enr. : " 
                     + jeuxEnregistres.size());
         
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
      if(debug)
          System.out.println("- Suppression d'un enregistrement");
      
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
