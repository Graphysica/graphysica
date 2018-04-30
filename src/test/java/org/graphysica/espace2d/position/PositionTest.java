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
import static org.graphysica.espace2d.position.Type.VIRTUELLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Teste la sérialisation et le déplacement de positions.
 *
 * @author Marc-Antoine Ouimet
 */
public class PositionTest {

    /**
     * L'incertitude sur les comparaison de valeurs <code>double</code>.
     */
    private static final double DELTA = 1e-8;

    /**
     * Le repère d'espace de déplacement des {@code POSITIONS_DEPLACEES}.
     */
    private static final Repere REPERE = new Repere(new Vector2D(250, 250),
            new Vector2D(50, 50));

    /**
     * Un ensemble de positions déplacées.
     */
    private static final PositionDeplacee[] POSITIONS_DEPLACEES = {
        // Déplacements réels
        new PositionDeplacee(new PositionReelle(Vector2D.ZERO),
        new PositionReelle(new Vector2D(1, 1)), new Vector2D(1, 1), REELLE),
        new PositionDeplacee(new PositionReelle(Vector2D.ZERO),
        new PositionReelle(new Vector2D(-1, -1)), new Vector2D(-1, -1), REELLE),
        new PositionDeplacee(new PositionReelle(Vector2D.ZERO),
        new PositionReelle(new Vector2D(-1, 1)), new Vector2D(-1, 1), REELLE),
        new PositionDeplacee(new PositionReelle(Vector2D.ZERO),
        new PositionReelle(new Vector2D(1, -1)), new Vector2D(1, -1), REELLE),
        new PositionDeplacee(new PositionReelle(new Vector2D(1, 1)),
        new PositionReelle(new Vector2D(2, 2)), new Vector2D(1, 1), REELLE),
        new PositionDeplacee(new PositionReelle(new Vector2D(1, 1)),
        new PositionReelle(Vector2D.ZERO), new Vector2D(-1, -1), REELLE),
        new PositionDeplacee(new PositionReelle(new Vector2D(1, 1)),
        new PositionReelle(new Vector2D(0, 2)), new Vector2D(-1, 1), REELLE),
        new PositionDeplacee(new PositionReelle(new Vector2D(1, 1)),
        new PositionReelle(new Vector2D(2, 0)), new Vector2D(1, -1), REELLE),
        // Déplacements virtuels
        new PositionDeplacee(new PositionReelle(Vector2D.ZERO),
        new PositionReelle(new Vector2D(1, -1)), new Vector2D(50, 50),
        VIRTUELLE),
        new PositionDeplacee(new PositionReelle(Vector2D.ZERO),
        new PositionReelle(new Vector2D(-1, 1)), new Vector2D(-50, -50),
        VIRTUELLE),
        new PositionDeplacee(new PositionReelle(Vector2D.ZERO),
        new PositionReelle(new Vector2D(-1, -1)), new Vector2D(-50, 50),
        VIRTUELLE),
        new PositionDeplacee(new PositionReelle(Vector2D.ZERO),
        new PositionReelle(new Vector2D(1, 1)), new Vector2D(50, -50),
        VIRTUELLE),
        new PositionDeplacee(new PositionReelle(new Vector2D(1, 1)),
        new PositionReelle(new Vector2D(2, 0)), new Vector2D(50, 50),
        VIRTUELLE),
        new PositionDeplacee(new PositionReelle(new Vector2D(1, 1)),
        new PositionReelle(new Vector2D(0, 2)), new Vector2D(-50, -50),
        VIRTUELLE),
        new PositionDeplacee(new PositionReelle(new Vector2D(1, 1)),
        new PositionReelle(Vector2D.ZERO), new Vector2D(-50, 50),
        VIRTUELLE),
        new PositionDeplacee(new PositionReelle(new Vector2D(1, 1)),
        new PositionReelle(new Vector2D(2, 2)), new Vector2D(50, -50),
        VIRTUELLE)
    };

    /**
     * Teste les valeurs d'emplacement de positions déplacées par un vecteur
     * réel ou virtuel.
     */
    @Test
    public void testDeplacements() {
        for (final PositionDeplacee position : POSITIONS_DEPLACEES) {
            assertEquals(position.finale.reelle(REPERE), position.initiale
                    .deplacer(position.deplacement, position.type, REPERE)
                    .reelle(REPERE));
        }
        for (final PositionDeplacee position : POSITIONS_DEPLACEES) {
            assertEquals(position.finale.virtuelle(REPERE), position.initiale
                    .deplacer(position.deplacement, position.type, REPERE)
                    .virtuelle(REPERE));
        }
    }

    /**
     * Teste les distances entre des positions déplacées.
     */
    @Test
    public void testDistances() {
        for (final PositionDeplacee position : POSITIONS_DEPLACEES) {
            assertEquals(position.deplacement.getNorm(),
                    position.initiale.distance(position.finale, position.type,
                            REPERE), DELTA);
        }
    }

    /**
     * Teste les distances vectorielles entre des positions déplacées.
     */
    @Test
    public void testDistancesVectorielles() {
        for (final PositionDeplacee position : POSITIONS_DEPLACEES) {
            assertEquals(position.deplacement,
                    position.initiale.distanceVectorielle(
                            position.finale, position.type, REPERE));
        }
    }

    /**
     * Teste la méthode de comparaison d'équivalence entre deux positions.
     */
    @Test
    public void testEquals() {
        final Position positionReelle1
                = new PositionReelle(Vector2D.ZERO);
        final Position positionReelle2
                = new PositionReelle(Vector2D.ZERO);
        final Position positionVirtuelle1
                = new PositionVirtuelle(Vector2D.ZERO);
        final Position positionVirtuelle2
                = new PositionVirtuelle(Vector2D.ZERO);
        assertTrue(!positionReelle1.equals(null));
        assertTrue(!positionReelle1.equals("Fausse position"));
        assertTrue(positionReelle1.equals(positionReelle1));
        assertTrue(positionReelle1.equals(positionReelle2));
        assertTrue(positionReelle2.equals(positionReelle1));
        assertTrue(positionVirtuelle1.equals(positionVirtuelle1));
        assertTrue(positionVirtuelle1.equals(positionVirtuelle2));
        assertTrue(positionVirtuelle2.equals(positionVirtuelle1));
        assertTrue(!positionReelle1.equals(positionVirtuelle1));
        assertTrue(!positionVirtuelle1.equals(positionReelle1));
    }

    /**
     * Un cas de positions unies par un déplacement de type spécifié.
     */
    private static class PositionDeplacee {

        /**
         * La position initiale du cas.
         */
        final Position initiale;

        /**
         * La position final du cas.
         */
        final Position finale;

        /**
         * La valeur du déplacement entre les positions initiale et finale du
         * cas.
         */
        final Vector2D deplacement;

        /**
         * Le type de déplacement du cas.
         */
        final Type type;

        public PositionDeplacee(@NotNull final Position initiale,
                @NotNull final Position finale,
                @NotNull final Vector2D deplacement, @NotNull final Type type) {
            this.initiale = initiale;
            this.finale = finale;
            this.deplacement = deplacement;
            this.type = type;
        }

    }

}
