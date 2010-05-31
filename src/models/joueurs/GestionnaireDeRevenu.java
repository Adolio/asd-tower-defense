package models.joueurs;

import models.jeu.Jeu;
import models.joueurs.Joueur;

/**
 * Classe de gestion des revenus des joueurs
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 * @since jdk1.6.0_16
 */
public class GestionnaireDeRevenu implements Runnable
{
    public static final double POURCENTAGE_NB_PIECES_OR_CREATURE = 0.01;
    
    private static final long TEMPS_REVENU_CREATURE = 5000; // ms
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
                joueur.donnerRevenu(TEMPS_REVENU_CREATURE);
              
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
