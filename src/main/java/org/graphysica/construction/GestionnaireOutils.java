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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableSet;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.graphysica.espace2d.Espace;
import org.graphysica.construction.outil.Outil;
import org.graphysica.construction.outil.OutilDeplacementElement;
import org.graphysica.util.SetChangeListener;

/**
 * Un gestionnaire d'outil gère les événements de la souris sur les espaces dans
 * un contexte d'outil actif spécifié. Les outils ont accès aux événements de
 * pression, de relâchement et de déplacement de la souris.
 *
 * @author Victor Babin
 * @author Marc-Antoine Ouimet
 */
public final class GestionnaireOutils {

    /**
     * Le gestionnaire des espaces de la construction gérée par ce gestionnaire
     * d'outils.
     */
    private final GestionnaireEspaces gestionnaireEspaces;

    /**
     * Le gestionnaire de commandes de la construction gérée par ce gestionnaire
     * d'outils.
     */
    private final GestionnaireCommandes gestionnaireCommandes;

    /**
     * Le gestionnaire de sélections de la construction gérée par ce
     * gestionnaire d'outils.
     */
    private final GestionnaireSelections gestionnaireSelections;

    /**
     * Les éléments gérés par ce gestionnaire d'outils.
     */
    private final ObservableSet<Element> elements;

    /**
     * L'outil actif de ce gestionnaire d'outils.
     */
    private final ObjectProperty<Outil> outilActif
            = new SimpleObjectProperty<>();

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

    /**
     * Construit un gestionnaire d'outils aux composantes définies.
     *
     * @param gestionnaireEspaces le gestionnaire des espaces sur la
     * construction.
     * @param gestionnaireCommandes le gestionnaire des commandes sur la
     * construction.
     * @param gestionnaireSelections le gestionnaire des sélections sur les
     * espaces de la construction.
     * @param espaces les espaces d'édition de la construction.
     * @param elements les éléments de la construcion.
     */
    GestionnaireOutils(
            @NotNull final GestionnaireEspaces gestionnaireEspaces,
            @NotNull final GestionnaireCommandes gestionnaireCommandes,
            @NotNull final GestionnaireSelections gestionnaireSelections,
            @NotNull final ObservableSet<Espace> espaces,
            @NotNull final ObservableSet<Element> elements) {
        espaces.addListener(new EspacesListener(espaces));
        this.gestionnaireEspaces = gestionnaireEspaces;
        this.gestionnaireCommandes = gestionnaireCommandes;
        this.gestionnaireSelections = gestionnaireSelections;
        this.elements = elements;
        outilActif.addListener((ObservableValue<? extends Outil> changement,
                final Outil ancienOutil, final Outil nouvelOutil) -> {
            if (ancienOutil != null && ancienOutil.isEnCours()) {
                ancienOutil.interrompre();
            }
        });
    }

    {
        setOutilActif(new OutilDeplacementElement(this));
    }

    /**
     * Interrompt l'outil actif.
     */
    public void interrompre() {
        if (getOutilActif().isEnCours()) {
            getOutilActif().interrompre();
            finOutil();
        }
    }

    /**
     * Met fin à l'utilisation de l'outil actif en le dupliquant.
     */
    public void finOutil() {
        if (aOutilActif()) {
            setOutilActif(getOutilActif().dupliquer());
        }
    }

    /**
     * Détermine si le gestionnaire d'outils a un outil actif.
     *
     * @return {@code true} s'il y a un outil actuellement actif.
     */
    public boolean aOutilActif() {
        return getOutilActif() != null;
    }

    public Outil getOutilActif() {
        return outilActif.getValue();
    }

    /**
     * Définit l'outil actif de ce gestionnaire d'outils. Permet à l'utilisateur
     * de sélectionner un outil parmi ceux de la barre d'outils.
     *
     * @param outilActif le nouvel outil actif.
     */
    public void setOutilActif(@NotNull final Outil outilActif) {
        this.outilActif.setValue(outilActif);
    }

    public ObjectProperty<Outil> outilActifProperty() {
        return outilActif;
    }

    public GestionnaireEspaces getGestionnaireEspaces() {
        return gestionnaireEspaces;
    }
    
    public GestionnaireSelections getGestionnaireSelections() {
        return gestionnaireSelections;
    }

    public GestionnaireCommandes getGestionnaireCommandes() {
        return gestionnaireCommandes;
    }

    public ObservableSet<Element> getElements() {
        return elements;
    }

    /**
     * La gestion de la souris.
     */
    private class GestionSouris implements EventHandler<MouseEvent> {

        @Override
        public void handle(@NotNull final MouseEvent evenement) {
            if (aOutilActif()) {
                getOutilActif().gerer(evenement);
            }
        }

    }

    /**
     * L'événement d'actualisation de l'ensemble des espaces. Ajoute et retire
     * les gestions de souris le cas échéant.
     */
    private class EspacesListener extends SetChangeListener<Espace> {

        /**
         * {@inheritDoc}
         */
        public EspacesListener(@NotNull final ObservableSet<Espace> elements) {
            super(elements);
        }

        @Override
        public void onAdd(@NotNull final Espace espace) {
            espace.addEventFilter(MouseEvent.MOUSE_PRESSED, pressionSouris);
            espace.addEventFilter(MouseEvent.MOUSE_RELEASED, relachementSouris);
            espace.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouvementSouris);
        }

        @Override
        public void onRemove(@NotNull final Espace espace) {
            espace.removeEventFilter(MouseEvent.MOUSE_PRESSED, pressionSouris);
            espace.removeEventFilter(MouseEvent.MOUSE_RELEASED,
                    relachementSouris);
            espace.removeEventFilter(MouseEvent.MOUSE_DRAGGED, mouvementSouris);
        }

    }

}
