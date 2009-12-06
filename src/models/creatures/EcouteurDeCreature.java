package models.creatures;

/**
 * Interface d'ecoute d'une creature.
 * 
 * Permet d'etre renseigne lorsqu'une creature subi des modifications.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Creature
 */
public interface EcouteurDeCreature
{
	/**
	 * Permet de savoir si une creature subie des degats.
	 * @param creature la creature qui a subie des degats
	 */
	void creatureTuee(Creature creature);
	
	/**
	 * Permet de savoir quand une creature a ete tuee.
	 * @param creature la creature qui a ete tuee
	 */
	void creatureBlessee(Creature creature);
}
