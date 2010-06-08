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
