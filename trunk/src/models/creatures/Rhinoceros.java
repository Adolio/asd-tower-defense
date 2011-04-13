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

package models.creatures;

import java.awt.Image;
import java.awt.Toolkit;

/**
 * Classe de gestion d'une creature volante.
 * 
 * @author Aurélien Da Campo
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Creature
 */
public class Rhinoceros extends Creature
{
	private static final long serialVersionUID = 1L;
	private static final Image IMAGE;
	
	static
	{
		IMAGE = Toolkit.getDefaultToolkit().getImage("img/creatures/rhinoceros.png");
	}
	
	/**
	 * Constructeur de base.
	 * @param santeMax la sante max de cette creature
	 * @param nbPiecesDOr le nombre de pieces d'or de cette creature
	 * @param vitesse vitesse de la creature
	 */
	public Rhinoceros(long santeMax, int nbPiecesDOr, double vitesse)
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
	public Rhinoceros(int x, int y, long santeMax, int nbPiecesDOr, double vitesse)
	{
		super(x, y, 24, 24, santeMax, nbPiecesDOr, vitesse, 
		      Creature.TYPE_TERRIENNE, IMAGE, "Rhinoceros");
	}

	/**
	 * permet de copier la creature
	 */
	public Creature copier()
	{
		return new Rhinoceros(x,y,getSanteMax(),getNbPiecesDOr(),getVitesseNormale());
	}
}
