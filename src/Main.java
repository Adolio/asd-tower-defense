
import javax.swing.UIManager;
import models.outils.GestionnaireSons;
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
 
      GestionnaireSons.setVolumeSysteme(GestionnaireSons.VOLUME_PAR_DEFAUT);         
           
      // creation du menu principal
      new Fenetre_MenuPrincipal();
   }
}
