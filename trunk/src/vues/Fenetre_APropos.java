package vues;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Fenetre d'affichage d'information a propos du logiciel.
 * 
 * Affiche : les auteurs, le cadre du travail, la date et la version
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see JFrame
 */
public class Fenetre_APropos extends JFrame
{
	private static final long serialVersionUID = 1L;

	JLabel lAuteurs = new JLabel("ASD Tower Defense v1.0");
	public final static int FENETRE_LARGEUR = 240;
	public final static int FENETRE_HAUTEUR = 120;
	
	/**
	 * Constructeur de la fenetre.
	 */
	public Fenetre_APropos()
	{
		super("A propos");
		
		setBounds(0,0,FENETRE_LARGEUR,FENETRE_HAUTEUR);
		setResizable(false); // taille fixe
		
		getContentPane().add(lAuteurs,BorderLayout.NORTH);
		
		setVisible(true);
	}
}
