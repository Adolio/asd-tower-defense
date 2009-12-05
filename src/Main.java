import javax.swing.UIManager;
import models.jeu.Jeu;
import models.terrains.*;
import models.tours.Tour;
import models.tours.TourDeFeu;
import vues.Fenetre_Jeu;

/**
 * Classe principale du Tower Defense
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
	   
	  TerrainDesert terrain = new TerrainDesert();
	  
	  // création du jeu
	  Jeu jeu = new Jeu(terrain);
	  
	  // création de la fenetre du jeu
	  new Fenetre_Jeu(jeu);
	  
	  /*
	  // Ajout des tours de base, pour test
		for(int i = 0;i<5;++i){
			Tour tour1 = new TourDeFeu(jeu);
			Tour tour2 = new TourDeFeu(jeu);
			
			tour1.x=380+i*20;
			tour1.y=120;
			jeu.poserTour(tour1);
			
			tour2.x = 360;
			tour2.y=20+i*20;
			if(i!=0)jeu.poserTour(tour2);
			
		}*/
   }
}
