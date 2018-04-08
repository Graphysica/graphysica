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

import com.sun.istack.internal.NotNull;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Repere;
import org.graphysica.espace2d.position.Type;

/**
 * Les objets implémentant cette interface peuvent être déplacés dans un repère
 * d'espace.
 *
 * @author Marc-Antoine Ouimet
 */
interface Deplaceable {

    /**
     * Déplace l'objet selon un déplacement de type spécifié dans un repère
     * d'espace défini.
     *
     * @param deplacement le vecteur de déplacement.
     * @param type le type du déplacement.
     * @param repere le repère d'espace.
     */
    public void deplacer(@NotNull final Vector2D deplacement,
            @NotNull final Type type, @NotNull final Repere repere);

}
