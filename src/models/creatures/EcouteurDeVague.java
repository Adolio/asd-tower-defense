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

/**
 * Interface d'ecoute d'une vague de creatures.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | 31 decembre 2009
 * @since jdk1.6.0_16
 * @see VagueDeCreatures
 */
public interface EcouteurDeVague
{
    /**
     * Permet d'informer l'ecouteur que la vague a entierement ete 
     * lancee sur le terrain.
     * 
     * @param vagueDeCreatures la vague concernee
     */
    public void vagueEntierementLancee(VagueDeCreatures vagueDeCreatures);
}

