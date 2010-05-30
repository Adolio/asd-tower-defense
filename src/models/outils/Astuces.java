package models.outils;

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
        "Le cercle blanc autour d'une tour indique la portee de la tour.",
        "Attention a ne pas vous faire surprendre par les vagues volantes",
        "Toutes les tours sont ameliorables moyennant quelques pieces d'or",
        "Les tours de glaces ralentissent les creatures, placez-les strategiquement",
        "Les etoiles vous permettent d'acceder a d'autres terrain de jeu",
        "A chaque fin de partie, vous pouvez sauver votre score",
        "La vente d'une tour vous fait recuperer 60 pourcent de son prix total",
        "Ce jeu est libre et gratuit, vous pouvez meme acceder au code source"
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
