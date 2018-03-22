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
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

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

    @NotNull
    private Vector2D origineVirtuelle;
    @NotNull
    private Vector2D echelle;
    @NotNull
    private Repere repere;

    @Before
    public void initialiser() {
        origineVirtuelle = new Vector2D(500, 500);
        echelle = new Vector2D(50, 50);
        repere = new Repere(origineVirtuelle, echelle);
    }

    @Test
    public void testAbscisseVirtuelle() {
        assertEquals(500 - 2 * 50, repere.abscisseVirtuelle(-2), DELTA);
        assertEquals(500 - 50, repere.abscisseVirtuelle(-1), DELTA);
        assertEquals(500, repere.abscisseVirtuelle(0), DELTA);
        assertEquals(500 + 50, repere.abscisseVirtuelle(1), DELTA);
        assertEquals(500 + 2 * 50, repere.abscisseVirtuelle(2), DELTA);
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
            repere.abscisseVirtuelle(-2),
            repere.abscisseVirtuelle(-1),
            repere.abscisseVirtuelle(0),
            repere.abscisseVirtuelle(1),
            repere.abscisseVirtuelle(2)
        }, DELTA);
    }

    @Test
    public void testOrdonneeVirtuelle() {
        assertEquals(500 + 2 * 50, repere.ordonneeVirtuelle(-2), DELTA);
        assertEquals(500 + 50, repere.ordonneeVirtuelle(-1), DELTA);
        assertEquals(500, repere.ordonneeVirtuelle(0), DELTA);
        assertEquals(500 - 50, repere.ordonneeVirtuelle(1), DELTA);
        assertEquals(500 - 2 * 50, repere.ordonneeVirtuelle(2), DELTA);
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
            repere.ordonneeVirtuelle(-2),
            repere.ordonneeVirtuelle(-1),
            repere.ordonneeVirtuelle(0),
            repere.ordonneeVirtuelle(1),
            repere.ordonneeVirtuelle(2)
        }, DELTA);
    }

    @Test
    public void testPositionVirtuelle() {
        assertEquals(new Vector2D(500 - 2 * 50, 500 + 2 * 50),
                repere.positionVirtuelle(new Vector2D(-2, -2)));
        assertEquals(new Vector2D(500 - 2 * 50, 500 + 50),
                repere.positionVirtuelle(new Vector2D(-2, -1)));
        assertEquals(new Vector2D(500 - 50, 500 + 2 * 50),
                repere.positionVirtuelle(new Vector2D(-1, -2)));
        assertEquals(new Vector2D(500 - 50, 500 + 50),
                repere.positionVirtuelle(new Vector2D(-1, -1)));
        assertEquals(new Vector2D(500 - 50, 500),
                repere.positionVirtuelle(new Vector2D(-1, 0)));
        assertEquals(new Vector2D(500, 500 + 50),
                repere.positionVirtuelle(new Vector2D(0, -1)));
        assertEquals(new Vector2D(500, 500),
                repere.positionVirtuelle(new Vector2D(0, 0)));
        assertEquals(new Vector2D(500, 500 - 50),
                repere.positionVirtuelle(new Vector2D(0, 1)));
        assertEquals(new Vector2D(500 + 50, 500),
                repere.positionVirtuelle(new Vector2D(1, 0)));
        assertEquals(new Vector2D(500 + 50, 500 - 50),
                repere.positionVirtuelle(new Vector2D(1, 1)));
        assertEquals(new Vector2D(500 + 50, 500 - 2 * 50),
                repere.positionVirtuelle(new Vector2D(1, 2)));
        assertEquals(new Vector2D(500 + 2 * 50, 500 - 50),
                repere.positionVirtuelle(new Vector2D(2, 1)));
        assertEquals(new Vector2D(500 + 2 * 50, 500 - 2 * 50),
                repere.positionVirtuelle(new Vector2D(2, 2)));
    }
    
    @Test
    public void testAbscisseReelle() {
        assertEquals(-2, repere.abscisseReelle(500 - 2 * 50), DELTA);
        assertEquals(-1, repere.abscisseReelle(500 - 50), DELTA);
        assertEquals(0, repere.abscisseReelle(500), DELTA);
        assertEquals(1, repere.abscisseReelle(500 + 50), DELTA);
        assertEquals(2, repere.abscisseReelle(500 + 2 * 50), DELTA);
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
            repere.abscisseReelle(500 - 2 * 50),
            repere.abscisseReelle(500 - 50),
            repere.abscisseReelle(500),
            repere.abscisseReelle(500 + 50),
            repere.abscisseReelle(500 + 2 * 50)
        }, DELTA);
    }

    @Test
    public void testOrdonneeReelle() {
        assertEquals(-2, repere.ordonneeReelle(500 + 2 * 50), DELTA);
        assertEquals(-1, repere.ordonneeReelle(500 + 50), DELTA);
        assertEquals(0, repere.ordonneeReelle(500), DELTA);
        assertEquals(1, repere.ordonneeReelle(500 - 50), DELTA);
        assertEquals(2, repere.ordonneeReelle(500 - 2 * 50), DELTA);
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
            repere.ordonneeReelle(500 + 2 * 50),
            repere.ordonneeReelle(500 + 50),
            repere.ordonneeReelle(500),
            repere.ordonneeReelle(500 - 2 * 50),
            repere.ordonneeReelle(500 - 50)
        }, DELTA);
    }

    @Test
    public void testPositionsReelles() {
        assertEquals(new Vector2D(-2, -2), repere.positionReelle(
                new Vector2D(500 - 2 * 50, 500 + 2 * 50)));
        assertEquals(new Vector2D(-2, -1), repere.positionReelle(
                new Vector2D(500 - 2 * 50, 500 + 50)));
        assertEquals(new Vector2D(-1, -2), repere.positionReelle(
                new Vector2D(500 - 50, 500 + 2 * 50)));
        assertEquals(new Vector2D(-1, -1), repere.positionReelle(
                new Vector2D(500 - 50, 500 + 50)));
        assertEquals(new Vector2D(-1, 0), repere.positionReelle(
                new Vector2D(500 - 50, 500)));
        assertEquals(new Vector2D(0, -1), repere.positionReelle(
                new Vector2D(500, 500 + 50)));
        assertEquals(new Vector2D(0, 0), repere.positionReelle(
                new Vector2D(500, 500)));
        assertEquals(new Vector2D(0, 1), repere.positionReelle(
                new Vector2D(500, 500 - 50)));
        assertEquals(new Vector2D(1, 0), repere.positionReelle(
                new Vector2D(500 + 50, 500)));
        assertEquals(new Vector2D(1, 1), repere.positionReelle(
                new Vector2D(500 + 50, 500 - 50)));
        assertEquals(new Vector2D(1, 2), repere.positionReelle(
                new Vector2D(500 + 50, 500 - 2 * 50)));
        assertEquals(new Vector2D(2, 1), repere.positionReelle(
                new Vector2D(500 + 2 * 50, 500 - 50)));
        assertEquals(new Vector2D(2, 2), repere.positionReelle(
                new Vector2D(500 + 2 * 50, 500 - 2 * 50)));
    }
    
}
