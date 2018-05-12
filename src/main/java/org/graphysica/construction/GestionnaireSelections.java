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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableSet;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Espace;
import org.graphysica.espace2d.forme.Forme;
import org.graphysica.espace2d.position.PositionReelle;
import org.graphysica.espace2d.position.PositionVirtuelle;
import org.graphysica.util.SetChangeListener;

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
     * Le gestionnaire d'espaces de ce gestionnaire de sélections.
     */
    private final GestionnaireEspaces gestionnaireEspaces;

    /**
     * L'ensemble des éléments à considérer dans ce gestionnaire de sélections.
     */
    private final Collection<Element> elements;

    /**
     * L'ensemble des éléments sélectionnés en ordre de sélection.
     */
    private final LinkedHashSet<Element> elementsSelectionnes
            = new LinkedHashSet<>();

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
     * @param gestionnaireEspaces le gestionnaire des espaces.
     * @param espaces les espaces à gérer.
     * @param elements les éléments pouvant être sélectionnés.
     */
    GestionnaireSelections(
            @NotNull final GestionnaireEspaces gestionnaireEspaces,
            @NotNull final ObservableSet<Espace> espaces,
            @NotNull final Collection<Element> elements) {
        espaces.addListener(new EspacesListener(espaces));
        this.gestionnaireEspaces = gestionnaireEspaces;
        this.elements = elements;
    }

    /**
     * Récupère l'élément correspondant à une forme définie parmi les éléments
     * du gestionnaire de sélections.
     *
     * @param forme la forme dont on cherche l'élément.
     * @return l'élément associé à la forme ou {@code null} si aucun élément
     * n'est associé à la forme spécifiée.
     */
    @Nullable
    private Element elementCorrespondant(@NotNull final Forme forme) {
        for (final Element element : elements) {
            for (final Forme composante : element.getFormes()) {
                if (composante == forme) {
                    return element;
                }
            }
        }
        return null;
    }

    /**
     * Renvoie l'ensemble des éléments sélectionnés à travers les espaces du
     * gestionnaire de sélections.
     *
     * @return l'ensemble des éléments sélectionnés.
     */
    public LinkedHashSet<Element> getElementsSelectionnes() {
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
     * Récupère l'ensemble des éléments survolés sur l'espace actif en ordre de
     * distance.
     *
     * @return l'ensemble des éléments survolés sur l'espace actif.
     */
    public LinkedHashSet<Element> getElementsSurvoles() {
        final LinkedHashSet<Element> elementsSurvoles = new LinkedHashSet<>();
        final LinkedHashSet<Forme> formesSurvolees
                = gestionnaireEspaces.espaceActif().formesSurvolees();
        for (final Forme forme : formesSurvolees) {
            elementsSurvoles.add(elementCorrespondant(forme));
        }
        return elementsSurvoles;
    }

    /**
     * Détermine si le survol est vide.
     *
     * @return {@code true} si aucun élément n'est survolé.
     */
    public boolean survolEstVide() {
        return getElementsSurvoles().isEmpty();
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
     * Récupère la propriété de position actuelle du curseur sur l'espace actif.
     * Si le curseur quitte l'espace actif, cette position sera fixe à la
     * dernière position enregistrée sur l'espace précédent.
     *
     * @return la propriété de position actuelle du curseur parmi les espaces.
     */
    public ObjectProperty<PositionReelle> positionCurseurProperty() {
        return gestionnaireEspaces.positionCurseurProperty();
    }

    /**
     * Récupère la position actuelle réelle du curseur sur l'espace actif.
     *
     * @return la position actuelle réelle du curseur parmi les espaces.
     */
    public PositionReelle positionReelleCurseur() {
        return gestionnaireEspaces.espaceActif().getPositionReelleCurseur();
    }

    /**
     * Récupère la position actuelle virtuelle du curseur sur l'espace actif.
     *
     * @return la position actuelle virtuelle du curseur parmi les espaces.
     */
    public PositionVirtuelle positionVirtuelleCurseur() {
        return gestionnaireEspaces.espaceActif().getPositionVirtuelleCurseur();
    }

    /**
     * Récupère le déplacement réel du curseur sur l'espace actif. Ce
     * déplacement permet de déplacer des éléments dans un espace.
     *
     * @return le déplacement réel du curseur parmi les espaces.
     */
    public Vector2D deplacementReelCurseur() {
        return gestionnaireEspaces.espaceActif().getDeplacementReelCurseur();
    }

    /**
     * Une gestion sur un espace.
     */
    private abstract class Gestion implements EventHandler<MouseEvent> {

        /**
         * L'espace de cette gestion.
         */
        private final Espace espace;

        /**
         * Construit une gestion sur un espace défini.
         *
         * @param espace l'espace à gérer.
         */
        public Gestion(@NotNull final Espace espace) {
            this.espace = espace;
        }

        public Espace getEspace() {
            return espace;
        }

    }

    /**
     * Une gestion de survol s'occupe d'actualiser l'état de survol des formes
     * parmi les espaces de ce gestionnaire de sélections.
     */
    private class GestionSurvol extends Gestion {

        /**
         * Construit une gestion de survol de formes sur un espace défini.
         *
         * @param espace l'espace à gérer.
         */
        public GestionSurvol(@NotNull final Espace espace) {
            super(espace);
        }

        @Override
        public void handle(@NotNull final MouseEvent evenement) {
            final Set<Forme> formesSurvolees
                    = getEspace().formesSurvolees();
            if (!formesSurvolees.isEmpty()) {
                getEspace().setCursor(Cursor.HAND);
            } else {
                getEspace().setCursor(Cursor.CROSSHAIR);
            }
            final Iterator<Forme> iteration
                    = formesEnSurbrillance.iterator();
            while (iteration.hasNext()) {
                final Forme forme = iteration.next();
                if (!formesSurvolees.contains(forme)
                        && !elementsSelectionnesComprennentForme(forme)) {
                    forme.setEnSurvol(false);
                    iteration.remove();
                }
            }
            formesEnSurbrillance.addAll(formesSurvolees);
            formesSurvolees.forEach((forme) -> {
                forme.setEnSurvol(true);
            });
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
     * formes dans un espace.
     */
    private class GestionSelection extends Gestion {

        /**
         * Construit une gestion de sélection sur un espace défini.
         *
         * @param espace l'espace à gérer.
         */
        public GestionSelection(@NotNull final Espace espace) {
            super(espace);
        }

        @Override
        public void handle(@NotNull final MouseEvent evenement) {
            if (evenement.getButton() == MouseButton.PRIMARY) {
                Element elementCorrespondant = null;
                final LinkedHashSet<Forme> formesSurvolees
                        = getEspace().formesSurvolees();
                if (!formesSurvolees.isEmpty()) {
                    final Forme formeSelectionnee = formesSurvolees
                            .iterator().next();
                    elementCorrespondant = elementCorrespondant(
                            formeSelectionnee);
                }
                actualiserSelections(elementCorrespondant,
                        evenement.isControlDown());
            }
        }

        /**
         * Actualise les sélections.
         *
         * @param elementSurvole l'élément survolé.
         */
        private void actualiserSelections(
                @Nullable final Element elementSurvole,
                final boolean controleAppuyee) {
            if (elementSurvole != null) {
                if (controleAppuyee) {
                    if (elementsSelectionnes.contains(elementSurvole)) {
                        deselectionner(elementSurvole);
                    } else {
                        selectionner(elementSurvole);
                    }
                } else if (!elementsSelectionnes.contains(elementSurvole)) {
                    toutDeselectionner();
                    selectionner(elementSurvole);
                }
            } else {
                toutDeselectionner();
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
            final GestionSurvol gestionSurvol = new GestionSurvol(espace);
            espace.addEventFilter(MouseEvent.MOUSE_MOVED, gestionSurvol);
            gestionsSurvol.put(espace, gestionSurvol);
            final GestionSelection gestionSelection = new GestionSelection(
                    espace);
            espace.addEventFilter(MouseEvent.MOUSE_PRESSED, gestionSelection);
            gestionsSelection.put(espace, gestionSelection);
        }

        @Override
        public void onRemove(@NotNull final Espace espace) {
            espace.removeEventFilter(MouseEvent.MOUSE_MOVED,
                    gestionsSurvol.remove(espace));
            espace.removeEventFilter(MouseEvent.MOUSE_PRESSED,
                    gestionsSelection.remove(espace));
        }

    }
    
}
