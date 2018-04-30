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

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Teste les calculs de coordonnées de repère d'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public class RepereTest {

    /**
     * L'incertitude sur les comparaison de valeurs <code>double</code>.
     */
    private static final double DELTA = 1e-8;

    /**
     * L'origine virtuelle du repère de test.
     */
    private static final Vector2D ORIGINE_VIRTUELLE = new Vector2D(500, 500);
    
    /**
     * L'échelle du repère de test.
     */
    private static final Vector2D ECHELLE = new Vector2D(50, 50);
    
    /**
     * Le repère de test.
     */
    private static final Repere REPERE = new Repere(ORIGINE_VIRTUELLE, ECHELLE);

    @Test
    public void testAbscisseVirtuelle() {
        assertEquals(500 - 2 * 50, REPERE.abscisseVirtuelle(-2), DELTA);
        assertEquals(500 - 50, REPERE.abscisseVirtuelle(-1), DELTA);
        assertEquals(500, REPERE.abscisseVirtuelle(0), DELTA);
        assertEquals(500 + 50, REPERE.abscisseVirtuelle(1), DELTA);
        assertEquals(500 + 2 * 50, REPERE.abscisseVirtuelle(2), DELTA);
    }

    @Test
    public void testAbscissesVirtuelles() {
        assertArrayEquals(new double[]{
            500 - 2 * 50,
            500 - 50,
            500,
            500 + 50,
            500 + 2 * 50
        }, new double[]{
            REPERE.abscisseVirtuelle(-2),
            REPERE.abscisseVirtuelle(-1),
            REPERE.abscisseVirtuelle(0),
            REPERE.abscisseVirtuelle(1),
            REPERE.abscisseVirtuelle(2)
        }, DELTA);
    }

    @Test
    public void testOrdonneeVirtuelle() {
        assertEquals(500 + 2 * 50, REPERE.ordonneeVirtuelle(-2), DELTA);
        assertEquals(500 + 50, REPERE.ordonneeVirtuelle(-1), DELTA);
        assertEquals(500, REPERE.ordonneeVirtuelle(0), DELTA);
        assertEquals(500 - 50, REPERE.ordonneeVirtuelle(1), DELTA);
        assertEquals(500 - 2 * 50, REPERE.ordonneeVirtuelle(2), DELTA);
    }

    @Test
    public void testOrdonneesVirtuelles() {
        assertArrayEquals(new double[]{
            500 + 2 * 50,
            500 + 50,
            500,
            500 - 50,
            500 - 2 * 50
        }, new double[]{
            REPERE.ordonneeVirtuelle(-2),
            REPERE.ordonneeVirtuelle(-1),
            REPERE.ordonneeVirtuelle(0),
            REPERE.ordonneeVirtuelle(1),
            REPERE.ordonneeVirtuelle(2)
        }, DELTA);
    }

    @Test
    public void testPositionVirtuelle() {
        assertEquals(new Vector2D(500 - 2 * 50, 500 + 2 * 50),
                REPERE.positionVirtuelle(new Vector2D(-2, -2)));
        assertEquals(new Vector2D(500 - 2 * 50, 500 + 50),
                REPERE.positionVirtuelle(new Vector2D(-2, -1)));
        assertEquals(new Vector2D(500 - 50, 500 + 2 * 50),
                REPERE.positionVirtuelle(new Vector2D(-1, -2)));
        assertEquals(new Vector2D(500 - 50, 500 + 50),
                REPERE.positionVirtuelle(new Vector2D(-1, -1)));
        assertEquals(new Vector2D(500 - 50, 500),
                REPERE.positionVirtuelle(new Vector2D(-1, 0)));
        assertEquals(new Vector2D(500, 500 + 50),
                REPERE.positionVirtuelle(new Vector2D(0, -1)));
        assertEquals(new Vector2D(500, 500),
                REPERE.positionVirtuelle(new Vector2D(0, 0)));
        assertEquals(new Vector2D(500, 500 - 50),
                REPERE.positionVirtuelle(new Vector2D(0, 1)));
        assertEquals(new Vector2D(500 + 50, 500),
                REPERE.positionVirtuelle(new Vector2D(1, 0)));
        assertEquals(new Vector2D(500 + 50, 500 - 50),
                REPERE.positionVirtuelle(new Vector2D(1, 1)));
        assertEquals(new Vector2D(500 + 50, 500 - 2 * 50),
                REPERE.positionVirtuelle(new Vector2D(1, 2)));
        assertEquals(new Vector2D(500 + 2 * 50, 500 - 50),
                REPERE.positionVirtuelle(new Vector2D(2, 1)));
        assertEquals(new Vector2D(500 + 2 * 50, 500 - 2 * 50),
                REPERE.positionVirtuelle(new Vector2D(2, 2)));
    }
    
    @Test
    public void testAbscisseReelle() {
        assertEquals(-2, REPERE.abscisseReelle(500 - 2 * 50), DELTA);
        assertEquals(-1, REPERE.abscisseReelle(500 - 50), DELTA);
        assertEquals(0, REPERE.abscisseReelle(500), DELTA);
        assertEquals(1, REPERE.abscisseReelle(500 + 50), DELTA);
        assertEquals(2, REPERE.abscisseReelle(500 + 2 * 50), DELTA);
    }
    
    @Test
    public void testAbscissesReelles() {
        assertArrayEquals(new double[]{
            -2,
            -1,
            0,
            1,
            2
        }, new double[]{
            REPERE.abscisseReelle(500 - 2 * 50),
            REPERE.abscisseReelle(500 - 50),
            REPERE.abscisseReelle(500),
            REPERE.abscisseReelle(500 + 50),
            REPERE.abscisseReelle(500 + 2 * 50)
        }, DELTA);
    }

    @Test
    public void testOrdonneeReelle() {
        assertEquals(-2, REPERE.ordonneeReelle(500 + 2 * 50), DELTA);
        assertEquals(-1, REPERE.ordonneeReelle(500 + 50), DELTA);
        assertEquals(0, REPERE.ordonneeReelle(500), DELTA);
        assertEquals(1, REPERE.ordonneeReelle(500 - 50), DELTA);
        assertEquals(2, REPERE.ordonneeReelle(500 - 2 * 50), DELTA);
    }

    @Test
    public void testOrdonneesReelles() {
        assertArrayEquals(new double[]{
            -2,
            -1,
            0,
            1,
            2
        }, new double[]{
            REPERE.ordonneeReelle(500 + 2 * 50),
            REPERE.ordonneeReelle(500 + 50),
            REPERE.ordonneeReelle(500),
            REPERE.ordonneeReelle(500 - 50),
            REPERE.ordonneeReelle(500 - 2 * 50)
        }, DELTA);
    }

    @Test
    public void testPositionsReelles() {
        assertEquals(new Vector2D(-2, -2), REPERE.positionReelle(
                new Vector2D(500 - 2 * 50, 500 + 2 * 50)));
        assertEquals(new Vector2D(-2, -1), REPERE.positionReelle(
                new Vector2D(500 - 2 * 50, 500 + 50)));
        assertEquals(new Vector2D(-1, -2), REPERE.positionReelle(
                new Vector2D(500 - 50, 500 + 2 * 50)));
        assertEquals(new Vector2D(-1, -1), REPERE.positionReelle(
                new Vector2D(500 - 50, 500 + 50)));
        assertEquals(new Vector2D(-1, 0), REPERE.positionReelle(
                new Vector2D(500 - 50, 500)));
        assertEquals(new Vector2D(0, -1), REPERE.positionReelle(
                new Vector2D(500, 500 + 50)));
        assertEquals(new Vector2D(0, 0), REPERE.positionReelle(
                new Vector2D(500, 500)));
        assertEquals(new Vector2D(0, 1), REPERE.positionReelle(
                new Vector2D(500, 500 - 50)));
        assertEquals(new Vector2D(1, 0), REPERE.positionReelle(
                new Vector2D(500 + 50, 500)));
        assertEquals(new Vector2D(1, 1), REPERE.positionReelle(
                new Vector2D(500 + 50, 500 - 50)));
        assertEquals(new Vector2D(1, 2), REPERE.positionReelle(
                new Vector2D(500 + 50, 500 - 2 * 50)));
        assertEquals(new Vector2D(2, 1), REPERE.positionReelle(
                new Vector2D(500 + 2 * 50, 500 - 50)));
        assertEquals(new Vector2D(2, 2), REPERE.positionReelle(
                new Vector2D(500 + 2 * 50, 500 - 2 * 50)));
    }
    
}
