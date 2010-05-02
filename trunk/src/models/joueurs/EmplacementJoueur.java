package models.joueurs;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.Serializable;

public class EmplacementJoueur implements Serializable
{
    private static final long serialVersionUID = 1L;
    transient Joueur joueur;
    private Color couleur;
    
    Rectangle zoneDeConstruction;
    
    public EmplacementJoueur(Rectangle zoneDeConstruction)
    {
        this.zoneDeConstruction = zoneDeConstruction;
        this.couleur = Color.BLACK;
    }
    
    public EmplacementJoueur(Rectangle zoneDeConstruction, Color couleur)
    {
        this.zoneDeConstruction = zoneDeConstruction;
        this.couleur = couleur;
    }
    
    public void setJoueur(Joueur joueur)
    {
        this.joueur = joueur;
    }
    
    public Color getCouleur()
    {
        return couleur;
    }
}
