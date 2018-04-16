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
import org.graphysica.espace2d.Espace;
import org.graphysica.espace2d.position.Position;

/**
 *
 * @author Victor Babin <vicbab@Graphysica>
 */
public abstract class Outil {

//    /**
//     * Liste des espaces concernés.
//     */
//    private ObservableList<Espace> espaces;
    /**
     * Si a une prochaine étape.
     */
    protected boolean aProchaineEtape;

    /**
     * Exécute les étapes.
     *
     * @param position la position initiale
     * @return l'élément à créer.
     */
    public Element executer(Position position) {
        while (aProchaineEtape) {
            prochaineEtape(position);
        }
        return derniereEtape(position);
    }

    /**
     * La prochaine étape à exécuter.
     *
     * @param position la position initiale
     */
    public abstract void prochaineEtape(Position position);

    /**
     * Dernière étape exécutée.
     *
     * @param position la position initiale
     * @return l'élément à créer
     */
    public abstract Element derniereEtape(Position position);

    public boolean hasNextEtape() {
        return aProchaineEtape;
    }

}
