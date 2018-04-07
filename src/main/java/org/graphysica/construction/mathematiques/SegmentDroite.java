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
import org.graphysica.espace2d.position.PositionReelle;

/**
 * Un segment de droite est un espace linéaire défini entre deux points.
 *
 * @author Marc-Antoine Ouimet
 */
public class SegmentDroite extends Ligne {

    /**
     * Construit un segment de droite aux extrémités définies.
     *
     * @param positionInterne1 la première position parmi le segment de droite
     * qui se trouve à sa première extrémité.
     * @param positionInterne2 la deuxième position parmi le segment de droite
     * qui se trouve à sa deuxième extrémité.
     */
    public SegmentDroite(@NotNull final PositionReelle positionInterne1,
            @NotNull final PositionReelle positionInterne2) {
        this.positionInterne1.setValue(positionInterne1);
        this.positionInterne2.setValue(positionInterne2);
    }

    @Override
    Forme creerForme() {
        return new org.graphysica.espace2d.forme.SegmentDroite(
                positionInterne1Property(), positionInterne2Property());
    }

}
