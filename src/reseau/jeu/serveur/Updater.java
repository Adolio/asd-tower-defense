package reseau.jeu.serveur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.json.JSONException;
import org.json.JSONObject;

import models.creatures.Creature;
import models.creatures.IDCreatures;
import models.tours.IDTours;
import models.tours.Tour;
import models.tours.*;

public class Updater implements Observer, Runnable, ConstantesServeurJeu,
		IDTours, IDCreatures
{
	// Thread actif
	private Thread thread;
	// Liste des clients
	private HashMap<Integer, JoueurDistant> clients;
	// Buffer des messages
	private ConcurrentLinkedQueue<Message> buffer;

	public Updater(HashMap<Integer, JoueurDistant> clients)
	{
		ServeurJeu.log("[UPDATER] Lancement de l'updater");
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
		synchronized (this)
		{
			notify();
		}
	}

	@Override
	public void run()
	{
		while (true)
		{
			// Itération sur le prochain message en attente
			Message message = buffer.poll();
			// Bloque le thread si le buffer est vide
			if (message == null)
			{
				try
				{
					synchronized (this)
					{
						this.wait();
					}
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			} else
			{
				// Signalisation aux clients du message
				for (Entry<Integer, JoueurDistant> joueur : clients.entrySet())
					joueur.getValue().envoyerSurCanalMAJ(message.toString());
			}

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
		JSONObject json = new JSONObject();

		Message(Creature c)
		{
			try
			{
				json.put("OBJECT", CREATURE);
			} catch (JSONException e)
			{
				e.printStackTrace();
			}
		}

		Message(Tour t)
		{
			try
			{
				json.put("TYPE", TOUR_AJOUT);
				json.put("JOUEUR", t.getPrioprietaire().getId());
				json.put("ID_TOUR", t.getId());
				json.put("X", t.getXi());
				json.put("Y", t.getYi());
				int type = -1;
				if (t instanceof TourArcher)
					type = TOUR_ARCHER;
				// TODO Faire la suite des tours
				json.put("TYPE_TOUR", type);
			} catch (JSONException e)
			{
				e.printStackTrace();
			}
		}

		public String toString()
		{
			return json.toString();
		}
	}
}
