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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import org.graphysica.construction.Construction;

/**
 * Le menu d'édition permet d'éditer les éléments et les commandes de la
 * construction.
 *
 * @author Victor Babin
 * @author Marc-Antoine Ouimet
 */
final class MenuEdition extends Menu {

    /**
     * La construction du menu.
     */
    private final Construction construction;

    /**
     * Le menu des propriétés des éléments sélectionnés.
     */
    private final MenuItem proprietes = new MenuItem("Proprietes");

    /**
     * Le menu d'annulation de la dernière commande exécutée dans le
     * gestionnaire des commandes.
     */
    private final MenuItem annuler = new MenuItem("Annuler");

    /**
     * Le menu de réexécution de la dernière commande annulée dans le
     * gestionnaire de commandes.
     */
    private final MenuItem refaire = new MenuItem("Refaire");

    /**
     * Le menu de copie des éléments sélectionnés.
     */
    private final MenuItem copier = new MenuItem("Copier");

    /**
     * Le menu de collage des éléments copiés.
     */
    private final MenuItem coller = new MenuItem("Coller");

    /**
     * Le menu de sélection de tous les éléments de la construction.
     */
    private final MenuItem toutSelectionner = new MenuItem("Tout sélectionner");

    /**
     * Le menu de désélection de tous les éléments de la construction.
     */
    private final MenuItem toutDeselectionner
            = new MenuItem("Tout désélectionner");

    /**
     * Construit un menu d'édition sur une construction définie.
     *
     * @param construction la construction gérée.
     */
    public MenuEdition(@NotNull final Construction construction) {
        this.construction = construction;
    }

    {
        setText("Édition");
        proprietes.acceleratorProperty().setValue(
                new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN));
        annuler.acceleratorProperty().setValue(
                new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        annuler.setOnAction(new Annuler());
        refaire.acceleratorProperty().setValue(
                new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
        refaire.setOnAction(new Refaire());
        copier.acceleratorProperty().setValue(
                new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        coller.acceleratorProperty().setValue(
                new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
        toutSelectionner.acceleratorProperty().setValue(
                new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
        toutSelectionner.setOnAction(new ToutSelectionner());
        toutDeselectionner.acceleratorProperty().setValue(
                new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN,
                        KeyCombination.SHIFT_DOWN));
        toutDeselectionner.setOnAction(new ToutDeselectionner());
        getItems().addAll(proprietes, new SeparatorMenuItem(),
                annuler, refaire, new SeparatorMenuItem(),
                copier, coller, new SeparatorMenuItem(),
                toutSelectionner, toutDeselectionner);
    }

    /**
     * L'événement d'annulation de la dernière commande exécutée sur la
     * construction.
     */
    private class Annuler implements EventHandler<ActionEvent> {

        @Override
        public void handle(@NotNull final ActionEvent evenement) {
            construction.getGestionnaireOutils().interrompre();
            construction.getGestionnaireCommandes().annuler();
        }

    }

    /**
     * L'événement de réexécution de la dernière commande annulée de la
     * construction.
     */
    private class Refaire implements EventHandler<ActionEvent> {

        @Override
        public void handle(@NotNull final ActionEvent evenement) {
            construction.getGestionnaireOutils().interrompre();
            construction.getGestionnaireCommandes().refaire();
        }

    }

    /**
     * L'événement de sélection de tous les éléments de la construction.
     */
    private class ToutSelectionner implements EventHandler<ActionEvent> {

        @Override
        public void handle(@NotNull final ActionEvent evenement) {
            construction.getGestionnaireOutils().interrompre();
            construction.getGestionnaireSelections().toutSelectionner();
        }

    }

    /**
     * L'événement de désélection de tous les éléments de la construction.
     */
    private class ToutDeselectionner implements EventHandler<ActionEvent> {

        @Override
        public void handle(@NotNull final ActionEvent evenement) {
            construction.getGestionnaireOutils().interrompre();
            construction.getGestionnaireSelections().toutDeselectionner();
        }

    }

}
