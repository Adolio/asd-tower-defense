package models.maillage;

import java.util.ArrayList;
import java.util.List;

// avec matrice d'adjacence
public class GraphePondere_MA
{

    private Noeud[] noeuds;
    private int[][] arcs;
    private int nbArcs = 0;

    public GraphePondere_MA(int nbNoeuds)
    {
        noeuds = new Noeud[nbNoeuds];

        System.out.println((long) nbNoeuds * nbNoeuds * 32 / 8 / 1024 / 1024
                + " Méga de RAM !");

        arcs = new int[nbNoeuds][nbNoeuds];

        for (int i = 0; i < nbNoeuds; i++)
        {
            noeuds[i] = new Noeud(0, 0, 1);

            for (int j = 0; j < nbNoeuds; j++)
                arcs[i][j] = Integer.MAX_VALUE;
        }
    }

    public void setNoeud(int i, Noeud n)
    {
        noeuds[i] = n;
    }

    public void ajouterArc(int i, int j, int poids)
    {
        // si il n'y avait pas d'arcs
        if (this.arcs[i][j] == Integer.MAX_VALUE)
            nbArcs++;

        // non orienté
        this.arcs[i][j] = poids;
        this.arcs[j][i] = poids;
    }

    public int getNbNoeuds()
    {
        return noeuds.length;
    }

    public int getNbArcs()
    {
        return nbArcs;
    }

    public Noeud[] getNoeuds()
    {
        return noeuds.clone();
    }

    public Arc[] getArcs()
    {
        ArrayList<Arc> arcs = new ArrayList<Arc>();

        for (int i = 0; i < noeuds.length; i++)
            for (int j = 0; j < noeuds.length; j++)
                if (this.arcs[i][j] != Integer.MAX_VALUE)
                    arcs.add(new Arc(getNoeud(i), getNoeud(j)));

        Arc arcsArray[] = new Arc[arcs.size()];
        return arcs.toArray(arcsArray);
    }

    public int getPoids(int idNoeud1, int idNoeud2)
    {
        return arcs[idNoeud1][idNoeud2];
    }

    public int[] getIdVoisins(int idNoeud)
    {
        ArrayList<Integer> voisins = new ArrayList<Integer>();

        for (int i = 0; i < noeuds.length; i++)
        {
            // si l'arc existe
            if (getPoids(idNoeud, i) != Integer.MAX_VALUE)
                voisins.add(i);
        }

        return convertIntegers(voisins);
    }

    public Noeud getNoeud(int i)
    {
        return noeuds[i];
    }

    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }

    public Arc getArc(int idNoeud1, int idNoeud2)
    {
        return new Arc(getNoeud(idNoeud1), getNoeud(idNoeud2));
    }
}
