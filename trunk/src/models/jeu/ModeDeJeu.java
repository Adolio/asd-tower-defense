package models.jeu;

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
