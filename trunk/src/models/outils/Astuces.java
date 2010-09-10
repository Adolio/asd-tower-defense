package models.outils;

import i18n.Langue;

/**
 * Classe de gestion des astuces
 * 
 * Les astuces font leur apparitions dans le formulaire de selection d'un terrain en
 * mode solo. Elle est là pour donner des informations diverses au joueur.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 * @since jdk1.6.0_16
 */
public class Astuces
{
    private static final String[] astuces = new String[] {
        Langue.getTexte(Langue.ID_TXT_ASTUCE_1),
        Langue.getTexte(Langue.ID_TXT_ASTUCE_2),
        Langue.getTexte(Langue.ID_TXT_ASTUCE_3),
        Langue.getTexte(Langue.ID_TXT_ASTUCE_4),
        Langue.getTexte(Langue.ID_TXT_ASTUCE_5),
        Langue.getTexte(Langue.ID_TXT_ASTUCE_6),
        Langue.getTexte(Langue.ID_TXT_ASTUCE_7),
        Langue.getTexte(Langue.ID_TXT_ASTUCE_8),
    };
     
    /**
     * Permet de recuperer une astuces aleatoirement
     * 
     * @return l'astuce
     */
    public static String getAstuceAleatoirement()
    {
        int i = Outils.tirerNombrePseudoAleatoire(0, astuces.length - 1);
        return astuces[i];   
    }
}
