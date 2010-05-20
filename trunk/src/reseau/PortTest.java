package reseau;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * @author lazhar
 * 
 */
public class PortTest
{
   
   Port port;
   
   /**
    * Test method for {@link reseau.Port#Port(int)}.
    */
   @Test
   public void testPortInt()
   {
      port = new Port(1234);
      assertNotNull(port);
      assertEquals(port.getNumeroPort(), 1234);
   }
   
   /**
    * Test method for {@link reseau.Port#Port(java.lang.String)}.
    */
   @Test
   public void testPortString()
   {
      port = new Port("1234");
      assertNotNull(port);
      assertEquals(port.getNumeroPort(), 1234);
   }
   
   /**
    * Test method for {@link reseau.Port#getNumeroPort()}.
    */
   @Test
   public void testGetNumeroPort()
   {
      port = new Port(54321);
      assertEquals(port.getNumeroPort(), 54321);
   }
   
   /**
    * Test method for {@link reseau.Port#reserver()}.
    */
   @Test
   public void testReserver()
   {
      port = new Port(1234);
      port.reserver();
      assertTrue(port.getServerSocket().isBound());
      assertNotNull(port.getServerSocket());
      port.liberer();
   }
   
   /**
    * Test method for {@link reseau.Port#liberer()}.
    */
   @Test
   public void testLiberer()
   {
      port = new Port(1234);
      port.reserver();
      port.liberer();
      assertTrue(port.getServerSocket().isClosed());
   }
   
}
