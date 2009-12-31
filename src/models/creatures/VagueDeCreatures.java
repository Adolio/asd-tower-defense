package models.creatures;

import java.awt.Rectangle;
import models.terrains.Terrain;

/**
 * Structure permettant de stoquer des informations et 
 * lancer une vague de creatures.
 * 
 * Une vagues de creature contient un certain nombres de creatures du meme type.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Creature
 */
public class VagueDeCreatures implements Runnable
{
	/**
	 * le nombre de creatures de la vague
	 */
    private final int NB_CREATURES;
    
    /**
     * le temps d'attente entre chaque apparition de creature lors du lancement
     * de la vague.
     */
	private final long TEMPS_LANCER_ENTRE_CREATURE;
	
	/**
	 * position de la creature dans la zone de depart lors du lancement
     * de la vague.
     * 
     * true     = aleatoire dans la zone
     * false    = au centre de la zone
	 */
	private final boolean POSITION_DEPART_ALEATOIRE;
	
	/**
	 * le type de la creature a envoyer
	 */
	private final Creature CREATURE_A_ENVOYER;
	
	/**
	 * la description de la vague
	 */
	private final String DESCRIPTION;
	
	/**
	 * thread de gestion du lancement des creature
	 */
	private Thread thread;
	
	/**
	 * le terrain su lequel lancer les creatures
	 */
	private Terrain terrain;
	
	/**
	 * l'ecouteur de creature a fournir a chaque creature lors du lancement
	 */
	private EcouteurDeCreature edc;
	
	/**
     * l'ecouteur de vague pour informer des evenements de la vague
     */
	private EcouteurDeVague edv;
	
	
	/**
	 * Constructeur de la vague de creatures
	 * @param nbCreatures le nombre de copie de la creature a envoyer
	 * @param creatureAEnvoyer un objet de la creature a envoyer nbCreatures fois
	 * @param tempsLancerEntreCreature temps d'attente entre chaque creature envoyee
	 * @param positionDepartAleatoire la creature est-elle positionnee aleatoirement 
	 *                                dans la zone de depart ou au centre ?
	 * @param description description de la vague
	 */
	public VagueDeCreatures(int nbCreatures, 
							Creature creatureAEnvoyer, 
							long tempsLancerEntreCreature,
							boolean positionDepartAleatoire,
							String description)
	{
		NB_CREATURES		        = nbCreatures;
		TEMPS_LANCER_ENTRE_CREATURE = tempsLancerEntreCreature;
		POSITION_DEPART_ALEATOIRE   = positionDepartAleatoire;
		CREATURE_A_ENVOYER 	        = creatureAEnvoyer;
		DESCRIPTION		            = description;
	}
	
	/**
     * Constructeur de la vague de creatures sans description
     * 
     * @param nbCreatures le nombre de copie de la creature a envoyer
     * @param creatureAEnvoyer un objet de la creature a envoyer nbCreatures fois
     * @param tempsLancerEntreCreature temps d'attente entre chaque creature envoyee
     * @param positionDepartAleatoire la creature est-elle positionnee aleatoirement 
     *                                dans la zone de depart ou au centre ?
     */
    public VagueDeCreatures(int nbCreatures, 
                            Creature creatureAEnvoyer,
                            long tempsLancerEntreCreature,
                            boolean positionDepartAleatoire)
    {
        this(nbCreatures,
             creatureAEnvoyer,
             tempsLancerEntreCreature,
             positionDepartAleatoire,
             "");
    }
    
	/**
	 * Permet de recuperer le nombre de creatures dans la vague
	 * @return le nombre de creatures dans la vague
	 */
	public int getNbCreatures()
	{
		return NB_CREATURES;
	}
	
	/**
	 * Permet de recuperer une copie de la creature a envoyer.
	 * @return une copie de la creature a envoyer
	 */
	public Creature getNouvelleCreature()
	{
		return CREATURE_A_ENVOYER.copier();
	}
	
	/**
	 * Permet de recuperer la description de la vague.
	 * @return la description de la vague.
	 */
	public String getDescription()
	{
		return DESCRIPTION;
	}
	
	/**
     * Permet de recuperer une synthese generee des proprietes de la vague
     * @return la description de la vague.
     */
    public String toString()
    {
        return NB_CREATURES+" Creature(s) "+CREATURE_A_ENVOYER.getNomType().toLowerCase()+"(s) [ " +
        		"sante : "+CREATURE_A_ENVOYER.getSanteMax()+", " +
        		"gain : "+CREATURE_A_ENVOYER.getNbPiecesDOr()+", "+
        		"vitesse : "+CREATURE_A_ENVOYER.getVitesseNormale()+" ]";
    }

    
    /**
     * Permet de lancer la vague de creature sur le terrain
     * 
     * @param terrain le terrain en question
     * @param edc l'ecouteur de creature fourni a chaque creature creee
     */
    public void lancerVague(Terrain terrain, EcouteurDeVague edv, EcouteurDeCreature edc)
    {
       this.terrain = terrain;
       this.edc     = edc;
       this.edv     = edv;
       thread       = new Thread(this);
       thread.start();
    }
    
    
    public void run()
    {
        // recuperation des zones
        final Rectangle ZONE_DEPART  = terrain.getZoneDepart();
        final Rectangle ZONE_ARRIVEE = terrain.getZoneArrivee();
        
        int xDepart = (int) ZONE_DEPART.getCenterX();
        int yDepart = (int) ZONE_DEPART.getCenterY();
        
        // creation des creatures de la vague
        for(int i=0;i<NB_CREATURES;i++)
        {   
            // calcul d'une position aleatoire de la creature dans la zone de depart
            if(POSITION_DEPART_ALEATOIRE)
            {
                xDepart = (int)(Math.random() * (ZONE_DEPART.getWidth() 
                        + 1) + ZONE_DEPART.getX());
                yDepart = (int)(Math.random() * (ZONE_DEPART.getHeight() 
                       + 1) + ZONE_DEPART.getY());
            }
            
            // creation d'une nouvelle instance de la creature
            // et affectation de diverses proprietes
            Creature creature = getNouvelleCreature();
            creature.setX(xDepart);
            creature.setY(yDepart);
            creature.ajouterEcouteurDeCreature(edc);
            creature.setChemin(terrain.getCheminLePlusCourt(
                                    xDepart, 
                                    yDepart, 
                                    (int) ZONE_ARRIVEE.getCenterX(), 
                                    (int) ZONE_ARRIVEE.getCenterY(),
                                    creature.getType()));
          
            terrain.ajouterCreature(creature);
            creature.demarrer();
            
            // temps d'attente entre chaque creature
            try{
                Thread.sleep(TEMPS_LANCER_ENTRE_CREATURE);
            } 
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        
        if(edv != null)
            edv.vagueEntierementLancee(this);
    }
    
    /**
     * Permet de modifier l'ecouteur de vague
     * 
     * @param edv le nouvel ecouteur de vague
     */
    public void setEcouteurDeVague(EcouteurDeVague edv)
    {
        this.edv = edv;
    }
}
