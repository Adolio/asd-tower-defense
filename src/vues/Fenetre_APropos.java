package vues;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

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
    private static final int MARGES_PANEL = 40;
    private static final long serialVersionUID = 1L;
	private static final ImageIcon I_AIDE = new ImageIcon("img/icones/help.png");
	private JButton bFermer = new JButton("Fermer");
	
	/**
	 * liste des auteurs du programme
	 */
	private final JPanel pAuteurs;
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
		getContentPane().setBackground(LookInterface.COULEUR_DE_FOND);
		
		
		JPanel pFormulaire = new JPanel(new BorderLayout());
		pFormulaire.setOpaque(false);
		pFormulaire.setBorder(new EmptyBorder(new Insets(MARGES_PANEL, MARGES_PANEL,
                MARGES_PANEL, MARGES_PANEL)));
		
		// ajout des auteurs
		pAuteurs = new JPanel(new GridLayout(0,2));
		pAuteurs.setOpaque(false);
		pAuteurs.add(new JLabel("Auteurs :"));
		for(String auteur : auteurs)
		{
		    pAuteurs.add(new JLabel(auteur));
		    
		    JPanel p = new JPanel();
		    p.setOpaque(false);
		    pAuteurs.add(p);
		}
		pFormulaire.add(pAuteurs,BorderLayout.CENTER);
		
		// ajout de la version
		JLabel lversion = new JLabel(Jeu.getVersion());
		lversion.setFont(GestionnaireDesPolices.POLICE_DONNEES);
		pFormulaire.add(lversion,BorderLayout.NORTH);
		
		// ajout du bouton
		bFermer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){   
                dispose();
            }
        });
		
		JPanel pTmp = new JPanel();
		pTmp.setOpaque(false);
		
		pTmp.add(bFermer);
		pFormulaire.add(pTmp,BorderLayout.SOUTH);
		
		getContentPane().add(pFormulaire,BorderLayout.CENTER);
		
		// dernieres proprietes de la fenetre
		pack();
		setLocationRelativeTo(null); // centrage de la fenetre
		setVisible(true);
	}
}
