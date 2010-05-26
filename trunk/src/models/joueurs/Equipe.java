package models.joueurs;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe de gestion d'une equipe.
 * 
 * @author Aurelien Da Campo
 * @version 1.1 | mai 2010
 * @since jdk1.6.0_16
 */
public class Equipe implements Serializable
{
    private static final long serialVersionUID = 1L;

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
    * Zone de départ des créatures ennemies
    */
   private ArrayList<Rectangle> zonesDepartCreatures = new ArrayList<Rectangle>();
   
   /**
    * Zone d'arrivée des créatures ennemies
    */
   private Rectangle zoneArriveeCreatures;

   /**
    * Stockage des emplacements de joueurs
    */
   private ArrayList<EmplacementJoueur> emplacementsJoueur = new ArrayList<EmplacementJoueur>();
   
   /**
    * TODO
    */
   private double longueurChemin = 0.0;
   
   /**
    * Constucteur
    * 
    * @param nom
    * @param couleur
    */
   public Equipe(String nom, Color couleur)
   {
       this.nom = nom;
       this.couleur = couleur;
   }
   
   /**
    * Permet de récupérer le nom
    * 
    * @return le nom
    */
   public String getNom()
   {
       return nom;
   }
   
   /**
    * Permet de récupérer la couleur
    * 
    * @return la couleur
    */
   public Color getCouleur()
   {
       return couleur;
   }
   
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
          
       EmplacementJoueur ej = trouverEmplacementDiponible();
       if(ej == null) // emplacement non trouvé
           throw new IllegalArgumentException("Aucun emplacement disponible");
       else // emplacement trouvé
       {
           // on retire le joueur de son ancienne equipe
           if(joueur.getEquipe() != null)
               joueur.getEquipe().retirerJoueur(joueur);
           
           // on l'ajout dans la nouvelle equipe
           joueurs.add(joueur);

           // on modifier sont equipe
           joueur.setEquipe(this);
           
           // on lui attribut le nouvel emplacement
           joueur.setEmplacementJoueur(ej);
       }
   }

   /**
    * TODO
    * @return
    */
   public EmplacementJoueur trouverEmplacementDiponible()
   {
       // cherche un emplacement disponible
       for(EmplacementJoueur ej : emplacementsJoueur)
           if(ej.getJoueur() == null)
               return ej;
  
       return null;
   }
   
   /**
    * Permet d'ajouter un joueur
    * 
    * @param joueur le joueur a ajouter
    * @param l'emplacement du joueur sur le terrain
    * @throws IllegalArgumentException si le joueur est nul
    */
   /*public void ajouterJoueur(Joueur joueur, EmplacementJoueur emplacementJoueur)
   {
       joueurs.add(joueur);
       joueur.setEquipe(this);
       
       //emplacementJoueur.setJoueur(joueur);
       joueur.setEmplacementJoueur(emplacementJoueur);
   }*/
   
   /**
    * TODO
    * @param joueur
    */
   public void retirerJoueur(Joueur joueur)
   {
       // effacement
       joueurs.remove(joueur);
       
       // quitte l'emplacement
       if(joueur.getEmplacement() != null)
           joueur.getEmplacement().retirerJoueur();
       
       // quitte l'equipe
       joueur.setEquipe(null);
   }

   public boolean contient(Joueur joueur)
   {
       return joueurs.contains(joueur);
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

    /**
     * Permet d'ajouter une zone de départ des créatures ennemies
     * 
     * @param zone la zone
     */
    public void ajouterZoneDepartCreatures(Rectangle zone)
    {
        zonesDepartCreatures.add(zone);
    }
    
    /**
     * Permet de récupérer la zone de départ des créatures
     * 
     * @return la zone de départ des créatures
     */
    public Rectangle getZoneDepartCreatures(int index)
    {
        return zonesDepartCreatures.get(index);
    }
    
    /**
     * Permet d'ajouter une zone d'arrivée des créatures ennemies
     * 
     * @param zone la zone
     */
    public void setZoneArriveeCreatures(Rectangle zone)
    {
        zoneArriveeCreatures = zone;
    }
    
    /**
     * Permet de récupérer la zone d'arrivee des créatures
     * 
     * @return la zone d'arrivee des créatures
     */
    public Rectangle getZoneArriveeCreatures()
    {
        return zoneArriveeCreatures;
    }
  
    /**
     * Permet d'ajouter un emplacement de joueur
     * 
     * @param emplacementJoueur l'emlacement
     */
    public void ajouterEmplacementJoueur(EmplacementJoueur emplacementJoueur)
    {
        emplacementsJoueur.add(emplacementJoueur);
    }

    /**
     * Permet de savoir le nombre d'emplacements disponibles de joueur
     * 
     * @return le nombre d'emplacements disponibles de joueur 
     */
    public int getNbEmplacements()
    {
        return emplacementsJoueur.size();
    }

    /**
     * Perme de recupérer les emplacements de joueur
     * 
     * @return les emplacements de joueur
     */
    public ArrayList<EmplacementJoueur> getEmplacementsJoueur()
    {
        return emplacementsJoueur;
    }
    
    @Override
    public String toString()
    {
        return nom;
    }

    /**
     * Permet de récupérer la longueur du chemin
     *
     * @return la longueur du chemin
     */
    public double getLongueurChemin()
    {
        return longueurChemin;
    }
    
    /**
     * Permet de modifier la longueur du chemin
     * 
     * @param longueur la longueur du chemin
     */
    public void setLongueurChemin(double longueur)
    {
        this.longueurChemin = longueur;
    }

    /**
     * Permet de savoir si l'equipe a perdue
     * 
     * @return true si elle a perdue, false sinon
     */
    public boolean aPerdu()
    {
        return nbViesRestantes <= 0;
    }
}
