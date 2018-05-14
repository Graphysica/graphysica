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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
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
        objetsMathematiques.addListener(new ObjetsMathematiquesListener(
                objetsMathematiques));
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

    /**
     * L'événement de changement des objets mathématiques de l'inspecteur.
     */
    private class ObjetsMathematiquesListener
            extends SetChangeListener<ObjetMathematique> {

        /**
         * L'association des panneaux d'information aux objets mathématiques.
         */
        private final Map<ObjetMathematique, Node> informations
                = new HashMap<>();

        /**
         * {@inheritDoc}
         */
        public ObjetsMathematiquesListener(
                @NotNull final ObservableSet<ObjetMathematique> elements) {
            super(elements);
        }

        @Override
        public void onAdd(@NotNull final ObjetMathematique element) {
            final Node panneau = panneauInformations(element);
            informations.put(element, panneau);
            getChildren().add(panneau);
        }

        /**
         * Crée un panneau d'informations d'un élément défini.
         *
         * @param element l'élément représenté.
         * @return la panneau d'informations généré.
         */
        private Node panneauInformations(
                @NotNull final ObjetMathematique element) {
            final TitledPane panneau = new TitledPane();
            panneau.setAnimated(false);
            panneau.setExpanded(false);
            panneau.setText("Élément #" + element.getId());
            panneau.setContent(contenuInformations(element));
            return panneau;
        }

        /**
         * Crée le contenu d'un panneau d'informations d'un élément défini.
         *
         * @param element l'élément représenté.
         * @return le contenu des contrôles d'édition des informations de
         * l'élément.
         */
        private VBox contenuInformations(
                @NotNull final ObjetMathematique element) {
            final VBox contenu = new VBox();
            final CheckBox affiche = new CheckBox("Affiché");
            affiche.selectedProperty().bindBidirectional(
                    element.afficheProperty());
            contenu.getChildren().add(affiche);
            final ColorPicker couleur = new ColorPicker();
            couleur.valueProperty().bindBidirectional(
                    element.couleurProperty());
            contenu.getChildren().add(couleur);
            return contenu;
        }

        @Override
        public void onRemove(@NotNull final ObjetMathematique element) {
            getChildren().remove(informations.remove(element));
        }

    }

}
