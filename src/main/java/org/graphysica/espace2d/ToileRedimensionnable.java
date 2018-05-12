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

import javafx.beans.InvalidationListener;
import javafx.scene.canvas.Canvas;

/**
 * Une toile redimensionnable est actualisée lorsqu'elle est redimensionnée. Il
 * faut lier les dimensions de la toile aux dimensions du panneau parent.
 * <pre>
 *      ToileRedimensionnable toile = ...;
 *      AnchorPane panneau = new AnchorPane(toile);
 *      toile.widthProperty().bind(panneau.widthProperty());
 *      toile.heightProperty().bind(panneau.heightProperty());
 * </pre>
 *
 * @author Marc-Antoine Ouimet
 */
abstract class ToileRedimensionnable extends Canvas
        implements Actualisable {

    /**
     * L'événement d'actualisation de la toile redimmensionable.
     */
    protected final InvalidationListener evenementActualisation = (observable)
            -> {
        actualiser();
    };

    /**
     * Construit une toile redimensionnable.
     */
    public ToileRedimensionnable() {
    }

    /**
     * Construit une toile redimensionnable aux dimensions définies.
     *
     * @param largeur la largeur initiale de la toile.
     * @param hauteur la hauteur initiale de la toile.
     */
    public ToileRedimensionnable(final double largeur, final double hauteur) {
        super(largeur, hauteur);
    }

    {
        //Traiter la redimension de la toile
        widthProperty().addListener(evenementActualisation);
        heightProperty().addListener(evenementActualisation);
    }

    @Override
    public final boolean isResizable() {
        return true;
    }

    @Override
    public final double prefHeight(final double hauteur) {
        return getHeight();
    }

    @Override
    public final double prefWidth(final double largeur) {
        return getWidth();
    }

}
