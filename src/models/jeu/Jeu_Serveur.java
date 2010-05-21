package models.jeu;

import java.net.ConnectException;
import org.json.JSONException;
import org.json.JSONObject;
import outils.fichierDeConfiguration;
import reseau.Canal;
import reseau.CanalException;
import serveur.enregistrement.CodeEnregistrement;
import serveur.enregistrement.RequeteEnregistrement;
import serveur.jeu.ServeurJeu;

public class Jeu_Serveur extends Jeu
{

    private Canal canalServeurEnregistrement;
    private ServeurJeu serveurDeJeu;
    
    private boolean enregistrementReussie = false;
    
    /**
     * TODO
     */
    public boolean enregistrerSurSE(String nomServeur, int nbJoueurs, String nomTerrain, int mode)
    {
        // recuperation des configurations
        fichierDeConfiguration config = new fichierDeConfiguration("cfg/config.cfg");
        String IP_SE = config.getProprety("IP_SE");
        int PORT_SE = Integer.parseInt(config.getProprety("PORT_SE"));
        int PORT_SJ = Integer.parseInt(config.getProprety("PORT_SJ"));
        // IP idael : "188.165.41.224";
        // IP lazhar : "10.192.51.161";
        
        try
        {
            canalServeurEnregistrement = new Canal(IP_SE, PORT_SE, true);
            
            // Création de la requete d'enregistrement
            String requete = RequeteEnregistrement.getRequeteEnregistrer(
                    nomServeur, PORT_SJ, nbJoueurs, nomTerrain, ModeDeJeu.getNomMode(mode));

            // Envoie de la requete
            canalServeurEnregistrement.envoyerString(requete);
            
            // Attente du résultat
            String resultat = canalServeurEnregistrement.recevoirString();
            
            try
            {
                // Analyse de la réponse du serveur d'enregistrement
                JSONObject jsonResultat = new JSONObject(resultat);
                
                if(jsonResultat.getInt("status") == CodeEnregistrement.OK)
                {
                    enregistrementReussie = true;
                    return true;
                }
                else
                    return false;
            } 
            catch (JSONException e1)
            {
                e1.printStackTrace();
            }
        } 
        catch (ConnectException e)
        {
            e.printStackTrace();
        } 
        catch (CanalException e)
        {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * TODO
     */
    public void desenregistrerSurSE()
    {
        // fermeture du canal s'il est ouvert
        if (canalServeurEnregistrement != null)
        {
            try
            {
                // désenregistrement du serveur
                canalServeurEnregistrement.envoyerString(RequeteEnregistrement.DESENREGISTRER);
                canalServeurEnregistrement.recevoirString();

                // fermeture propre du canal
                //canalServeurEnregistrement.envoyerString(RequeteEnregistrement.STOP);
                //canalServeurEnregistrement.recevoirString();
            }
            // il y a eu une erreur... on quitte tout de même
            catch (CanalException ce)
            {
                ce.printStackTrace();
            }

            canalServeurEnregistrement.fermer();
        }
    }

    // TODO
    public boolean getEnregistrementReussie()
    {
        return enregistrementReussie;
    }
}
