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
import com.sun.istack.internal.Nullable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.graphysica.espace2d.Espace;
import org.graphysica.espace2d.forme.Forme;
import org.graphysica.espace2d.forme.Grille;

/**
 * Le gestionnaire de sélections permet à l'utilisateur d'interagir avec la
 * construction. Une gestion de surbrillance assure une réponse graphique du
 * survol des éléments dans l'espace. Une gestion des sélections assure la
 * sélection ponctuelle et multiples d'éléments de la construction.
 *
 * @author Marc-Antoine Ouimet
 */
public final class GestionnaireSelections {

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
     * L'ensemble des formes présentement en surbrillance.
     */
    private final Set<Forme> formesEnSurbrillance = new HashSet<>();

    /**
     * La gestion de la surbrillance des éléments.
     */
    private final GestionSurbrillance gestionSurbrillance
            = new GestionSurbrillance();

    /**
     * La gestion des sélections d'éléments.
     */
    private final GestionSelections gestionSelections = new GestionSelections();

    public GestionnaireSelections(@NotNull final Set<Element> elements) {
        this.elements = elements;
    }

    public void ajouterEspace(@NotNull final Espace espace) {
        espaces.add(espace);
        espace.addEventFilter(MouseEvent.MOUSE_MOVED, gestionSurbrillance);
        espace.addEventFilter(MouseEvent.MOUSE_CLICKED, gestionSelections);
    }

    public Set<Element> getElementsSelectionnes() {
        return elementsSelectionnes;
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

        @Override
        public void handle(@NotNull final MouseEvent evenement) {
            for (final Espace espace : espaces) {
                final Set<Forme> formesSelectionnees
                        = espace.formesSurvolees();
                final Iterator<Forme> iteration
                        = formesEnSurbrillance.iterator();
                while (iteration.hasNext()) {
                    final Forme forme = iteration.next();
                    if (!formesSelectionnees.contains(forme)
                            && !elementsSelectionnesComprennentForme(forme)) {
                        forme.setEnSurvol(false);
                        iteration.remove();
                    }
                }
                formesEnSurbrillance.addAll(formesSelectionnees);
                formesSelectionnees.forEach((forme) -> {
                    forme.setEnSurvol(true);
                });
                for (final Forme forme : formesEnSurbrillance) {
                    if (!(forme instanceof Grille)) {
                        espace.setCursor(Cursor.HAND);
                        break;
                    }
                }
            }
        }

        /**
         * Détermine si une forme spécifiée figure parmi les formes des éléments
         * sélectionnés.
         *
         * @param forme la forme à vérifier.
         * @return {@code true} si un élément sélectionné est représenté par la
         * forme spécifiée.
         */
        private boolean elementsSelectionnesComprennentForme(
                @NotNull final Forme forme) {
            for (final Element element : elementsSelectionnes) {
                for (final Forme correspondante : element.getFormes()) {
                    if (forme == correspondante) {
                        return true;
                    }
                }
            }
            return false;
        }

    }

    /**
     * Une gestion de sélections permet de sélectionner des éléments à partir
     * des formes dans les espaces.
     */
    private class GestionSelections implements EventHandler<MouseEvent> {

        @Override
        public void handle(@NotNull final MouseEvent evenement) {
            if (evenement.getButton() == MouseButton.PRIMARY) {
                Element elementCorrespondant = null;
                for (final Espace espace : espaces) {
                    final Set<Forme> formesSelectionnees
                            = espace.formesSurvolees();
                    if (!formesSelectionnees.isEmpty()) {
                        final Forme formeSelectionnee = formesSelectionnees
                                .iterator().next();
                        elementCorrespondant = elementCorrespondant(
                                formeSelectionnee);
                        break;
                    }
                }
                if (evenement.isControlDown()) {
                    if (elementCorrespondant != null) {
                        if (elementsSelectionnes.contains(
                                elementCorrespondant)) {
                            elementsSelectionnes.remove(elementCorrespondant);
                        } else {
                            elementsSelectionnes.add(elementCorrespondant);
                        }
                    }
                } else {
                    deselectionner();
                    if (elementCorrespondant != null) {
                        elementsSelectionnes.add(elementCorrespondant);
                    }
                }
            }
        }

        /**
         * Récupère l'élément correspondant à une forme définie parmi les
         * éléments du gestionnaire de sélections.
         *
         * @param forme la forme dont on cherche l'élément.
         * @return l'élément associé à la forme ou {@code null} si aucun élément
         * n'est associé à la forme spécifiée.
         */
        @Nullable
        private Element elementCorrespondant(@NotNull final Forme forme) {
            for (final Element element : elements) {
                for (final Forme composantes : element.getFormes()) {
                    if (composantes == forme) {
                        return element;
                    }
                }
            }
            return null;
        }

        /**
         * Déselectionne l'ensemble des éléments sélectionnés et actualise
         * l'état de surbrillance de leurs formes.
         */
        private void deselectionner() {
            elementsSelectionnes.forEach((element) -> {
                for (final Forme forme : element.getFormes()) {
                    forme.setEnSurvol(false);
                    formesEnSurbrillance.remove(forme);
                }
            });
            elementsSelectionnes.clear();
        }

    }

}
