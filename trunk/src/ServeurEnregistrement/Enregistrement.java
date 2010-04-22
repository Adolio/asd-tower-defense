package ServeurEnregistrement;

import Reseau.*;

/**
 * 
 * @author lazhar
 *
 */
public class Enregistrement {
   
   private String nom;
   private String adresseIp;
   private Port port;
   
   /**
    * 
    * @param nom
    * @param adresseIp
    * @param port
    */
   public Enregistrement(String nom, String adresseIp, Port port)
   {
      this.nom = nom;
      this.adresseIp = adresseIp;
      this.port = port;
   }

   /**
    * @return the nom
    */
   public String getNom() {
      return nom;
   }

   /**
    * @param nom the nom to set
    */
   public void setNom(String nom) {
      this.nom = nom;
   }

   /**
    * @return the adresseIp
    */
   public String getAdresseIp() {
      return adresseIp;
   }

   /**
    * @param adresseIp the adresseIp to set
    */
   public void setAdresseIp(String adresseIp) {
      this.adresseIp = adresseIp;
   }

   /**
    * @return the port
    */
   public Port getPort() {
      return port;
   }

   /**
    * @param port the port to set
    */
   public void setPort(Port port) {
      this.port = port;
   }

   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      return "Enregistrement [adresseIp=" + adresseIp + ", nom=" + nom
            + ", port=" + port + "]";
   }
   
}
