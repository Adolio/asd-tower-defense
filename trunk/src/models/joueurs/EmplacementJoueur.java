package models.joueurs;

import java.awt.Rectangle;
import java.io.Serializable;

public class EmplacementJoueur implements Serializable
{
    private static final long serialVersionUID = 1L;
    transient Joueur joueur;
    Rectangle zoneDeConstruction;
    
    public EmplacementJoueur(Rectangle zoneDeConstruction)
    {
        this.zoneDeConstruction = zoneDeConstruction;
    }
    
    public void setJoueur(Joueur joueur)
    {
        this.joueur = joueur;
    }
}
