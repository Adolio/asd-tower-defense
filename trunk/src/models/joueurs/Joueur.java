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
     * Nombre de pieces d'or du joueur.
     * <br>
     * Cette variable fluctue en fonction des creatures tuees et de 
     * l'achat et vente des tours.
     */
    private int nbPiecesDOr = 0;
    
    /**
     * Equipe du joueur
     */
    private Equipe equipe;
   
    /**
     * score courant du joueur. Cette valeur equivaux a la somme 
     * de toutes les pieces d'or amassee par le joueur durant la partie.
     */
    private int score = 0;
    
    /**
     * Nombre de créatures tuées
     */
    private int nbCreaturesTuees = 0;
    
    /**
     * Constructeur
     */
    public Joueur(Equipe equipe)
    {
        if(equipe == null)
            throw new IllegalArgumentException("Equipe nulle");
          
        this.equipe = equipe;
        this.id = ++cmpId;
    }

    /**
     * Permet de recuperer le score du joueur
     * 
     * @return le score
     */
    public int getScore()
    {
        return score;
    }
    
    /**
     * Permet de modifier le nombre de pieces d'or.
     * 
     * @param nbPiecesDOr le nouveau nombre de pieces d'or
     */
    public void setNbPiecesDOr(int nbPiecesDOr)
    {
        this.nbPiecesDOr = nbPiecesDOr;
    }

    public int getNbPiecesDOr()
    {
        return nbPiecesDOr;
    }

    public void setScore(int score)
    {
        this.score = score;
    }
    
    public Equipe getEquipe()
    {
        return equipe;
    }
    
    
    /**
     * Permet de savoir si le joueur a perdu
     * 
     * @return true s'il a perdu, false sinon
     */
    public boolean aPerdu()
    {
        return equipe.getNbViesRestantes() <= 0;
    }
}
