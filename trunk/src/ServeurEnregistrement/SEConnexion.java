package ServeurEnregistrement;

import Reseau.*;
import org.json.*;


/**
 * 
 * @author lazhar
 *
 */
public class SEConnexion implements Runnable, CodeEnregistrement {
   
   private Canal canal;
   private Enregistrement enregisrementCourant;
   private JSONObject messageJsonRecu;
   private JSONObject contenu;
   private int code;
   private String jsonString;
   private String messageRecu;
   
   /**
    * 
    * @param canal
    * @param SEInscription
    */
   public SEConnexion(Canal canal)
   {
      this.canal = canal;
   }
   
   /**
    * 
    */
   @Override
   public void run() {
      bouclePrincipale:
      while (true)
      {
         try {
            messageRecu = canal.recevoirString();
            messageJsonRecu = new JSONObject(messageRecu);
            code = messageJsonRecu.getJSONObject("donnees").getInt("code");
            
            switch(code)
            {
               case STOP :
                  canal.envoyerString("{\"status\" : \"OK\"}");
                  canal.fermer();
                  break bouclePrincipale;
                  
               case TEST :
                  canal.envoyerString("{\"status\" : \"OK\"}");
                  break;
                  
               case ENREGISTRER :
                  contenu = messageJsonRecu.getJSONObject("donnees")
                                           .getJSONObject("contenu");
                  enregisrementCourant = new Enregistrement(
                              contenu.getString("nomPartie"),
                              contenu.getString("adresseIp"),
                              new Port(contenu.getInt("numeroPort")),
                              contenu.getInt("capacite"));
                  
                  if (SEInscription.ajouterEnregistrement(enregisrementCourant))
                  {
                     canal.envoyerString("{\"status\" : \"OK\"}");
                  }
                  else
                  {
                     canal.envoyerString("{\"status\" : \"ERROR\"," +
                                         "\"message\" : \"Cette partie existe deja!\"}");
                  }
                  break;
                  
               case DESENREGISTRER :
                  if (enregisrementCourant != null)
                  {
                     SEInscription.enleverEnregistrement(enregisrementCourant);
                     canal.envoyerString("{\"status\" : \"OK\"}");
                     canal.fermer();
                     break bouclePrincipale;
                  }
                  canal.envoyerString("{\"status\" : \"ERROR\"," +
                                      "\"message\" : \"Aucun enregistrement n'a ete fait!\"}");
                  break;
                  
               case NOMBRE_PARTIES :
                  canal.envoyerString("{\"status\" : \"OK\"," +
                                      "\"nombreParties\" : " + 
                                          SEInscription.getNombreEnregistrements() + "}");
                  break;
                  
               case INFOS_PARTIES :
                  if (enregisrementCourant != null)
                  {
                     jsonString = "{\"status\" : \"OK\", \"parties\" : [";
                     for (Enregistrement e : SEInscription.getJeuxEnregistres())
                     {
                        jsonString.concat("{");
                        jsonString.concat("\"nomPartie\" : \"");
                        jsonString.concat(e.getNomPartie());
                        jsonString.concat("\",");
                        jsonString.concat("\"adresseIp\" : \"");
                        jsonString.concat(e.getAdresseIp());
                        jsonString.concat("\",");
                        jsonString.concat("\"numeroPort\" : " + e.getPort().getNumeroPort() + ",");
                        jsonString.concat("\"capacite\" : " + e.getCapacite() + ",");
                        jsonString.concat("\"placesRestantes\" : " + e.getPlacesRestantes());
                        jsonString.concat("},");
                     }
                     jsonString = jsonString.substring(0, jsonString.length() - 2);
                     jsonString.concat("]}");
                     
                     canal.envoyerString(jsonString);
                     break;
                  }
                  canal.envoyerString("{\"status\" : \"ERROR\"," +
                                      "\"message\" : \"Aucun enregistrement n'a ete fait!\"}");
                  break;
                  
               case AJOUTER_JOUEUR :
                  if (enregisrementCourant != null)
                  {
                     canal.envoyerString("{\"status\" : \"OK\"}");
                     enregisrementCourant.setPlacesRestantes(enregisrementCourant.getPlacesRestantes() - 1);
                     break;
                  }
                  canal.envoyerString("{\"status\" : \"ERROR\"," +
                                      "\"message\" : \"Aucun enregistrement n'a ete fait!\"}");
                  break;
                  
               default :
                  canal.envoyerString("{\"status\" : \"ERROR\"," +
                                      "\"message\" : \"Code errone!\"}");
                  break;
            }
         }
         catch (JSONException e1) {
            // JSON exception
            e1.printStackTrace();
         }
         
         miseAJourEnregistrements();
      }
   }
   
   /**
    * 
    */
   private void miseAJourEnregistrements()
   {
      for (Enregistrement e : SEInscription.getJeuxEnregistres())
      {
         try {
            Canal c = new Canal(e.getAdresseIp(), e.getPort().getNumeroPort(), true);
            c.envoyerString("TEST");
            c.fermer();
         }
         catch (Exception exc)
         {
            SEInscription.enleverEnregistrement(e);
         }
      }
   }
}
