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
package org.graphysica.vue.barremenu;

import com.sun.istack.internal.NotNull;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import org.graphysica.construction.Construction;
import org.graphysica.espace2d.Espace;
import org.graphysica.vue.inspecteur.Inspecteur;

/**
 * Le menu d'affichage des fenêtres de la construction.
 *
 * @author Victor Babin
 * @author Marc-Antoine Ouimet
 */
final class MenuAffichage extends Menu {

    /**
     * La construction du menu.
     */
    private final Construction construction;
    
    /**
     * L'inspecteur des éléments de la construction.
     */
    private final Inspecteur inspecteur;
    
    /**
     * Le menu d'affichage de l'inspecteur d'éléments.
     */
    private final CheckMenuItem afficherInspecteur 
            = new CheckMenuItem("Inspecteur");

    /**
     * Le menu d'affichage du deuxième espace de la construction.
     */
    private final CheckMenuItem afficherDeuxiemeEspace
            = new CheckMenuItem("Deuxième espace");

    /**
     * Construit un menu d'affichage sur une construction définie.
     *
     * @param construction la construction gérée.
     * @param inspecteur l'inspecteur des éléments de la construction.
     */
    public MenuAffichage(@NotNull final Construction construction, 
            @NotNull final Inspecteur inspecteur) {
        this.construction = construction;
        this.inspecteur = inspecteur;
        afficherInspecteur.selectedProperty()
                .bindBidirectional(inspecteur.afficheProperty());
    }

    {
        setText("Affichage");
        afficherInspecteur.acceleratorProperty().setValue(
                new KeyCodeCombination(KeyCode.F1, KeyCombination.SHIFT_DOWN));
        afficherDeuxiemeEspace.acceleratorProperty().setValue(
                new KeyCodeCombination(KeyCode.F2, KeyCombination.SHIFT_DOWN));
        afficherDeuxiemeEspace.setOnAction(new AfficherDeuxiemeEspace());
        afficherInspecteur.setSelected(true);
        getItems().addAll(afficherInspecteur, afficherDeuxiemeEspace);
    }

    /**
     * L'événement d'affichage du deuxième espace de la construction.
     */
    private class AfficherDeuxiemeEspace implements EventHandler<ActionEvent> {

        /**
         * L'espace ajouté.
         */
        private Espace espace;

        @Override
        public void handle(@NotNull final ActionEvent evenement) {
            if (afficherDeuxiemeEspace.isSelected()) {
                espace = new Espace();
                construction.getEspaces().add(espace);
            } else {
                construction.getEspaces().remove(espace);
            }
        }

    }

}
