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
import javafx.scene.canvas.Canvas;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.position.Position;
import org.graphysica.espace2d.Repere;
import org.graphysica.espace2d.position.PositionVirtuelle;

/**
 * Une droite bissecte l'espace en traversant deux points distincts.
 *
 * @author Marc-Antoine Ouimet
 */
public final class Droite extends Ligne {

    /**
     * Calcule la position des points d'origine et d'arrivée de la trace de
     * cette droite aux bornes de la toile.
     *
     * @param toile les contraintes du tracé de la droite.
     * @param repere le repère de l'espace de dessin de la droite.
     */
    @Override
    protected void calculerOrigineEtArrivee(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        final Vector2D point1Virtuel = getPoint1().virtuelle(repere);
        final Vector2D point2Virtuel = getPoint2().virtuelle(repere);
        final double variationAbscisses = point2Virtuel.getX() 
                - point1Virtuel.getX();
        final double variationOrdonnees = point2Virtuel.getY() 
                - point1Virtuel.getY();
        if (Math.abs(variationAbscisses) > Math.abs(variationOrdonnees)) {
            // La droite est définie même si {@code variationOrdonnees == 0}
            final double m = variationOrdonnees / variationAbscisses;
            // La droite est d'équation: y = m * x + b
            final double b = point1Virtuel.getY()
                    - m * point1Virtuel.getX();
            // Soient les points P(xmin, yP) et  Q(xmax, yQ)
            final double yP = b;
            final double yQ = m * toile.getWidth() + b;
            origineTrace = new PositionVirtuelle(new Vector2D(0, yP)); // P
            arriveeTrace = new PositionVirtuelle(
                    new Vector2D(toile.getWidth(), yQ)); // Q
        } else {
            // La droite est définie même si {@code variationAbscisses == 0}
            final double m = variationAbscisses / variationOrdonnees;
            // La droite est d'équation x = m * y + b
            final double b = point1Virtuel.getX()
                    - m * point1Virtuel.getY();
            // Soient les points P(xP, ymin) et Q(xQ, ymax)
            final double xP = b;
            final double xQ = m * toile.getHeight() + b;
            origineTrace = new PositionVirtuelle(new Vector2D(xP, 0)); // P
            arriveeTrace = new PositionVirtuelle(
                    new Vector2D(xQ, toile.getHeight())); // Q
        }
    }

    @Override
    public double distance(@NotNull final Position curseur,
            @NotNull final Repere repere) {
        /**
         * La droite a déjà été dessinée, alors {@code origineTrace} et
         * {@code arriveeTrace} sont aux bons endroits.
         */
        return new Segment(origineTrace.virtuelle(repere),
                arriveeTrace.virtuelle(repere), null).distance(
                curseur.virtuelle(repere));
    }

}
