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
import javafx.scene.layout.AnchorPane;

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
    
    public void ajouter(@NotNull final Forme forme) {
        toile.ajouter(forme);
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

}
