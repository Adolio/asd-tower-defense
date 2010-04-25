package models.jeu;

import java.util.ArrayList;
import models.animations.*;
import models.creatures.Creature;
import models.creatures.EcouteurDeCreature;
import models.creatures.EcouteurDeVague;
import models.creatures.GestionnaireCreatures;
import models.creatures.VagueDeCreatures;
import models.joueurs.Equipe;
import models.joueurs.Joueur;
import models.terrains.Terrain;
import models.tours.GestionnaireTours;
import models.tours.Tour;

/**
 * Classe de gestion du jeu.
 * 
 * Cette classe contient :
 * <br>
 * - Le terrain qui contient les tours et les creatures
 * <br>
 * - Les informations du jeu et du joueur (score, pieces d'or, etc.)
 * <p>
 * Elle sert principalement de classe d'encapsulation du terrain.
 * Elle gere egalement tout ce qui concerne les pieces d'or du joueur 
 * (achat, amelioration et vente des tours)
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Tour
 */
public class Jeu
{
	/**
	 * version du jeu
	 */
    private static final String VERSION 
        = "heig-vd - ASD2 :: Tower Defense v1.0 | janvier 2010";

	/**
	 * vies restantes du joueur. 
	 * <br>
	 * Note : Lorsque un ennemi atteint la zone d'arrive, le nombre de vies est
	 * decremente.
	 */
    // TODO effacer
	//private int viesRestantes 	= 20;
	
	/**
	 * Le terrain de jeu que contient tous les elements principaux :
	 * - Les tours
	 * - Les creatures
	 * - Le maillage
	 */
	private Terrain terrain;
	
	/**
	 * Collection des équipes en jeu
	 */
	private ArrayList<Equipe> equipes = new ArrayList<Equipe>();
	
    /**
     * Les tours sont posees sur le terrain et permettent de tuer les creatures.
     * 
     * @see Tour
     */
    private GestionnaireTours gestionnaireTours;

    /**
     * Les creatures de deplacent sur le terrain d'une zone de depart a une zone
     * d'arrivee.
     * 
     * @see Creature
     */
    private GestionnaireCreatures gestionnaireCreatures;

    /**
     * Outil de gestion des animations
     */
    private GestionnaireAnimations gestionnaireAnimations;

    /**
     * Variable d'etat de la pause
     */
    private boolean enPause;
  
    /**
     * TODO commenter
     */
    VagueDeCreatures vagueCourante;
    
    /**
     * Constructeur
     */
    public Jeu()
    {
        gestionnaireTours     = new GestionnaireTours(this);
        gestionnaireCreatures = new GestionnaireCreatures();
        gestionnaireAnimations = new GestionnaireAnimations();  
    }
    
    /**
     * Permet de recuperer la version du jeu.
     * 
     * @return la version du jeu.
     */
    public static String getVersion()
    {
        return VERSION;
    }

	/**
	 * Permet de lancer une nouvelle vague de creatures.
	 */
	public void lancerVagueSuivante(Equipe cible, EcouteurDeVague edv,EcouteurDeCreature edc)
	{
	    // lancement de la vague
	    VagueDeCreatures vagueCourante = terrain.getVagueDeCreaturesSuivante();
        
	    terrain.passerALaProchaineVague();
	    
        vagueCourante.lancerVague(this, cible, edv, edc);
	}

    /**
     * Permet de terminer la partie en cours
     */
    public void terminerLaPartie()
    {
        arreterTout();
    }

    /**
     * Permet de recuperer le gestionnaire d'animations
     * 
     * @return le gestionnaire d'animations
     */
    public GestionnaireAnimations getGestionnaireAnimations()
    {
        return gestionnaireAnimations;    
    }
  
    /**
     * Permet de recuper la couleur des murs du terrain
     * 
     * @param l'equipe a ajouter
     * @throws IllegalArgumentException si l'equipe est nulle.
     */
    public void ajouterEquipe(Equipe equipe) throws IllegalArgumentException
    {
        if(equipe == null)
            throw new IllegalArgumentException();
        
        equipes.add(equipe);
    }
    
    /**
     * Permet d'initialiser le terrain de jeu
     * 
     * @param terrain le terrain
     * @throws IllegalArgumentException si le terrain à déjà été initialisé.
     */
    public void setTerrain(Terrain terrain) throws IllegalArgumentException
    {
        if(this.terrain != null)
            throw new IllegalArgumentException("Terrain déjà initialisé");

        this.terrain  = terrain;
    }

    /**
     * Permet de recuperer le terrain
     * 
     * @return le terrain
     * @throws Exception si le terrain est nul
     */
    public Terrain getTerrain()
    {
        if(terrain == null)
            throw new IllegalStateException("Le terrain ne doit jamais etre nul !");
        
        return terrain;
    }

    /**
     * Permet de recuperer le gestionnaire de tours
     * 
     * @return le gestionnaire de tours
     */
    public GestionnaireTours getGestionnaireTours()
    {
        return gestionnaireTours;
    }
    
    /**
     * Permet de recuperer le gestionnaire de creatures
     * 
     * @return le gestionnaire de creatures
     */
    public GestionnaireCreatures getGestionnaireCreatures()
    {
        return gestionnaireCreatures;
    }

    /**
     * Permet de stope tous les threads des elements
     */
    public void arreterTout()
    {
        // arret de toutes les tours
        gestionnaireTours.arreterTours();

        // arret de toutes les creatures
        gestionnaireCreatures.arreterCreatures();

        // arret de toutes les animations
        gestionnaireAnimations.arreterAnimations();
    }


    /**
     * Permet d'initialiser la partie avant le commencement
     */
    public void initialiser()
    {
        if(terrain == null)
            throw new IllegalStateException("Terrain nul");
        
        if(equipes.size() == 0)
            throw new IllegalStateException("Aucune équipe inscrite");
        
        // initialisation des valeurs par defaut
        for(Equipe equipe : equipes)
        {
            // initialisation des vies restantes
            equipe.setNbViesRestantes(terrain.getNbViesInitiales());
            
            // initialisation des pieces d'or des joueurs
            for(Joueur joueur : equipe.getJoueurs())
                joueur.setNbPiecesDOr(terrain.getNbPiecesOrInitiales());
        }     
    }

    /**
     * Permet de mettre en pause le jeu.
     * 
     * @return true si le jeu est en pause après l'appel false sinon
     */
    public boolean togglePause()
    {
        if(enPause)
        {
            gestionnaireTours.sortirDeLaPause();
            gestionnaireCreatures.sortirDeLaPause();
            gestionnaireAnimations.sortirDeLaPause();
            terrain.sortirDeLaPause();
        }
        else
        {
            gestionnaireTours.mettreEnPause();
            gestionnaireCreatures.mettreEnPause();
            gestionnaireAnimations.mettreEnPause();
            terrain.mettreEnPause();
        }
        
        return enPause = !enPause;  
    }

    /**
     * Permet de savoir si le jeu est en pause
     * 
     * @return true s'il l'est false sinon
     */
    public boolean estEnPause()
    {
        return enPause;
    }

    public Equipe getEquipe(int i)
    {
        return equipes.get(0);
    }

    public ArrayList<Equipe> getEquipes()
    {
        return equipes;
    }
}
