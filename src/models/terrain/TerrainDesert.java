package models.terrain;

import java.awt.Rectangle;

public class TerrainDesert extends Terrain
{
	public TerrainDesert()
	{
		super(500, 500, "../img/cartes/desert.png");
		
		// tour du terrain
		ajouterMur(new Rectangle(0,0,20,380)); 		// gauche
		ajouterMur(new Rectangle(0,0,500,20)); 		// haut
		ajouterMur(new Rectangle(0,480,500,20)); 	// bas
		ajouterMur(new Rectangle(480,120,20,380)); 	// droit
	}
	
}
