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
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Repere;

/**
 * Une aire représente un polygone formé d'une séquence de points dans l'espace.
 * Pour représenter un polygone et ses côtés, il faudra utiliser à la fois une
 * aire et des segments de droite.
 *
 * @author Marc-Antoine Ouimet
 */
@SuppressWarnings("unchecked")
public class Aire extends Forme {

    /**
     * La couleur par défaut d'une aire.
     */
    private static final Color COULEUR_PAR_DEFAUT = new Color(0.72, 0.52,
            0.04, 0.4);

    /**
     * L'ensemble ordonné des points de coordonnées réelles délimitant cette 
     * aire.
     */
    private final ObservableSet<ObjectProperty<Vector2D>> points
            = FXCollections.observableSet(new LinkedHashSet<>());

    /**
     * Construit une aire vide.
     */
    public Aire() {
    }

    /**
     * Construit une aire sur un ensemble défini de points.
     *
     * @param points l'ensemble de points traçant le polygone de l'aire.
     */
    public Aire(@NotNull final ObjectProperty<Vector2D>[] points) {
        setPoints(points);
        setCouleur(COULEUR_PAR_DEFAUT);
    }

    /**
     * Construit une prévisualisation d'aire définie sur un ensemble de points
     * et la position du curseur.
     *
     * @param points l'ensemble des points définis de l'aire à prévisualiser.
     * @param curseur la position réelle du curseur correspondant au prochain
     * point de {@code points}.
     */
    public Aire(@NotNull final ObjectProperty<Vector2D>[] points,
            @NotNull final ObjectProperty<Vector2D> curseur) {
        this(points);
        this.points.add(curseur);
    }

    {
        proprietesActualisation.add(points);
    }

    @Override
    public void dessiner(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        if (isEnSurbrillance()) {
            dessinerSurbrillance(toile, repere);
        }
        dessinerAire(toile, repere.positionsVirtuelles(getPoints()),
                getCouleur());
    }

    /**
     * Dessine une aire aux contraintes et à la couleur définis sur une toile.
     *
     * @param toile la toile sur laquelle dessiner l'aire.
     * @param pointsVirtuels les points virtuels délimitant l'aire.
     * @param couleur la couleur de l'aire.
     */
    private static void dessinerAire(@NotNull final Canvas toile,
            @NotNull final Vector2D[] pointsVirtuels,
            @NotNull final Color couleur) {
        final GraphicsContext contexteGraphique = toile.getGraphicsContext2D();
        contexteGraphique.setFill(couleur);
        contexteGraphique.fillPolygon(abscisses(pointsVirtuels),
                ordonnees(pointsVirtuels), pointsVirtuels.length);
    }

    @Override
    public void dessinerSurbrillance(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        dessinerAire(toile, repere.positionsVirtuelles(getPoints()),
                getCouleur().darker());
    }

    /**
     * Récupère l'ensemble des abscisses des points dans leur séquence.
     *
     * @return les abscisses de points.
     */
    private static double[] abscisses(@NotNull final Vector2D[] points) {
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
     *
     * @return les ordonnées de points.
     */
    private static double[] ordonnees(@NotNull final Vector2D[] points) {
        final double[] ordonnees = new double[points.length];
        int i = 0;
        for (final Vector2D point : points) {
            ordonnees[i] = point.getY();
            i++;
        }
        return ordonnees;
    }

    @Override
    public double distance(@NotNull final Vector2D curseur,
            @NotNull final Repere repere) {
        if (positionDansAire(curseur, repere)) {
            return 0;
        }
        Vector2D premierPoint = null;
        Vector2D point1 = null;
        Vector2D point2;
        double distance = Double.MAX_VALUE;
        int i = 0;
        final Iterator<ObjectProperty<Vector2D>> iteration = points.iterator();
        while (i < points.size()) {
            if (i == 0) {
                point1 = repere.positionVirtuelle(iteration.next().getValue());
                premierPoint = point1;
            }
            i++;
            if (i < points.size()) {
                point2 = repere.positionVirtuelle(iteration.next().getValue());
            } else {
                point2 = premierPoint;
            }
            distance = Math.min(distance,
                    new Segment(point1, point2, null).distance(curseur));
            point1 = point2;
        }
        return distance;
    }

    /**
     * Détermine si la position du curseur se retrouve dans l'aire. Cette
     * méthode est une implémentation de
     * <a href="https://wrf.ecse.rpi.edu//Research/Short_Notes/pnpoly.html">l'algorithme
     * de W. Randolph Franklin</a>.
     *
     * @param curseur la position virtuelle du curseur.
     * @param repere le repère de l'espace.
     * @return {@code true} si le curseur est dans cette aire.
     */
    private boolean positionDansAire(@NotNull final Vector2D curseur,
            @NotNull final Repere repere) {
        boolean dansAire = false;
        final int npol = points.size();
        final Vector2D[] pts = repere.positionsVirtuelles(getPoints());
        for (int i = 0, j = npol - 1; i < npol; j = i++) {
            if ((((pts[i].getY() <= curseur.getY())
                    && (curseur.getY() < pts[j].getY()))
                    || ((pts[j].getY() <= curseur.getY())
                    && (curseur.getY() < pts[i].getY())))
                    && (curseur.getX() < (pts[j].getX() - pts[i].getX())
                    * (curseur.getY() - pts[i].getY())
                    / (pts[j].getY() - pts[i].getY()) + pts[i].getX())) {
                dansAire = !dansAire;
            }
        }
        return dansAire;
    }

    /**
     * Récupère l'ensemble des positions réelles des points délimitant cette
     * aire.
     *
     * @return les points délimitant cette aire.
     */
    public Vector2D[] getPoints() {
        final Vector2D[] pointsReels = new Vector2D[points.size()];
        int i = 0;
        for (final ObjectProperty<Vector2D> point : points) {
            pointsReels[i] = point.getValue();
            i++;
        }
        return pointsReels;
    }
    
    public final void setPoints(
            @NotNull final LinkedHashSet<ObjectProperty<Vector2D>> points) {
        this.points.clear();
        this.points.addAll(points);
    }
    
    public final void setPoints(
            @NotNull final ObjectProperty<Vector2D>... points) {
        this.points.clear();
        for (final ObjectProperty<Vector2D> point : points) {
            this.points.add(point);
        }
    }

}
