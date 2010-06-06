package models.jeu;

import java.io.IOException;
import java.net.ConnectException;

import models.joueurs.GestionnaireDeRevenu;

import org.json.JSONException;
import org.json.JSONObject;
import outils.fichierDeConfiguration;
import reseau.CanalTCP;
import reseau.CanalException;
import reseau.jeu.serveur.ServeurJeu;
import serveur.enregistrement.CodeEnregistrement;
import serveur.enregistrement.RequeteEnregistrement;

/**
 * Classe de gestion du jeu réseau.
 * 
 * @author Aurelien Da Campo
 * @version 1.1 | mai 2010
 * @since jdk1.6.0_16
 */
public class Jeu_Serveur extends Jeu
{
    /**
     * Connexion au serveur d'enregistrement
     */
    private CanalTCP canalServeurEnregistrement;
    
    /**
     * Connexions réseaux du serveur
     */
    private ServeurJeu serveurDeJeu;
    
    /**
     * Permet de savoir si l'enregistrement au SE a réussi
     */
    private boolean enregistrementReussie = false;
    
    /**
     * Gestionnaire de revenu.
     */
    private GestionnaireDeRevenu gRevenus = new GestionnaireDeRevenu(this);

    @Override
    public void demarrer()
    {
        super.demarrer();
        
        gRevenus.demarrer();
    }

    /**
     * Permet de savoir si le serveur est 
     * enregistré sur le Serveur d'Enregistrement.
     * 
     * @return true si il l'est, false sinon
     */
    public boolean estEnregisterSurSE()
    {
        return enregistrementReussie;
    }
    
    /**
     * Permet d'établir la connexion du serveur.
     * 
     * @throws IOException 
     */
    public void etablissementDuServeur() throws IOException
    {
        serveurDeJeu = new ServeurJeu(this);
    }

    /**
     * Permet de stopper le serveur de jeu
     */
    public void stopperServeurDeJeu()
    {
        serveurDeJeu.stopper();
    }

    //------------------------------
    //-- SERVEUR D'ENREGISTREMENT --
    //------------------------------
    
    /**
     * Permet d'enregistrer le jeu sur le serveur d'enregistrement
     * 
     * @param nomServeur le nom
     * @param nbJoueurs le nombre de joueurs
     * @param nomTerrain le terrain
     * @param mode le mode de jeu
     * @return true = ok, false = erreur
     */
    public boolean enregistrerSurSE(String nomServeur, int nbJoueurs, String nomTerrain, int mode)
    {
        // recuperation des configurations
        fichierDeConfiguration config = new fichierDeConfiguration("cfg/config.cfg");
        String IP_SE = config.getProprety("IP_SE");
        int PORT_SE  = Integer.parseInt(config.getProprety("PORT_SE"));

        try
        {
            canalServeurEnregistrement = new CanalTCP(IP_SE, PORT_SE, true);
            
            // Création de la requete d'enregistrement
            String requete = RequeteEnregistrement.getRequeteEnregistrer(
                    nomServeur, ServeurJeu.PORT, nbJoueurs, nomTerrain, ModeDeJeu.getNomMode(mode));

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
        catch (ConnectException e){} 
        catch (CanalException e){}
        
        return false;
    }
    
    /**
     * Permet de surpprimer l'enregistrement du jeu sur le SE 
     */
    public void desenregistrerSurSE()
    {
        // fermeture du canal s'il est ouvert
        if (canalServeurEnregistrement != null && estEnregisterSurSE())
        {
            try
            {
                // désenregistrement du serveur
                canalServeurEnregistrement.envoyerString(RequeteEnregistrement.DESENREGISTRER);
                canalServeurEnregistrement.recevoirString();

                // fermeture propre du canal
                //canalServeurEnregistrement.envoyerString(RequeteEnregistrement.STOP);
                //canalServeurEnregistrement.recevoirString();
            
                canalServeurEnregistrement.fermer();}
                // il y a eu une erreur... on quitte tout de même
            
            catch (CanalException ce)
            {
                ce.printStackTrace();
            }
        }
    }
    
    /**
     * Permet de mettre à jour les infos du jeu sur le SE
     */
    public void miseAJourSE()
    {
        if(enregistrementReussie)
        {
            // Création de la requete d'enregistrement
            String requete = RequeteEnregistrement.getRequeteMiseAJour(terrain.getNbJoueursMax() - getJoueurs().size());
    
            try
            {
                // Envoie de la requete 
                canalServeurEnregistrement.envoyerString(requete);
            
                // Attente du résultat
                canalServeurEnregistrement.recevoirString();
            } 
            catch (CanalException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
