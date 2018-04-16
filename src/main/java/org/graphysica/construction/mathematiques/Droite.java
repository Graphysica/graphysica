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
package org.graphysica.construction.mathematiques;

import com.sun.istack.internal.NotNull;

/**
 * Une droite est un espace linéaire qui bissecte un espace 2D. Une droite peut
 * être définie à partir de deux points réels de l'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public class Droite extends Ligne {

    /**
     * Construit une droite passant par deux points définis.
     *
     * @param point1 le premier point compris dans cette droite.
     * @param point2 le deuxième point compris dans cette droite.
     */
    public Droite(@NotNull final Point point1,
            @NotNull final Point point2) {
        positionInterne1.bind(point1.positionInterneProperty());
        positionInterne2.bind(point2.positionInterneProperty());
    }

    {
        formes.add(new org.graphysica.espace2d.forme.Droite(
                positionInterne1, positionInterne2));
    }

}
