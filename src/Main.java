import javax.swing.UIManager;
import vues.Fenetre_MenuPrincipal;

/**
 * Classe principale du jeu Tower Defense.
 * 
 * Cette classe contient la methode main du programme.
 * Elle s'occupe de configurer le style de l'interface graphique
 * et d'ouvrir la premiere fenetre du programme.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 */
public class Main
{
   /**
    * Programme principal.
    * 
    * Configure le style de l'interface et ouvre la premiere fenetre.
    * 
    * @param args Les arguments fournis au programme principal.
    */
   public static void main(String[] args) 
   {
      // modification du style de l'interface
	  try 
	  { 
		  UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); 
	  } 
	  catch (Exception e) 
	  { 
		  /* 
		   * On fait rien, c'est pas grave. 
		   * C'est juste le look and feel qui n'est pas installe.
		   */ 
	  }

	  new Fenetre_MenuPrincipal();
   }
}
