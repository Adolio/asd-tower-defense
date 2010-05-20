package reseau;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * @author lazhar
 * 
 */
public class PaquetTest
{

   byte[] b = new byte[100];
   Paquet p = new Paquet(10, b);

   
   /**
    * Test method for {@link reseau.Paquet#Paquet(int, byte[])}.
    */
   @Test
   public void testPaquet()
   {
      b = new byte[100];
      p = new Paquet(10, b);
      assertEquals(p.getEnTete(), 10);
      assertArrayEquals(new byte[100], b);
   }
   
   /**
    * Test method for {@link reseau.Paquet#getOctets()}.
    */
   @Test
   public void testGetOctets()
   {
      b = new byte[30];
      p = new Paquet(7, b);
      assertEquals(p.getOctets(), b);
   }
   
   
   /**
    * Test method for {@link reseau.Paquet#getEnTete()}.
    */
   @Test
   public void testGetEnTete()
   {
      b = new byte[430];
      p = new Paquet(17, b);
      assertEquals(p.getEnTete(), 17);
   }
   
}
