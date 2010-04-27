package testClient.client;

import java.util.HashMap;

/** The Client class is responsible for the logic of the game client,
  * and gluing together the networking part and the UI part. A single
  * instance of this class is used, we call it the &quot;Client object&quot;.
  */
public class my_Client {
    
    /** Variables */
    private String version;
    private int fieldWidth,fieldHeight;
    private String name;
    private byte id;
    private int udpPort;
    private int score;
    
    private HashMap<Byte, String> players = null;
    
    public my_Client(String version){
	players = new HashMap<Byte, String>();
    }
    
    /** Use this method to display message in the client terminal.
      * VERY USEFUL!
      */
    void log(String msg){
	Logger.log(msg);
    }

    /** You can use this method to retrieve the client version.
      */
    String getVersion(){
	return version;
    }

    /** OBSOLETE
      */
    boolean isCompatibleWith(String serverVersion){
	return serverVersion.equals(version);
    }

    /** Use this method to inform the Client object about the game
      * field size.
      */
    void initializeField(short fieldWidth, short fieldHeight){
	this.fieldHeight= fieldHeight;
	this.fieldWidth = fieldWidth;
    }

    /** Use this method to inform the Client object your player name
      * and ID.
      */
    void initializePlayer(String name, byte id){
	this.name = name;
	this.id = id;
	players.put(id, name);
    }

    /** Use this method to inform the Client object about the server
      * UDP port.
      */
    void initializeServerUdpPort(int port){
	this.udpPort = port;
    }

    /** Use this method to retrieve the UDP port of your client.
      */
    int getUdpPort(){
	return udpPort;
    }

    /** Use this method to inform the Client object about a player
      * that joined the game.
      */
    void playerJoined(byte id, String name){
	log(" ---> Player \" \033[35m" + name + "\033[00m\" $" + id + "joins the game");
	players.put(id,name);
    }

    /** Use this method to inform the Client object about a player
      * that left the game.
      */
    void playerLeft(byte id){
	String playerName = playerName(id);
	if(playerName.equals("null")){
	    log("Oops, a player attemps to connect the game...");
	}
	else {
	    log(" <--- Player \"" + playerName(id) + "\" with ID " + id + " left the game");
	    players.remove(id);
	}
    }
    
    String playerName(byte id){
	return players.get(id);
    }

    /** Use this method to inform the Client object about a score
      * of some player.
      */
    void receivedScore(byte id, int score){
	log("The score of player " + playerName(id) + " is " + score);    
    }

    /** Use this method to inform the Client object about a message
      * received from some player.
      */
    void receivedMessage(byte id, String msg){
	 log("Player " + playerName(id) + " sends \033[35m" + msg + "\033[00m");
	   
    }
    
    /** Use this method to inform the Client object about an
      * announcement received from the server.
      */
    void receivedAnnouncement(String msg){
	log("Server sends : " + msg);
    }

    /** Use this method to ask the user for a new player name.
      */
    String getPlayerName(){
	return name;
    }

}
