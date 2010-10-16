package models.creatures;

import java.awt.Image;
import java.awt.Toolkit;

/**
 * Classe de gestion d'un mouton.
 * 
 * @author Aurélien Da Campo
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Creature
 */
public class Paysan extends Creature
{
	private static final long serialVersionUID = 1L;
	private static final Image IMAGES[];
	
	
	static
	{
		IMAGES = new Image[]
		{        
		        Toolkit.getDefaultToolkit().getImage("img/creatures/paysan/paysan_0_20.png"),
		        Toolkit.getDefaultToolkit().getImage("img/creatures/paysan/paysan_1_20.png")
		};
	}
	
	/**
	 * Constructeur de base.
	 * 
	 * @param santeMax la sante max de cette creature
	 * @param nbPiecesDOr le nombre de pieces d'or de cette creature
	 * @param vitesse vitesse de la creature
	 */
	public Paysan(long santeMax, int nbPiecesDOr, double vitesse)
	{
		this(0, 0, santeMax, nbPiecesDOr,vitesse);
	}

	/**
	 * Constructeur avec position initiale.
	 * 
	 * @param x la position sur l'axe X de la creature
	 * @param y la position sur l'axe Y de la creature
	 * @param santeMax la sante max de cette creature
	 * @param nbPiecesDOr le nombre de pieces d'or de cette creature
	 * @param vitesse vitesse de la creature
	 */
	public Paysan(int x, int y, long santeMax, int nbPiecesDOr, double vitesse)
	{
		super(x, y, IMAGES[0].getWidth(null), IMAGES[0].getHeight(null), santeMax, nbPiecesDOr, vitesse,
		        Creature.TYPE_TERRIENNE, IMAGES[0], "Paysan");
	}

	/**
	 * Permet de copier la creature
	 */
	public Creature copier()
	{
		return new Paysan(x,y,getSanteMax(),getNbPiecesDOr(),getVitesseNormale());
	}
	
	
	long temps = 0;
	int iImage = 0;
	public void action(long tempsPasse)
	{
	    super.action(tempsPasse);
	    
	    temps += tempsPasse;
	    
	    if(temps > 300)
	    {
	        image = IMAGES[iImage++ % IMAGES.length];
	        temps = 0;
	    }
	}
	
}
