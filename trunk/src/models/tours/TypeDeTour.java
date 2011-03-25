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

package models.tours;

import exceptions.TypeDeTourInvalideException;

/**
 * Classe de gestion des types de tour pour les communications réseaux
 * 
 * @author Aurélien Da Campo
 * @version 1.0 | juin 2010
 * @since jdk1.6.0_16
 * @see Tour
 */
public class TypeDeTour
{
	private static final int TOUR_ARCHER        = 1;
	private static final int TOUR_CANON         = 2;
	private static final int TOUR_AA            = 3;
	private static final int TOUR_DE_GLACE      = 4;
	private static final int TOUR_ELECTRIQUE    = 5;
	private static final int TOUR_DE_FEU        = 6;
	private static final int TOUR_D_AIR         = 7;
	private static final int TOUR_DE_TERRE      = 8;
	
	
	/**
     * Permet de recuperer le type d'une tour
     */
    public static int getTypeDeTour(Tour tour)
    {
        if (tour instanceof TourArcher)
            return TOUR_ARCHER;
        else if (tour instanceof TourCanon)
            return TOUR_CANON;
        else if (tour instanceof TourAntiAerienne)
            return TOUR_AA;
        else if (tour instanceof TourDeGlace)
            return TOUR_DE_GLACE;
        else if (tour instanceof TourElectrique)
            return TOUR_ELECTRIQUE;
        else if (tour instanceof TourDeFeu)
            return TOUR_DE_FEU;
        else if (tour instanceof TourDAir)
            return TOUR_D_AIR;
        else if (tour instanceof TourDeTerre)
            return TOUR_DE_TERRE;
        
        return -1;
    }


    public static Tour getTour(int typeDeCreature) 
        throws TypeDeTourInvalideException
    {
        switch(typeDeCreature)
        {
            case TOUR_ARCHER : 
                return new TourArcher();
            case TOUR_AA : 
                return new TourAntiAerienne();
            case TOUR_CANON :
                return new TourCanon();
            case TOUR_D_AIR :
                return new TourDAir();
            case TOUR_DE_FEU : 
                return new TourDeFeu();
            case TOUR_DE_GLACE : 
                return new TourDeGlace();
            case TOUR_ELECTRIQUE :
                return new TourElectrique();
            case TOUR_DE_TERRE :
                return new TourDeTerre();
            default : 
                throw new TypeDeTourInvalideException(
                        "Le type " + typeDeCreature + " est invalide");  
        }
    }
}
