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
import static org.graphysica.espace2d.position.Type.VIRTUELLE;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Marc-Antoine Ouimet
 */
public class SegmentDroiteTest extends FormeTest {

    /**
     * Un segment de droite horizontal reliant les points réels (0,0) et (1,0).
     */
    private static final SegmentDroite SEGMENT = new SegmentDroite(
            new SimpleObjectProperty<>(new PositionReelle(Vector2D.ZERO)),
            new SimpleObjectProperty<>(new PositionReelle(new Vector2D(1, 0))));

    @Override
    public void testDistance() {
        assertEquals(0, SEGMENT.distance(
                new PositionReelle(Vector2D.ZERO), REPERE), DELTA);
        assertEquals(0, SEGMENT.distance(
                new PositionReelle(new Vector2D(1, 0)), REPERE), DELTA);
        assertEquals(0, SEGMENT.distance(
                new PositionReelle(new Vector2D(0.5, 0)), REPERE), DELTA);
        assertEquals(1, SEGMENT.distance(
                new PositionReelle(Vector2D.ZERO).deplacer(
                        new Vector2D(-1, 0), VIRTUELLE, REPERE), REPERE),
                DELTA);
        assertEquals(1, SEGMENT.distance(
                new PositionReelle(new Vector2D(1, 0)).deplacer(
                        new Vector2D(1, 0), VIRTUELLE, REPERE), REPERE), DELTA);
        assertEquals(1, SEGMENT.distance(
                new PositionReelle(new Vector2D(0.5, 0)).deplacer(
                        new Vector2D(0, 1), VIRTUELLE, REPERE), REPERE), DELTA);
        assertEquals(1, SEGMENT.distance(
                new PositionReelle(new Vector2D(0.5, 0)).deplacer(
                        new Vector2D(0, -1), VIRTUELLE, REPERE), REPERE),
                DELTA);
        assertEquals(Math.sqrt(Math.pow(50, 2) + Math.pow(50, 2)),
                SEGMENT.distance(new PositionReelle(new Vector2D(-1, -1)),
                        REPERE), DELTA);
        assertEquals(Math.sqrt(Math.pow(50, 2) + Math.pow(50, 2)),
                SEGMENT.distance(new PositionReelle(new Vector2D(2, -1)),
                        REPERE), DELTA);
        assertEquals(50, SEGMENT.distance(
                new PositionReelle(new Vector2D(0.5, -1)), REPERE), DELTA);
    }

}
