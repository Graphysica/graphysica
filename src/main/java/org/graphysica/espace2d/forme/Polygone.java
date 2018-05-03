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
import java.util.Iterator;
import java.util.LinkedHashSet;
import javafx.beans.property.ObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.position.Position;
import org.graphysica.espace2d.Repere;

/**
 * Un polygone est une aire formée par une séquence de points dans l'espace.
 * Pour représenter un polygone et ses côtés, il faudra ajouter séparément des
 * segments de droites reliant les points en séquence.
 *
 * @author Marc-Antoine Ouimet
 */
@SuppressWarnings("unchecked")
public class Polygone extends Aire {

    /**
     * L'ensemble ordonné des points de coordonnées réelles délimitant ce
     * polygone.
     */
    private final LinkedHashSet<ObjectProperty<Position>> points 
            = new LinkedHashSet<>();

    /**
     * Construit un polygone aux points non-définis.
     */
    protected Polygone() {
    }

    /**
     * Construit un polygone sur un ensemble défini de points.
     *
     * @param points l'ensemble de points traçant le polygone de l'aire.
     */
    public Polygone(@NotNull final ObjectProperty<Position>... points) {
        setPoints(points);
        setCouleur(COULEUR_PAR_DEFAUT);
    }

    {
        points.forEach((point) -> {
            proprietes.add(point);
        });
    }

    @Override
    public void dessinerNormal(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        dessinerPolygone(toile, repere.positionsVirtuelles(getPoints(repere)),
                getCouleur());
    }

    @Override
    public void dessinerSurvol(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        dessinerPolygone(toile, repere.positionsVirtuelles(getPoints(repere)),
                getCouleur().darker());
    }

    /**
     * Dessine un polygone aux contraintes et à la couleur définis sur une
     * toile.
     *
     * @param toile la toile sur laquelle dessiner le polygone.
     * @param pointsVirtuels les points virtuels délimitant le polygone.
     * @param couleur la couleur du polygone.
     */
    private static void dessinerPolygone(@NotNull final Canvas toile,
            @NotNull final Vector2D[] pointsVirtuels,
            @NotNull final Color couleur) {
        final GraphicsContext contexteGraphique = toile.getGraphicsContext2D();
        contexteGraphique.setFill(couleur);
        contexteGraphique.fillPolygon(abscisses(pointsVirtuels),
                ordonnees(pointsVirtuels), pointsVirtuels.length);
    }

    /**
     * Récupère l'ensemble des abscisses des points dans leur séquence.
     * @param points les points dont on veut les abscisses.
     * 
     * @return les abscisses de points.
     */
    private static double[] abscisses(@NotNull final Vector2D... points) {
        final double[] abscisses = new double[points.length];
        int i = 0;
        for (final Vector2D point : points) {
            abscisses[i] = point.getX();
            i++;
        }
        return abscisses;
    }

    /**
     * Récupère l'ensemble des ordonnées des points dans leur séquence.
     * @param points les points dont on veut les ordonnées.
     * 
     * @return les ordonnées de points.
     */
    private static double[] ordonnees(@NotNull final Vector2D... points) {
        final double[] ordonnees = new double[points.length];
        int i = 0;
        for (final Vector2D point : points) {
            ordonnees[i] = point.getY();
            i++;
        }
        return ordonnees;
    }

    @Override
    public double distance(@NotNull final Position curseur,
            @NotNull final Repere repere) {
        if (positionDansPolygone(curseur, repere)) {
            return 0;
        }
        Vector2D premierPoint = null;
        Vector2D point1 = null;
        Vector2D point2;
        double distance = Double.MAX_VALUE;
        int i = 0;
        final Iterator<ObjectProperty<Position>> iteration = points.iterator();
        while (i < points.size()) {
            if (i == 0) {
                point1 = iteration.next().getValue().virtuelle(repere);
                premierPoint = point1;
            }
            i++;
            if (i < points.size()) {
                point2 = iteration.next().getValue().virtuelle(repere);
            } else {
                point2 = premierPoint;
            }
            distance = Math.min(distance,
                    new Segment(point1, point2, null).distance(
                            curseur.virtuelle(repere)));
            point1 = point2;
        }
        return distance;
    }

    /**
     * Détermine si la position du curseur se retrouve dans le polyogne. Cette
     * méthode est une implémentation de
     * <a href="https://wrf.ecse.rpi.edu//Research/Short_Notes/pnpoly.html">l'algorithme
     * de W. Randolph Franklin</a>.
     *
     * @param curseur la position virtuelle du curseur.
     * @param repere le repère de l'espace.
     * @return {@code true} si le curseur est dans ce polygone.
     */
    private boolean positionDansPolygone(@NotNull final Position curseur,
            @NotNull final Repere repere) {
        final Vector2D pt = curseur.virtuelle(repere);
        boolean dansPolygone = false;
        final int npol = points.size();
        final Vector2D[] pts = repere.positionsVirtuelles(getPoints(repere));
        for (int i = 0, j = npol - 1; i < npol; j = i++) {
            if ((((pts[i].getY() <= pt.getY())
                    && (pt.getY() < pts[j].getY()))
                    || ((pts[j].getY() <= pt.getY())
                    && (pt.getY() < pts[i].getY())))
                    && (pt.getX() < (pts[j].getX() - pts[i].getX())
                    * (pt.getY() - pts[i].getY())
                    / (pts[j].getY() - pts[i].getY()) + pts[i].getX())) {
                dansPolygone = !dansPolygone;
            }
        }
        return dansPolygone;
    }

    /**
     * Récupère l'ensemble des positions réelles des points délimitant ce
     * polygone.
     *
     * @param repere le repère d'espace du polygone.
     * @return les points délimitant ce polygone.
     */
    private Vector2D[] getPoints(@NotNull final Repere repere) {
        final Vector2D[] pointsReels = new Vector2D[points.size()];
        int i = 0;
        for (final ObjectProperty<Position> point : points) {
            pointsReels[i] = point.getValue().reelle(repere);
            i++;
        }
        return pointsReels;
    }

    protected final void setPoints(
            @NotNull final ObjectProperty<Position>... points) {
        for (final ObjectProperty<Position> point : points) {
            this.points.add(point);
        }
    }

}
