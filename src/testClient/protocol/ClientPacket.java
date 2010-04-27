/*
 * ServerPacket.java
 *
 * Created on October 19, 2007, 10:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package testClient.protocol;

import java.net.ProtocolException;
import java.nio.ByteBuffer;

/**
 * The ClientPacket class is responsible for creating and reading server updates.
 * 
 * The client will create one or many ClientPacket objects, then call the fill() method with the new contents of the Client Update to send.
 * 
 * You must implement the fill() method so that when it is called, it clears the internal ByteBuffer object and fills it with the new contents.
 * 
 * When fill() returns, the client will use the getByteBuffer() method to get the ByteBuffer object, and will send it over to the server.
 * 
 * @author max
 */
public class ClientPacket {

    /**
     * This class wraps the three relevant values in a ClientUpdate message.
     */
    public static class ClientCommand {
	public final byte heading;
	public final byte speed;
	public final boolean shot;

	public ClientCommand(byte heading, byte speed, boolean shot) {
	    this.heading = heading;
	    this.speed = speed;
	    this.shot = shot;
	}

	@Override
	public String toString() {
	    return "ClientUpdate/heading=" + heading + ",speed=" + speed + ",shot=" + shot;
	}

    }

    public static int MAX_PACKET_SIZE = 2048;

    /**
     * This is the internal bytebuffer !
     */
    private final ByteBuffer buffy;

    /**
     * Creates a new instance of ClientPacket. Remember that the client may decide to recycle a single ClientPacket object.
     */
    public ClientPacket() {
	buffy = ByteBuffer.allocate(MAX_PACKET_SIZE);
    }

    /**
     * Returns the internal ByteBuffer backing the packet, so that it can be sent over the wire.
     * 
     * @return The bytebuffer to send to the server
     */
    public ByteBuffer getByteBuffer() {
	return buffy;
    }

    /**
     * Implement this method so that it reads the values from the ClientCommand object, and create a packet from it in the internal ByteBuffer object.
     * 
     * You can either use the ByteBuffer object directly, or, if you're not confident about it, use the .array() method to get a byte[] array to work on.
     * 
     */
    public void fill(ClientCommand command) {
	buffy.clear(); // Clear the buffer

	/**
	 * Spec : [ type = 1 byte ][ heading = 1 byte][ shot = 1 bit + speed = 7 bits ]
	 */
	buffy.put((byte) 0x10);
	buffy.put(command.heading);
	// Concaténation du bit du shoot avec le byte de la vitesse
	// Vitesse évaluée sur 7 bits mais représentée sur 8.
	// Bit de poid fort = shoot
	byte shoot = (byte) 0x00;
	if (command.shot)
	    shoot = (byte) 0x80;
	buffy.put((byte) (command.speed | shoot));
   }

    /**
     * This method is used by the server to read the contents of a Client Update. You need not worry about it, but you can use it as an example of how to use a ByteBuffer
     * 
     * @throws java.net.ProtocolException
     * When the received packet did not match the format of a Client Update
     * @return A ClientCommand object representing the new command's content
     */
    public ClientCommand parse() throws ProtocolException {

	buffy.rewind(); // Call this before reading a buffer

	if (buffy.get() != (byte) 0x10)
	    throw new ProtocolException("UDP client packet with unknown magic number");

	byte heading = buffy.get();
	byte b = buffy.get();

	boolean shot = (b & 0x80) != 0;
	byte speed = (byte) (b & (byte) 0x7F);

	ClientCommand c = new ClientCommand(heading, speed, shot);
	System.err.println("[CPKT] parsed: " + c);
	return c;

    }

}
