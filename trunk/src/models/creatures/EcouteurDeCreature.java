package models.creatures;

import models.joueurs.Joueur;

/**
 * Interface d'ecoute d'une creature.
 * 
 * Permet d'etre renseigne lorsqu'une creature subi des modifications.
 * 
 * @author Aurélien Da Campo
 * @version 1.1 | mai 2010
 * @since jdk1.6.0_16
 * @see Creature
 */
public interface EcouteurDeCreature
{
	/**
	 * Permet de savoir si une creature subie des degats.
	 * @param creature la creature qui a subie des degats
	 * @param tueur 
	 */
    void creatureTuee(Creature creature, Joueur tueur);
	
	/**
	 * Permet de savoir quand une creature a ete tuee.
	 * @param creature la creature qui a ete tuee
	 */
	void creatureBlessee(Creature creature);
	
	/**
	 * Permet de savoir quand une creature arrive à la fin du parcours.
	 * @param creature la creature qui est arrivee
	 */
	void creatureArriveeEnZoneArrivee(Creature creature);
}
