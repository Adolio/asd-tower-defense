package ServeurEnregistrement;

import Reseau.*;


/**
 * 
 * @author lazhar
 *
 */
public class SEConnexion implements Runnable {
   
   private Canal canal;
   private SEInscription seInscription;
   private String messageRecu;
   private String nomPartieCourante;
   private boolean helloOk = false;
   private Enregistrement enregisrementCourant;
   
   /**
    * 
    * @param canal
    * @param seInscription
    */
   public SEConnexion(Canal canal, SEInscription seInscription)
   {
      this.canal = canal;
      this.seInscription = seInscription;
   }
   
   /**
    * 
    */
   @Override
   public void run() {
      bouclePrincipale:
      while (true)
      {
         messageRecu = canal.recevoirString();
         
         if (messageRecu.equals("BREAK"))
         {
            canal.fermer();
            break bouclePrincipale;
         }
         
         if (!messageRecu.equals("HELLO") && !helloOk)
         {
            canal.envoyerString("NACK");
            miseAJourEnregistrements();
            continue bouclePrincipale;
         }
         
         if (messageRecu.equals("HELLO"))
         {
            nomPartieCourante = canal.recevoirString();
            canal.envoyerString("ACK");
            helloOk = true;
            miseAJourEnregistrements();
            continue bouclePrincipale;
         }
         
         if (messageRecu.equals("REGISTER")) {
            enregisrementCourant = new Enregistrement(
               nomPartieCourante,
               canal.recevoirString(),
               new Port(canal.recevoirString())
            );
            
            if (seInscription.ajouterEnregistrement(enregisrementCourant))
            {
               canal.envoyerString("ACK");
            }
            else
            {
               canal.envoyerString("NACK");
               System.out.println("Partie déjà existante!");
            }
            miseAJourEnregistrements();
            continue bouclePrincipale;
         }
         
         if (messageRecu.equals("UNREGISTER")) {
            seInscription.enleverEnregistrement(enregisrementCourant);
            canal.envoyerString("ACK");
            canal.fermer();
            break bouclePrincipale;
         }
         
         if (messageRecu.equals("GAMESNUMBER"))
         {
            canal.envoyerString("ACK");
            canal.envoyerInt(seInscription.getNombreEnregistrements());
            miseAJourEnregistrements();
            continue bouclePrincipale;
         }
         
         if (messageRecu.equals("GAMESLOCATIONS"))
         {
            if (seInscription.getNombreEnregistrements() == 0)
            {
               canal.envoyerString("NACK");
            }
            else
            {
               canal.envoyerString("ACK");
               for (Enregistrement e : seInscription.getJeuxEnregistres())
               {
                  canal.envoyerString(e.getNom());
                  canal.envoyerString(e.getAdresseIp());
                  canal.envoyerInt(e.getPort().getNumeroPort());
               }
            }
            miseAJourEnregistrements();
            continue bouclePrincipale;
         }
      }
   }
   
   /**
    * 
    */
   private void miseAJourEnregistrements()
   {
      for (Enregistrement e : seInscription.getJeuxEnregistres())
      {
         try {
            Canal c = new Canal(e.getAdresseIp(), e.getPort().getNumeroPort(), true);
            c.envoyerString("TEST");
            c.fermer();
         }
         catch (Exception exc)
         {
            seInscription.enleverEnregistrement(e);
         }
      }
   }
}
