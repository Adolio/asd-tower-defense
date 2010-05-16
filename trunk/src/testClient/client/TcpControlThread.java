package testClient.client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpControlThread extends Thread
{
	private UdpAgentThread udpAgent = null;
	private Client client = null;
	private Socket serverSocket = null;
	private String serverAddress = null;
	private int connectionTimeoutMillis = 2000;
	private int tcpServerPort = 0;
	private final char ENDOFLINE = '\n';

	public TcpControlThread(String address, int port, Client client)
			throws IOException
	{
		try
		{
			this.serverAddress = address;
			this.tcpServerPort = port;
			this.client = client;
			connect(InetAddress.getByName(serverAddress));
			new Thread(this).start();
		} catch (ConnectException e)
		{
			Logger.err("Connection error at " + serverAddress + ":"
					+ tcpServerPort);
			exit();
		}
	}

	/** Implements the TCP message exchange (FSM) */
	public void run()
	{
		log("Starting TCP Thread");
		new FSM(client, this);
	}

	/**
	 * This method is executed by the Client object when the user wants to send
	 * a message.
	 */
	synchronized void sendMessage(String message) throws IOException
	{
		send("Msg: " + message);
	}

	/**
	 * Send a string to the server
	 * 
	 * @param msg
	 * @throws IOException
	 */
	public void send(String message) throws IOException
	{
		log("[Send] \"" + message + "\"");
		if (serverSocket == null)
			throw new IOException("The serverSocket is not running...");
		else
			serverSocket.getOutputStream().write((message + "\n").getBytes());
	}

	/**
	 * Get an answer from the Server
	 * 
	 * @return
	 * @throws IOException
	 */
	public String get() throws IOException
	{
		String msg = readLine(serverSocket);
		log("[Get] \"" + msg + "\"");
		return msg;
	}

	/**
	 * Méthode personnelle... il existe une méthode de l'API, mais il est plus
	 * pédagogique de la comprendre par soi même
	 * 
	 * @param socket
	 * @return
	 * @throws IOException
	 */
	private String readLine(Socket socket) throws IOException
	{
		// Dynamique Buffer
		StringBuffer buffer = new StringBuffer();
		do
		{
			// read the next char in the socket
			int in = socket.getInputStream().read();
			// if -1 from the socket
			if (in == -1)
				throw new IOException("EOF from the Socket");
			// If end of line
			if (in == (int) ENDOFLINE)
				break;
			// Put the char in the buffer
			else
				buffer.append((char) in);
		} while (true);

		// Return the result as a String
		return buffer.toString();
	}

	public void close() throws IOException
	{
		log("Disconnect");
		if (serverSocket != null)
			serverSocket.close();
	}

	public void connect(InetAddress target) throws IOException
	{
		// Openning TCP Connection
		log("Connect to " + target.getHostAddress() + " at port "
				+ tcpServerPort);
		serverSocket = new Socket();
		serverSocket.connect(new InetSocketAddress(target, tcpServerPort),
				connectionTimeoutMillis);
		// Log : OK
		log("Connection OK");
	}

	public void exit()
	{
		log("Exiting");
		// Disconnect sockets
		try
		{
			this.close();
		} catch (Exception e)
		{
			error("Exit \"TCPClose\" : " + e.getMessage());
		} finally
		{
			try
			{
				udpAgent.close();
			} catch (Exception e)
			{
				error("Exit \"UDPClose\" : " + e.getMessage());
			} finally
			{
				// In fine exitimus
				log("*** Bye bye ***");
				System.exit(0);
			}
		}
	}

	public Socket getSocket()
	{
		return serverSocket;
	}

	public boolean isConnected()
	{
		return serverSocket.isConnected();
	}

	private void log(String message)
	{
		Logger.log("[TCPAgent] " + message);
	}

	private void error(String message)
	{
		Logger.err("[TCPAgent] " + message);
		if (client != null)
			client.log(message);
	}
}
