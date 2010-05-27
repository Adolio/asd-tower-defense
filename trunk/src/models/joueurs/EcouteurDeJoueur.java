package models.joueurs;

public interface EcouteurDeJoueur
{
    /**
     * Permet d'informer l'écouteur qu'un joueur a été modifié
     */
    public void joueurMisAJour(Joueur joueur);
}
