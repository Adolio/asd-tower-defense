package models.tours;

import java.awt.Rectangle;
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
        
        Thread thread = new Thread(this);
        thread.start();
    }
    
    /**
     * Permet de poser une tour avec tous les controles necessaires
     * 
     * @param tour la tour a poser
     * @throws Exception si la zone n'est pas accessible (occupee)
     * @throws Exception si la tour bloque empeche tous chemins
     */
    public void poserTour(Tour tour) throws Exception
    {
        // c'est bien une tour valide ?
        if (tour == null)
            throw new IllegalArgumentException("Tour nulle");

        // suffisemment d'argent ?
        if(!laTourPeutEtreAchetee(tour))    
            throw new Exception("Pose impossible : Pas assez d'argent");
        
        // si elle peut pas etre posee
        if (!laTourPeutEtrePosee(tour))
            throw new Exception("Pose impossible : Zone non accessible");

        // si elle bloque le chemin de A vers B
        if (jeu.getTerrain().laTourBloqueraLeChemin(tour))
            throw new Exception("Pose impossible : Chemin bloqué");

        // desactive la zone dans le maillage qui correspond a la tour
        jeu.getTerrain().desactiverZone(tour, true);

        // ajout de la tour
        ajouterTour(tour);
        
        // mise a jour du jeu de la tour
        tour.setJeu(jeu);
        
        // mise en jeu de la tour
        tour.mettreEnJeu();
        
        // debit des pieces d'or
        tour.getPrioprietaire().setNbPiecesDOr(
                tour.getPrioprietaire().getNbPiecesDOr() - tour.getPrixAchat());
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
        
        // il n'y a pas un mur
        if(!jeu.getTerrain().laTourPeutEtrePosee(tour))
            return false;

        // il n'y a pas deja une creature
        Vector<Creature> creatures = jeu.getGestionnaireCreatures().getCreatures();
        synchronized (creatures)
        {
            for (Rectangle creature : creatures)
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
     * Permet d'ameliorer une tour.
     * 
     * @param tour la tour a ameliorer
     * @return vrai si operation realisee avec succes, sinon faux 
     * @throws Exception si pas assez d'argent 
     * @throws Exception si niveau max de la tour atteint
     */
    public void ameliorerTour(Tour tour) throws Exception
    {
        if(!tour.peutEncoreEtreAmelioree())
            throw new Exception("Amélioration impossible : Niveau max atteint");
        
        if(tour.getPrioprietaire().getNbPiecesDOr() < tour.getPrixAchat())
            throw new Exception("Amélioration impossible : Pas assez d'argent");

        // debit des pieces d'or
        tour.getPrioprietaire().setNbPiecesDOr(tour.getPrioprietaire().getNbPiecesDOr() - tour.getPrixAchat());
     
        // amelioration de la tour
        tour.ameliorer();
    }
    
    /**
     * Permet de supprimer une tour du terrain.
     * 
     * @param tour la tour a supprimer
     */
    public void supprimerTour(Tour tour)
    {
        // c'est bien une tour valide ?
        if (tour == null)
            throw new IllegalArgumentException("Tour nulle");

        // arret du thread
        tour.arreter();

        tours.remove(tour);
        
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
        tours.add(tour);
    }
    
    /**
     * Permet de vendre une tour.
     * 
     * @param tour la tour a vendre
     */
    public void vendreTour(Tour tour)
    {
        // supprime la tour
        supprimerTour(tour);
        
        // debit des pieces d'or
        tour.getPrioprietaire().setNbPiecesDOr(
                tour.getPrioprietaire().getNbPiecesDOr() + tour.getPrixDeVente());
    }

    // TODO [ORGANISATION] mettre en place
    /**
     * Permet de dessiner toutes les tours
     */
    /*
    public void dessinerTours(Graphics2D g2)
    {
        Enumeration<Tour> eTours = tours.elements();
        
        Tour tour;
        while(eTours.hasMoreElements())
        {
            tour = eTours.nextElement();
            
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
    
    // TODO [PAS PROPRE] faire mieux, perd l'encapsulation
    /**
     * Permet de recuperer la collection des tours
     */
    public Vector<Tour> getTours()
    {
        return tours;
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
}
