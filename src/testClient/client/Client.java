package testClient.client;

import java.util.Set;

import testClient.protocol.ServerPacket.FieldObjectInfo;

/**
 * Simple client de test pour dialoguer avec le serveur
 * @author Pierre-Do
 *
 */
public class Client
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}
	
	public void log(String msg){
	
	}

	public String getVersion()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void initializeField(short weight, short heigth)
	{
		// TODO Auto-generated method stub
		
	}

	public String getPlayerName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void playerJoined(byte parseByte, String string)
	{
		// TODO Auto-generated method stub
		
	}

	public void playerLeft(byte id)
	{
		// TODO Auto-generated method stub
		
	}

	public void receivedScore(byte id, int parseInt)
	{
		// TODO Auto-generated method stub
		
	}

	public void receivedMessage(byte id, String string)
	{
		// TODO Auto-generated method stub
		
	}

	public void receivedAnnouncement(String param)
	{
		// TODO Auto-generated method stub
		
	}

	public void initializeServerUdpPort(int udpPortServer)
	{
		// TODO Auto-generated method stub
		
	}

	public String getUdpPort(boolean activePlayer)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void initializePlayer(String name, byte iD)
	{
		// TODO Auto-generated method stub
		
	}

	public void handleUpdates(Set<FieldObjectInfo> parse)
	{
		// TODO Auto-generated method stub
		
	}

}
