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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Repere;

/**
 * Une grille permet de subdiviser l'espace selon un espacement virtuel qui
 * correspond à un pourcentage entier de son échelle. La grille n'est pas
 * dessinée à l'aide d'instances de droites horizontales et verticales par
 * soucis de gestion de mémoire.
 *
 * @author Marc-Antoine Ouimet
 */
public final class Grille extends Forme {

    /**
     * L'espacement minimum des graduations de la grille exprimée en pixels.
     */
    private final ObjectProperty<Vector2D> espacement
            = new SimpleObjectProperty<>();

    /**
     * L'épaisseur du tracé de la grille.
     */
    private final Taille epaisseur = Taille.de("grille");

    /**
     * L'ensemble des graduations virtuelles horizontales de la grille en ordre
     * croissant, recalculé à chaque dessin de la grille.
     */
    private double[] graduationsHorizontales;

    /**
     * L'ensemble des graduations virtuelles verticales de la grille en ordre
     * croissant, recalculé à chaque dessin de la grille.
     */
    private double[] graduationsVerticales;

    /**
     * Construit une grille dont l'espacement et la couleur sont définis.
     *
     * @param espacement l'espacement virtuel entre chaque graduation de la
     * grille.
     * @param couleur la couleur de la grille.
     */
    public Grille(@NotNull final Vector2D espacement,
            @NotNull final Color couleur) {
        setEspacement(espacement);
        setCouleur(couleur);
    }

    {
        proprietesActualisation.add(epaisseur);
    }

    @Override
    public void dessiner(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        graduationsHorizontales = repere.graduationsHorizontales(
                toile.getHeight(), getEspacement().getY());
        graduationsVerticales = repere.graduationsVerticales(
                toile.getWidth(), getEspacement().getX());
        final GraphicsContext contexteGraphique = toile.getGraphicsContext2D();
        contexteGraphique.setStroke(getCouleur());
        contexteGraphique.setLineWidth(epaisseur.getValue());
        for (final double y : graduationsHorizontales) {
            contexteGraphique.strokeLine(0, y, toile.getWidth(), y);
        }
        for (final double x : graduationsVerticales) {
            contexteGraphique.strokeLine(x, 0, x, toile.getHeight());
        }
    }

    @Override
    public double distance(@NotNull final Vector2D curseur,
            @NotNull final Repere repere) {
        double distanceHorizontale = Double.MAX_VALUE;
        for (final double graduationHorizontale : graduationsHorizontales) {
            distanceHorizontale = Math.min(distanceHorizontale,
                    Math.abs(curseur.getX() - graduationHorizontale));
        }
        double distanceVerticale = Double.MAX_VALUE;
        for (final double graduationVerticale : graduationsVerticales) {
            distanceVerticale = Math.min(distanceVerticale,
                    Math.abs(curseur.getY() - graduationVerticale));
        }
        return Math.min(Math.min(distanceHorizontale, distanceVerticale),
                new Vector2D(distanceHorizontale, distanceVerticale).getNorm());
    }

    public final Vector2D getEspacement() {
        return espacement.getValue();
    }

    public final void setEspacement(@NotNull final Vector2D espacement) {
        this.espacement.setValue(espacement);
    }

    public final ObjectProperty<Vector2D> espacementProperty() {
        return espacement;
    }

}
