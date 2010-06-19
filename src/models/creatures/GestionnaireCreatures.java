package models.creatures;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

import models.jeu.Jeu;
import models.joueurs.Equipe;
import models.joueurs.GestionnaireDeRevenu;
import models.joueurs.Joueur;
import models.maillage.PathNotFoundException;

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
    private Jeu jeu;

    
    /**
     * Constructeur du gestionnaire des creatures
     */
    public GestionnaireCreatures(Jeu jeu)
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
        
        ArrayList<Creature> creaturesASupprimer = new ArrayList<Creature>();
        Creature creature;
        
        while(gestionEnCours)
        { 
            try
            {
               
                Enumeration<Creature> eCreatures = creatures.elements();
                while(eCreatures.hasMoreElements())
                {
                    creature = eCreatures.nextElement();
                    
                    creature.effacerSiPasMisAJour();
                    
                    // efface les creatures mortes
                    if(creature.aDetruire())
                        // ajout dans la liste des créatures à supprimer
                        creaturesASupprimer.add(creature);
                    else
                        // anime la creature
                        creature.action((long)(TEMPS_ATTENTE*jeu.getCoeffVitesse()));
                }
            }
            catch(NoSuchElementException nse)
            {
                System.err.println("[ERREUR] Créature introuvable");
            }
            
            // suppression des créatures
            for(Creature creatureASupprimer : creaturesASupprimer)
                creatures.remove(creatureASupprimer);
            creaturesASupprimer.clear();
            
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
    
    /**
     * Permet de recuperer une copie de la collection des creatures
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
            pause.notifyAll();
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

    public void detruire()
    {
        arreterCreatures();
        
        creatures.clear();
    }
    
    
    /**
     * Permet de lancer la vague de creature sur le terrain
     * 
     * @param terrain le terrain en question
     * @param edc l'ecouteur de creature fourni a chaque creature creee
     */
    public void lancerVague(final VagueDeCreatures vague,
                            final Joueur lanceur,
                            final Equipe equipeCiblee, 
                            final EcouteurDeVague edv,
                            final EcouteurDeCreature edc)
    {

        new Thread(
        new Runnable()
        {
            @Override
            public void run()
            {
                // recuperation des zones
                // TODO pour chaque zone de depart, lancer la vague...
                final Rectangle ZONE_DEPART = equipeCiblee.getZoneDepartCreatures(0);
                final Rectangle ZONE_ARRIVEE = equipeCiblee.getZoneArriveeCreatures();

                int xDepart = (int) ZONE_DEPART.getCenterX();
                int yDepart = (int) ZONE_DEPART.getCenterY();

                // creation des creatures de la vague
                for (int i = 0; i < vague.getNbCreatures() && jeu.estDemarre(); i++)
                {
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
                      
                    if(jeu.estDemarre())
                    {
                    
                        // calcul d'une position aleatoire de la creature dans la zone de
                        // depart
                       
                        if (jeu.getModeDePositionnnementDesCreatures() == Jeu.MODE_POSITIONNNEMENT_ALETOIRE)
                        {
                            xDepart = (int) (Math.random() * (ZONE_DEPART.getWidth() + 1) + ZONE_DEPART
                                    .getX());
                            yDepart = (int) (Math.random() * (ZONE_DEPART.getHeight() + 1) + ZONE_DEPART
                                    .getY());
                        }
            
                        // creation d'une nouvelle instance de la creature
                        // et affectation de diverses proprietes
                        Creature creature = vague.getNouvelleCreature();
                        creature.setX(xDepart);
                        creature.setY(yDepart);
                        creature.setEquipeCiblee(equipeCiblee);
                        creature.ajouterEcouteurDeCreature(edc);
            
                        try
                        {    
                            creature.setChemin(jeu.getTerrain().getCheminLePlusCourt(xDepart,
                                    yDepart, (int) ZONE_ARRIVEE.getCenterX(),
                                    (int) ZONE_ARRIVEE.getCenterY(), creature.getType()));
                            
                        }
                        catch (PathNotFoundException e1) 
                        {
                            // le chemin reste nul.
                        }
            
                        lanceur.ajouterRevenu(creature.getNbPiecesDOr()
                                *GestionnaireDeRevenu.POURCENTAGE_NB_PIECES_OR_CREATURE);
                        
                        ajouterCreature(creature);
                        jeu.creatureAjoutee(creature);
            
                        // temps d'attente entre chaque creature
                        try
                        {
                            Thread.sleep((long)(VagueDeCreatures.getTempsLancement(creature.getVitesseNormale()) / jeu.getCoeffVitesse()));
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

                if (edv != null)
                    edv.vagueEntierementLancee(vague); 
            }
        }).start();
  
        
    }


    
}
