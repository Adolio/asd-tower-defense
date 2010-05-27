package models.tours;

import java.util.Enumeration;
import java.util.Vector;

import models.creatures.Creature;
import models.jeu.Jeu;

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
    private boolean gestionEnCours;
    private Jeu jeu;
    private boolean enPause = false;
    private Object pause = new Object();
    
    /**
     * Constructeur du gestionnaire des animations
     */
    public GestionnaireTours(Jeu jeu)
    {
        this.jeu = jeu; 
    }
    
    /**
     * Permet de demarrer la gestion
     */
    public void demarrer()
    {
        Thread thread = new Thread(this);
        thread.start();
    }
    
    /**
     * Permet de supprimer une tour du terrain.
     * 
     * @param tour la tour a supprimer
     * @param miseAJourMaillage 
     */
    public void supprimerTour(Tour tour)
    {
        // c'est bien une tour valide ?
        if (tour == null)
            throw new IllegalArgumentException("Tour nulle");

        // arret du thread
        tour.arreter();

        synchronized (tours)
        {
            tours.remove(tour);
        }
        
        // reactive la zone dans le maillage qui correspond a la tour
        jeu.getTerrain().activerZone(tour, true);
    }
    
    /**
     * Permet d'ajouter une tour
     * 
     * @param tour la tour a ajouter
     */
    public void ajouterTour(Tour tour)
    {
        synchronized(tours)
        {
            tours.add(tour);
        }
    }
    
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
                    if(tour.estEnJeu())
                        tour.action(TEMPS_ATTENTE); 
                }
            }
            
            // gestion de la pause
            try
            {
                synchronized (pause)
                {
                    if(enPause)
                        pause.wait();
                } 
            } 
            catch (InterruptedException e1)
            {
                e1.printStackTrace();
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
    }
    
    /**
     * Permet de recuperer une copie de la collection des tours
     */
    @SuppressWarnings("unchecked")
    public Vector<Tour> getTours()
    {
        return (Vector<Tour>) tours.clone();
    }

    /**
     * Permet de mettre les tours en pause.
     */
    public void mettreEnPause()
    {
        enPause = true;
    }
    
    /**
     * Permet de sortir les tours de la pause.
     */
    public void sortirDeLaPause()
    { 
        synchronized (pause)
        {
            enPause = false;
            pause.notify(); 
        }
    }

    /**
     * Permet de savoir si une tour peut etre posee.
     * 
     * Controle de l'intersection avec les tours.
     * Controle de l'intersection avec les creatures.
     * Controle de l'intersection avec les zones du terrain. (murs et depart / arrive)
     * 
     * @param tour la tour a posee
     * @return true si la tour peut etre posee, false sinon
     */
    public boolean laTourPeutEtrePosee(Tour tour)
    {
        // c'est une tour valide ?
        if (tour == null)
            return false;

        // suffisemment d'argent ?
        if(!laTourPeutEtreAchetee(tour))
            return false;
        
        // il n'y a pas deja une tour
        synchronized (tours)
        {
            for (Tour tourCourante : tours)
                if (tour.intersects(tourCourante))
                    return false;
        }
        
        // elle est dans la zone de construction du joueur
        if(!tour.getPrioprietaire().getEmplacement().getZoneDeConstruction().contains(tour))
            return false;
        
        // il n'y a pas un mur et elle est bien dans le terrain
        if(!jeu.getTerrain().laTourPeutEtrePosee(tour))
            return false;

        // il n'y a pas deja une creature
        Creature creature;
        Enumeration<Creature> eCreatures = jeu.getCreatures().elements();
        while(eCreatures.hasMoreElements())
        {
            creature = eCreatures.nextElement();
        
            if (tour.intersects(creature))
                return false;
        }

        // rien empeche la tour d'etre posee
        return true;
    }

    /**
     * Permet de savoir si une tour peut etre achetee.
     * 
     * @param tour la tour a achetee
     * @return true si le joueur a assez de pieces d'or, false sinon
     */
    public boolean laTourPeutEtreAchetee(Tour tour)
    {
        if(tour != null)
            return (tour.getPrioprietaire().getNbPiecesDOr() - tour.getPrixAchat()) >= 0;
        
        return false;
    }

    /**
     * Permet de recuperer une tour grace a son identificateur
     * 
     * @param idTour identificateur de la tour
     * @return la tour, null si non trouvé
     */
    public Tour getTour(int idTour)
    {
        synchronized (tours)
        { 
            Tour tour;
            for(int i=0;i<tours.size();i++)
            {
                tour = tours.get(i);
                
                if(tour.getId() == idTour)
                    return tour;
            }
        }
        
        return null;
    }
}
