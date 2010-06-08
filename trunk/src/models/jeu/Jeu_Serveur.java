package models.jeu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ConnectException;

import models.creatures.Creature;
import models.joueurs.GestionnaireDeRevenu;
import models.joueurs.Joueur;

import org.json.JSONException;
import org.json.JSONObject;
import outils.Configuration;
import reseau.CanalTCP;
import reseau.CanalException;
import reseau.jeu.serveur.ServeurJeu;
import serveur.enregistrement.CodeEnregistrement;
import serveur.enregistrement.RequeteEnregistrement;

/**
 * Classe de gestion du moteur de jeu réseau.
 * 
 * @author Aurelien Da Campo
 * @version 1.1 | mai 2010
 * @since jdk1.6.0_16
 * 
 * @see serveurDeJeu
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

    /**
     * Temps avant que le serveur incrémente son niveau
     * 
     * Le niveau du jeu influ sur la santé des créatures lancées
     * En effet, la santé des créatures est générer en fonction du niveau du jeu.
     */
    private static final int TEMPS_ENTRE_CHAQUE_LEVEL = 20; // secondes
    
    @Override
    public void demarrer()
    {
        super.demarrer();
        
        gRevenus.demarrer();
        
        
        // gestionnaire des niveaux (applé toutes les secondes de jeu)
        timer.addActionListener(new ActionListener()
        {  
            int secondes = 0;
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                secondes++;
                
                if(secondes == TEMPS_ENTRE_CHAQUE_LEVEL)
                {
                    passerALaProchaineVague();
                    secondes = 0;
                    
                    // TODO effacer et informer les clients ou non.
                    System.out.println("Nouveau level du serveur "+getNumVagueCourante());
                } 
            }
        });
    }
    
    @Override
    synchronized public void creatureTuee(Creature creature, Joueur tueur)
    {
        // gain de pieces d'or
        tueur.setNbPiecesDOr(tueur.getNbPiecesDOr() + creature.getNbPiecesDOr() / 5);
        
        // augmentation du score
        tueur.setScore(tueur.getScore() + creature.getNbPiecesDOr());

        // notification de la mort de la créature
        if(edj != null)
            edj.creatureTuee(creature,tueur);
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
        try
        {
            canalServeurEnregistrement = new CanalTCP(Configuration.getIpSE(), 
                                                      Configuration.getPortSE(), 
                                                      true);
            
            // Création de la requete d'enregistrement
            String requete = RequeteEnregistrement.getRequeteEnregistrer(
                    nomServeur, Configuration.getPortSJ(), nbJoueurs, nomTerrain, ModeDeJeu.getNomMode(mode));

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
                e.printStackTrace();
            }
        }
    }
    
    public void terminer(ResultatJeu resultatJeu)
    {
        if(!estTermine)
        {
            estTermine = true;
            
            arreterTout();
            
            if(edj != null)
                edj.partieTerminee(resultatJeu);
        }
    }
}
