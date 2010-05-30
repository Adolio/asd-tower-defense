package models.jeu;

/**
 * Classe stockant les constantes des modes de jeu.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 * @since jdk1.6.0_16
 */
public class ModeDeJeu
{
    public static final int MODE_SOLO = 0;
    public static final int MODE_VERSUS = 1;
    public static final int MODE_COOP = 2;
    
    public static String getNomMode(int mode)
    {
        switch(mode)
        {
            case 0 : return "Solo";
            case 1 : return "Versus";
            case 2 : return "Coop";
        }
        
        return "Inconnu";
    }
}
