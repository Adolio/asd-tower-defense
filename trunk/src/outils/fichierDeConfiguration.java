package outils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/*****************************************************************************
 * Cette classe va permettre d'obtenir et mettre à jour des fichiers de 
 * configuration.
 ******************************************************************************/
public class fichierDeConfiguration
{
    Properties config = new Properties();
    File fichier;
    
    /**
     * Constructeur
     * 
     * @param cheminFichier le chemin complet vers le fichier de configuration
     * @throws IOException 
     */
    public fichierDeConfiguration(String cheminFichier)
    {
        try
        {
            fichier = new File(cheminFichier);
            
            chargement();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /*************************************************************************
     * Chargement du fichier de configuration.
     * @throws IOException 
     ************************************************************************/
    private void chargement() throws IOException
    {
        if(fichier == null)
            throw new IllegalArgumentException("Fichier incorrect");
        
        
        // On fait pointer notre Properties sur le fichier
        FileInputStream fis;
        
        fis = new FileInputStream(fichier);
        config.load(fis);
        fis.close();
        fis = null; // Optimisation garbage collector

    }
    
    /*************************************************************************
     * Récupère la valeur associé à une clé
     * 
     * @param cle la clé
     * @return String value
     ************************************************************************/
    public String getProprety(String cle)
    {
        String valeur = config.getProperty(cle);
        
        // Clé inconnue
        if (valeur == null)
            throw new IllegalArgumentException("La valeur correspondant à '" + cle + "' n'existe pas dans le fichier '" + fichier.getAbsolutePath() +"'");
        
        return valeur;
    }
    
    /*************************************************************************
     * Modifie la valeur associé à une clé
     * 
     * @param cle la clé
     * @param valeur String 
     ************************************************************************/
    public void setProperty(String cle, String valeur)
    {
        FileOutputStream fos;
        try 
        {
            fos = new FileOutputStream(fichier);
            config.setProperty(cle,valeur);
            config.store(fos,"Derniere mise a jour :");
            fos.close();
            fos = null;
        } 
        catch (FileNotFoundException e) { e.printStackTrace();} 
        catch (IOException e)           { e.printStackTrace();}
    }
}