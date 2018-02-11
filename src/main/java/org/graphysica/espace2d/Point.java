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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 *
 * @author Marc-Antoine Ouimet
 */
public class Point extends Forme {

    /**
     * La couleur par défaut d'un point.
     */
    private static final Color COULEUR_PAR_DEFAUT = Color.BLUE;

    /**
     * La couleur de la bordure du point.
     */
    private static final Color COULEUR_BORDURE = Color.BLACK;

    /**
     * La taille par défaut d'un point.
     *
     * @see Point#taille
     */
    private static final int TAILLE_PAR_DEFAUT = 5;

    /**
     * La position du point selon la base canonique dans l'espace 2D.
     */
    private final ObjectProperty<Vector2D> position
            = new SimpleObjectProperty<>(new Vector2D(0, 0));

    /**
     * La taille du point sur l'écran, variant de 1 à 10.
     */
    private final IntegerProperty taille = new SimpleIntegerProperty(
            TAILLE_PAR_DEFAUT);

    @Override
    public void dessiner(@NotNull final GraphicsContext contexteGraphique) {
        throw new UnsupportedOperationException();
    }

    public ObjectProperty<Vector2D> positionProperty() {
        return position;
    }

    public Vector2D getPosition() {
        return position.getValue();
    }

    public void setPosition(@NotNull final Vector2D position) {
        this.position.setValue(position);
    }

    public void setPosition(final double abscisse, final double ordonnee) {
        position.setValue(new Vector2D(abscisse, ordonnee));
    }

    public double getAbscisse() {
        return position.getValue().getX();
    }

    public double getOrdonnee() {
        return position.getValue().getY();
    }

    public int getTaille() {
        return taille.getValue();
    }

    public void setTaille(final int taille) throws IllegalArgumentException {
        if (taille < 1 ^ taille > 10) {
            throw new IllegalArgumentException(
                    "Taille spécifiée non-comprise entre 1 et 10: " + taille);
        }
        this.taille.setValue(taille);
    }

    public IntegerProperty tailleProperty() {
        return taille;
    }

}
