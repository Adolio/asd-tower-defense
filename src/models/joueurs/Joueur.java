package models.joueurs;

import models.outils.Score;

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
    private String pseudo;
    
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
    private Score score = new Score();
    
    /**
     * Nombre de créatures tuées
     */
    private int nbCreaturesTuees = 0;
    
    /**
     * Emplacement du joueur sur le terrain
     * 
     * Permet de définir les zones de construction du joueur
     */
    private EmplacementJoueur emplacement;
    
    /**
     * Constructeur
     */
    public Joueur(String pseudo)
    {
        this.pseudo = pseudo;
        this.id = ++cmpId;
    }

    /**
     * Permet de recuperer le score du joueur
     * 
     * @return le score
     */
    public int getScore()
    {
        return score.getValeur();
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

    /**
     * Permet de récupérer le nombre de pieces d'or du joueur
     * 
     * @return le nombre de pieces d'or du joueur
     */
    public int getNbPiecesDOr()
    {
        return nbPiecesDOr;
    }

    /**
     * Permet de récupérer le score du joueur
     * 
     * @return le score du joueur
     */
    public void setScore(int score)
    {
        this.score.setValeur(score);
    }
    
    /**
     * Permet de récupérer le nombre d'étoiles du joueur
     * 
     * @return le nombre d'étoiles du joueur
     */
    public int getNbEtoiles()
    {
        return this.score.getNbEtoiles();
    }
    
    /**
     * Permet de récupérer l'équipe du joueur
     * 
     * @return l'équipe du joueur
     */
    public Equipe getEquipe()
    {
        return equipe;
    }
    
    /**
     * Permet de récupérer l'emplacement du joueur
     * 
     * @return l'emplacement du joueur
     */
    public EmplacementJoueur getEmplacement()
    {
        return emplacement;
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

    /**
     * Permet de récupérer le pseudo
     * 
     * @return le pseudo
     */
    public String getPseudo()
    {
        return pseudo;
    }
    
    /**
     * Permet de modifier l'équipe du joueur
     * 
     * @param equipe la nouvelle équipe du joueur
     */
    public void setEquipe(Equipe equipe)
    {
        // si le joueur avait une equipe qui le contenait
        if(this.equipe != null && this.equipe.contient(this))
            this.equipe.retirerJoueur(this);
        
        this.equipe = equipe;
    }
    
    
    
    
    
    /**
     * Permet de modifier l'emplacement du joueur
     * 
     * @param emplacementJoueur l'emplacement du joueur
     */
    public void setEmplacementJoueur(EmplacementJoueur emplacementJoueur)
    {
        if(emplacementJoueur != null 
        && emplacementJoueur.getJoueur() != this) // fin de récursion de maj
            emplacementJoueur.setJoueur(this);
        
        this.emplacement = emplacementJoueur;
    }

    
    
    public void quitterEmplacementJoueur()
    {
        // fin de récursion de maj
        if(emplacement != null && emplacement.getJoueur() != this) 
            emplacement.retirerJoueur();
        
        emplacement = null;
    } 
}
