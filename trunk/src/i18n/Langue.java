package i18n;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.json.JSONException;
import org.json.JSONObject;

public class Langue
{
    /**
     * Identificateur des textes dans le fichier de langue
     */
    
    // BOUTON ET ELEMENTS D'ACTION
    public static final String ID_TXT_BTN_SOLO          = "TXT_BTN_SOLO";
    public static final String ID_TXT_BTN_VOS_PARTIES   = "TXT_BTN_VOS_PARTIES";
    public static final String ID_TXT_BTN_REJOINDRE     = "TXT_BTN_REJOINDRE";
    public static final String ID_TXT_BTN_CREER         = "TXT_BTN_CREER";
    public static final String ID_TXT_BTN_REGLES        = "TXT_BTN_REGLES";
    public static final String ID_TXT_BTN_A_PROPOS      = "TXT_BTN_A_PROPOS";
    public static final String ID_TXT_BTN_OPTIONS       = "TXT_BTN_OPTIONS";
    public static final String ID_TXT_BTN_QUITTER       = "TXT_BTN_QUITTER";
    public static final String ID_TXT_BTN_RETOUR        = "TXT_BTN_RETOUR"; 
    public static final String ID_TXT_BTN_FICHIER       = "TXT_BTN_FICHIER"; 
    public static final String ID_TXT_BTN_EDITION       = "TXT_BTN_EDITION"; 
    public static final String ID_TXT_BTN_LANCER_VAGUE = "TXT_BTN_LANCER_VAGUE";
    public static final String ID_TXT_BTN_AFFICHAGE     = "TXT_BTN_AFFICHAGE";
    public static final String ID_TXT_BTN_JEU           = "TXT_BTN_JEU";
    public static final String ID_TXT_BTN_SON           = "TXT_BTN_SON";
    public static final String ID_TXT_BTN_AIDE          = "TXT_BTN_AIDE";
    public static final String ID_TXT_BTN_RAYONS_DE_PORTEE  = "TXT_BTN_RAYONS_DE_PORTEE";
    public static final String ID_TXT_BTN_MODE_DEBUG        = "TXT_BTN_MODE_DEBUG";
    public static final String ID_TXT_BTN_MAILLAGE          = "TXT_BTN_MAILLAGE";
    public static final String ID_TXT_BTN_ACTIVE_DESACTIVE  = "TXT_BTN_ACTIVE_DESACTIVE";
    public static final String ID_TXT_BTN_PAUSE             = "TXT_BTN_PAUSE";
    public static final String ID_TXT_BTN_RETOUR_MENU_P     = "TXT_BTN_RETOUR_MENU_P";
    public static final String ID_TXT_BTN_REDEMARRER_PARTIE = "TXT_BTN_REDEMARRER_PARTIE";
    public static final String ID_TXT_BTN_AMELIORER         = "TXT_BTN_AMELIORER";
    public static final String ID_TXT_BTN_VENDRE            = "TXT_BTN_VENDRE";
    
    // TITRES
    public static final String ID_TITRE_PARTIE_SOLO         = "TITRE_PARTIE_SOLO"; 
    public static final String ID_TITRE_INFO_SELECTION      = "TITRE_INFO_SELECTION";
    public static final String ID_TITRE_PARTIE_TERMINEE     = "TITRE_PARTIE_TERMINEE";
    
    // TEXTES
    public static final String ID_TXT_CLIQUER_SUR_TERRAIN   = "TXT_CLIQUER_SUR_TERRAIN";
    public static final String ID_TXT_NIVEAU                = "TXT_NIVEAU";
    public static final String ID_TXT_VAGUE_SUIVANTE        = "TXT_VAGUE_SUIVANTE";
    public static final String ID_TXT_PRIX_ACHAT            = "TXT_PRIX_ACHAT";
    public static final String ID_TXT_VALEUR_PRIX           = "TXT_VALEUR_PRIX";
    public static final String ID_TXT_DEGATS                = "TXT_DEGATS";
    public static final String ID_TXT_PORTEE                = "TXT_PORTEE";
    public static final String ID_TXT_TIRS_SEC              = "TXT_TIRS_SEC";
    public static final String ID_TXT_DPS                   = "TXT_DPS";
    public static final String ID_TXT_ATTAQE_LA_CREATURE    = "TXT_ATTAQE_LA_CREATURE";
    public static final String ID_TXT_AIR                   = "TXT_AIR";
    public static final String ID_TXT_TERRE                 = "TXT_TERRE";
    public static final String ID_TXT_SANTE                 = "TXT_SANTE";
    public static final String ID_TXT_GAIN                  = "TXT_GAIN";
    public static final String ID_TXT_VITESSE               = "TXT_VITESSE";
    public static final String ID_TXT_TERRIENNE             = "TXT_TERRIENNE";
    public static final String ID_TXT_AERIENNE              = "TXT_AERIENNE";
    
    // DIALOG
    public static final String ID_TXT_DIALOG_ARRETER_PARTIE = "TXT_DIALOG_ARRETER_PARTIE";
    public static final String ID_TXT_DIALOG_QUITTER_JEU    = "TXT_DIALOG_QUITTER_JEU";
    public static final String ID_TXT_DIALOG_SAUVER         = "TXT_DIALOG_SAUVER";
    public static final String ID_TXT_SCORE_OBTENU          = "TXT_SCORE_OBTENU";
    public static final String ID_TXT_VOTRE_PSEUDO          = "TXT_VOTRE_PSEUDO";
    public static final String ID_TXT_OK                    = "TXT_OK";
    public static final String ID_TXT_FERMER                = "TXT_FEMER";

    
    private static boolean initialise = false;
    
    private static JSONObject jo;
    
    /**
     * Contructeur
     * 
     * @param fichierDeLangue le fichier de langue
     */
    public static boolean initaliser(String fichierDeLangue)
    {  
        
        String chaine="";
        
        //lecture du fichier texte  
        try{
            InputStream ips=new FileInputStream(fichierDeLangue); 
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);
            String ligne;
            while ((ligne=br.readLine())!=null){
                System.out.println(ligne);
                chaine+=ligne+"\n";
            }
            br.close(); 
        }       
        catch (Exception e){
            System.out.println(e.toString());
        }

        try
        {
            jo = new JSONObject(chaine);
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        initialise = true;

        return true;
    }
    
    public static String getTexte(String idTxt)
    {
        if(initialise)
            try
            {
                return jo.getString(idTxt);
            } 
            catch (JSONException e)
            {
                return "ID_INDEFINI";
            }
        
        return "LANGAGE_NON_INITIALISE";
    }
    
    public static void sauver(String nomDuFichier)
    {
        if(jo != null)
        {
            try 
            {
                FileWriter fw = new FileWriter (nomDuFichier);
                BufferedWriter bw = new BufferedWriter (fw);
                PrintWriter fichierSortie = new PrintWriter (bw); 
                    fichierSortie.println (jo.toString()); 
                fichierSortie.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public static void tmp()
    {
        try
        {
            // EN
            
            jo = new JSONObject();
            
            jo.put(ID_TXT_BTN_SOLO, "Solo");
            jo.put(ID_TXT_BTN_VOS_PARTIES, "Costum");
            jo.put(ID_TXT_BTN_REJOINDRE, "Join");
            jo.put(ID_TXT_BTN_CREER, "Create");
            jo.put(ID_TXT_BTN_REGLES, "Rules");
            jo.put(ID_TXT_BTN_A_PROPOS, "About");
            jo.put(ID_TXT_BTN_OPTIONS, "Options");
            jo.put(ID_TXT_BTN_QUITTER, "Quit");
            jo.put(ID_TXT_BTN_RETOUR, "Back");
            jo.put(ID_TXT_BTN_FICHIER, "File");
            jo.put(ID_TXT_BTN_EDITION, "Edition");
            jo.put(ID_TXT_BTN_LANCER_VAGUE, "Edit");
            jo.put(ID_TXT_BTN_JEU, "Game");
            jo.put(ID_TXT_BTN_AFFICHAGE, "View");
            jo.put(ID_TXT_BTN_SON, "Sound");
            jo.put(ID_TXT_BTN_AIDE, "Help");
            jo.put(ID_TXT_BTN_RETOUR_MENU_P, "Back to main menu");
            jo.put(ID_TXT_BTN_REDEMARRER_PARTIE, "Restart the game");
            jo.put(ID_TXT_BTN_LANCER_VAGUE,"Next wave");
            jo.put(ID_TXT_BTN_RAYONS_DE_PORTEE,"Ranges");
            jo.put(ID_TXT_BTN_MODE_DEBUG,"Debug mode");
            jo.put(ID_TXT_BTN_MAILLAGE,"Mesh");
            jo.put(ID_TXT_BTN_ACTIVE_DESACTIVE,"On / Off");
            jo.put(ID_TXT_BTN_PAUSE,"Pause");
            jo.put(ID_TXT_BTN_AMELIORER,"Upgrade");
            jo.put(ID_TXT_BTN_VENDRE,"Sell");
            
            jo.put(ID_TXT_NIVEAU,"level");
            jo.put(ID_TXT_CLIQUER_SUR_TERRAIN, "Click on an available field to start the game.");
            jo.put(ID_TXT_VAGUE_SUIVANTE,"Next wave");
            jo.put(ID_TXT_PRIX_ACHAT,"Purchase price");
            jo.put(ID_TXT_VALEUR_PRIX,"Value, price");
            jo.put(ID_TXT_DEGATS,"Dammages");
            jo.put(ID_TXT_PORTEE,"Range");
            jo.put(ID_TXT_TIRS_SEC,"Hits / sec.");
            jo.put(ID_TXT_DPS,"DPS");
            jo.put(ID_TXT_ATTAQE_LA_CREATURE,"Attacks the");
            jo.put(ID_TXT_AIR,"Air");
            jo.put(ID_TXT_TERRE,"Earth");  
            jo.put(ID_TXT_SANTE,"Health");
            jo.put(ID_TXT_GAIN,"Gain");
            jo.put(ID_TXT_VITESSE,"Speed");
            jo.put(ID_TXT_TERRIENNE,"Earthling");
            jo.put(ID_TXT_AERIENNE,"Air");
            
            jo.put(ID_TXT_DIALOG_ARRETER_PARTIE,"Are you sure you would like to stop the game ?");
            jo.put(ID_TXT_DIALOG_QUITTER_JEU,"Are you sure you would like to quit the game ?");
            jo.put(ID_TXT_DIALOG_SAUVER, "Would you like to save your score ?");
            
            jo.put(ID_TITRE_PARTIE_SOLO, "SINGLE PLAYER");
            jo.put(ID_TITRE_INFO_SELECTION,"Information of selection");
            jo.put(ID_TITRE_PARTIE_TERMINEE,"Game over");
            
            jo.put(ID_TXT_SCORE_OBTENU,"Your score");
            jo.put(ID_TXT_VOTRE_PSEUDO,"Your pseudo");

            jo.put(ID_TXT_OK,"OK");
            jo.put(ID_TXT_FERMER,"Close");
            
            Langue.sauver("lang/en_En.json");
        
            
            
            // FR
            
            jo = new JSONObject();
            
            jo.put(ID_TXT_BTN_SOLO, "Solo");
            jo.put(ID_TXT_BTN_VOS_PARTIES, "Vos parties");
            jo.put(ID_TXT_BTN_REJOINDRE, "Rejoindre");
            jo.put(ID_TXT_BTN_CREER, "Créer");
            jo.put(ID_TXT_BTN_REGLES, "Règles");
            jo.put(ID_TXT_BTN_A_PROPOS, "A propos");
            jo.put(ID_TXT_BTN_OPTIONS, "Options");
            jo.put(ID_TXT_BTN_QUITTER, "Quitter");
            jo.put(ID_TXT_BTN_RETOUR, "Retour");
            
            jo.put(ID_TXT_BTN_FICHIER, "Fichier");
            jo.put(ID_TXT_BTN_EDITION, "Edition");
            jo.put(ID_TXT_BTN_JEU, "Jeu");
            jo.put(ID_TXT_BTN_AFFICHAGE, "Affichage");
            jo.put(ID_TXT_BTN_SON, "Son");
            jo.put(ID_TXT_BTN_AIDE, "Aide");
            
            
            jo.put(ID_TXT_CLIQUER_SUR_TERRAIN, "Cliquez sur un terrain débloqué pour commencer une partie.");
            jo.put(ID_TXT_BTN_LANCER_VAGUE,"Lancer la vague");
            
            jo.put(ID_TXT_BTN_RETOUR_MENU_P, "Retour au menu principale");
            jo.put(ID_TXT_BTN_REDEMARRER_PARTIE, "Redémarrer la partie");
            
            jo.put(ID_TXT_NIVEAU,"niveau");
            jo.put(ID_TXT_BTN_RAYONS_DE_PORTEE,"Rayons de portée");
            jo.put(ID_TXT_BTN_MODE_DEBUG,"Mode debug");
            jo.put(ID_TXT_BTN_MAILLAGE,"Maillage");
            jo.put(ID_TXT_BTN_ACTIVE_DESACTIVE,"Activé / Désactivé");
            jo.put(ID_TXT_BTN_PAUSE,"Pause");
            
            jo.put(ID_TXT_DIALOG_ARRETER_PARTIE,"Etes-vous sûr de vouloir arrêter cette partie ?");
            jo.put(ID_TXT_DIALOG_QUITTER_JEU,"Etes-vous sûr de vouloir quitter le jeu ?");
            jo.put(ID_TXT_DIALOG_SAUVER, "Voulez vous sauver votre score ?");
            
            jo.put(ID_TITRE_PARTIE_SOLO, "Partie solo");
            jo.put(ID_TITRE_INFO_SELECTION,"Information sur la selection");
            jo.put(ID_TITRE_PARTIE_TERMINEE,"Partie terminee");
             
            jo.put(ID_TXT_VAGUE_SUIVANTE,"Vague suivante");
            
            jo.put(ID_TXT_BTN_AMELIORER,"Améliorer");
            jo.put(ID_TXT_BTN_VENDRE,"Vendre");
            
            jo.put(ID_TXT_PRIX_ACHAT,"Prix d'achat");
            jo.put(ID_TXT_VALEUR_PRIX,"Valeur,Prix");
            jo.put(ID_TXT_DEGATS,"Dégâts");
            jo.put(ID_TXT_PORTEE,"Portée");
            jo.put(ID_TXT_TIRS_SEC,"Tirs / sec.");
            jo.put(ID_TXT_DPS,"DPS");
            jo.put(ID_TXT_ATTAQE_LA_CREATURE,"Attaque la créature");
            
            jo.put(ID_TXT_AIR,"Air");
            jo.put(ID_TXT_TERRE,"Terre");
            
            jo.put(ID_TXT_SANTE,"Santé");
            jo.put(ID_TXT_GAIN,"Gain");
            jo.put(ID_TXT_VITESSE,"Vitesse");
            
            jo.put(ID_TXT_TERRIENNE,"Terrienne");
            jo.put(ID_TXT_AERIENNE,"Aérienne");
            
            jo.put(ID_TXT_SCORE_OBTENU,"Score obtenu");
            jo.put(ID_TXT_VOTRE_PSEUDO,"Ton pseudo");
           
            jo.put(ID_TXT_OK,"OK");
            jo.put(ID_TXT_FERMER,"Fermer");
            
            Langue.sauver("lang/fr_FR.json");
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        } 
    }
}
