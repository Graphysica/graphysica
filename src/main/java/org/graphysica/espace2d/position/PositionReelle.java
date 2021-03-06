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

import com.sun.istack.internal.NotNull;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Repere;
import static org.graphysica.espace2d.position.Type.REELLE;

/**
 * Une position réelle est exprimée en unitées réelles. Sa valeur réelle est
 * invariante, et sa valeur virtuelle varie selon le repère d'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public class PositionReelle extends Position {

    /**
     * Construit une position réelle dont la valeur est définie.
     *
     * @param position la valeur de la position.
     */
    public PositionReelle(@NotNull final Vector2D position) {
        super(position);
    }

    @Override
    public Vector2D reelle(@NotNull final Repere repere) {
        return position;
    }

    @Override
    public Vector2D virtuelle(@NotNull final Repere repere) {
        return repere.positionVirtuelle(position);
    }

    /**
     * Récupère la distance vectorielle réelle entre deux positions réelles,
     * orientée de cette position vers la position spécifiée.
     *
     * @param position la position d'arrivée de la distance vectorielle.
     * @return la distance vectorielle réelle de cette position à la position
     * spécifiée.
     */
    public Vector2D distance(@NotNull final PositionReelle position) {
        return position.getValeur().subtract(getValeur());
    }

    /**
     * Récupère une nouvelle position réelle résultant d'un déplacement
     * vectoriel réel spécifiée partant de cette position.
     *
     * @param deplacementReel le déplacement réel de cette position.
     * @return une nouvelle position réelle correspondant au déplacement de
     * cette position.
     */
    public PositionReelle deplacer(@NotNull final Vector2D deplacementReel) {
        return new PositionReelle(getValeur().add(deplacementReel));
    }

    @Override
    public Type getType() {
        return REELLE;
    }

}
