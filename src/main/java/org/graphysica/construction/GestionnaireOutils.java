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
package org.graphysica.construction;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.graphysica.espace2d.Espace;
import org.graphysica.construction.outil.Outil;

/**
 *
 * @author Victor Babin
 * @author Marc-Antoine Ouimet
 */
public class GestionnaireOutils {

    /**
     * L'outil actif de ce gestionnaire d'outils.
     */
    @Nullable
    private Outil outilActif = null;

    /**
     * La construction de ce gestionnaire d'outils.
     */
    private final Construction construction;

    /**
     * Le gestionnaire de sélections de la construction.
     */
    private final GestionnaireSelections gestionnaireSelections;

    /**
     * L'événement de gestion de la pression de la souris sur les espaces.
     */
    private final GestionSouris pressionSouris = new GestionSouris();

    /**
     * L'événement de gestion du relâchement de la souris sur les espaces.
     */
    private final GestionSouris relachementSouris = new GestionSouris();

    /**
     * L'événement de gestion du mouvement de la souris sur les espaces.
     */
    private final GestionSouris mouvementSouris = new GestionSouris();

    public GestionnaireOutils(@NotNull final Construction construction,
            @NotNull final ObservableList<Espace> espaces) {
        this.construction = construction;
        this.gestionnaireSelections = construction.getGestionnaireSelections();
        espaces.addListener(changementEspaces);
        espaces.forEach((espace) -> {
            ajouterGestionOutils(espace);
        });
    }

    /**
     * L'événement d'actualisation de la liste des espaces.
     */
    private final ListChangeListener<Espace> changementEspaces = (@NotNull
            final ListChangeListener.Change<? extends Espace> changements) -> {
        while (changements.next()) {
            changements.getAddedSubList().stream().forEach((espace) -> {
                ajouterGestionOutils(espace);
            });
            changements.getRemoved().stream().forEach((espace) -> {
                retirerGestionOutils(espace);
            });
        }
    };

    /**
     * Ajoute les gestions d'outils sur un espace défini.
     *
     * @param espace l'espace sur lequel ajouter les gestions d'outils.
     */
    private void ajouterGestionOutils(@NotNull final Espace espace) {
        espace.addEventFilter(MouseEvent.MOUSE_PRESSED, pressionSouris);
        espace.addEventFilter(MouseEvent.MOUSE_RELEASED, relachementSouris);
        espace.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouvementSouris);

    }

    /**
     * Retire les gestions d'outils sur un espace défini.
     *
     * @param espace l'espace duquel retirer les gestions d'outils.
     */
    private void retirerGestionOutils(@NotNull final Espace espace) {
        espace.removeEventFilter(MouseEvent.MOUSE_PRESSED, pressionSouris);
        espace.removeEventFilter(MouseEvent.MOUSE_RELEASED, relachementSouris);
        espace.removeEventFilter(MouseEvent.MOUSE_DRAGGED, mouvementSouris);
    }

    public void finOutil() {
        if (outilActif != null) {
            setOutilActif(outilActif.dupliquer());
        }
    }

    public Construction getConstruction() {
        return construction;
    }

    public GestionnaireSelections getGestionnaireSelections() {
        return gestionnaireSelections;
    }

    public Outil getOutilActif() {
        return outilActif;
    }

    /**
     * Définit l'outil actif de ce gestionnaire d'outils. Permet à l'utilisateur
     * de sélectionner un outil parmi ceux de la barre d'outils.
     *
     * @param outilActif le nouvel outil actif.
     */
    public void setOutilActif(@NotNull final Outil outilActif) {
        this.outilActif = outilActif;
    }

    /**
     * La gestion de la souris.
     */
    private class GestionSouris implements EventHandler<MouseEvent> {

        @Override
        public void handle(@NotNull final MouseEvent evenement) {
            if (outilActif != null) {
                outilActif.gerer(evenement);
            }
        }

    }

}
