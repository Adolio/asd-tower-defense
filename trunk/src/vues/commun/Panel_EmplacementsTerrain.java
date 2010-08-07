package vues.commun;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;

import vues.GestionnaireDesPolices;
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

            // image ou couleur de fond
            if(terrain.getImageDeFond() != null)
            {   
                Image image1 = terrain.getImageDeFond();
                
                for(int l=0;l<terrain.getLargeur();l+=image1.getWidth(null))
                    for(int h=0;h<terrain.getHauteur();h+=image1.getHeight(null))
                        g2.drawImage(image1, l, h, null);
            }
            else
            {
                // couleur de fond
                g2.setColor(terrain.getCouleurDeFond());
                g2.fillRect(0, 0, terrain.getLargeur(), terrain.getHauteur());
            }
            
            // murs
            if(terrain.getAfficherMurs())
            {
                g2.setColor(terrain.getCouleurMurs());
                ArrayList<Rectangle> murs = terrain.getMurs();
                g2.setColor(terrain.getCouleurMurs());
                for(Rectangle mur : murs)
                    dessinerZone(mur,g2);
            }
            
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
        validate();
        revalidate();
        
        
        repaint();
    }
    
    /**
     * Permet de dessiner une zone rectangulaire sur le terrain.
     * 
     * @param zone la zone rectangulaire
     * @param g2 le Graphics2D pour dessiner
     */
    private void dessinerZone(final Rectangle zone, final Graphics2D g2)
    {
        g2.fillRect((int) zone.getX(), 
                    (int) zone.getY(), 
                    (int) zone.getWidth(), 
                    (int) zone.getHeight());
    }
}
