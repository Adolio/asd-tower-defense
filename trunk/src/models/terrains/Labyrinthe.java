package models.terrains;

import java.awt.Rectangle;

/**
 * Classe de gestion d'un terrain dans le desert.
 * 
 * Cette classe herite de la classe Terrain de base.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Terrain
 */
public class Labyrinthe extends Terrain
{
	/**
	 * Constructeur du terrain dans le desert
	 */
	public Labyrinthe()
	{
		super(500, 500, "img/cartes/spiral3d.png", 
			  new Rectangle(460,40,20,80),
			  new Rectangle(320,280,20,60));
		
		
		/* definition des murs du labyrinthe :
		 
		 		 1
		|------------------
		|
		|           5
		|	 |-------------
		|	 |	   9	  |
	  2 |	6|  -----|	  |
		|	 |		 | 8  | 4
		|    |-------|	  |
		|		 7		  |
		|-----------------|
				3
		*/
	
		ajouterMur(new Rectangle(20,0,460,20)); 	// 1
		ajouterMur(new Rectangle(0,0,20,500));	 	// 2	
		ajouterMur(new Rectangle(20,480,440,20)); 	// 3
		ajouterMur(new Rectangle(460,140,20,360));  // 4
		ajouterMur(new Rectangle(140,140,340,20));  // 5
		ajouterMur(new Rectangle(120,140,20,240));	// 6
		ajouterMur(new Rectangle(140,360,200,20));	// 7
		ajouterMur(new Rectangle(340,240,20,140));	// 8
		ajouterMur(new Rectangle(240,240,100,20));	// 9
	}
}
