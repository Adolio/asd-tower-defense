package reseau.jeu.serveur;

/**
 * Cet interface contient l'ensemble des constantes necessaires au protocole de
 * dialogue entre le client et le serveur de jeu.
 * 
 * @author Da Campo Aurélien
 * @author Pierre-Dominique Putallaz
 * @author Romain Poulain
 */
public interface ConstantesServeurJeu
{
    
    // COMMUNICATION 0-99
    
    /**
     * Message à destination de tous les joueurs
     */
    public final int A_TOUS = -1;
    
    /**
     * Message en provenance du serveur de jeu
     */
    public final int DU_SERVEUR = 2;
    
    
    // PARTIE 100-199
    
    /**
	 * Démarrage de la partie
	 */
	public final int PARTIE_INITIALISEE = 100;
	
	/**
     * Démarrage de la partie
     */
    public final int PARTIE_LANCEE = 101;
	
	/**
	 * Arrêt de la partie
	 */
	public final int PARTIE_FIN = 102;
	
    /**
     * Code pour quitter la partie
     */
    public final int PARTIE_QUITTER = 103;
	
	/**
     * Type de message : état de la partie
     */
    public final int PARTIE_ETAT = 104;
    
    /**
     * Changement d'état de la partie : en pause
     */
    public final int EN_PAUSE = 105;
    
    /**
     * Changement d'état de la partie : en jeu
     */
    public final int EN_JEU = 106;

    /**
     * Changement d'état de la partie : en jeu
     */
    public final int PARTIE_TERMINEE = 107;
    
    /**
     * Code pour quitter la partie
     */
    public final int PARTIE_STOPPEE = 108;
    
    
	// SUCCES 200-299
	
	/**
	 * Code de succès
	 */
	public final int OK = 200;
	
	
	// JOUEUR & EQUIPE 300-309
	
	/**
     * Type de message : initialisation d'un joueur
     */
    public final int JOUEUR_INITIALISATION = 300;

    /**
     * Type de message : ajout d'un joueur
     */
    public final int JOUEUR_CHANGER_EQUIPE = 302;
    
	/**
	 * Type de message : état d'un joueur
	 */
	public final int JOUEUR_ETAT = 303;
	
    /**
     * Type de message : un message texte
     */
    public final int JOUEURS_ETAT = 304;
    
    /**
     * Type de message : un message texte
     */
    public final int JOUEUR_PRET = 305;
    
    /**
     * Type de message : un message texte
     */
    public final int JOUEUR_DECONNEXION = 306;
    
	/**
     * Type de message : un message texte
     */
    public final int MSG = 309;
	
    
	// CREATURE 310-319
	
	/**
	 * Type de message : ajout d'une créature
	 */
	public final int CREATURE_AJOUT = 310;
	
	/**
     * Type de message : etat d'une créature
     */
    public final int CREATURE_ETAT = 311;
	
	/**
     * Type de message : suppression d'une créature
     */
    public final int CREATURE_SUPPRESSION = 312;
	
    /**
     * Type de message : créature est arrivé
     */
    public final int CREATURE_ARRIVEE = 313;
    
    /**
     * Type de message : vague de création
     */
    public final int VAGUE = 314;
    
	
   
	// TOUR 320-329
	
	/**
     * Type de message : nouvelle tour
     */
    public final int TOUR_AJOUT = 320;
    
    /**
     * Type de message : suppression d'une tour
     */
    public final int TOUR_SUPRESSION = 321;
    
    /**
     * Type de message : amélioration d'une tour
     */
    public final int TOUR_AMELIORATION = 322;
    
    /**
     * Type de message : vente tour
     */
    public final int TOUR_VENTE = 323;
	
	
	// ANIMATION 330 - 340
    
	/**
	 * Type de message : état d'une animation
	 */
	public final int ANIMATION_AJOUT = 330;
	

	
	
	// AUTRES 350 - 399
	
	/**
	 * Type de message : un objet
	 */
	public final int OBJET = 350;
	
	

	// ERREURS 400-499
	
	/**
	 * Code d'erreur : pas assez d'argent
	 */
	public final int ARGENT_INSUFFISANT = 400; //ou PAUVRE, a choix :P
	
	/**
	 * Code d'erreur : mauvaise position de l'objet
	 */
	public final int ZONE_INACCESSIBLE = 401;
	
	/**
	 * Code d'erreur : chemin bloqué
	 */
	public final int CHEMIN_BLOQUE = 402;

	/**
     * Code d'erreur : Niveau max de la tour atteint
     */
    public final int NIVEAU_MAX_ATTEINT = 403;
	
    /**
     * Code d'erreur : action non autorisee
     */
    public final int ACTION_NON_AUTORISEE = 404;
    
    /**
     * Code d'erreur : pas de place dans l'equipe
     */
    public final int PAS_DE_PLACE = 405;
    
    /**
     * Code d'erreur : jeu déjà en cours
     */
    public final int JEU_EN_COURS = 406;
    
    /**
     * Code d'erreur : tour inconnue
     */
    public final int TOUR_INCONNUE = 407;
    
    /**
     * Code d'erreur : joueur inconnu
     */
    public final int JOUEUR_INCONNU = 408;
    
	/**
	 * Code d'erreur : erreur quelconque
	 */
	public final int ERREUR = 450;
}
