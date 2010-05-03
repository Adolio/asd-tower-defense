package models.joueurs;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.Serializable;

public class EmplacementJoueur implements Serializable
{
    private static final long serialVersionUID = 1L;
    transient Joueur joueur;
    private Color couleur;
    private static int compteur = 0;
    private int id;
    Rectangle zoneDeConstruction;
    
    public EmplacementJoueur(Rectangle zoneDeConstruction)
    {
        this(zoneDeConstruction,Color.BLACK);
    }
    
    public EmplacementJoueur(Rectangle zoneDeConstruction, Color couleur)
    {
        this.zoneDeConstruction = zoneDeConstruction;
        this.couleur = couleur;
        this.id = compteur++;
    }
    
    /**
     * Permet de modifier le joueur.
     * 
     * Attention : la mise a jour de l'emplacement du joueur sera affectué.
     * 
     * @param joueur le nouveau joueur, null pour retirer l'ancien joueur
     */
    public void setJoueur(Joueur joueur)
    {
        // suppression
        if(joueur == null)
        { 
            // seulement s'il y avait un joueur avant
            if(this.joueur != null)
            {
                this.joueur.setEmplacementJoueur(null);
                this.joueur = null;
            }
        }
        // modification
        else
        {
            // occupe
            if(this.joueur != null)
                throw new IllegalArgumentException("Emplacement occupée");
            // ok
            else
            { 
                // on libère l'ancien emplacement
                if(joueur.getEmplacement() != null)
                    joueur.getEmplacement().setJoueur(null);
                
                // mise a jour
                this.joueur = joueur;
                joueur.setEmplacementJoueur(this);
            }
        }   
    }
    
    public Color getCouleur()
    {
        return couleur;
    }
    
    public Rectangle getZoneDeConstruction()
    {
        return zoneDeConstruction;
    }

    public Joueur getJoueur()
    {
        return joueur;
    }
    
    public String toString()
    {
        return "Zone "+id;
    }

    public void retirerJoueur()
    {
        // seulement s'il y avait un joueur avant
        if(this.joueur != null)
        {
            this.joueur.quitterEmplacementJoueur();
            this.joueur = null;
        }
        
    }
}
