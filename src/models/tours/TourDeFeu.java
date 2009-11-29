package models.tours;

import java.awt.Color;

public class TourDeFeu extends Tour
{
	private static final long serialVersionUID = 1L;
	public static final Color COULEUR = Color.RED;
	public TourDeFeu()
	{
		super(0, 0, 20, 20, COULEUR,"Tour de feu",10);
		
		description = "La tour de feu est une tour \nqui fait beaucoup degats";
	}
	
	public void ameliorer()
	{
		niveau++;
	}
}
