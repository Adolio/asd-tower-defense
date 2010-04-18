package models.joueurs;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Classe de gestion d'une equipe.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | 16 avril 2010
 * @since jdk1.6.0_16
 */
public class Equipe
{
   /**
    * Identificateur de l'equipe
    */
   private int id;
   
   /**
    * Nom de l'equipe
    */
   private String nom;
   
   /**
    * Couleur
    */
   private Color couleur;
   
   /**
    * Liste des joueurs
    */
   private ArrayList<Joueur> joueurs = new ArrayList<Joueur>();
   
   /**
    * nombre de vies restantes. 
    * <br>
    * Note : Lorsque un ennemi atteint la zone d'arrive, le nombre de vies est
    * decremente.
    */
   private int nbViesRestantes;
   
   /**
    * Zones de construction
    */
   private Rectangle[] zonesDeConstruction;
   
   /**
    * Permet d'ajouter un joueur
    * 
    * @param joueur le joueur a ajouter
    * @throws IllegalArgumentException si le joueur est nul
    */
   public void ajouterJoueur(Joueur joueur) throws IllegalArgumentException
   {
       if(joueur == null)
           throw new IllegalArgumentException();
               
       joueurs.add(joueur);
   }
   
   /**
    * Permet de recuperer la collection des joueurs
    * 
    * @return la collection des joueurs
    */
   public ArrayList<Joueur> getJoueurs()
   {
       return joueurs;
   }
   
   /**
    * Permet de recuperer le score de l'equipe qui correspond à la somme des scores
    * de joueurs de l'équipe.
    * 
    * @return le score
    */
   public int getScore()
   {
       int somme = 0;
       
       for(Joueur joueur : joueurs)
           somme += joueur.getScore();
       
       return somme;
   }
   
   /**
    * Permet de recuperer le nombre de vies restantes de l'equipe
    * 
    * @return le nombre de vies restantes de l'equipe
    */
   public int getNbViesRestantes()
   {
       return nbViesRestantes;
   }

   /**
    * Permet de faire perdre une vie a l'equipe
    */
    synchronized public void perdreUneVie()
    {
        nbViesRestantes--;
    }

    /**
     * Permet de modifier le nombre de vies restantes de l'equipe
     * 
     * @param nbViesRestantes le nouveau nombre de vies restantes
     */
    public void setNbViesRestantes(int nbViesRestantes)
    {
        this.nbViesRestantes = nbViesRestantes;
    }
}
