package models.creatures;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;
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
    private boolean gestionEnCours;
    private boolean enPause = false;
    private Object pause = new Object();
    
    
    /**
     * Constructeur du gestionnaire des creatures
     */
    public GestionnaireCreatures()
    {}
    
    /**
     * Permet de demarrer la gestion
     */
    public void demarrer()
    {
        Thread thread = new Thread(this);
        thread.start();
    }
    
    /**
     * Permet d'ajouter une creature
     * 
     * @param creature la creature a ajouter
     */
    public void ajouterCreature(Creature creature)
    {
        if (creature == null)
            throw new IllegalArgumentException("Creature nulle");
        
        creatures.add(creature);
    }
    
    /**
     * Permet de supprimer une creature
     */
    public void supprimerCreature(Creature creature)
    {
        if (creature != null)
            creatures.remove(creature);  
    }

    @Override
    public void run()
    {
        gestionEnCours = true;
        
        while(gestionEnCours)
        {
            Creature creature;
            Enumeration<Creature> eCreatures = creatures.elements();
            while(eCreatures.hasMoreElements())
            {
                creature = eCreatures.nextElement();
                
                // efface les creatures mortes
                if(creature.estMorte() || creature.aDetruire())
                    creatures.remove(creature);
                else
                    // anime la creature
                    creature.action(TEMPS_ATTENTE);
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
            
            try
            {
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
    }
    
    // TODO [PAS PROPRE] faire mieux, perd l'encapsulation
    /**
     * Permet de recuperer la collection des creatures
     */
    @SuppressWarnings("unchecked")
    public Vector<Creature> getCreatures()
    {
        return (Vector<Creature>) creatures.clone();
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

    /**
     * Permet de recupérer le créatures qui intersectent un rectangle
     * 
     * @param rectangle la zone
     * @return une collection de créatures
     */
    public Vector<Creature> getCreaturesQuiIntersectent(Rectangle rectangle)
    {
        
        Vector<Creature> creaturesIntersctees = new Vector<Creature>();
        Creature creature;
        Enumeration<Creature> eCreatures = creatures.elements();
        while(eCreatures.hasMoreElements())
        {
            creature = eCreatures.nextElement();
    
            for(int i=0;i<creatures.size();i++)
            {
                creature = creatures.get(i);

                if(creature.intersects(rectangle))
                    creaturesIntersctees.add(creature);
            }
        }
        
        return creaturesIntersctees;
    }
    
    
    /**
     * Permet de recupérer le créatures qui intersectent un cercle
     * 
     * @param rectangle la zone
     * @return une collection de créatures
     */
    public Vector<Creature> getCreaturesQuiIntersectent(int x, int y, double rayon)
    {
        Vector<Creature> creaturesIntersctees = new Vector<Creature>();
        
        synchronized (creatures)
        { 
            Creature creature;
            for(int i=0;i<creatures.size();i++)
            {
                creature = creatures.get(i);

                Point pCreature = new Point((int)creature.getCenterX(), 
                                            (int)creature.getCenterY());
                Point pCercle = new Point(x,y);
                
                if(pCreature.distance(pCercle) < rayon + creature.getWidth() / 2)
                    creaturesIntersctees.add(creature);
            }
        }
        
        return creaturesIntersctees;
    }
}
