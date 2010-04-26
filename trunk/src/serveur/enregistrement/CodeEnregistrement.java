package serveur.enregistrement;

public interface CodeEnregistrement
{
   // Codes d'envoi vers le serveur
   public final int STOP           = 100;
   public final int TEST           = 101;
   public final int ENREGISTRER    = 102;
   public final int DESENREGISTRER = 103;
   public final int NOMBRE_PARTIES = 104;
   public final int INFOS_PARTIES  = 105;
   public final int MISE_A_JOUR    = 106;
   
   // Codes de réponse du serveur
   public final int OK             = 200;
   public final int ERREUR         = 201;
   
}
