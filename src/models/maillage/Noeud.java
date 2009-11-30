package models.maillage;

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
public class Noeud
{
    // La coordonnée x par rapport au repère de la fenêtre graphique
    private int x;
    // La coordonnée y par rapport au repère de la fenêtre graphique
    private int y;
    // Ce point (noeud) est-il franchissable depuis les airs?
    private boolean franchissableAirs;
    // Ce noeud est-il actif?
    private boolean actif;

    /**
     * @param x
     * @param y
     * @param franchissableAirs
     * @param actif
     * @throws IllegalArgumentException
     */
    public Noeud(int x, int y, boolean franchissableAirs, boolean actif)
            throws IllegalArgumentException
    {
        verifierCoordonnees(x, y);
        this.x = x;
        this.y = y;
        this.franchissableAirs = franchissableAirs;
        this.actif = actif;
    }

    /**
     * Getter pour le champ <tt>x</tt>
     * 
     * @return La valeur du champ <tt>x</tt>
     */
    public int getX()
    {
        return x;
    }

    /**
     * Getter pour le champ <tt>y</tt>
     * 
     * @return La valeur du champ <tt>y</tt>
     */
    public int getY()
    {
        return y;
    }

    /**
     * Getter pour le champ <tt>franchissableAirs</tt>
     * 
     * @return La valeur du champ <tt>franchissableAirs</tt>
     */
    public boolean isFranchissableAirs()
    {
        return franchissableAirs;
    }

    /**
     * Getter pour le champ <tt>actif</tt>
     * 
     * @return La valeur du champ <tt>actif</tt>
     */
    public boolean isActif()
    {
        return actif;
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
     * Setter pour le champ <tt>franchissableAirs</tt>
     * 
     * @param franchissableAirs
     *            La valeur qu'on veut attribuer au champ
     *            <tt>franchissableAirs</tt>
     */
    public void setFranchissableAirs(boolean franchissableAirs)
    {
        this.franchissableAirs = franchissableAirs;
    }

    /**
     * Setter pour le champ <tt>actif</tt>
     * 
     * @param actif
     *            La valeur qu'on veut attribuer au champ <tt>actif</tt>
     */
    public void setActif(boolean actif)
    {
        this.actif = actif;
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
        {
            throw new IllegalArgumentException("La coordonnée x est négative!");
        }
        if (y < 0)
        {
            throw new IllegalArgumentException("La coordonnée y est négative!");
        }
    }

}
