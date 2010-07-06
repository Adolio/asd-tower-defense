package vues;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import models.jeu.Jeu;
import models.joueurs.Equipe;
import models.terrains.Terrain;

/**
 * Panel de conception graphique du terrain.
 * 
 * Ce panel hérite du panel d'affichage du terrain de jeu durant la partie.
 * 
 * TODO compléter
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | juillet 2010
 * @since jdk1.6.0_16
 * @see Terrain
 */
public class Panel_CreationTerrain extends Panel_Terrain
{
    private static final long serialVersionUID  = 1L;
    private static final int MODE_DEPLACEMENT   = 0;
    private static final int MODE_MURS          = 1;
    
    //private Terrain terrain;
    private int mode = MODE_DEPLACEMENT;
    private Rectangle recEnTraitement;
    
    /**
     * largeur d'un case du maillage pour le positionnement des tours
     */
    private static final int CADRILLAGE = 10; // unite du cadriallage en pixel

    
    public Panel_CreationTerrain(Jeu jeu, EcouteurDePanelTerrain edpt)
    {
        super(jeu,edpt);
        
        quadrillage = true;
        afficherMurs = true;
    }
    
    
    
    private int taillePoignee = 6;
    private int taillePoigneeSur2 = taillePoignee / 2;
    private boolean redimGrab;
    private int poigneeGrab;
    private Rectangle recEnTraitementOriginal;
    private boolean deplGrab;
    private EcouteurDePanelCreationTerrain edpct;
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        
        if(recEnTraitement != null)
        {
            setTransparence(1.0f,g2);
            
            // dessin du tour (selection)
            g2.setColor(Color.WHITE);
            g.drawRect(recEnTraitement.x,
                    recEnTraitement.y,
                    recEnTraitement.width,
                    recEnTraitement.height);
            
            // dessin des poignée 
            g2.setColor(Color.RED);
            Rectangle poignee;
            for(int i=0;i<4;i++)
            {
                poignee = getPoignee(recEnTraitement,i);
                g2.fillRect(poignee.x,poignee.y,poignee.width,poignee.height);
            }
        }
    }

    private Rectangle getPoignee(Rectangle rectangle, int i)
    {
        final int L_SUR_2 = recEnTraitement.width / 2;
        final int H_SUR_2 = recEnTraitement.height / 2;
        
        
        taillePoignee = (int) (6 / coeffTaille);
        taillePoigneeSur2 = taillePoignee / 2;
        
        
        switch(i)
        {
            case 0 : // droite
                return new Rectangle(recEnTraitement.x-taillePoigneeSur2,
                        recEnTraitement.y+H_SUR_2-taillePoigneeSur2,taillePoignee,taillePoignee);
            case 1 : // haut
                
                return new Rectangle(recEnTraitement.x+recEnTraitement.width-taillePoigneeSur2,
                        recEnTraitement.y+H_SUR_2-taillePoigneeSur2,taillePoignee,taillePoignee);
            case 2 : // gauche
                return new Rectangle(recEnTraitement.x+L_SUR_2-taillePoigneeSur2,
                        recEnTraitement.y-taillePoigneeSur2,taillePoignee,taillePoignee);
            case 3 : // bas
                return new Rectangle(recEnTraitement.x+L_SUR_2-taillePoigneeSur2,
                        recEnTraitement.y+recEnTraitement.height-taillePoigneeSur2,taillePoignee,taillePoignee);
        }
        
        return null;
    }

    @Override
    public void mousePressed(MouseEvent me)
    {
        boutonGrab = me.getButton();

        switch(mode)
        {
            case MODE_DEPLACEMENT : 
                super.mousePressed(me);
                break;
                
            case MODE_MURS : 
                
                sourisGrabX = me.getX();
                sourisGrabY = me.getY();
                
                decaleGrabX = decaleX;
                decaleGrabY = decaleY;
                
                redimGrab = false;
                deplGrab  = false;
                
                Point p = getCoordoneeSurTerrainOriginal(me.getPoint());
                
                
                if(recEnTraitement != null)
                {
                    // Contact avec une poignée ?
                    Rectangle poignee;
                    for(int i=0;i<4;i++)
                    {
                        poignee = getPoignee(recEnTraitement,i);
                        
                        if(poignee.contains(p))
                        {
                            redimGrab = true;
                            poigneeGrab = i;
                            return;
                            //recEnTraitement.width+=10; 
                        }
                    }   
                }
                
                // si il y une zone de départ ou d'arrivee
                for(Equipe e : jeu.getEquipes())
                {
                    
                    // zone de départ
                    for(int i=0;i<e.getNbZonesDepart();i++)
                    {
                        Rectangle r = e.getZoneDepartCreatures(i); 
                        
                        if(r.contains(p))
                        {
                            setRecEnTraitement(r);
                            return;
                        }
                    }
                     
                    // zone d'arrive
                    if(e.getZoneArriveeCreatures().contains(p))
                    {
                        setRecEnTraitement(e.getZoneArriveeCreatures());
                        return;
                    }
                }
                
                // si il a un mur
                for(Rectangle r : jeu.getTerrain().getMurs())
                    if(r.contains(p))
                    {
                        setRecEnTraitement(r);
                        
                        if(edpct != null)
                            edpct.zoneSelectionnee(r);
                        
                        return;
                    }
                
                
                // si il y une zone de construction
                /*
                for(Equipe e : jeu.getEquipes())
                {
                    for(EmplacementJoueur ej : e.getEmplacementsJoueur())
                    {
                        if(ej.getZoneDeConstruction().contains(me.getPoint()))
                        {
                            setRecEnTraitement(ej.getZoneDeConstruction());
                            return;
                        }
                    }
                }
                */

                break;
        }
        
        
    }

    @Override
    public void mouseMoved(MouseEvent me)
    {
        switch(mode)
        {
            case MODE_DEPLACEMENT : 
                super.mouseMoved(me);
                break;
                
            case MODE_MURS : 
                
                // FIXME correct ? 
                super.mouseMoved(me);
                break;
        }
    }
    
    /**
     * Methode de gestion du clique enfoncé de la souris lorsque qu'elle bouge.
     * 
     * @param me evenement lie a cette action
     * @see MouseMotionListener
     */
    @Override
    public void mouseDragged(MouseEvent me)
    {
        switch(mode)
        {
            case MODE_DEPLACEMENT : 
                super.mouseDragged(me);
                break;
                
            case MODE_MURS : 
                
                Point p = getCoordoneeSurTerrainOriginal(me.getPoint());
                Point sourisGrab = getCoordoneeSurTerrainOriginal(sourisGrabX,sourisGrabY);
                //Point reto_CTO = getCoordoneeSurTerrainOriginal(recEnTraitementOriginal.x,recEnTraitementOriginal.y);
                
                
                
                if(boutonGrab == MouseEvent.BUTTON1)
                {
                    if(recEnTraitement != null)
                    {
                        if(redimGrab)
                        {
                            Rectangle tmpRec = new Rectangle(recEnTraitement);
                            
                            switch(poigneeGrab)
                            {
                                case 0: // gauche
                                    recEnTraitement.width = getLongueurSurGrillage(sourisGrab.x - p.x + recEnTraitementOriginal.width);
                                    recEnTraitement.x     = getPositionSurQuadrillage(p.x);// FIXME recEnTraitementOriginal.x + (recEnTraitementOriginal.width - recEnTraitement.width);
                                    break;
                                case 1: // droite
                                    recEnTraitement.width = getLongueurSurGrillage(p.x - sourisGrab.x + recEnTraitementOriginal.width);
                                    break;
                                case 2: // haut
                                    recEnTraitement.height = getLongueurSurGrillage(sourisGrab.y - p.y + recEnTraitementOriginal.height);
                                    recEnTraitement.y      = getPositionSurQuadrillage(p.y);   // FIXME recEnTraitementOriginal.y + (recEnTraitementOriginal.height - recEnTraitement.height);
                                    break;
                                case 3: // bas
                                    recEnTraitement.height = getLongueurSurGrillage(p.y - sourisGrab.y + recEnTraitementOriginal.height);
                                    break;
                            } 
                            
                            // pas de taille négative
                            if(recEnTraitement.width <= 0)
                            {
                                recEnTraitement.width   = tmpRec.width;
                                recEnTraitement.x       = tmpRec.x;
                            }
                            if(recEnTraitement.height <= 0)
                            {
                                recEnTraitement.height  = tmpRec.height;
                                recEnTraitement.y       = tmpRec.y;
                            }
                            
                            if(edpct != null)
                                edpct.zoneModifiee(recEnTraitement);
                        }
                        else if(deplGrab)
                        {
                            recEnTraitement.x = getPositionSurQuadrillage(recEnTraitementOriginal.x + me.getX() - sourisGrabX);
                            recEnTraitement.y = getPositionSurQuadrillage(recEnTraitementOriginal.y + me.getY() - sourisGrabY);
                        
                            if(edpct != null)
                                edpct.zoneModifiee(recEnTraitement);
                        }
                    }
                }
                
                break;
        }
    }

    private int getLongueurSurGrillage(int longueur)
    {
        return longueur - longueur % CADRILLAGE;
    }

    @Override
    public void mouseReleased(MouseEvent e)
    { 
        switch(mode)
        {
            case MODE_DEPLACEMENT : 
                super.mouseReleased(e);
                break;
                
            case MODE_MURS : 
                
                if(!deplGrab && !redimGrab)
                {  
                    Point p = getCoordoneeSurTerrainOriginal(e.getPoint());
                    
                    // si il a un mur
                    for(Rectangle r : jeu.getTerrain().getMurs())
                        if(r.contains(p))
                        {
                            setRecEnTraitement(r);
                            
                            if(edpct != null)
                                edpct.zoneSelectionnee(r);
                            
                            return;
                        }
                    
                    // creation d'un mur si pas de mur
                    setRecEnTraitement(new Rectangle(
                            getPositionSurQuadrillage(p.x),
                            getPositionSurQuadrillage(p.y),
                            20,20));
                    
                    if(edpct != null)
                        edpct.zoneSelectionnee(recEnTraitement);
                    
                    jeu.getTerrain().ajouterMur(recEnTraitement);
                }
                
                break;
        }
    }
    
    void setRecEnTraitement(Rectangle r)
    {
        recEnTraitement = r;
        deplGrab = true;
        recEnTraitementOriginal = new Rectangle(recEnTraitement);
        mode = MODE_MURS;
    }
    
    void deselectionnerRecEnTraitement()
    {
        recEnTraitement = null;
    }
    
    @Override
    public void keyReleased(KeyEvent ke)
    {
        switch(mode)
        {
            case MODE_DEPLACEMENT : 
                super.keyReleased(ke);
                break;
                
            case MODE_MURS : 
                
                if(ke.getKeyCode() == KeyEvent.VK_DELETE)
                {
                    if(recEnTraitement != null)
                    {
                        jeu.getTerrain().supprimerMur(recEnTraitement);
                        recEnTraitement = null;
                    }
                }
                
                break;
        }
    }
    
    
    public void activerModeCreationMurs()
    {
        mode = MODE_MURS;
    }

    public void activerModeDeplacement()
    {
        recEnTraitement = null;
        
        mode = MODE_DEPLACEMENT; 
    }

    public void setEcouteurDeCreationTerrain(EcouteurDePanelCreationTerrain edpct)
    {
        this.edpct = edpct;
    }

    public Rectangle getRecEnTraitement()
    {
        return recEnTraitement;
    }
}
