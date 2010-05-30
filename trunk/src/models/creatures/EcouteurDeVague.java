package models.creatures;

/**
 * Interface d'ecoute d'une vague de creatures.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | 31 decembre 2009
 * @since jdk1.6.0_16
 * @see VagueDeCreatures
 */
public interface EcouteurDeVague
{
    /**
     * Permet d'informer l'ecouteur que la vague a entierement ete 
     * lancee sur le terrain.
     * 
     * @param vagueDeCreatures la vague concernee
     */
    public void vagueEntierementLancee(VagueDeCreatures vagueDeCreatures);
}

