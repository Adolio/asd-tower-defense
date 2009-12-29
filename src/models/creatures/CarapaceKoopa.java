package models.creatures;

import java.awt.Image;
import java.awt.Toolkit;

/**
 * Classe de gestion d'une creature.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Creature
 */
public class CarapaceKoopa extends Creature
{
	private static final long serialVersionUID = 1L;
	private static final Image[] IMAGES;
	
	static
	{
		IMAGES = new Image[]
		{
			Toolkit.getDefaultToolkit().getImage("img/creatures/carapaceKoopa/carapaceKoopa_0.gif"),
			Toolkit.getDefaultToolkit().getImage("img/creatures/carapaceKoopa/carapaceKoopa_1.gif"),
			Toolkit.getDefaultToolkit().getImage("img/creatures/carapaceKoopa/carapaceKoopa_2.gif"),
			Toolkit.getDefaultToolkit().getImage("img/creatures/carapaceKoopa/carapaceKoopa_3.gif")
		};
	}
	
	/**
	 * Constructeur de base.
	 * 
	 * @param santeMax la sante max de cette creature
	 * @param nbPiecesDOr le nombre de pieces d'or de cette creature
	 * @param vitesse vitesse de la creature
	 */
	public CarapaceKoopa(int santeMax, int nbPiecesDOr, double vitesse)
	{
		this(0, 0, santeMax, nbPiecesDOr,vitesse);
	}
	
	/**
	 * Constructeur avec position initiale.
	 * 
	 * @param x la position sur l'axe X de la creature
	 * @param y la position sur l'axe Y de la creature
	 * @param santeMax la sante max de cette creature
	 * @param nbPiecesDOr le nombre de pieces d'or de cette creature
	 * @param vitesse vitesse de la creature
	 */
	public CarapaceKoopa(int x, int y, int santeMax, int nbPiecesDOr, double vitesse)
	{
		super(x, y, 14, 14, santeMax,nbPiecesDOr,vitesse,
		        Creature.TYPE_TERRIENNE ,IMAGES[0],"Carapace Koopa");
		
		Thread animation = new Thread( new Runnable()
        {
            public void run()
            {
                int i = 1;
                enJeu = true;
                
                // tant que la creature est en jeu et vivante
                while(enJeu && !estMorte())
                {
                    // image suivante
                    i %= (IMAGES.length);
                    image = IMAGES[i++];

                    try{
                        Thread.sleep(100);
                    } 
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });
		animation.start();
	}

	/**
	 * permet de copier la creature
	 */
	public Creature copier()
	{
		return new CarapaceKoopa(x,y,getSanteMax(),getNbPiecesDOr(),getVitesseNormale());
	}
}
