package vues;

import java.awt.Color;
import outils.Configuration;

/**
 * Classe de gestion des styles graphique
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | juin 2010
 * @since jdk1.6.0_16
 */
public class LookInterface
{
    // bleu Color(0,112,112)
    // vert Color(62,140,28)
    // gris Color(220,220,220)
    
    public static Color COULEUR_DE_FOND_PRI  = new Color(62,140,28);
    public static Color COULEUR_DE_FOND_SEC = new Color(90,180,50);
    public static Color COULEUR_DE_FOND_BTN = new Color(20, 20, 20);
    
    static
    {
        /*
        Configuration.setProperty(Configuration.COULEUR_DE_FOND_P, COULEUR_DE_FOND_PRI.getRGB()+"");
        Configuration.setProperty(Configuration.COULEUR_DE_FOND_S, COULEUR_DE_FOND_SEC.getRGB()+"");
        Configuration.setProperty(Configuration.COULEUR_DE_FOND_B, COULEUR_DE_FOND_BTN.getRGB()+"");
        */
        
        COULEUR_DE_FOND_PRI   = new Color(Integer.parseInt(Configuration.getProprety(Configuration.COULEUR_DE_FOND_P)));
        COULEUR_DE_FOND_SEC   = new Color(Integer.parseInt(Configuration.getProprety(Configuration.COULEUR_DE_FOND_S)));
        COULEUR_DE_FOND_BTN   = new Color(Integer.parseInt(Configuration.getProprety(Configuration.COULEUR_DE_FOND_B)));
    }
    
}
