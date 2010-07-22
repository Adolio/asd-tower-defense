package outils;

import java.awt.event.KeyEvent;

/**
 * Classe de gestion de la configuration.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | juin 2010
 */
public class Configuration
{
    public static final String DEPL_HAUT    = "KC_DEPL_HAUT";
    public static final String DEPL_BAS     = "KC_DEPL_BAS";
    public static final String DEPL_DROITE  = "KC_DEPL_DROITE";
    public static final String DEPL_GAUCHE  = "KC_DEPL_GAUCHE"; 
    public static final String LANCER_VAGUE = "KC_LANCER_VAGUE"; 
    public static final String VENDRE_TOUR  = "KC_VENDRE_TOUR"; 
    public static final String AMELIO_TOUR  = "KC_AMELIO_TOUR"; 
    public static final String PAUSE        = "KC_PAUSE"; 
    public static final String SUIVRE_CREATURE = "KC_SUIVRE_CREATURE"; 
    public static final String AUG_VIT_JEU  = "KC_AUG_VIT_JEU"; 
    public static final String DIM_VIT_JEU  = "KC_DIM_VIT_JEU"; 
    
    public static final String COULEUR_DE_FOND_P  = "COULEUR_DE_FOND_P";
    public static final String COULEUR_DE_FOND_S  = "COULEUR_DE_FOND_S";
    public static final String COULEUR_DE_FOND_B  = "COULEUR_DE_FOND_B";
    
    private static final String CFG = "cfg/config.cfg";

    // réseau
    private static String IP_SE;
    private static int PORT_SE;
    private static int PORT_SJ;
    private static int PORT_SJ_JD;
  
    // l'utilisateur
    private static String PSEUDO_JOUEUR;
   //private final static String LANGUE = "FR";
    
    // Commandes 
    private static int CMD_DEPL_HAUT   = KeyEvent.VK_W;
    private static int CMD_DEPL_BAS    = KeyEvent.VK_S;
    private static int CMD_DEPL_DROITE = KeyEvent.VK_A;
    private static int CMD_DEPL_GAUCHE = KeyEvent.VK_D;
    
    private static int CMD_VENDRE      = KeyEvent.VK_V;
    private static int CMD_AMELIORER   = KeyEvent.VK_Q;
    private static int CMD_PAUSE       = KeyEvent.VK_P;

    private static fichierDeConfiguration config = new fichierDeConfiguration(CFG);
    
    static
    {
        IP_SE           = config.getProperty("IP_SE");
        PORT_SE         = Integer.parseInt(config.getProperty("PORT_SE"));
        PORT_SJ         = Integer.parseInt(config.getProperty("PORT_SJ"));
        PORT_SJ_JD      = Integer.parseInt(config.getProperty("PORT_SJ_JD"));
        PSEUDO_JOUEUR   = config.getProperty("PSEUDO_JOUEUR");
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

    public static int getDeplHaut()
    {
        return CMD_DEPL_HAUT;
    }

    public static int getDeplBas()
    {
        return CMD_DEPL_BAS;
    }

    public static int getDeplDroite()
    {
        return CMD_DEPL_DROITE;
    }
    
    public static int getDeplGauche()
    {
        return CMD_DEPL_GAUCHE;
    }

    public static int getCmdVendre()
    {
        return CMD_VENDRE;
    }
    
    public static int getCmdAmeliorer()
    {
        return config.getProperty("KC_AMELIO_TOUR").charAt(0);
    }
    
    public static String getProprety(String cle)
    {
        return config.getProperty(cle);
    }
    
    public static void setProperty(String cle, String valeur)
    {
        config.setProperty(cle,valeur);
    }
    
    public static int getKeyCode(String cle)
    {
        return Integer.parseInt(config.getProperty(cle));
    }
}
