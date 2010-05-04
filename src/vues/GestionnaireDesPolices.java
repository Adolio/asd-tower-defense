package vues;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;

public class GestionnaireDesPolices
{
    public static Font POLICE_TITRE;
    public static Font POLICE_SOUS_TITRE;

    public static final Font POLICE_VALEUR_CHAMP    = new Font("Verdana", Font.BOLD, 12);
    public static final Font POLICE_TITRE_CHAMP     = new Font("", Font.BOLD, 14);
    public static final Font POLICE_CONSOLE         = new Font("",Font.TRUETYPE_FONT,10);
    public static final Font POLICE_INFO            = new Font("", Font.BOLD, 12);
    
    
    public static final Color COULEUR_TITRE     = Color.BLACK;
    public static final Color COULEUR_TEXTE     = Color.MAGENTA;
    public static final Color COULEUR_INFO      = new Color(30,0,170);
    public static final Color COULEUR_ERREUR    = Color.ORANGE;
    public static final Color COULEUR_SUCCES    = Color.CYAN;
    public static final Color COULEUR_SOUS_TITRE = Color.LIGHT_GRAY;
    public static final Color COULEUR_TXT_BOUTON = Color.WHITE;
    
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
        //bouton.setFont(POLICE_SOUS_TITRE);
        composant.setForeground(COULEUR_TXT_BOUTON);
        composant.setBackground(LookInterface.COULEUR_BOUTON);
    }
    
}
