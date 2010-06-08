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

/**
 * Panel de visualisation des emplacement de jeu
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | juin 2010
 * @since jdk1.6.0_16
 */
public class Panel_EmplacementsTerrain extends JPanel
{
    private static final long serialVersionUID = 1L;
    private Terrain terrain;
    private double scaleX = 0.7;
    private static final int xOffsetPseudo = 10, 
                             yOffsetPseudo = 30;
 
    // TODO proportionnel au plus grand coté
    private int largeur;
    
    @SuppressWarnings("unused")
    private int hauteur; 
    
    public Panel_EmplacementsTerrain(Terrain terrain, int largeur, int hauteur)
    {
        this.terrain = terrain;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.scaleX = 1.0 / terrain.getLargeur() * largeur;
        setPreferredSize(new Dimension(largeur,hauteur));
    }
    
    
    public Panel_EmplacementsTerrain(int largeur, int hauteur)
    {
        this.largeur = largeur;
        this.hauteur = hauteur;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        if(terrain != null)
        { 
            g2.scale(scaleX, scaleX);
            
            
            //g2.fillRect(0,0,500,500);
            
            
            
            //--------------------------
            //-- affichage du terrain --
            //--------------------------
            if(terrain.getImageDeFond() != null)
                // image de fond
                g2.drawImage(terrain.getImageDeFond(), 0, 0, terrain.getLargeur(),terrain.getHauteur(),  null);
        
            
            //---------------------------------------
            //-- affichage des zone de contruction --
            //---------------------------------------
            Rectangle zone;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            for (Equipe e : terrain.getEquipesInitiales())
                for (EmplacementJoueur ej : e.getEmplacementsJoueur())
                {
                    zone = ej.getZoneDeConstruction();
    
                    g2.setColor(ej.getCouleur());
                    g2.fillRect(zone.x, zone.y, zone.width, zone.height); 
                }
            
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
            
            int oldy = -1000;
            int lastOffset = 1;
            for (Equipe e : terrain.getEquipesInitiales())
                for (EmplacementJoueur ej : e.getEmplacementsJoueur())
                {
                    zone = ej.getZoneDeConstruction();
                    
                    if(ej.getJoueur() != null)
                    {
                        g2.setFont(GestionnaireDesPolices.POLICE_TITRE);
                        
                        int x = zone.x+xOffsetPseudo;
                        int y = zone.y+yOffsetPseudo;
                        
                        if(oldy == y)
                        {
                           y = y + 50 * lastOffset;
                           lastOffset *= -1; 
                        } 
                        
                        oldy = y;
                        
                        g2.setColor(e.getCouleur());
                        g2.drawString(ej.getJoueur().getPseudo(), x, y);
                        
                        g2.setColor(Color.BLACK);
                        g2.drawString(ej.getJoueur().getPseudo(), x+2, y+2);
                    }
                }
        }
        else
        {
            g2.drawString("Pas de terrain", 10, 10); 
        }
    }

    public void setTerrain(Terrain terrain)
    {
        this.terrain = terrain;
         
        scaleX = 1.0 / terrain.getLargeur() * largeur;
        setPreferredSize(new Dimension((int)(terrain.getLargeur()*scaleX),(int)(terrain.getHauteur()*scaleX)));

        repaint();
    }
}
