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
import javafx.scene.canvas.GraphicsContext;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Toile;

/**
 * Une flèche relie un point d'origine vers un point d'arrivée. La direction de
 * la flèche est représentée par un triangle dont le sommet correspond à
 * l'arrivée de la flèche.
 *
 * @author Marc-Antoine Ouimet
 */
public class Fleche extends SegmentDroite {

    /**
     * La tête de cette flèche.
     */
    private final Tete tete = new Tete();

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
    public void dessiner(@NotNull final Toile toile) {
        super.dessiner(toile);
        tete.dessiner(toile);
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
    private class Tete extends Forme {

        /**
         * La hauteur virtuelle du triangle.
         */
        private final Taille hauteur = Taille.de("teteflechehauteur");

        /**
         * La largeur virtuelle du triangle.
         */
        private final Taille largeur = Taille.de("teteflechelargeur");

        /**
         * Détermine l'ensemble des points virtuels qui tracent une tête
         * triangulaire de flèche.
         *
         * @param arriveeVirtuelle l'arrivée virtuelle de la tête.
         * @return l'ensemble ordonné des points aux coordonnées virtuelles
         * traçant la tête de flèche.
         */
        public Vector2D[] polygoneTete(
                @NotNull final Vector2D arriveeVirtuelle) {
            final Vector2D directionReelle = getOrigine()
                    .subtract(getArrivee());
            final Vector2D vecteurDirecteurReel = directionReelle
                    .scalarMultiply(1 / directionReelle.getNorm());
            final Vector2D perpendiculaireVirtuel = new Vector2D(
                    vecteurDirecteurReel.getY(), vecteurDirecteurReel.getX());
            final Vector2D pied = arriveeVirtuelle.add(
                    new Vector2D(2 * getHauteur() * vecteurDirecteurReel.getX(),
                            - 2 * getHauteur() * vecteurDirecteurReel.getY()));
            return new Vector2D[]{
                pied.add(getLargeur(), perpendiculaireVirtuel),
                arriveeVirtuelle,
                pied.subtract(getLargeur(), perpendiculaireVirtuel)
            };
        }

        private double abscisse(@NotNull final Vector2D point) {
            return point.getX();
        }

        private double[] abscisses(@NotNull final Vector2D... points) {
            final double[] abscisses = new double[points.length];
            for (int i = 0; i < abscisses.length; i++) {
                abscisses[i] = abscisse(points[i]);
            }
            return abscisses;
        }

        private double ordonnee(@NotNull final Vector2D point) {
            return point.getY();
        }

        private double[] ordonnees(@NotNull final Vector2D... points) {
            final double[] ordonnees = new double[points.length];
            for (int i = 0; i < ordonnees.length; i++) {
                ordonnees[i] = ordonnee(points[i]);
            }
            return ordonnees;
        }

        @Override
        public void dessiner(@NotNull final Toile toile) {
            final GraphicsContext contexteGraphique = toile
                    .getGraphicsContext2D();
            contexteGraphique.setFill(Fleche.this.getCouleur());
            final Vector2D[] pointsTrace = polygoneTete(toile.positionVirtuelle(
                    getArrivee()));
            contexteGraphique.fillPolygon(abscisses(pointsTrace),
                    ordonnees(pointsTrace), pointsTrace.length);
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
