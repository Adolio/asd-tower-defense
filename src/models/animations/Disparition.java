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

/**
 * Classe de gestion de l'animation d'une tache de sang
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 * @since jdk1.6.0_16
 */
public class Disparition extends Animation
{
	// constantes statiques
    private static final long serialVersionUID = 1L;
	
	private final long DUREE_DE_VIE;
	
	// attributs
	private float alpha = 1.0f;
    private Image image;
    private long tempsPasse = 0;
    
	/**
	 * Constructeur de l'animation
	 * 
	 * @param largeur largeur de l'anim
	 * @param hauteur hauteur de l'anim
	 */
	public Disparition(int centerX, int centerY, Image image, long duree)
	{
	    super(centerX, centerY);
		
		this.image = image;
		this.DUREE_DE_VIE = duree;
	}

	@Override
	public void dessiner(Graphics2D g2)
	{
	    // style
	    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    
	    // dessin
		g2.drawImage(image,x-image.getWidth(null)/2,y-image.getHeight(null)/2,null);
		
		// retabli la transparence
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
	}

    @Override
    public void animer(long tempsPasse)
    {
        this.tempsPasse += tempsPasse;
        
        // temps de vie passé
        if(this.tempsPasse > DUREE_DE_VIE)
            estTerminee = true;
        else
            // fondu
            alpha = 1.f - (float) this.tempsPasse / (float) DUREE_DE_VIE;
    }
}
