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

import javafx.beans.property.SimpleObjectProperty;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.position.PositionReelle;
import org.graphysica.espace2d.position.PositionVirtuelle;
import static org.junit.Assert.assertEquals;

/**
 * Teste les représentations graphiques de points.
 *
 * @author Marc-Antoine Ouimet
 */
public class PointTest extends FormeTest {

    /**
     * Une point centré à l'origine réelle.
     */
    private static final Point ORIGINE_REELLE = new Point(
            new SimpleObjectProperty<>(new PositionReelle(Vector2D.ZERO)));

    @Override
    public void testDistance() {
        assertEquals(0, ORIGINE_REELLE.distance(
                new PositionReelle(Vector2D.ZERO), REPERE), DELTA);
        assertEquals(0, ORIGINE_REELLE.distance(
                new PositionVirtuelle(
                        ORIGINE_VIRTUELLE.add(new Vector2D(1, 1))), REPERE),
                DELTA);
        assertEquals(0, ORIGINE_REELLE.distance(
                new PositionVirtuelle(
                        ORIGINE_VIRTUELLE.add(new Vector2D(2, 2))), REPERE),
                DELTA);
        assertEquals(0, ORIGINE_REELLE.distance(
                new PositionVirtuelle(
                        ORIGINE_VIRTUELLE.add(new Vector2D(-1, -1))), REPERE),
                DELTA);
        assertEquals(0, ORIGINE_REELLE.distance(
                new PositionVirtuelle(
                        ORIGINE_VIRTUELLE.add(new Vector2D(-2, -2))), REPERE),
                DELTA);
        assertEquals(Math.sqrt(Math.pow(50, 2) + Math.pow(50, 2))
                - ORIGINE_REELLE.getTaille(),
                ORIGINE_REELLE.distance(new PositionReelle(
                        new Vector2D(1, 1)), REPERE), DELTA);
        assertEquals(Math.sqrt(Math.pow(50, 2) + Math.pow(50, 2))
                - ORIGINE_REELLE.getTaille(),
                ORIGINE_REELLE.distance(new PositionReelle(
                        new Vector2D(-1, 1)), REPERE), DELTA);
        assertEquals(Math.sqrt(Math.pow(50, 2) + Math.pow(50, 2))
                - ORIGINE_REELLE.getTaille(),
                ORIGINE_REELLE.distance(new PositionReelle(
                        new Vector2D(1, -1)), REPERE), DELTA);
        assertEquals(Math.sqrt(Math.pow(50, 2) + Math.pow(50, 2))
                - ORIGINE_REELLE.getTaille(),
                ORIGINE_REELLE.distance(new PositionReelle(
                        new Vector2D(-1, -1)), REPERE), DELTA);
    }

}
