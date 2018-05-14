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
package org.graphysica.vue.inspecteur;

import com.sun.istack.internal.NotNull;
import javafx.collections.ObservableSet;
import javafx.geometry.Orientation;
import javafx.scene.layout.VBox;
import org.graphysica.construction.Element;

/**
 * Un inspecteur d'éléments permet de lister les détails d'édition de chacun de
 * ses éléments.
 *
 * @author Marc-Antoine Ouimet
 */
abstract class InspecteurElements extends VBox {

    /**
     * L'ensemble des éléments de la construction.
     */
    protected final ObservableSet<Element> elements;

    /**
     * Construit un inspecteur d'éléments sur un ensemble défini d'éléments de
     * la construction.
     *
     * @param elements les éléments à inspecter dans la construction.
     */
    public InspecteurElements(@NotNull final ObservableSet<Element> elements) {
        this.elements = elements;
        setFillWidth(true);
    }

}
