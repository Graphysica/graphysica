/*
 * Copyright (C) 2018 Graphysica
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.graphysica.vue.barreoutils;

import com.sun.istack.internal.NotNull;
import javafx.collections.ObservableList;
import org.graphysica.construction.Element;
import org.graphysica.construction.GestionnaireOutils;
import org.graphysica.espace2d.Espace;
import org.graphysica.espace2d.position.Position;

/**
 *
 * @author Victor Babin
 */
public abstract class Outil {

    /**
     * Si a une prochaine étape.
     */
    protected boolean aProchaineEtape;

    /**
     * Gestionnaire d'outils pour la construction.
     */
    protected final GestionnaireOutils gestionnaireOutils;
    
    public Outil(boolean aProchaineEtape, 
           GestionnaireOutils gestionnaireOutils) {
        this.aProchaineEtape = aProchaineEtape;
        this.gestionnaireOutils = gestionnaireOutils;
    }

    /**
     * Exécute les étapes.
     *
     * @param position la position initiale
     * @return l'élément à créer.
     */
    public void executer() {
        while (aProchaineEtape) {
            prochaineEtape();
        }
        derniereEtape();
    }

    /**
     * La prochaine étape à exécuter.
     *
     * @param position la position initiale
     */
    public abstract void prochaineEtape();

    /**
     * Dernière étape exécutée.
     *
     * @param position la position initiale
     * @return l'élément à créer
     */
    public abstract void derniereEtape();

    public boolean hasNextEtape() {
        return aProchaineEtape;
    }

}
