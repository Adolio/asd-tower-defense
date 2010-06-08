package outils;

import java.io.File;

/**
 * Classe utilitaire pour les fichiers
 * 
 * @author Aurélien Da Campo
 * @version 1.0 | mai 2010
 * @since jdk1.6.0_16
 */
public class OutilsFichier
{
    public static String getExtension(File fichier)
    {
        String nomFichier = fichier.getAbsolutePath();
        return nomFichier.substring(nomFichier.lastIndexOf('.') + 1, nomFichier
                .length());
    }
}
