package models.outils;

// TODO comment
public class Astuces
{
    private static final String[] astuces = new String[] {
        "Le cercle blanc autour d'une tour indique sa portee.",
        "Attention a ne pas vous faire surprendre par les vagues volantes",
        "Ce jeu est libre et gratuit, vous pouvez meme acceder au code source"
    };
                                                       
    public static String getAstuceAleatoirement()
    {
        int i = Outils.tirerNombrePseudoAleatoire(0, astuces.length - 1);
        return astuces[i];   
    }
}
