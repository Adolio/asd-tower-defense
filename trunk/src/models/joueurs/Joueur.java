/*
  Copyright (C) 2010 Aurelien Da Campo

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/

package models.joueurs;

import models.outils.Score;

/**
 * Le joueur est un élément très important du jeu.
 * 
 * Il est stocké dans une équipe et possède un emplacement de construction.
 * 
 * Il a également des pièces d'or et un score. 
 * Les vies restantes sont gérées par son équipe.
 * 
 * Il possède aussi un revenu qui lui est distribué par le gestionnaire de revenu.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 * @since jdk1.6.0_16
 * @see GestionnaireDeRevenu
 */
public class Joueur
{
    /**
     * Compteur pour générer les identificateurs.
     */
    private static int idCourant = 0;
    
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
     * 
     * Elle est en double pour gérer les revenus flottant (voir gestionnaireDeRevenus)
     */
    private double nbPiecesDOr = 0;
    
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
     * Emplacement du joueur sur le terrain
     * 
     * Permet de définir les zones de construction du joueur
     */
    private EmplacementJoueur emplacement;

    /**
     * Permet de notifier l'ecouteur de joueur
     */
    private EcouteurDeJoueur edj;

    /**
     * Revenu périodique par seconde
     */
    private double revenu = 1.0;
    
    /**
     * Le joueur est hors jeu.
     * 
     * Si tout les joueurs de l'équipe son hors jeu, l'équipe 
     * sera hors jeu et aura perdu.
     */
    private boolean estHorsJeu;

    /**
     * Constructeur
     */
    public Joueur(String pseudo)
    {
        this.pseudo = pseudo;
        this.id = ++idCourant;
    }

    /**
     * Permet de recuperer l'id
     * @return l'id
     */
    public int getId()
    {
        return id;
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
     * Permet savoir si le joueur est hors jeu
     * 
     * @return true s'il l'est, false sinon.
     */
    public boolean estHorsJeu()
    {
        return estHorsJeu;
    }

    /**
     * Permet mettre le joueur hors jeu suite à 
     * une déconnexion
     * 
     * @return true s'il l'est, false sinon.
     */
    public void mettreHorsJeu()
    {
        estHorsJeu = true;
    }
    
    /**
     * Permet de modifier le nombre de pieces d'or.
     * 
     * @param nbPiecesDOr le nouveau nombre de pieces d'or
     */
    public void setNbPiecesDOr(double nbPiecesDOr)
    {
        this.nbPiecesDOr = nbPiecesDOr;
        
        if(edj != null)
            edj.joueurMisAJour(this);
    }

    /**
     * Permet de récupérer le nombre de pieces d'or du joueur
     * 
     * @return le nombre de pieces d'or du joueur
     */
    public double getNbPiecesDOr()
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
        
        if(edj != null)
            edj.joueurMisAJour(this);
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
        
        // FIXME Décommenter et voir les conscéquences !
        //equipe.ajouterJoueur(this);
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

    /**
     * Permet de quitter l'emplacement
     */
    public void quitterEmplacementJoueur()
    {
        // fin de récursion de maj
        if(emplacement != null && emplacement.getJoueur() != this) 
            emplacement.retirerJoueur();
        
        emplacement = null;
    }

    /**
     * Permet de modifier l'id du joueur
     * 
     * @param id le nouvel id du joueur
     */
    public void setId(int id)
    {
        this.id = id; 
    }

    /**
     * Permet de modifier l'écouteur de joueur
     * 
     * @param edj l'écouteur de joueur
     */
    public void setEcouteurDeJoueur(EcouteurDeJoueur edj)
    {
        this.edj = edj;
    }

    /**
     * Permet de recuperer le revenu du joueur
     * 
     * @return le revenu actuel
     */
    public double getRevenu()
    {
        return revenu;
    }
    
    /**
     * Permet d'ajouter une somme au revenu
     * 
     * @param somme la somme a ajouter 
     */
    public void ajouterRevenu(double somme)
    {
        revenu += somme;
    }
    
    public void setRevenu(double revenu)
    {
        this.revenu = revenu;
    }
    
    /**
     * Permet de donner le revenu au joueur
     * 
     * @param temps le temps en ms ecoulees (pour calcul du revenu / sec) 
     */
    public synchronized void donnerRevenu(long temps)
    {
        setNbPiecesDOr(getNbPiecesDOr() + revenu * (temps / 1000.0));
    }

    
}
