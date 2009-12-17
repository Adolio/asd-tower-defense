package models.maillage;

import java.awt.Point;

/**
 * Fichier : PointNodal.java
 * 
 * <p>
 * But : Une abstraction pour représenter un point dans le système de coordonnée
 * nodale à partir d'un point dans les coordonnées pixélaires.
 * <p>
 * Remarques :
 * 
 * @author Pierre-Dominique Putallaz
 * @version 17 déc. 2009
 * @since jdk1.6.0_16
 */
public class PointNodal extends Point
{
	protected final int LARGEUR_NOEUD;

	public PointNodal(int x, int y, int cote)
	{
		this.x = convert(x, cote);
		this.y = convert(y, cote);
		this.LARGEUR_NOEUD = cote;
	}

	/**
	 * Converti un point en coordonnées modales.
	 * 
	 * @param i
	 * @param cote
	 * @return
	 */
	public static int convert(int i, int cote)
	{
		return i - (i % cote) + (cote / 2);
	}

	public String toString()
	{
		return super.toString() + " coté : " + LARGEUR_NOEUD;
	}

}
