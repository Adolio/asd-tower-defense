package models.maillage;

import java.awt.Point;

/**
 * Fichier : Noeud.java
 * <p>
 * Encodage : UTF-8
 * 
 * <p>
 * Cette classe modélise un noeud du graphe. Elle étend la classe Point pour
 * permettre un dessin plus facile et une encapsulation des coordonnées x et y
 * en pixel.
 * <p>
 * Remarques :
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 30 nov. 2009
 * @since jdk1.6.0_16
 */
public class Noeud extends Point
{

	/**
	 * Fanion pour savoir si le noeud est actif ou pas. Inactif par défaut,
	 * obligatoirement.
	 */
	private boolean actif = false;

	/**
	 * Largeur du noeud
	 */
	protected final int LARGEUR_NOEUD;

	/**
	 * @param x
	 *            La coordonnée x du centre du noeud, en pixel.
	 * @param y
	 *            La coordonnée y du centre du noeud, en pixel.
	 * @param cote
	 */
	public Noeud(int x, int y, int cote)
	{
		this.x = centre(x, cote);
		this.y = centre(y, cote);
		this.LARGEUR_NOEUD = cote;
	}

	/**
	 * Calcul le centre du noeud contenant la coordonnée passée en paramêtre.
	 * 
	 * @param i La coordonnée quelconque en pixel
	 * @param cote La largeur du noeud en pixel
	 * @return La coordonnée du centre du noeud en pixel
	 */
	public static int centre(int i, int cote)
	{
		return i - (i % cote) + (cote / 2);
	}
	
	/**
	 * Converti un point quelconque en pixel en la coordonnée modale du noeud correspondand
	 * @param x
	 * @param cote
	 * @return
	 */
	public static int pixelANodale(int x, int cote){
		return (centre(x,cote) - (cote/2)) / cote;
	}

	/**
	 * 
	 * @param noeud
	 * @return
	 */
	public static int[] convert(Noeud noeud)
	{
		int[] r = { centre(noeud.x, noeud.LARGEUR_NOEUD),
				centre(noeud.y, noeud.LARGEUR_NOEUD) };
		return r;
	}

	/**
	 * Retourne les coordonnées nodales d'un noeud passé en paramètre, avec des
	 * deltas x et y
	 * 
	 * @param noeud
	 *            Le noeud à convertir
	 * @param deltaX
	 *            Le delta sur l'axe des x
	 * @param deltaY
	 *            Le delta sur l'axe des y
	 * @return un tableau de deux éléments contenant les coordonnées converties.
	 */
	public static int[] coordonnee(Noeud noeud, int deltaX, int deltaY)
	{
		int[] r = convert(noeud);
		r[0] = (r[0] - deltaX) / noeud.LARGEUR_NOEUD;
		r[1] = (r[1] - deltaY) / noeud.LARGEUR_NOEUD;
		return r;
	}

	/**
	 * Retourne les coordonnées nodales d'un noeud passé en paramètre
	 * 
	 * @param noeud
	 *            Le noeud à convertir
	 * @return un tableau de deux éléments contenant les x et y.
	 */
	public static int[] coordonnee(Noeud noeud)
	{
		return coordonnee(noeud, 0, 0);
	}

	public String toString()
	{
		return super.toString() + " coté : " + LARGEUR_NOEUD;
	}

	/**
	 * Défini le noeud comme actif, ou pas.
	 * 
	 * @param actif
	 *            True si le noeud est actif, False sinon.
	 */
	public void setActif(boolean actif)
	{
		this.actif = actif;
	}

	/**
	 * Retourne True si le noeud est actif, False sinon.
	 * 
	 * @return True si le noeud est actif, False sinon.
	 */
	public boolean isActif()
	{
		return actif;
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Noeud noeud)
	{
		return x == noeud.x && y == noeud.y && LARGEUR_NOEUD == noeud.LARGEUR_NOEUD;
	}

}
