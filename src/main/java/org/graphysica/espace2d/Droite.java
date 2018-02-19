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
package org.graphysica.espace2d;

import com.sun.istack.internal.NotNull;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Une droite bissecte l'espace en traversant deux points distincts.
 *
 * @author Marc-Antoine Ouimet
 */
public class Droite extends Forme {

    /**
     * La couleur par défaut d'une droite.
     */
    static final Color COULEUR_PAR_DEFAUT = Color.BLACK;

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
     * L'épaisseur du tracé de la droite.
     */
    protected final ObjectProperty<Taille> epaisseur
            = new SimpleObjectProperty<>(Taille.de("ligne"));

    /**
     * TODO: Les attributs plus bas ne sont utiles que pour le tracé de droites.
     */
    /**
     * La position virtuelle du point 1.
     *
     * @see Droite#point1
     */
    private Vector2D point1Virtuel;

    /**
     * La position virtuelle du point 2.
     *
     * @see Droite#point2
     */
    private Vector2D point2Virtuel;

    /**
     * La position d'origine de la trace de la droite.
     */
    private Vector2D origineTrace;

    /**
     * La position de l'arrivée de la trace de la droite.
     */
    private Vector2D arriveeTrace;

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
        proprietesActualisation.add(this.point1);
        proprietesActualisation.add(this.point2);
        proprietesActualisation.add(epaisseur);
        this.point1.setValue(point1.getPosition());
        this.point1.bind(point1.positionProperty());
        this.point2.setValue(point2.getPosition());
        this.point2.bind(point2.positionProperty());
    }

    @Override
    public void dessiner(@NotNull final Toile toile) {
        if (!isIndefinie()) {
            point1Virtuel = toile.positionVirtuelle(getPoint1());
            point2Virtuel = toile.positionVirtuelle(getPoint2());
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
            dessinerContinue(toile.getGraphicsContext2D());
        }
    }

    private void dessinerContinue(
            @NotNull final GraphicsContext contexteGraphique) {
        contexteGraphique.setStroke(COULEUR_PAR_DEFAUT);
        contexteGraphique.setLineWidth(getEpaisseur());
        contexteGraphique.strokeLine(origineTrace.getX(), origineTrace.getY(),
                arriveeTrace.getX(), arriveeTrace.getY());
    }

    public Vector2D getPoint1() {
        return point1.getValue();
    }

    public Vector2D getPoint2() {
        return point2.getValue();
    }

    protected int getEpaisseur() {
        return epaisseur.getValue().getTaille();
    }

    protected boolean isIndefinie() {
        return getPoint1().equals(getPoint2());
    }

}
