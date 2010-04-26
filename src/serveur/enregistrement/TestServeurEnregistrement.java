package serveur.enregistrement;

import reseau.*;

/**
 * 
 * @author lazhar
 *
 */
public class TestServeurEnregistrement {
   
   /**
    * Programme principal du serveur d'enregistrement. Permet de le lancer.
    * @param args
    */
   public static void main (String[] args)
   {
      switch (args.length)
      {
      // Il y a exactement 1 argument donné au programme.
      case 1 :
         (new SEInscription(new Port(args[0]), false)).lancer();
         break;
      // Il y a exactement 2 arguments donnés au programme.
      case 2 :
         (new SEInscription(new Port(args[0]), true)).lancer();
         break;
      default :
         System.out.println("Usage : java ServeurEnregistrement <numeroPort> [true|false]");
         break;
      }
   }
}
