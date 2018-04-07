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
package org.graphysica.espace2d.forme;

import com.sun.istack.internal.NotNull;
import org.graphysica.espace2d.position.Position;
import org.graphysica.espace2d.Repere;

/**
 * Les classes implémentant cette interface peuvent être sélectionnées. La
 * sélection d'une forme est évaluée à partir de la distance entre le curseur et
 * la forme. Parmi un ensemble de formes sélectionnées, la forme dont la
 * distance est minimale avec le curseur est celle qui est sélectionnée. La
 * sélection doit aussi prendre compte de l'ordre de rendu des formes.
 *
 * @author Marc-Antoine Ouimet
 */
interface Selectionnable {

    /**
     * Détermine si cette forme est sélectionnée.
     *
     * @param curseur la position du curseur.
     * @param repere le repère de l'espace.
     * @return {@code true} si la forme est sélectionnée.
     */
    boolean isSelectionne(@NotNull final Position curseur,
            @NotNull final Repere repere);

    /**
     * Calcule la distance entre la position virtuelle du curseur et cette forme
     * dans un repère défini.
     *
     * @param curseur la position du curseur.
     * @param repere le repère de l'espace.
     * @return la distance entre le curseur et cette forme.
     */
    double distance(@NotNull final Position curseur,
            @NotNull final Repere repere);

}
