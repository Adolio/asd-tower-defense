import models.outils.*;

/**
 * Fichier  : TestSon.java
 * <p>
 * Encodage : UTF-8
 *
 * <p> Cette classe...
 * <p> Remarques :
 *
 * @author  Lazhar Farjallah
 * @version 17 déc. 2009
 * @since   jdk1.6.0_16
 */
public class TestSon
{
   
   /**
    * Programme principal.
    * 
    * @param args Les arguments fournis au programme principal.
    * @throws InterruptedException 
    * @throws JavaLayerException 
    * @throws FileNotFoundException 
    */
   public static void main (String[] args) throws InterruptedException
   {
      //----------------------------------------------------------------------------
      // Partie déclarative (constantes, variables).
      //----------------------------------------------------------------------------
      Musique musique1 = new Musique("snd/arc.mp3");
      Musique musique2 = new Musique("snd/canon.mp3");
      
      //----------------------------------------------------------------------------
      // Présentation et entrée des données par l'utilisateur.
      //----------------------------------------------------------------------------
      System.out.print("\n<----------------------------------------------------\n"
            + " TestSon.java : ......... \n"
            + "-----------------------------------------------------\n\n");
      
      //----------------------------------------------------------------------------
      // Calcul et affichage du résultat.
      //----------------------------------------------------------------------------
      musique1.lire(7);
      musique2.lire(3);
      
      //----------------------------------------------------------------------------
      // Fin du programme.
      //----------------------------------------------------------------------------
      System.out.print("\n-----------------------------------------------------\n"
            + "Fin du programme principal.\n"
            + "----------------------------------------------------->\n\n");
   }
   
}
