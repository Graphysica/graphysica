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
package org.graphysica.espace2d;

import com.sun.istack.internal.NotNull;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Une position est un emplacement dans un espace.
 *
 * @author Marc-Antoine Ouimet
 */
public abstract class Position {

    /**
     * L'énumération des types de position.
     */
    public static enum Type {
        REELLE, VIRTUELLE
    }

    /**
     * La valeur de cette position.
     */
    protected final Vector2D position;


    /**
     * Construit une position dont la valeur et le repère sont définis.
     *
     * @param position la valeur de la position.
     */
    protected Position(@NotNull final Vector2D position) {
        this.position = position;
    }

    /**
     * Récupère la position réelle de cette position.
     *
     * @param repere le repère dans lequel déterminer la position réelle.
     * @return la position réelle de cette position.
     */
    public abstract Vector2D reelle(@NotNull final Repere repere);

    /**
     * Récupère la position virtuelle de cette position.
     *
     * @param repere le repère dans lequel déterminer la position virtuelle.
     * @return la position virtuelle de cette position.
     */
    public abstract Vector2D virtuelle(@NotNull final Repere repere);

    /**
     * Construit une nouvelle position de valeur et de type spécifié dans un
     * repère d'espace.
     *
     * @param position la valeur de la position.
     * @param type le type de la position.
     * @return la position construite.
     */
    public static Position a(@NotNull final Vector2D position,
            @NotNull final Type type) {
        switch (type) {
            case REELLE:
                return new PositionReelle(position);
            case VIRTUELLE:
                return new PositionVirtuelle(position);
            default:
                throw new IllegalArgumentException(
                        "Type de position non supporté.");
        }
    }

    /**
     * Une position réelle est exprimée en unitées réelles.
     */
    private static class PositionReelle extends Position {

        /**
         * Construit une position réelle dont la valeur et le repère sont
         * définis.
         *
         * @param position la valeur de la position.
         * @param repere le repère de l'espace dans lequel se situe la position.
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

    }

    /**
     * Une position virtuelle est exprimée en unités d'affichage à l'écran.
     */
    private static class PositionVirtuelle extends Position {

        /**
         * Construit une position virtuelle dont la valeur et le repère sont
         * définis.
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


    }

}
