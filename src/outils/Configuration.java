package outils;

/**
 * Classe de gestion de la configuration.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | juin 2010
 */
public class Configuration
{
    private static final String CFG = "cfg/config.cfg";
    
    // réseau
    private static String IP_SE;
    private static int PORT_SE;
    private static int PORT_SJ;
    private static int PORT_SJ_JD;
  
    // l'utilisateur
    private static String PSEUDO_JOUEUR;
    //private static final static String LANGUE;
    
    private static fichierDeConfiguration config = new fichierDeConfiguration(CFG);
    
    static
    {
        IP_SE           = config.getProprety("IP_SE");
        PORT_SE         = Integer.parseInt(config.getProprety("PORT_SE"));
        PORT_SJ         = Integer.parseInt(config.getProprety("PORT_SJ"));
        PORT_SJ_JD      = Integer.parseInt(config.getProprety("PORT_SJ_JD"));
        PSEUDO_JOUEUR   = config.getProprety("PSEUDO_JOUEUR");
    }

    public static String getIpSE()
    {
        return IP_SE;
    }
    
    public static int getPortSE()
    {
        return PORT_SE;
    }
    
    public static int getPortSJ()
    {
        return PORT_SJ;
    }
    
    public static String getPseudoJoueur()
    {
        return PSEUDO_JOUEUR;
    }
    
    public static void setIpSE(String iP_SE)
    {
        IP_SE = iP_SE;
        
        config.setProperty("IP_SE", IP_SE);
    }
    
    public static void setPortSE(int pORT_SE)
    {
        PORT_SE = pORT_SE;
        
        config.setProperty("PORT_SE", PORT_SE+"");
    }

    public static void setPortSJ(int PORT_SJ_2)
    {
        PORT_SJ = PORT_SJ_2;
        
        config.setProperty("PORT_SJ", PORT_SJ+"");
    }
    
    public static void setPseudoJoueur(String PSEUDO_JOUEUR_2)
    {
        PSEUDO_JOUEUR = PSEUDO_JOUEUR_2;
        
        config.setProperty("PSEUDO_JOUEUR", PSEUDO_JOUEUR);
    }

    public static void setPortSJ_JD(int PORT_SJ_JD_2)
    {
        PORT_SJ_JD = PORT_SJ_JD_2;
        
        config.setProperty("PORT_SJ_JD", PORT_SJ_JD+"");
    }

    public static int getPortSJ_JD()
    {
        return PORT_SJ_JD;
    }
}
