/*
  Copyright (C) 2010 Aurelien Da Campo

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/

package models.animations;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import models.jeu.Jeu;
import models.outils.Outils;

/**
 * Classe de gestion d'un Nuage
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 * @since jdk1.6.0_16
 */
public class Nuage extends Animation
{
	// constantes statiques
    private static final long serialVersionUID         = 1L;
	private static final Image[] IMAGES                = new Image[3];

	// attributs
	private double vitesse;
	private Image IMAGE;
	private float alpha = 1.0f;
	private Jeu jeu;
	
	static
    { 
	    for(int i=0;i<IMAGES.length;i++)
            IMAGES[i] = Toolkit.getDefaultToolkit().getImage("img/animations/nuages/"+i+".png");
    }
	
	/**
	 * Constructeur de l'animation
	 * 
	 * @param largeur largeur de l'anim
	 * @param hauteur hauteur de l'anim
	 */
	public Nuage(Jeu jeu)
	{
		super(Outils.tirerNombrePseudoAleatoire(-500,-300), Outils.tirerNombrePseudoAleatoire(-100, jeu.getTerrain().getHauteur()));
	    
		IMAGE = IMAGES[Outils.tirerNombrePseudoAleatoire(0, 2)];
		vitesse = Outils.tirerNombrePseudoAleatoire(2, 10) / 100.0;
		
	    this.jeu = jeu;
		
		hauteur = HAUTEUR_AIR;
	}

	@Override
	public void dessiner(Graphics2D g2)
	{
	    // style
	    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    
	    // dessin
		g2.drawImage(IMAGE,x,y,null);
		
		// retabli la transparence
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
	}

    @Override
    public void animer(long tempsPasse)
    {
        this.x += tempsPasse * vitesse;
        
        if(this.x > jeu.getTerrain().getLargeur() + 100)
            estTerminee = true;
    }
}
