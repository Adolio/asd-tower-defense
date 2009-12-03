
import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;

import models.maillage.*;

/**
 * Fichier  : Test.java
 * <p>
 * Encodage : UTF-8
 *
 * <p> Cette classe...
 * <p> Remarques :
 *
 * @author  Lazhar Farjallah
 * @version 30 nov. 2009
 * @since   jdk1.6.0_16
 */
public class TestMaillage
{
    
    private static Scanner scanner = new Scanner(System.in);
    
    /**
     * Programme principal.
     * 
     * @param args Les arguments fournis au programme principal.
     */
    public static void main(String[] args)
    {
        //----------------------------------------------------------------------------
        // Partie déclarative (constantes, variables).
        //----------------------------------------------------------------------------
        Maillage maillage;
        long temps;
        
        //----------------------------------------------------------------------------
        // Présentation et entrée des données par l'utilisateur.
        //----------------------------------------------------------------------------
        System.out.print("\n<----------------------------------------------------\n"
                + " Test du maillage : ......... \n"
                + "-----------------------------------------------------\n\n");
        
        //----------------------------------------------------------------------------
        // Calcul et affichage du résultat.
        //----------------------------------------------------------------------------
        temps = System.currentTimeMillis();
        maillage = new Maillage(10, 10);
        temps = System.currentTimeMillis() - temps;
        System.out.println(maillage + "\n");
        System.out.println("Temps de génération du maillage : " +
                           temps / 1000.0 + " [sec.]");
        System.out.println("Mémoire utilisée                : " +
                           Math.round(memoireUtilisee() / (1024.0 * 1024.0) * 100)
                                                                 / 100.0 + " [Mb]");
        
        temps = System.currentTimeMillis();
        try {
            ArrayList<Point> points = maillage.plusCourtChemin(3, 3, 1, 2);
        }
        catch (Exception e) {
            System.out.println("Merde!");
        }
        temps = System.currentTimeMillis() - temps;
        System.out.println("Temps algorithme ACPC : " + temps / 1000.0 + " [sec.]");
        
        //----------------------------------------------------------------------------
        // Fin du programme.
        //----------------------------------------------------------------------------
        //System.out.print("\n\nAppuyez sur une touche pour terminer...");
        //scanner.nextLine();
        System.out.print("\n-----------------------------------------------------\n"
                + "Fin du programme principal.\n"
                + "----------------------------------------------------->\n\n");
    }
    
    private static long memoireUtilisee()
    {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }
    
}
