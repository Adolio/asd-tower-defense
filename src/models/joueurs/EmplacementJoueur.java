package models.joueurs;

import java.awt.Rectangle;

public class EmplacementJoueur
{
    Joueur joueur;
    
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
