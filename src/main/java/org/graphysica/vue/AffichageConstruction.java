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
package org.graphysica.vue;

import com.sun.istack.internal.NotNull;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.graphysica.construction.Construction;
import org.graphysica.espace2d.Espace;
import org.graphysica.vue.barreoutils.BarreOutils;

/**
 * Un affichage de construction permet d'afficher et d'interagir avec une
 * construction.
 *
 * @author Marc-Antoine Ouimet
 * @see Construction
 */
public class AffichageConstruction extends BorderPane {

    /**
     * La construction affichée.
     */
    private final Construction construction;

    /**
     * Construit et assemble un affichage de construction sur une construction
     * définie.
     *
     * @param construction la construction affichée.
     */
    public AffichageConstruction(@NotNull final Construction construction) {
        this.construction = construction;
        assembler();
    }

    /**
     * Assemble l'affichage de la construction.
     */
    private void assembler() {
        final BarreOutils barreOutils = new BarreOutils(
                construction.getGestionnaireOutils());
        setTop(barreOutils);
        setCenter(new AffichageEspaces());
    }

    /**
     * Un affichage d'espaces permet d'afficher des espaces liés d'une
     * construction.
     */
    private class AffichageEspaces extends SplitPane {

        /**
         * L'association des espaces à leur panneau parent.
         */
        private final Map<Espace, Pane> panneaux = new HashMap<>();

        /**
         * Initialise un affichage d'espaces sur la construction de l'affichage
         * de construction.
         */
        public AffichageEspaces() {
            final ObservableList<Espace> espaces = construction.getEspaces();
            espaces.addListener(changementEspaces);
            espaces.forEach((espace) -> {
                ajouter(espace);
            });
        }

        /**
         * L'événement de modification de la liste des espaces de la
         * construction. Permet d'ajouter à l'affichage des espaces un nouveau
         * panneau lié aux dimensions de l'espace dupliqué.
         */
        private final ListChangeListener<Espace> changementEspaces = (@NotNull
                final ListChangeListener.Change<? extends Espace> changements) -> {
            while (changements.next()) {
                changements.getAddedSubList().stream().forEach((espace) -> {
                    ajouter(espace);
                });
                changements.getRemoved().stream().forEach((espace) -> {
                    retirer(espace);
                });
            }
        };

        /**
         * Ajoute un espace à cet affichage d'espaces.
         *
         * @param espace l'espace à ajouter.
         */
        private void ajouter(@NotNull final Espace espace) {
            final Pane panneau = new Pane(espace);
            panneaux.put(espace, panneau);
            espace.widthProperty().bind(panneau.widthProperty());
            espace.heightProperty().bind(panneau.heightProperty());
            getItems().add(panneau);
        }

        /**
         * Retire un espace de cet affichage d'espaces.
         *
         * @param espace l'espace à retirer.
         */
        private void retirer(@NotNull final Espace espace) {
            getItems().remove(panneaux.remove(espace));
        }
    }

}
