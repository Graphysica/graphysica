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
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import org.graphysica.construction.GestionnaireOutils;
import org.graphysica.construction.outil.Outil;
import org.graphysica.util.ListChangeListener;
import org.graphysica.util.ToggleSplitMenuButton;

/**
 * Un groupe permet de regrouper des items d'outil selon des catégories
 * d'utilisation.
 *
 * @author Marc-Antoine Ouimet
 */
abstract class Groupe extends ToggleSplitMenuButton {

    /**
     * Le gestionnaire d'outils de ce groupe d'outils.
     */
    protected final GestionnaireOutils gestionnaireOutils;

    /**
     * Le denier outil utilisé dans ce groupe d'outils.
     */
    @NotNull
    protected Outil dernierOutilUtilise;

    /**
     * Crée un groupe ayant un item d'outil défini.
     *
     * @param gestionnaireOutils le gestionnaire d'outils.
     * @param item l'item initial du groupe.
     */
    public Groupe(@NotNull final GestionnaireOutils gestionnaireOutils) {
        this.gestionnaireOutils = gestionnaireOutils;
        getItems().addListener(new ItemsListener());
    }

    {
        setOnAction(new ClicGroupe());
    }

    /**
     * Définit le dernier outil utilisé dans ce groupe.
     *
     * @param item le dernier item ayant été utilisé dans ce groupe.
     */
    protected void definirDernierOutil(@NotNull final Item item) {
        dernierOutilUtilise = item.getOutil().dupliquer();
    }

    /**
     * L'événement d'actualisation de la liste des items du groupe.
     */
    private class ItemsListener extends ListChangeListener<MenuItem> {

        @Override
        public void onAdd(@NotNull final MenuItem item) {
            if (item instanceof Item) {
                final Item itemOutil = (Item) item;
                itemOutil.setOnAction(new SelectionItem(itemOutil));
                if (getItems().size() == 1) {
                    definirDernierOutil(itemOutil);
                    setGraphic(itemOutil.affichageImage());
                }
            }
        }

        @Override
        public void onRemove(@NotNull final MenuItem element) {
        }

    }

    /**
     * Un clic sur le groupe définit l'outil actif comme étant le dernier outil
     * ayant été utilisé dans le groupe.
     */
    private class ClicGroupe implements EventHandler {

        @Override
        public void handle(@NotNull final Event evenement) {
            gestionnaireOutils.setOutilActif(dernierOutilUtilise.dupliquer());
        }

    }

    /**
     * Une sélection sur un item du groupe modifie l'outil actif du gestionnaire
     * d'outil et redéfinit le dernier outil utilisé parmi ceux du groupe.
     */
    private class SelectionItem implements EventHandler<ActionEvent> {

        /**
         * L'item sélectionné.
         */
        private final Item item;

        /**
         * Crée une gestion de sélection d'item sur un item défini.
         *
         * @param item l'item dont on gère la sélection.
         */
        public SelectionItem(@NotNull final Item item) {
            this.item = item;
        }

        @Override
        public void handle(@NotNull final ActionEvent evenement) {
            gestionnaireOutils.setOutilActif(item.getOutil().dupliquer());
            definirDernierOutil(item);
        }

    }

}
