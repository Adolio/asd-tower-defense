/*
 * ServerPacket.java
 *
 * Created on October 19, 2007, 10:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package testClient.protocol;

import java.nio.ByteBuffer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import testClient.client.ProtocoleException;

/**
 * This class is responsible for creating/reading server update packets.
 * 
 * The client will create one or many ServerPacket objects. When the client
 * receives a packet, it will fetch the internal ByteBuffer with the
 * getByteBuffer() method, and fill it with the contents of the packets. It will
 * then call the parse() method.
 * 
 * You must implement the parse method so that it reads the contents of the
 * packet and returns a collection of FieldObject
 * 
 * @author max
 */
public class ServerPacket
{

	public static int MAX_PACKET_SIZE = 64;

	public static class FieldObjectInfo
	{
		public final byte id;
		public final short x;
		public final short y;
		public final byte heading;

		FieldObjectInfo(byte id, short x, short y, byte heading)
		{
			this.id = id;
			this.x = x;
			this.y = y;
			this.heading = heading;
		}
	}

	/**
	 * This is the internal ByteBuffer !
	 */
	private final ByteBuffer buffy;

	/** Creates a new instance of ServerPacket */
	public ServerPacket()
	{
		buffy = ByteBuffer.allocate(MAX_PACKET_SIZE);
	}

	/**
	 * Returns the internal ByteBuffer backing the packet, so that it can be
	 * sent over the wire.
	 * 
	 * @return The bytebuffer to send to the server
	 */
	public ByteBuffer getByteBuffer()
	{
		return buffy;
	}

	/**
	 * Reads the values from the Field object, and creates a packet from it in
	 * the internal ByteBuffer object. This method is only used by the server.
	 *//*
	public void fill(Field field)
	{
		buffy.clear(); // Use this before filling the buffer
		Map<Byte, FieldObjectInfo> m = field.getObjects();

		buffy.put((byte) 0x20);

		buffy.put((byte) m.size());
		synchronized (m)
		{
			for (FieldObjectInfo e : m.values())
			{
				buffy.put(e.getID());
				buffy.putShort(e.getX());
				buffy.putShort(e.getY());
				buffy.put(e.getHeading());
			}
		}
		buffy.limit(buffy.position()); // This marks the end of the packet.
		// If you forget it, your packet
		// will have trailing NULLs
		buffy.rewind();

	}*/

	/**
	 * This method is used by the client to read the contents of a server
	 * Update.
	 */
	public Set<FieldObjectInfo> parse() throws ProtocoleException
	{
		buffy.rewind();
		Set<FieldObjectInfo> set = new HashSet<FieldObjectInfo>();

		/**
		 * La spec du protocole Packet : [ type = 1 byte ][ objCount = 1 byte][
		 * Objects = n*6 byte ] Object : [ R = 1 bit + ID = 7 bits ][ x = 2
		 * bytes ][ y = 2 bytes ][ heading = 1 byte ]
		 */
		byte type = buffy.get();
		if (type != 0x20)
			throw new ProtocoleException("UDP Server : Bad type");

		int objCount = buffy.get();
		for (int i = 0; i < objCount; ++i)
		{
			byte id = buffy.get();
			short x = buffy.getShort();
			short y = buffy.getShort();
			byte heading = buffy.get();
			set.add(new FieldObjectInfo(id, x, y, heading));
		}

		return set;
	}
}
