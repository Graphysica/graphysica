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
package org.graphysica.vue.barreoutils;

import com.sun.istack.internal.NotNull;
import javafx.scene.control.ToolBar;
import org.graphysica.construction.GestionnaireOutils;

/**
 * Une barre d'outils permet de sélectionner des outils pour interagir avec la
 * construction.
 *
 * @author Marc-Antoine Ouimet
 */
public final class BarreOutils extends ToolBar {

    /**
     * Le gestionnaire d'outils que contrôle cette barre d'outils.
     */
    private final GestionnaireOutils gestionnaireOutils;

    /**
     * Construit une barre d'outils sur un gestionnaire d'outils.
     *
     * @param gestionnaireOutils
     */
    public BarreOutils(@NotNull final GestionnaireOutils gestionnaireOutils) {
        this.gestionnaireOutils = gestionnaireOutils;
        assembler();
    }
    
    /**
     * Assemble la barre d'outils.
     */
    private void assembler() {
        getStyleClass().add("barre-outils");
        getItems().addAll(new GroupeSelection(gestionnaireOutils),
                new GroupePrimitif(gestionnaireOutils),
                new GroupeLignes(gestionnaireOutils));
    }

}
