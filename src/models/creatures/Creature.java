package models.creatures;

import java.awt.*;
import java.util.*;
import models.joueurs.*;
import models.tours.Tour;

/**
 * Classe de gestion d'une creature.
 * <p>
 * Les creatures sont des bestioles qui attaque le joueur. L'objectif de celles-ci
 * est simple : ce rendre le plus vite possible (chemin le plus court) d'une zone
 * A a un zone B. Si la creature arrive a survivre jusqu'a la zone B, le joueur 
 * perdra une de ses precieuses vies.
 * <p>
 * Il existe deux types de creatures, les volantes et les terriennes. Les volantes
 * ne sont pas affecter par les murs et l'emplacement des tours. Elle volent 
 * simplement de la zone A a la zone B sans suivre un chemin particulier.
 * 
 * @author Aurélien Da Campo
 * @version 1.2 | mai 2010
 * @since jdk1.6.0_16
 */
public abstract class Creature extends Rectangle
{
	private static final long serialVersionUID = 1L;
	
	
	/**
     * Identificateur unique de la créature
     */
    private int id;
    private static int idCourant = 0;
	
	
	/**
	 * Permet de stocker sous la forme reelle la position de la creature pour rendre 
	 * fluide les mouvements
	 * 
	 * Notons que Rectangle nous fourni egalement des positions 
	 * mais elles sont entieres
	 */
	private double xReel, yReel;
	
	/**
	 * definition des deux types de creature
	 */
	public static final int TYPE_TERRIENNE 	= 0;
	public static final int TYPE_AERIENNE 	= 1;


	/**
	 * Temps avant la suppression de la créature si aucune nouvelle n'est recue.
	 * 
	 * Valable pour les clients réseau uniquement.
	 */
    private static final long TEMPS_AVANT_SUPPRESSION_SI_AUCUNE_MAJ = 3000;

	/**
	 * nom de la creature
	 */
    private final String NOM;
    
    /**
     * type de la creature
     */
	private final int TYPE;
	
	/**
	 * chemin actuel de la creature
	 */
	private ArrayList<Point> chemin;
	
	/**
     * position actuelle sur le chemin (toujours > 0)
     */
	private int indiceCourantChemin;
	
	/**
	 * sante de la creature, si la sante est <= 0, la creature est morte. 
	 * A ce moment la, elle donne au joueur ses pieces d'or
	 */
	private long sante;
	
	/**
	 * sante maximale de la creature. Utilise pour calculer le pourcentage de 
	 * vie restante de la creature.
	 */
	private long santeMax;
	
	/**
	 * le nombre de pieces d'or que la creature fourni au joueur apres ca mort
	 */
	private int nbPiecesDOr;
	
	/**
     * le prix de la créature
     */
    // TODO private int prixAchat;
	
	/**
	 * Image actuelle de la creature
	 */
	protected Image image;
	
	/**
	 * vitesse de deplacement de la creature sur le terrain en pixel(s) par seconde
	 */
	protected double vitesseNormale; // en pixel(s) / seconde
	
	/**
     * ralentissement de deplacement de la creature sur le terrain
     */
    protected double coeffRalentissement; // 1.0 = 100%

	/**
	 * permet d'informer d'autres entites du programme lorsque la creature
	 * subie des modifications.
	 */
	private ArrayList<EcouteurDeCreature> ecouteursDeCreature;
	
	/**
	 * Permet de savoir s'il faut detruire l'animation
	 */
	private boolean aDetruire;
	
	/**
	 * Il s'agit de la cible a attaquer
	 */
	private Equipe equipeCiblee;

	/**
	 * Stockage de l'angle entre la créature et son prochain noeud
	 */
    private double angle = 0.0;
	
    /**
     * Utiliser pour les clients réseau.
     * 
     * Il ne faut pas que les animations (gérées en local) puissent 
     * blesser les créatures. Donc une maniere rapide de faire 
     * ceci est de rendre les créatures invincibles pour ne pas 
     * la détruire chez le client.
     */
    private boolean invincible = false;

    
	/**
	 * Constructeur de la creature.
	 * 
	 * @param x la position sur l'axe X de la creature
	 * @param y la position sur l'axe Y de la creature
	 * @param largeur la largeur de la creature
	 * @param hauteur la hauteur de la creature
	 * @param santeMax la sante maximale de la creature
	 * @param nbPiecesDOr le nombre de pieces de la creature
	 * @param vitesse vitesse de deplacement de la creatures
	 * @param type type de creature
	 * @param image image de la creature sur le terrain
	 * @param nom nom de l'espece de creature
	 */
	public Creature(int x, int y, int largeur, int hauteur, 
					long santeMax, int nbPiecesDOr, double vitesse, 
					int type, Image image, String nom)
	{
		super(x,y,largeur,hauteur);
		
		xReel = x;
		yReel = y;
		
		this.id             = ++idCourant;
		this.nbPiecesDOr 	= nbPiecesDOr;
		this.santeMax		= santeMax;
		sante 				= santeMax;
		this.vitesseNormale = vitesse;
		ecouteursDeCreature = new ArrayList<EcouteurDeCreature>();
		this.image 			= image;
		TYPE                = type;
		NOM                 = nom;
	}

	/**
	 * Force les fils de la classe a gerer la copie de la creature.
	 * <p>
	 * Note : cette methode est utilisee lors de la creation d'une vague
	 * de creatures. Au lieu de stocker toutes les creatures de la vague, 
	 * on creer une seule instance de la creature et on la duplique le nombre de
	 * fois souhaite.
	 * 
	 * @return la copie de la creature.
	 */
	abstract public Creature copier();
	
	/**
	 * Permet de recuperer le chemin actuellement suivi par la creature.
	 * @return le chemin actuellement suivi par la creature
	 */
	public ArrayList<Point> getChemin()
	{
		return chemin;
	}

	/**
     * Permet de récupérer l'identificateur de la créature
     * @return l'identifiacteur de la créature
     */
    public int getId()
    {
        return id;
    }
	
	/**
	 * Permet de recuperer le type de la creature.
	 * 
	 * @return le type de la creature
	 */
	public int getType()
	{
		return TYPE;
	}
	
	/**
	 * Permet de recuperer la sante de la creature.
	 * 
	 * @return la sante de la creature
	 */
	public long getSante()
	{
		return sante;
	}

	/**
	 * Permet de recuperer la sante maximale de la creature.
	 * 
	 * @return la sante maximale de la creature
	 */
	public long getSanteMax()
	{
		return santeMax;
	}
	
	/**
	 * Permet de recuperer le nombre de pieces d'or de la creature.
	 * 
	 * @return le nombre de pieces d'or de la creature
	 */
	public int getNbPiecesDOr()
	{
		return nbPiecesDOr;
	}
	
	/**
	 * Permet de recuperer la vitesse normale (sans ralentissement) 
	 * de la creature
	 * 
	 * @return la vitesse de la creature
	 */
	public double getVitesseNormale()
	{
		return vitesseNormale;
	}
	
	/**
     * Permet de recuperer la vitesse reelle de la creature (avec ralentissement)
     * 
     * @return la vitesse reelle de la creature
     */
    public double getVitesseReelle()
    {
        return vitesseNormale - vitesseNormale * coeffRalentissement;
    }
    
    /**
     * Permet de recuperer le coefficient de ralentissement
     * @return
     */
    public double getCoeffRalentissement()
    {
        return coeffRalentissement;
    }
    
    /**
     * Permet de rendre une créature invincible ou non
     * 
     * @param invincible l'état
     */
    public void setInvincible(boolean invincible)
    {
        this.invincible = invincible;
    }
    
    /**
     * Permet de modifier le coefficient de ralentissement
     * @param coeffRalentissement le nouveau coefficient de ralentissement
     */
    public void setCoeffRalentissement(double coeffRalentissement)
    {
        if(coeffRalentissement > 1.0)
            coeffRalentissement = 1.0;
        else if(coeffRalentissement < 0.0)
            coeffRalentissement = 0.0;
        else  
            this.coeffRalentissement = coeffRalentissement;
    }
    
	
	/**
	 * Permet de recuperer l'image actuelle de la creature
	 * 
	 * @return l'image actuelle de la creature
	 */
	public Image getImage()
	{
		return image;
	}
	
	/**
     * Permet de recuperer le type de la creature sous forme textuelle
     * 
     * @return le type de la creature sous forme textuelle
     */
    public String getNomType()
    {
        if(TYPE == TYPE_TERRIENNE)
            return "Terrienne";
        else
            return "Aerienne";
    }

    /**
     * Permet de recuperer le nom de la creature
     * @return le nom de la creature
     */
    public String getNom()
    {
        return NOM;
    }
	
    /**
     * Permet de recuperer l'angle entre la creature et le prochain noeud
     * [angle de déplcement]
     * 
     * @return l'angle entre la creature et le prochain noeud
     */
    public double getAngle()
    {
        return angle;
    }
    
	/**
	 * Permet de recuperer la position sur l'axe X de la creature
	 * 
	 * @param x la position sur l'axe X de la creature
	 */
	public void setX(int x)
	{
		this.x = x;
	}
	
	/**
	 * Permet de recuperer la position sur l'axe Y de la creature
	 * 
	 * @param x la position sur l'axe Y de la creature
	 */
	public void setY(int y)
	{
		this.y = y;
	}

	/**
	 * Permet de modifier le chemin actuel de la creature
	 * 
	 * @param chemin le nouveau chemin
	 */
	public void setChemin(ArrayList<Point> chemin)
	{
	    // on est deja au point 0, on ne vas donc pas y aller...
        // (i) corrige un petit bug de retour en arriere.
        indiceCourantChemin = 1; 
	    
	    this.chemin = chemin;
	}
	
	/**
	 * Permet de recuperer l'indice du point courant sur le chemin
	 * 
	 * @return l'indice du point courant sur le chemin
	 */
	public int getIndiceCourantChemin()
	{
	    return indiceCourantChemin;
	}
	
	/**
	 * Cette methode est appelee pour dire a la creature d'effectuee des actions
	 * 
	 * TODO j'ai du adapter les temps passe lors de l'implémentation des pauses
	 */
	public void action(long tempsPasse)
	{
	    // avance la creature
	    //avancerSurChemin(getTempsAppel());
	    avancerSurChemin(tempsPasse);
	    
	    // la creature est arrivee a destination !
        if(chemin != null && indiceCourantChemin == chemin.size() 
           && !aDetruire && !estMorte())
        {
            aDetruire = true;

            // informe les ecouteurs que la creature est arrivee 
            // a la fin du parcours
            for(EcouteurDeCreature edc : ecouteursDeCreature)
                edc.creatureArriveeEnZoneArrivee(this);
        }
	}
	
	/**
     * Permet de faire avancer le creature sur son chemin.
     * 
     * Celle-ci avance d'un pixel jusqu'au point suivant du chemin puis 
     * increment alors l'indice courant du chemin.
     * 
     * @param tempsEcoule le temps ecoule depuis le dernier appel
     */
    protected void avancerSurChemin(long tempsEcoule)
    {
        ArrayList<Point> chemin = getChemin();
        
        // si la creature a un chemin et que le chemin n'est pas terminee, 
        // elle avance...
        if(chemin != null && indiceCourantChemin < chemin.size())
        {
            // recuperation des noeuds
            Point pPrecedent = chemin.get(indiceCourantChemin-1);
            Point pSuivant   = chemin.get(indiceCourantChemin);
            
            // TODO [OPTIMISATION] faire des constantes LARGEUR_MOITIE et HAUTEUR_MOITIE
            // calcul du centre de la creature
            double centreX = xReel + width / 2.0 ;
            double centreY = yReel + height / 2.0;
  
            //---------------------------------------------
            //-- calcul de la position apres deplacement --
            //---------------------------------------------
            
            // calcul de l'angle entre la creature et le noeud suivant
            // /!\ Math.atan2(y,x) /!\
            angle = Math.atan2(centreY - pSuivant.y,centreX - pSuivant.x);

            // calcul de la distance effectuee
            double distance = getVitesseReelle() * ((double) tempsEcoule / 1000.0);
            
            // calcul la position apres mouvement de la creature
            xReel -= Math.cos(angle)*distance; // x
            yReel -= Math.sin(angle)*distance; // y

            // calcul du nouveau centre de la creature
            centreX = xReel + width / 2.0;
            centreY = yReel + height / 2.0;
            
            //--------------------------
            //-- calcul des distances --
            //--------------------------
            // pour savoir si la creature a depasser le point suivant du chemin
            
            // calcul de la distance entre le noeud precedent et suivant
            double distanceEntre2Noeuds = Point.distance(pSuivant.x, pSuivant.y,
                                                   pPrecedent.x, pPrecedent.y);
            
            // calcul de la distance entre la creature et le noeud precedent
            double distanceDuNoeudPrecedent = Point.distance(pPrecedent.x, pPrecedent.y,
                                                             centreX, centreY);
              
            //---------------------
            //-- noeud atteint ? --
            //---------------------
            // si la creature a depassee le noeud suivant
            if(distanceDuNoeudPrecedent >= distanceEntre2Noeuds)
            { 
                // il prend la position du noeud suivant
                xReel = pSuivant.x - width / 2.0;
                yReel = pSuivant.y - height / 2.0;
                
                // on change de noeud suivant
                indiceCourantChemin++;
            }
            
            // mise a jour des coordonnees entieres
            x = (int) Math.round(xReel);
            y = (int) Math.round(yReel);
        }
    }
	
	/**
	 * Permet de faire subir des degats sur la creature
	 * 
	 * L'attaque pouvant venir de plusieurs tours en meme temps, cette 
	 * methode doit etres synchronisee.
	 * 
	 * @param degats les degats recus
	 */
	synchronized public void blesser(long degats, Joueur joueur)
	{
		// si pas deja morte et que le joueur peu la tuer
		if(!estMorte() && joueur.getEquipe() == equipeCiblee)
		{
			// diminution de la sante
			if(!invincible) // pour les clients...
			    sante -= degats;
			
			// appel des ecouteurs de la creature
			for(EcouteurDeCreature edc : ecouteursDeCreature)
				edc.creatureBlessee(this);
			
			// est-elle morte ?
			if(estMorte())
				mourrir(joueur);
		}
	}
	
	/**
	 * Permet savoir si la creature est morte
	 */
	public boolean estMorte()
	{
		return sante <= 0;
	}
	
	/**
	 * Permet de tuer la creature
	 */
	public void mourrir(Joueur tueur)
	{ 
	    sante = 0;
		
		// appel des ecouteurs de la creature
		for(EcouteurDeCreature edc : ecouteursDeCreature)
			edc.creatureTuee(this,tueur);
		
		aDetruire = true;
	}

	/**
	 * Permet d'ajouter un ecouteur de la creature
	 * 
	 * @param edc une classe implementant EcouteurDeCreature
	 */
	public void ajouterEcouteurDeCreature(EcouteurDeCreature edc)
	{
		ecouteursDeCreature.add(edc);
	}
	
	/**
     * Permet de recuperer le temps ecouler depuis le dernier appel de cette meme 
     * fonction
     * @return le temps en milliseconde entre chaque appel de cette fonction
     *         si c'est le premier appel, retourne 0.
     */
    protected long getTempsAppel()
    {
        // initialisation du temps actuel
        long maintenant = System.currentTimeMillis(); 
        
        // si c'est la premiere fois qu'on passe
        if(tempsDernierAppel == 0)
        {
            tempsDernierAppel = maintenant;
            return 0;
        }
        
        // temps passe depuis le dernier appel
        long tempsEcoule = maintenant - tempsDernierAppel;
        
        tempsDernierAppel = maintenant;
        return tempsEcoule;
    }
    private long tempsDernierAppel;

    /**
     * Permet d'informer un instance superieure qu'il faut detruire la creature
     * 
     * @return true s'il faut la detruire, false sinon
     */
    public boolean aDetruire()
    {
        return aDetruire;
    }

    /**
     * Permet de récupérer l'équipe ennemie ciblée
     * 
     * @return l'équipe ennemie ciblée
     */
    public Equipe getEquipeCiblee()
    {
        return equipeCiblee;
    }

    /**
     * Permet de modifier l'equipe ciblée par la créature
     * 
     * @param equipeCiblee le nouvelle équipe ciblée
     */
    public void setEquipeCiblee(Equipe equipeCiblee)
    {
        this.equipeCiblee = equipeCiblee;
    }

    /**
     * Permet de modifier l'id 
     * 
     * @param id le nouvel id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Permet de modifier la sante
     * 
     * @param sante la nouvelle sante
     */
    public void setSante(int sante)
    {
        this.sante = sante;
    }

    /**
     * Permet de modifier l'angle
     * 
     * @param angle le nouvel angle
     */
    public void setAngle(double angle)
    {
        this.angle = angle;
    }
    
    
    /**
     * Permet de savoir si une creature peut etre blessee.
     * 
     * Il s'agit en fait d'une verification des types et des equipes.
     * 
     * @param creature le créature a testee
     * @return true si la creature peut etre blessee, false sinon
     */
    public boolean peutEtreAttaquee(Tour tour)
    {
        // si c'est pas une créature ennemie
        if(tour.getPrioprietaire().getEquipe() != equipeCiblee)
            return false;
        
        // elle est blessable
        int typeTour = tour.getType();
        return typeTour == Tour.TYPE_TERRESTRE_ET_AIR 
            || (typeTour == Tour.TYPE_TERRESTRE && TYPE == Creature.TYPE_TERRIENNE) 
            || (typeTour == Tour.TYPE_AIR && TYPE == Creature.TYPE_AERIENNE);
    }

    public void setSanteMax(long santeMax)
    {
        this.santeMax = santeMax;
    }

    public void setNbPiecesDOr(int nbPiecesDOr)
    {
        this.nbPiecesDOr = nbPiecesDOr;
    }

    public void setVitesse(double vitesse)
    {
        this.vitesseNormale = vitesse;
    }

    
    
    long tempsDerniereMAJ = 0;
    public void misAJour()
    {
        tempsDerniereMAJ = new Date().getTime();   
    }

    public void effacerSiPasMisAJour()
    {
        long maintenant = new Date().getTime(); 
        
        if(tempsDerniereMAJ != 0) // seulement si mis a jour une fois
        {
            if(maintenant - tempsDerniereMAJ > TEMPS_AVANT_SUPPRESSION_SI_AUCUNE_MAJ)
            {
                System.out.println("CREATURE TUEE PAR FORCE APRES "+
                       (TEMPS_AVANT_SUPPRESSION_SI_AUCUNE_MAJ / 1000.0)+
                       " SEC. D'INACTIVITE ! (id : "+id+" )");
                
                mourrir(null);
            }
        }
    }
}
