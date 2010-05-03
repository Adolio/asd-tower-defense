package vues;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

import models.joueurs.EmplacementJoueur;
import models.joueurs.Equipe;
import models.terrains.Terrain;

public class Panel_EmplacementsTerrain extends JPanel
{
    private static final long serialVersionUID = 1L;
    private Terrain terrain;
    private double scale = 0.7;
    private static final int xOffsetPseudo = 10, 
                             yOffsetPseudo = 30;
    
    public Panel_EmplacementsTerrain(Terrain terrain)
    {
        this.terrain = terrain;
        
        setPreferredSize(new Dimension((int)(terrain.getLargeur()*scale),(int)(terrain.getHauteur()*scale)));
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
           
        g2.scale(scale, scale);
        
        //--------------------------
        //-- affichage du terrain --
        //--------------------------
        if(terrain.getImageDeFond() != null)
            // image de fond
            g2.drawImage(terrain.getImageDeFond(), 0, 0, terrain.getLargeur(),terrain.getHauteur(),  null);
    
        
        //---------------------------------------
        //-- affichage des zone de contruction --
        //---------------------------------------
        for (Equipe e : terrain.getEquipesInitiales())
        { 
            for (EmplacementJoueur ej : e.getEmplacementsJoueur())
            {
                Rectangle zone = ej.getZoneDeConstruction();

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2.setColor(ej.getCouleur());
                g2.fillRect(zone.x, zone.y, zone.width, zone.height); 
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
                
                //g2.setColor(e.getCouleur());
                //g2.drawRect(zone.x, zone.y, zone.width, zone.height);
                
                //g2.setColor(Color.BLACK);

                if(ej.getJoueur() != null)
                {
                    g2.setFont(GestionnaireDesPolices.POLICE_TITRE);
                    
                    
                    
                    g2.setColor(e.getCouleur());
                    g2.drawString(ej.getJoueur().getPseudo(), zone.x+xOffsetPseudo, zone.y+yOffsetPseudo);
                    
                    g2.setColor(Color.BLACK);
                    g2.drawString(ej.getJoueur().getPseudo(), zone.x+xOffsetPseudo+2, zone.y+yOffsetPseudo+2);
                }
            
            
            }
        }
    }
}
