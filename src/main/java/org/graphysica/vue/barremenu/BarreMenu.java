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
import javafx.scene.control.MenuBar;
import org.graphysica.construction.Construction;
import org.graphysica.vue.inspecteur.Inspecteur;

/**
 * Une barre de menu permet d'éditer une construction.
 *
 * @author Victor Babin
 * @author Marc-Antoine Ouimet
 */
public final class BarreMenu extends MenuBar {

    /**
     * Construit une barre de menu sur une construction définie.
     *
     * @param construction la construction gérée.
     * @param inspecteur l'inspecteur des éléments de la construction.
     */
    public BarreMenu(@NotNull final Construction construction, 
            @NotNull final Inspecteur inspecteur) {
        getMenus().addAll(new MenuFichier(construction),
                new MenuEdition(construction),
                new MenuAffichage(construction, inspecteur),
                new MenuAide(construction));
    }

}
