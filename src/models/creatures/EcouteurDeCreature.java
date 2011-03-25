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

import models.joueurs.Joueur;

/**
 * Interface d'ecoute d'une creature.
 * 
 * Permet d'etre renseigne lorsqu'une creature subi des modifications.
 * 
 * @author Aurélien Da Campo
 * @version 1.1 | mai 2010
 * @since jdk1.6.0_16
 * @see Creature
 */
public interface EcouteurDeCreature
{
	/**
	 * Permet de savoir si une creature subie des degats.
	 * @param creature la creature qui a subie des degats
	 * @param tueur 
	 */
    void creatureTuee(Creature creature, Joueur tueur);
	
	/**
	 * Permet de savoir quand une creature a ete tuee.
	 * @param creature la creature qui a ete tuee
	 */
	void creatureBlessee(Creature creature);
	
	/**
	 * Permet de savoir quand une creature arrive à la fin du parcours.
	 * @param creature la creature qui est arrivee
	 */
	void creatureArriveeEnZoneArrivee(Creature creature);
}
