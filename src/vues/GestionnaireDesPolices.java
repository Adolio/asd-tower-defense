package vues;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JComponent;

/**
 * Classe de gestion des polices d'écriture (Font)
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | juin 2010
 * @since jdk1.6.0_16
 */
public class GestionnaireDesPolices
{
    public static Font POLICE_TITRE;
    public static Font POLICE_SOUS_TITRE;

    public static final Font POLICE_VALEUR_CHAMP    = new Font("Verdana", Font.PLAIN, 12);
    public static final Font POLICE_TITRE_CHAMP     = new Font("", Font.BOLD, 12);
    public static final Font POLICE_CONSOLE         = new Font("",Font.TRUETYPE_FONT,10);
    public static final Font POLICE_INFO            = new Font("", Font.BOLD, 12);
    
    static
    {
        try
        {
            File fichier = new File ("fonts/titre.ttf");
            FileInputStream in = new FileInputStream (fichier);
            Font dynamicFont = Font.createFont (Font.TRUETYPE_FONT, in);
            POLICE_TITRE = dynamicFont.deriveFont (24f);
            POLICE_SOUS_TITRE = dynamicFont.deriveFont (12f);
        } 
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (FontFormatException e)
        {
            e.printStackTrace();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void setStyle(JComponent composant)
    {
        composant.setForeground(LookInterface.COULEUR_TEXTE_BTN);
        composant.setBackground(LookInterface.COULEUR_DE_FOND_BTN);
    }
    
}
