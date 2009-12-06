package models.creatures;

/**
 * Structure permettant de stoquer des informations sur une vague de creatures.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Creature
 */
public class VagueDeCreatures
{
	private final int NB_CREATURES;
	private Creature creatureAEnvoyer;
	
	/**
	 * Constructeur de la vague de creatures
	 * @param nbCreatures le nombre de copie de la creature a envoyer
	 * @param creatureAEnvoyer un objet de la creature a envoyer nbCreatures fois
	 */
	public VagueDeCreatures(int nbCreatures, Creature creatureAEnvoyer)
	{
		this.NB_CREATURES		= nbCreatures;
		this.creatureAEnvoyer 	= creatureAEnvoyer;
	}
	
	/**
	 * Permet de recuperer le nombre de creatures dans la vague
	 * @return le nombre de creatures dans la vague
	 */
	public int getNbCreatures()
	{
		return NB_CREATURES;
	}
	
	/**
	 * Permet de recuperer une copie de la creature a envoyer.
	 * @return une copie de la creature a envoyer
	 */
	public Creature getNouvelleCreature()
	{
		return creatureAEnvoyer.copier();
	}
}
