package models.tours;

import java.awt.Color;

public class TourDeGlace extends Tour
{
	private static final long serialVersionUID = 1L;
	public static final Color COULEUR = Color.BLUE;
	
	public TourDeGlace()
	{
		super(0, 0, 20, 20, COULEUR,"Tour de glace",20);
		
		description = "La tour de glace est une \ntour rapide qui ralenti les ennemis";
	}
	
	public void ameliorer()
	{
		// le prix total est ajouté du prix d'achat de la tour
		prixTotal += prixAchat;
		
		switch(niveau)
		{
			case 1 :
				prixAchat 	= 20;
				degats 		= 20;
				break;
			case 2 :
				prixAchat 	= 40;
				degats 		= 40;
				break;
		}
		
		niveau++;
	}
}
