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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Classe de test d'une toile.
 *
 * @author Marc-Antoine Ouimet
 */
public class ToileTest {

    @Test
    public void testPositionVirtuelle() throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        final Toile toile = new Toile(100, 100);
        final Method positionVirtuelle = toile.getClass().getMethod(
                "positionVirtuelle", Vector2D.class);
        toile.setEchelle(new Vector2D(50, 50)); //(50, 50) px/m
        toile.setOrigine(new Vector2D(50, 50)); //(50, 50) px
        //Couples de positions (réelle, virtuelle)
        final Vector2D[][] positions = {
            {new Vector2D(0, 0), new Vector2D(50, 50)},
            {new Vector2D(1, 1), new Vector2D(50 + 50, 50 - 50)},
            {new Vector2D(-1, -1), new Vector2D(50 - 50, 50 - (- 50))},
            {new Vector2D(-1, 1), new Vector2D(50 - 50, 50 - 50)},
            {new Vector2D(1, -1), new Vector2D(50 + 50, 50 - (- 50))}
        };
        for (final Vector2D[] couple : positions) {
            assertEquals(couple[1], positionVirtuelle.invoke(toile, couple[0]));
        }
    }

    @Test
    public void testPositionReelle() throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        final Toile toile = new Toile(100, 100);
        final Method positionReelle = toile.getClass().getMethod(
                "positionReelle", Vector2D.class);
        toile.setEchelle(new Vector2D(50, 50)); //(50, 50) px/m
        toile.setOrigine(new Vector2D(50, 50)); //(50, 50) px
        //Couples de positions (réelle, virtuelle)
        final Vector2D[][] positions = {
            {new Vector2D(0, 0), new Vector2D(50, 50)},
            {new Vector2D(1, 1), new Vector2D(50 + 50, 50 - 50)},
            {new Vector2D(-1, -1), new Vector2D(50 - 50, 50 - (- 50))},
            {new Vector2D(-1, 1), new Vector2D(50 - 50, 50 - 50)},
            {new Vector2D(1, -1), new Vector2D(50 + 50, 50 - (- 50))}
        };
        for (final Vector2D[] couple : positions) {
            assertEquals(couple[0], positionReelle.invoke(toile, couple[1]));
        }
    }

}
