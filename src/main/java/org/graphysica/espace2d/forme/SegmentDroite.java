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
import javafx.scene.canvas.Canvas;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.position.Position;
import org.graphysica.espace2d.Repere;

/**
 * Un segment de droite relie deux points distincts dans l'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public class SegmentDroite extends Ligne {

    /**
     * Construit un segment de droite à partir de deux vecteurs position fixes.
     *
     * @param point1 le premier vecteur position du segment de droite.
     * @param point2 le deuxième vecteur position du segment de droite.
     */
    public SegmentDroite(@NotNull final Position point1,
            @NotNull final Position point2) {
        this.position1.setValue(point1);
        this.position2.setValue(point2);
    }

    /**
     * Construit une prévisualisation de segment de droite à partir d'un vecteur
     * position et de la position du curseur sur la droite.
     *
     * @param point le vecteur position de l'origine du segment de droite.
     * @param curseur l'arrivée du segment de droite, qui correspond à la
     * position réelle du curseur sur la toile.
     */
    public SegmentDroite(@NotNull final Position point,
            @NotNull final ObjectProperty<Position> curseur) {
        this.position1.setValue(point);
        this.position2.bind(curseur);
    }

    {
        proprietesActualisation.add(position1);
        proprietesActualisation.add(position2);
        proprietesActualisation.add(epaisseur);
    }

    @Override
    protected void calculerOrigineEtArrivee(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        origineTrace = getPoint1();
        arriveeTrace = getPoint2();
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
    public double distance(@NotNull final Position curseur,
            @NotNull final Repere repere) {
        final Vector2D p1 = getPoint1().virtuelle(repere);
        final Vector2D p2 = getPoint2().virtuelle(repere);
        return new Segment(p1, p2, null).distance(curseur.virtuelle(repere));
    }

}
