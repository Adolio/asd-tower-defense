package models.creatures;

public class TypeDeCreature
{
    private static final int AIGLE           = 0;
    private static final int ARAIGNEE        = 1;
    private static final int ELEPHANT        = 2;
    private static final int GRANDE_ARAIGNEE = 3;
    private static final int MOUTON          = 4;
	private static final int PIGEON          = 5;
	private static final int RHINOCEROS      = 6;
	
	public static Creature getCreature(int typeDeCreature, long santeMax, int nbPiecesDOr, double vitesse)
	{
	    switch(typeDeCreature)
        {
            case AIGLE :
                return new Aigle(santeMax, nbPiecesDOr, vitesse);
            case ARAIGNEE : 
                return new Araignee(santeMax, nbPiecesDOr, vitesse);
            case ELEPHANT :
                return new Elephant(santeMax, nbPiecesDOr, vitesse);
            case GRANDE_ARAIGNEE : 
                return new GrandeAraignee(santeMax, nbPiecesDOr, vitesse);
            case MOUTON :
                return new Mouton(santeMax, nbPiecesDOr, vitesse);
            case PIGEON : 
                return new Pigeon(santeMax, nbPiecesDOr, vitesse);
            case RHINOCEROS :
                return new Rhinoceros(santeMax, nbPiecesDOr, vitesse);
            default :
                return null; // TODO erreur
        }
	}

    public static int getTypeCreature(Creature creature)
    {
        if (creature instanceof Aigle)
            return AIGLE;
        else if (creature instanceof Araignee)
            return ARAIGNEE;
        else if (creature instanceof Elephant)
            return ELEPHANT;
        else if (creature instanceof Mouton)
            return MOUTON;
        else if (creature instanceof Pigeon)
            return PIGEON;
        else if (creature instanceof Rhinoceros)
            return RHINOCEROS;
        else if (creature instanceof GrandeAraignee)
            return GRANDE_ARAIGNEE;
        
        return -1;
    }
}
