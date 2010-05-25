package models.jeu;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Vector;

import exceptions.AucunePlaceDisponibleException;
import exceptions.ZoneInaccessibleException;
import exceptions.JeuEnCoursException;
import exceptions.NiveauMaxAtteintException;
import exceptions.ArgentInsuffisantException;
import exceptions.CheminBloqueException;
import exceptions.TerrainDejaInitialise;
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
 * @author Aurelien Da Campo
 * @version 2.1 | mai 2010
 * @since jdk1.6.0_16
 * @see Tour
 */
public abstract class Jeu implements EcouteurDeCreature, 
                            EcouteurDeVague
{
	/**
	 * version du jeu
	 */
    private static final String VERSION 
        = "ASD - Tower Defense v2.0 [dev. version] | juin 2010 | heig-vd";

	/**
	 * Le terrain de jeu que contient tous les elements principaux :
	 * - Les tours
	 * - Les creatures
	 * - Le maillage
	 */
    protected Terrain terrain;
	
	/**
	 * Collection des équipes en jeu
	 */
	private ArrayList<Equipe> equipes = new ArrayList<Equipe>();
	
    /**
     * Les tours sont posees sur le terrain et permettent de tuer les creatures.
     * 
     * @see Tour
     */
	protected GestionnaireTours gestionnaireTours;

    /**
     * Les creatures de deplacent sur le terrain d'une zone de depart a une zone
     * d'arrivee.
     * 
     * @see Creature
     */
	protected GestionnaireCreatures gestionnaireCreatures;

    /**
     * Outil de gestion des animations
     */
	protected GestionnaireAnimations gestionnaireAnimations;

    /**
     * Variable d'etat de la pause
     */
	protected boolean enPause;
  
    /**
     * Stockage de la vagues courante
     */
    VagueDeCreatures vagueCourante;

    /**
     * Permet de savoir si la partie est terminée
     */
    protected boolean estTermine;

    /**
     * Permet de savoir si la partie est initialisée
     */
    private boolean estInitialise;
    
    /**
     * Pour notifications (observable)
     */
    private EcouteurDeJeu edj;
    
    /**
     * Joueur principal  
     */
    protected Joueur joueur;

    /**
     * Permet de savoir si la partie est à été démarrée
     */
    private boolean partieDemarree;
    
    /**
     * Constructeur
     */
    public Jeu()
    {
        gestionnaireTours      = new GestionnaireTours(this);
        gestionnaireCreatures  = new GestionnaireCreatures();
        gestionnaireAnimations = new GestionnaireAnimations();
    }
    
    /**
     * Permet d'initialiser la partie avant le commencement
     * 
     * @param joueur 
     */
    public void initialiser(Joueur joueur)
    {
        if(terrain == null)
            throw new IllegalStateException("Terrain nul");
        
        if(equipes.size() == 0)
            throw new IllegalStateException("Aucune équipe inscrite");
        
        // le joueur principal
        this.joueur = joueur;
        
        // initialisation des valeurs par defaut
        for(Equipe equipe : equipes)
        {
            // initialisation des vies restantes
            equipe.setNbViesRestantes(terrain.getNbViesInitiales());
            
            // initialisation des pieces d'or des joueurs
            for(Joueur j : equipe.getJoueurs())
                j.setNbPiecesDOr(terrain.getNbPiecesOrInitiales());
        }  
        
        estInitialise = true;
    }

    /**
     * Permet de démarrer la partie
     */
    public void demarrer()
    {
        if(terrain == null)
            throw new IllegalStateException("Terrain nul");
        
        if(!estInitialise)
            throw new IllegalStateException("Le jeu n'est pas initialisé");
            
        // donne les pieces aux joueurs et les vies aux equipes
        for(Equipe equipe : getEquipes())
        {
            equipe.setNbViesRestantes(terrain.getNbViesInitiales());
            
            for(Joueur joueur : equipe.getJoueurs())
                joueur.setNbPiecesDOr(terrain.getNbPiecesOrInitiales());
        }
        
        // demarrage des gestionnaires
        gestionnaireTours.demarrer();
        gestionnaireCreatures.demarrer();
        gestionnaireAnimations.demarrer();
        
        partieDemarree = true;
        
        // notification
        edj.partieDemarree();
    }
    
    /**
     * Indique au jeu qu'une vague veut etre lancée
     * 
     * @param vague la vague
     */
    public void lancerVague(VagueDeCreatures vague)
    { 
        vague.lancerVague(this, joueur.getEquipe(), this, this);
    }
    
    /**
     * Permet de poser un tour
     * 
     * @param tour la tour
     * @throws Exception si c'est pas possible
     */
    public void poserTour(Tour tour) throws ArgentInsuffisantException, ZoneInaccessibleException, CheminBloqueException
    {
        // c'est bien une tour valide ?
        if (tour == null)
            throw new IllegalArgumentException("Tour nulle");

        // suffisemment d'argent ?
        if(!laTourPeutEtreAchetee(tour))    
            throw new ArgentInsuffisantException("Pose impossible : Pas assez d'argent");
        
        // si elle peut pas etre posee
        if (!laTourPeutEtrePosee(tour))
            throw new ZoneInaccessibleException("Pose impossible : Zone non accessible");

        // si elle bloque le chemin de A vers B
        if (terrain.laTourBloqueraLeChemin(tour))
            throw new CheminBloqueException("Pose impossible : Chemin bloqué");

        // desactive la zone dans le maillage qui correspond a la tour
        terrain.desactiverZone(tour, true);

        // ajout de la tour
        gestionnaireTours.ajouterTour(tour);
        
        // mise a jour du jeu de la tour
        tour.setJeu(this);
        
        // mise en jeu de la tour
        tour.mettreEnJeu();
        
        // debit des pieces d'or
        tour.getPrioprietaire().setNbPiecesDOr(
                tour.getPrioprietaire().getNbPiecesDOr() - tour.getPrixAchat());
    }
    
    
    /**
     * Permet de vendre une tour.
     * 
     * @param tour la tour a vendre
     */
    public void vendreTour(Tour tour)
    {
        // supprime la tour
        gestionnaireTours.supprimerTour(tour);
        
        // debit des pieces d'or
        tour.getPrioprietaire().setNbPiecesDOr(
                tour.getPrioprietaire().getNbPiecesDOr() + tour.getPrixDeVente());
    }
 
    
    /**
     * Permet d'ameliorer une tour.
     * 
     * @param tour la tour a ameliorer
     * @return vrai si operation realisee avec succes, sinon faux 
     * @throws ArgentInsuffisantException si pas assez d'argent 
     * @throws NiveauMaxAtteintException si niveau max de la tour atteint
     */
    public void ameliorerTour(Tour tour) throws NiveauMaxAtteintException, ArgentInsuffisantException
    {
        if(!tour.peutEncoreEtreAmelioree())
            throw new NiveauMaxAtteintException("Amélioration impossible : Niveau max atteint");
        
        if(tour.getPrioprietaire().getNbPiecesDOr() < tour.getPrixAchat())
            throw new ArgentInsuffisantException("Amélioration impossible : Pas assez d'argent");

        // debit des pieces d'or
        tour.getPrioprietaire().setNbPiecesDOr(tour.getPrioprietaire().getNbPiecesDOr() - tour.getPrixAchat());
     
        // amelioration de la tour
        tour.ameliorer();
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
	public void lancerVagueSuivante(Equipe cible)
	{
	    // lancement de la vague
	    VagueDeCreatures vagueCourante = terrain.getVagueDeCreaturesSuivante();
        
	    terrain.passerALaProchaineVague();
	    
        vagueCourante.lancerVague(this, cible, this, this);
	}
	
	/**
     * Permet de savoir si la partie est terminée
     * 
     * @return true si elle l'est false sinon
     */
	public boolean estTermine()
	{
	    return estTermine;
	}
	

    /**
     * Permet de terminer la partie en cours
     */
    public void terminer()
    {
        estTermine = true;
        
        arreterTout();
        
        edj.partieTerminee();
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
            throw new TerrainDejaInitialise("Terrain déjà initialisé");

        equipes = terrain.getEquipesInitiales();
        
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
            throw new NullPointerException("Le terrain ne doit jamais etre nul !");
        
        return terrain;
    }
    
    /**
     * Permet de recuperer le gestionnaire de creatures
     * 
     * @return le gestionnaire de creatures
     */
    public Vector<Creature> getCreatures()
    {
        return gestionnaireCreatures.getCreatures();
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

    /**
     * Permet de recupérer la collection des équipes
     * 
     * @return la collection des équipes
     */
    public ArrayList<Equipe> getEquipes()
    {
        return equipes;
    }

    /**
     * Retourne une collection avec tous les joueurs fesant partie
     * d'une des équipes du jeu.
     * 
     * @return les joueurs
     */
    public ArrayList<Joueur> getJoueurs()
    {
        // création de la collection
        ArrayList<Joueur> joueurs = new ArrayList<Joueur>();
        
        // ajout de tous les joueurs
        for(Equipe e : equipes)
            for(Joueur j : e.getJoueurs())
                joueurs.add(j);
        
        // retour
        return joueurs;
    }
    
    /**
     * Permet d'ajouter un joueur dans le premier emplacement disponible
     * 
     * @param joueur le joueur
     * @throws JeuEnCoursException Si la partie à déjà démarrée
     * @throws AucunePlaceDisponibleException Aucune place disponible dans les équipes.
     */
    public void ajouterJoueur(Joueur joueur) throws JeuEnCoursException, AucunePlaceDisponibleException
    {
        // si la partie est en court
        if(partieDemarree)
            throw new JeuEnCoursException("La partie à déjà démarrée");
        
        // ajout du joueur dans le premier emplacement disponible
        for(int i=0;i<equipes.size();i++)
        {
            try
            {              
                // on tente l'ajout...
                equipes.get(i).ajouterJoueur(joueur);
                
                // notification
                if(edj != null)
                    edj.joueurAjoute(joueur);
                
                return; // équipe trouvée
            }
            catch(IllegalArgumentException iae)
            {
                
            }
        }
        
       throw new AucunePlaceDisponibleException("Aucune place disponible.");
    }

    /**
     * Permet de modifier l'écouteur de jeu
     * 
     * @param edj l'écouteur de jeu
     */
    public void setEcouteurDeJeu(EcouteurDeJeu edj)
    {
        this.edj = edj;
    }
    
    /**
     * Permet de recuperer le joueur principal du jeu
     * 
     * @param joueur le joueur principal du jeu
     */
    public Joueur getJoueurPrincipal()
    { 
        return joueur;
    }
    
    /**
     * Permet de modifier le joueur principal du jeu
     * 
     * @param joueur le joueur principal du jeu
     */
    public void setJoueurPrincipal(Joueur joueur)
    {
        this.joueur = joueur;
    }
    
    @Override
    public void creatureBlessee(Creature creature)
    {
        if(edj != null)
            edj.creatureBlessee(creature);
        
        ajouterAnimation(new TacheDeSang((int)creature.getCenterX(),(int) creature.getCenterY()));
    }

    @Override
    public void creatureTuee(Creature creature)
    {
        // gain de pieces d'or
        joueur.setNbPiecesDOr(joueur.getNbPiecesDOr() + creature.getNbPiecesDOr());
        
        // augmentation du score
        int nbEtoiles = joueur.getNbEtoiles();
        
        joueur.setScore(joueur.getScore() + creature.getNbPiecesDOr());
        
        // nouvelle étoile
        if(nbEtoiles < joueur.getNbEtoiles())
            edj.etoileGagnee();
 
        if(edj != null)
            edj.creatureTuee(creature);
    }

    @Override
    synchronized public void creatureArriveeEnZoneArrivee(Creature creature)
    {
        // si pas encore perdu
        if(!joueur.aPerdu())
        {
            joueur.getEquipe().perdreUneVie();
            
            if(edj != null)
                edj.creatureArriveeEnZoneArrivee(creature);
            
            // le joueur n'a plus de vie
            if(joueur.aPerdu())
                terminer();
        }  
    }

    @Override
    public void vagueEntierementLancee(VagueDeCreatures vagueDeCreatures)
    {
        if(edj != null)
            edj.vagueEntierementLancee(vagueDeCreatures); 
    }
    
    /**
     * Permet de savoir si une tour peut etre posee.
     * 
     * Controle de l'intersection avec les tours.
     * Controle de l'intersection avec les creatures.
     * Controle de l'intersection avec les zones du terrain. (murs et depart / arrive)
     * 
     * @param tour la tour a posee
     * @return true si la tour peut etre posee, false sinon
     */
    public boolean laTourPeutEtrePosee(Tour tour)
    {
        return gestionnaireTours.laTourPeutEtrePosee(tour);
    }
    
    /**
     * Permet de savoir si une tour peut etre achetee.
     * 
     * @param tour la tour a achetee
     * @return true si le joueur a assez de pieces d'or, false sinon
     */
    public boolean laTourPeutEtreAchetee(Tour tour)
    {  
        return gestionnaireTours.laTourPeutEtreAchetee(tour);
    }

    /**
     * Permet de recuperer une copie de la collection des tours
     */
    public Vector<Tour> getTours()
    {
        return gestionnaireTours.getTours();
    }

    public Vector<Creature> getCreaturesQuiIntersectent(int x, int y,
            int rayon)
    {
        return gestionnaireCreatures.getCreaturesQuiIntersectent(x, y, rayon);
    }

    public void ajouterCreature(Creature creature)
    {
        gestionnaireCreatures.ajouterCreature(creature);
        
        edj.creatureAjoutee(creature);
    }

    public void ajouterAnimation(Animation animation)
    {
        gestionnaireAnimations.ajouterAnimation(animation);
        
        edj.animationAjoutee(animation);
    }

    public void dessinerAnimations(Graphics2D g2, int hauteur)
    {
        gestionnaireAnimations.dessinerAnimations(g2,hauteur);
    }
}
