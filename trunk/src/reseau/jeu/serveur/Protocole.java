package reseau.jeu.serveur;

import models.creatures.Creature;
import models.creatures.TypeDeCreature;
import models.joueurs.Equipe;
import models.joueurs.Joueur;
import models.terrains.Terrain;
import models.tours.Tour;
import models.tours.TypeDeTour;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Aurélien Da Campo
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
    
    public static String construireMsgJoueurInitialisation(int pasDePlace)
    {
        JSONObject msg = new JSONObject();
        
        try {
            msg.put("TYPE", JOUEUR_INITIALISATION);
            msg.put("STATUS", PAS_DE_PLACE);
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
            msg.put("JOUEUR", tour.getPrioprietaire().getId());
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
            msg.put("TYPE", MSG);
            JSONObject content = new JSONObject();
            content.put("CIBLE", A_TOUS);
            content.put("MESSAGE", message);
            msg.put("CONTENU", content);
        } 
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        return msg.toString();
    }

    public static String construireMsgChangerEquipeOk(Joueur joueur, Equipe e)
    {
        JSONObject msg = new JSONObject();
        
        try {
            msg.put("TYPE", JOUEUR_CHANGER_EQUIPE);
            msg.put("STATUS", OK);
            msg.put("ID_EQUIPE", joueur.getEquipe().getId());
            msg.put("ID_EMPLACEMENT", joueur.getEmplacement().getId()); 
        }
        catch (JSONException jsone){
            jsone.printStackTrace();
        }
        
        return msg.toString();
    }

    public static String construireMsgChangerEquipeEchec(Joueur joueur, Equipe e)
    {
        JSONObject msg = new JSONObject();
        
        try {
            msg.put("TYPE", JOUEUR_CHANGER_EQUIPE);
            msg.put("STATUS", PAS_DE_PLACE);
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
}
