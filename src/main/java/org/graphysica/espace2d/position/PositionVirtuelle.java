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
package org.graphysica.espace2d.position;

import com.google.gson.annotations.JsonAdapter;
import com.sun.istack.internal.NotNull;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Repere;
import static org.graphysica.espace2d.position.Type.VIRTUELLE;
import org.graphysica.gson.PositionJsonAdaptateur;

/**
 * Une position virtuelle est exprimée en unités d'affichage à l'écran. Sa
 * valeur virtuelle est invariante, et sa valeur réelle varie selon le repère
 * d'espace.
 *
 * @author Marc-Antoine Ouimet
 */
@JsonAdapter(PositionJsonAdaptateur.class)
public class PositionVirtuelle extends Position {

    /**
     * Construit une position virtuelle dont la valeur est définie.
     *
     * @param position la valeur de la position.
     */
    public PositionVirtuelle(@NotNull final Vector2D position) {
        super(position);
    }

    @Override
    public Vector2D reelle(@NotNull final Repere repere) {
        return repere.positionReelle(position);
    }

    @Override
    public Vector2D virtuelle(@NotNull final Repere repere) {
        return position;
    }

    @Override
    public Type getType() {
        return VIRTUELLE;
    }

}
