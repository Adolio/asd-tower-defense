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
	 * Construit à noeud à partir des paramètres donnés. Restriction des droits
	 * au paquet seulement.
	 * 
	 * @param x
	 *            La coordonnée x du centre du noeud, en pixel.
	 * @param y
	 *            La coordonnée y du centre du noeud, en pixel.
	 * @param cote
	 */
	Noeud(int x, int y, int cote)
	{
		this.x = centre(x, cote);
		this.y = centre(y, cote);
		this.LARGEUR_NOEUD = cote;
	}

	/**
	 * Constructeur de copie. Restriction des droits au paquet seulement.
	 * 
	 * @param noeud
	 *            Le noeud à copie.
	 */
	Noeud(Noeud noeud)
	{
		this.x = noeud.x;
		this.y = noeud.y;
		this.LARGEUR_NOEUD = noeud.LARGEUR_NOEUD;
		this.actif = noeud.actif;
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
	 * Compare le noeud courant à un noeud donné en paramêtre.
	 * 
	 * @param noeud
	 *            Le noeud à comparer.
	 * @return True si les deux noeuds sont égaux, false sinon.
	 * @see Object#equals(Object)
	 */
	public boolean equals(Noeud noeud)
	{
		/*
		 * Test du paramêtre.
		 */
		if (noeud == null)
			throw new IllegalArgumentException(
					"Le noeud passé en paramêtre ne peut pas être null");
		// Retour de la valeur boolean calculée.
		return x == noeud.x && y == noeud.y
				&& LARGEUR_NOEUD == noeud.LARGEUR_NOEUD;
	}

	public String toString()
	{
		return super.toString() + " coté : " + LARGEUR_NOEUD + "actif : "
				+ actif;
	}

	/**
	 * Défini le noeud comme actif, ou pas. Restriction au droit de package, il
	 * ne faut pas que quelqu'un d'externe puisse désactiver un noeud sans
	 * passer par le maillage.
	 * 
	 * @param actif
	 *            True si le noeud est actif, False sinon.
	 */
	void setActif(boolean actif)
	{
		this.actif = actif;
	}

	/**
	 * Calcul le centre du noeud contenant la coordonnée passée en paramêtre.
	 * 
	 * @param i
	 *            La coordonnée quelconque en pixel
	 * @param cote
	 *            La largeur du noeud en pixel
	 * @return La coordonnée du centre du noeud en pixel
	 */
	public static int centre(int i, int cote)
	{
		return i - (i % cote) + (cote / 2);
	}

	/**
	 * Converti un point quelconque en pixel en la coordonnée modale du noeud
	 * correspondant
	 * 
	 * @param x
	 *            Le point quelconque en pixel
	 * @param cote
	 *            Le coté du noeud
	 * @return La coordonnée nodale correspondante.
	 */
	public static int pixelANodale(int x, int cote)
	{
		return (centre(x, cote) - (cote / 2)) / cote;
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
		/*
		 * Test du paramètre
		 */
		if (noeud == null)
			throw new IllegalArgumentException(
					"Le noeud passé en paramêtre ne peut pas être null");
		// Calcul de la coordonnée
		int[] r = new int[2];
		r[0] = (noeud.x - deltaX) / noeud.LARGEUR_NOEUD;
		r[1] = (noeud.y - deltaY) / noeud.LARGEUR_NOEUD;
		return r;
	}
}
