package models.creatures;
/**
 * Classe de gestion de type de créatures pour le passage par le réseau.
 * 
 * @author Aurélien Da Campo
 * @author Pierre-Dominique Putallaz
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Creature
 */
public class TypeDeCreature
{
    private static final int MOUTON          = 0;
    private static final int ARAIGNEE        = 1;
    private static final int PIGEON          = 2;
    private static final int RHINOCEROS      = 3;
    private static final int AIGLE           = 4;
    private static final int ELEPHANT        = 5;
    private static final int GRANDE_ARAIGNEE = 6;

	public static Creature getCreature(int typeDeCreature, int noVague, boolean invincible)
	{
	    
	    Creature c = null;
	    switch(typeDeCreature)
        {
	    
            case MOUTON :
                c = new Mouton(100*noVague, 5, VagueDeCreatures.VITESSE_CREATURE_NORMALE); 
                break;
            case ARAIGNEE : 
                c = new Araignee(200*noVague, 10, VagueDeCreatures.VITESSE_CREATURE_RAPIDE);
                break;
            case PIGEON :
                c = new Pigeon(300*noVague, 15, VagueDeCreatures.VITESSE_CREATURE_NORMALE);    
                break;
            case RHINOCEROS :
                c = new Rhinoceros(600*noVague, 60, VagueDeCreatures.VITESSE_CREATURE_RAPIDE);
                break;
            case AIGLE : 
                c = new Aigle(1200*noVague, 30, VagueDeCreatures.VITESSE_CREATURE_NORMALE); 
                break;
            case ELEPHANT :
                c = new Elephant(3000*noVague, 100, VagueDeCreatures.VITESSE_CREATURE_LENTE);
                break;
            case GRANDE_ARAIGNEE :
                c = new GrandeAraignee(3000*noVague, 150, VagueDeCreatures.VITESSE_CREATURE_RAPIDE); 
                break;
            default :
                return null; // TODO erreur
            
	    /*
            case MOUTON :
                c = new Mouton(100, 5, VagueDeCreatures.VITESSE_CREATURE_NORMALE); 
                break;
            case ARAIGNEE : 
                c = new Araignee(200, 10, VagueDeCreatures.VITESSE_CREATURE_RAPIDE);
                break;
            case PIGEON :
                c = new Pigeon(300, 15, VagueDeCreatures.VITESSE_CREATURE_NORMALE);    
                break;
            case RHINOCEROS :
                c = new Rhinoceros(600, 60, VagueDeCreatures.VITESSE_CREATURE_RAPIDE);
                break;
            case AIGLE : 
                c = new Aigle(1200, 30, VagueDeCreatures.VITESSE_CREATURE_NORMALE); 
                break;
            case ELEPHANT : 
                c = new Elephant(3000, 100, VagueDeCreatures.VITESSE_CREATURE_LENTE);
                break;
            case GRANDE_ARAIGNEE :
                c = new GrandeAraignee(3000, 150, VagueDeCreatures.VITESSE_CREATURE_RAPIDE); 
                break;
            default :
                return null; // TODO erreur
           */
        }
	    
	    c.setInvincible(invincible);
	    
        return c;
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
