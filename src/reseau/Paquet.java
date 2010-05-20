package reseau;

/**
 * Cette classe représente un paquet qui peut être envoyé sur un canal de
 * communication. Un paquet contient un entier représentant l'en-tête (index) du
 * paquet ainsi qu'un tableau d'octets (bytes).
 * 
 * @author lazhar
 * 
 */
public class Paquet implements java.io.Serializable
{
   
   private static final long serialVersionUID = 1L;
   private int enTete;
   private byte[] octets;
   
   /**
    * 
    * @param enTete
    * @param octets
    */
   public Paquet(int enTete, byte[] octets)
   {
      this.enTete = enTete;
      this.octets = octets;
   }
   
   /**
    * 
    * @return
    */
   public byte[] getOctets()
   {
      return octets;
   }
   
   /**
    * @see java.io.Serializable#toString()
    */
   @Override
   public String toString()
   {
      if (octets != null)
      {
         return "Paquet avec en-tête " + enTete + " et " + octets.length + " octets.";
      }
      
      return "Paquet avec en-tête " + enTete + " et 0 octets (paquet vide).";
   }
   
   /**
    * @return the enTete
    */
   public int getEnTete()
   {
      return enTete;
   }
}
