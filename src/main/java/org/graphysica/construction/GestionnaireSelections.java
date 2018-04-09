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
package org.graphysica.construction;

import com.sun.istack.internal.NotNull;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import org.graphysica.espace2d.Espace;
import org.graphysica.espace2d.forme.Forme;
import org.graphysica.espace2d.forme.Grille;

/**
 *
 * @author Marc-Antoine Ouimet
 */
public class GestionnaireSelections {

    /**
     * L'ensemble des espaces compris par ce gestionnaire de sélections.
     */
    private final Set<Espace> espaces = new HashSet<>();

    /**
     * L'ensemble des éléments à considérer dans ce gestionnaire de sélections.
     */
    private final Set<Element> elements;

    /**
     * L'ensemble des éléments sélectionnés en ordre de sélection.
     */
    private final Set<Element> elementsSelectionnes = new LinkedHashSet<>();

    /**
     * La gestion de la surbrillance des éléments.
     */
    private final GestionSurbrillance gererSurbrillance = new GestionSurbrillance();

    public GestionnaireSelections(@NotNull final Set<Element> elements) {
        this.elements = elements;
    }

    public void ajouterEspace(@NotNull final Espace espace) {
        espaces.add(espace);
        espace.addEventFilter(MouseEvent.MOUSE_MOVED, gererSurbrillance);
    }

    /**
     * Détermine si la sélection est vide.
     *
     * @return {@code true} si aucun élément n'est sélectionné.
     */
    public boolean selectionEstVide() {
        return elementsSelectionnes.isEmpty();
    }

    /**
     * Détermine si la sélection est unique.
     *
     * @return {@code true} si un et un seul élément est sélectionné parmi les
     * espaces.
     */
    public boolean selectionEstPonctuelle() {
        return elementsSelectionnes.size() == 1;
    }

    /**
     * Détermine si la sélection est multiple.
     *
     * @return {@code true} si plus d'un élément est sélectionné parmi les
     * espaces.
     */
    public boolean selectionEstMultiple() {
        return elementsSelectionnes.size() > 1;
    }

    /**
     * Une gestion de surbrillance s'occupe d'actualiser l'état de surbrillance
     * des formes parmi les espaces de ce gestionnaire de sélections.
     */
    private class GestionSurbrillance implements EventHandler<MouseEvent> {

        /**
         * L'ensemble des formes présentement en surbrillance.
         */
        private final Set<Forme> formesEnSurbrillance = new HashSet<>();

        @Override
        public void handle(@NotNull final MouseEvent evenement) {
            for (final Espace espace : espaces) {
                final Set<Forme> formesSelectionnees
                        = espace.formesSelectionnees();
                final Iterator<Forme> iteration
                        = formesEnSurbrillance.iterator();
                while (iteration.hasNext()) {
                    final Forme forme = iteration.next();
                    if (!formesSelectionnees.contains(forme)) {
                        forme.setEnSurbrillance(false);
                        iteration.remove();
                    }
                }
                formesEnSurbrillance.addAll(formesSelectionnees);
                formesSelectionnees.forEach((forme) -> {
                    forme.setEnSurbrillance(true);
                });
                for (final Forme forme : formesEnSurbrillance) {
                    if (!(forme instanceof Grille)) {
                        espace.setCursor(Cursor.HAND);
                        break;
                    }
                }
            }
        }

    }

}
