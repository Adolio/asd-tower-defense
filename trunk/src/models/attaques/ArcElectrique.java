package models.attaques;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import models.creatures.Creature;
import models.jeu.Jeu;
import models.outils.MeilleursScores;
import models.tours.Tour;

/**
 * Eclair
 * 
 * Cette classe est une animation qui dessine un arc électrique partant d'une tour
 * vers une creature.
 * 
 * @author Romain Poulain
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 * @since jdk1.6.0_16
 * @see MeilleursScores
 */
public class ArcElectrique extends Attaque
{
    
    // constantes finales
    private static final long serialVersionUID  = 1L;
    
    // attributs membres
    
    //
    private ArrayList<ArrayList<Point>> arcs = new ArrayList<ArrayList<Point>>();
    
    //private ArrayList<Point> segments = new ArrayList<Point>();
    
    /**
     * Constructeur de l'attaque
     * 
     * @param terrain le terrain sur lequel l'attaque est lancee
     * @param attaquant la tour attaquante
     * @param cible la creature visee
     */
    public ArcElectrique(Jeu jeu, Tour attaquant, Creature cible, long degats)
    {
        super((int) attaquant.getCenterX(),(int) attaquant.getCenterY(), jeu, attaquant, cible);
        
        this.degats = degats;
  
        arcs.add(creerArc());
        arcs.add(creerArc());
        arcs.add(creerArc());
        arcs.add(creerArc());
        arcs.add(creerArc());
    }

    
    /**
     * Constructeur de l'attaque
     * 
     * @param terrain le terrain sur lequel l'attaque est lancee
     * @param attaquant la tour attaquante
     * @param cible la creature visee
     */
    public ArcElectrique(Jeu jeu, Tour attaquant, int xDepart, int yDepart, Creature cible, long degats)
    {
        super((int) attaquant.getCenterX(),(int) attaquant.getCenterY(), jeu, attaquant, cible);
        
        this.degats = degats;
  
        arcs.add(creerArc());
        arcs.add(creerArc());
        arcs.add(creerArc());
        arcs.add(creerArc());
        arcs.add(creerArc());
    }
    
    private ArrayList<Point> creerArc()
    {
        // création de l'arc
        
        
        Point creature = new Point((int)cible.getCenterX(),(int)cible.getCenterY());
        Point tour = new Point((int)attaquant.getCenterX(),(int)attaquant.getCenterY());
        
        int NB_Points = (int) (creature.distance(tour) / 10);
        
        
        ArrayList<Point> arc = new ArrayList<Point>();
        
        
        arc.add(new Point((int)attaquant.getCenterX(),
                               (int)attaquant.getCenterY()));

        Point vecteur = new Point(creature.x - tour.x, creature.y - tour.y);
        double nx = vecteur.y / vecteur.distance(0.0, 0.0);
        double ny = - vecteur.x / vecteur.distance(0.0, 0.0);
        
        Random ran = new Random();
        
        for(int i = 0;i < NB_Points - 1; i++)
        {
            tour.x += vecteur.x / NB_Points;
            tour.y += vecteur.y / NB_Points;
            
            int rand = ran.nextInt(20)-10;

            arc.add(new Point((int) (tour.x + rand * nx ),
                    (int) (tour.y + rand * ny)));
        }
        
        arc.add(new Point((int)cible.getCenterX(),
                (int)cible.getCenterY()));
        
        return arc;
    }


    private static final BasicStroke PEN  = new BasicStroke(3.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND);
    
    
    public void dessinerArc(Graphics2D g2, ArrayList<Point> arc, float alpha)
    {    
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        Point p = arc.get(0);
        for(int i=1;i < arc.size(); i++)
        {
            // dessin de la boule de feu
            g2.drawLine((int) p.x,(int) p.y,
                    (int) arc.get(i).x,(int) arc.get(i).y);
            
            p = arc.get(i);
        }
    }
    
    
    @Override
    public void dessiner(Graphics2D g2)
    {    
        // style
        BasicStroke old = (BasicStroke) g2.getStroke();
        
        
        float alpha = 0.6f;
        
        boolean premier = true;
        
        for(ArrayList<Point> arc : arcs)
        {
            if(premier)
            {
                g2.setStroke(PEN);
                
                g2.setColor(new Color(0,86,255));
                dessinerArc(g2, arc, alpha);
                
                g2.setColor(Color.WHITE);
                g2.setStroke(old);

                premier = false;
            }
            else
                g2.setColor(new Color(0,86,255));
            
            dessinerArc(g2, arc, alpha);
            
            alpha -= 0.1f;
        }
        
        // retabli la transparence
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));   
        g2.setStroke(old);
    }

    @Override
    public void animer(long tempsPasse)
    {   
        cible.blesser(degats, attaquant.getPrioprietaire());
        
        estTerminee = true;
    }
}
