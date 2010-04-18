package models.joueurs;

public class Joueur
{
    /**
     * Compteur pour générer les identificateurs.
     */
    private static int cmpId = 0;
    
    /**
     * Adresse du joueur
     */
    private String IP;
    
    /**
     * Identificateur du joueur
     */
    private int id;
    
    /**
     * Pseudo
     */
    private int pseudo;
    
    /**
     * Equipe du joueur
     */
    private Equipe equipe;
   
    /**
     * Score
     */
    private int score = 0;
    
    /**
     * Nombre de créatures tuées
     */
    private int nbCreaturesTuees = 0;
    
    /**
     * Constructeur
     */
    public Joueur()
    {
        this.id = ++cmpId;
    }
}
