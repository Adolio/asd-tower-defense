package models.terrains;

import java.awt.*;
import java.awt.geom.Line2D;
import java.io.*;
import java.util.*;
import javax.swing.*;
import models.creatures.*;
import models.jeu.Jeu;
import models.joueurs.Equipe;
import models.maillage.*;
import models.outils.GestionnaireSons;
import models.outils.Son;
import models.tours.Tour;

/**
 * Classe de gestion d'un terrain de jeu.
 * <p>
 * Cette classe contient tous les elements contenu sur le terrain, c'est a dire
 * :<br>
 * - Les tours
 * <p>
 * - Les creatures : la liste des creatures et les vagues de creatures
 * <p>
 * - Les maillages : Il y a deux types de maillages, un pour les creatures
 * terrestres ou l'emplacement des tours a une influence sur le chemin des
 * creatures et un autre pour les creatures aerienne ou l'emplacement des tours
 * n'a aucune influence sur le chemin des creatures (elles passent par dessus
 * les tours)
 * <p>
 * - Les murs propres au terrain : simples zones inaccessibles
 * <p>
 * - Les zones de depart et arrivee des creatures<br>
 * <p>
 * - L'image de fond du terrain
 * <p>
 * <p>
 * Plusieurs methodes sont mises a disposition par cette classe pour gerer les
 * elements qu'elle contient.
 * <p>
 * De plus, cette classe est abstraite, elle ne peut pas etre instanciee en tant
 * que telle mais doit etre heritee.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Tour
 * @see Creature
 * @see Maillage
 */
public abstract class Terrain implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String EXTENSION_FICHIER = "map";

    /**
     * nom de la zone de jeu
     */
    private final String NOM;

    /**
     * nombre de vies au debut de la partie
     */
    private final int NB_VIES_INITIALES;

    /**
     * nombre de pieces d'or au debut de la partie
     */
    private final int NB_PIECES_OR_INITIALES;

    /**
     * Taille du terrain
     */
    private final int LARGEUR, // en pixels
            HAUTEUR; // en pixels

    /**
     * precision du maillage, espace entre deux noeuds
     */
    private final int PRECISION_MAILLAGE = 10; // pixels

    /**
     * Le maillage permet de definir les chemins des creatures sur le terrain.
     * Ici, pour les creatures terriennes avec prise en compte de la position
     * des tours.
     * 
     * @see Maillage
     */
    transient private Maillage MAILLAGE_TERRESTRE;
    transient private Maillage MAILLAGE_AERIEN;
    
    /**
     * Dimention du maillage
     */
    int largeurMaillage, hauteurMaillage;
    
    /**
     * Offset du maillage
     */
    int positionMaillageX, positionMaillageY;
    
    /**
     * Les creatures volantes n'ont pas besoins d'une maillage mais uniquement
     * du chemin le plus court entre la zone de depart et la zone d'arrivee
     */
    transient ArrayList<Point> cheminAerien;

    /**
     * Image de fond du terrain. <br>
     * Note : les murs du terrain sont normalement lies a cette image
     */
    transient private Image imageDeFond;
    
    // seule les ImageIcon peuvent etre serialisé
    // utilisation de cette variable pour sauver l'image de fond
    private ImageIcon iconImageDeFond;
    
    /**
     * Pour le mode debug
     */
    private final Color COULEUR_MURS;
    private final Color COULEUR_DE_FOND;
    
    /**
     * Les murs sont utilises pour empecher le joueur de construire des tours
     * dans certaines zones. Les creatures ne peuvent egalement pas si rendre.
     * En fait, les murs reflettent les zones de la carte non accessible. Les
     * murs ne sont pas affiches. Ils sont simplement utilises pour les controls
     * d'acces de la carte.
     */
    protected ArrayList<Rectangle> murs = new ArrayList<Rectangle>();
  
    /**
     * musique d'ambiance du terrain
     */
    protected File fichierMusiqueDAmbiance;

    /**
     * Stockage du jeu
     */
    transient private Jeu jeu;
    
    /**
     * Stockage de la vague courante
     */
    transient private VagueDeCreatures vagueCourante;
    
    /**
     * Liste des equipes, utilisé pour la définition du terrain.
     * <br>
     * C'est le terrain qui fourni les équipes, on doit pouvoir
     * les sérialiser.
     */
    protected ArrayList<Equipe> equipes = new ArrayList<Equipe>();
    
    /**
     * Mode de jeu du terrain, utilisé pour construire les bons formulaires 
     * et affichages
     */
    private final int MODE;
    
    /**
     * Permet de definit la taille du panel du terrain
     */
    protected Dimension taillePanelTerrain = null;
    
    /**
     * Constructeur du terrain.
     * 
     * @param largeur la largeur en pixels du terrain (utilisé pour le maillage)
     * @param hauteur la hauteur en pixels du terrain (utilisé pour le maillage)
     * @param nbPiecesOrInitiales le nom de piece d'or en debut de partie
     * @param positionMaillageX position du point 0 du maillage
     * @param positionMaillageY position du point 0 du maillage
     * @param largeurMaillage largeur du maillage en pixel
     * @param hauteurMaillage hauteur du maillage en pixel
     * @param imageDeFond le chemin jusqu'a l'image de fond
     * @param nom nom de la zone de jeu
     */
    public Terrain(Jeu jeu, int largeur, int hauteur, int nbPiecesOrInitiales,
            int nbViesInitiales, int positionMaillageX, int positionMaillageY,
            int largeurMaillage, int hauteurMaillage, int mode, Color couleurDeFond, 
            Color couleurMurs, Image imageDeFond, String nom)
    {
        this.jeu = jeu; 
        LARGEUR = largeur;
        HAUTEUR = hauteur;
        NB_PIECES_OR_INITIALES = nbPiecesOrInitiales;
        NB_VIES_INITIALES      = nbViesInitiales;
        this.imageDeFond       = imageDeFond;
        this.iconImageDeFond   = new ImageIcon(imageDeFond);
        
        this.largeurMaillage = largeurMaillage;
        this.hauteurMaillage = hauteurMaillage;
        this.positionMaillageX = positionMaillageX;
        this.positionMaillageY = positionMaillageY;
        
        NOM             = nom;
        COULEUR_DE_FOND = couleurDeFond;
        COULEUR_MURS    = couleurMurs;
        MODE            = mode;   
    }
   
    /**
     * Permet d'initialiser le terrain.
     * 
     * Il s'agit de construire les maillages et d'activer les murs.
     */
    public void initialiser()
    {
        // creation des deux maillages
        MAILLAGE_TERRESTRE = new Maillage_v1(largeurMaillage, hauteurMaillage,
                PRECISION_MAILLAGE, positionMaillageX, positionMaillageY);
        
        MAILLAGE_AERIEN = new Maillage_v1(largeurMaillage, hauteurMaillage,
                PRECISION_MAILLAGE, positionMaillageX, positionMaillageY);  
        
        // activation des murs
        for(Rectangle mur : murs)
        {
            MAILLAGE_TERRESTRE.desactiverZone(mur);
            MAILLAGE_AERIEN.desactiverZone(mur);
        }
    }
    
    // ------------------------------
    // -- GETTER / SETTER BASIQUES --
    // ------------------------------

    /**
     * Permet de recuperer la largeur du terrain.
     * 
     * @return la largeur du terrain
     */
    public int getLargeur()
    {
        return LARGEUR;
    }

    /**
     * Permet de recuperer la hauteur du terrain.
     * 
     * @return la hauteur du terrain
     */
    public int getHauteur()
    {
        return HAUTEUR;
    }

    /**
     * Permet de recuperer l'image de fond du terrain.
     * 
     * @return l'image de fond du terrain
     */
    public Image getImageDeFond()
    {      
        return imageDeFond;
    }

    /**
     * Permet de recuperer le nombre de pieces initial
     * 
     * @return le nombre de pieces initiales
     */
    public int getNbPiecesOrInitiales()
    {
        return NB_PIECES_OR_INITIALES;
    }

    /**
     * Permet de recuperer le nombre de vie du joueur en debut de partie
     * 
     * @return le nombre de vie du joueur en debut de partie
     */
    public int getNbViesInitiales()
    {
        return NB_VIES_INITIALES;
    }

    /**
     * Permet de recuperer le nom du terrain
     * 
     * @return le nom du terrain
     */
    public String getNom()
    {
        return NOM;
    }
    
    /**
     * Permet de recuperer le mode de jeu du terrain
     * 
     * @return le mode de jeu
     */
    public int getMode()
    {
        return MODE;
    }

    /**
     * Permet de recuperer la taille voulue pour le panel du terrain
     * 
     * @return la taille voulue pour le panel du terrain
     */
    public Dimension getTaillePanelTerrain()
    {
        if(taillePanelTerrain != null)
            return taillePanelTerrain;
        else  
            return new Dimension(LARGEUR,HAUTEUR);
    }
    
    // ----------------------
    // -- GESTION DES MURS --
    // ----------------------

    /**
     * Permet d'ajouter un mur sur le terrain.
     * 
     * @param mur le mur ajouter
     */
    public void ajouterMur(Rectangle mur)
    {
        // c'est bien un mur valide ?
        if (mur == null)
            throw new IllegalArgumentException("Mur nul");

        // desactive la zone dans le maillage qui correspond au mur
        if(MAILLAGE_TERRESTRE != null)
            MAILLAGE_TERRESTRE.desactiverZone(mur);
        
        if(MAILLAGE_AERIEN != null)
            MAILLAGE_AERIEN.desactiverZone(mur);

        // ajout du mur
        murs.add(mur);

        /*
         * Recalculation du chemin des créatures volantes
         */
        // TODO adapter pour chemin aérien
        /*
        try
        {
            cheminAerien = MAILLAGE_TERRESTRE.plusCourtChemin((int) ZONE_DEPART
                    .getCenterX(), (int) ZONE_DEPART.getCenterY(),
                    (int) ZONE_ARRIVEE.getCenterX(), (int) ZONE_ARRIVEE
                            .getCenterY());

        } catch (PathNotFoundException e)
        {
            // ne peut pas arriver, au pire on l'affiche
            e.printStackTrace();
        }*/
    }

    /**
     * Permet de recuperer les murs du terrain
     * 
     * @return les murs du terrain
     */
    public ArrayList<Rectangle> getMurs()
    {
        return murs;
    }

    /**
     * Permet de récupérer la couleur de fond (mode debug)
     * @return la couleur de fond
     */
    public Color getCouleurDeFond()
    {
        return COULEUR_DE_FOND;
    }

    /**
     * Permet de récupérer la couleur des murs (mode debug)
     * @return la couleur des murs
     */
    public Color getCouleurMurs()
    {
        return COULEUR_MURS;
    }
    
    /**
     * Permet de recuperer les équipes initiales du terrain
     * 
     * @return les équipes initiales
     */
    public ArrayList<Equipe> getEquipesInitiales()
    {
        return equipes;
    }
    
    /**
     * Permet de recuperer les nombres joueurs max que peut accueillir le terrain
     * 
     * @return le nombre de joueur max
     */
    public int getNbJoueursMax()
    {
        int somme = 0;
           
        for(Equipe e : equipes)
            somme += e.getNbEmplacements();
        
        return somme;
    }

    // -----------------------
    // -- GESTION DES TOURS --
    // -----------------------

    /**
     * Permet de savoir si une tour peut etre posee a un certain endroit sur le
     * terrain.
     * 
     * @param tour la tour a posee
     * @return true si la tour peut etre posee, false sinon
     */
    public boolean laTourPeutEtrePosee(Tour tour)
    {
        // c'est une tour valide ?
        if (tour == null)
            return false;

        // elle est bien dans le terrain
        if (tour.getX() < 0 || tour.getX() > LARGEUR-tour.width
         || tour.getY() < 0 || tour.getY() > HAUTEUR-tour.height)
            return false;
            
        // il n'y a pas un mur
        synchronized (murs)
        {
            for (Rectangle mur : murs)
                if (tour.intersects(mur))
                    return false;
        }

        // il n'y a pas les zones de depart ou d'arrivee
        for(Equipe e : jeu.getEquipes())
            // TODO gérer plusieurs zone de depart
            if (tour.intersects(e.getZoneDepartCreatures(0)) || tour.intersects(e.getZoneArriveeCreatures()))
                return false;

        // rien empeche la tour d'etre posee
        return true;
    }

    /**
     * Permet de savoir si apres la pose d'une tour en parametre le chemin
     * deviendra bloque ?
     * 
     * @param tour la tour a testee si elle bloquera le chemin
     * @return true si elle le bloquera lors de la pose, false sinon
     */
    public boolean laTourBloqueraLeChemin(Tour tour)
    {
        // c'est une tour valide ?
        if (tour == null)
            return false;

        // si l'on construit la tour, il existe toujours un chemin
        desactiverZone(tour, false);

        try {
            
            Equipe equipe = tour.getPrioprietaire().getEquipe();

            // on part du principe que le joueur ne peu blocker que son chemin
            // car il contruit sur son troncon... A VOIR!
            
            // TODO gérer plusieurs zone de depart
            Rectangle zoneDepart = equipe.getZoneDepartCreatures(0);
            Rectangle zoneArrivee = equipe.getZoneArriveeCreatures();
            
            // calcul du chemin et attente une exception
            // PathNotFoundException s'il y a un probleme
            ArrayList<Point> chemin = getCheminLePlusCourt((int) zoneDepart.getCenterX(),
                    (int) zoneDepart.getCenterY(), (int) zoneArrivee
                            .getCenterX(), (int) zoneArrivee.getCenterY(),
                    Creature.TYPE_TERRIENNE);

            double longueur = MAILLAGE_TERRESTRE.getLongueurChemin(chemin);
            
            
            // mise a jour du chemin
            equipe.setLongueurChemin(longueur);
            
            
            // il existe un chemin, donc elle ne bloque pas.
            activerZone(tour, false); // on reactive la zone
            return false;

        } 
        catch (PathNotFoundException e) {
            // il n'existe pas de chemin, donc elle bloque le chemin.
            activerZone(tour, false); // on reactive la zone
            return true;
        }
    }
    
    // ---------------------------
    // -- GESTION DES CREATURES --
    // ---------------------------
    
    /**
     * Methode qui permet de recuperer la vague de creatures suivantes
     * 
     * Note : cette méthode peut etre redéfinie par les fils de Terrain
     * 
     * @return la vague de creatures suivante
     */
    public VagueDeCreatures getVagueDeCreatures(int noVague)
    {
        return VagueDeCreatures.genererVagueStandard(noVague);
    }

    /**
     * Permet de recuperer la description vague suivante
     * 
     * @return la description de la vague suivante
     */
    public String getDescriptionVague(int noVague)
    {   
        // récupération de la vague suivante
        VagueDeCreatures vagueSuivante = getVagueDeCreatures(noVague);
        
        // s'il y a une description, on la retourne
        String descriptionVague = vagueSuivante.getDescription();
        if (!descriptionVague.isEmpty())
            return descriptionVague;

        // sinon on genere une description
        return vagueSuivante.toString();
    }
    
    
    // -------------------------
    // -- GESTION DU MAILLAGE --
    // -------------------------

    /**
     * Permet d'activer ou reactiver un zone rectangulaire du maillage.
     * 
     * @param zone la zone rectangulaire a activer
     * @param miseAJourDesCheminsDesCreatures faut-il mettre a jour les chemins
     *            des creatures ?
     */
    public void activerZone(Rectangle zone,
            boolean miseAJourDesCheminsDesCreatures)
    {
        // activation de la zone
        if(MAILLAGE_TERRESTRE != null)
        {
            try
            {
                MAILLAGE_TERRESTRE.activerZone(zone);
            }
            catch(IllegalArgumentException e)
            {}
            
            // mise a jour des chemins si necessaire
            if (miseAJourDesCheminsDesCreatures)
                miseAJourDesCheminsDesCreatures();
        }
    }

    /**
     * Permet de desactiver un zone rectangulaire du maillage.
     * 
     * @param zone la zone rectangulaire a desactiver
     * @param miseAJourDesCheminsDesCreatures faut-il mettre a jour les chemins
     *            des creatures ?
     */
    public void desactiverZone(Rectangle zone,
            boolean miseAJourDesCheminsDesCreatures)
    {
        // desactivation de la zone
        if(MAILLAGE_TERRESTRE != null)
            MAILLAGE_TERRESTRE.desactiverZone(zone);
        
        // mise a jour des chemins si necessaire
        if (miseAJourDesCheminsDesCreatures)
            miseAJourDesCheminsDesCreatures();
    }

    /**
     * Permet de mettre a jour les chemins des creatures lors de la modification
     * du maillage.
     */
    synchronized private void miseAJourDesCheminsDesCreatures()
    {
        // Il ne doit pas y avoir de modifications sur la collection
        // durant le parcours.
        Creature creature;
        Enumeration<Creature> eCreatures = jeu.getCreatures().elements();
        while(eCreatures.hasMoreElements())
        {
            creature = eCreatures.nextElement();
        
            // les tours n'affecte que le chemin des creatures terriennes
            if (creature.getType() == Creature.TYPE_TERRIENNE)   
            {
                Rectangle zoneArrivee = creature.getEquipeCiblee().getZoneArriveeCreatures();
                
                try
                { 
                    creature.setChemin(getCheminLePlusCourt((int) creature
                            .getCenterX(), (int) creature.getCenterY(),
                            (int) zoneArrivee.getCenterX(),
                            (int) zoneArrivee.getCenterY(), creature
                                    .getType()));
                }
                catch (PathNotFoundException e)
                {
                    /*
                     *  s'il n'y a pas de chemin, 
                     *  on essaye depuis le noeud precedent
                     */
                    try
                    {
                        ArrayList<Point> chemin = creature.getChemin();
                        
                        if(chemin != null)
                        {
                            // recuperation du noeud precedent sur le chemin
                            Point noeudPrecedent;
                            
                            if(creature.getIndiceCourantChemin() > 0) // pas au depart
                                noeudPrecedent = chemin.get(creature.getIndiceCourantChemin()-1);
                            else
                                noeudPrecedent = new Point(zoneArrivee.x, zoneArrivee.y);
         
                            // calcul du nouveau chemin
                            creature.setChemin(getCheminLePlusCourt(
                                    (int) noeudPrecedent.x, 
                                    (int) noeudPrecedent.y,
                                    (int) zoneArrivee.getCenterX(),
                                    (int) zoneArrivee.getCenterY(), 
                                    creature.getType())); 
                        } 
                    }
                    catch (PathNotFoundException e2)
                    {
                        // s'il n'y a toujours pas de chemin, on garde l'ancien.
                    }
                }
            }
        }
    }

    /**
     * Permet de recuperer le chemin le plus court entre deux points sur le
     * terrain.
     * 
     * Cette methode fait appel au maillage pour decouvrir ce chemin
     * 
     * @param xDepart la position x du point de depart
     * @param yDepart la position y du point de depart
     * @param xArrivee la position x du point d'arrivee
     * @param yArrivee la position y du point d'arrivee
     * @return le chemin sous la forme d'un ArrayList de java.awt.Point ou
     *         <b>null si aucun chemin ne relie les deux points</b>.
     * @throws PathNotFoundException
     * @throws IllegalArgumentException
     * @see Maillage
     */
    public ArrayList<Point> getCheminLePlusCourt(int xDepart, int yDepart,
            int xArrivee, int yArrivee, int typeCreature)
            throws IllegalArgumentException, PathNotFoundException
    {
        // TODO adapter pour chemin aérien
        if (typeCreature == Creature.TYPE_TERRIENNE)
            return MAILLAGE_TERRESTRE.plusCourtChemin(xDepart, yDepart,
                    xArrivee, yArrivee);
        else
            return MAILLAGE_AERIEN.plusCourtChemin(xDepart, yDepart,
                    xArrivee, yArrivee);
    }

    /**
     * Permet de recuperer la liste des arcs actifs du maillage terrestre.
     * 
     * @return une collection de java.awt.geom.Line2D representant les arcs
     *         actifs du maillage
     */
    public Line2D[] getArcsActifs()
    {
        return MAILLAGE_TERRESTRE.getArcs();
    }

    /**
     * Permet de recuperer les noeuds du maillage terrestre
     * 
     * @return Une collection de noeuds
     */
    public Noeud[] getNoeuds()
    {
        return MAILLAGE_TERRESTRE.getNoeuds();
    }

    // -------------
    // -- MUSIQUE --
    // -------------

    /**
     * Permet de demarrer la lecture de la musique d'ambiance
     */
    public void demarrerMusiqueDAmbiance()
    {
        if (fichierMusiqueDAmbiance != null)
        {
            Son musiqueDAmbiance = new Son(fichierMusiqueDAmbiance);

            GestionnaireSons.ajouterSon(musiqueDAmbiance);
            musiqueDAmbiance.lire(0); // lecture infinie
        }
    }

    /**
     * Permet d'arreter la lecture de la musique d'ambiance
     */
    public void arreterMusiqueDAmbiance()
    {
        if (fichierMusiqueDAmbiance != null)
            GestionnaireSons.arreterTousLesSons(fichierMusiqueDAmbiance);
    }

    // -----------
    // -- PAUSE --
    // -----------
    
    /**
     * Permet de mettre les créatures en pause.
     */
    public void mettreEnPause()
    {
        if(vagueCourante != null)
            vagueCourante.mettreEnPause();
    }
    
    /**
     * Permet de sortir les créatures de la pause.
     */
    public void sortirDeLaPause()
    { 
        if(vagueCourante != null)
            vagueCourante.sortirDeLaPause();
    }
    
    /**
     * Permet de sérialisé un Terrain
     * 
     * @param terrain le terrain à sérialiser
     * @param fichier le fichier de destination
     */
    public static void serialiser(Terrain terrain)
    {
       File fichier = new File("maps/"+terrain.getClass().getSimpleName()+".map");

       try
       {
          // Ouverture d'un flux de sortie vers le fichier FICHIER.
          FileOutputStream fluxSortieFichier = new FileOutputStream(fichier);
          
          // Creation d'un "flux objet" avec le flux fichier.
          ObjectOutputStream fluxSortieObjet = new ObjectOutputStream(fluxSortieFichier);
          try
          {
             // Serialisation : ecriture de l'objet dans le flux de sortie.
             fluxSortieObjet.writeObject(terrain);
             
             // On vide le tampon.
             fluxSortieObjet.flush();
          }
          finally
          {
             // Fermeture des flux (important!).
             try
             {
                fluxSortieObjet.close();
             }
             finally
             {
                fluxSortieFichier.close();
             }
          }
       }
       catch (IOException erreur) // Erreur sur l'ObjectOutputStream.
       {
          erreur.printStackTrace();
       }
    }
    
    /**
     * Permet de charger un terrain serialisé
     * 
     * @param fichier le fichier Terrain serialisé
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    public static Terrain charger(File fichier) throws IOException, ClassNotFoundException, ClassCastException
    {
       
      // Ouverture d'un flux d'entree depuis le fichier FICHIER.
      FileInputStream fluxEntreeFichier = new FileInputStream(fichier);
      // Creation d'un "flux objet" avec le flux d'entree.
      ObjectInputStream fluxEntreeObjet = new ObjectInputStream(fluxEntreeFichier);
      try
      {
         // Deserialisation : lecture de l'objet depuis le flux d'entree
         // (chargement des donnees du fichier).
          
         Terrain terrain = (Terrain) fluxEntreeObjet.readObject();
         
         // seule les ImageIcon peuvent etre serialisée 
         // donc la on met a jour l'image de font avec une ImageIcon
         terrain.imageDeFond = terrain.iconImageDeFond.getImage();
        
         return terrain ;
      }
      finally
      {
         // On ferme les flux (important!).
         try{
            fluxEntreeObjet.close();
         }
         finally
         {
            fluxEntreeFichier.close();
         }
      }
    }

    public void setJeu(Jeu jeu)
    {
        this.jeu = jeu;
    }
}