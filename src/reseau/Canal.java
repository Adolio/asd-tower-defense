package reseau;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Cette classe implémente un canal de transmission sur lequel on peut envoyer
 * diverses données ainsi qu'en lire.
 * 
 * @author Lazhar Farjallah
 * 
 */
public class Canal
{

	// La socket qui est associée à ce côté du canal.
	private Socket socket = null;

	// Les flux In/Out sont utilisés pour lire/écrire depuis/sur le canal.
	private ObjectInputStream canalIn = null;
	private ObjectOutputStream canalOut = null;

	// Pour savoir si on affiche les messages de debug dans la console
	// (pratique...).
	private boolean afficherMessagesDebug = false;

	/**
	 * L'appelant de ce constructeur veut attendre des connexions entrantes sur
	 * le port donné en paramètre ou sinon chercher une connexion qui est déjà
	 * en attente. Ce constructeur ne se termine pas tant qu'une connexion n'est
	 * pas établie. Si un problème survient, une exception de type
	 * CanalException est levée. Il s'agit alors d'une erreur fatale et le
	 * programme devrait se terminer.
	 * 
	 * @param port
	 *            Le port associé à ce côté du canal
	 * @param afficherMessagesDebug
	 *            Pour afficher ou non les messages de debug en console
	 * @throws CanalException
	 *             Si un problème de connexion survient
	 */
	public Canal(Port port, boolean afficherMessagesDebug)
			throws CanalException
	{
		this.afficherMessagesDebug = afficherMessagesDebug;

		try
		{
			if (afficherMessagesDebug)
			{
				System.out
						.print("     Canal: en attente de connexion sur le port <"
								+ port.getNumeroPort() + "> à l'adresse ");
				for (NetworkInterface netint : Collections
						.list(NetworkInterface.getNetworkInterfaces()))
				{
					for (InetAddress inetAddress : Collections.list(netint
							.getInetAddresses()))
					{
						if (!inetAddress.toString().contains(":")
								&& !inetAddress.toString()
										.contains("127.0.0.1"))
						{
							System.out.print("["
									+ inetAddress.toString().substring(1)
									+ "] ou ");
						}
					}
				}
				System.out.println("[127.0.0.1]\n");
			}

			// Accepter une connexion, c'est-à-dire soit prendre une connexion
			// en attente ou
			// alors attendre jusqu'à ce que quelqu'un se connecte. Attention,
			// cette ligne
			// est bloquante jusqu'à ce qu'une connexion soit disponible.
			socket = port.getServerSocket().accept();

			if (afficherMessagesDebug)
			{
				System.out.println("     Canal: connexion établie");
			}

			// Configurer les flux entrant/sortant pour la lecture et l'écriture
			// sur le
			// canal de transmission.
			configurerFlux();
		} catch (Exception e)
		{
			System.out
					.println("Canal: une erreur est survenue pendant l'attente d'une connexion entrante sur le port "
							+ port.getNumeroPort());
			e.printStackTrace();
			throw new CanalException(e);
		}
	}

	/**
	 * Ce constructeur permet à son appelant de se connecter à l'adresse IP
	 * donnée et sur le port donnés en paramètres. Cela attendra que l'autre
	 * côté du canal réponde. Si la connexion est refusée, une exception
	 * java.net.ConnectException est levée. Ce n'est cependant pas une erreur
	 * fatale, et le programme peut tout aussi bien continuer si cette denière
	 * est catchée. Si la connexion ne peut être établie pour une raison
	 * quelconque, une exception de type CanalException est levée. Cette fois,
	 * il s'agit d'une erreur fatale et le programme devrait être interrompu.
	 * 
	 * @param adresseIp
	 *            L'adresse IP vers laquelle on veut créer un canal
	 * @param numeroPort
	 *            Le numéro de port de l'adresse IP
	 * @param afficherMessagesDebug
	 *            Si on veut afficher les messages de debug
	 * @throws ConnectException
	 *             Si la connexion est refusée à cette IP + Port
	 * @throws CanalException
	 *             Si une erreur de connexion survient
	 */
	public Canal(String adresseIp, int numeroPort, boolean afficherMessagesDebug)
			throws ConnectException, CanalException
	{
		this.afficherMessagesDebug = afficherMessagesDebug;

		try
		{
			if (afficherMessagesDebug)
			{
				System.out
						.println("     Canal: tentative de connexion à l'adresse "
								+ adresseIp + " sur le port " + numeroPort);
			}

			// Créée une connexion, c'est-à-dire une socket vers l'IP et le port
			// donnés et
			// attend qu'il y ait une réponse.
			socket = new Socket(adresseIp, numeroPort);

			if (afficherMessagesDebug)
			{
				System.out.println("     Canal: connexion établie");
			}

			// Configurer les flux entrant/sortant pour la lecture et l'écriture
			// sur le
			// canal de transmission.
			configurerFlux();
		} catch (ConnectException e)
		{
			// La connexion a été refusée. Pas considéré comme une erreur
			// fatale.
			if (afficherMessagesDebug)
			{
				System.out.println("     Canal: connexion refusée");
			}
			throw e;
		} catch (Exception e)
		{
			// Un problème de connexion est survenu. Considéré comme une erreur
			// fatale.
			System.out
					.println("Canal: une erreur est survenue lors de la tentative de connexion à l'adresse "
							+ adresseIp + " sur le port " + numeroPort);
			e.printStackTrace();
			throw new CanalException(e);
		}
	}

	/**
	 * Cette méthode est appelée quand une socket a été établie entre les 2
	 * extrémités connectées. Elle configure les flux d'entrée/sortie pour le
	 * canal courant. Si un problème est rencontré, une exception de type
	 * CanalException est levée.
	 * 
	 * @throws CanalException
	 *             Si un problème de flux survient.
	 */
	public void configurerFlux() throws CanalException
	{
		try
		{
			// Récupérer les flux déjà associés à la socket créée.
			InputStream generalIn = socket.getInputStream();
			OutputStream generalOut = socket.getOutputStream();

			// Nous voulons écrire toutes sortes d'informations sur le canal. Le
			// filtre le
			// plus approprié est donc Object.
			// On crée d'abord le flux de sortie puis on le flush() avant de
			// créer le flux
			// d'entrée, sinon le constructeur du flux d'entrée peut bloquer
			// (voir la doc
			// de l'API pour plus d'infos). Cela ne convient qu'aux flux
			// d'objets, et non
			// aux flux de données (Data Streams).
			// Il ne faut enfin pas oublier d'envoyer des objets sur le flux de
			// sortie qui
			// sont sérialisés (ils doivent implémenter java.io.Serializable).
			canalOut = new ObjectOutputStream(generalOut);
			canalOut.flush();
			canalIn = new ObjectInputStream(generalIn);
		} catch (Exception e)
		{
			System.out
					.println("Canal: une erreur est survenue pendant la configuration des flux In/Out");
			e.printStackTrace();
			throw new CanalException(e);
		}
	}

	/**
	 * Cette méthode envoie un message sur le canal de transmission sous forme
	 * de String.
	 * 
	 * @param message
	 *            Le String à envoyer.
	 * @throws CanalException
	 *             Si un problème de transmission survient.
	 */
	public void envoyerString(String message) throws CanalException
	{
		if (afficherMessagesDebug)
		{
			System.out.println("     Canal: envoi du String " + message);
		}

		try
		{
			canalOut.writeUTF(message);
			// Ne pas oublier de vider le flux de sortie après avoir écrit
			// dessus!
			canalOut.flush();
		} catch (Exception e)
		{
			System.out
					.println("Canal: une erreur est survenue pendant l'envoi du String");
			e.printStackTrace();
			throw new CanalException(e);
		}
	}

	/**
	 * Cette méthode permet d'attendre de recevoir un message de type String
	 * venant du canal de transmission.
	 * 
	 * @return
	 * @throws CanalException
	 */
	public String recevoirString() throws CanalException
	{
		String message = null;
		if (afficherMessagesDebug)
		{
			System.out.println("     Canal: en attente d'un String...");
		}

		try
		{
			message = canalIn.readUTF();
		}

		catch (Exception e)
		{
			e.printStackTrace();
			throw new CanalException(e);
		}
		if (afficherMessagesDebug)
		{
			System.out.println("     Canal: réception du String " + message);
		}
		return message;
	}

	/**
	 * Cette méthode envoie un message sur le canal de transmission sous forme
	 * de int.
	 * 
	 * @param i
	 * @throws CanalException
	 */
	public void envoyerInt(int i) throws CanalException
	{
		if (afficherMessagesDebug)
		{
			System.out.println("     Canal: envoi de l'int " + i);
		}

		try
		{
			canalOut.writeInt(i);
			// Ne pas oublier de vider le flux de sortie après avoir écrit
			// dessus!
			canalOut.flush();
		} catch (Exception e)
		{
			System.out
					.println("Canal: une erreur est survenue pendant l'envoi de l'int");
			e.printStackTrace();
			throw new CanalException(e);
		}
	}

	/**
	 * Cette méthode permet d'attendre de recevoir un message de type int venant
	 * du canal de transmission.
	 * 
	 * @return
	 * @throws CanalException
	 */
	public int recevoirInt() throws CanalException
	{
		int intRecu = 0;
		if (afficherMessagesDebug)
		{
			System.out.println("     Canal: en attente d'un int...");
		}

		try
		{
			intRecu = canalIn.readInt();
		} catch (Exception e)
		{
			System.out
					.println("Canal: une erreur est survenue pendant l'attente de réception d'un int");
			e.printStackTrace();
			throw new CanalException(e);
		}
		if (afficherMessagesDebug)
		{
			System.out.println("     Canal: réception de l'int " + intRecu);
		}
		return intRecu;
	}

	/**
	 * Cette méthode envoie un message sur le canal de transmission sous forme
	 * de double.
	 * 
	 * @param d
	 * @throws CanalException
	 */
	public void envoyerDouble(double d) throws CanalException
	{
		if (afficherMessagesDebug)
		{
			System.out.println("     Canal: envoi du double " + d);
		}

		try
		{
			canalOut.writeDouble(d);
			// Ne pas oublier de vider le flux de sortie après avoir écrit
			// dessus!
			canalOut.flush();
		} catch (Exception e)
		{
			System.out
					.println("Canal: une erreur est survenue pendant l'envoi du double");
			e.printStackTrace();
			throw new CanalException(e);
		}
	}

	/**
	 * Cette méthode permet d'attendre de recevoir un message de type double
	 * venant du canal de transmission.
	 * 
	 * @return
	 * @throws CanalException
	 */
	public double recevoirDouble() throws CanalException
	{
		double doubleRecu = 0.0;
		if (afficherMessagesDebug)
		{
			System.out.println("     Canal: en attente d'un double...");
		}

		try
		{
			doubleRecu = canalIn.readDouble();
		} catch (Exception e)
		{
			System.out
					.println("Canal: une erreur est survenue pendant l'attente de réception d'un double");
			e.printStackTrace();
			throw new CanalException(e);
		}
		if (afficherMessagesDebug)
		{
			System.out.println("     Canal: réception du double " + doubleRecu);
		}
		return doubleRecu;
	}

	/**
	 * Cette méthode envoie un message sur le canal de transmission sous forme
	 * de tableau de bytes.
	 * 
	 * @param b
	 * @throws CanalException
	 */
	public void envoyerBytes(byte[] b) throws CanalException
	{
		if (afficherMessagesDebug)
		{
			System.out.println("     Canal: envoi de " + b.length + " bytes");
		}

		try
		{
			canalOut.write(b);
			// Ne pas oublier de vider le flux de sortie après avoir écrit
			// dessus!
			canalOut.flush();
		} catch (Exception e)
		{
			System.out
					.println("Canal: une erreur est survenue pendant l'envoi des bytes");
			e.printStackTrace();
			throw new CanalException(e);
		}
	}

	/**
	 * Cette méthode permet d'attendre de recevoir un message de type tableau de
	 * bytes venant du canal de transmission.
	 * 
	 * @param size
	 * @return
	 * @throws CanalException
	 */
	public byte[] recevoirBytes(int size) throws CanalException
	{
		byte[] bytesRecus = new byte[size];
		if (afficherMessagesDebug)
		{
			System.out.println("     Canal: en attente de " + size
					+ " bytes ...");
		}

		try
		{
			canalIn.readFully(bytesRecus);
		} catch (Exception e)
		{
			System.out
					.println("Canal: une erreur est survenue pendant l'attente de réception des bytes");
			e.printStackTrace();
			throw new CanalException(e);
		}
		if (afficherMessagesDebug)
		{
			System.out.println("     Canal: réception de " + bytesRecus.length
					+ " bytes");
		}
		return bytesRecus;
	}

	/**
	 * Cette méthode envoie un message sur le canal de transmission sous forme
	 * de Paquet (objet de la classe Paquet).
	 * 
	 * @param p
	 * @throws CanalException
	 */
	public void envoyerPaquet(Paquet p) throws CanalException
	{
		if (afficherMessagesDebug)
		{
			System.out.println("     Canal: envoi du paquet " + p);
		}

		try
		{
			canalOut.writeObject(p);
			// Ne pas oublier de vider le flux de sortie après avoir écrit
			// dessus!
			canalOut.flush();
		}

		catch (Exception e)
		{
			System.out
					.println("Canal: une erreur est survenue pendant l'envoi du paquet");
			e.printStackTrace();
			throw new CanalException(e);
		}
	}

	/**
	 * Cette méthode permet d'attendre de recevoir un message de type Paquet
	 * (objet de la classe Paquet) venant du canal de transmission.
	 * 
	 * @return
	 * @throws CanalException
	 */
	public Paquet recevoirPaquet() throws CanalException
	{
		Paquet paquetRecu = null;
		if (afficherMessagesDebug)
		{
			System.out.println("     Canal: en attente d'un paquet ...");
		}

		try
		{
			paquetRecu = (Paquet) (canalIn.readObject());
			if (paquetRecu != null && afficherMessagesDebug)
			{
				System.out.println("     Canal: réception du paquet "
						+ paquetRecu
						+ " contenant "
						+ (paquetRecu.getOctets() == null ? 0 : paquetRecu
								.getOctets().length) + " bytes");
			}
		} catch (Exception e)
		{
			System.out
					.println("Canal: une erreur est survenue pendant l'attente de réception d'un paquet");
			e.printStackTrace();
			throw new CanalException(e);
		}
		return paquetRecu;
	}

	public String getIpClient()
	{
		return socket.getInetAddress().toString().substring(1);
	}

	/**
	 * TODO : Ajouter des méthodes pour envoyer des objets sérializés
	 * quelconques.
	 */

	/**
	 * Cette méthode permet de fermer correctement le canal en fermant les flux
	 * d'entrée/ sortie ainsi que les deux sockets de chaque côté du canal.
	 * 
	 * @throws CanalException
	 *             Si un problème de fermeture des flux/Sockets survient.
	 */
	public void fermer() throws CanalException
	{
		try
		{
			canalIn.close();
			canalOut.close();
			socket.close();
		} catch (Exception e)
		{
			System.out
					.println("Canal: une erreur est survenue pendant la fermeture du canal");
			e.printStackTrace();
			throw new CanalException(e);
		}
	}
}
