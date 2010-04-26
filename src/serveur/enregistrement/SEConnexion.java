package serveur.enregistrement;

import org.json.*;

import reseau.*;


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
                  canal.envoyerString("{\"status\" :" + OK + "}");
                  canal.fermer();
                  break bouclePrincipale;
                  
               case TEST :
                  canal.envoyerString("{\"status\" :" + OK + "}");
                  break;
                  
               case ENREGISTRER :
                  contenu = messageJsonRecu.getJSONObject("donnees")
                                           .getJSONObject("contenu");
                  enregisrementCourant = new Enregistrement(
                              contenu.getString("nomPartie"),
                              contenu.getString("adresseIp"),
                              new Port(contenu.getInt("numeroPort")),
                              contenu.getInt("capacite"),
                              contenu.getString("nomTerrain"),
                              contenu.getString("mode"));
                  
                  if (SEInscription.ajouterEnregistrement(enregisrementCourant))
                  {
                     canal.envoyerString("{\"status\" :" + OK + "}");
                  }
                  else
                  {
                     canal.envoyerString("{\"status\" : " + ERREUR + "," +
                                         "\"message\" : \"Cette partie existe deja!\"}");
                  }
                  break;
                  
               case DESENREGISTRER :
                  if (enregisrementCourant != null)
                  {
                     SEInscription.enleverEnregistrement(enregisrementCourant);
                     canal.envoyerString("{\"status\" :" + OK + "}");
                     canal.fermer();
                     break bouclePrincipale;
                  }
                  canal.envoyerString("{\"status\" : " + ERREUR + "," +
                                      "\"message\" : \"Aucun enregistrement n'a ete fait!\"}");
                  break;
                  
               case NOMBRE_PARTIES :
                  canal.envoyerString("{\"status\" : " + OK + "," +
                                      "\"nombreParties\" : " + 
                                          SEInscription.getNombreEnregistrements() + "}");
                  break;
                  
               case INFOS_PARTIES :
                  
                  // TODO arrete la drogue!
                  //if (enregisrementCourant != null)
                  if (SEInscription.getNombreEnregistrements() > 0) 
                  {
                     jsonString = "{\"status\" : " + OK + ", \"parties\" : [";
                     for (Enregistrement e : SEInscription.getJeuxEnregistres())
                     {
                        jsonString = jsonString.concat("{");
                        jsonString = jsonString.concat("\"nomPartie\" : \"" + e.getNomPartie() + "\",");
                        jsonString = jsonString.concat("\"adresseIp\" : \"" + e.getAdresseIp() + "\",");
                        jsonString = jsonString.concat("\"numeroPort\" : " + e.getPort().getNumeroPort() + ",");
                        jsonString = jsonString.concat("\"capacite\" : " + e.getCapacite() + ",");
                        jsonString = jsonString.concat("\"placesRestantes\" : " + e.getPlacesRestantes() + ",");
                        jsonString = jsonString.concat("\"nomTerrain\" : \"" + e.getNomTerrain() + "\",");
                        jsonString = jsonString.concat("\"mode\" : \"" + e.getMode() + "\"");
                        jsonString = jsonString.concat("},");
                     }
                     
                     jsonString = jsonString.concat("]}");
                     
                     System.out.println("\n\n\n" + jsonString + "\n\n\n");
                     
                     canal.envoyerString(jsonString);
                     break;
                  }
                  canal.envoyerString("{\"status\" : " + ERREUR + "," +
                                      "\"message\" : \"Aucun enregistrement n'a ete fait!\"}");
                  break;
                  
               case MISE_A_JOUR :
                  if (enregisrementCourant != null)
                  {
                     canal.envoyerString("{\"status\" :" + OK + "}");
                     contenu = messageJsonRecu.getJSONObject("donnees")
                                              .getJSONObject("contenu");
                     enregisrementCourant.setPlacesRestantes(contenu.getInt("placesRestantes"));
                     break;
                  }
                  canal.envoyerString("{\"status\" : " + ERREUR + "," +
                                      "\"message\" : \"Aucun enregistrement n'a ete fait!\"}");
                  break;
                  
               default :
                  canal.envoyerString("{\"status\" : " + ERREUR +"," +
                                      "\"message\" : \"Code errone!\"}");
                  break;
            }
         }
         catch (JSONException e1) {
            // JSON exception
            e1.printStackTrace();
         }
         
         //miseAJourEnregistrements();
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
            //c.envoyerString("TEST");
            c.fermer();
         }
         catch (Exception exc)
         {
            SEInscription.enleverEnregistrement(e);
         }
      }
   }
}
