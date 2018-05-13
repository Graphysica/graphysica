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
import java.util.HashSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import org.graphysica.construction.Element;
import org.graphysica.construction.mathematiques.ObjetMathematique;
import org.graphysica.util.SetChangeListener;

/**
 * Un inspecteur de mathématique permet d'éditer les détails des objets
 * mathématiques de la construction.
 *
 * @author Marc-Antoine Ouimet
 */
final class InspecteurMathematique extends InspecteurElements {

    /**
     * L'ensemble des objets mathématiques parmi les éléments de la
     * construction.
     */
    private final ObservableSet<ObjetMathematique> objetsMathematiques
            = FXCollections.observableSet(new HashSet<>());

    /**
     * Construit un inspecteur de mathématique sur un ensemble d'éléments de la
     * construction.
     *
     * @param elements les éléments à inspecter de la construction.
     */
    public InspecteurMathematique(
            @NotNull final ObservableSet<Element> elements) {
        super(elements);
        elements.addListener(new ElementsListener(elements));
    }

    /**
     * L'événement de changement des éléments de la construction.
     */
    private class ElementsListener extends SetChangeListener<Element> {
        
        /**
         * {@inheritDoc}
         */
        public ElementsListener(
                @NotNull final ObservableSet<Element> elements) {
            super(elements);
        }

        @Override
        public void onAdd(@NotNull final Element element) {
            if (element instanceof ObjetMathematique) {
                objetsMathematiques.add((ObjetMathematique) element);
            }
        }

        @Override
        public void onRemove(@NotNull final Element element) {
            if (element instanceof ObjetMathematique) {
                objetsMathematiques.remove((ObjetMathematique) element);
            }
        }
        
    }

}
