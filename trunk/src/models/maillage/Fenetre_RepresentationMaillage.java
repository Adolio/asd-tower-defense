package models.maillage;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.*;

import models.outils.Outils;

@SuppressWarnings("serial")
public class Fenetre_RepresentationMaillage extends JFrame
{
    class Panel_Maillage extends JPanel implements ActionListener
    {
        private final int NB_CHEMINS_VISIBLES = 100;
        private Maillage m;
        private JButton bRecalculer;
        private JButton bCalculerDijsktra;
        
        public Panel_Maillage(Maillage maillage)
        {
            m = maillage;
            
            // boutons
            bCalculerDijsktra = new JButton("Calculer une fois Dijkstra");
            bCalculerDijsktra.addActionListener(this);
            add(bCalculerDijsktra);
            
            bRecalculer = new JButton("Recalculer "+NB_CHEMINS_VISIBLES+" chemins");
            bRecalculer.addActionListener(this);
            add(bRecalculer);

            setPreferredSize(new Dimension(m.getLargeurPixels(),m.getHauteurPixels())); 
        }

        @Override
        public void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            
            // background
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, 1000, 1000);
            
            // affichage des arcs
            g2.setColor(Color.yellow);
            
            for(Line2D arc : m.getArcs())
                dessinerArc(arc, g2);
            
            // affichage des noeuds
            g2.setColor(Color.RED);
            for(Noeud noeud : m.getNoeuds())
                g2.fillOval(noeud.x - 1, noeud.y - 1, 2, 2);
            
            g2.setColor(Color.GREEN);
            for(int i=0;i<NB_CHEMINS_VISIBLES;i++)
                afficherCheminPourNoeud(
                        Outils.tirerNombrePseudoAleatoire(0, m.getLargeurPixels()),
                        Outils.tirerNombrePseudoAleatoire(0, m.getHauteurPixels()),
                        g2);
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object source = e.getSource();
            
            if(source == bCalculerDijsktra)
            {
                //m.activerZone(new Rectangle()); //TODO
                m.desactiverZone(new Rectangle(Outils.tirerNombrePseudoAleatoire(0, m.getLargeurPixels()),
                        Outils.tirerNombrePseudoAleatoire(0, m.getHauteurPixels()),50,50)); //TODO
            }

            repaint();
        }
        
        private void afficherCheminPourNoeud(int x, int y, Graphics2D g2)
        {  
            ArrayList<Point> chemin;
            
            try 
            {
                chemin = m.plusCourtChemin(x, y, 0, 0);
                
                if(chemin.size() > 0)
                {
                    Point pPrec = chemin.get(0);
                    for(int i=1;i<chemin.size();i++)
                    {
                        Point p =  chemin.get(i);
                        
                        g2.drawLine((int) pPrec.x, 
                                (int) pPrec.y, 
                                (int) p.x, 
                                (int) p.y);
                        
                        pPrec = p; 
                    }
                }
                else
                    System.out.println("Pas de chemin");
            } 
            catch (IllegalArgumentException e){
                e.printStackTrace();
            } 
            catch (PathNotFoundException e){
                e.printStackTrace();
            }
        }

        private void dessinerArc(Line2D arc, Graphics2D g2)
        {
            g2.drawLine((int) arc.getX1(), 
                    (int) arc.getY1(), 
                    (int) arc.getX2(), 
                    (int) arc.getY2());
        }
    }
    
    public Fenetre_RepresentationMaillage(Maillage maillage)
    {
        super("Représentation du maillage"); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(new Panel_Maillage(maillage));
        
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }
    
    public static void main(String[] args) 
    {
        int largeur = 600;
        int hauteur = 600;
        
        //Maillage maillage_v1 = new Maillage_v1(largeur, hauteur, 10);
        Maillage maillage_v2 = new Maillage_v2(largeur, hauteur, 10, largeur/2, hauteur/2);
        
        new Fenetre_RepresentationMaillage(maillage_v2);
    }
}
