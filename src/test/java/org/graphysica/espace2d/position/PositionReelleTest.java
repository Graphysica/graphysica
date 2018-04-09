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
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Teste les fonctions propres aux positiones réelles.
 *
 * @author Marc-Antoine Ouimet
 */
public class PositionReelleTest {

    /**
     * L'incertitude sur les comparaison de valeurs <code>double</code>.
     */
    private static final double DELTA = 1e-8;

    /**
     * L'ensemble des positions distancées de test.
     */
    private static final PositionDistancee[] POSITIONS_DISTANCEES = {
        new PositionDistancee(new PositionReelle(new Vector2D(1, 1)),
        new PositionReelle(new Vector2D(1, 2)), new Vector2D(0, 1)),
        new PositionDistancee(new PositionReelle(new Vector2D(1, 2)),
        new PositionReelle(new Vector2D(1, 3)), new Vector2D(0, 1)),
        new PositionDistancee(new PositionReelle(new Vector2D(-1, 2)),
        new PositionReelle(new Vector2D(1, 3)), new Vector2D(2, 1)),
        new PositionDistancee(new PositionReelle(new Vector2D(1, 2)),
        new PositionReelle(new Vector2D(-1, 3)), new Vector2D(-2, 1)),
        new PositionDistancee(new PositionReelle(new Vector2D(1, 4)),
        new PositionReelle(new Vector2D(-1, 3)), new Vector2D(-2, -1))
    };

    /**
     * Teste la fonction de distance entre deux positions réelles.
     */
    @Test
    public void testDistance() {
        for (final PositionDistancee positionDistancee : POSITIONS_DISTANCEES) {
            assertEquals(positionDistancee.distance,
                    positionDistancee.positionInitiale.distance(
                            positionDistancee.positionFinale));
        }
    }

    /**
     * Teste que la distance entre une position réelle et elle-même est nulle.
     */
    @Test
    public void testDistanceAvecElleMeme() {
        final PositionReelle position = new PositionReelle(new Vector2D(1, 1));
        assertEquals(Vector2D.ZERO, position.distance(position));
    }

    /**
     * Teste la fonction de déplacement d'une position vers une autre position.
     */
    @Test
    public void testDeplacer() {
        for (final PositionDistancee positionDistancee : POSITIONS_DISTANCEES) {
            assertEquals(positionDistancee.positionFinale,
                    positionDistancee.positionInitiale.deplacer(
                            positionDistancee.distance));
        }
    }

    /**
     * Une position distancée décrit deux positions réelles séparées par une
     * distance vectorielle.
     */
    private static class PositionDistancee {

        /**
         * La position initiale du déplacement.
         */
        private final PositionReelle positionInitiale;

        /**
         * La position finale du déplacement.
         */
        private final PositionReelle positionFinale;

        /**
         * La distance de la première position vers la deuxième position.
         */
        private final Vector2D distance;

        /**
         * Construit un cas de test de position distancée.
         *
         * @param position1 la position initiale du déplacement.
         * @param position2 la position finale du déplacement.
         * @param distance la distance entre la première et la deuxième
         * positioné
         */
        public PositionDistancee(@NotNull final PositionReelle position1,
                @NotNull final PositionReelle position2,
                @NotNull final Vector2D distance) {
            this.positionInitiale = position1;
            this.positionFinale = position2;
            this.distance = distance;
        }

    }

}
