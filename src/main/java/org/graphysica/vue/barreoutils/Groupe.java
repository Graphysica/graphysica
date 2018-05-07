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
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import org.graphysica.construction.GestionnaireOutils;
import org.graphysica.construction.outil.Outil;

/**
 * Un groupe permet de regrouper des items d'outil selon des catégories
 * d'utilisation. 
 *
 * @author Marc-Antoine Ouimet
 */
class Groupe extends MenuButton {

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
        getItems().addListener(changementItems);
    }

    {
        addEventFilter(MouseEvent.MOUSE_CLICKED, new ClicGroupe());
    }

    /**
     * L'événement d'actualisation de la liste des items du groupe.
     */
    private final ListChangeListener<MenuItem> changementItems = (@NotNull
            final ListChangeListener.Change<? extends MenuItem> changements)
            -> {
        while (changements.next()) {
            changements.getAddedSubList().stream().forEach((item) -> {
                ajouter(item);
            });
        }
    };

    /**
     * Ajoute un item au groupe.
     *
     * @param item l'item à ajouter.
     */
    private void ajouter(@NotNull final MenuItem item) {
        if (item instanceof Item) {
            final Item itemOutil = (Item) item;
            itemOutil.setOnAction(new SelectionItem(itemOutil));
        }
    }

    /**
     * Définit le dernier outil utilisé par ce groupe.
     *
     * @param item le dernier item ayant été utilisé dans ce groupe.
     */
    protected void definirDernierOutil(@NotNull final Item item) {
        dernierOutilUtilise = item.getOutil().dupliquer();
        setGraphic(item.affichageImage());
    }

    /**
     * Un clic sur le groupe définit l'outil actif comme étant le dernier outil
     * ayant été utilisé dans le groupe.
     */
    private class ClicGroupe implements EventHandler<MouseEvent> {

        @Override
        public void handle(@NotNull final MouseEvent evenement) {
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
