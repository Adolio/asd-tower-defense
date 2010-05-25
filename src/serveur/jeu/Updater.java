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
	// Thread actif
	private Thread thread;
	// Liste des clients
	private HashMap<Integer, JoueurDistant> clients;
	// Buffer des messages
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
		// Extraction des nouvelles données
		ServeurJeu srv = (ServeurJeu) o;
		parseCreatures(srv.getCreatures());
		parseTours(srv.getTours());
		// Réveille le thread s'il dort
		this.notify();
	}

	@Override
	public void run()
	{
		while (true)
		{
			// Bloque le thread si le buffer est vide
			if (buffer.size() == 0)
			{
				try
				{
					this.wait();
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			// Itération sur le prochain message en attente
			Message message = buffer.poll();
			// Signalisation aux clients du message
			for (Entry<Integer, JoueurDistant> joueur : clients.entrySet())
				joueur.getValue().update(message.toString());
			
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
		
		public String toString(){
			return "Test";
		}
	}

}
