package models.creatures;

import java.awt.Image;
import java.awt.Toolkit;

/**
 * Classe de gestion d'une creature.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Creature
 */
public class GrandeAraignee extends Creature
{
	private static final long serialVersionUID = 1L;
	private static final Image IMAGE;
	
	static
	{
		IMAGE = Toolkit.getDefaultToolkit().getImage("img/creatures/grandeAraignee.png");
	}
	
	/**
	 * Constructeur de base.
	 * 
	 * @param santeMax la sante max de cette creature
	 * @param nbPiecesDOr le nombre de pieces d'or de cette creature
	 * @param vitesse vitesse de la creature
	 */
	public GrandeAraignee(long santeMax, int nbPiecesDOr, double vitesse)
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
	public GrandeAraignee(int x, int y, long santeMax, int nbPiecesDOr, double vitesse)
	{
		super(x, y, 32, 32, santeMax, nbPiecesDOr, vitesse,
		        Creature.TYPE_TERRIENNE, IMAGE, "Grande Araignee");
	}

	/**
	 * Permet de copier la creature
	 */
	public Creature copier()
	{
		return new GrandeAraignee(x,y,getSanteMax(),getNbPiecesDOr(),getVitesseNormale());
	}
}
