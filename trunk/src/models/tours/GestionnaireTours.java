package models.tours;

import java.util.Vector;

/**
 * Classe d'encapsulation des tours.
 * 
 * Permet de faire vivre toutes les tours sous le meme thread et ainsi 
 * aleger le processeur.
 * 
 * Toutes les tours tournent sous le meme clock.
 */
public class GestionnaireTours implements Runnable
{
    private static final long TEMPS_ATTENTE = 50;
    private Vector<Tour> tours = new Vector<Tour>();
    private Thread thread;
    private boolean gestionEnCours;
    
    /**
     * Constructeur du gestionnaire des animations
     */
    public GestionnaireTours()
    {
        thread = new Thread(this);
        thread.start();
    }
    
    /**
     * Permet d'ajouter une tour
     * 
     * @param tour la tour a ajouter
     */
    public void ajouterTour(Tour tour)
    {
        tours.add(tour);
    }
    
    /**
     * Permet de supprimer une tour
     */
    public void supprimerTour(Tour tour)
    {
        tours.remove(tour);
    }

    /**
     * Permet de dessiner toutes les tours
     */
    // TODO mettre en place
    /*
    public void dessinerTours(Graphics2D g2)
    {
        Enumeration<Tour> eTours = tours.elements();
        
        Tour tour;
        while(eTours.hasMoreElements())
        {
            tour = eTours.nextElement();
            
            // TODO
            //tour.dessiner(g2);
        }
    }*/
    
    @Override
    public void run()
    {
        gestionEnCours = true;
        
        while(gestionEnCours)
        {
            synchronized (tours)
            { 
                Tour tour;
                for(int i=0;i<tours.size();i++)
                {
                    tour = tours.get(i);
                    
                    // anime l'animation
                    tour.action(); 
                }
            }
            
            try{
                 Thread.sleep(TEMPS_ATTENTE);
            } 
            catch (InterruptedException e){
                 e.printStackTrace();
            }
        }
    }
    
    /**
     * Permet d'arreter toutes les tours
     */
    public void arreterTours()
    {
        gestionEnCours = false;
        
        tours.clear();
    }
    
    // TODO faire mieux
    /**
     * Permet de recuperer la collection des tours
     */
    public Vector<Tour> getTours()
    {
        return tours;
    }
}
