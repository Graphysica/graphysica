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
import org.graphysica.espace2d.forme.Forme;

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
     * @param point1 le premier point à la première extrémité du segmente de
     * droite.
     * @param point2 le deuxième point à la deuxième extrémité du segmente de
     * droite.
     */
    public SegmentDroite(@NotNull final Point point1,
            @NotNull final Point point2) {
        this.positionInterne1.bind(point1.positionProperty());
        this.positionInterne2.bind(point2.positionProperty());
    }

    @Override
    Forme creerForme() {
        return new org.graphysica.espace2d.forme.SegmentDroite(
                positionInterne1Property(), positionInterne2Property());
    }

}
