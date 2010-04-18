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
    * Nombre de vies restantes
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
    */
   public void ajouterJoueur(Joueur joueur)
   {
       if(joueur != null)
           joueurs.add(joueur);
   }
}
