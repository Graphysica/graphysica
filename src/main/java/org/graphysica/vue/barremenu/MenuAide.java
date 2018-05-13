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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import org.graphysica.construction.Construction;

/**
 * Le menu d'aide du logiciel.
 *
 * @author Victor Babin
 * @author Marc-Antoine Ouimet
 */
final class MenuAide extends Menu {

    /**
     * La construction du menu.
     */
    private final Construction construction;

    /**
     * Le menu d'affichage du manuel du logiciel.
     */
    private final MenuItem manuel = new MenuItem("Manuel");

    /**
     * Le menu d'affichage de la licence du logiciel.
     */
    private final MenuItem licence = new MenuItem("Licence");

    /**
     * Le menu d'affichage des informations à propos du logiciel.
     */
    private final MenuItem aPropos = new MenuItem("À propos");

    /**
     * Construit un menu d'aide sur une construction définie.
     *
     * @param construction la constructoin gérée.
     */
    public MenuAide(@NotNull final Construction construction) {
        this.construction = construction;
    }

    {
        setText("Aide");
        manuel.acceleratorProperty().setValue(
                new KeyCodeCombination(KeyCode.F1));
        getItems().addAll(manuel, licence, aPropos);
    }

}
