package models.creatures;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * Classe d'encapsulation des tours.
 * 
 * Permet de faire vivre toutes les tours sous le meme thread et ainsi 
 * aleger le processeur.
 * 
 * Toutes les tours tournent sous le meme clock.
 * 
 * @author Aurélien Da Campo
 * @version 1.1 | juin 2010
 * @since jdk1.6.0_16
 * @see Creature
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
    public GestionnaireCreatures(){}
    
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
            try
            {
                Creature creature;
                Enumeration<Creature> eCreatures = creatures.elements();
                while(eCreatures.hasMoreElements())
                {
                    creature = eCreatures.nextElement();
                    
                    creature.effacerSiPasMisAJour();
                    
                    // efface les creatures mortes
                    if(creature.aDetruire())
                        creatures.remove(creature);
                    else
                    {
                        // anime la creature
                        creature.action(TEMPS_ATTENTE);
                    }
                }
            }
            catch(NoSuchElementException nse)
            {
                System.err.println("[ERREUR] Créature introuvable");
            }
            
            
            // gestion de la pause
            try
            {
                synchronized (pause)
                {
                    if(enPause)
                        pause.wait();
                } 
            
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
        Vector<Creature> creaturesIntersectees = new Vector<Creature>();
        
        try
        {
            Creature creature;
            Enumeration<Creature> eCreatures = creatures.elements();
            while(eCreatures.hasMoreElements())
            {
                creature = eCreatures.nextElement();
        
                if(creature.intersects(rectangle))
                    creaturesIntersectees.add(creature);
            }
        }
        catch(NoSuchElementException nse)
        {
            System.err.println("[ERREUR] Créature introuvable");
        }

        return creaturesIntersectees;
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
        
        try
        {
            Creature creature;
            Enumeration<Creature> eCreatures = creatures.elements();
            while(eCreatures.hasMoreElements())
            {
                creature = eCreatures.nextElement();
    
                Point pCreature = new Point((int)creature.getCenterX(), 
                                            (int)creature.getCenterY());
                Point pCercle = new Point(x,y);
                
                if(pCreature.distance(pCercle) < rayon + creature.getWidth() / 2)
                    creaturesIntersctees.add(creature);
            }
        }
        catch(NoSuchElementException nse)
        {
            System.err.println("[ERREUR] Créature introuvable");
        }
        
        return creaturesIntersctees;
    }

    /**
     * Permet de recuperer une créature à l'aide de son identificateur
     * 
     * @param id l'identificateur
     * @return la créature trouvée, null sinon
     */
    public Creature getCreature(int id)
    {
        try
        {
            Creature creature;
            Enumeration<Creature> eCreatures = creatures.elements();
            while(eCreatures.hasMoreElements())
            {
                creature = eCreatures.nextElement();
                    
                if(creature.getId() == id)
                    return creature;
            }
        }
        catch(NoSuchElementException nse)
        {
            System.err.println("[ERREUR] Créature introuvable");
        }
        
        return null;
    }
}
