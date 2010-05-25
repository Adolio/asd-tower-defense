package serveur.jeu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

import models.creatures.Creature;
import models.tours.Tour;

public class Updater implements Observer, Runnable
{
	private Thread thread;
	private HashMap<Integer, JoueurDistant> clients;
	private ConcurrentLinkedQueue<Message> buffer;

	public Updater(HashMap<Integer, JoueurDistant> clients)
	{
		this.clients = clients;
		buffer = new ConcurrentLinkedQueue<Message>();
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void update(Observable o, Object arg)
	{
		ServeurJeu srv = (ServeurJeu) o;
		parseCreatures(srv.getCreatures());
		parseTours(srv.getTours());
	}

	@Override
	public void run()
	{
		while (true)
		{
			// Signalisation aux clients que la partie à commencé
			for (Entry<Integer, JoueurDistant> joueur : clients.entrySet())
				joueur.getValue().send("Update");
		}
	}

	private ArrayList<Message> parseCreatures(ArrayList<Creature> array)
	{
		ArrayList<Message> retour = new ArrayList<Message>();
		for (Creature c : array)
		{
			buffer.add(new Message(c));
		}
		return retour;
	}

	private ArrayList<Message> parseTours(ArrayList<Tour> array)
	{
		ArrayList<Message> retour = new ArrayList<Message>();
		for (Tour t : array)
		{
			buffer.add(new Message(t));
		}
		return retour;
	}

	private class Message
	{
		Message(Creature c)
		{
		}

		Message(Tour t)
		{
		}
	}

}
