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
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.position.PositionReelle;

/**
 * Un point distancé est une position réelle dépendante distante d'une certaine
 * valeur réelle à partir d'un autre point dans l'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public final class PointDistance extends Point {

    /**
     * La position externe de ce point distancé qui correspond au point
     * d'ancrage de ce point.
     */
    private final ObjectProperty<PositionReelle> positionExterne
            = new SimpleObjectProperty<>();

    /**
     * La distance scalaire entre la position de ce point et la position
     * externe.
     */
    private final DoubleProperty distance = new SimpleDoubleProperty();

    /**
     * La distance vectorielle de la position externe vers la position interne
     * de ce point distancé.
     */
    private Vector2D distanceVectorielle;

    /**
     * L'événement d'actualisation de ce point distancé en fonction de la
     * distance scalaire.
     */
    private final InvalidationListener evenementActualisationDistance
            = (@NotNull final Observable observable) -> {
                distanceVectorielle = distanceVectorielle.normalize()
                        .scalarMultiply(distance.getValue());
                actualiserPositionInterne();
            };

    /**
     * L'événement d'actualisation de ce point distancé en fonction de sa
     * position externe.
     */
    private final InvalidationListener evenementActualisationPositionExterne
            = (@NotNull final Observable observable) -> {
                actualiserPositionInterne();
            };

    /**
     * Construit un point distancé à droite d'une position réelle selon une
     * distance scalaire spécifiée.
     *
     * @param point le point de repère de ce point distancé.
     * @param distance la distance entre ce point et le point spécifié.
     */
    public PointDistance(@NotNull final Point point, final double distance) {
        dependances.add(point);
        positionExterne.bind(point.positionInterneProperty());
        setDistance(distance);
        distanceVectorielle = new Vector2D(distance, 0);
        actualiserPositionInterne();
        positionExterne.addListener(evenementActualisationPositionExterne);
        this.distance.addListener(evenementActualisationDistance);
    }

    /**
     * Actualise la position interne de ce point distancé à partir de la
     * distance vectorielle et de la position externe du point.
     */
    private void actualiserPositionInterne() {
        positionInterne.setValue(getPositionExterne().deplacer(
                distanceVectorielle));
    }

    @Override
    public void deplacer(@NotNull final Vector2D deplacement) {
        distanceVectorielle = distanceVectorielle.add(deplacement).normalize()
                .scalarMultiply(distance.getValue());
        actualiserPositionInterne();
    }

    @Override
    public void deplacer(@NotNull final PositionReelle curseur) {
        final Vector2D deplacement = getPositionExterne().distance(curseur);
        distanceVectorielle = deplacement.normalize().scalarMultiply(
                distance.getValue());
        actualiserPositionInterne();
    }
    
    public DoubleProperty distanceProperty() {
        return distance;
    }

    private void setDistance(final double distance) {
        this.distance.setValue(distance);
    }

    private PositionReelle getPositionExterne() {
        return positionExterne.getValue();
    }

}
