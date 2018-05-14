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
import javafx.scene.paint.Color;
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
     * Construit un segment de droite reliant deux positions.
     *
     * @param position1 la première position du segment de droite.
     * @param position2 la deuxième position du segment de droite.
     */
    public SegmentDroite(
            @NotNull final ObjectProperty<? extends Position> position1,
            @NotNull final ObjectProperty<? extends Position> position2) {
        super(position1, position2);
    }

    /**
     * Construit un segment de droite reliant deux positions.
     *
     * @param position1 la première position du segment de droite.
     * @param position2 la deuxième position du segment de droite.
     * @param couleur la couleur du segment de droite.
     */
    public SegmentDroite(
            @NotNull final ObjectProperty<? extends Position> position1,
            @NotNull final ObjectProperty<? extends Position> position2,
            @NotNull final ObjectProperty<Color> couleur) {
        this(position1, position2);
        couleurProperty().bind(couleur);
    }

    @Override
    public void calculerOrigineEtArrivee(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        origineTrace = getPosition1();
        arriveeTrace = getPosition2();
    }

    /**
     * Détermine si le segment de droite est indéfini. Il est impossible de
     * tracer un segment de droite passant par deux points équivalents.
     *
     * @return {@code true} si le segment de droite est indéfini.
     */
    protected boolean isIndefinie() {
        return getPosition1().equals(getPosition2());
    }

    @Override
    public double distance(@NotNull final Position curseur,
            @NotNull final Repere repere) {
        final Vector2D p1 = getPosition1().virtuelle(repere);
        final Vector2D p2 = getPosition2().virtuelle(repere);
        return new Segment(p1, p2, null).distance(curseur.virtuelle(repere));
    }

}
