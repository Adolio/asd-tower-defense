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
    // COULEURS PAR DEFAUT
    public static final Color DEF_COULEUR_DE_FOND_PRI     = new Color(62,140,28);
    public static final Color DEF_COULEUR_TEXTE_PRI       = new Color(20,60,0);
    
    public static final Color DEF_COULEUR_DE_FOND_SEC     = new Color(90,180,50);
    public static final Color DEF_COULEUR_TEXTE_SEC       = new Color(20,60,0);
    
    public static final Color DEF_COULEUR_DE_FOND_BTN     = new Color(20, 20, 20);
    public static final Color DEF_COULEUR_TEXTE_BTN       = Color.WHITE;
    
    // COULEURS
    public static Color COULEUR_DE_FOND_PRI     = DEF_COULEUR_DE_FOND_PRI;
    public static Color COULEUR_TEXTE_PRI       = DEF_COULEUR_TEXTE_PRI;
    
    public static Color COULEUR_DE_FOND_SEC     = DEF_COULEUR_DE_FOND_SEC;
    public static Color COULEUR_TEXTE_SEC       = DEF_COULEUR_TEXTE_SEC;
    
    public static Color COULEUR_DE_FOND_BTN     = DEF_COULEUR_DE_FOND_BTN;
    public static Color COULEUR_TEXTE_BTN       = DEF_COULEUR_TEXTE_BTN;
    
    public static Color COULEUR_ERREUR          = Color.ORANGE;
    public static Color COULEUR_SUCCES          = new Color(0,200,20);
    
    static
    {
        COULEUR_DE_FOND_PRI   = new Color(Integer.parseInt(Configuration.getProprety(Configuration.COULEUR_DE_FOND_P)));
        COULEUR_TEXTE_PRI     = new Color(Integer.parseInt(Configuration.getProprety(Configuration.COULEUR_TEXTE_P)));
        
        COULEUR_DE_FOND_SEC   = new Color(Integer.parseInt(Configuration.getProprety(Configuration.COULEUR_DE_FOND_S)));
        COULEUR_TEXTE_SEC     = new Color(Integer.parseInt(Configuration.getProprety(Configuration.COULEUR_TEXTE_S)));
        
        COULEUR_DE_FOND_BTN   = new Color(Integer.parseInt(Configuration.getProprety(Configuration.COULEUR_DE_FOND_B)));
        COULEUR_TEXTE_BTN     = new Color(Integer.parseInt(Configuration.getProprety(Configuration.COULEUR_TEXTE_B)));
    
        /*
            System.out.println(Outils.ColorToHexa(COULEUR_DE_FOND_PRI));
            System.out.println(Outils.ColorToHexa(COULEUR_TEXTE_PRI));
            System.out.println(Outils.ColorToHexa(COULEUR_DE_FOND_SEC));
        */ 
    }
}
