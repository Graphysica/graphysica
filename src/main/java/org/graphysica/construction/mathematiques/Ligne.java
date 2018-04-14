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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.forme.Forme;
import org.graphysica.espace2d.forme.Taille;
import org.graphysica.espace2d.position.PositionReelle;

/**
 * Un point est un élément primitif de qui relie des points dans l'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public abstract class Ligne extends ObjetMathematique {

    /**
     * L'épaisseur de la ligne dans sa représentation dans un espace.
     */
    private final Taille epaisseur = Taille.de("point");

    /**
     * La première position interne de la ligne.
     */
    protected final ObjectProperty<PositionReelle> positionInterne1
            = new SimpleObjectProperty<>(new PositionReelle(Vector2D.ZERO));

    /**
     * La deuxième position interne de la ligne.
     */
    protected final ObjectProperty<PositionReelle> positionInterne2
            = new SimpleObjectProperty<>(new PositionReelle(Vector2D.ZERO));
    
    public ObjectProperty<PositionReelle> positionInterne1Property() {
        return positionInterne1;
    }

    public ObjectProperty<PositionReelle> positionInterne2Property() {
        return positionInterne2;
    }

    public Taille epaisseurProperty() {
        return epaisseur;
    }

}
