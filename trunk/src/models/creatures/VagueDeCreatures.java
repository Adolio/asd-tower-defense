package models.creatures;

import i18n.Langue;

/**
 * Structure permettant de stoquer des informations et lancer une vague de
 * creatures.
 * 
 * Une vagues de creature contient un certain nombres de creatures du meme type.
 * 
 * @author Aurelien Da Campo
 * @version 2.1 | mai 2010
 * @since jdk1.6.0_16
 * @see Creature
 */
public class VagueDeCreatures
{
    /**
     * le nombre de creatures de la vague
     */
    private final int NB_CREATURES;

    /**
     * le type de la creature a envoyer
     */
    private final Creature CREATURE_A_ENVOYER;

    /**
     * la description de la vague
     */
    private final String DESCRIPTION;
    
    /**
     * Constructeur de la vague de creatures
     * 
     * @param nbCreatures le nombre de copie de la creature a envoyer
     * @param creatureAEnvoyer un objet de la creature a envoyer nbCreatures
     *            fois
     * @param tempsLancerEntreCreature temps d'attente entre chaque creature
     *            envoyee
     * @param positionDepartAleatoire la creature est-elle positionnee
     *            aleatoirement dans la zone de depart ou au centre ?
     * @param description description de la vague
     */
    public VagueDeCreatures(int nbCreatures, Creature creatureAEnvoyer,
            long tempsLancerEntreCreature, String description)
    {
        NB_CREATURES = nbCreatures;
        CREATURE_A_ENVOYER = creatureAEnvoyer;
        DESCRIPTION = description;
    }

    /**
     * Constructeur de la vague de creatures sans description
     * 
     * @param nbCreatures le nombre de copie de la creature a envoyer
     * @param creatureAEnvoyer un objet de la creature a envoyer nbCreatures
     *            fois
     * @param tempsLancerEntreCreature temps d'attente entre chaque creature
     *            envoyee
     * @param positionDepartAleatoire la creature est-elle positionnee
     *            aleatoirement dans la zone de depart ou au centre ?
     */
    public VagueDeCreatures(int nbCreatures, Creature creatureAEnvoyer,
            long tempsLancerEntreCreature)
    {
        this(nbCreatures, creatureAEnvoyer, tempsLancerEntreCreature, "");
    }

    /**
     * Permet de recuperer le nombre de creatures dans la vague
     * 
     * @return le nombre de creatures dans la vague
     */
    public int getNbCreatures()
    {
        return NB_CREATURES;
    }

    /**
     * Permet de recuperer une copie de la creature a envoyer.
     * 
     * @return une copie de la creature a envoyer
     */
    public Creature getNouvelleCreature()
    {
        return CREATURE_A_ENVOYER.copier();
    }

    /**
     * Permet de recuperer la description de la vague.
     * 
     * @return la description de la vague.
     */
    public String getDescription()
    {
        return DESCRIPTION;
    }

    /**
     * Permet de recuperer une synthese generee des proprietes de la vague
     * 
     * @return la description de la vague.
     */
    public String toString()
    {
        String type;
        
        if(CREATURE_A_ENVOYER.getType() == Creature.TYPE_AERIENNE)
            type = Langue.getTexte(Langue.ID_TXT_AIR).toUpperCase();
        else
            type = Langue.getTexte(Langue.ID_TXT_TERRE).toUpperCase();

        return NB_CREATURES + "x " +CREATURE_A_ENVOYER.getNom() + " (<b>"
                + type + "</b>) [ "
                + Langue.getTexte(Langue.ID_TXT_SANTE)+" : " + CREATURE_A_ENVOYER.getSanteMax() + ", "
                + Langue.getTexte(Langue.ID_TXT_GAIN)+" : " + CREATURE_A_ENVOYER.getNbPiecesDOr() + ", "
                + Langue.getTexte(Langue.ID_TXT_VITESSE)+" : " + CREATURE_A_ENVOYER.getVitesseNormale() + " ]";
    }

    public static final double VITESSE_CREATURE_LENTE = 30.0;
    public static final double VITESSE_CREATURE_RAPIDE = 60.0;
    public static final double VITESSE_CREATURE_NORMALE = 50.0;
    public static final double COEF_SANTE_CREATURE_RESISTANTE = 1.5;
    public static final double COEF_SANTE_CREATURE_RAPIDE = 0.8;
    public static final double COEF_SANTE_CREATURE_AERIENNE = 0.8;
    public static final double COEF_SANTE_PRE_BOSS = 4.0;
    public static final double COEF_SANTE_BOSS = 8.0;

    /**
     * Permet de generer une vague en fonction de son indice de vague courante
     * 
     * Cette methode permet d'eviter de gerer les vagues pour chaque terrain.
     * Mias rien n'empeche au developpeur de terrain de gerer lui-meme les
     * vagues qu'il veut envoye.
     * 
     * @return la vague generee
     */
    public static VagueDeCreatures genererVagueStandard(int indiceVagueCourante)
    {
        int noVague = indiceVagueCourante;
        int uniteVague = noVague % 10;

        final long SANTE_CREATURE_NORMALE = fSante(noVague);
        final long GAIN_VAGUE_COURANTE = fGainVague(SANTE_CREATURE_NORMALE);

        switch (uniteVague)
        {
        
        case 1: // 5 normales
            return new VagueDeCreatures(5, new Mouton(SANTE_CREATURE_NORMALE,
                    (int) (GAIN_VAGUE_COURANTE / 15), VITESSE_CREATURE_NORMALE),
                    getTempsLancement(VITESSE_CREATURE_NORMALE));

        case 2: // 10 normales
            return new VagueDeCreatures(10, new Araignee(SANTE_CREATURE_NORMALE,
                    (int) (GAIN_VAGUE_COURANTE / 10), VITESSE_CREATURE_NORMALE),
                    getTempsLancement(VITESSE_CREATURE_NORMALE));

        case 3: // 10 volantes
            return new VagueDeCreatures(
                    10,
                    new Aigle(
                            (int) (SANTE_CREATURE_NORMALE * COEF_SANTE_CREATURE_AERIENNE),
                            (int) (GAIN_VAGUE_COURANTE / 10), VITESSE_CREATURE_NORMALE),
                    getTempsLancement(VITESSE_CREATURE_NORMALE));

        case 4: // 10 resistantes
            return new VagueDeCreatures(
                    10,
                    new Rhinoceros(
                            (int) (SANTE_CREATURE_NORMALE * COEF_SANTE_CREATURE_RESISTANTE),
                            (int) (GAIN_VAGUE_COURANTE / 10), VITESSE_CREATURE_LENTE),
                            getTempsLancement(VITESSE_CREATURE_LENTE));

        case 5: // 10 rapides
            return new VagueDeCreatures(
                    10,
                    new Mouton(
                            (int) (SANTE_CREATURE_NORMALE * COEF_SANTE_CREATURE_RAPIDE),
                            (int) (GAIN_VAGUE_COURANTE / 10), VITESSE_CREATURE_RAPIDE),
                            getTempsLancement(VITESSE_CREATURE_RAPIDE));

        case 6: // 15 normales
            return new VagueDeCreatures(15, new Mouton(SANTE_CREATURE_NORMALE,
                    (int) (GAIN_VAGUE_COURANTE / 15), VITESSE_CREATURE_NORMALE),
                    getTempsLancement(VITESSE_CREATURE_NORMALE));

        case 7: // 15 resistantes
            return new VagueDeCreatures(
                    15,
                    new Elephant(
                            (int) (SANTE_CREATURE_NORMALE * COEF_SANTE_CREATURE_RESISTANTE),
                            (int) (GAIN_VAGUE_COURANTE / 15), VITESSE_CREATURE_LENTE),
                            getTempsLancement(VITESSE_CREATURE_LENTE));

        case 8: // 30 volantes
            return new VagueDeCreatures(
                    30,
                    new Pigeon(
                            (int) (SANTE_CREATURE_NORMALE * COEF_SANTE_CREATURE_AERIENNE),
                            (int) (GAIN_VAGUE_COURANTE / 30), VITESSE_CREATURE_NORMALE),
                            getTempsLancement(VITESSE_CREATURE_NORMALE));

        case 9: // 3 pre-boss
            return new VagueDeCreatures(3, new Araignee(
                    (int) (SANTE_CREATURE_NORMALE * COEF_SANTE_PRE_BOSS),
                    (int) (GAIN_VAGUE_COURANTE / 3), VITESSE_CREATURE_LENTE),
                    getTempsLancement(VITESSE_CREATURE_LENTE));

        default: // boss
            return new VagueDeCreatures(1, new GrandeAraignee(
                    (int) (SANTE_CREATURE_NORMALE * COEF_SANTE_BOSS),
                    (int) GAIN_VAGUE_COURANTE, VITESSE_CREATURE_LENTE),
                    getTempsLancement(VITESSE_CREATURE_LENTE));
        }
    }

    /**
     * Permet de recuperer le temps de lancement (standard) entre chaque créature
     * selon une vitesse donnée.
     * 
     * @param vitesse la vitesse normale de la créature
     * @return le temps (standard) entre chaque lancement
     */
    public static long getTempsLancement(double vitesse)
    {
        return (long) (1000.0 / (vitesse / 40.0));
    }
    
    
    /**
     * Permet de calculer la sante d'une creature de facon exponentielle pour
     * rendre le jeu de plus en plus dur.
     * 
     * @param noVague le numero de la vague
     * 
     * @return la valeur de la sante
     */
    public static long fSante(int noVague)
    {
        // 0.01 * noVague^4 + 0.25 * noVague + 70
        return (long) (0.01 * noVague * noVague * noVague * noVague + 0.25
                * noVague + 70);
    }

    /**
     * Permet de calculer les gains de pieces d'or d'une vague des creatures.
     * 
     * @param santeCreature la sante d'une creature de la vague
     * 
     * @return la valeur de gain de la vague entiere
     */
    private static long fGainVague(long santeCreature)
    {
        return (long) (0.08 * santeCreature) + 30;
    }
}
