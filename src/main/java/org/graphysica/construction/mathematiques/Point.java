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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.forme.Forme;
import org.graphysica.espace2d.forme.Taille;
import org.graphysica.espace2d.position.PositionReelle;

/**
 * Un point est un élément primitif de repérage réel dans l'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public class Point extends ObjetMathematique {

    /**
     * La position réelle du point.
     */
    private final ObjectProperty<PositionReelle> position
            = new SimpleObjectProperty<>(new PositionReelle(Vector2D.ZERO));

    /**
     * La taille du point dans sa représentation dans un espace.
     */
    private final Taille taille = Taille.de("point");

    /**
     * Construit un point mathématique à une position réelle spécifiée.
     *
     * @param position la position réelle du point.
     */
    public Point(@NotNull final PositionReelle position) {
        this.position.setValue(position);
        couleurProperty().setValue(Color.BLUE);
    }

    @Override
    protected Forme creerForme() {
        return new org.graphysica.espace2d.forme.Point(positionProperty(),
                couleurProperty(), tailleProperty());
    }

    public final ObjectProperty<PositionReelle> positionProperty() {
        return position;
    }

    public Taille tailleProperty() {
        return taille;
    }

}
