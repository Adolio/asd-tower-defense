package models.animations;

import java.awt.Graphics2D;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Classe d'encapsulation des animations.
 * 
 * Permet de faire vivre toutes les animation sous le meme thread et ainsi 
 * aleger le processeur.
 * 
 * Toutes les animations tournent sous le meme clock.
 */
public class GestionnaireAnimations implements Runnable
{
    private static final long TEMPS_ATTENTE = 50;
    private Vector<Animation> animations = new Vector<Animation>();
    private Thread thread;
    private boolean gestionEnCours;
    
    /**
     * Constructeur du gestionnaire des animations
     */
    public GestionnaireAnimations()
    {
        thread = new Thread(this);
        thread.start();
    }
    
    /**
     * Permet d'ajouter une animation
     * 
     * @param animation l'animation a ajouter
     */
    public void ajouterAnimation(Animation animation)
    {
        animations.add(animation);
    }
    
    /**
     * Permet de dessiner toutes les animations
     */
    public void dessinerAnimations(Graphics2D g2)
    {
        synchronized (animations)
        {
            Enumeration<Animation> eAnimations = animations.elements();
            Animation animation;
            while(eAnimations.hasMoreElements())
            {
                animation = eAnimations.nextElement();
                animation.dessiner(g2);
            }
        }
    }

    @Override
    public void run()
    {
       gestionEnCours = true;
        
       while(gestionEnCours)
       {
           synchronized (animations)
           { 
               Animation animation;
               for(int i=0;i<animations.size();i++)
               {
                   animation = animations.get(i);
                   
                   // detruit l'animation si elle est terminee
                   if(animation.estTerminee())
                       animations.remove(i--);
                   else
                   {
                       // anime l'animation
                       animation.animer();
                   }
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
     * Permet d'arreter toutes les animations
     */
    public void arreterAnimations()
    {
        gestionEnCours = false;
    }
}
