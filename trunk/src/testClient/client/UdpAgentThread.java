package testClient.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

import testClient.protocol.ClientPacket;
import testClient.protocol.ServerPacket;
import testClient.protocol.ClientPacket.ClientCommand;

class UdpAgentThread extends Thread
{

	private int udpPortServer = -1; // Port sur le server
	private DatagramSocket udpSocket = null; // La socket UDP
	private DatagramPacket receive = null; // Le datagram que l'on reçoit
	private byte[] inputPacketBuffer = null; // Le buffer d'entrée
	private Client client = null; // Le client lié à l'UDPAgent
	private InetAddress destination = null; // L'adresse du server

	/** Creates a new instance of UDPAgentThread: opens the UDP socket */
	UdpAgentThread(Client client) throws IOException
	{
		log("Openning local socket on " + getLocalPort());
		this.client = client;
		udpSocket = new DatagramSocket(getLocalPort());
	}

	/** Gets the server address and port from the Client object */
	public void connect(String serverHostname, int serverPort)
			throws IOException
	{
		log("New UDP connexion to " + serverHostname + ":" + serverPort);
		this.udpPortServer = serverPort;
		destination = InetAddress.getByName(serverHostname);
		inputPacketBuffer = new byte[ServerPacket.MAX_PACKET_SIZE];
		receive = new DatagramPacket(inputPacketBuffer,
				inputPacketBuffer.length);

		this.start();
	}

	/**
	 * Envoie les données de type datagram sur la socket
	 * 
	 * @param message
	 */
	public void send(byte[] message)
	{
		// Send Datagram to the server
		try
		{
			udpSocket.send(new DatagramPacket(message, message.length,
					destination, udpPortServer));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Lit les données sur la socket
	 * 
	 * @return
	 * @throws IOException
	 */
	public byte[] get() throws IOException
	{
		udpSocket.receive(receive);
		return receive.getData();
	}

	/**
	 * Ferme la socket UDP
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception
	{
		log("Disconnect");
		udpSocket.close();
	}

	/**
	 * Log un message
	 * 
	 * @param message
	 */
	private void log(String message)
	{
		Logger.log("[\033[34mUDPAgent\033[00m] " + message);
	}

	/**
	 * Creates a ServerPacket object to receive the server update packet, parses
	 * it (using the ServerPacket.parse method) and calls the
	 * client.handleUpdates method to notify the client object about the
	 * received data.
	 */
	public void run()
	{
		log("Starting UDP Thread");
		ServerPacket packet = new ServerPacket();
		ByteBuffer buffy = packet.getByteBuffer();
		while (true)
		{
			try
			{
				// Avoid the buffer
				buffy.clear();
				// put the data in the buffer
				buffy.put(get());
				// client.handleUpdates(packet.parse());
			} catch (IOException e)
			{
				Logger.err("IOError : " + e.getMessage());
				e.printStackTrace();
			} catch (Exception e)
			{
				if (FSM.tcp != null)
					try
					{
						FSM.tcp.send("Error: " + e.getMessage());
					} catch (IOException e1)
					{
						e1.printStackTrace();
					}
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates a ClientPacket object, fills it with update data (using the
	 * ClientPacket.fill method) and sends it to the server.
	 */
	void sendUpdate(byte heading, byte speed, boolean shooting)
	{
		ClientPacket packet = new ClientPacket();
		packet.fill(new ClientCommand(heading, speed, shooting));
		send(packet.getByteBuffer().array());
	}

	/**
	 * Returns the local UDP port (used in the TCP prolog).
	 * 
	 * Le la méthode garantie de choisir un port ouvert plus grand que 1024
	 * (usermode)
	 */
	int cache = -1;

	int getLocalPort()
	{
		if (cache == -1)
		{
			cache = 1024 + (int) (Math.random() * 1337) + 1;
			do
			{
				try
				{
					(new DatagramSocket(cache)).close();
					break;
				} catch (SocketException e)
				{
					Logger
							.err("Port " + cache
									+ " is not ready for playing...");
					if (++cache > 65535)
					{
						Logger.err("No port available");
						System.exit(0);
					}
				}
			} while (true);
			log("The new UDP Port is " + cache);
		}
		return cache;
	}

	public void send(String substring)
	{
		// TODO Auto-generated method stub
		
	}
}