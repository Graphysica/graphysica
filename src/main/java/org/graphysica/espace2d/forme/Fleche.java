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
import org.graphysica.espace2d.position.Position;
import org.graphysica.espace2d.Repere;
import static org.graphysica.espace2d.position.Type.VIRTUELLE;

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

    /**
     * Construit une flèche d'une position d'origine vers une position
     * d'arrivée.
     *
     * @param origine l'origine de la flèche.
     * @param arrivee l'arrivée de la flèche.
     */
    public Fleche(@NotNull final ObjectProperty<? extends Position> origine,
            @NotNull final ObjectProperty<? extends Position> arrivee) {
        super(origine, arrivee);
    }

    @Override
    public double distance(@NotNull final Position curseur,
            @NotNull final Repere repere) {
        return Math.min(super.distance(curseur, repere),
                tete.distance(curseur, repere));
    }

    @Override
    public void dessinerNormal(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        super.dessinerNormal(toile, repere);
        tete.calculerPositionsPoints(repere, getArrivee());
        tete.dessinerNormal(toile, repere);
    }

    @Override
    public void dessinerSurbrillance(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        super.dessinerSurbrillance(toile, repere);
        tete.dessinerSurbrillance(toile, repere);
    }

    private Position getOrigine() {
        return getPosition1();
    }

    private Position getArrivee() {
        return getPosition2();
    }

    /**
     * Une tête de flèche, représentée par un triangle.
     */
    private class Triangle extends Polygone {

        /**
         * La hauteur virtuelle du triangle.
         */
        private final Taille hauteur = Taille.de("teteflechehauteur");

        /**
         * La largeur virtuelle du triangle.
         */
        private final Taille largeur = Taille.de("teteflechelargeur");

        private final ObjectProperty<Position> sommet
                = new SimpleObjectProperty<>();

        private final ObjectProperty<Position> pied1
                = new SimpleObjectProperty<>();

        private final ObjectProperty<Position> pied2
                = new SimpleObjectProperty<>();

        public Triangle() {
            super();
            setPoints(pied1, sommet, pied2);
        }

        {
            proprietesActualisation.add(largeur);
            proprietesActualisation.add(hauteur);
        }

        private void calculerPositionsPoints(@NotNull final Repere repere,
                @NotNull final Position arrivee) {
            final Vector2D directionVirtuelle = getArrivee().virtuelle(repere)
                    .subtract(getOrigine().virtuelle(repere));
            final Vector2D vecteurDirecteur = directionVirtuelle
                    .scalarMultiply(1 / directionVirtuelle.getNorm());
            final Vector2D perpendiculaire = new Vector2D(
                    -vecteurDirecteur.getY(), vecteurDirecteur.getX());
            final Vector2D arriveeVirtuelle = arrivee.virtuelle(repere);
            final Vector2D pied = arriveeVirtuelle.subtract(
                    new Vector2D(2 * getHauteur() * vecteurDirecteur.getX(),
                            2 * getHauteur() * vecteurDirecteur.getY()));
            pied1.setValue(Position.a(
                    pied.add(getLargeur(), perpendiculaire), VIRTUELLE));
            sommet.setValue(arrivee);
            pied2.setValue(Position.a(
                    pied.subtract(getLargeur(), perpendiculaire), VIRTUELLE));
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
