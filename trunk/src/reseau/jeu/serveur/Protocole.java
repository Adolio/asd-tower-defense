package reseau.jeu.serveur;

import java.util.ArrayList;
import models.creatures.*;
import models.joueurs.Joueur;
import models.terrains.Terrain;
import models.tours.*;
import org.json.*;

/**
 * Classe de définition du protocole.
 * 
 * Elle permet de créer les messages qui transiteront sur les canaux.
 * 
 * @author Aurélien Da Campo
 * @version 1.0 | mai 2010
 */
public class Protocole implements ConstantesServeurJeu
{

    //------------------------------
    //-- CONSTRUCTION DE MESSAGES --
    //------------------------------
    
    public static String construireMsgJoueurInitialisation(Joueur joueur, Terrain terrain)
    {
        JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", JOUEUR_INITIALISATION);
            msg.put("STATUS", OK);
            msg.put("ID_JOUEUR", joueur.getId());
            msg.put("ID_EMPLACEMENT", joueur.getEmplacement().getId());
            msg.put("ID_EQUIPE", joueur.getEquipe().getId());
            msg.put("NOM_FICHIER_TERRAIN", terrain.getClass().getSimpleName()+".map");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg.toString(); 
    }
    
    public static String construireMsgJoueursEtat(ArrayList<Joueur> joueurs)
    {
        JSONObject msg = new JSONObject();
        JSONArray JSONjoueurs = new JSONArray();
        
        try
        {
            msg.put("TYPE", JOUEURS_ETAT);
            
            Joueur joueur;
            JSONObject JSONjoueur;
              
            for(int j=0; j < joueurs.size();j++)
            {
                // recuperation du joueur
                joueur = joueurs.get(j);
                    
                // construction du joueur
                JSONjoueur = new JSONObject();
                JSONjoueur.put("ID_JOUEUR", joueur.getId());
                JSONjoueur.put("NOM_JOUEUR", joueur.getPseudo());
                JSONjoueur.put("ID_EMPLACEMENT", joueur.getEmplacement().getId());
                JSONjoueur.put("ID_EQUIPE", joueur.getEquipe().getId());
                
                // ajout à la liste des joueurs
                JSONjoueurs.put(JSONjoueur);
            }
            
            msg.put("JOUEURS",JSONjoueurs);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg.toString(); 
    }
    
    
    public static String construireMsgJoueurInitialisation(int etat)
    {
        JSONObject msg = new JSONObject();
        
        try {
            msg.put("TYPE", JOUEUR_INITIALISATION);
            msg.put("STATUS", etat);
        } 
        catch (JSONException jsone){
            jsone.printStackTrace();
        }
        
        return msg.toString();
    }
    
    
    public static String construireMsgPartieChangementEtat(int etat)
    {
        JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", PARTIE_ETAT);
            msg.put("ETAT", etat); 
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg.toString(); 
    }
    
    /**
     * Permet de construire le message d'état d'un joueur
     * 
     * @param joueur le joueur
     * @return Une structure JSONObject
     */
    public static String construireMsgJoueurEtat(Joueur joueur)
    {
        JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", JOUEUR_ETAT);
            msg.put("ID_JOUEUR", joueur.getId());
            msg.put("NB_PIECES_OR", joueur.getNbPiecesDOr());
            msg.put("NB_VIES_RESTANTES_EQUIPE", joueur.getEquipe().getNbViesRestantes());
            msg.put("REVENU", joueur.getRevenu());
            msg.put("SCORE", joueur.getScore());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg.toString(); 
    }
    
    
    /**
     * Permet de construire le message de demande d'ajout d'une tour
     * 
     * @param tour la tour
     * @return Une structure JSONObject
     */
    public static String construireMsgTourAjout(Tour tour)
    {
        JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", TOUR_AJOUT);
            msg.put("ID_PROPRIETAIRE", tour.getPrioprietaire().getId());
            msg.put("ID_TOUR", tour.getId());
            msg.put("X", tour.x);
            msg.put("Y", tour.y);
            msg.put("TYPE_TOUR", TypeDeTour.getTypeDeTour(tour));
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg.toString();
    }
    
    /**
     * Permet de construire le message de demande d'amélioration d'une tour
     * 
     * @param tour la tour
     * @return Une structure JSONObject
     */
    public static String construireMsgTourAmelioration(Tour tour)
    {
        JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", TOUR_AMELIORATION);
            msg.put("ID_TOUR", tour.getId());
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg.toString();
    }
    
    /**
     * Permet de construire le message de demander la suppression d'une tour
     * 
     * @param tour la tour
     * @return Une structure JSONObject
     */
    public static String construireMsgTourSuppression(Tour tour)
    {
        JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", TOUR_SUPRESSION);
            msg.put("ID_TOUR", tour.getId());
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg.toString();
    }
    
    // CREATURES
    
    /**
     * Permet de construire le message d'ajout d'une créature
     * 
     * @param creature la creature
     * @return Une structure JSONObject
     */
    public static String construireMsgCreatureAjout(Creature creature)
    {
        JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", CREATURE_AJOUT);
            msg.put("TYPE_CREATURE", TypeDeCreature.getTypeCreature(creature));
            msg.put("ID_CREATURE", creature.getId());
            msg.put("ID_EQUIPE_CIBLEE", creature.getEquipeCiblee().getId());
            msg.put("X", creature.x);
            msg.put("Y", creature.y);
            msg.put("SANTE_MAX", creature.getSanteMax());
            msg.put("NB_PIECES_OR", creature.getNbPiecesDOr());
            msg.put("VITESSE", creature.getVitesseNormale());  
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg.toString();  
    }
    
    /**
     * Permet de construire le message d'état d'une créature
     * 
     * @param creature la creature
     * @return Une structure JSONObject
     */
    public static String construireMsgCreatureEtat(Creature creature)
    {
        JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", CREATURE_ETAT);
            msg.put("ID_CREATURE", creature.getId());
           
            msg.put("X", creature.x);
            msg.put("Y", creature.y);
            msg.put("SANTE", creature.getSante());
            msg.put("ANGLE", creature.getAngle());
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg.toString();
    }
    
    /**
     * Permet de construire le message de suppression d'une créature
     * 
     * @param creature la creature
     * @return Une structure JSONObject
     */
    public static String construireMsgCreatureSuppression(Creature creature)
    {
        JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", CREATURE_SUPPRESSION);
            msg.put("ID_CREATURE", creature.getId()); 
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg.toString();  
    }

    public static String construireMsgPartieTerminee()
    {
        JSONObject msg = new JSONObject();
        
        try
        {
            msg.put("TYPE", PARTIE_TERMINEE);
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg.toString(); 
    }

    public static String construireMsgChat(String message, int cible)
    {
        JSONObject msg = new JSONObject();
        
        try 
        {
            msg.put("TYPE", JOUEUR_MESSAGE);
            msg.put("CIBLE", cible);
            msg.put("MESSAGE", message);
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg.toString();
    }

    public static String construireMsgChangerEquipe(int etat)
    {
        JSONObject msg = new JSONObject();
        
        try {
            msg.put("TYPE", JOUEUR_CHANGER_EQUIPE);
            msg.put("STATUS", etat);
        }
        catch (JSONException jsone){
            jsone.printStackTrace();
        }
        
        return msg.toString();
    }

    public static String construireMsgCreatureArrivee(Creature creature)
    {
        JSONObject msg = new JSONObject();
        
        try {
            msg.put("TYPE", CREATURE_ARRIVEE);
            msg.put("ID_CREATURE", creature.getId());
        }
        catch (JSONException jsone){
            jsone.printStackTrace();
        }
        
        return msg.toString();
    }
    
    
    public static String construireMsgMessage(int idAuteur, String contenu)
    {
        JSONObject msg = new JSONObject();
        
        try {
            // Construction de la structure JSON
            msg.put("TYPE", JOUEUR_MESSAGE);
            msg.put("ID_JOUEUR", idAuteur);
            msg.put("MESSAGE", contenu);
        }
        catch (JSONException jsone){
            jsone.printStackTrace();
        }
        
        return msg.toString();
    }
}
