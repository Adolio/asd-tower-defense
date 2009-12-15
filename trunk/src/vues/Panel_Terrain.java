package vues;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import models.creatures.Creature;
import models.jeu.Jeu;
import models.tours.Tour;

/**
 * Panel d'affichage du terrain de jeu.
 * 
 * Ce panel affiche la zone de jeu en elle-même.
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
	private static final int HAUTEUR_BARRE_VIE = 4; // pixels
	
	// % de la largeur de la creature
	private static final float COEFF_LARGEUR_BARRE_VIE = 1.5f; // 150%

	private static final int CADRILLAGE = 10; // unite du cadriallage en pixel
	private static final float [] DASHES = {2.0F, 2.0F}; // trait tillé
	
	// 0.0f = 100% transparent et 1.0f vaut 100% opaque.
	private static final float ALPHA_PERIMETRE_PORTEE = .6f;
	private static final float ALPHA_SURFACE_PORTEE   = .3f;
	private static final float ALPHA_MAILLAGE   	  = .4f;
	private static final float ALPHA_SURFACE_ZONE_DA  = .5f;
	
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
	
	private static final BasicStroke TRAIT_TILLE = new BasicStroke(
			1.0f,BasicStroke.CAP_ROUND, 
            BasicStroke.JOIN_MITER, 
            10.0F, DASHES, 0.F);

	

	
	
	//------------
	//-- thread --
	//------------
	private Thread thread;
	private int TEMPS_REPOS_THREAD = 40;
	
	// souris
	private int sourisX, sourisY, // position reelle
				sourisCaseX, sourisCaseY; // position sur le cadriallage virtuel
	
	private boolean sourisSurTerrain;
	
	// gestion des interactions avec les tours 
	/**
	 * Le terrain permet de choisir la tour a poser sur le terrain
	 * Si cette variable est non nulle et que le joueur clique sur le
	 * terrain, la tour a ajouter sera posée.
	 */
	private Tour tourAAjouter;
	
	/**
	 * Lorsque le joueur clique sur une tour, elle devient selectionnee.
	 * Une fois selectionnee des informations sur la tour apparaissent
	 * dans le menu d'interaction. Le joueur pourra alors améliorer ou 
	 * detruire la tour.
	 */
	private Tour tourSelectionnee;
	
	// le jeu a gerer
	private Jeu jeu;
	
	private boolean afficherMaillage; // affichage du graphe ?
	
	// affichage de tous les rayons de portee ?
	private boolean afficherRayonsDePortee;
	
	private Fenetre_Jeu fenJeu;

	private ArrayList<Animation> animations = new ArrayList<Animation>();

	
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
		
		LARGEUR = jeu.getLargeurTerrain();
		HAUTEUR = jeu.getHauteurTerrain();
		// propriete du panel
		setPreferredSize(new Dimension(LARGEUR,HAUTEUR));
		
		// ajout des ecouteurs
		addKeyListener(this);
		setFocusable(true);

		addMouseListener(this);
		addMouseMotionListener(this);
		
		
		// demarrage du thread de rafraichissement
		thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * Permet de modifier la tour a ajouter sur le terrain
	 * 
	 * @param tour la tour sélectionnée
	 */
	public void setTourAAjouter(Tour tour)
	{
		tour.setLocation(sourisCaseX, sourisCaseY);
		
		tourAAjouter = tour;

		if(tourAAjouter != null)
			tourSelectionnee = null;
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
		
		// comment faire une rotation ?
        //AffineTransform tx = new AffineTransform();
        //double radians = -Math.PI/4;
        //tx.rotate(radians);
		//g2.drawImage(terrain.getImageDeFond(), tx, this);
		
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
			// affichage des surfaces
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA_SURFACE_ZONE_DA));
			
			g2.setColor(COULEUR_ZONE_DEPART);
			Rectangle zoneDepart = jeu.getZoneDepart();
			g2.fillRect((int)zoneDepart.getX(), 
						(int) zoneDepart.getY(), 
						(int)zoneDepart.getWidth(), 
						(int)zoneDepart.getHeight());
			
			g2.setColor(COULEUR_ZONE_ARRIVEE);
			Rectangle zoneArrivee = jeu.getZoneArrivee();
			g2.fillRect((int) zoneArrivee.getX(), 
						(int) zoneArrivee.getY(), 
						(int)zoneArrivee.getWidth(), 
						(int)zoneArrivee.getHeight());
		}
		
		//-------------------------------------
		//-- Affichage du grillage du graphe --
		//-------------------------------------
		if(afficherMaillage)
		{	
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA_MAILLAGE));
			
			ArrayList<Line2D> arcsActifs = jeu.getArcsActifs();
			
			if(arcsActifs != null)
				for(Line2D arc : arcsActifs)
				{
					g2.setColor(COULEUR_MAILLAGE);
					g2.drawLine((int)arc.getX1(),(int)arc.getY1(),
							(int)arc.getX2(),(int)arc.getY2());
				}
			/*
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
			
			// remet la valeur initiale
	        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
		
		//-----------------------------
		//-- affichage des creatures --
		//-----------------------------
		
		Iterator<Creature> iCreatures = jeu.getCreatures().iterator();
		Creature creature;
		while(iCreatures.hasNext())
		{
			creature = iCreatures.next();
		
			if(creature.getImage() != null)
				// affichage de l'image de la creature au centre de sa position
				g2.drawImage(creature.getImage(),
						(int) (creature.getX() - creature.getWidth() / 2), 
						(int) (creature.getY() - creature.getHeight() / 2), 
						(int) creature.getWidth(), (int) creature.getHeight(), null);
			else
			{
				// affichage d'un cercle au centre de la position de la creature
				g2.setColor(COULEUR_CREATURE_SANS_IMAGE);
				g2.fillOval((int) (creature.getX() - creature.getWidth() / 2), 
						(int) (creature.getY() - creature.getHeight() / 2),
						(int) creature.getWidth(), 
						(int) creature.getHeight());
			}
			
			//-----------------------------------
			//-- affichage des barres de sante --
			//-----------------------------------
			int largeurBarre 	= (int)(creature.getWidth() * COEFF_LARGEUR_BARRE_VIE);
			int positionXBarre 	= (int)(creature.getX()-creature.getWidth()/2
										- (largeurBarre - creature.getWidth())/2);
			int positionYBarre 	= (int)(creature.getY()+creature.getHeight()/2+2);
			
			// affichage des barres de vie
			g2.setColor(COULEUR_CONTENEUR_SANTE);
			g2.fillRect(positionXBarre,positionYBarre, 
						largeurBarre, HAUTEUR_BARRE_VIE);
			
			g2.setColor(COULEUR_SANTE);
			g2.fillRect(positionXBarre+1, positionYBarre+1, 
					(int)(creature.getSante()*largeurBarre/creature.getSanteMax())-2,
					HAUTEUR_BARRE_VIE-2);
			
			// affichage du chemin des creatures
			if(afficherMaillage)
			{
				ArrayList<Point> chemin = creature.getChemin();
				if(chemin != null && chemin.size() > 0)
				{
					Point PointPrecedent = chemin.get(0);
					
					synchronized(chemin)
					{
						Iterator<Point> it = chemin.iterator();
						Point point;
						while(it.hasNext())
						{
							point = it.next();
							
							g2.setColor(COULEUR_CHEMIN);
							g2.drawLine(PointPrecedent.x, PointPrecedent.y, 
										point.x, point.y);
							PointPrecedent = point;
						}
					}
				}
			}
		}
		
		//-------------------------
		//-- affichage des tours --
		//-------------------------
		for(Tour tour : jeu.getTours())
			dessinerTour(tour,g2,false);
		
		
		//---------------------------------
		//-- entour la tour selectionnee --
		//---------------------------------
		if(tourSelectionnee != null)
		{
			dessinerTour(tourSelectionnee,g2,true);
			
			g2.setColor(COULEUR_SELECTION);
			g2.setStroke(TRAIT_TILLE);
			g2.drawRect(tourSelectionnee.getXi(), tourSelectionnee.getYi(),
					(int) (tourSelectionnee.getWidth()),
					(int) (tourSelectionnee.getHeight()));
		}
		
		//------------------------------------
		//-- affichage des rayons de portee --
		//------------------------------------
		if(afficherRayonsDePortee)
			for(Tour tour : jeu.getTours())
				dessinerPortee(tour,g2);
		
		
		//------------------------------
		//-- affichage des animations --
		//------------------------------
		Iterator<Animation> iAnimations = animations.iterator();
		while(iAnimations.hasNext())
			synchronized (iAnimations)
			{
				iAnimations.next().dessiner(g2);				
			}
	
		//------------------------------------
		//-- affichage de la tour a ajouter --
		//------------------------------------
		if(tourAAjouter != null && sourisSurTerrain)
		{
			// dessin de la tour
			dessinerTour(tourAAjouter,g2,false);
			
			// positionnnable ou non
			if(!jeu.laTourPeutEtrePosee(tourAAjouter))
			{
				g2.setColor(COULEUR_POSE_IMPOSSIBLE);
				g2.drawLine(sourisCaseX, sourisCaseY,
						(int) (sourisCaseX + tourAAjouter.getWidth()),
						(int) (sourisCaseY + tourAAjouter.getHeight()));
				
				g2.drawLine(sourisCaseX, (int) (sourisCaseY + tourAAjouter.getHeight()),
						(int) (sourisCaseX + tourAAjouter.getWidth()),
						sourisCaseY);
				
				g2.drawRect(sourisCaseX, sourisCaseY,
						(int) (tourAAjouter.getWidth()),
						(int) (tourAAjouter.getHeight()));
			}
			else
				// affichage du rayon de portee
				dessinerPortee(tourAAjouter,g2);
		}
	}
	
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
		
		
		// dessin du niveau
		for(int i=0;i < tour.getNiveau() - 1;i++)
		{
			g2.setColor(COULEUR_NIVEAU_PERIMETRE);
			g2.fillRect(tour.getXi() + 4 * i + 1, (int) (tour.getYi() + tour.getHeight() - 4), 3,3);
			g2.setColor(COULEUR_NIVEAU);
			g2.fillRect(tour.getXi() + 4 * i + 2, (int) (tour.getYi() + tour.getHeight() - 3), 1,1);
		}
		// dessin de la portee
		if(avecPortee)
			dessinerPortee(tour,g2);
	}
	
	
	private void dessinerPortee(Tour tour,Graphics2D g2)
	{
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA_PERIMETRE_PORTEE));
		//g2.setColor(tour.getCouleurDeFond());
        g2.setColor(COULEUR_RAYON_PORTEE);
		g2.drawOval((int)(tour.getXi() - tour.getRayonPortee() + tour.getWidth()/2), 
					(int)(tour.getYi() - tour.getRayonPortee() + tour.getHeight()/2), 
					(int)tour.getRayonPortee()*2, 
					(int)tour.getRayonPortee()*2);

	
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA_SURFACE_PORTEE));
        g2.setColor(COULEUR_RAYON_PORTEE);
        g2.fillOval((int)(tour.getXi() - tour.getRayonPortee() + tour.getWidth()/2), 
        			(int)(tour.getYi() - tour.getRayonPortee() + tour.getHeight()/2), 
        			(int)tour.getRayonPortee()*2, 
        			(int)tour.getRayonPortee()*2);
        
        // remet la valeur initial
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}
	
	// TODO comment
	public void toggleAfficherMaillage()
	{
		afficherMaillage = !afficherMaillage;
	}
	
	// TODO comment
	public void toggleAfficherRayonPortee()
	{
		afficherRayonsDePortee = !afficherRayonsDePortee;
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
			repaint(); // -> appel de paint
			
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
		if (me.getButton() == MouseEvent.BUTTON1)
		{
			// la selection se fait lors du clique
			for(Tour tour : jeu.getTours()) // pour chaque tour... 
				if (tour.intersects(sourisX,sourisY,1,1)) // la souris est dedans ?
				{	
					if (tourSelectionnee == tour)
						tourSelectionnee = null; // deselection
					else
					{
						tourSelectionnee = tour; // la tour est selectionnee
						// si une tour est selectionnee, il n'y pas d'ajout
						tourAAjouter = null;  
					}
					
					fenJeu.tourSelectionnee(tourSelectionnee,
											Panel_InfoTour.MODE_SELECTION);
					return;
				}
		
			// aucun tour trouvee => clique dans le vide.
			tourSelectionnee = null;
			
			fenJeu.tourSelectionnee(tourSelectionnee,
					Panel_InfoTour.MODE_SELECTION);
		}
		else
		{
			// deselection total
			tourSelectionnee 	= null;
			tourAAjouter 		= null;
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
	 * Methode de gestion de la souris lorsque qu'elle entre dans le panel
	 * 
	 * @param me evenement lie a cette action
	 * @see MouseMotionListener
	 */
	public void mouseEntered(MouseEvent me)
	{
		sourisSurTerrain = true;
		
		// recuperation du focus. 
		// Important pour la gestion des touches clavier
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
	public void mouseDragged(MouseEvent me){}

	public void setTourSelectionnee(Tour tour)
	{
		tourSelectionnee = tour;
	}

	/**
	 * Methode de gestion des evenements lors de la relache d'une touche
	 */
	public void keyReleased(KeyEvent ke)
	{
		if(tourSelectionnee != null)
		{
			// raccourci de vente
			if(ke.getKeyChar() == 'v' || ke.getKeyChar() == 'V')
				fenJeu.vendreTour(tourSelectionnee);
			// raccourci d'amelioration
			else if(ke.getKeyChar() == 'a' || ke.getKeyChar() == 'A')
				fenJeu.ameliorerTour(tourSelectionnee);
		}
	}
	public void keyPressed(KeyEvent ke){}
	public void keyTyped(KeyEvent ke){}

	public void deselectionner()
	{
		tourAAjouter 		= null;
		tourSelectionnee 	= null;
	}

	public void addAnimation(Animation animation)
	{
		animations.add(animation);
	}
}
