package models.creatures;

public class Creature1 extends Creature
{
	public Creature1()
	{
		super(0, 0);
	}
	
	public Creature1(int x, int y)
	{
		super(x, y);
	}

	public Creature copier()
	{
		return new Creature1(x,y);
	}

}
