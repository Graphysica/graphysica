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
package org.graphysica.construction.commande;

import com.sun.istack.internal.NotNull;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.construction.Construction;
import org.graphysica.espace2d.Point;

/**
 * Une commande de création de point permet de créer un point sur une
 * construction.
 *
 * @author Marc-Antoine
 */
public class CreerPoint extends CreerElement {

    /**
     * La position réelle où créer le point.
     */
    private final Vector2D position;

    /**
     * Construit une commande de création de point sur une construction à une
     * position réelle définie.
     *
     * @param construction la construction sur laquelle créer le point.
     * @param position la position réelle du point dans l'espace.
     */
    public CreerPoint(@NotNull final Construction construction,
            @NotNull final Vector2D position) {
        super(construction);
        this.position = position;
    }

    @Override
    public void executer() {
        element = new Point(position);
        super.executer();
    }

}
