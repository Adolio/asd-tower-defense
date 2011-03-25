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

import java.awt.*;

/**
 * Classe de gestion d'une animation
 * <p>
 * Cette classe est utilisee pour afficher des animations
 * <p>
 * Elle est abstraite et doit etre heritee pour etre ensuite instanciee.
 * 
 * @author Aurelien Da Campo
 * @version 1.1 | mai 2010
 * @since jdk1.6.0_16
 */
public abstract class Animation extends Point
{
    public static final int HAUTEUR_SOL = 0;
    public static final int HAUTEUR_AIR = 1;
    
    private static final long serialVersionUID = 1L;
	protected boolean estTerminee;
    protected int hauteur = HAUTEUR_AIR;
    
	/**
	 * Constructeur de l'animation.
	 * 
	 * @param x position initiale x
	 * @param y position initiale y
	 */
	public Animation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Permet de dessiner l'animation.
	 * 
	 * @param g2 le Graphics2D pour dessiner
	 */
	abstract public void dessiner(Graphics2D g2);

	/**
     * Permet d'animer l'animation
     */
    abstract public void animer(long tempsPasse);
	
	/**
	 * Permet de savoir si l'animation est terminee
	 * 
	 * Lorsqu'une animation est terminee, elle sera detruite.
	 * 
	 * @return true si elle est terminee et false sinon
	 */
    public boolean estTerminee()
    {
        return estTerminee;
    }
     
    /**
     * Permet de recuperer la hauteur de l'animation
     * 
     * @return la hauteur
     */
    public int getHauteur()
    {
        return hauteur;
    }
}
