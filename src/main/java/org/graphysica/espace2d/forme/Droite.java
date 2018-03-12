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
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Toile;

/**
 * Une droite bissecte l'espace en traversant deux points distincts.
 *
 * @author Marc-Antoine Ouimet
 */
public final class Droite extends SegmentDroite {

    /**
     * L'écart entre les abscisses des points virtuels dans le sens du système
     * d'axes virtuels. Correspond à
     * {@code point2Virtuel.getX() - point1Virtuel.getX()}.
     *
     * @see Droite#point1Virtuel
     * @see Droite#point2Virtuel
     */
    private double variationAbscisses;

    /**
     * L'écart entre les ordonnées des points virtuels dans le sens du système
     * d'axes virtuels. Correspond à
     * {@code point2Virtuel.getY() - point1Virtuel.getY()}.
     *
     * @see Droite#point1Virtuel
     * @see Droite#point2Virtuel
     */
    private double variationOrdonnees;

    /**
     * Construit une droite passant par deux points.
     *
     * @param point1 le premier point en coordonnées réelles.
     * @param point2 le deuxième point en coordonnées réelles.
     */
    public Droite(@NotNull final Point point1, @NotNull final Point point2) {
        super(point1, point2);
    }

    @Override
    public void dessiner(@NotNull final Toile toile) {
        if (!isIndefinie()) {
            calculerPositionTraces(toile);
            dessinerContinue(toile.getGraphicsContext2D());
        }
    }

    /**
     * Calcule la position des points de trace de la droite.
     *
     * @param toile les contraintes du tracé de la droite.
     * @see Droite#origineTrace
     * @see Droite#arriveeTrace
     */
    private void calculerPositionTraces(final Toile toile) {
        final Vector2D point1Virtuel = toile.positionVirtuelle(getPoint1());
        final Vector2D point2Virtuel = toile.positionVirtuelle(getPoint2());
        variationAbscisses = point2Virtuel.getX() - point1Virtuel.getX();
        variationOrdonnees = point2Virtuel.getY() - point1Virtuel.getY();
        if (Math.abs(variationAbscisses) > Math.abs(variationOrdonnees)) {
            //La droite est définie même si {@code variationOrdonnees == 0}
            final double m = variationOrdonnees / variationAbscisses;
            //La droite est d'équation: y=mx+b
            final double b = point1Virtuel.getY()
                    - m * point1Virtuel.getX();
            //Soient les points P(xmin, yP) et  Q(xmax, yQ)
            final double yP = b;
            final double yQ = m * toile.getWidth() + b;
            origineTrace = new Vector2D(0, yP); //P
            arriveeTrace = new Vector2D(toile.getWidth(), yQ); //Q
        } else {
            //La droite est définie même si {@code variationAbscisses == 0}
            final double m = variationAbscisses / variationOrdonnees;
            //La droite est d'équation x=my+b
            final double b = point1Virtuel.getX()
                    - m * point1Virtuel.getY();
            //Soient les points P(xP, ymin) et Q(xQ, ymax)
            final double xP = b;
            final double xQ = m * toile.getHeight() + b;
            origineTrace = new Vector2D(xP, 0); //P
            arriveeTrace = new Vector2D(xQ, toile.getHeight()); //Q
        }

    }

}
