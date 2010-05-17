package serveur.jeu;

import reseau.Canal;

public class Client implements Runnable
{
	private Thread thread;
	private Canal canal;

	public Client(String ID, Canal canal)
	{
		System.out.println("Nouveau client ID " + ID);
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run()
	{
		// Envoit de la version du serveur au client
		canal.envoyerString(ServeurJeu.VERSION);
		while(true){
			canal.recevoirString();
		}
	}
}
