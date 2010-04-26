package serveur.enregistrement;

import reseau.*;

/**
 * 
 * @author lazhar
 *
 */
public class Enregistrement {
   
   private String nomPartie;
   private String adresseIp;
   private Port port;
   private int capacite;
   private int placesRestantes;
   private String nomTerrain;
   private String mode;
   
   /**
    * 
    * @param nom
    * @param adresseIp
    * @param port
    */
   public Enregistrement(String nom, String adresseIp, Port port, int capacite,
                         String nomTerrain, String mode)
   {
      this.nomPartie = nom;
      this.adresseIp = adresseIp;
      this.port = port;
      this.capacite = capacite;
      this.placesRestantes = this.capacite - 1;
      this.nomTerrain = nomTerrain;
      this.mode = mode;
   }

   /**
    * @return the nomTerrain
    */
   public String getNomTerrain() {
      return nomTerrain;
   }

   /**
    * @param nomTerrain the nomTerrain to set
    */
   public void setNomTerrain(String nomTerrain) {
      this.nomTerrain = nomTerrain;
   }

   /**
    * @return the mode
    */
   public String getMode() {
      return mode;
   }

   /**
    * @param mode the mode to set
    */
   public void setMode(String mode) {
      this.mode = mode;
   }

   /**
    * @return the placesRestantes
    */
   public int getPlacesRestantes() {
      return placesRestantes;
   }

   /**
    * @param placesRestantes the placesRestantes to set
    */
   public void setPlacesRestantes(int placesRestantes) {
      this.placesRestantes = placesRestantes;
   }

   /**
    * @return the nomPartie
    */
   public String getNomPartie() {
      return nomPartie;
   }

   /**
    * @param nomPartie the nomPartie to set
    */
   public void setNomPartie(String nomPartie) {
      this.nomPartie = nomPartie;
   }

   /**
    * @return the capacite
    */
   public int getCapacite() {
      return capacite;
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
      return "Enregistrement [adresseIp=" + adresseIp + ", nom=" + nomPartie
            + ", port=" + port + "]";
   }
   
}
