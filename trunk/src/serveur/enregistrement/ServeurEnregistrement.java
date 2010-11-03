package serveur.enregistrement;

import outils.Configuration;
import reseau.*;

/**
 * Classe de lancement du serveur d'enregistrement 
 * 
 * @author lazhar
 */
public class ServeurEnregistrement
{
   /**
    * Programme principal du serveur d'enregistrement. Permet de le lancer.
    * 
    * @param args
    */
   public static void main(String[] args)
   {
      new SEInscription(new Port(Configuration.getPortSE())).lancer();
   }
}
