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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;

/**
 * Une toile permettant d'afficher un ensemble de formes.
 *
 * @author Marc-Antoine Ouimet
 */
public class ToileFormes extends ToileRedimensionnable {

    /**
     * Le contexte graphique de dessin de la toile.
     */
    private final GraphicsContext contexteGraphique = getGraphicsContext2D();

    /**
     * La liste observable des formes dessinées sur la toile.
     */
    private final ObservableList<Forme> formes
            = FXCollections.observableArrayList();

    public ToileFormes(final double largeur, final double hauteur) {
        super(largeur, hauteur);
    }

    @Override
    public void actualiser() {
        effacerAffichage();
    }

    private void effacerAffichage() {
        contexteGraphique.clearRect(0, 0, getWidth(), getHeight());
    }

}
