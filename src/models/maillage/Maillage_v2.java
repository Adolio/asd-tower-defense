package models.maillage;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import models.outils.Outils;

/**
 * TODO
 * 
 * @author Dark
 */
public class Maillage_v2 implements Maillage
{
	
    /*
	 * Constantes
	 */
	/**
	 * La largeur en pixel de chaque maille, ou noeud
	 */
	private final int LARGEUR_NOEUD;
	
	/**
	 * Le poid d'un arc diagonal
	 */
	private final int POIDS_DIAGO;

	/**
	 * La largeur en pixel totale du maillage (axe des x)
	 */
	private final int LARGEUR_EN_PIXELS;
	
	/**
	 * La hauteur en pixel totale du maillage (axe des y)
	 */
	private final int HAUTEUR_EN_PIXELS;
	
	/**
	 * Les dimensions en maille (ou noeuds) du maillage
	 */
	private final int NB_NOEUDS_LARGEUR, NB_NOEUDS_HAUTEUR;

	/*
	 * Attributs
	 */
	
	private int VOISINS_MAX_PAS_NOEUDS = 8;
	
	private int NB_NOEUDS;
	private int nbArcsActifs;
	
	/**
     * Tableau des noeuds
     * <br>
     * Note : l'indice du tableau spécifie le numéro du noeud
     */
    private Noeud[] noeuds;
	
	/**
	 * Tableau du nombre de voisins d'un noeuds
	 * <br>
	 * Note : l'indice du tableau spécifie le numéro du noeud
	 */
	private int[] nbVoisins;
	
	/**
	 * Tableau des indices des voisins d'un noeuds
	 * <br>
	 * Note : l'indice 1 du tableau spécifie le numéro du noeud
	 */
	private int[][] voisins;
	
	/**
	 * Tableau des poinds jusqu'au voisins d'un noeuds
	 * <br>
     * Note : l'indice 1 du tableau spécifie le numéro du noeud
	 */
	private int[][] poids;
	
	/**
	 * Le decalage de base.
	 */
	private int xOffset, yOffset;
	
	/**
	 * Noeud d'arrivee
	 */
    private int iNoeudArrivee;
   

	/**
	 * Un maillage dynamique représentant une aire de jeu.
	 * 
	 * @param largeurPixels
	 *            Largeur en pixel de la zone.
	 * @param hauteurPixels
	 *            Hauteur en pixel de la zone.
	 * @param largeurDuNoeud
	 *            La largeur en pixel de chaque maille.
	 * @param xOffset
	 *            Le décalage en x du maillage, en pixels.
	 * @param yOffset
	 *            Le décalage en y du maillage, en pixels.
	 * @return 
	 * @throws IllegalArgumentException
	 *             Levé si les dimensions ne correspondent pas.
	 */
	public Maillage_v2(final int largeurPixels, final int hauteurPixels,
			final int largeurDuNoeud, int arriveX, int arriveY, int xOffset, int yOffset)
			throws IllegalArgumentException
	{

		// Assignation de la largeur du noeud (ou de la maille).
		LARGEUR_NOEUD = largeurDuNoeud;

		// Calcule une fois pour toute la distance diagonale
		POIDS_DIAGO = (int) Math.sqrt(2 * LARGEUR_NOEUD * LARGEUR_NOEUD);

		// Assignation de la dimension en pixel unitaire du maillage
		LARGEUR_EN_PIXELS = largeurPixels;
		HAUTEUR_EN_PIXELS = hauteurPixels;

		// Conversion en dimension en maille.
		NB_NOEUDS_LARGEUR = (largeurPixels / LARGEUR_NOEUD);
		NB_NOEUDS_HAUTEUR = (hauteurPixels / LARGEUR_NOEUD);

		
		NB_NOEUDS = NB_NOEUDS_LARGEUR * NB_NOEUDS_HAUTEUR;
		
		
		// Les offsets du décalage
		this.xOffset = xOffset;
		this.yOffset = yOffset;

		// Construction du graphe
		construireGraphe();
		
	    iNoeudArrivee = getIndiceNoeud(arriveX, arriveY);
		
		
        contruireArbreDijkstra(iNoeudArrivee);
	}

	/**
	 * Permet de trouver l'indice d'un noeud le plus proche d'une coordonnée.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
    private int getIndiceNoeud(int x, int y)
    { 
        if(noeuds.length == 0)
            throw new IllegalArgumentException("Pas de noeud");
        
        Noeud n;
        int iNoeudLePlusProche = -1;
        double distMax = Integer.MAX_VALUE;
        Point cible = new Point(x,y);
        for(int i=0;i<noeuds.length;i++)
        {
            n = noeuds[i];
            
            if(cible.distance(n) < distMax)
            {
                iNoeudLePlusProche = i;
                distMax = cible.distance(n);
            }
        }
             
        return iNoeudLePlusProche;
    }

    /**
     * 
     * @param largeurPixels
     * @param hauteurPixels
     * @param largeurDuNoeud
     * @throws IllegalArgumentException
     */
    public Maillage_v2(final int largeurPixels, final int hauteurPixels,
            final int largeurDuNoeud, int arriveX, int arriveY) throws IllegalArgumentException
    {
        this(largeurPixels, hauteurPixels, largeurDuNoeud, arriveX, arriveY,  0, 0);
    }
	
	private void construireGraphe()
    {
	    // allocation mémoire
	    noeuds = new Noeud[NB_NOEUDS];   
	    nbVoisins = new int[NB_NOEUDS];
        voisins = new int[NB_NOEUDS][VOISINS_MAX_PAS_NOEUDS];
        poids = new int[NB_NOEUDS][VOISINS_MAX_PAS_NOEUDS];
        
        // par defaut, pas de voisins
        for(int i=0;i<NB_NOEUDS;i++)
        {
            nbVoisins[i] = 0;
        
            for(int j=0;j<VOISINS_MAX_PAS_NOEUDS;j++)
                poids[i][j] = Integer.MAX_VALUE;
        }
 
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
                n.setActif(true);
                noeuds[iNoeud] = n;
                
                //--------------------
                //-- ajout des arcs --
                //--------------------
                // pas la derniere colonne
                if(i != NB_NOEUDS_LARGEUR-1 && boolAlea())
                {
                    ajouterArc(iNoeud, iNoeud+NB_NOEUDS_HAUTEUR, LARGEUR_NOEUD);
                    ajouterArc(iNoeud+NB_NOEUDS_HAUTEUR, iNoeud, LARGEUR_NOEUD);
                }
                
                // pas la derniere ligne
                if(j != NB_NOEUDS_HAUTEUR-1 && boolAlea())
                {
                    ajouterArc(iNoeud, iNoeud+1, LARGEUR_NOEUD);
                    ajouterArc(iNoeud+1, iNoeud, LARGEUR_NOEUD);
                }
                    
                // pas la derniere ligne et la derniere colonne
                if(i != NB_NOEUDS_LARGEUR-1 && j != NB_NOEUDS_HAUTEUR-1 && boolAlea())
                {
                    ajouterArc(iNoeud, iNoeud+NB_NOEUDS_HAUTEUR+1, POIDS_DIAGO);
                    ajouterArc(iNoeud+NB_NOEUDS_HAUTEUR+1, iNoeud, POIDS_DIAGO);
                }
                
                // pas la première ligne et la derniere colonne
                if(j != 0 && i != NB_NOEUDS_LARGEUR-1 && boolAlea())   
                {
                    ajouterArc(iNoeud, iNoeud+NB_NOEUDS_HAUTEUR-1, POIDS_DIAGO);
                    ajouterArc(iNoeud+NB_NOEUDS_HAUTEUR-1, iNoeud, POIDS_DIAGO);
                }
                iNoeud++;
            } 
        }
    }  
	
	private void ajouterArc(int iNoeud, int voisin, int poidsArc)
    {
	    if(nbVoisins[iNoeud] == VOISINS_MAX_PAS_NOEUDS)
	        throw new IllegalArgumentException("Nombre de voisins max atteint");
	    
	    if(poidsArc == Integer.MAX_VALUE)
	        throw new IllegalArgumentException("Poids impossible");
	    
	    nbArcsActifs++;
	    nbVoisins[iNoeud]++;
	    
	    voisins[iNoeud][nbVoisins[iNoeud]-1] = voisin;
	    poids[iNoeud][nbVoisins[iNoeud]-1] = poidsArc;
    }

    private boolean boolAlea()
    {
        return Outils.tirerNombrePseudoAleatoire(0, 2) >= 1;
    }

	@Override
	public synchronized ArrayList<Point> plusCourtChemin(int xDepart,
			int yDepart, int xArrivee, int yArrivee)
			throws PathNotFoundException, IllegalArgumentException
	{
		
	    ArrayList<Point> ps = new ArrayList<Point>();
	    
	    int in = getIndiceNoeud(xDepart, yDepart);
	
	    ps.add(new Point(noeuds[in]));
	    
        while(in != -1)
        {
            int pred = infoNoeuds[in].pred;
            
            if(pred == -1)
                break;
            
            ps.add(new Point(noeuds[pred]));

            System.out.println(in+" -> "+pred);
            
            in = pred;
        }  
	    
	    return ps;
	}

	@Override
	synchronized public void activerZone(Rectangle rectangle)
			throws IllegalArgumentException
	{
	    // TODO
        contruireArbreDijkstra(iNoeudArrivee);
	}

	@Override
	synchronized public void desactiverZone(Rectangle rectangle)
			throws IllegalArgumentException
	{ 
	    Noeud n;
	    for(int i=0;i<NB_NOEUDS;i++)
	    {
	        n = noeuds[i];
	        
	        if(rectangle.contains(n))
	        {
	            // pour tous les arcs
	            for(int j=0;j<nbVoisins[i];j++)
	            {
	                n.setActif(false);
	                //poids[i][j] = Integer.MAX_VALUE;
	                //nbArcsActifs-=2; // bidirection
	            }	            
	        }
	    }
	    
        contruireArbreDijkstra(iNoeudArrivee);
	}

	@Override
	public int getLargeurPixels()
	{
		return LARGEUR_EN_PIXELS;
	}

	@Override
	public int getHauteurPixels()
	{
		return HAUTEUR_EN_PIXELS;
	}

	@Override
	public Noeud[] getNoeuds()
	{
		return noeuds;
	}

	@Override
	public Line2D[] getArcs()
	{
	    
	    ArrayList<Line2D> arcs = new ArrayList<Line2D>();

	    int iArc = 0;
	    Arc arc;
	    for(int i=0;i<NB_NOEUDS;i++)
	        if(noeuds[i].isActif())
    	        for(int j=0;j<nbVoisins[i];j++)
    	        {      
    	            // les arcs de poids infinis sont inexistants.
    	            if(poids[i][j] != Integer.MAX_VALUE && noeuds[voisins[i][j]].isActif())
    	            {
    	                arc = new Arc(noeuds[i], noeuds[voisins[i][j]]);
    	                arcs.add(arc.toLine2D()); 
    	            }
    	        }
	    

	    Line2D[]tabArcs = new Line2D[arcs.size()];
	    arcs.toArray(tabArcs);
	    
	    return tabArcs;
	}

	/**
	 * Calcul la distance entre chaque point
	 * 
	 * @param chemin une collection de point
	 * @return la longueur du chemin
	 */
    public double getLongueurChemin(ArrayList<Point> chemin)
    {
        return 0.0;
    }
    
    
    /**
     * Arbre des chemins les plus courts.
     */
    private InfoNoeud[] infoNoeuds;
       
    /**
     * Dijkstra's algorithm to find shortest path from iNoeudArrive 
     * to all other nodes
     * 
     * @param iNoeudArrive
     */
    private void contruireArbreDijkstra(int iNoeudArrive)
    {
        final InfoNoeud[] infoNoeuds = new InfoNoeud[NB_NOEUDS];

        // creation des noeuds d'information
        for(int i=0;i<NB_NOEUDS;i++)
            infoNoeuds[i] = new InfoNoeud(i,noeuds[i]);

        // Sommet de départ à zéro
        infoNoeuds[iNoeudArrive].distArrivee = 0;
        
        // Pour chaque noeuds
        for (int i = 0; i < NB_NOEUDS; i++)
        {
            if(noeuds[i].isActif())
            {
                // Cherche le noeud suivant à traiter
                final int next = minVertex(infoNoeuds);
                
                if(next != -1)
                {
                    // Traitement du noeud
                    infoNoeuds[next].visited = true;
        
                    // Pour tous les voisins du noeud
                    for (int j = 0; j < nbVoisins[next]; j++)
                    {
                        if(noeuds[j].isActif())
                        { 
                            final int iVoisin = voisins[next][j];
                            final int distArrivee = infoNoeuds[next].distArrivee + poids[next][j];
                            if (infoNoeuds[iVoisin].distArrivee > distArrivee)
                            {
                                infoNoeuds[iVoisin].distArrivee = distArrivee;
                                infoNoeuds[iVoisin].pred = next;
                            }
                        }
                    }
                }
            }
        }

        this.infoNoeuds = infoNoeuds; // (ignore pred[s]==0!)
    }
   
    /**
     * Retour l'indice du noeud le dont la distance est la plus faible. 
     * 
     * @param dist les distances depuis le noeud de départ
     * @param visited les noeuds visités
     * @return l'indice du noeud le dont la distance est la plus faible.
     *          ou -1 s'il n'y en a pas
     */
    private int minVertex(InfoNoeud[] infoNoeuds)
    {
        int x = Integer.MAX_VALUE;
        int y = -1; // graph not connected, or no unvisited vertices
        
        // Pour chaque noeuds
        for (int i = 0; i < infoNoeuds.length; i++)
        {
            if(noeuds[i].isActif())
            {
                // Si pas visité et la distance est plus faible 
                if (!infoNoeuds[i].visited && infoNoeuds[i].distArrivee < x)
                {
                    y = i;
                    x = infoNoeuds[i].distArrivee;
                }
            }
        }
        
        return y;
    }

    @Override
    public int getNbNoeuds()
    {
        return NB_NOEUDS;
    }

    
    public static class InfoNoeud
    {
        int id;
        Noeud noeud;
        int distArrivee = Integer.MAX_VALUE;
        int pred = -1;
        boolean visited = false;
        int[] voisins;
        
        public InfoNoeud(int id,Noeud noeud)
        {
            this.id     = id;
            this.noeud  = noeud;
        }
    }
}
