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
	// Actif par defaut, obligatoirement.
	private boolean actif = true;
	
    /**
     * @param x
     * @param y
     * @throws IllegalArgumentException
     */
    public Noeud(int x, int y) throws IllegalArgumentException
    {
        verifierCoordonnees(x, y);
        this.x = x;
        this.y = y;
    }
    
    /**
     * Constructeur 
     * @param source
     */
    public Noeud (Noeud source) {
       this(source.x, source.y);
    }
    
    /**
     * 
     */
    public String toString() {
        return "[" + x + ", " + y + "] Actif : "+actif;
    }
    
    public void setActif(boolean actif){
    	this.actif = actif;
    }
    
    public boolean isActif(){
    	return actif;
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
