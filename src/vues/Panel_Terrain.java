package vues;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.*;

import javax.swing.*;
import models.creatures.Creature;
import models.jeu.Jeu;
import models.joueurs.Joueur;
import models.tours.Tour;

/**
 * Panel d'affichage du terrain de jeu.
 * <p>
 * Ce panel affiche la zone de jeu avec tous les elements que contient le terrain.
 * <p>
 * Celle-ci affichera les tours avec les créatures et gèrera le positionnement
 * des tours et la selection des tours.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see JPanel
 * @see Runnable
 * @see MouseListener
 * @see MouseMotionListener
 */
public class Panel_Terrain extends JPanel implements Runnable, 
													 MouseListener,
													 MouseMotionListener,
													 KeyListener,
													 MouseWheelListener
{
	private static final long serialVersionUID = 1L;
	
	//-------------------------
	//-- proprietes du panel --
	//-------------------------
	private static final Color COULEUR_FOND = new Color(50,200,50);
	private final int LARGEUR;
	private final int HAUTEUR;
	
	/**
	 * Hauteur de la barre de vie d'une creature. (en pixels)
	 */
	private static final int HAUTEUR_BARRE_VIE = 4;
	
	/** 
	 * Coefficient de largeur de la barre de vie d'une creature.
	 * (en % de la largeur de la creature)
	 */
	private static final float COEFF_LARGEUR_BARRE_VIE = 1.5f; // 150%

	/**
	 * largeur d'un case du maillage pour le positionnement des tours
	 */
	private static final int CADRILLAGE = 10; // unite du cadriallage en pixel
	
	/**
	 * Coefficient de taille du rendu final du terrain
	 */
	private double coeffTaille = 1.0;
	
	/**
	 * Taille des marges pour le scrolling automatique sur les bords du panel
	 */
	private final int MARGES_DEPLACEMENT = 20;
	
	/**
     * Décalages du rendu par rapport à la position 0 du panel (scale)
     */
	private int decaleX = 0,
	            decaleY = 0;
	
	//---------------------------
	//-- preferences de dessin --
	//---------------------------
	/**
	 * Crayon pour un trait tillé
	 */
	// 2 pixels remplis suivi de 2 pixels transparents
	private static final float [] DASHES   = {2.0F, 2.0F}; 
	private static final BasicStroke TRAIT_TILLE = new BasicStroke(
	            1.0f,BasicStroke.CAP_ROUND, 
	            BasicStroke.JOIN_MITER, 
	            10.0F, DASHES, 0.F);
	Stroke traitTmp;
	
	// 0.0f = 100% transparent et 1.0f vaut 100% opaque.
	private static final float ALPHA_PERIMETRE_PORTEE = .6f;
	private static final float ALPHA_SURFACE_PORTEE   = .3f;
	private static final float ALPHA_MAILLAGE   	  = .4f;
	private static final float ALPHA_SURFACE_ZONE_DA  = .5f;
	private static final float ALPHA_TOUR_A_AJOUTER   = .7f;
	private static final float ALPHA_CHEMIN_CREATURE  = .5f;
	private static final float ALPHA_SURFACE_MUR      = .8f;
	
	private static final Color COULEUR_ZONE_DEPART 	= Color.GREEN;
	private static final Color COULEUR_ZONE_ARRIVEE = Color.RED;
	private static final Color COULEUR_MAILLAGE 	= Color.WHITE;
	private static final Color COULEUR_SANTE 		= Color.GREEN;
	private static final Color COULEUR_CHEMIN 		= Color.BLUE;
	private static final Color COULEUR_CREATURE_SANS_IMAGE = Color.YELLOW;
	private static final Color COULEUR_SELECTION	= Color.WHITE;
	private static final Color COULEUR_POSE_IMPOSSIBLE = Color.RED;
	private static final Color COULEUR_CONTENEUR_SANTE = Color.BLACK;
	private static final Color COULEUR_RAYON_PORTEE = Color.WHITE;
	private static final Color COULEUR_NIVEAU 		= Color.WHITE;
	private static final Color COULEUR_NIVEAU_PERIMETRE = Color.YELLOW;
	
	
	/**
	 * Thread de gestion du rafraichissement de l'affichage
	 */
	private Thread thread;
	
	/**
	 * Temps de repose dans la boucle d'affichage
	 */
	private static final int TEMPS_REPOS_THREAD = 40;

	/**
	 * Position exacte de la souris sur le terrain
	 */
	private int sourisX, sourisY;
		
	
	/**
	 * Permet de stocker l'endroit du debut de l'agrippage
	 */
	private int sourisGrabX, sourisGrabY;
	
	/**
	 * Permet de savoir le decalage effectuer depuis le position
	 * du debut de l'agrippage
	 */
	private int decaleGrabX, decaleGrabY;
	
	/**
	 * Position de la souris sur le cadriallage virtuel
	 */
	private int sourisCaseX, sourisCaseY;
	
	/**
	 * Permet de savoir si la souris est actuellement sur le panel
	 */
	private boolean sourisSurTerrain;
	
	/**
	 * Le terrain permet de choisir la tour a poser sur le terrain.
	 * Si cette variable est non nulle et que le joueur clique sur le
	 * terrain, la tour a ajouter sera posée.
	 */
	private Tour tourAAjouter;
	
	/**
	 * Lorsque le joueur clique sur une tour, elle devient selectionnee.
	 * Une fois selectionnee des informations sur la tour apparaissent
	 * dans le menu d'interaction. Le joueur pourra alors améliorer ou 
	 * vendre la tour.
	 */
	private Tour tourSelectionnee;
	
	/**
     * Lorsque le joueur clique sur une creature, elle devient selectionnee.
     * Une fois selectionnee des informations sur la creature apparaissent
     * dans le menu d'interaction.
     */
	private Creature creatureSelectionnee;
	
	/**
	 * Reference vers le jeu a gerer
	 */
	private Jeu jeu;
	
	/**
     * Reference vers le joueur à gerer
     */
	private Joueur joueur;
	
	/**
	 * Reference vers la fenetre parent
	 */
	private EcouteurDePanelTerrain edpt;
	
	/**
	 * Permet d'afficher ou non les elements invisible (maillage, chemins, etc.)
	 */
	private boolean afficherMaillage;
	
	/**
	 * Permet d'afficher ou non tous les rayons de portee des tours
	 */
	private boolean afficherRayonsDePortee;
	
	/**
	 * Etape d'une echelle de zoom
	 */
	private final double ETAPE_ZOOM = 0.2;

	/**
	 * Stockage du bouton lors d'un aggripement
	 */
    private int boutonDragg;

    private boolean centrerSurCreatureSelectionnee;
	
	// curseurs
	private static Cursor curRedimDroite   = new Cursor(Cursor.E_RESIZE_CURSOR);
	private static Cursor curRedimGauche   = new Cursor(Cursor.W_RESIZE_CURSOR);
	private static Cursor curRedimHaut     = new Cursor(Cursor.N_RESIZE_CURSOR);
	private static Cursor curRedimBas      = new Cursor(Cursor.S_RESIZE_CURSOR);
	private static Cursor curNormal        = new Cursor(Cursor.DEFAULT_CURSOR);
	private static Cursor curMainAgripper;
	
	static
	{
	    /*
	    TODO [INFO] curseur transparent
	    int[] pixels = new int[16 * 16];
	    Image image = Toolkit.getDefaultToolkit().createImage(
	            new MemoryImageSource(16, 16, pixels, 0, 16));
	    curTransparent = Toolkit.getDefaultToolkit().createCustomCursor
	                 (image, new Point(0, 0), "curseurInvisible");
	    */
	    
	    Image imagee = Toolkit.getDefaultToolkit().getImage("img/icones/hand_grab2.png");
	    curMainAgripper = Toolkit.getDefaultToolkit().createCustomCursor
        (imagee,  new Point(0, 0), "curseurInvisible"); 
	}
	
	
	/**
	 * Constructeur du panel du terrain
	 * 
	 * @param jeu Le jeu a gerer
	 */
	public Panel_Terrain(Jeu jeu, EcouteurDePanelTerrain edpt, Joueur joueur)
	{
		// sauvegarde du jeu
		this.jeu 	  = jeu;
		this.edpt     = edpt;
		this.joueur   = joueur;
		
		// proprietes du panel
		LARGEUR = jeu.getTerrain().getLargeur();
		HAUTEUR = jeu.getTerrain().getHauteur();
		setPreferredSize(new Dimension(LARGEUR,HAUTEUR));
		setFocusable(true);
		
		// ajout des ecouteurs
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		
		// demarrage du thread de rafraichissement de l'affichage
		thread = new Thread(this);
		thread.start();
	}
	
	/**
     * Permet de modifier la tour selectionnee depuis l'exterieur de l'objet
     * 
     * @param tour le tour a selectionnee
     */
    public void setTourSelectionnee(Tour tour)
    {
        tourSelectionnee = tour;
    }
	
	/**
	 * Permet de modifier la tour a ajouter depuis l'exterieur de l'objet
	 * 
	 * @param tour la tour sélectionnée
	 */
	public void setTourAAjouter(Tour tour)
	{
		
	    Point p = getCoordoneeSurTerrainOriginal(sourisCaseX, sourisCaseY);
	    
	    // mise a jour de la position de la tour
	    tour.setLocation(p.x, p.y);
		
	    // la tour devient la tour a ajouter
		tourAAjouter = tour;

		// s'il y a un tour a ajouter, il n'y pas de tour selectionnee !
		if(tourAAjouter != null)
			tourSelectionnee = null;
	}
	
	/**
	 * Permet de recuperer la creature selectionnee
	 * 
	 * @return la creature selectionnee
	 */
	public Creature getCreatureSelectionnee()
    {
        return creatureSelectionnee;
    }
	
	/**
     * Permet de modifier la creature selectionnee depuis l'exterieur de l'objet
     * 
     * @param creature la creature a selectionnee
     */
    public void setCreatureSelectionnee(Creature creature)
    {
        creatureSelectionnee = creature;
    }
	
	/**
     * Permet de tout deselectionner
     */
    public void toutDeselectionner()
    {
        tourAAjouter        = null;
        tourSelectionnee    = null;
    }
    
    /**
     * Permet de basculer de l'affichage au non affichage du maillage et vis versa.
     * @return l'etat actuel (true si afficher et false sinon)
     */
    public boolean basculerAffichageMaillage()
    {
        return afficherMaillage = !afficherMaillage;
    }
    
    /**
     * Permet de basculer de l'affichage au non affichage des rayons de portee 
     * et vis versa.
     * @return l'etat actuel (true si afficher et false sinon)
     */
    public boolean basculerAffichageRayonPortee()
    {
        return afficherRayonsDePortee = !afficherRayonsDePortee;
    }
	
	@Override
	public void paintComponent(Graphics g)
	{
	    Graphics2D g2 = (Graphics2D) g;

	    if(coeffTaille < 1)
	    {
	        decaleX = (int) (getSize().width/2.0 - LARGEUR*coeffTaille/2.0);
	        decaleY = (int) (getSize().height/2.0 - HAUTEUR*coeffTaille/2.0);
	    }
	    
	    g2.scale(coeffTaille, coeffTaille);
	    g2.translate(decaleX, decaleY);
	    
	    
	    //---------------------------
        //-- affichage de l'espace --
        //---------------------------
	    g2.setColor(LookInterface.COULEUR_DE_FOND_2);
	    g2.fillRect(-1000, -1000, 2000, 2000);
	    
	    
		//--------------------------
		//-- affichage du terrain --
		//--------------------------
		if(jeu.getTerrain().getImageDeFond() != null)
			// image de fond
			g2.drawImage(jeu.getTerrain().getImageDeFond(), 0, 0, null);
		else
		{
			// couleur de fond
			g2.setColor(COULEUR_FOND);
			g2.fillRect(0, 0, LARGEUR, HAUTEUR);
		}

		//-------------------------------------------------
		//-- Affichage de la zone de depart et d'arrivee --
		//-------------------------------------------------
		if(afficherMaillage)
		{
		    // couleur de fond
            g2.setColor(jeu.getTerrain().getCouleurDeFond());
            g2.fillRect(0, 0, LARGEUR, HAUTEUR);
		    
		    // modification de la transparence
		    setTransparence(ALPHA_SURFACE_ZONE_DA, g2);
			
			// dessin de la zone de depart
			g2.setColor(COULEUR_ZONE_DEPART);
			dessinerZone(joueur.getEquipe().getZoneDepartCreatures(0),g2);
			
			// dessin de la zone d'arrivee
			g2.setColor(COULEUR_ZONE_ARRIVEE);
			dessinerZone(joueur.getEquipe().getZoneArriveeCreatures(),g2);
			
			ArrayList<Rectangle> murs = jeu.getTerrain().getMurs();
			setTransparence(ALPHA_SURFACE_MUR, g2);
			g2.setColor(jeu.getTerrain().getCouleurMurs());
			for(Rectangle mur : murs)
			    dessinerZone(mur,g2);
			
		}
		
		//------------------------------------
		//-- Affichage du maillage (graphe) --
		//------------------------------------
		if(afficherMaillage)
		{	
		    // modification de la transparence
		    setTransparence(ALPHA_MAILLAGE, g2);
			
		    // recuperation de la liste des arcs actifs
			ArrayList<Line2D> arcsActifs = jeu.getTerrain().getArcsActifs();
			
			if(arcsActifs != null)
			{
			    // affichage des arcs actifs
			    g2.setColor(COULEUR_MAILLAGE);
				for(Line2D arc : arcsActifs) 
					g2.drawLine((int)arc.getX1(),(int)arc.getY1(),
							    (int)arc.getX2(),(int)arc.getY2());
			}
			
			// reinitialisation de la transparence
			setTransparence(1.f, g2);
		}
		
		//----------------------------------------
		//-- affichage des creatures terrestres --
		//----------------------------------------
		Creature creature;
        Enumeration<Creature> eCreatures = jeu.getGestionnaireCreatures().getCreatures().elements();
        while(eCreatures.hasMoreElements())
        {
            creature = eCreatures.nextElement();
                
            // affichage des creatures terriennes uniquement
            if(creature.getType() == Creature.TYPE_TERRIENNE)
                dessinerCreature(creature,g2);
        }
		
		//-------------------------
		//-- affichage des tours --
		//-------------------------
		for(Tour tour : jeu.getGestionnaireTours().getTours())
			dessinerTour(tour,g2,false);
		
	    //--------------------------------------
        //-- affichage des creatures aerienne --
        //--------------------------------------
		eCreatures = jeu.getGestionnaireCreatures().getCreatures().elements();
        while(eCreatures.hasMoreElements())
        {
            creature = eCreatures.nextElement();
            
            // dessine toutes les barres de sante
            dessinerBarreDeSante(creature, g2);
            
            if(creature.getType() == Creature.TYPE_AERIENNE)
                dessinerCreature(creature,g2);
        }
		
        
        traitTmp = g2.getStroke();
        
		//---------------------------------
		//-- entour la tour selectionnee --
		//---------------------------------
		if(tourSelectionnee != null)
		{
			dessinerPortee(tourSelectionnee,g2,COULEUR_RAYON_PORTEE);
			
			g2.setColor(COULEUR_SELECTION);
			g2.setStroke(TRAIT_TILLE);
			g2.drawRect(tourSelectionnee.getXi(), tourSelectionnee.getYi(),
					(int) (tourSelectionnee.getWidth()),
					(int) (tourSelectionnee.getHeight()));
		}
		
		//-------------------------------------
		//-- entour la creature selectionnee --
		//-------------------------------------
		if(creatureSelectionnee != null)
		{
			g2.setColor(COULEUR_SELECTION);
			g2.setStroke(TRAIT_TILLE);
			g2.drawOval((int) (creatureSelectionnee.getX()), 
						(int) (creatureSelectionnee.getY()),
						(int) creatureSelectionnee.getWidth(),
						(int) creatureSelectionnee.getHeight());
			
			// dessine son chemin
			setTransparence(ALPHA_CHEMIN_CREATURE,g2);
			dessinerCheminCreature(creatureSelectionnee,g2);
			
			if(centrerSurCreatureSelectionnee )
			    centrerSur((int) creatureSelectionnee.getX(),
			               (int) creatureSelectionnee.getY());
			
			setTransparence(1.f,g2);
		}
		
		g2.setStroke(traitTmp);
		
		//------------------------------------
		//-- affichage des rayons de portee --
		//------------------------------------
		if(afficherRayonsDePortee)
			for(Tour tour : jeu.getGestionnaireTours().getTours())
				dessinerPortee(tour,g2,COULEUR_RAYON_PORTEE);
		
		//------------------------------
		//-- affichage des animations --
		//------------------------------
		jeu.getGestionnaireAnimations().dessinerAnimations(g2);
		
		//------------------------------------
		//-- affichage de la tour a ajouter --
		//------------------------------------
		if(tourAAjouter != null && sourisSurTerrain)
		{
		    // modification de la transparence
		    setTransparence(ALPHA_TOUR_A_AJOUTER,g2);
		    
		    // dessin de la tour
			dessinerTour(tourAAjouter,g2,false);
			
			// positionnnable ou non
			if(!jeu.getGestionnaireTours().laTourPeutEtrePosee(tourAAjouter))
				dessinerPortee(tourAAjouter,g2,COULEUR_POSE_IMPOSSIBLE);
			else
				// affichage du rayon de portee
				dessinerPortee(tourAAjouter,g2,COULEUR_RAYON_PORTEE);
		}
		
		//-----------------------------
        //-- affichage du mode pause --
        //-----------------------------
		if(jeu.estEnPause())
	    {
            setTransparence(0.3f, g2);
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, 0, LARGEUR, HAUTEUR);
            setTransparence(1.0f, g2);
            
            g2.setColor(Color.WHITE);
            g2.setFont(GestionnaireDesPolices.POLICE_TITRE);
            g2.drawString("[ EN PAUSE ]", LARGEUR / 2 - 50, HAUTEUR / 2 - 50);
	    }
	}

	/**
	 * Permet de dessiner une zone rectangulaire sur le terrain.
	 * 
	 * @param zone la zone rectangulaire
	 * @param g2 le Graphics2D pour dessiner
	 */
	private void dessinerZone(final Rectangle zone, final Graphics2D g2)
    {
        g2.fillRect((int) zone.getX(), 
                    (int) zone.getY(), 
                    (int) zone.getWidth(), 
                    (int) zone.getHeight());
    }
	
	/**
	 * Permet de dessiner une creature sur le terrain.
	 * 
	 * @param creature la creature a dessiner
	 * @param g2 le Graphics2D pour dessiner
	 */
	private void dessinerCreature(final Creature creature, final Graphics2D g2)
	{  
	    if(creature.getImage() != null)
	    {
	        // rotation des créatures
	        AffineTransform tx = new AffineTransform();
	        tx.translate(creature.getX()+creature.getWidth()/2, creature.getY()+creature.getHeight()/2);
	        tx.rotate(creature.getAngle()+Math.PI/2);
	        tx.translate(-creature.getWidth()/2, -creature.getHeight()/2);
	        
	        // dessin de la créature avec rotation
	        g2.drawImage(creature.getImage(), tx, this);
	        
	        /*
	        // affichage de l'image de la creature au centre de sa position
            g2.drawImage(creature.getImage(),
                    (int) creature.getX(), 
                    (int) creature.getY(), 
                    (int) creature.getWidth(), 
                    (int) creature.getHeight(), null);*/
	    }
        else
        {
            // affichage d'un cercle au centre de la position de la creature
            g2.setColor(COULEUR_CREATURE_SANS_IMAGE);
            g2.fillOval((int) creature.getX(), 
                        (int) creature.getY(),
                        (int) creature.getWidth(), 
                        (int) creature.getHeight());
        }
	    
        // affichage du chemin des creatures
        if(afficherMaillage)
        {
            g2.setColor(COULEUR_CHEMIN);
            dessinerCheminCreature(creature,g2);
        }  
	}
	
	
	/**
	 * Permet de dessiner la barre de sante d'une creature.
	 * 
	 * @param creature la creature correspondante
     * @param g2 le Graphics2D pour dessiner
	 */
	private void dessinerBarreDeSante(final Creature creature, final Graphics2D g2)
	{
	    // calculs des proprietes
	    int largeurBarre    = (int) (creature.getWidth() * COEFF_LARGEUR_BARRE_VIE);
        int positionXBarre  = (int) ( creature.getX() - 
                              (largeurBarre - creature.getWidth()) / 2);
        int positionYBarre  = (int)(creature.getY()+creature.getHeight());
        
        // affichage du conteneur
        g2.setColor(COULEUR_CONTENEUR_SANTE);
        g2.fillRect(positionXBarre,positionYBarre, 
                    largeurBarre, HAUTEUR_BARRE_VIE);
        
        // affichage du contenu
        g2.setColor(COULEUR_SANTE);
        
        g2.fillRect(positionXBarre+1, positionYBarre+1, 
                (int)(creature.getSante()*(largeurBarre - 2)/creature.getSanteMax()),
                HAUTEUR_BARRE_VIE-2);
	}
	
	/**
	 * Permet de dessiner le chemin d'une creature.
	 * 
	 * @param creature la creature concernee
	 * @param g2 le Graphics2D pour dessiner
	 */
    private void dessinerCheminCreature(final Creature creature, final Graphics2D g2)
    {
    	// recuperation du chemin
        ArrayList<Point> chemin = creature.getChemin();
        
        // s'il est valide
        if(chemin != null && chemin.size() > 0)
        {
            // initialisation du point precedent
            Point PointPrecedent = chemin.get(creature.getIndiceCourantChemin()-1);
            
            // bloque la reference du chemin
            synchronized(chemin)
            {
                // pour chaque point du chemin
                Point point;
                for(int i=creature.getIndiceCourantChemin();i<chemin.size();i++)
                {
                    /* 
                     * affichage du segment de parcours 
                     * entre le point precedent et la suivant
                     */
                    point = chemin.get(i);
                    
                    g2.drawLine(PointPrecedent.x, PointPrecedent.y, 
                                point.x, point.y);
                    PointPrecedent = point;
                }
            }
        }
    }
	
	/**
	 * Permet de dessiner une tour
	 * 
	 * @param tour la tour a dessiner
	 * @param g2 le Graphics2D pour dessiner
	 * @param avecPortee dessiner ou non la portee de la tour ?
	 */
	private void dessinerTour(final Tour tour,
							  final Graphics2D g2,
							  final boolean avecPortee)
	{
		// dessin de l'image
		if(tour.getImage() != null)
			g2.drawImage(tour.getImage(), tour.getXi(), tour.getYi(), 
					(int)tour.getWidth(), 
					(int)tour.getHeight(),null);
		// dessin d'un forme de couleur
		else
		{	 
			g2.setColor(tour.getCouleurDeFond());
			g2.fillRect(tour.getXi(), tour.getYi(), 
				(int)tour.getWidth(), 
				(int)tour.getHeight());
		}
		
		// dessin du niveau actuelle de la tour (petits carres)
		for(int i=0;i < tour.getNiveau() - 1;i++)
		{
			g2.setColor(COULEUR_NIVEAU_PERIMETRE);
			g2.fillRect(tour.getXi() + 4 * i + 1, 
			        (int) (tour.getYi() + tour.getHeight() - 4), 3,3);
			g2.setColor(COULEUR_NIVEAU);
			g2.fillRect(tour.getXi() + 4 * i + 2, 
			        (int) (tour.getYi() + tour.getHeight() - 3), 1,1);
		}
		// dessin de la portee
		if(avecPortee)
			dessinerPortee(tour,g2,COULEUR_RAYON_PORTEE);
	}
	
	/**
	 * Permet de dessiner le rayon de portee d'une tour
	 * 
	 * @param tour la tour concernee
	 * @param g2 le Graphics2D pour dessiner
	 */
	private void dessinerPortee(Tour tour,Graphics2D g2, Color couleurRayonDePortee)
	{
        // affichage du perimetre du rayon de portee
	    setTransparence(ALPHA_PERIMETRE_PORTEE,g2);
        g2.setColor(couleurRayonDePortee);
		g2.drawOval((int)(tour.getXi() - tour.getRayonPortee() + tour.getWidth()/2), 
					(int)(tour.getYi() - tour.getRayonPortee() + tour.getHeight()/2), 
					(int)tour.getRayonPortee()*2, 
					(int)tour.getRayonPortee()*2);

		// affichage de la surface du rayon de portee
		setTransparence(ALPHA_SURFACE_PORTEE,g2);
        g2.setColor(couleurRayonDePortee);
        g2.fillOval((int)(tour.getXi() - tour.getRayonPortee() + tour.getWidth()/2), 
        			(int)(tour.getYi() - tour.getRayonPortee() + tour.getHeight()/2), 
        			(int)tour.getRayonPortee()*2, 
        			(int)tour.getRayonPortee()*2);
        
        // remet la valeur initial
        setTransparence(1.f,g2);
	}
	
	/**
	 * Permet de modifier la transparence du Graphics2D
	 * 
	 * @param tauxTransparence le taux (1.f = 100% opaque et 0.f = 100% transparent)
	 * @param g2 le Graphics2D a configurer
	 */
	private void setTransparence(float tauxTransparence, Graphics2D g2)
    {
	    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, tauxTransparence));
    }
	
	/**
	 * Méthode de refraichissement du panel
	 * 
	 * L'implémentation de Runnable nous force à définir cette méthode.
	 * Celle-ci sera appelée lors du démarrage du thread.
	 * 
	 * @see Runnable
	 */
	@Override
	public void run()
	{
		// Tant que la partie est en cours...
		while(!joueur.aPerdu())
		{
			// Raffraichissement du panel
			repaint(); // -> appel paint
			
			// Endore le thread
			try {
				Thread.sleep(TEMPS_REPOS_THREAD);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Métode de gestion des cliques de la souris
	 * 
	 * @param me l'evenement lie a cette action 
	 * @see MouseListener
	 */
	@Override
	public void mousePressed(MouseEvent me)
	{
	    boutonDragg = me.getButton();
	    
	    // clique gauche
	    if (me.getButton() == MouseEvent.BUTTON1)
		{
	        
	            
	        sourisGrabX = me.getX();
	        sourisGrabY = me.getY();
	        
	        decaleGrabX = decaleX;
	        decaleGrabY = decaleY;
	        
	        
	        Point positionSurTerrain = getCoordoneeSurTerrainOriginal(sourisX,sourisY);
	        
	        
	        //--------------------------
	        //-- selection d'une tour --
	        //--------------------------
	        
	        // la selection se fait lors du clique
			for(Tour tour : jeu.getGestionnaireTours().getTours()) // pour chaque tour... 
				if (tour.intersects(positionSurTerrain.x,positionSurTerrain.y,1,1)) // la souris est dedans ?
				{	
					// si le joueur clique sur une tour deja selectionnee
				    if (tourSelectionnee == tour)
						tourSelectionnee = null; // deselection
					else
					{
						tourSelectionnee = tour; // la tour est selectionnee
						// si une tour est selectionnee, il n'y pas d'ajout
						tourAAjouter = null;  
					}
					
				    // informe la fenetre qu'une tour a ete selectionnee
					edpt.tourSelectionnee(tourSelectionnee,
											Panel_InfoTour.MODE_SELECTION);
					return;
				}
		
	        // aucun tour trouvee => clique dans le vide.
            tourSelectionnee = null;
            
            edpt.tourSelectionnee(tourSelectionnee,
                    Panel_InfoTour.MODE_SELECTION);
			
            //------------------------------
            //-- selection d'une creature --
            //------------------------------

			Creature creature;
			Vector<Creature> creatures = jeu.getGestionnaireCreatures().getCreatures();
			
			// parcours a l'envers car il faut traiter les creatures les plus
            // devant en premier (les derniers affiches)
			for(int i = creatures.size()-1; i >= 0 ;i--)
			{
			    creature = creatures.get(i);

			    if (creature.intersects(positionSurTerrain.x,positionSurTerrain.y,1,1)) // la souris est dedans ?
				{	
			        // on enleve le suivi de la creature
			        centrerSurCreatureSelectionnee = false;
			        
			        // si le joueur clique sur une creature deja selectionnee
			        if (creatureSelectionnee == creature)
						creatureSelectionnee = null; // deselection
			        
					else
						creatureSelectionnee = creature; // la creature est selectionnee
					
					edpt.creatureSelectionnee(creatureSelectionnee);
					
					return;
				}
			}
			
			// aucun creature trouvee => clique dans le vide.
			creatureSelectionnee = null;
			edpt.creatureSelectionnee(creatureSelectionnee);
		}
		else // clique droit ou autre
		{
			// deselection total
			tourSelectionnee 	 = null;
			tourAAjouter 		 = null;
			creatureSelectionnee = null;
			
			edpt.tourSelectionnee(tourSelectionnee,
                    Panel_InfoTour.MODE_SELECTION);
			edpt.creatureSelectionnee(creatureSelectionnee);
		}
	}
	
	/**
	 * Métode de gestion des relachements du clique de la souris
	 * 
	 * @param me l'evenement lie a cette action 
	 */
	@Override
	public void mouseReleased(MouseEvent me)
	{
	    if(!jeu.estEnPause())
        {
    	    // l'ajout se fait lors de la relache du clique
    		if(tourAAjouter != null)
    		{
    			edpt.acheterTour(tourAAjouter);
    			setCursor(curNormal);
    			
    			// informe la fenetre qu'une tour a ete selectionnee
                edpt.tourSelectionnee(tourAAjouter, Panel_InfoTour.MODE_SELECTION);
    		}
        }
	}
	
	/**
	 * Méthode de gestion des deplacements de la souris
	 * 
	 * @param me evenement lie a cette action
	 * @see MouseMotionListener
	 */
	@Override
	public void mouseMoved(MouseEvent me)
	{
		// mise a jour des coordonees de la souris
		sourisX = me.getX();
		sourisY = me.getY();
		
		// mise a jour de la position de la souris sur le grillage vituel
        sourisCaseX = getPositionSurQuadrillage(sourisX);
        sourisCaseY = getPositionSurQuadrillage(sourisY);
		
        
        //---------------------------
        //-- gestion des decalages --
        //---------------------------
        setCursor(curNormal);
		
        // les décalage psr les bordures marchent uniquement lors de l'ajout 
        // d'une tour
        if(tourAAjouter != null)
        {
        
    		if(sourisX > 0 
    		&& sourisX < MARGES_DEPLACEMENT
    		&& decaleX < 0)
    		{
                setCursor(curRedimGauche);
    		    decaleX++;
    		}
    		
    		if(sourisX > LARGEUR-MARGES_DEPLACEMENT 
    		&& sourisX < LARGEUR 
    		&& getCoordoneeSurTerrainOriginal(LARGEUR,0).x < LARGEUR)
    		{
    		    setCursor(curRedimDroite);
    		    decaleX--;
    		} 
    		
    		if(sourisY > 0 
            && sourisY < MARGES_DEPLACEMENT
            && decaleY < 0) 
    		{
                setCursor(curRedimBas);
                decaleY++;
    		}
    		
    		if(sourisY > HAUTEUR-MARGES_DEPLACEMENT 
            && sourisY < HAUTEUR 
            && getCoordoneeSurTerrainOriginal(0,HAUTEUR).y < HAUTEUR)
    		{
                setCursor(curRedimHaut);    
                decaleY--;
    		}
    
    		
		    // mise a jour de la position de la tour à ajoutée   
		    // pour eviter des pertes de précision,on récupère d'abord 
		    // la position sur le terrain de taille normal...
		    Point p = getCoordoneeSurTerrainOriginal(sourisX, sourisY);
		    
		    // ... puis on calcul la position sur le quadrillage
		    tourAAjouter.setLocation(
		        getPositionSurQuadrillage(p.x), 
		        getPositionSurQuadrillage(p.y)
		        );
		}
	}
	
	/**
	 * Permet de recupérer la case sur le quadrillage d'une position.
	 * 
	 * @param position une position X ou Y
	 * @return la case sur le quadrillage carré
	 */
	public int getPositionSurQuadrillage(int position)
	{
	    return Math.round(position/CADRILLAGE)*CADRILLAGE;
	}
	
	/**
     * Permet de faire correspondre une coordonnée donnée sur la position normale
     * du terrain (sans zoom et décalage)
     * 
	 * @param x la position x du point
	 * @param y la position y du point
	 * @return le point correspondant sur le terrain de taille normal
	 */
	private Point getCoordoneeSurTerrainOriginal(int x, int y)
	{
	    return new Point((int)(x / coeffTaille - decaleX), 
	                     (int)(y / coeffTaille - decaleY));
	}
	
	/**
     * Methode de gestion du clique enfoncé de la souris lorsque qu'elle bouge.
     * 
     * @param me evenement lie a cette action
     * @see MouseMotionListener
     */
	@Override
    public void mouseDragged(MouseEvent me)
    {
	    if(boutonDragg == MouseEvent.BUTTON1)
	    {
	        // si il n'y a pas de tour a ajouter, c'est comme si elle bougeait normalement 
	        if(tourAAjouter != null)
	            mouseMoved(me);
	   
            // si rien n'est selectionner, on autorise le grab
            else
            {
                setCursor(curMainAgripper);
                
                decaleX = decaleGrabX - (sourisGrabX - me.getX()) / 2;
                decaleY = decaleGrabY - (sourisGrabY - me.getY()) / 2;
            }
        }
    }

	/**
	 * Methode de gestion de la souris lorsque qu'elle entre dans le panel
	 * 
	 * @param me evenement lie a cette action
	 * @see MouseMotionListener
	 */
    @Override
	public void mouseEntered(MouseEvent me)
	{
		sourisSurTerrain = true;
		
		//if(tourAAjouter != null)
		//    setCursor(curseurTransparent);

		// recuperation du focus. 
		// /!\ Important pour la gestion des touches clavier /!\
		requestFocusInWindow(true); 
	}
	
	/**
	 * Methode de gestion de la souris lorsque qu'elle sort du panel
	 * 
	 * @param me evenement lie a cette action
	 * @see MouseMotionListener
	 */
	@Override
	public void mouseExited(MouseEvent me)
	{
		sourisSurTerrain = false;
	}
	
	// methodes non redéfinies (voir MouseListener)
	public void mouseClicked(MouseEvent me){}
	
	/**
	 * Methode de gestion des evenements lors de la relache d'une touche
	 */
	@Override
	public void keyReleased(KeyEvent ke)
	{
		if(!jeu.estEnPause())
		{
    	    // raccourcis des tours
    	    if(tourSelectionnee != null)
    		{
    	        // raccourci de vente
    			if(ke.getKeyChar() == 'v' || ke.getKeyChar() == 'V')
    				edpt.vendreTour(tourSelectionnee);
    			// raccourci d'amelioration
    			else if(ke.getKeyChar() == 'a' || ke.getKeyChar() == 'A')
    				edpt.ameliorerTour(tourSelectionnee);
    		}
    	    
    	    // focus sur la creature
    	    if(ke.getKeyChar() == 'f' && creatureSelectionnee != null)
    	        centrerSurCreatureSelectionnee = true;
    	    
    		// raccourci lancer vague suivante
            if(Character.isSpaceChar(ke.getKeyChar()))  
                edpt.lancerVagueSuivante();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent ke)
	{}
	
	@Override
	public void keyTyped(KeyEvent ke){}

	@Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        // recupère le point avant le changement d'echelle
        Point pAvant = getCoordoneeSurTerrainOriginal(sourisX, sourisY);
        
        // adaptation de l'echelle
        if(e.getWheelRotation() < 0 || coeffTaille > 1.0) // zoom
            coeffTaille -= e.getWheelRotation()*ETAPE_ZOOM;
        //else if() // dézoom
        //    coeffTaille -= e.getWheelRotation()*ETAPE_ZOOM;

        // recupère le point après le changement d'echelle
        Point pAprès = getCoordoneeSurTerrainOriginal(sourisX, sourisY);
        
        // adapte le décalage pour que le point ciblé reste au même
        // endroit en proportion de du panel
        decaleX +=  pAprès.x - pAvant.x;
        decaleY +=  pAprès.y - pAvant.y;
    }
    
    /**
     * Permet de centrer le terrain sur une coordonnée du terrain de taille
     * normale.
     * 
     * @param x la position sur le terrain normal
     * @param y la position sur le terrain normal
     */
    private void centrerSur(int x, int y)
    {
        // TODO marche pas vraiment avec les zooms
        decaleX = (int) (getSize().width/2.0 - x * coeffTaille);
        decaleY = (int) (getSize().height/2.0 - y * coeffTaille);
    }
    
}
