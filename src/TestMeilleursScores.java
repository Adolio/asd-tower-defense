import models.outils.*;

/**
 * Fichier : TestMeilleursScores.java
 * <p>
 * Encodage : UTF-8
 * <p>
 * Cette classe...
 * <p>
 * Remarques :
 * 
 * @author Lazhar Farjallah
 * @version 16 déc. 2009
 * @since jdk1.6.0_16
 */
public class TestMeilleursScores
{
   
   /**
    * Programme principal.
    * 
    * @param args
    *        Les arguments fournis au programme principal.
    */
   public static void main (String[] args)
   {
      // ----------------------------------------------------------------------------
      // Partie déclarative (constantes, variables).
      // ----------------------------------------------------------------------------
      MeilleursScores scores = new MeilleursScores("test.scores");
      
      // ----------------------------------------------------------------------------
      // Présentation et entrée des données par l'utilisateur.
      // ----------------------------------------------------------------------------
      System.out.print("\n<----------------------------------------------------\n"
            + " TestMeilleursScores.java : ......... \n"
            + "-----------------------------------------------------\n\n");
      
      // ----------------------------------------------------------------------------
      // Calcul et affichage du résultat.
      // ----------------------------------------------------------------------------
      for (int i=0; i<15; ++i) {
         scores.ajouterMeilleurScore("Joueur" + Integer.toString(i), 191919 * i);
         System.out.println(scores);
      }
      
      // ----------------------------------------------------------------------------
      // Fin du programme.
      // ----------------------------------------------------------------------------
      System.out.print("\n-----------------------------------------------------\n"
            + "Fin du programme principal.\n"
            + "----------------------------------------------------->\n\n");
   }
   
}
