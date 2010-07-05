package models.outils;

public class Timer 
{
	long timestamp;
	boolean started;
	
	public void start()
	{
		timestamp = System.currentTimeMillis();
		started = true;
	}
	
	public void pause(){}
	
	public long getTime()
	{
		if(started)
			return System.currentTimeMillis() - timestamp;
		else
			return 0;
	}
	
	public void reset()
	{
		timestamp = System.currentTimeMillis();
	}
}
