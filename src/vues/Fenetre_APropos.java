package vues;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import models.jeu.Jeu;

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

	private String[] auteurs = {
	                            "Lazhar Farjallah",
	                            "Pierre-Dominique Putallaz",
	                            "Aurélien Da Campo"
	                            };

	JPanel pAuteurs = new JPanel(new GridLayout(0,2));
	
	/**
	 * Constructeur de la fenetre.
	 */
	public Fenetre_APropos()
	{
		super("A propos");
		setResizable(false); // taille fixe
		
		pAuteurs.add(new JLabel("Auteurs :"));
		for(String auteur : auteurs)
		{
		    pAuteurs.add(new JLabel(auteur));
		    pAuteurs.add(new JPanel());
		}
		
		JLabel lversion = new JLabel(Jeu.getVersion());
		lversion.setFont(new Font("", Font.BOLD, 14));
		
		getContentPane().add(lversion,BorderLayout.NORTH);
		getContentPane().add(pAuteurs,BorderLayout.CENTER);
		
		pack();
		setVisible(true);
		setLocationRelativeTo(null); // centrage de la fenetre
	}
}
