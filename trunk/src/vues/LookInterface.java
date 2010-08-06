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
    public static Color COULEUR_DE_FOND_PRI     = new Color(62,140,28);
    public static Color COULEUR_DE_FOND_SEC     = new Color(90,180,50);
    public static Color COULEUR_DE_FOND_BTN     = new Color(20, 20, 20);
    
    static
    {
        COULEUR_DE_FOND_PRI   = new Color(Integer.parseInt(Configuration.getProprety(Configuration.COULEUR_DE_FOND_P)));
        COULEUR_DE_FOND_SEC   = new Color(Integer.parseInt(Configuration.getProprety(Configuration.COULEUR_DE_FOND_S)));
        COULEUR_DE_FOND_BTN   = new Color(Integer.parseInt(Configuration.getProprety(Configuration.COULEUR_DE_FOND_B)));
    }
}
