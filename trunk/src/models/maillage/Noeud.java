package models.maillage;

import java.awt.Point;

/**
 * Fichier : Noeud.java
 * <p>
 * Encodage : UTF-8
 * 
 * <p>
 * Cette classe...
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
	
	// Fanion pour savoir si le noeud est actif ou pas.
	private boolean actif = true;
	
    /**
     * @param x
     * @param y
     * @param franchissableAirs
     * @param actif
     * @throws IllegalArgumentException
     */
    public Noeud(int x, int y) throws IllegalArgumentException
    {
        verifierCoordonnees(x, y);
        this.x = x;
        this.y = y;
    }
    
    /**
     * @param source
     */
    public Noeud (Noeud source) {
       this(source.x, source.y);
    }

    /**
     * Getter pour le champ <tt>x</tt>
     * 
     * @return La valeur du champ <tt>x</tt>
     */
    public double getX()
    {
        return x;
    }

    /**
     * Getter pour le champ <tt>y</tt>
     * 
     * @return La valeur du champ <tt>y</tt>
     */
    public double getY()
    {
        return y;
    }

    /**
     * Setter pour le champ <tt>x</tt>
     * 
     * @param x
     *            La valeur qu'on veut attribuer au champ <tt>x</tt>
     */
    public void setX(int x)
    {
        this.x = x;
    }

    /**
     * Setter pour le champ <tt>y</tt>
     * 
     * @param y
     *            La valeur qu'on veut attribuer au champ <tt>y</tt>
     */
    public void setY(int y)
    {
        this.y = y;
    }
    
    /**
     * 
     */
    public String toString() {
        return "[" + x + ", " + y + "] Actif : "+actif;
    }

    /**
     * 
     * @param x
     * @param y
     * @throws IllegalArgumentException
     */
    private static void verifierCoordonnees(int x, int y)
            throws IllegalArgumentException
    {
        if (x < 0)     
            throw new IllegalArgumentException("La coordonnée x est négative!");
        
        if (y < 0)
            throw new IllegalArgumentException("La coordonnée y est négative!");
        
    }

}
