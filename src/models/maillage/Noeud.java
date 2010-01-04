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
	 * Fanion pour savoir si le noeud est actif ou pas. Actif par défaut,
	 * obligatoirement.
	 */
	private boolean actif = true;

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

	public static int[] convert(Noeud noeud)
	{
		int[] r = { convert(noeud.x, noeud.LARGEUR_NOEUD),
				convert(noeud.y, noeud.LARGEUR_NOEUD) };
		return r;
	}
	
	public static int[] coordonnee(Noeud noeud){
		int[] r = convert(noeud);
		r[0] = r[0]/noeud.LARGEUR_NOEUD;
		r[1] = r[1]/noeud.LARGEUR_NOEUD;
		return r;
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

}
