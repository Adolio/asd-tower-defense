package models.tours;

import java.awt.*;
import java.util.Iterator;

import models.creatures.Creature;
import models.terrains.Terrain;

/**
 * Classe de gestion d'une tour
 * 
 * Cette classe est abstraite et doit etre heritee pour etre ensuite instanciee.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 */
public abstract class Tour extends Rectangle implements Runnable
{
	private static final long serialVersionUID = 1L;

	/**
	 * coefficient de prix de vente de la tour. utilisee pour calculer le prix
	 * de vente de la tour.
	 */
	private static final double COEFF_PRIX_VENTE = 0.6;

	/**
	 * couleur de fond de la tour
	 */
	private Color couleurDeFond;

	/**
	 * nom de la tour
	 */
	private final String NOM;

	/**
	 * texte descriptif de la tour
	 */
	protected String description;

	/**
	 * degats de la tour
	 */
	protected int degats;

	/**
	 * niveau actuel de la tour
	 */
	protected int niveau = 1;

	/**
	 * prix achat de la tour ou de son amelioration
	 */
	protected int prixAchat;

	/**
	 * prix total de la tour en compant toutes les ameliorations utilisee pour
	 * calculer le prix de vente de la tour
	 */
	protected int prixTotal;

	/**
	 * rayon de portee de la tour
	 */
	protected double rayonPortee = 100;

	/**
	 * image
	 */
	protected Image image;

	/**
	 * le terrain pour recuperer des informations sur les creatures
	 */
	protected Terrain terrain;

	/**
	 * la tour gere sont propre thread qui s'occupe de trouver une creature et
	 * de lui tirer dessus.
	 */
	private Thread thread;

	/**
	 * Utilise pour savoir si la tour est en jeu. (thread active)
	 */
	private boolean enJeu;

	protected double cadenceTir; // par seconde

	/**
	 * Constructeur de la tour.
	 * 
	 * @param x
	 *            la position sur l'axe X de la tour
	 * @param y
	 *            la position sur l'axe Y de la tour
	 * @param largeur
	 *            la largeur de la tour
	 * @param hauteur
	 *            la hauteur de la tour
	 * @param couleurDeFond
	 *            la couleur de fon de la tour
	 * @param nom
	 *            le nom de la tour
	 * @param prixAchat
	 *            le prix d'achat de la tour
	 */
	public Tour(int x, int y, int largeur, int hauteur, Color couleurDeFond,
			String nom, int prixAchat, int degats, double rayonPortee, 
			double cadenceTir, Image image)
	{
		this.x = x;
		this.y = y;

		width = largeur;
		height = hauteur;

		this.NOM = nom;
		this.couleurDeFond = couleurDeFond;
		this.prixAchat = prixAchat;
		prixTotal = prixAchat;
		this.degats = degats;
		this.rayonPortee = rayonPortee;

		this.cadenceTir = cadenceTir;
		
		this.image = image;
	}

	/**
	 * permet de recuperer le temps de preparation d'un tir
	 * @return le temps de preparation d'un tir en miliseconde
	 */
	public double getCadenceTir()
	{
		return cadenceTir;
	}
	
	/**
	 * methode a redefinir qui prend les decision concernant les améliorations
	 * de la tour.
	 */
	public abstract void ameliorer();

	/**
	 * methode a redefinir qui prend les decision concernant les tires de la
	 * tour.
	 */
	public abstract void tirer(Creature creature);

	/**
	 * Permet de modifier le terrain
	 * 
	 * @param terrain
	 *            le terrain sur lequel la tour est posee
	 */
	public void setTerrain(Terrain terrain)
	{
		this.terrain = terrain;
	}

	/**
	 * Permet de recuperer la couleur de fond d'une tour
	 * 
	 * @return la couleur de fond
	 */
	public Color getCouleurDeFond()
	{
		return couleurDeFond;
	}

	/**
	 * Permet de recuperer le nom de la tour
	 * 
	 * @return
	 */
	public String getNom()
	{
		return NOM;
	}

	/**
	 * Permet de recuperer la position de la tour sur l'axe X
	 * 
	 * @return la position de la tour sur l'axe X
	 */
	public int getXi()
	{
		return x;
	}

	/**
	 * Permet de recuperer la position de la tour sur l'axe Y
	 * 
	 * @return la position de la tour sur l'axe Y
	 */
	public int getYi()
	{
		return y;
	}

	/**
	 * Permet de recuperer la description de la tour
	 * 
	 * @return la description de la tour
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Permet de recuperer le prix d'achat de la tour ou de son niveau suivant
	 * 
	 * @return le prix d'achat de la tour ou de son niveau suivant
	 */
	public int getPrixAchat()
	{
		return prixAchat;
	}

	/**
	 * Permet de recuperer le rayon de portee de la tour
	 * 
	 * @return le rayon de portee de la tour
	 */
	public double getRayonPortee()
	{
		return rayonPortee;
	}

	/**
	 * Permet de recuperer le prix de vente de la tour
	 * 
	 * @return
	 */
	public int getPrixDeVente()
	{
		return (int) (prixTotal * COEFF_PRIX_VENTE);
	}

	/**
	 * Permet de recuperer le prix total de la tour
	 * 
	 * @return le prix total de la tour
	 */
	public int getPrixTotal()
	{
		return prixTotal;
	}

	/**
	 * Permet de recuperer l'image de la tour
	 * 
	 * @return l'image de la tour
	 */
	public Image getImage()
	{
		return image;
	}

	/**
	 * Permet de mettre la tour en jeu. Cette methode demarre le thread de la
	 * tour. Des lors, elle tirera sur les creatures.
	 */
	public void demarrer()
	{
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Permet d'arreter la tour, de la sortir du jeu
	 */
	public void arreter()
	{
		enJeu = false;
	}

	/**
	 * Methode des gestion du thread de la tour.
	 * 
	 * Cette methode est regie de l'implementation de l'interface Runnable.
	 */
	public void run()
	{
		enJeu = true;

		while (enJeu)
		{
			// recuperation de la creature la plus proche et a portee de la tour
			Creature creature = getCreatureLaPlusProcheEtAPortee();

			// si elle existe
			if (creature != null)
			{

				// preparation du tire
				// TODO mieux gerer les temps
				try
				{
					Thread.sleep((long) (1000.0 / cadenceTir));
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				tirer(creature);
			} else
			{
				// si pas de creature aux environs, on attend
				// TODO mieux gerer les temps
				try
				{
					Thread.sleep(50);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Permet de recuperer la creature la plus proche et a portee de la tour.
	 * 
	 * @return la creature la plus proche et a portee de la tour ou <b>null s'il
	 *         n'y a pas de creature a portee</b>
	 */
	private Creature getCreatureLaPlusProcheEtAPortee()
	{

		// le terrain a bien ete setter ?
		if (terrain == null)
			return null;

		// variables temporaires pour calcul
		Creature creatureLaPlusProche = null;
		double distanceLaPlusProche = 0;
		double distance = 0;

		Iterator<Creature> iCreatures = terrain.getCreatures().iterator();
		Creature creature;

		// pour chaque creature sur le terrain
		while (iCreatures.hasNext())
		{
				creature = iCreatures.next();

				// calcul de la distance entre la tour et la creature
				distance = getDistance(creature);

				// est-elle a portee ?
				if (distance <= rayonPortee)
				{
					// la creature actuelle est-elle plus proche que la derniere
					// creature a portee testee ?
					if (creatureLaPlusProche == null
							|| distance < distanceLaPlusProche)
					{
						// nouvelle creature plus proche trouvee!
						creatureLaPlusProche = creature;
						distanceLaPlusProche = distance;
					}
				}
		}

		return creatureLaPlusProche;

	}

	/**
	 * Calcul la distance en vol d'oiseau entre la tour et une creature
	 * 
	 * @param creature
	 *            la creature
	 * @return la distance en vol d'oiseau entre la tour et une creature
	 */
	synchronized private double getDistance(Creature creature)
	{
		return Point.distance(x, y, creature.x, creature.y);
	}

	abstract public Tour getCopieOriginale();

	abstract public boolean peutEncoreEtreAmelioree();

	public int getNiveau()
	{
		return niveau;
	}

	// TODO
	public int getDegats()
	{
		return degats;
	}
}
