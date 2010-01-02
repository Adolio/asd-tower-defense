package models.outils;

import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javazoom.jl.player.Player;

/**
 * Fichier : Son.java
 * <p>
 * Encodage : UTF-8
 * <p>
 * Cette classe interne privee permet de representer du son (flux).
 * <p>
 * Remarques : <br>
 * Cette classe est "threadee", elle permet de lire plusieurs fois la meme
 * musique en concurrence.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 17 dec. 2009
 * @since jdk1.6.0_16
 */
public class Son2 extends Thread
{
    // Le player de son.
    private Player player;
    // Le flux d'entree lie au fichier a jouer.
    private FileInputStream fluxEntreeFichier;
    // Le nombre de fois qu'on veut repeter le son.
    private int nombreRepetitions = 1;
    // Le fichier ou est stocke la musique a lire.
    private File fichier;
    // Pour savoir si le son actuel est à l'arret.
    private boolean arret = false;
    // Controle d'entree pour le volume.
    private static Control ctrlIn;
    
    private ArrayList<EcouteurDeSon> ecouteursDeSon = new ArrayList<EcouteurDeSon>();

    /**
     * Ce constructeur permet de creer un objet Son en fonction d'un fichier
     * donne ainsi que d'un nombre de repetitions donne.
     * 
     * @param fichier Le fichier a lire.
     * @param repetitions Le nombre de fois que la musique doit etre jouee. Si
     *            cette valeur vaut 0, la musique est repetee a l'infini.
     */
    public Son2(File fichier)
    {
        this.fichier = fichier;
    }

    /**
     * Cette methode permet de lire le son une fois.
     */
    public void lire()
    {
        start();
    }
    
    /**
     * Cette methode permet de lire la musique courante, en fonction du nombre
     * de repetitions donne. Si <tt>nombreRepetitions</tt> vaut 0, on repete la
     * musique $ l'infini, sinon on repete la musique <tt>nombreRepetitions</tt>
     * fois.
     * 
     * @param nombreRepetitions Le nombre de fois que la musique doit etre
     *            jouee. Si ce parametre vaut 0, la musique est jouee a l'infini
     *            (ne s'arrete jamais).
     */
    public void lire(int nombreRepetitions)
    {
        this.nombreRepetitions = nombreRepetitions == 0 ? Integer.MAX_VALUE
                : nombreRepetitions;
        start();
    }

    /**
     * Cette methode permet d'arreter toutes les occurrences de cette musique
     * qui sont en train d'etre lues. En bref, elle stoppe tous les threads en
     * train de lire cette musique.
     */
    public void arreter()
    {
        interrupt();
    }

    /**
     * Cette methode permet de determiner si toutes les occurrences de lecture
     * de la musique courante sont terminees ou non.
     * 
     * @return True si toutes les occurrences sont terminees, false sinon.
     */
    public boolean estTerminee()
    {
        return arret;
    }

    /**
     * Permet de lancer le thread qui lit le son.
     * 
     * @see Thread#run()
     */
    @Override
    public void run()
    {
        try
        {
            // On lit le nombre de fois voulu le son.
            for (int i = 0; !arret && i < nombreRepetitions; ++i)
            {
                // Creation du flux d'entree en fonction du fichier.
                fluxEntreeFichier = new FileInputStream(fichier);
                // Initialisation de l'objet player en fonction du flux.
                player = new Player(fluxEntreeFichier);
                // Lecture de la musique.
                player.play();
            }
            // On arrete le thread courant car le son est termine.
            interrupt();
        } catch (Exception erreur) // Erreur liee a la lecture du son.
        {
            erreur.printStackTrace();
        }
    }

    /**
     * Permet d'interrompre un thread.
     * 
     * @see Thread#interrupt()
     */
    public void interrupt()
    {
        // On passe en mode arret.
        arret = true;
        // On ferme le lecteur de son.
        player.close();

        // informe les ecouteurs que le son est termine
        for(EcouteurDeSon ecouteurDeSon : ecouteursDeSon)
            ecouteurDeSon.estTerminee(this);
    }

    /**
     * Permet d'ajouter un ecouteur de son
     * @param eds l'ecouteur de son
     */
    public void ajouterEcouteurDeMusique(EcouteurDeSon eds)
    {
        ecouteursDeSon.add(eds);
    }
    
    /**
     * Cette methode permet de savoir (en pourcentage) quel est le volume actuel
     * du systeme.
     * 
     * @return Le pourcentage du volume du systeme actuel.
     */
    public int getVolume()
    {
        return 100 * (int) (((FloatControl) ctrlIn).getValue() / ((FloatControl) ctrlIn)
                .getMaximum());
    }

    /**
     * Cette methode permet de regler le volume du systeme en fonction d'un
     * pourcentage donne.
     * 
     * @param volumePourcent Le pourcentage du volume qu'on veut appliquer.
     */
    public void setVolume(int volumePourcent)
    {
        // Pour les ports de sortie audio.
        Port lineOut;

        try
        {
            // Le systeme possede une sortie LINE_OUT.
            if (AudioSystem.isLineSupported(Port.Info.LINE_OUT))
            {
                lineOut = (Port) AudioSystem.getLine(Port.Info.LINE_OUT);
                // On ouvre ce port.
                lineOut.open();
            }
            // Le systeme possede une sortie HEADPHONE.
            else if (AudioSystem.isLineSupported(Port.Info.HEADPHONE))
            {
                lineOut = (Port) AudioSystem.getLine(Port.Info.HEADPHONE);
                // On ouvre ce port.
                lineOut.open();
            }
            // Le systeme possede une sortie SPEAKER.
            else if (AudioSystem.isLineSupported(Port.Info.SPEAKER))
            {
                lineOut = (Port) AudioSystem.getLine(Port.Info.SPEAKER);
                // On ouvre ce port.
                lineOut.open();
            }
            // Le systeme ne possede pas de sortie audio (triste!).
            else
            {
                System.out.println("Impossible d'avoir une sortie audio!");
                return;
            }

            // On recupere le controle d'entree du volume.
            ctrlIn = lineOut.getControl(FloatControl.Type.VOLUME);
            // On change le volume du systeme avec celui donne en parametre.
            ((FloatControl) ctrlIn).setValue(volumePourcent / 100.0f);
        } catch (Exception erreur) // Une erreur liee au volume est survenue.
        {
            erreur.printStackTrace();
        }
    }
}
