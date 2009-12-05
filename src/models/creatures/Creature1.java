package models.creatures;

public class Creature1 extends Creature
{
	private static final int SANTE_MAX = 300;
	private static final int GAIN_PIECE_D_OR = 4;
	
	public Creature1()
	{
		this(0, 0);
	}
	
	public Creature1(int x, int y)
	{
		super(x, y,10,10,SANTE_MAX,GAIN_PIECE_D_OR);
		
	}

	public Creature copier()
	{
		return new Creature1(x,y);
	}
}
