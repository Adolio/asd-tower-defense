package outils;

import java.io.File;

public class OutilsFichier
{

    public static String getExtension(File fichier)
    {
        String nomFichier = fichier.getAbsolutePath();
        return nomFichier.substring(nomFichier.lastIndexOf('.') + 1, nomFichier
                .length());
    }
}
