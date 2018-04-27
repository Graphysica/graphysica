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

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Repere;
import org.junit.Test;

/**
 * Teste les formes.
 * 
 * @author Marc-Antoine Ouimet
 */
public abstract class FormeTest {
    
    /**
     * L'incertitude sur les comparaison de valeurs <code>double</code>.
     */
    protected static final double DELTA = 1e-8;

    /**
     * L'origine virtuelle du repère de test.
     */
    protected static final Vector2D ORIGINE_VIRTUELLE = new Vector2D(500, 500);
    
    /**
     * L'échelle du repère de test.
     */
    protected static final Vector2D ECHELLE = new Vector2D(50, 50);
    
    /**
     * Le repère de test.
     */
    protected static final Repere REPERE = new Repere(
            ORIGINE_VIRTUELLE, ECHELLE);
    
    /**
     * Teste la distance entre cette forme et des positions du curseur.
     */
    @Test
    public abstract void testDistance();
    
}
