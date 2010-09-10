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
    public static final String ID_TXT_BTN_OK                = "TXT_BTN_OK";
    public static final String ID_TXT_BTN_FERMER            = "TXT_BTN_FEMER";
    
    // DIALOG
    public static final String ID_TXT_DIALOG_ARRETER_PARTIE = "TXT_DIALOG_ARRETER_PARTIE";
    public static final String ID_TXT_DIALOG_QUITTER_JEU    = "TXT_DIALOG_QUITTER_JEU";
    public static final String ID_TXT_DIALOG_SAUVER         = "TXT_DIALOG_SAUVER";

    // ADRESSES
    public static final String ID_ADRESSE_A_PROPOS          = "ADRESSE_A_PROPOS";
    public static final String ID_ADRESSE_REGLES_DU_JEU     = "ADRESSE_REGLES_DU_JEU";
    
    // TITRES
    public static final String ID_TITRE_PARTIE_SOLO         = "TITRE_PARTIE_SOLO"; 
    public static final String ID_TITRE_INFO_SELECTION      = "TITRE_INFO_SELECTION";
    public static final String ID_TITRE_PARTIE_TERMINEE     = "TITRE_PARTIE_TERMINEE";
    public static final String ID_TITRE_ETOILE_GAGNEE       = "TITRE_ETOILE_GAGNEE";
    public static final String ID_TITRE_CHOIX_TERRAIN       = "TITRE_CHOIX_TERRAIN";
    public static final String ID_TITRE_RESEAU              = "TITRE_RESEAU";
    public static final String ID_TITRE_PARTIE_PERSONNALISEES = "TITRE_PARTIE_PERSONNALISEES";
    
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
    public static final String ID_TXT_ATTAQUE_LA_CREATURE   = "TXT_ATTAQUE_LA_CREATURE";
    public static final String ID_TXT_AIR                   = "TXT_AIR";
    public static final String ID_TXT_TERRE                 = "TXT_TERRE";
    public static final String ID_TXT_SANTE                 = "TXT_SANTE";
    public static final String ID_TXT_GAIN                  = "TXT_GAIN";
    public static final String ID_TXT_VITESSE               = "TXT_VITESSE";
    public static final String ID_TXT_TERRIENNE             = "TXT_TERRIENNE";
    public static final String ID_TXT_AERIENNE              = "TXT_AERIENNE";
    public static final String ID_TXT_SCORE_OBTENU          = "TXT_SCORE_OBTENU";
    public static final String ID_TXT_VOTRE_PSEUDO          = "TXT_VOTRE_PSEUDO";
    public static final String ID_TXT_DESC_TOUR_ARCHER      = "TXT_DESC_TOUR_ARCHER";
    public static final String ID_TXT_DESC_TOUR_CANON       = "TXT_DESC_TOUR_CANON";
    public static final String ID_TXT_DESC_TOUR_ANTI_AERIENNE = "TXT_DESC_TOUR_ANTI_AERIENNE";
    public static final String ID_TXT_DESC_TOUR_GLACE       = "TXT_DESC_TOUR_GLACE";
    public static final String ID_TXT_DESC_TOUR_FEU         = "TXT_DESC_TOUR_FEU";
    public static final String ID_TXT_DESC_TOUR_ELECTRIQUE  = "TXT_DESC_TOUR_ELECTRIQUE";
    public static final String ID_TXT_DESC_TOUR_TERRE       = "TXT_DESC_TOUR_TERRE";
    public static final String ID_TXT_DESC_TOUR_AIR         = "TXT_DESC_TOUR_AIR";
    public static final String ID_TXT_LA_PLUS_PROCHE        = "TXT_LA_PLUS_PROCHE";
    public static final String ID_TXT_LA_PLUS_LOIN          = "TXT_LA_PLUS_LOIN";
    public static final String ID_TXT_LA_PLUS_FAIBLE        = "TXT_LA_PLUS_FAIBLE";
    public static final String ID_TXT_LA_PLUS_FORTE         = "TXT_LA_PLUS_FORTE";
    public static final String ID_TXT_JOUEUR                = "TXT_JOUEUR";
    public static final String ID_TXT_COMMANDES             = "TXT_COMMANDES";
    public static final String ID_TXT_RESEAU                = "TXT_RESEAU";
    public static final String ID_TXT_STYLE                 = "TXT_STYLE";
    public static final String ID_TXT_ASTUCE_1              = "TXT_ASTUCE_1";
    public static final String ID_TXT_ASTUCE_2              = "TXT_ASTUCE_2";
    public static final String ID_TXT_ASTUCE_3              = "TXT_ASTUCE_3";
    public static final String ID_TXT_ASTUCE_4              = "TXT_ASTUCE_4";
    public static final String ID_TXT_ASTUCE_5              = "TXT_ASTUCE_5";
    public static final String ID_TXT_ASTUCE_6              = "TXT_ASTUCE_6";
    public static final String ID_TXT_ASTUCE_7              = "TXT_ASTUCE_7";
    public static final String ID_TXT_ASTUCE_8              = "TXT_ASTUCE_8";
    public static final String ID_TXT_BTN_EDITEUR_DE_TERRAIN = "TXT_BTN_EDITEUR_DE_TERRAIN";
    public static final String ID_TXT_BTN_DEMARRER          = "TXT_BTN_DEMARRER";
    public static final String ID_TXT_LES_X_MEILLEURS_SCORES = "TXT_LES_X_MEILLEURS_SCORES";
    public static final String ID_TXT_SCORE                 = "TXT_SCORE";
    public static final String ID_TXT_DUREE                 = "TXT_DUREE";
    public static final String ID_TXT_DATE                  = "TXT_DATE";
    

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
            jo.put(ID_TXT_BTN_EDITEUR_DE_TERRAIN,"Field editor");
            jo.put(ID_TXT_BTN_DEMARRER,"Start");
            
            jo.put(ID_TXT_NIVEAU,"level");
            jo.put(ID_TXT_CLIQUER_SUR_TERRAIN, "Click on an available field to start the game.");
            jo.put(ID_TXT_VAGUE_SUIVANTE,"Next wave");
            jo.put(ID_TXT_PRIX_ACHAT,"Purchase price");
            jo.put(ID_TXT_VALEUR_PRIX,"Value, price");
            jo.put(ID_TXT_DEGATS,"Damage");
            jo.put(ID_TXT_PORTEE,"Range");
            jo.put(ID_TXT_TIRS_SEC,"Hits / sec.");
            jo.put(ID_TXT_DPS,"DPS");
            jo.put(ID_TXT_ATTAQUE_LA_CREATURE,"Attacks the");
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
            
            jo.put(ID_ADRESSE_A_PROPOS,"aPropos/aPropos_en.html");
            jo.put(ID_ADRESSE_REGLES_DU_JEU,"donnees/regles/regles_en.html");
            
            jo.put(ID_TITRE_PARTIE_SOLO, "Single player");
            jo.put(ID_TITRE_INFO_SELECTION,"Information of selection");
            jo.put(ID_TITRE_PARTIE_TERMINEE,"Game over");
            jo.put(ID_TITRE_CHOIX_TERRAIN,"Select your battlefield");
            jo.put(ID_TITRE_PARTIE_PERSONNALISEES,"Costum game"); 
            jo.put(ID_TITRE_RESEAU,"Network");
            jo.put(ID_TITRE_ETOILE_GAGNEE,"Star won");
            
            jo.put(ID_TXT_SCORE_OBTENU,"Your score");
            jo.put(ID_TXT_VOTRE_PSEUDO,"Your pseudo");
            jo.put(ID_TXT_JOUEUR,"Player");
            jo.put(ID_TXT_COMMANDES,"Commands");
            jo.put(ID_TXT_RESEAU,"Network");
            jo.put(ID_TXT_STYLE,"Style");   
            
            jo.put(ID_TXT_BTN_OK,"OK");
            jo.put(ID_TXT_BTN_FERMER,"Close");
            
            jo.put(ID_TXT_DESC_TOUR_ARCHER, "Quick tower inflicts low damage. " +
            "It attacks all types of creatures.");
            
            jo.put(ID_TXT_DESC_TOUR_CANON, "Slow tower with fairly good splash damage. " +
            "It only attacks creatures on earth");
            
            jo.put(ID_TXT_DESC_TOUR_ANTI_AERIENNE, "Very powerful tower but it only " +
            "attacks flying creatures.");
                        
            jo.put(ID_TXT_DESC_TOUR_GLACE, "Quick tower that slows creatures. " +
            "It attacks all types of creatures."); 
            
            jo.put(ID_TXT_DESC_TOUR_FEU, "Extremely fast tower which shoots fireballs. " +
            "It attacks all types of creatures"); 
            
            jo.put(ID_TXT_DESC_TOUR_ELECTRIQUE, "Tower which emits powerful arcs " +
            "to a relatively low frequency. It only attacks creatures on earth."); 
            
            jo.put(ID_TXT_DESC_TOUR_TERRE, "Slow tower which making " +
            "awesome splash damage with a very huge range. Unfortunately, " +
            "it only attacks creatures on earth.");
            
            jo.put(ID_TXT_DESC_TOUR_AIR, "It throws gusts that hurt all the " +
            "flying creatures in its path. The damage from a burst decreases " +
            "gradually. The indicated damage correspond to the damage inflicted at " +
            "the source of the gust.");
            
            jo.put(ID_TXT_LA_PLUS_PROCHE,"nearest creature");
            jo.put(ID_TXT_LA_PLUS_LOIN,"further creature");
            jo.put(ID_TXT_LA_PLUS_FAIBLE,"weakest creature (hp)");
            jo.put(ID_TXT_LA_PLUS_FORTE,"strongest creature (hp)");
              
            
            
            jo.put(ID_TXT_ASTUCE_1,"The white circle around a tower indicates the range of the tower.");
            jo.put(ID_TXT_ASTUCE_2,"Beware not to get stuck with the flying waves ");
            jo.put(ID_TXT_ASTUCE_3,"All tours are improvable through a few pieces of gold");
            jo.put(ID_TXT_ASTUCE_4,"The ice towers slow creatures, place them strategically");
            jo.put(ID_TXT_ASTUCE_5,"Stars allow you to have access to others playgrounds");
            jo.put(ID_TXT_ASTUCE_6,"At each game over, you can save your score");
            jo.put(ID_TXT_ASTUCE_7,"The sale of a tower makes you recover 60 percent of its total price");
            jo.put(ID_TXT_ASTUCE_8,"This game is free, you can even access the source code");
            
            jo.put(ID_TXT_LES_X_MEILLEURS_SCORES,"The %d best scores");
            
            jo.put(ID_TXT_SCORE,"Score");
            jo.put(ID_TXT_DUREE,"Duration");
            jo.put(ID_TXT_DATE,"Date");
            
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
            jo.put(ID_TXT_BTN_RAYONS_DE_PORTEE,"Rayons de portée");
            jo.put(ID_TXT_BTN_MODE_DEBUG,"Mode debug");
            jo.put(ID_TXT_BTN_MAILLAGE,"Maillage");
            jo.put(ID_TXT_BTN_ACTIVE_DESACTIVE,"Activé / Désactivé");
            jo.put(ID_TXT_BTN_PAUSE,"Pause");
            jo.put(ID_TXT_BTN_LANCER_VAGUE,"Lancer la vague");
            jo.put(ID_TXT_BTN_RETOUR_MENU_P, "Retour au menu principale");
            jo.put(ID_TXT_BTN_REDEMARRER_PARTIE, "Redémarrer la partie");
            jo.put(ID_TXT_BTN_AMELIORER,"Améliorer");
            jo.put(ID_TXT_BTN_VENDRE,"Vendre");
            jo.put(ID_TXT_BTN_OK,"OK");
            jo.put(ID_TXT_BTN_FERMER,"Fermer");
            jo.put(ID_TXT_BTN_EDITEUR_DE_TERRAIN,"Editeur de terrain");
            jo.put(ID_TXT_BTN_DEMARRER,"Démarrer");
            
            jo.put(ID_TXT_DIALOG_ARRETER_PARTIE,"Etes-vous sûr de vouloir arrêter cette partie ?");
            jo.put(ID_TXT_DIALOG_QUITTER_JEU,"Etes-vous sûr de vouloir quitter le jeu ?");
            jo.put(ID_TXT_DIALOG_SAUVER, "Voulez vous sauver votre score ?");
            
            jo.put(ID_ADRESSE_A_PROPOS,"aPropos/aPropos_fr.html");
            jo.put(ID_ADRESSE_REGLES_DU_JEU,"donnees/regles/regles_fr.html");
            
            jo.put(ID_TITRE_PARTIE_SOLO, "Partie solo");
            jo.put(ID_TITRE_INFO_SELECTION,"Information sur la selection");
            jo.put(ID_TITRE_PARTIE_TERMINEE,"Partie terminee");
            jo.put(ID_TITRE_CHOIX_TERRAIN,"Selectionnez votre terrain");
            jo.put(ID_TITRE_PARTIE_PERSONNALISEES,"Partie personnalisees"); 
            jo.put(ID_TITRE_RESEAU,"Reseau");
            jo.put(ID_TITRE_ETOILE_GAGNEE,"Etoile gagnee");
            
            jo.put(ID_TXT_VAGUE_SUIVANTE,"Vague suivante");
            jo.put(ID_TXT_PRIX_ACHAT,"Prix d'achat");
            jo.put(ID_TXT_VALEUR_PRIX,"Valeur,Prix");
            jo.put(ID_TXT_DEGATS,"Dégâts");
            jo.put(ID_TXT_PORTEE,"Portée");
            jo.put(ID_TXT_TIRS_SEC,"Tirs / sec.");
            jo.put(ID_TXT_DPS,"DPS");
            jo.put(ID_TXT_ATTAQUE_LA_CREATURE,"Attaque la créature");
            jo.put(ID_TXT_NIVEAU,"niveau");
            jo.put(ID_TXT_CLIQUER_SUR_TERRAIN, "Cliquez sur un terrain débloqué pour commencer une partie.");
            jo.put(ID_TXT_AIR,"Air");
            jo.put(ID_TXT_TERRE,"Terre");
            jo.put(ID_TXT_SANTE,"Santé");
            jo.put(ID_TXT_GAIN,"Gain");
            jo.put(ID_TXT_VITESSE,"Vitesse");
            jo.put(ID_TXT_TERRIENNE,"Terrienne");
            jo.put(ID_TXT_AERIENNE,"Aérienne");
            jo.put(ID_TXT_SCORE_OBTENU,"Score obtenu");
            jo.put(ID_TXT_VOTRE_PSEUDO,"Ton pseudo");
            jo.put(ID_TXT_JOUEUR,"Joueur");
            jo.put(ID_TXT_COMMANDES,"Commandes");
            jo.put(ID_TXT_RESEAU,"Réseau");
            jo.put(ID_TXT_STYLE,"Style");
            jo.put(ID_TXT_DESC_TOUR_ARCHER, "Tour rapide qui inflige de faible dégâts. " +
            "Elle attaque tous types de créatures.");
            jo.put(ID_TXT_DESC_TOUR_CANON, "Tour lente avec d'assez bons dégâts de zone. " +
            "Elle n'attaque que les créatures terrestres.");
            jo.put(ID_TXT_DESC_TOUR_ANTI_AERIENNE, "Tour très performante qui" +
            " n'attaque que les créatures volantes.");
            jo.put(ID_TXT_DESC_TOUR_GLACE, "tour rapide qui ralenti les créatures. " +
            "Cette tour attaque tous types de creatures.");
            jo.put(ID_TXT_DESC_TOUR_FEU, "Tour extrêment rapide qui projette des boules de feu. " +
            "Cette tour attaque tous types de créatures.");
            jo.put(ID_TXT_DESC_TOUR_ELECTRIQUE, "Tour qui émet des arcs très puissants à une " +
            "fréquence relativement faible. " +
            "Cette tour n'attaque que les créatures terriennes.");
            jo.put(ID_TXT_DESC_TOUR_TERRE, "Tour lente qui fait énormement de dégâts de zone" +
            " et qui a une gigantesque portée. Malheureusement, elle n'attaque " +
            "que les créatures terrestre.");
            jo.put(ID_TXT_DESC_TOUR_AIR, "Elle souffle des rafales qui blessent toutes les " +
            "créatures volantes sur son passage. Plus la rafale s'éloigne " +
            "moins elle inflige de dégâts. Les dégâts indiqués correspondent " +
            "aux dégâts qu'inflige une rafale à sa source.");
            jo.put(ID_TXT_LA_PLUS_PROCHE,"la plus proche");
            jo.put(ID_TXT_LA_PLUS_LOIN,"la plus loin");
            jo.put(ID_TXT_LA_PLUS_FAIBLE,"la plus faible (pv)");
            jo.put(ID_TXT_LA_PLUS_FORTE,"la plus forte (pv)");
            jo.put(ID_TXT_ASTUCE_1,"Le cercle blanc autour d'une tour indique la portee de la tour.");
            jo.put(ID_TXT_ASTUCE_2,"Attention a ne pas vous faire surprendre par les vagues volantes");
            jo.put(ID_TXT_ASTUCE_3,"Toutes les tours sont ameliorables moyennant quelques pieces d'or");
            jo.put(ID_TXT_ASTUCE_4,"Les tours de glace ralentissent les creatures, placez-les strategiquement");
            jo.put(ID_TXT_ASTUCE_5,"Les etoiles vous permettent d'acceder a d'autres terrains de jeu");
            jo.put(ID_TXT_ASTUCE_6,"A chaque fin de partie, vous pourrez sauver votre score");
            jo.put(ID_TXT_ASTUCE_7,"La vente d'une tour vous fait recuperer 60 pourcent de son prix total");
            jo.put(ID_TXT_ASTUCE_8,"Ce jeu est libre et gratuit, vous pouvez meme acceder au code source");

            jo.put(ID_TXT_LES_X_MEILLEURS_SCORES,"Les %d Meilleurs scores");
           
            jo.put(ID_TXT_SCORE,"Score");
            jo.put(ID_TXT_DUREE,"Durée");
            jo.put(ID_TXT_DATE,"Date");
            
            Langue.sauver("lang/fr_FR.json");
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        } 
    }
}
