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

package models.tours;

import i18n.Langue;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import models.attaques.*;
import models.creatures.Creature;

/**
 * Classe de gestion d'une tour d'archer.
 * <p>
 * La tour d'archer est une tour qui est rapide, 
 * mais elle fait peu de degats. Elle attaque tous types de creatures.
 * 
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Tour
 */
public class TourArcher extends Tour
{
	private static final long serialVersionUID = 1L;
	public static final Color COULEUR;
	public static final Image IMAGE;
	public static final Image ICONE;
	public static final int NIVEAU_MAX = 5;
	public static final int PRIX_ACHAT = 10;
    private static final String DESCRIPTION = Langue.getTexte(Langue.ID_TXT_DESC_TOUR_ARCHER);

    static
    {
		COULEUR = new Color(128,0,0);
		IMAGE 	= Toolkit.getDefaultToolkit().getImage("img/tours/tourArcher.png");
		ICONE   = Toolkit.getDefaultToolkit().getImage("img/tours/icone_tourArcher.png");
	}
	
    /**
     * Constructeur de la tour.
     */
    public TourArcher()
	{
	    super(0,                // x
			  0, 				// y
			  20, 				// largeur
			  20, 				// hauteur
			  COULEUR,			// couleur de fond
			  Langue.getTexte(Langue.ID_TXT_NOM_TOUR_ARCHER),	        // nom
			  PRIX_ACHAT,       // prix achat
			  5,                // degats
			  50,               // rayon de portee
			  2,                // cadence de tir (tirs / sec.)
			  Tour.TYPE_TERRESTRE_ET_AIR, // type
			  IMAGE,            // image sur terrain
			  ICONE);		    // icone pour bouton
	
		description = DESCRIPTION;
	}
	
	public void ameliorer()
	{
	    if(peutEncoreEtreAmelioree())
        {
			// le prix total est ajouté du prix d'achat de la tour
			prixTotal 	+= prixAchat;
			
			// augmentation du prix du prochain niveau
			prixAchat 	*= 2;
			
			// augmentation des degats
            degats      = getDegatsLvlSuivant();
            
            // augmentation du rayon de portee
            rayonPortee = getRayonPorteeLvlSuivant();
            
            // raccourcissement du temps de preparation du tire
            setCadenceTir(getCadenceTirLvlSuivant());
		
			niveau++;
		}
	}

	public void tirer(Creature creature)
	{
	    jeu.ajouterAnimation(new Fleche(jeu,this,creature,degats));
	}

	public Tour getCopieOriginale()
	{
		return new TourArcher();
	}

	public boolean peutEncoreEtreAmelioree()
	{
		return niveau < NIVEAU_MAX;
	}
	
    @Override
    public double getCadenceTirLvlSuivant()
    {
        return getCadenceTir() * 1.2;
    }

    @Override
    public long getDegatsLvlSuivant()
    {
        return (long) (degats * 1.5);
    }

    @Override
    public double getRayonPorteeLvlSuivant()
    {
        return rayonPortee + 10;
    }
}
