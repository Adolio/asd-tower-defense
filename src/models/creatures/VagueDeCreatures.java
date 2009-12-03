package models.creatures;

public class VagueDeCreatures
{
	private int nbDeCreatures;
	private Creature creatureAEnvoyer;
	
	public VagueDeCreatures(int nbDeCreatures, Creature creatureAEnvoyer)
	{
		this.nbDeCreatures		= nbDeCreatures;
		this.creatureAEnvoyer 	= creatureAEnvoyer;
	}
	
	public int getNbDeCreatures()
	{
		return nbDeCreatures;
	}
	
	public Creature getNouvelleCreature()
	{
		return creatureAEnvoyer.copier();
	}
}
