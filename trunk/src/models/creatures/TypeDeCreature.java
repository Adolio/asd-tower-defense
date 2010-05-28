package models.creatures;

public class TypeDeCreature
{
    private static final int MOUTON          = 0;
    private static final int ARAIGNEE        = 1;
    private static final int PIGEON          = 2;
    private static final int AIGLE           = 3;
    private static final int RHINOCEROS      = 4;
    private static final int ELEPHANT        = 5;
    private static final int GRANDE_ARAIGNEE = 6;

	public static Creature getCreature(int typeDeCreature)
	{
	    double vitesse = 20;
	    
	    switch(typeDeCreature)
        {
            case MOUTON :
                return new Mouton(100, 5, vitesse);
            case ARAIGNEE : 
                return new Araignee(200, 10, vitesse);
            case PIGEON :
                return new Pigeon(300, 15, vitesse);     
            case AIGLE : 
                return new Aigle(600, 30, vitesse);  
            case RHINOCEROS :
                return new Rhinoceros(1200, 60, vitesse);
            case ELEPHANT : 
                return new Elephant(2000, 100, vitesse);
            case GRANDE_ARAIGNEE :
                return new GrandeAraignee(3000, 150, vitesse);
                
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
