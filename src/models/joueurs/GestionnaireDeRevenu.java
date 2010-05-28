package models.joueurs;

import models.jeu.Jeu;
import models.joueurs.Joueur;

/**
 * Classe d'encapsulation des tours.
 * 
 * Permet de faire vivre toutes les tours sous le meme thread et ainsi 
 * aleger le processeur.
 * 
 * Toutes les tours tournent sous le meme clock.
 */
public class GestionnaireDeRevenu implements Runnable
{
    private static final long TEMPS_REVENU_CREATURE = 10000; // 10 sec
    private boolean gestionEnCours;
    private boolean enPause = false;
    private Object pause = new Object();
    private Jeu jeu;
    
    
    /**
     * Constructeur du gestionnaire des creatures
     */
    public GestionnaireDeRevenu(Jeu jeu)
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
 
    @Override
    public void run()
    {
        gestionEnCours = true;
        
        while(gestionEnCours)
        {
            // donne de l'argent
            for(Joueur joueur : jeu.getJoueurs())
                synchronized(joueur)
                {
                    joueur.setNbPiecesDOr(joueur.getNbPiecesDOr() + 100);
                }
            
            // gestion de la pause
            try
            {
                synchronized (pause)
                {
                    if(enPause)
                        pause.wait();
                } 
                
                Thread.sleep(TEMPS_REVENU_CREATURE);
            } 
            catch (InterruptedException e){
                 e.printStackTrace();
            }
        }
    }
    
    /**
     * Permet d'arreter toutes les creatures
     */
    public void arreterCreatures()
    {
        gestionEnCours = false;
    }
    
    /**
     * Permet de mettre les créatures en pause.
     */
    public void mettreEnPause()
    {
        enPause = true;
    }
    
    /**
     * Permet de sortir les créatures de la pause.
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
