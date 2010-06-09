package models.maillage;

public class Dijkstra_MA
{
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
    
    // Dijkstra's algorithm to find shortest path from s to all other nodes
    public static InfoNoeud[] dijkstra(GraphePondere_MA graphe, int iNoeudArrive) throws IllegalAccessException
    {  
        int nbNoeuds = graphe.getNbNoeuds();
        
        final InfoNoeud[] infoNoeuds = new InfoNoeud[nbNoeuds];

        // creation des noeuds d'information
        for(int i=0;i<nbNoeuds;i++)
            infoNoeuds[i] = new InfoNoeud(i,graphe.getNoeud(i));

        
        // Sommet de départ à zéro
        infoNoeuds[iNoeudArrive].distArrivee = 0;
        
        // Pour chaque noeuds
        for (int i = 0; i < nbNoeuds; i++)
        {
            // Cherche le noeud suivant à traiter
            final int next = minVertex(infoNoeuds);
            
            if(next != -1)
            {
                // Traitement du noeud
                infoNoeuds[next].visited = true;
    
                // Pour tous les voisins du noeud
                final int [] voisins = graphe.getIdVoisins(next);
                for (int j = 0; j < voisins.length; j++)
                {
                    final int v = voisins[j];
                    final int d = infoNoeuds[next].distArrivee + graphe.getPoids(next, v);
                    if (infoNoeuds[v].distArrivee > d)
                    {
                        infoNoeuds[v].distArrivee = d;
                        infoNoeuds[v].pred = next;
                    }
                }
            }
        }

        return infoNoeuds; // (ignore pred[s]==0!)
    }
   
    /**
     * Retour l'indice du noeud le dont la distance est la plus faible. 
     * 
     * @param dist les distances depuis le noeud de départ
     * @param visited les noeuds visités
     * @return l'indice du noeud le dont la distance est la plus faible.
     *          ou -1 s'il n'y en a pas
     */
    private static int minVertex(InfoNoeud[] infoNoeuds)
    {
        int x = Integer.MAX_VALUE;
        int y = -1; // graph not connected, or no unvisited vertices
        
        // Pour chaque noeuds
        for (int i = 0; i < infoNoeuds.length; i++)
        {
            // Si pas visité et la distance est plus faible 
            if (!infoNoeuds[i].visited && infoNoeuds[i].distArrivee < x)
            {
                y = i;
                x = infoNoeuds[i].distArrivee;
            }
        }
        
        return y;
    }
}
