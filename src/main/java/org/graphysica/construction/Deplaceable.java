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
package org.graphysica.construction;

import com.sun.istack.internal.NotNull;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Les objets déplaceables peuvent être déplacés selon un déplacement réel.
 * Cependant, des objets liés (définis par intersection, par exemple) ne peuvent
 * pas être expréssément être déplacés.
 *
 * @author Marc-Antoine Ouimet
 */
interface Deplaceable {

    /**
     * Détermine si l'objet est lié. S'il est lié, il ne peut pas être déplacé
     * expréssément.
     *
     * @return {@code true} si l'objet est lié.
     */
    boolean isLie();
    
    /**
     * Déplace l'objet selon le déplacement réel spécifié.
     *
     * @param deplacement le déplacement réel à effectuer.
     */
    void deplacer(@NotNull final Vector2D deplacement);

}
