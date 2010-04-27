package testClient.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class CaveShooterClient implements Runnable {

    private static boolean QUIT;

    private final static String VERSION = "0.3";
    private final static String NAME = "CaveShooter";

    private final static int STATE_ERROR = -1;
    private final static int STATE_INIT = 0;
    private final static int STATE_REGISTER = 1;
    private final static int STATE_PORT = 2;
    private final static int STATE_UPDATE = 3;

    private int state = 0;
    private BufferedReader input = null;
    private static boolean spectator = false;

    private Thread mainThread = null;

    /*
     * Personnal config
     */
    private String playerName = "Akkenar";
    private int udpPort = 1337;
    private my_Client client;

    /*
     * ConfigServer
     */
    private static int tcpPortServer = 10250;
    private static String ipAdresseServer = "je.epfl.ch";

    private static TcpControlThread tcpAgent = null;
    private static UDPAgent udpAgent = null;

    /*
     * Server's variables
     */
    private String serverVersion = null;
    private String gameState = null;
    private int udpPortServer = 0;

    /**
     * @param args
     */
    public static void main(String[] args) {

	if (args.length > 0) {
	    ipAdresseServer = args[0];
	}
	
	CaveShooterClient instance = new CaveShooterClient();
	try {
	    while (true) {
		Thread.sleep(100);
		String msg = instance.getMessageFromUser();
		if (msg.startsWith("exit"))
		    instance.exit();
		else if (msg.startsWith("UDP "))
		    udpAgent.send(msg.substring(4));
		else
		    tcpAgent.send(msg);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

    }

    private CaveShooterClient() {
	log("*** Welcome in CaveShooter v." + VERSION + " ***");
	// Variables initialization
	state = 0;
	client = new my_Client(VERSION);
	input = new BufferedReader(new InputStreamReader(System.in));
	// Start the main Thread
	mainThread = new Thread(this);
	mainThread.start();
    }

    public void run() {
	try {
	    tcpAgent = new TcpControlThread(ipAdresseServer, tcpPortServer, null);
	    finiteStateMachine();
	} catch (IOException e) {
	    // IO Error : Timeout, connexion refused, etc...
	    error("Error IO : " + e.getMessage());
	    // e.printStackTrace();
	} catch (Exception e) {
	    // Other kind of error
	    error("Run : " + e.getClass() + " : " + e.getMessage());
	    e.printStackTrace();
	} finally {
	    exit();
	}
    }

    @SuppressWarnings("deprecation")
    public void exit() {
	synchronized (this) {
	    QUIT = true;
	    try {
		// Stopping main thread
		mainThread.stop();
	    } catch (Exception e) {
		error("Exit \"Stopping Thread\" : " + e.getMessage());
	    } finally {
		try {
		    // Disconnect sockets
		    tcpAgent.close();
		} catch (Exception e) {
		    error("Exit \"TCPClose\" : " + e.getMessage());
		} finally {
		    try {
			udpAgent.close();
		    } catch (Exception e) {
			error("Exit \"UDPClose\" : " + e.getMessage());
		    } finally {
			// In fine exitimus
			log("*** Bye bye ***");
			System.exit(0);
		    }
		}
	    }
	}
    }

    public String getMessageFromUser() throws IOException {
	return input.readLine();

    }

    private void finiteStateMachine() throws Exception {
	while (!QUIT) {
	    log("*** Next Stage : \033[33m" + getCurrentStateName() + "\033[00m ****");
	    switch (state) {
	    /*
	     * Tier -1 : Error
	     */
	    case STATE_ERROR:
		// TODO : Exception
		error("FSM : Something wrong...");
		exit();
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
		tcpAgent.send(NAME + " client version " + VERSION);
		// We wait for the answer
		serverVersion = Parser.getNext(tcpAgent.get(), "version ");

		String[] tmp = tcpAgent.get().split(" ");
		short wight = Short.parseShort(tmp[1]);
		short heigth = Short.parseShort(tmp[2]);
		client.initializeField(wight, heigth);

		gameState = tmp[3];
		log("Dim : " + wight + "x" + heigth + "");
		log("Status : " + gameState);
		/* Check version */
		if (!serverVersion.equals(VERSION)) {
		    // Error, bad version
		    error("Bad protocol version : Server is \"" + serverVersion + "\" and client \"" + VERSION + "\"");
		    state = STATE_REGISTER;
		}
		/* Check game's state*/
		else if (gameState.equals("close")) {
		    error("You cannot pass !!! Game is closed !");
		    state = STATE_ERROR;
		} else if (gameState.equals("full")) {
		    log("The game is full, only spectator mode are allowed...");
		    spectator = true;
		    state = STATE_REGISTER;
		}
		/* Else OK */
		else {
		    // Next state
		    state = STATE_REGISTER;
		}
		break;

	    /*
	     * Tier 1 : Wait for a player's name 
	     * Client : Player : $PLAYERNAME
	     * ToClient : 	if used => Player : name in use => Go State1 
	     * 			else => Player : Ok, id $ID => Go State2
	     */
	    case STATE_REGISTER:
		tcpAgent.send("Player: " + playerName);
		String answer = tcpAgent.get();
		if (answer.contains("name in use")) {
		    System.out.println("The name \"" + playerName + "\" is already in use. Please try again...");
		    playerName = getMessageFromUser();
		    state = STATE_REGISTER;
		} else {
		    byte playerID = Byte.parseByte(answer.split("id ")[1]);
		    // Put himself in the players' set
		    log("Player ID : " + playerID);
		    client.initializePlayer(playerName, playerID);
		    state = STATE_PORT;
		}
		break;

	    /*
	     * Tier 2 : Wait for UDP Port 
	     * Client : Port $UDP 
	     * ToClient : Port
	     * $UDP => Go State3
	     */
	    case STATE_PORT:
		boolean OK = false;
		do{
		try {
		    udpAgent = new UDPAgent(udpPort);
		    OK=true;
		} catch (IOException e) {
		    ++udpPort;
		}}while(!OK);
		tcpAgent.send("Port: " + udpPort);
		udpPortServer = Integer.parseInt(tcpAgent.get().split("Port:")[1].replace(" ", ""));
		state = STATE_UPDATE;
		udpAgent.setServer(ipAdresseServer, udpPortServer);
		break;

	    /*
	     * Tier 3 : Update in UDP <> $MSG TCP Routines : Client : Error :
	     * $MSG ToClient : Join : $ID $NAME Part : $ID Score : $ID $SCORE
	     * Msg : $MSG Msg : $ID $MSG Announce : $MSG Error : $MSG
	     */
	    case STATE_UPDATE:
		log("Wait for TCP messages and UDP connexion..");
		parseUpdateMessages(tcpAgent.get());
		break;
	    /*
	     * Default
	     */
	    default:
		error("FSM : Bad state \"" + state + "\"");
		state = STATE_ERROR;
		break;
	    }
	}
    }

    private void parseUpdateMessages(String message) throws ProtocoleException {
	// Test if msg == null
	if (message.equals("null"))
	    new ProtocoleException("Null message from the server");
	try {
	    String[] msg = message.split(": ");
	    String cmd = msg[0];
	    String param = msg[1];
	    String[] params = param.split(" ");
	    if (cmd.equals("Error")) {
		// Go to the state : ERROR
		Logger.err(param);
		state = STATE_ERROR;
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
		client.receivedAnnouncement(param);
	    }
	} catch (NullPointerException e) {
	    error("ParseCMD : " + e.getMessage());
	    new ProtocoleException("Protocole Error");
	}
    }

    private static void log(String message) {
	Logger.log(message);
    }

    private static void error(String message) {
	Logger.err(message);
    }

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
	    return "Unknow state : " + state;
	}
    }
}
