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
 * Un segment de droite est un espace linéaire défini entre deux points.
 *
 * @author Marc-Antoine Ouimet
 */
public class SegmentDroite extends Ligne {

    /**
     * Construit un segment de droite lié à des extrémités définies par des
     * points.
     *
     * @param point1 le premier point à la première extrémité du segment de
     * droite.
     * @param point2 le deuxième point à la deuxième extrémité du segment de
     * droite.
     */
    public SegmentDroite(@NotNull final Point point1,
            @NotNull final Point point2) {
        this.positionInterne1.bind(point1.positionInterneProperty());
        this.positionInterne2.bind(point2.positionInterneProperty());
    }

    {
        formes.add(new org.graphysica.espace2d.forme.SegmentDroite(
                positionInterne1Property(), positionInterne2Property()));
    }

}
