package vues;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.*;
import javax.swing.*;
import models.animations.*;
import models.creatures.Creature;
import models.jeu.Jeu;
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
													 KeyListener
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
	private static final int CADRILLAGE    = 10; // unite du cadriallage en pixel
	
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
	
	// 0.0f = 100% transparent et 1.0f vaut 100% opaque.
	private static final float ALPHA_PERIMETRE_PORTEE = .6f;
	private static final float ALPHA_SURFACE_PORTEE   = .3f;
	private static final float ALPHA_MAILLAGE   	  = .4f;
	private static final float ALPHA_SURFACE_ZONE_DA  = .5f;
	private static final float ALPHA_TOUR_A_AJOUTER   = .7f;
	
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
	 * Reference vers la fenetre parent
	 */
	private Fenetre_Jeu fenJeu;
	
	/**
	 * Permet d'afficher ou non les elements invisible (maillage, chemins, etc.)
	 */
	private boolean afficherMaillage;
	
	/**
	 * Permet d'afficher ou non tous les rayons de portee des tours
	 */
	private boolean afficherRayonsDePortee;
	
	/**
	 * Constructeur du panel du terrain
	 * 
	 * @param jeu Le jeu a gerer
	 */
	public Panel_Terrain(Jeu jeu,Fenetre_Jeu fenJeu)
	{
		// sauvegarde du jeu
		this.jeu 	= jeu;
		this.fenJeu = fenJeu;
		
		// proprietes du panel
		LARGEUR = jeu.getLargeurTerrain();
		HAUTEUR = jeu.getHauteurTerrain();
		setPreferredSize(new Dimension(LARGEUR,HAUTEUR));
		setFocusable(true);
		
		// ajout des ecouteurs
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

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
		// mise a jour de la position de la tour
	    tour.setLocation(sourisCaseX, sourisCaseY);
		
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
	
	/**
	 * Surdéfinition de la méthode d'affichage du panel.
	 * 
	 * Cette methode affiche la scene du jeu. Elle recupere differents 
	 * elements du jeu et les affiches.
	 * 
	 * @param g Le graphics du panel pour dessin
	 */
	public void paint(Graphics g)
	{
	    Graphics2D g2 = (Graphics2D) g;

		//--------------------------
		//-- affichage du terrain --
		//--------------------------
		if(jeu.getImageDeFondTerrain() != null)
			// image de fond
			g2.drawImage(jeu.getImageDeFondTerrain(), 0, 0, null);
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
			// modification de la transparence
		    setTransparence(ALPHA_SURFACE_ZONE_DA, g2);
			
			// dessin de la zone de depart
			g2.setColor(COULEUR_ZONE_DEPART);
			dessinerZone(jeu.getZoneDepart(),g2);
			
			// dessin de la zone d'arrivee
			g2.setColor(COULEUR_ZONE_ARRIVEE);
			dessinerZone(jeu.getZoneArrivee(),g2);
		}
		
		//------------------------------------
		//-- Affichage du maillage (graphe) --
		//------------------------------------
		if(afficherMaillage)
		{	
		    // modification de la transparence
		    setTransparence(ALPHA_MAILLAGE, g2);
			
		    // recuperation de la liste des arcs actifs
			ArrayList<Line2D> arcsActifs = jeu.getArcsActifs();
			
			if(arcsActifs != null)
			{
			    // affichage des arcs actifs
			    g2.setColor(COULEUR_MAILLAGE);
				for(Line2D arc : arcsActifs) 
					g2.drawLine((int)arc.getX1(),(int)arc.getY1(),
							    (int)arc.getX2(),(int)arc.getY2());
			}
			
			/*
			// affichage des noeuds actifs ou non
			ArrayList<Noeud> noeuds = jeu.getNoeuds();
			for(Noeud n : noeuds)
			{
				if(n.isActif())
				    g2.setColor(Color.GREEN);
				else
					g2.setColor(Color.RED);
				
				g2.drawRect((int)n.getX(),(int)n.getY(),1,1);
			}
			*/
			
			// reinitialisation de la transparence
			setTransparence(1.f, g2);
		}
		
		//----------------------------------------
		//-- affichage des creatures terrestres --
		//----------------------------------------
		Creature creature;
		synchronized (jeu.getCreatures())
        {
    		Iterator<Creature> iCreatures = jeu.getCreatures().iterator();
            while(iCreatures.hasNext())
            {
                creature = iCreatures.next();
                
                /* efface les creatures mortes
                 * 
                 * TODO on peut faire mieux mais ca résout 
                 * les problèmes de créatures fantomes
                 */
                if(creature.estMorte())
                {
                    iCreatures.remove();
                    System.out.println("Panel_Terrain effacement d'une creature mal tuee");
                    continue;
                }
                  
                // affichage des creatures terriennes uniquement
                if(creature.getType() == Creature.TYPE_TERRIENNE)
                    dessinerCreature(creature,g2);
            }
        }
		
		//-------------------------
		//-- affichage des tours --
		//-------------------------
		for(Tour tour : jeu.getTours())
			dessinerTour(tour,g2,false);
		
	    //--------------------------------------
        //-- affichage des creatures aerienne --
        //--------------------------------------
		Enumeration<Creature> eCreatures = jeu.getCreatures().elements();
        while(eCreatures.hasMoreElements())
        {
            creature = eCreatures.nextElement();
            
            // dessine toutes les barres de sante
            dessinerBarreDeSante(creature, g2);
            
            if(creature.getType() == Creature.TYPE_AERIENNE)
                dessinerCreature(creature,g2);
        }
		
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
		}
		
		//------------------------------------
		//-- affichage des rayons de portee --
		//------------------------------------
		if(afficherRayonsDePortee)
			for(Tour tour : jeu.getTours())
				dessinerPortee(tour,g2,COULEUR_RAYON_PORTEE);
		
		//------------------------------
		//-- affichage des animations --
		//------------------------------
		synchronized (jeu.getAnimations())
        {
		    Animation animation;
		    Iterator<Animation> iAnimation = jeu.getAnimations().iterator();
    		while(iAnimation.hasNext())
    		{
    		    animation = iAnimation.next();
    	
    		    if (!animation.estTerminee())   
                    animation.dessiner(g2);
                else
                    iAnimation.remove();
    		} 
        }
		
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
			if(!jeu.laTourPeutEtrePosee(tourAAjouter))
				dessinerPortee(tourAAjouter,g2,COULEUR_POSE_IMPOSSIBLE);
			else
				// affichage du rayon de portee
				dessinerPortee(tourAAjouter,g2,COULEUR_RAYON_PORTEE);
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
	        // TODO
	        // comment faire une rotation ?
	        //AffineTransform tx = new AffineTransform();
	        //double radians = -Math.PI/4;
	        //tx.rotate(radians);
	        //g2.drawImage(terrain.getImageDeFond(), tx, this);
	        
	        // affichage de l'image de la creature au centre de sa position
            g2.drawImage(creature.getImage(),
                    (int) creature.getX(), 
                    (int) creature.getY(), 
                    (int) creature.getWidth(), 
                    (int) creature.getHeight(), null);
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
	    
	    // affichage des barres de sante
        // dessinerBarreDeSante(creature,g2);
        
        // affichage du chemin des creatures
        if(afficherMaillage)
            dessinerCheminCreature(creature,g2);
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
            Point PointPrecedent = chemin.get(0);
            
            // TODO control de la concurrence
            // bloque la reference du chemin
            synchronized(chemin)
            {
                // pour chaque point du chemin
                Point point;
                for(int i=1;i<chemin.size();i++)
                {
                    /* 
                     * affichage du segment de parcours 
                     * entre le point precedent et la suivant
                     */
                    point = chemin.get(i);
                    
                    g2.setColor(COULEUR_CHEMIN);
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
	public void run()
	{
		// Tant que la partie est en cours...
		while(true)
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
	public void mousePressed(MouseEvent me)
	{
		
	    // clique gauche
	    if (me.getButton() == MouseEvent.BUTTON1)
		{
			//--------------------------
	        //-- selection d'une tour --
	        //--------------------------
	        
	        // la selection se fait lors du clique
			for(Tour tour : jeu.getTours()) // pour chaque tour... 
				if (tour.intersects(sourisX,sourisY,1,1)) // la souris est dedans ?
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
					fenJeu.tourSelectionnee(tourSelectionnee,
											Panel_InfoTour.MODE_SELECTION);
					return;
				}
		
	        // aucun tour trouvee => clique dans le vide.
            tourSelectionnee = null;
            
            fenJeu.tourSelectionnee(tourSelectionnee,
                    Panel_InfoTour.MODE_SELECTION);
			
            //------------------------------
            //-- selection d'une creature --
            //------------------------------

			Creature creature;
			Vector<Creature> creatures = jeu.getCreatures();
			
			// parcours a l'envers car il faut traiter les creatures les plus
            // devant en premier (les derniers affiches)
			for(int i = creatures.size()-1; i >= 0 ;i--)
			{
			    creature = creatures.get(i);
			    if (creature.intersects(sourisX,sourisY,1,1)) // la souris est dedans ?
				{	
			        // si le joueur clique sur une creature deja selectionnee
			        if (creatureSelectionnee == creature)
						creatureSelectionnee = null; // deselection
					else
						creatureSelectionnee = creature; // la creature est selectionnee
					
					fenJeu.creatureSelectionnee(creatureSelectionnee);
					
					return;
				}
			}
			
			// aucun creature trouvee => clique dans le vide.
			creatureSelectionnee = null;
			fenJeu.creatureSelectionnee(creatureSelectionnee);
		}
		else // clique droit ou autre
		{
			// deselection total
			tourSelectionnee 	 = null;
			tourAAjouter 		 = null;
			creatureSelectionnee = null;
			
			fenJeu.tourSelectionnee(tourSelectionnee,
                    Panel_InfoTour.MODE_SELECTION);
			fenJeu.creatureSelectionnee(creatureSelectionnee);
		}
	}
	
	/**
	 * Métode de gestion des relachements du clique de la souris
	 * 
	 * @param me l'evenement lie a cette action 
	 */
	public void mouseReleased(MouseEvent me)
	{
		// l'ajout se fait lors de la relache du clique
		if(tourAAjouter != null)
			fenJeu.acheterTour(tourAAjouter);
	}
	
	/**
	 * Méthode de gestion des deplacements de la souris
	 * 
	 * @param me evenement lie a cette action
	 * @see MouseMotionListener
	 */
	public void mouseMoved(MouseEvent me)
	{
		// mise a jour des coordonees de la souris
		sourisX = me.getX();
		sourisY = me.getY();
		
		// mise a jour de la position de la souris sur le grillage vituel
		sourisCaseX = Math.round(me.getX()/CADRILLAGE)*CADRILLAGE;
		sourisCaseY = Math.round(me.getY()/CADRILLAGE)*CADRILLAGE;
		
		if(tourAAjouter != null)
			tourAAjouter.setLocation(sourisCaseX, sourisCaseY);
	}
	
	/**
     * Methode de gestion du clique enfoncé de la souris lorsque qu'elle bouge.
     * 
     * @param me evenement lie a cette action
     * @see MouseMotionListener
     */
    public void mouseDragged(MouseEvent me)
    {
       // pour nous c'est comme si elle bougeait normalement
       mouseMoved(me);
    }

	/**
	 * Methode de gestion de la souris lorsque qu'elle entre dans le panel
	 * 
	 * @param me evenement lie a cette action
	 * @see MouseMotionListener
	 */
	public void mouseEntered(MouseEvent me)
	{
		sourisSurTerrain = true;
		
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
	public void mouseExited(MouseEvent me)
	{
		sourisSurTerrain = false;
	}
	
	// methodes non redéfinies (voir MouseListener)
	public void mouseClicked(MouseEvent me){}
	
	/**
	 * Methode de gestion des evenements lors de la relache d'une touche
	 */
	public void keyReleased(KeyEvent ke)
	{
		// raccourcis des tours
	    if(tourSelectionnee != null)
		{
			// raccourci de vente
			if(ke.getKeyChar() == 'v' || ke.getKeyChar() == 'V')
				fenJeu.vendreTour(tourSelectionnee);
			// raccourci d'amelioration
			else if(ke.getKeyChar() == 'a' || ke.getKeyChar() == 'A')
				fenJeu.ameliorerTour(tourSelectionnee);
		}
		
		// raccourci lancer vague suivante
        if(Character.isSpaceChar(ke.getKeyChar()))
            fenJeu.lancerVagueSuivante();
	}
	
	public void keyPressed(KeyEvent ke){}
	public void keyTyped(KeyEvent ke){}
}
