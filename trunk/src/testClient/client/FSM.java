/**
 * Ceci est le coeur du programme, la Finite State Machine qui contrôle
 * l'état du client.
 */

package testClient.client;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.regex.*;

public class FSM {

    /**
     * Les variables d'états
     */
    public final static int STATE_ERROR 	= -1;
    public final static int STATE_INIT 		=  0;
    public final static int STATE_REGISTER 	=  1;
    public final static int STATE_PORT 		=  2;
    public final static int STATE_UPDATE 	=  3;

    /**
     * Le Thread de contrôle TCP
     */
    public static TcpControlThread tcp 		=  null;
    
    private int state 				=  0;		// L'état courant
    private int udpPortServer 			=  0;		// Le port UDP du server
    private boolean activePlayer 		=  false;	// Si le player est actife
    private String serverVersion 		=  null;	// La version du serveur
    private Client client 			=  null;	// L'instance du client
    
    /**
     * Donne le nom de l'état courant
     * @return Le nom de l'état courant
     */
    private String getCurrentStateName() {
	switch (state) {
	case STATE_ERROR:
	    return "ERROR";
	case STATE_INIT:
	    return "INIT";
	case STATE_REGISTER:
	    return "REGISTER";
	case STATE_PORT:
	    return "PORT";
	case STATE_UPDATE:
	    return "UPDATE";
	default:
	    // Si l'état non trouvé dans la liste
	    return "Unknow state : " + state;
	}
    }

    /**
     * Constructeur, initialise une FSM
     * @param client La référence vers le Client
     * @param tcp La référence vers le TCPControlThread qui appelle la FSM
     */
    public FSM(Client client, TcpControlThread tcp) {
	FSM.tcp = tcp;
	this.client = client;
	try {
	    finiteStateMachine();
	} 
	// En cas d'erreur exception générique
	catch (Exception e) {
	    error(e.getMessage());
	    e.printStackTrace();
	}
    }

    /**
     * Raccourcis pour Logger.log(msg)
     * @param message Le message à logguer
     */
    private void log(String message) {
	Logger.log(message);
    }

    /**
     * Loggue le message vers l'objet Logger et vers le client, 
     * s'il existe...
     * @param message Le message à logguer
     */
    private void error(String message) {
	Logger.err(message);
	if (client != null) client.log(message);
    }

    /**
     * Méthode principale de la classe : la FSM en elle même.
     * Techniquemene une boucle while infinie avec un case sur
     * la variable state pour déterminer l'état courant. Implémentation
     * la plus commune pour ce genre de pattern.
     * 
     * @throws IOException En cas IOException
     */
    private void finiteStateMachine() throws IOException {
	while (true) {
	    try {
		log("*** Next Stage : \033[33m" + getCurrentStateName() + "\033[00m ****");
		switch (state) {
		/*
		 * Tier -1 : Error
		 */
		case STATE_ERROR:
		    error("FSM : Something wrong...");
		    tcp.exit();
		    break;
		/*
		 * Tier 0 : Wait for incoming connection 
		 * Client : $GAMENAME "client" $VERSION 
		 * ToClient : $GAMENAME "server" $VERSION 
		 * ToClient : Game :$WIDTH $HEIGHT $STATE 
		 * => Go State1 => $STATE = {open,full,closed}
		 */
		case STATE_INIT:
		    // Send a question to the server
		    tcp.send("CaveShooter client version " + client.getVersion());
		    // We wait for the answer
		    serverVersion = getNext(tcp.get(), "version ");
		    String[] tmp = tcp.get().split(" ");
		    // Parse the answer
		    short weight = Short.parseShort(tmp[1]);
		    short heigth = Short.parseShort(tmp[2]);
		    String gameState = tmp[3];
		    // Log results
		    log("Dim : " + weight + "x" + heigth + "");
		    log("Status : " + gameState);
		    // Launch client with weight and heigth
		    client.initializeField(weight, heigth);
		    /* Check version */
		    if (!serverVersion.equals(client.getVersion())) {
			// Error, bad version... this is not a fatal error
			log("Bad protocol version : Server is \"" + serverVersion + "\" and client \"" + client.getVersion() + "\"");
			state = STATE_REGISTER;
		    }
		    /* 
		     * Check game's state
		     */
		    /* The Game's close*/
		    if (gameState.equals("closed")) {
			error("You cannot pass !!! The Game is close !");
			state = STATE_ERROR;
		    }
		    /* The Game is full*/
		    else if (gameState.equals("full")) {
			log("The game is full, only spectator mode is allowed...");
			state = STATE_REGISTER;
		    }
		    /* Else OK */
		    else {
			// Enable player
			activePlayer = true;
			// Next state
			state = STATE_REGISTER;
		    }
		    break;

		/*
		 * Tier 1 : Wait for a player's name 
		 * Client : Player : $PLAYERNAME
		 * ToClient : 	if used => Player : name in use => Go State1 
		 * 		else => Player : Ok, id $ID => Go State2
		 */
		case STATE_REGISTER:
		    String name = client.getPlayerName();
		    tcp.send("Player: " + name);
		    String answer = tcp.get();
		    if (!answer.contains("name in use")) {
			byte ID = Byte.parseByte(getNext(answer, "id "));
			log("Player ID : " + ID);
			// New player
			client.initializePlayer(name, ID);
			state = STATE_PORT;
		    } else {
			error("The name \"" + name + "\" is already in use. Please try again...");
			state = STATE_REGISTER;
		    }
		    break;

		/*
		 * Tier 2 : Wait for UDP Port 
		 * Client : Port $UDP 
		 * ToClient : Port
		 * $UDP => Go State3
		 */
		case STATE_PORT:
		    log("Client active : " + activePlayer);
		    tcp.send("Port: " + client.getUdpPort(activePlayer));
		    udpPortServer = Integer.parseInt(tcp.get().split("Port:")[1].replace(" ", ""));
		    client.initializeServerUdpPort(udpPortServer);
		    state = STATE_UPDATE;
		    break;

		/*
		 * Tier 3 : Update in UDP <
		 * > $MSG TCP Routines : Client : Error :
		 * $MSG ToClient : Join : $ID $NAME Part : $ID Score : $ID $SCORE
		 * Msg : $MSG Msg : $ID $MSG Announce : $MSG Error : $MSG
		 */
		case STATE_UPDATE:
		    log("Wait for TCP messages and UDP connexion...");
		    // Wait for TCP Messages from the Server
		    while (true) parseUpdateMessages(tcp.get());

		    /*
		    * Default
		    */
		default:
		    error("FSM : Bad state \"" + state + "\"");
		    state = STATE_ERROR;
		    break;
		}

	    } catch (ProtocolException e) {
		error("Error : " + e.getMessage());
		tcp.send("Error: " + e.getMessage());
		e.printStackTrace();
		// Stop the FSM
		return;
	    }
	}
    }
    
    private String[] split(String message) throws IllegalStateException {
	return split(message, ": ");
    }

    public String getNext(String message, String expr) throws IllegalStateException {
	return split(message, expr)[1];
    }
    
    private String[] split(String message, String pattern) throws IllegalStateException {
	Pattern p = Pattern.compile("(.+?)" + pattern + "(.*)");
	Matcher m = p.matcher(message);
	// Return an array
	if (m.find())	 return new String[] { m.group(1), m.group(2) };
	else		 return new String[] { message };
    }

    
    /**
     * TODO QQCH de propre
     * @param message
     * @throws ProtocolException
     */
    private void parseUpdateMessages(String message) throws ProtocolException  {
	// Test if msg == null
	if (message.equals("null")) throw new ProtocolException("During the parsing : Null message from the server");
	else try {
	    
	    String[] tmp    = split(message);
	    String   cmd    = tmp[0];
	    String   param  = tmp[1];
	    String[] params = param.split(" ");
	    
	    if (cmd.equals("Error")) {
		// Go to the state : ERROR
		Logger.err(param);
		state = STATE_ERROR;
		throw new ProtocolException(param);
	    } else if (cmd.equals("Join")) {
		// Join: <id> <name>
		client.playerJoined(Byte.parseByte(params[0]), params[1]);
	    } else if (cmd.equals("Part")) {
		// Part: <id>
		byte id = Byte.parseByte(param);
		client.playerLeft(id);
	    } else if (cmd.equals("Score")) {
		// Score: <id> <score>
		byte id = Byte.parseByte(params[0]);
		client.receivedScore(id, Integer.parseInt(params[1]));
	    } else if (cmd.equals("Msg")) {
		// Msg: <id> <msg>
		byte id = Byte.parseByte(params[0]);
		client.receivedMessage(id, params[1]);
	    } else if (cmd.equals("Announce")) {
		// Msg: <message>
		log("Announce from server : "+param);
		client.receivedAnnouncement(param);
	    } else throw new ProtocolException("Protocole Error : No matching found");
	    
	} catch (NullPointerException e) {
	    error("ParseCMD : " + e.getMessage());
	    e.printStackTrace();
	    throw new ProtocolException("Protocole Error : Null pointer");
	} catch (IllegalStateException e){
	    error(e.getMessage());
	    e.printStackTrace();
	    throw new ProtocolException("Protocole Error : IllegalStateException");
	}
    }
}
