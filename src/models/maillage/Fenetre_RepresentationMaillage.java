package models.maillage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import models.outils.Outils;
import models.maillage.Dijkstra_MA.InfoNoeud;

@SuppressWarnings("serial")
public class Fenetre_RepresentationMaillage extends JFrame
{
    class Panel_Maillage extends JPanel implements ActionListener
    {
        private GraphePondere_MA graphe;
        private final int NB_CHEMINS_VISIBLES   = 1000;
        private final int NB_NOEUDS_LARGEUR     = 80;
        private final int NB_NOEUDS_HAUTEUR     = 80;
        private final int LARGEUR_NOEUD         = 10;

        private InfoNoeud[] infoNoeuds;
        
        private JButton bRecalculer;
        private JButton bCalculerDijsktra;
         
        public Panel_Maillage()
        {
            graphe = new GraphePondere_MA(NB_NOEUDS_LARGEUR*NB_NOEUDS_HAUTEUR);
            
            // construction du graphe
            Noeud n;
            int iNoeud = 0;

            for(int i=0;i<NB_NOEUDS_LARGEUR;i++)
            {
                for(int j=0;j<NB_NOEUDS_HAUTEUR;j++)
                {
                    //--------------------------
                    //-- mise a jour du noeud --
                    //--------------------------
                    n = new Noeud(i*LARGEUR_NOEUD,j*LARGEUR_NOEUD,LARGEUR_NOEUD);
                    graphe.setNoeud(iNoeud,n);
                    
                    //--------------------
                    //-- ajout des arcs --
                    //--------------------
                    int poids;
                    
                    poids = tirerPoids();  
                    // pas la derniere colonne
                    if(i != NB_NOEUDS_LARGEUR-1)
                        graphe.ajouterArc(iNoeud, iNoeud+NB_NOEUDS_HAUTEUR, poids);
                    
                    poids = tirerPoids();
                    // pas la derniere ligne
                    if(j != NB_NOEUDS_HAUTEUR-1)
                        graphe.ajouterArc(iNoeud, iNoeud+1, poids);
                    
                    poids = tirerPoids(); 
                    // pas la derniere ligne et la derniere colonne
                    if(i != NB_NOEUDS_LARGEUR-1 && j != NB_NOEUDS_HAUTEUR-1)
                        graphe.ajouterArc(iNoeud, iNoeud+NB_NOEUDS_HAUTEUR+1, poids);

                    poids = tirerPoids();
                    // pas la première ligne et la derniere colonne
                    if(j != 0 && i != NB_NOEUDS_LARGEUR-1)
                        graphe.ajouterArc(iNoeud, iNoeud+NB_NOEUDS_HAUTEUR-1, poids);
                    
                    iNoeud++;
                } 
            }
            
            // boutons
            bCalculerDijsktra = new JButton("Calculer une fois Dijkstra");
            bCalculerDijsktra.addActionListener(this);
            add(bCalculerDijsktra);
            
            bRecalculer = new JButton("Recalculer "+NB_CHEMINS_VISIBLES+" chemins");
            bRecalculer.addActionListener(this);
            add(bRecalculer);

            setPreferredSize(new Dimension(NB_NOEUDS_LARGEUR*LARGEUR_NOEUD,NB_NOEUDS_HAUTEUR*LARGEUR_NOEUD)); 
        }
        
        private int tirerPoids()
        {
            if(Outils.tirerNombrePseudoAleatoire(0, 1) == 1)
                return 1;
            else
                return Integer.MAX_VALUE;
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
            
            for(Arc arc : graphe.getArcs())
                dessinerArc(arc, g2);
            
            // affichage des noeuds
            g2.setColor(Color.RED);
            for(Noeud noeud : graphe.getNoeuds())
                g2.fillOval(noeud.x - 1, noeud.y - 1, 2, 2);
            
            for(int i=0;i<NB_CHEMINS_VISIBLES;i++)
                afficherCheminPourNoeud(Outils.tirerNombrePseudoAleatoire(0, graphe.getNbNoeuds()-1),g2);
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object source = e.getSource();
            
            if(source == bCalculerDijsktra)
            {
                try
                {
                    infoNoeuds = Dijkstra_MA.dijkstra(graphe, NB_NOEUDS_LARGEUR * NB_NOEUDS_HAUTEUR / 2 + NB_NOEUDS_HAUTEUR / 2);
                } 
                catch (IllegalAccessException e1){
                     e1.printStackTrace();
                     
                     infoNoeuds = null;
                }  
            }
            
            repaint();
        }
        
        private void afficherCheminPourNoeud(int idNoeud, Graphics2D g2)
        {
            g2.setColor(Color.GREEN);
            if(infoNoeuds != null)
            {
                int in = infoNoeuds[idNoeud].id;
                
                while(in != -1)
                {
                    int pred = infoNoeuds[in].pred;
                    
                    if(pred == -1)
                        break;
                    
                    dessinerArc(graphe.getArc(in,pred), g2);
                    
                    in = pred;
                }  
            }
        }

        private void dessinerArc(Arc arc, Graphics2D g2)
        {
            g2.drawLine((int) arc.getDepart().x, 
                    (int) arc.getDepart().y, 
                    (int) arc.getArrivee().x, 
                    (int) arc.getArrivee().y);
        }
    }
    
    public Fenetre_RepresentationMaillage()
    {
        super("Représentation du maillage"); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        add(new Panel_Maillage());
        
        setVisible(true);
        pack();
    }
    
    public static void main(String[] args) 
    {
        new Fenetre_RepresentationMaillage();
    }
}
