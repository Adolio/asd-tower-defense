package models.animations;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;
import models.jeu.Jeu;

/**
 * Classe d'encapsulation des animations.
 * 
 * Permet de faire vivre toutes les animation sous le meme thread et ainsi 
 * alléger le processeur.
 * 
 * Toutes les animations tournent sous le meme clock.
 * 
 * @author Aurelien Da Campo
 * @version 1.1 | juin 2010
 * @since jdk1.6.0_16
 */
public class GestionnaireAnimations implements Runnable
{
    private static final long TEMPS_ATTENTE = 50;
    private Vector<Animation> animations = new Vector<Animation>();
    private Thread thread;
    private boolean gestionEnCours;
    private boolean enPause = false;
    private Object pause = new Object();
    private Jeu jeu;
    
    /**
     * Constructeur du gestionnaire des animations
     */
    public GestionnaireAnimations(Jeu jeu)
    {
        this.jeu = jeu;
    }
    
    /**
     * Permet de demarrer la gestion
     */
    public void demarrer()
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
    public void dessinerAnimations(Graphics2D g2, int hauteur)
    {
        try
        {
            Enumeration<Animation> eAnimations = animations.elements();
            Animation animation;
            while(eAnimations.hasMoreElements())
            {
                animation = eAnimations.nextElement();
                
                // seulement pour l'hauteur passée
                if(animation.getHauteur() == hauteur)
                    animation.dessiner(g2);
            }
        }
        
        // Cette erreur vient de la suppression d'une animation
        // dans la méthode run. J'ai essayé d'utiliser un objet Iterator 
        // pour pouvoir supprimmer proprement l'animation du vecteur
        // mais des erreurs de concurrence surviennent! Même en englobant 
        // l'iterateur d'un synchronized(animations)... pourtant cette structure
        // est recommandé sur les forums et sitewebs...
        catch(NoSuchElementException nse)
        {
            System.err.println("[ERREUR] Animation introuvable");
        }
    }

    @Override
    public void run()
    {
       gestionEnCours = true;
        
       /*
       // CECI NE MARCHE PAS :
       // Poutant Iterator doit normalement gérer cela ?!?
       // Une erreur de concurrence est levée 
       // j'utilise alors un tableau temporaire de suppression
       while(gestionEnCours)
       {
           Iterator<Animation> iAnimations = animations.iterator();
           synchronized(animations)
           {
               Animation animation;
              
               while(iAnimations.hasNext())
               {
                   animation = iAnimations.next();
                   
                   // detruit l'animation si elle est terminee
                   if(animation.estTerminee())
                       iAnimations.remove();
                   else
                   {
                       // anime l'animation
                       animation.animer(TEMPS_ATTENTE);
                   }
               }
           }
           
       */
       

       ArrayList<Animation> animationsASupprimer = new ArrayList<Animation>();
       Animation animation;
       
       while(gestionEnCours)
       {
           try
           {
               //System.out.println(animations.size());
               
               Enumeration<Animation> eAnimations = animations.elements();
               while(eAnimations.hasMoreElements())
               {
                   animation = eAnimations.nextElement();

                   // detruit l'animation si elle est terminee
                   if(animation.estTerminee())
                       animationsASupprimer.add(animation);
                   else
                       // anime l'animation
                       animation.animer((long)(TEMPS_ATTENTE*jeu.getCoeffVitesse()));
               }
           }
           catch(NoSuchElementException nse)
           {
               System.err.println("[ERREUR] Animation introuvable");
           }
 
           // suppression des créatures
           for(Animation animationASupprimer : animationsASupprimer)
               animations.remove(animationASupprimer);
           animationsASupprimer.clear();
         
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
     * Permet d'arreter toutes les animations
     */
    public void arreterAnimations()
    {
        gestionEnCours = false;
    }

    /**
     * Permet de mettre les animations en pause.
     */
    public void mettreEnPause()
    {
        enPause = true;
    }
    
    /**
     * Permet de sortir les animations de la pause.
     */
    public void sortirDeLaPause()
    { 
        synchronized (pause)
        {
            enPause = false;
            pause.notify();
        }
    }

    public void detruire()
    {
        arreterAnimations();
        animations.clear();
    }
}
