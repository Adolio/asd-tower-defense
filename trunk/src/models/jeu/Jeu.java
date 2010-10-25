package models.jeu;

import i18n.Langue;

import java.awt.Graphics2D;
import java.util.*;
import outils.myTimer;
import exceptions.*;
import models.animations.*;
import models.creatures.*;
import models.joueurs.*;
import models.terrains.*;
import models.tours.*;

/**
 * C'est la classe principale du jeu (Moteur)
 * 
 * Elle encapsule tous les éléments du jeu.
 * 
 * Elle utilise des gestionnaires pour gérer ces éléments
 * 
 * @author Aurelien Da Campo
 * @version 2.1 | mai 2010
 * @since jdk1.6.0_16
 * @see Terrain
 * @see GestionnaireTours
 * @see GestionnaireCreatures
 * @see GestionnaireAnimations
 */
public abstract class Jeu implements EcouteurDeJoueur,
                                     EcouteurDeCreature, 
                                     EcouteurDeVague
{
	/**
	 * version du jeu
	 */
    private static final String VERSION 
        = "ASD - Tower Defense v2.0 (beta 4) | november 2010 | heig-vd";
    
    /**
     * Mode de positionnement centré des créatures dans la zone de départ
     */
    public static final int MODE_POSITIONNNEMENT_CENTRE = 0;
    
    /**
     * Mode de positionnement aléatoire des créatures dans la zone de départ.
     */
    public static final int MODE_POSITIONNNEMENT_ALETOIRE = 1;
    
    /**
     * Mode de positionnement courant des créatures dans la zone de départ
     */
    private static final int MODE_DE_POSITIONNEMENT = MODE_POSITIONNNEMENT_ALETOIRE;

    /**
     * Etape d'incrémentation du coefficient de vitesse du jeu.
     */
    private static final double ETAPE_COEFF_VITESSE = 0.5;

    /**
     * Coefficient maximal de la vitesse du jeu
     */
    private static final double MAX_COEFF_VITESSE = 5.0;
    
    /**
     * Coefficient minimal de la vitesse du jeu
     */
    private static final double MIN_COEFF_VITESSE = 0.1; // /!\ >0 /!\
      
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
	protected ArrayList<Equipe> equipes = new ArrayList<Equipe>();
	
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
     * Gestion des vagues de creatures. C'est le joueur que decident le moment
     * ou il veut lancer une vague de creatures. Une fois que toutes les vagues
     * de creatures ont ete detruites, le jeu est considere comme termine.
     */
    protected int indiceVagueCourante = 1;
	
    /**
     * Stockage de la vagues courante
     */
    VagueDeCreatures vagueCourante;
    
    
    /**
     * Permet de savoir si la partie est initialisée
     */
    protected boolean estInitialise;
    
    /**
     * Permet de savoir si la partie est à été démarrée
     */
    private boolean estDemarre;
    
    /**
     * Permet de savoir si la partie est terminée
     */
    protected boolean estTermine;
    
    /**
     * Permet de savoir si la partie est détruite
     */
    protected boolean estDetruit;
    
    /**
     * Pour notifications (observable)
     */
    protected EcouteurDeJeu edj;
    
    /**
     * Joueur principal  
     */
    protected Joueur joueur;

    /**
     * Timer pour gérer le temps de jeu
     */
    protected myTimer timer = new myTimer(1000,null);

    /**
     * Coefficient de vitesse de déroulement de la partie.
     * 
     * Permet de résoudre les problèmes de lenteur du jeu.
     */
    private double coeffVitesse;

    /**
     * Constructeur
     */
    public Jeu()
    {
        gestionnaireTours      = new GestionnaireTours(this);
        gestionnaireCreatures  = new GestionnaireCreatures(this);
        gestionnaireAnimations = new GestionnaireAnimations(this);
    }
    
    /**
     * Permet d'initialiser la partie avant le commencement
     * 
     * @param joueur 
     */
    synchronized public void initialiser()
    {
        if(terrain == null)
            throw new IllegalStateException("Terrain nul");
        
        if(equipes.size() == 0)
            throw new IllegalStateException("Aucune équipe inscrite");
        
        // le joueur principal
        //if(joueur != null)
        //    setJoueurPrincipal(joueur);
        
        // attributs
        indiceVagueCourante = 1;
        enPause             = false;
        estDemarre          = false;
        estTermine          = false;
        estDetruit          = false;
        vagueCourante       = null;
        coeffVitesse        = 1.0;
        
        // initialisation des valeurs par defaut
        for(Equipe equipe : equipes)
        {
            // initialisation des vies restantes
            equipe.setNbViesRestantes(terrain.getNbViesInitiales());
            
            // initialisation des pieces d'or des joueurs
            for(Joueur j : equipe.getJoueurs())
            {
                j.setScore(0);
                j.setNbPiecesDOr(terrain.getNbPiecesOrInitiales());
            }
        }  
        
        estInitialise = true;
        
        if(edj != null)
            edj.partieInitialisee();
    }
    
    public void reinitialiser()
    {
        terrain.arreterMusiqueDAmbiance();
        
        // réinitialisation du terrain
        terrain.reinitialiser();
        
        estInitialise = false;
        estDemarre = false;
        
        initialiser();
        
        
        // arret des gestionnaires
        gestionnaireTours.arreterTours();
        gestionnaireCreatures.arreterCreatures();
        gestionnaireAnimations.arreterAnimations();
        
        gestionnaireTours = new GestionnaireTours(this);
        gestionnaireCreatures = new GestionnaireCreatures(this);
        gestionnaireAnimations = new GestionnaireAnimations(this);
        
        
        
        // ajout de tous les joueurs
        for(Equipe e : equipes)
        {
            e.setNbViesRestantes(terrain.getNbViesInitiales());
            
            for(Joueur j : e.getJoueurs())    
            {
                j.setNbPiecesDOr(terrain.getNbPiecesOrInitiales());
                j.setScore(0);
            }
        }
        
        // arret du timer
        timer.stop();
        timer = new myTimer(1000,null);
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
            
        if(estDemarre)
            throw new IllegalStateException("Le jeu est déjà démarré");
        
        // demarrage des gestionnaires
        gestionnaireTours.demarrer();
        gestionnaireCreatures.demarrer();
        gestionnaireAnimations.demarrer();
        
        timer.start();
        
        estDemarre = true;
        
        // notification
        if(edj != null)
            edj.partieDemarree();
    }
    
    /**
     * Indique au jeu qu'une vague veut etre lancée
     * 
     * @param vague la vague
     * @throws ArgentInsuffisantException 
     */
    public void lancerVague(Joueur joueur, Equipe cible, VagueDeCreatures vague) throws ArgentInsuffisantException
    { 
        gestionnaireCreatures.lancerVague(vague, joueur, cible, this, this);
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
            throw new ArgentInsuffisantException(Langue.getTexte(Langue.ID_ERROR_POSE_IMPOSSIBLE_PAS_ASSEZ_D_ARGENT));
        
        // si elle peut pas etre posee
        if (!laTourPeutEtrePosee(tour))
            throw new ZoneInaccessibleException(Langue.getTexte(Langue.ID_ERROR_POSE_IMPOSSIBLE_ZONE_INACCESSIBLE));

        // si elle bloque le chemin de A vers B
        if (terrain.laTourBloqueraLeChemin(tour))
            throw new CheminBloqueException(Langue.getTexte(Langue.ID_ERROR_POSE_IMPOSSIBLE_CHEMIN_BLOQUE));
        
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
    
       
        //ajouterAnimation(new Fumee((int)tour.getCenterX(),(int)tour.getCenterY()));
        
        if(edj != null)
            edj.tourPosee(tour);
    }
    
    
    /**
     * Permet de vendre une tour.
     * 
     * @param tour la tour a vendre
     * @throws ActionNonAutoriseeException 
     */
    public void vendreTour(Tour tour) throws ActionNonAutoriseeException
    {
        // supprime la tour
        gestionnaireTours.supprimerTour(tour);
        
        // debit des pieces d'or
        tour.getPrioprietaire().setNbPiecesDOr(
                tour.getPrioprietaire().getNbPiecesDOr() + tour.getPrixDeVente());
    
        ajouterAnimation(new Fumee((int)tour.getCenterX(),(int)tour.getCenterY()));
        
        if(edj != null)
            edj.tourVendue(tour);
    }
 
    
    /**
     * Permet d'ameliorer une tour.
     * 
     * @param tour la tour a ameliorer
     * @return vrai si operation realisee avec succes, sinon faux 
     * @throws ArgentInsuffisantException si pas assez d'argent 
     * @throws NiveauMaxAtteintException si niveau max de la tour atteint
     * @throws ActionNonAutoriseeException 
     * @throws JoueurHorsJeu 
     */
    public void ameliorerTour(Tour tour) throws NiveauMaxAtteintException, ArgentInsuffisantException, ActionNonAutoriseeException, JoueurHorsJeu
    {
        if(!tour.peutEncoreEtreAmelioree())
            throw new NiveauMaxAtteintException(Langue.getTexte(Langue.ID_ERROR_AMELIORATON_IMPOSSIBLE_NIVEAU_MAX_ATTEINT));
        
        if(tour.getPrioprietaire().getNbPiecesDOr() < tour.getPrixAchat())
            throw new ArgentInsuffisantException(Langue.getTexte(Langue.ID_ERROR_AMELIORATON_IMPOSSIBLE_PAS_ASSEZ_D_ARGENT));

        // debit des pieces d'or
        tour.getPrioprietaire().setNbPiecesDOr(tour.getPrioprietaire().getNbPiecesDOr() - tour.getPrixAchat());
     
        // amelioration de la tour
        tour.ameliorer();
        
        if(edj != null)
            edj.tourAmelioree(tour);
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
	public void lancerVagueSuivante(Joueur joueur, Equipe cible)
	{
	    // lancement de la vague
	    VagueDeCreatures vagueCourante = terrain.getVagueDeCreatures(indiceVagueCourante);
        
	    passerALaProchaineVague();
	    
	    gestionnaireCreatures.lancerVague(vagueCourante, joueur, cible, this, this);
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
        if(!estTermine)
        {
            estTermine = true;
            
            arreterTout();
              
            Equipe equipeGagnante = null;
            int maxScore = -1;
            
            // FIXME gestion des égalités !

            // selection des equipes en jeu
            ArrayList<Equipe> equipesEnJeu = new ArrayList<Equipe>();
            for(Equipe equipe : equipes)
                if(!equipe.aPerdu())
                {
                    // selection de l'equipe gagnante
                    if(equipe.getScore() > maxScore)
                    {
                        equipeGagnante = equipe;
                        maxScore = equipe.getScore();
                    }
                    
                    equipesEnJeu.add(equipe);
                }
            
            if(edj != null)
                edj.partieTerminee(new ResultatJeu(equipeGagnante)); // TODO check
        }
    }

    /**
     * Permet d'initialiser le terrain de jeu
     * 
     * @param terrain le terrain
     * @throws IllegalArgumentException si le terrain à déjà été initialisé.
     */
    public void setTerrain(Terrain terrain) throws IllegalArgumentException
    {
        // J'ai mis en commentaire! pour le chargement dans l'editeur de niveau
        //if(this.terrain != null) 
        //    throw new TerrainDejaInitialise("Terrain déjà initialisé");

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
    protected void arreterTout()
    {
        // arret de toutes les tours
        gestionnaireTours.arreterTours();

        // arret de toutes les creatures
        gestionnaireCreatures.arreterCreatures();

        // arret de toutes les animations
        gestionnaireAnimations.arreterAnimations();
        
        // arret du timer
        timer.stop();
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
            timer.play();
        }
        else
        {
            gestionnaireTours.mettreEnPause();
            gestionnaireCreatures.mettreEnPause();
            gestionnaireAnimations.mettreEnPause();
            timer.pause();
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
        if(estDemarre)
            throw new JeuEnCoursException("La partie à déjà démarrée");
        
        // ajout du joueur dans le premier emplacement disponible
        for(int i=0;i<equipes.size();i++)
        {
            try
            {              
                // on tente l'ajout...
                equipes.get(i).ajouterJoueur(joueur);
                
                // ajout de l'ecouteur
                joueur.setEcouteurDeJoueur(this);
                
                // notification
                if(edj != null)
                    edj.joueurAjoute(joueur);
  
                return; // équipe trouvée
            }
            catch (AucunePlaceDisponibleException e)
            {
                // on essaye encore...
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
        
        // mis à jour de l'écouteur
        joueur.setEcouteurDeJoueur(this);
    }
    
    @Override
    public void creatureBlessee(Creature creature)
    {
        if(edj != null)
            edj.creatureBlessee(creature);
        
        ajouterAnimation(new TacheDeSang((int)creature.getCenterX(),(int) creature.getCenterY()));
    }

    @Override
    synchronized public void creatureTuee(Creature creature, Joueur tueur)
    {
        // gain de pieces d'or
        tueur.setNbPiecesDOr(tueur.getNbPiecesDOr() + creature.getNbPiecesDOr());
        
        // nombre d'etoile avant l'ajout du score
        int nbEtoilesAvantAjoutScore = tueur.getNbEtoiles();
        
        // augmentation du score
        tueur.setScore(tueur.getScore() + creature.getNbPiecesDOr());

        // nouvelle étoile ?
        if(nbEtoilesAvantAjoutScore < tueur.getNbEtoiles())
            if(edj != null)  
                edj.etoileGagnee();
 
        // notification de la mort de la créature
        if(edj != null)
            edj.creatureTuee(creature,tueur);
    }

    @Override
    synchronized public void creatureArriveeEnZoneArrivee(Creature creature)
    {
        Equipe equipe = creature.getEquipeCiblee();
        
        // si pas encore perdu
        if(!equipe.aPerdu())
        {
            equipe.perdreUneVie();
            
            if(edj != null)
            {
                edj.creatureArriveeEnZoneArrivee(creature);
                
                // FIXME IMPORTANT faire plutot une mise a jour des donnees de l'equipe 
                // -> ajout au protocole EQUIPE_ETAT
                // et appeler plutot edj.equipeMiseAJour(equipe) 
                // pour tous les joueurs de l'equipe
                for(Joueur joueur : equipe.getJoueurs())
                    edj.joueurMisAJour(joueur);
            }
            
            // controle de la terminaison du jeu.
            if(equipe.aPerdu())
            {
                if(edj != null)
                    edj.equipeAPerdue(equipe);
                
                // s'il il reste au moins deux equipes en jeu
                // la partie n'est pas terminée
                int nbEquipesRestantes = 0;
                for(Equipe tmpEquipe : equipes)
                    if(!tmpEquipe.aPerdu())
                        nbEquipesRestantes++;
             
                // fin de la partie
                if(nbEquipesRestantes <= 1)
                    terminer();
            }
        } 
    }

    @Override
    public void vagueEntierementLancee(VagueDeCreatures vagueDeCreatures)
    {
        if(edj != null)
            edj.vagueEntierementLancee(vagueDeCreatures); 
    }
    
    @Override
    public void joueurMisAJour(Joueur joueur)
    {
        if(edj != null)
            edj.joueurMisAJour(joueur);
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

    /**
     * Permet de récupérer les créatures qui intersectent un cercle
     *  
     * @param centerX centre x du cercle
     * @param centreY centre y du cercle
     * @param rayon rayon du cercle
     * @return Une collection de créatures
     */
    public Vector<Creature> getCreaturesQuiIntersectent(int centerX, int centreY,
            int rayon)
    {
        return gestionnaireCreatures.getCreaturesQuiIntersectent(centerX, centreY, rayon);
    }

    /**
     * Permet de nofifier le jeu qu'un créature à été ajoutée
     * @param creature
     */
    public void creatureAjoutee(Creature creature)
    {
        if(edj != null)
            edj.creatureAjoutee(creature);
    }

    /**
     * Permet d'ajouter une animation
     * 
     * @param animation l'animation à ajouter
     */
    public void ajouterAnimation(Animation animation)
    {
        gestionnaireAnimations.ajouterAnimation(animation);
        
        if(edj != null)
            edj.animationAjoutee(animation);
    }

    /**
     * Permet de dessiner toutes les animations
     * 
     * @param g2 le Graphics2D
     * @param hauteur la hauteur des animations
     * @see Animation.HAUTEUR_SOL
     * @see Animation.HAUTEUR_AIR
     */
    public void dessinerAnimations(Graphics2D g2, int hauteur)
    {
        gestionnaireAnimations.dessinerAnimations(g2,hauteur);
    }
    
    /**
     * Permet de recuperer un joueur grace a son identificateur
     * 
     * @param idJoueur identificateur du joueur
     * 
     * @return le joueur ou null
     */ 
    public Joueur getJoueur(int idJoueur)
    {
        ArrayList<Joueur> joueurs = getJoueurs();

        for(Joueur joueur : joueurs)
            if(joueur.getId() == idJoueur)
                return joueur;
        
        return null;
    }

    /**
     * Permet de recuperer une équipe grace a son identificateur
     * 
     * @param idEquipe identificateur de l'équipe
     * 
     * @return l'équipe ou null
     */
    public Equipe getEquipe(int idEquipe)
    {
        for(Equipe equipe : equipes)
            if(equipe.getId() == idEquipe)
                return equipe;
        
        return null;
    }
    
    /**
     * Permet de recuperer un emplacement grace a son identificateur
     * 
     * @param idEmplacement identificateur de l'emplacement
     * 
     * @return l'emplacement ou null
     */
    public EmplacementJoueur getEmplacementJoueur(int idEmplacement)
    {
        for(Equipe equipe : equipes)
            for(EmplacementJoueur ej : equipe.getEmplacementsJoueur())
                if(ej.getId() == idEmplacement)
                    return ej;

        return null;
    }
    
    /**
     * Permet de recuperer une tour à l'aide de son identificateur
     * 
     * @param idTour l'identificateur de la tour
     * @return la tour trouvée ou null
     */
    public Tour getTour(int idTour)
    {
        for (Tour tour : getTours())
            if (tour.getId() == idTour)
                return tour;
        
        return null;
    }
    
    /**
     * Permet de recuperer une créature grace a son identificateur
     * 
     * @param idCreature identificateur de la créature
     * 
     * @return la créature ou null
     */
    public Creature getCreature(int idCreature)
    {
        return gestionnaireCreatures.getCreature(idCreature);
    }

    /**
     * Permet de recuperer l'equipe valide suivante 
     * (qui contient au moins un joueur)
     * 
     * Les équipes qui ont perdue ne sont pas prises en compte.
     * 
     * S'il n'y a pas d'équipe valide suivante, l'équipe en paramètre 
     * est retournée.
     * 
     * @param equipe l'équipe suivante de quelle équipe ?
     * @return l'équipe suivant qui peut-être la même équipe (si seule)
     */
    public Equipe getEquipeSuivanteNonVide(Equipe equipe)
    {
        // on trouve l'equipe directement suivante
        int i = (equipes.indexOf(equipe)+1) % equipes.size();
        
        // tant qu'il n'y a pas de joueur ou que l'équipe a perdue
        // on prend la suivante...
        // au pire on retombera sur la même equipe qu'en argument
        while(equipes.get(i).getJoueurs().size() == 0 || equipes.get(i).aPerdu())
            i = ++i % equipes.size();
        
        return equipes.get(i);
    }

    /**
     * Permet de savoir si le jeu a été initialisé
     * 
     * @return true s'il l'est false sinon
     */
    public boolean estInitialise()
    {
        return estInitialise;
    }
    
    /**
     * Permet de savoir si le jeu a été démarré
     * 
     * @return true s'il l'est false sinon
     */
    public boolean estDemarre()
    {
        return estDemarre;
    }

    /**
     * Permet de recuperer le numero de la vague courante
     * 
     * @return le numero de la vague courante
     */
    public int getNumVagueCourante()
    {
        return indiceVagueCourante;
    }
    
    /**
     * Permet de passer à la prochaine vague
     */
    public void passerALaProchaineVague()
    {
        ++indiceVagueCourante;
    }

    /**
     * Permet de recuperer le timer
     * 
     * @return
     */
    public myTimer getTimer()
    {
        return timer;
    }

    /**
     * Permet de détruire le jeu.
     * 
     * Détruit tous les objets du jeu.
     */
    public void detruire()
    {
        estDetruit = true;
        
        gestionnaireCreatures.detruire();
        gestionnaireTours.detruire();
        gestionnaireAnimations.detruire();
    }
    
    /**
     * Permet de savoir si le jeu a été détruit
     * 
     * @return true s'il l'est false sinon
     */
    public boolean estDetruit()
    {
        return estDetruit;
    }

    /**
     * Permet de récupérer le coefficient de vitesse du jeu
     * 
     * @return le coefficient de vitesse du jeu
     */
    public double getCoeffVitesse()
    {
        return coeffVitesse;
    }

    /**
     * Permet d'augmenter le coefficient de vitesse du jeu.
     * 
     * @return retour la nouvelle valeur du coefficient
     */
    public synchronized double augmenterCoeffVitesse()
    {
        if(coeffVitesse + ETAPE_COEFF_VITESSE <= MAX_COEFF_VITESSE)
        {    
            coeffVitesse += ETAPE_COEFF_VITESSE;
            
            if(edj != null)
                edj.coeffVitesseModifie(coeffVitesse);
        }
        
        return coeffVitesse;
    }

    /**
     * Permet de diminuer le coefficient de vitesse du jeu.
     * 
     * @return retour la nouvelle valeur du coefficient
     */
    synchronized public double diminuerCoeffVitesse()
    {
        if(coeffVitesse - ETAPE_COEFF_VITESSE >= MIN_COEFF_VITESSE)
        { 
            coeffVitesse -= ETAPE_COEFF_VITESSE;
         
            if(edj != null)
                edj.coeffVitesseModifie(coeffVitesse);
        }    
        return coeffVitesse;
    }
    
    /**
     * Permet de modifier directement coefficient de vitesse du jeu.
     * @param value le nouveau coefficient de vitesse du jeu.
     */
    public void setCoeffVitesse(double value)
    {
        if(coeffVitesse - ETAPE_COEFF_VITESSE < MIN_COEFF_VITESSE
        && coeffVitesse + ETAPE_COEFF_VITESSE > MAX_COEFF_VITESSE)
            throw new IllegalArgumentException(
                    "Le coefficient de vitesse non authorisé");
            
        coeffVitesse = value;
        
        if(edj != null)
            edj.coeffVitesseModifie(coeffVitesse);
    }
    
    /**
     * Permet de récupérer le type de positionnement des créatures dans 
     * la zone de départ
     * 
     * @return le type de positionnement des créatures dans la zone de départ
     */
    public int getModeDePositionnnementDesCreatures()
    {
        return MODE_DE_POSITIONNEMENT;
    }
    
    /**
     * Permet d'ajouter une equipe (utilisé pour l'éditeur de terrain)
     * 
     * @param equipe la nouvelle equipe
     */
    public void ajouterEquipe(Equipe equipe)
    {
        equipes.add(equipe);
    }
 
    /**
     * Permet de supprimer une equipe (utilisé pour l'éditeur de terrain)
     * 
     * @param equipe l'équipe à supprimer
     */
    public void supprimerEquipe(Equipe equipe)
    {
        equipes.remove(equipe);
    }
}
