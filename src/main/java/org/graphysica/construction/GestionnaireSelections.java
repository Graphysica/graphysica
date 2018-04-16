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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.graphysica.espace2d.Espace;
import org.graphysica.espace2d.forme.Forme;
import org.graphysica.espace2d.forme.Grille;

/**
 * Le gestionnaire de sélections permet à l'utilisateur d'interagir avec la
 * construction. Une gestion de surbrillance assure une réponse graphique du
 * survol des éléments dans l'espace. Une gestion des sélections assure la
 * sélection ponctuelle et multiples d'éléments de la construction.
 *
 * @author Marc-Antoine Ouimet
 */
public final class GestionnaireSelections {

    /**
     * L'ensemble des espaces compris par ce gestionnaire de sélections.
     */
    private final ObservableList<Espace> espaces;

    /**
     * L'ensemble des éléments à considérer dans ce gestionnaire de sélections.
     */
    private final Set<Element> elements;

    /**
     * L'ensemble des éléments sélectionnés en ordre de sélection.
     */
    private final Set<Element> elementsSelectionnes = new LinkedHashSet<>();

    /**
     * L'ensemble des formes présentement en surbrillance.
     */
    private final Set<Forme> formesEnSurbrillance = new HashSet<>();

    /**
     * L'association des espaces à leur gestion de survol de formes.
     */
    private final Map<Espace, GestionSurvol> gestionsSurvol = new HashMap<>();

    /**
     * L'association des espaces à leur gestion de sélection d'éléments.
     */
    private final Map<Espace, GestionSelection> gestionsSelection
            = new HashMap<>();

    /**
     * Construit un gestionnaire de sélections sur une liste d'espaces et un
     * ensemble d'éléments qui y sont représentés.
     *
     * @param espaces les espaces à gérer.
     * @param elements les éléments pouvant être sélectionnés.
     */
    public GestionnaireSelections(@NotNull final ObservableList<Espace> espaces,
            @NotNull final Set<Element> elements) {
        espaces.addListener(changementEspaces);
        for (final Espace espace : espaces) {
            ajouterGestionsSelection(espace);
        }
        this.espaces = espaces;
        this.elements = elements;
    }

    /**
     * L'événement d'actualisation de la liste des espaces.
     */
    private final ListChangeListener<Espace> changementEspaces
            = (@NotNull final ListChangeListener.Change<? extends Espace> changements) -> {
                while (changements.next()) {
                    changements.getAddedSubList().stream().forEach((espace) -> {
                        ajouterGestionsSelection(espace);
                    });
                    changements.getRemoved().stream().forEach((espace) -> {
                        retirerGestionsSelection(espace);
                    });
                }
            };

    /**
     * Ajoute des modules de gestion de sélection sur un espace défini.
     *
     * @param espace l'espace à gérer.
     */
    private void ajouterGestionsSelection(@NotNull final Espace espace) {
        final GestionSurvol gestionSurvol = new GestionSurvol(espace);
        espace.addEventFilter(MouseEvent.MOUSE_MOVED, gestionSurvol);
        gestionsSurvol.put(espace, gestionSurvol);
        final GestionSelection gestionSelection = new GestionSelection(espace);
        espace.addEventFilter(MouseEvent.MOUSE_CLICKED, gestionSelection);
        gestionsSelection.put(espace, gestionSelection);
    }

    /**
     * Retire les modules de gestion de sélection d'un espace défini.
     *
     * @param espace l'espace qui n'est plus gérer par le gestionnaire de
     * sélections.
     */
    private void retirerGestionsSelection(@NotNull final Espace espace) {
        espace.removeEventFilter(MouseEvent.MOUSE_MOVED,
                gestionsSurvol.remove(espace));
        espace.removeEventFilter(MouseEvent.MOUSE_CLICKED,
                gestionsSelection.remove(espace));
    }

    /**
     * Renvoie l'ensemble des éléments sélectionnés à travers les espaces du
     * gestionnaire de sélections.
     *
     * @return l'ensemble des éléments sélectionnés.
     */
    public Set<Element> getElementsSelectionnes() {
        return elementsSelectionnes;
    }

    /**
     * Détermine si la sélection est vide.
     *
     * @return {@code true} si aucun élément n'est sélectionné.
     */
    public boolean selectionEstVide() {
        return elementsSelectionnes.isEmpty();
    }

    /**
     * Détermine si la sélection est unique.
     *
     * @return {@code true} si un et un seul élément est sélectionné parmi les
     * espaces.
     */
    public boolean selectionEstPonctuelle() {
        return elementsSelectionnes.size() == 1;
    }

    /**
     * Détermine si la sélection est multiple.
     *
     * @return {@code true} si plus d'un élément est sélectionné parmi les
     * espaces.
     */
    public boolean selectionEstMultiple() {
        return elementsSelectionnes.size() > 1;
    }

    /**
     * Sélectionne un élément défini parmi les éléments du gestionnaire de
     * sélections.
     *
     * @param element l'élément à sélectionner.
     */
    public void selectionner(@NotNull final Element element) {
        if (elements.contains(element)) {
            if (elementsSelectionnes.add(element)) {
                for (final Forme forme : element.getFormes()) {
                    forme.setEnSurvol(true);
                }
            }
        }
    }

    /**
     * Sélectionne tous les éléments du gestionnaire de sélections.
     */
    public void toutSelectionner() {
        for (final Element element : elements) {
            selectionner(element);
        }
    }

    /**
     * Déselectionne un élément défini.
     *
     * @param element l'élément à désélectionner.
     */
    public void deselectionner(@NotNull final Element element) {
        if (elementsSelectionnes.remove(element)) {
            for (final Forme forme : element.getFormes()) {
                forme.setEnSurvol(false);
            }
        }
    }

    /**
     * Déselectionne l'ensemble des éléments sélectionnés et actualise l'état de
     * surbrillance de leurs formes.
     */
    public void toutDeselectionner() {
        for (final Element element : elementsSelectionnes) {
            for (final Forme forme : element.getFormes()) {
                forme.setEnSurvol(false);
            }
        }
        elementsSelectionnes.clear();
    }

    /**
     * Une gestion de survol s'occupe d'actualiser l'état de survol des formes
     * parmi les espaces de ce gestionnaire de sélections.
     */
    private class GestionSurvol implements EventHandler<MouseEvent> {

        /**
         * L'espace de cette gestion de survol.
         */
        private final Espace espace;

        /**
         * Construit une gestion de survol de formes sur un espace défini.
         *
         * @param espace l'espace à gérer.
         */
        public GestionSurvol(@NotNull final Espace espace) {
            this.espace = espace;
        }

        @Override
        public void handle(@NotNull final MouseEvent evenement) {
            final Set<Forme> formesSelectionnees
                    = espace.formesSurvolees();
            final Iterator<Forme> iteration
                    = formesEnSurbrillance.iterator();
            while (iteration.hasNext()) {
                final Forme forme = iteration.next();
                if (!formesSelectionnees.contains(forme)
                        && !elementsSelectionnesComprennentForme(forme)) {
                    forme.setEnSurvol(false);
                    iteration.remove();
                }
            }
            formesEnSurbrillance.addAll(formesSelectionnees);
            formesSelectionnees.forEach((forme) -> {
                forme.setEnSurvol(true);
            });
            for (final Forme forme : formesEnSurbrillance) {
                if (!(forme instanceof Grille)) {
                    espace.setCursor(Cursor.HAND);
                    break;
                }
            }
        }

        /**
         * Détermine si une forme spécifiée figure parmi les formes des éléments
         * sélectionnés.
         *
         * @param forme la forme à vérifier.
         * @return {@code true} si un élément sélectionné est représenté par la
         * forme spécifiée.
         */
        private boolean elementsSelectionnesComprennentForme(
                @NotNull final Forme forme) {
            for (final Element element : elementsSelectionnes) {
                for (final Forme correspondante : element.getFormes()) {
                    if (forme == correspondante) {
                        return true;
                    }
                }
            }
            return false;
        }

    }

    /**
     * Une gestion de sélection permet de sélectionner des éléments à partir des
     * formes dans les espaces.
     */
    private class GestionSelection implements EventHandler<MouseEvent> {

        /**
         * L'espace de cette gestion de sélection.
         */
        private final Espace espace;

        /**
         * Construit une gestion de sélection sur un espace défini.
         *
         * @param espace l'espace à gérer.
         */
        public GestionSelection(@NotNull final Espace espace) {
            this.espace = espace;
        }

        @Override
        public void handle(@NotNull final MouseEvent evenement) {
            if (evenement.getButton() == MouseButton.PRIMARY) {
                Element elementCorrespondant = null;
                final Set<Forme> formesSurvolees
                        = espace.formesSurvolees();
                if (!formesSurvolees.isEmpty()) {
                    final Forme formeSelectionnee = formesSurvolees
                            .iterator().next();
                    elementCorrespondant = elementCorrespondant(
                            formeSelectionnee);
                }
                if (evenement.isControlDown()) {
                    if (elementCorrespondant != null) {
                        if (elementsSelectionnes.contains(
                                elementCorrespondant)) {
                            deselectionner(elementCorrespondant);
                        } else {
                            selectionner(elementCorrespondant);
                        }
                    }
                } else {
                    toutDeselectionner();
                    if (elementCorrespondant != null) {
                        selectionner(elementCorrespondant);
                    }
                }
            }
        }

        /**
         * Récupère l'élément correspondant à une forme définie parmi les
         * éléments du gestionnaire de sélections.
         *
         * @param forme la forme dont on cherche l'élément.
         * @return l'élément associé à la forme ou {@code null} si aucun élément
         * n'est associé à la forme spécifiée.
         */
        @Nullable
        private Element elementCorrespondant(@NotNull final Forme forme) {
            for (final Element element : elements) {
                for (final Forme composantes : element.getFormes()) {
                    if (composantes == forme) {
                        return element;
                    }
                }
            }
            return null;
        }

    }

}