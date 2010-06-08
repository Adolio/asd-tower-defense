package models.jeu;

import java.util.ArrayList;

import models.joueurs.Equipe;

public class ResultatJeu
{
    private Equipe equipeGagnante;
    private ArrayList<Equipe> equipes;

    /**
     * Contructeur
     * 
     * @param equipeGagnante
     */
    public ResultatJeu(Equipe equipeGagnante)
    {
        this.equipeGagnante = equipeGagnante;
    }

    public void setEquipeGagnante(Equipe equipeGagnante)
    {
        this.equipeGagnante = equipeGagnante;
    }

    public Equipe getEquipeGagnante()
    {
        return equipeGagnante;
    }

    public ArrayList<Equipe> getEquipes()
    {
        return equipes;
    }
}
