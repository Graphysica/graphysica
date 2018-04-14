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
import org.graphysica.espace2d.forme.Taille;
import org.graphysica.espace2d.position.PositionReelle;

/**
 * Un point est un élément primitif de repérage réel dans l'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public abstract class Point extends ObjetMathematique {

    /**
     * La position interne réelle du point.
     */
    protected final ObjectProperty<PositionReelle> positionInterne
            = new SimpleObjectProperty<>();

    /**
     * La taille du point dans sa représentation dans un espace.
     */
    protected final Taille taille = Taille.de("point");
    
    {
        formes.add(new org.graphysica.espace2d.forme.Point(
                positionInterneProperty(), couleurProperty(),
                tailleProperty()));
    }

    public final ObjectProperty<PositionReelle> positionInterneProperty() {
        return positionInterne;
    }

    public final Taille tailleProperty() {
        return taille;
    }

}
