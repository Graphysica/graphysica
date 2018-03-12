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
package org.graphysica.espace2d.forme;

import com.sun.istack.internal.NotNull;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Toile;

/**
 * Un segment de droite relie deux points distincts dans l'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public class SegmentDroite extends Ligne {

    /**
     * La position réelle du premier point dans la droite.
     */
    protected final ObjectProperty<Vector2D> point1
            = new SimpleObjectProperty<>();

    /**
     * La position réelle du deuxième point dans la droite.
     */
    protected final ObjectProperty<Vector2D> point2
            = new SimpleObjectProperty<>();
    
    /**
     * Construit un segment de droite lié à la position de deux points définis.
     *
     * @param point1 le premier point en coordonnées réelles.
     * @param point2 le deuxième point en coordonnées réelles.
     */
    public SegmentDroite(@NotNull final Point point1,
            @NotNull final Point point2) {
        super();
        this.point1.setValue(point1.getPosition());
        this.point1.bind(point1.positionProperty());
        this.point2.setValue(point2.getPosition());
        this.point2.bind(point2.positionProperty());
    }
    
    {
        proprietesActualisation.add(point1);
        proprietesActualisation.add(point2);
        proprietesActualisation.add(epaisseur);
    }

    @Override
    public void dessiner(@NotNull final Toile toile) {
        if (!isIndefinie()) {
            origineTrace = toile.positionVirtuelle(getPoint1());
            arriveeTrace = toile.positionVirtuelle(getPoint2());
            //TODO: Implémenter le dessin en pointillé
            dessinerContinue(toile.getGraphicsContext2D());
        }
    }

    public Vector2D getPoint1() {
        return point1.getValue();
    }

    public Vector2D getPoint2() {
        return point2.getValue();
    }

    /**
     * Détermine si le segment de droite est indéfini. Il est impossible de
     * tracer un segment de droite passant par deux points équivalents.
     *
     * @return {@code true} si le segment de droite est indéfini.
     */
    protected boolean isIndefinie() {
        return getPoint1().equals(getPoint2());
    }

    @Override
    public Vector2D getVecteurDirecteur() {
        return getPoint1().subtract(getPoint2());
    }

}
