package vues;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
public class Fenetre_APropos extends JDialog
{
	private static final long serialVersionUID = 1L;
	private static final ImageIcon I_AIDE = new ImageIcon("img/icones/help.png");
	private JButton bFermer = new JButton("Fermer");
	
	/**
	 * liste des auteurs du programme
	 */
	private final JPanel pAuteurs = new JPanel(new GridLayout(0,2));
	private static final String[] auteurs = {
	                            "Lazhar Farjallah",
	                            "Pierre-Dominique Putallaz",
	                            "Aurélien Da Campo"
	                            };
	
	/**
	 * Constructeur de la fenetre.
	 */
	public Fenetre_APropos(Frame parent)
	{
		// preference de la fenetre
	    super(parent,"A propos",true);
	    setIconImage(I_AIDE.getImage());
		setResizable(false); // taille fixe
		
		// ajout des auteurs
		pAuteurs.add(new JLabel("Auteurs :"));
		for(String auteur : auteurs)
		{
		    pAuteurs.add(new JLabel(auteur));
		    pAuteurs.add(new JPanel());
		}
		getContentPane().add(pAuteurs,BorderLayout.CENTER);
		
		// ajout de la version
		JLabel lversion = new JLabel(Jeu.getVersion());
		lversion.setFont(GestionnaireDesPolices.POLICE_TITRE);
		getContentPane().add(lversion,BorderLayout.NORTH);
		
		// ajout du bouton
		bFermer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){   
                dispose();
            }
        });
		
		JPanel pTmp = new JPanel();
		pTmp.add(bFermer);
		getContentPane().add(pTmp,BorderLayout.SOUTH);
		
		// dernieres proprietes de la fenetre
		pack();
		setLocationRelativeTo(null); // centrage de la fenetre
		setVisible(true);
	}
}
