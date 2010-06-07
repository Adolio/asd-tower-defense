
import java.net.InetAddress;

import javax.swing.UIManager;

import outils.Configuration;

import models.jeu.Jeu_Solo;
import models.terrains.ElementTD_Coop;
import models.terrains.ElementTD_Versus_4;
import models.terrains.SimpleFiveVersus;
import models.terrains.Terrain;
import vues.Fenetre_MenuPrincipal;

/**
 * Classe principale du jeu Tower Defense.
 * 
 * Cette classe contient la methode main du programme.
 * Elle s'occupe de configurer le style de l'interface graphique
 * et d'ouvrir le menu principal du programme.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 */
public class Main
{
   /**
    * Programme principal.
    * 
    * Configure le style de l'interface et ouvre le menu principal.
    * 
    * @param args Les arguments fournis au programme principal.
    */
   public static void main(String[] args) 
   {
       // essaye de mettre le nouveau look and feel "Nimbus" fourni par Java
       for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels())
           if ("Nimbus".equals(laf.getName())) 
               try 
               {
                   UIManager.setLookAndFeel(laf.getClassName());
               } 
               catch (Exception e) 
               {
                   /* 
                    * On fait rien, c'est pas grave. 
                    * C'est juste le look and feel qui n'est pas installe.
                    */ 
               } 
                  
      // TODO [A EFFACER]
      /*
      // a reserializer pour le son
      Terrain t1 = new ElementTD_Coop(new Jeu_Solo());      
      Terrain.serialiser(t1); 
               
      Terrain t2 = new SimpleFiveVersus(new Jeu_Solo());      
      Terrain.serialiser(t2);
      
      Terrain t3 = new ElementTD_Versus_4(new Jeu_Solo());      
      Terrain.serialiser(t3);
      */         
              
      // creation du menu principal
	  new Fenetre_MenuPrincipal();
   }
}
