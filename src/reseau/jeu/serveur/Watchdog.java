package reseau.jeu.serveur;

import java.util.Observable;

public class Watchdog implements Runnable
{
	private long attente;
	private Observable target;

	public Watchdog(Observable o, long attente)
	{
		this.attente = attente;
		this.target = o;
		(new Thread(this)).start();
	}

	@Override
	public void run()
	{
		while (true)
		{
			// Met à jour
			target.notifyObservers();
			// Attente un nombre donné de temps
			try
			{
				synchronized (this)
				{
					wait(attente);
				}
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

	}

}
