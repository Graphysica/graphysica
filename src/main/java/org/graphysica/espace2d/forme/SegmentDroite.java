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
import javafx.scene.canvas.Canvas;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Repere;

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
     * Construit un segment de droite à partir de deux vecteurs position fixes.
     *
     * @param point1 le premier vecteur position du segment de droite.
     * @param point2 le deuxième vecteur position du segment de droite.
     */
    public SegmentDroite(@NotNull final Vector2D point1,
            @NotNull final Vector2D point2) {
        this.point1.setValue(point1);
        this.point2.setValue(point2);
    }

    /**
     * Construit un segment de droite lié à la position de deux points définis.
     *
     * @param point1 le premier point en coordonnées réelles.
     * @param point2 le deuxième point en coordonnées réelles.
     */
    public SegmentDroite(@NotNull final Point point1,
            @NotNull final Point point2) {
        this.point1.bind(point1.positionProperty());
        this.point2.bind(point2.positionProperty());
    }

    /**
     * Construit une prévisualisation de segment de droite à partir d'un vecteur
     * position et de la position du curseur sur la droite.
     *
     * @param point le vecteur position de l'origine du segment de droite.
     * @param curseur l'arrivée du segment de droite, qui correspond à la
     * position réelle du curseur sur la toile.
     */
    public SegmentDroite(@NotNull final Vector2D point,
            @NotNull final ObjectProperty<Vector2D> curseur) {
        this.point1.setValue(point);
        this.point2.bind(curseur);
    }

    /**
     * Construit une prévisualisation de segment de droite à partir d'un point
     * défini et de la position du curseur sur la toile.
     *
     * @param point le point d'origine du segment de droite.
     * @param curseur l'arrivée du segment de droite, qui correspond à la
     * position réelle du curseur sur la toile.
     */
    public SegmentDroite(@NotNull final Point point,
            @NotNull final ObjectProperty<Vector2D> curseur) {
        point1.bind(point.positionProperty());
        point2.bind(curseur);
    }

    {
        proprietesActualisation.add(point1);
        proprietesActualisation.add(point2);
        proprietesActualisation.add(epaisseur);
    }

    @Override
    public void dessiner(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        if (!isIndefinie()) {
            calculerOrigineEtArrivee(toile, repere);
            dessinerLigne(toile, origineTrace, arriveeTrace, getCouleur(),
                    getEpaisseur());
        }
    }

    @Override
    protected void calculerOrigineEtArrivee(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        origineTrace = repere.positionVirtuelle(getPoint1());
        arriveeTrace = repere.positionVirtuelle(getPoint2());
    }

    @Override
    public void dessinerSurbrillance(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        if (!isIndefinie()) {
            calculerOrigineEtArrivee(toile, repere);
            dessinerLigne(toile, origineTrace, arriveeTrace, 
                    getCouleur().deriveColor(1, 1, 1, 0.3), getEpaisseur() + 2);
            dessinerLigne(toile, origineTrace, arriveeTrace, getCouleur(),
                    getEpaisseur());
        }
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

    @Override
    public double distance(@NotNull final Vector2D curseur,
            @NotNull final Repere repere) {
        final Vector2D p1 = repere.positionVirtuelle(getPoint1());
        final Vector2D p2 = repere.positionVirtuelle(getPoint2());
        return new Segment(p1, p2, null).distance(curseur);
    }

    public Vector2D getPoint1() {
        return point1.getValue();
    }

    public Vector2D getPoint2() {
        return point2.getValue();
    }

}
