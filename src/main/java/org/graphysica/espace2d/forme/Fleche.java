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
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Repere;

/**
 * Une flèche relie un point d'origine vers un point d'arrivée. La direction de
 * la flèche est représentée par un triangle dont le sommet correspond à
 * l'arrivée de la flèche.
 *
 * @author Marc-Antoine Ouimet
 */
@SuppressWarnings("unchecked")
public class Fleche extends SegmentDroite {

    /**
     * La tête de cette flèche.
     */
    private final Triangle tete = new Triangle();

    public Fleche(@NotNull final Vector2D point1,
            @NotNull final Vector2D point2) {
        super(point1, point2);
    }

    public Fleche(@NotNull final Point point1,
            @NotNull final Point point2) {
        super(point1, point2);
    }

    public Fleche(@NotNull final Vector2D point,
            @NotNull final ObjectProperty<Vector2D> curseur) {
        super(point, curseur);
    }

    public Fleche(@NotNull final Point point,
            @NotNull final ObjectProperty<Vector2D> curseur) {
        super(point, curseur);
    }

    @Override
    public double distance(@NotNull final Vector2D curseur,
            @NotNull final Repere repere) {
        return Math.min(super.distance(curseur, repere),
                tete.distance(curseur, repere));
    }

    @Override
    public void dessiner(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        super.dessiner(toile, repere);
        tete.calculerPositionsPoints(repere, getArrivee());
        tete.dessiner(toile, repere);
    }

    public void setOrigine(@NotNull final Vector2D origine) {
        this.point1.setValue(origine);
    }

    public void setArrivee(@NotNull final Vector2D arrivee) {
        this.point2.setValue(arrivee);
    }

    private Vector2D getOrigine() {
        return getPoint1();
    }

    private Vector2D getArrivee() {
        return getPoint2();
    }

    /**
     * Une tête de flèche, représentée par un triangle.
     */
    private class Triangle extends Aire {

        /**
         * La hauteur virtuelle du triangle.
         */
        private final Taille hauteur = Taille.de("teteflechehauteur");

        /**
         * La largeur virtuelle du triangle.
         */
        private final Taille largeur = Taille.de("teteflechelargeur");

        private final ObjectProperty<Vector2D> sommet
                = new SimpleObjectProperty<>();

        private final ObjectProperty<Vector2D> pied1
                = new SimpleObjectProperty<>();
        
        private final ObjectProperty<Vector2D> pied2
                = new SimpleObjectProperty<>();

        public Triangle() {
            super();
            setPoints(pied1, sommet, pied2);
        }

        private void calculerPositionsPoints(@NotNull final Repere repere, 
                @NotNull final Vector2D arriveeReelle) {
            final Vector2D arriveeVirtuelle = repere.positionVirtuelle(
                    arriveeReelle);
            final Vector2D directionReelle = getOrigine()
                    .subtract(getArrivee());
            final Vector2D vecteurDirecteur = directionReelle
                    .scalarMultiply(1 / directionReelle.getNorm());
            final Vector2D perpendiculaire = new Vector2D(
                    vecteurDirecteur.getY(), vecteurDirecteur.getX());
            final Vector2D pied = arriveeVirtuelle.add(
                    new Vector2D(2 * getHauteur() * vecteurDirecteur.getX(),
                            - 2 * getHauteur() * vecteurDirecteur.getY()));
            pied1.setValue(repere.positionReelle(
                    pied.add(getLargeur(), perpendiculaire)));
            sommet.setValue(arriveeReelle);
            pied2.setValue(repere.positionReelle(
                    pied.subtract(getLargeur(), perpendiculaire)));
        }

        public int getHauteur() {
            return hauteur.getValue();
        }

        public Taille hauteurProperty() {
            return hauteur;
        }

        public int getLargeur() {
            return largeur.getValue();
        }

        public Taille largeurProperty() {
            return largeur;
        }

    }

}
