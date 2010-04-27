package vues;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GestionnaireDesPolices
{
    public static Font POLICE_TITRE;
    public static final Font POLICE_DONNEES    = new Font("Verdana", Font.BOLD, 12);
    public static final Font POLICE_NOM        = new Font("", Font.BOLD, 14);
    public static final Font POLICE_CONSOLE    = new Font("",Font.TRUETYPE_FONT,10);
    
    
    static
    {
        try
        {
            File fichier = new File ("fonts/titre.ttf");
            FileInputStream in = new FileInputStream (fichier);
            Font dynamicFont = Font.createFont (Font.TRUETYPE_FONT, in);
            POLICE_TITRE = dynamicFont.deriveFont (24f);
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
    
}
