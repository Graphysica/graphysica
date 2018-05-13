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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import org.graphysica.Application;
import org.graphysica.construction.Construction;

/**
 * Le menu d'aide du logiciel.
 *
 * @author Victor Babin
 * @author Marc-Antoine Ouimet
 */
final class MenuAide extends Menu {

    /**
     * L'URL vers le manuel du logiciel.
     */
    private static final String URL_MANUEL
            = "https://github.com/Graphysica/graphysica/wiki";

    /**
     * L'URL vers la licence du logiciel.
     */
    private static final String URL_LICENCE
            = "https://www.gnu.org/licenses/gpl-3.0.fr.html";

    /**
     * L'URL vers l'à propos du logiciel.
     */
    private static final String URL_A_PROPOS
            = "https://github.com/Graphysica/graphysica";

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
        manuel.setOnAction(new OuvrirLien(URL_MANUEL));
        aPropos.setOnAction(new OuvrirLien(URL_A_PROPOS));
        licence.setOnAction(new OuvrirLien(URL_LICENCE));
        manuel.acceleratorProperty().setValue(
                new KeyCodeCombination(KeyCode.F1));
        getItems().addAll(manuel, licence, aPropos);
    }

    /**
     * L'événement d'ouverture d'un lien à partir du fureteur de l'utilisateur.
     */
    private static class OuvrirLien implements EventHandler<ActionEvent> {
        
        /**
         * L'URL à visiter.
         */
        private final String url;

        /**
         * Construit un événement d'ouverture de lien vers un lien défini.
         * @param url le lien à ouvrir.
         */
        public OuvrirLien(@NotNull final String url) {
            this.url = url;
        }
        
        @Override
        public void handle(@NotNull final ActionEvent evenement) {
            Application.getInstance().getHostServices().showDocument(url);
        }

    }

}
