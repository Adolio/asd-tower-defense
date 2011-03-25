/*
  Copyright (C) 2010 Aurelien Da Campo

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/

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
