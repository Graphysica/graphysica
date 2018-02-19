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
 *
 * @author Marc-Antoine Ouimet
 */
public class Ligne extends Forme {

    /**
     * La couleur par défaut d'une droite.
     */
    static final Color COULEUR_PAR_DEFAUT = Color.BLACK;

    private final ObjectProperty<Vector2D> point1
            = new SimpleObjectProperty<>();

    private final ObjectProperty<Vector2D> point2
            = new SimpleObjectProperty<>();

    private Vector2D point1Trace;

    private Vector2D point2Trace;

    private final ObjectProperty<Taille> epaisseur
            = new SimpleObjectProperty<>(Taille.de("ligne"));

    public Ligne(@NotNull final Point point1, @NotNull final Point point2) {
        this.point1.setValue(point1.getPosition());
        this.point1.bind(point1.positionProperty());
        this.point2.setValue(point2.getPosition());
        this.point2.bind(point2.positionProperty());
    }

    @Override
    public void dessiner(@NotNull final Toile toile) {

    }

    private void dessinerContinue(
            @NotNull final GraphicsContext contexteGraphique) {
        contexteGraphique.setStroke(COULEUR_PAR_DEFAUT);
        contexteGraphique.setLineWidth(getEpaisseur());
        contexteGraphique.strokeLine(point1Trace.getX(), point1Trace.getY(),
                point2Trace.getX(), point2Trace.getY());
    }

    public Vector2D getPoint1() {
        return point1.getValue();
    }

    public Vector2D getPoint2() {
        return point2.getValue();
    }

    private int getEpaisseur() {
        return epaisseur.getValue().getTaille();
    }

}
