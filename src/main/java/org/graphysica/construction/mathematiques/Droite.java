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
import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import org.graphysica.espace2d.forme.Forme;
import org.graphysica.espace2d.position.PositionReelle;

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
        dependances.add(point1);
        dependances.add(point2);
        positionInterne1.bind(point1.positionInterneProperty());
        positionInterne2.bind(point2.positionInterneProperty());
    }

    /**
     * Construit une droite passant par un point et une position spécifiée.
     *
     * @param point1 le premier point compris dans cette droite.
     * @param position2 la deuxième position comprise dans cette droite.
     */
    public Droite(@NotNull final Point point1,
            @NotNull final ObjectProperty<PositionReelle> position2) {
        dependances.add(point1);
        positionInterne1.bind(point1.positionInterneProperty());
        positionInterne2.bind(position2);
    }
    
    @Override
    public Set<Forme> creerFormes() {
        final Set<Forme> formes = new HashSet<>();
        final org.graphysica.espace2d.forme.Droite forme
                = new org.graphysica.espace2d.forme.Droite(positionInterne1,
                        positionInterne2);
        formes.add(forme);
        ajouterForme(forme);
        return formes;
    }

}
