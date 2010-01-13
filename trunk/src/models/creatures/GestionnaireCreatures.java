package models.creatures;

import java.util.Vector;

/**
 * Classe d'encapsulation des tours.
 * 
 * Permet de faire vivre toutes les tours sous le meme thread et ainsi 
 * aleger le processeur.
 * 
 * Toutes les tours tournent sous le meme clock.
 */
public class GestionnaireCreatures implements Runnable
{
    private static final long TEMPS_ATTENTE = 50;
    private Vector<Creature> creatures = new Vector<Creature>();
    private Thread thread;
    private boolean gestionEnCours;
    
    /**
     * Constructeur du gestionnaire des creatures
     */
    public GestionnaireCreatures()
    {
        thread = new Thread(this);
        thread.start();
    }
    
    /**
     * Permet d'ajouter une creature
     * 
     * @param creature la creature a ajouter
     */
    public void ajouterCreature(Creature creature)
    {
        creatures.add(creature);
    }
    
    /**
     * Permet de supprimer une creature
     */
    public void supprimerCreature(Creature creature)
    {
        creatures.remove(creature);
    }

    @Override
    public void run()
    {
        gestionEnCours = true;
        
        while(gestionEnCours)
        {
            synchronized (creatures)
            { 
                Creature creature;
                for(int i=0;i<creatures.size();i++)
                {
                    creature = creatures.get(i);
                    
                    // efface les creatures mortes
                    if(creature.estMorte())
                        creatures.remove(i--);
                    else
                        // anime la creature
                        creature.action();
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
     * Permet d'arreter toutes les creatures
     */
    public void arreterCreatures()
    {
        gestionEnCours = false;
        
        creatures.clear();
    }
    
    // TODO [PAS PROPRE] faire mieux, perd l'encapsulation
    /**
     * Permet de recuperer la collection des creatures
     */
    public Vector<Creature> getCreatures()
    {
        return creatures;
    }
}
