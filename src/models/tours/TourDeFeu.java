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
		// le prix total est ajouté du prix d'achat de la tour
		prixTotal += prixAchat;
		
		prixAchat 	+= 10;
		degats    	*= 2;
		rayonPortee += 50;
		
		/*
		switch(niveau)
		{
			case 1 :
				prixAchat 	= 20;
				degats 		= 20;
				rayonPortee = 200;
				break;
			case 2 :
				prixAchat 	= 40;
				degats 		= 40;
				rayonPortee = 300;
				break;
		}
		*/
		
		niveau++;
	}
}
