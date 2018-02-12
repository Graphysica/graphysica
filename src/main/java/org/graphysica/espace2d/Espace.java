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
import javafx.scene.layout.AnchorPane;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Un espace est un panneau duquel l'utilisateur peut modifier les composantes
 * de sa construction.
 *
 * @author Marc-Antoine Ouimet
 */
public class Espace extends AnchorPane {

    /**
     * La toile des formes de l'espace.
     */
    private final ToileFormes toile;

    /**
     * L'échelle de l'espace exprimée en pixels par mètre.
     */
    private final ObjectProperty<Vector2D> echelle
            = new SimpleObjectProperty<>(new Vector2D(100, 100));

    /**
     * L'origine virtuelle de l'espace exprimée en pixels selon l'origine de
     * l'écran. Par défaut, l'origine de l'espace est au centre du panneau.
     */
    private final ObjectProperty<Vector2D> origine
            = new SimpleObjectProperty<>(
                    new Vector2D(getWidth() / 2, getHeight() / 2));

    /**
     * Construit un espace initialisé dont les dimensions sont liées à celles de
     * la toile des formes.
     *
     * @param largeur la largeur initiale de l'espace, exprimée en pixels.
     * @param hauteur la hauteur initiale de l'espace, exprimée en pixels.
     */
    public Espace(final double largeur, final double hauteur) {
        toile = new ToileFormes(largeur, hauteur);
        lierDimensions();
        initialiser();
    }

    private void initialiser() {
        getChildren().add(toile);
    }

    /**
     * Lie les dimensions de la toile aux dimensions de l'espace.
     */
    private void lierDimensions() {
        toile.widthProperty().bind(widthProperty());
        toile.heightProperty().bind(heightProperty());
    }

    public ObjectProperty<Vector2D> echelleProperty() {
        return echelle;
    }

    public Vector2D getEchelle() {
        return echelle.getValue();
    }

    public void setEchelle(@NotNull final Vector2D echelle) {
        this.echelle.setValue(echelle);
    }

    public ObjectProperty<Vector2D> origineProperty() {
        return origine;
    }

    public Vector2D getOrigine() {
        return origine.getValue();
    }

    public void setOrigine(@NotNull final Vector2D origine) {
        this.origine.setValue(origine);
    }

}
