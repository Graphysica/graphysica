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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import org.graphysica.construction.Construction;

/**
 * Le menu de fichier du logiciel.
 *
 * @author Victor Babin
 * @author Marc-Antoine Ouimet
 */
final class MenuFichier extends Menu {

    /**
     * La construction du menu.
     */
    private final Construction construction;

    /**
     * Le menu de nouvelle construction.
     */
    private final MenuItem nouveau = new MenuItem("Nouveau");

    /**
     * Le menu de chargement d'une construction.
     */
    private final MenuItem ouvrir = new MenuItem("Ouvrir...");

    /**
     * Le menu d'enregistrement d'une construction.
     */
    private final MenuItem enregistrer = new MenuItem("Enregistrer");

    /**
     * Le menu d'enregistrement d'une construction sous un autre fichier.
     */
    private final MenuItem enregistrerSous
            = new MenuItem("Enregistrer sous...");

    /**
     * Le menu de fermeture du logiciel.
     */
    private final MenuItem fermer = new MenuItem("Fermer");

    /**
     * Construit un menu de fichier sur une construction définie.
     *
     * @param construction la construction gérée.
     */
    public MenuFichier(@NotNull final Construction construction) {
        this.construction = construction;
    }

    {
        setText("Fichier");
        nouveau.acceleratorProperty().setValue(
                new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        ouvrir.acceleratorProperty().setValue(
                new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        enregistrer.acceleratorProperty().setValue(
                new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        enregistrerSous.acceleratorProperty().setValue(
                new KeyCodeCombination(KeyCode.S, KeyCombination.SHIFT_DOWN,
                        KeyCombination.CONTROL_DOWN));
        fermer.acceleratorProperty().setValue(
                new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN));
        getItems().addAll(nouveau, ouvrir, new SeparatorMenuItem(),
                enregistrer, enregistrerSous, new SeparatorMenuItem(),
                fermer);
    }

}
