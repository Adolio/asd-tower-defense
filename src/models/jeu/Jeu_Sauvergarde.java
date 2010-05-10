package models.jeu;

import java.io.File;
import java.util.Vector;
import models.animations.Animation;
import models.creatures.Creature;
import models.tours.Tour;

public class Jeu_Sauvergarde
{
    private Vector<Creature> creatures;
    private Vector<Tour> tours;
    private Vector<Animation> animations;
    
    public Jeu_Sauvergarde(Jeu jeu)
    {
        creatures = jeu.getGestionnaireCreatures().getCreatures();
        tours = jeu.getGestionnaireTours().getTours();
        //animations = jeu.getGestionnaireAnimations().getAnimations(); 
    }
    
    public void sauver( File fichier)
    {
  
    }
}
