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
package org.graphysica.construction.outil;

import com.sun.istack.internal.NotNull;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.construction.Element;
import org.graphysica.construction.GestionnaireOutils;
import org.graphysica.construction.GestionnaireSelections;
import org.graphysica.construction.commande.DeplacerElement;
import org.graphysica.espace2d.position.PositionReelle;

/**
 * Un outil de déplacement permet de déplacer des éléments sélectionnés dans
 * l'espace.
 *
 * @author Marc-Antoine Ouimet
 * @author Victor Babin
 */
public class OutilDeplacementElement extends Outil {

    /**
     * L'ensemble des éléments à déplacer.
     */
    private Set<Element> elements;

    /**
     * La position initiale du déplacement des éléments.
     */
    private PositionReelle initiale;

    /**
     * La position finale du déplacement des éléments.
     */
    private PositionReelle finale;

    /**
     * Construit un outil de déplacement d'éléments au gestionnaire d'outils
     * défini.
     *
     * @param gestionnaireOutils le gestionnaire d'outils de cet outil de
     * déplacement d'éléments.
     */
    public OutilDeplacementElement(
            @NotNull final GestionnaireOutils gestionnaireOutils) {
        super(gestionnaireOutils);
    }

    /**
     * Détermine si un déplacement est en cours.
     *
     * @return {@code true} si un déplacement a été initié.
     */
    private boolean deplacementEnCours() {
        return initiale != null;
    }

    @Override
    public void gerer(@NotNull final MouseEvent evenement) {
        final GestionnaireSelections gestionnaireSelections
                = gestionnaireOutils.getGestionnaireSelections();
        if (evenement.getButton() == MouseButton.PRIMARY
                && evenement.getEventType() == MouseEvent.MOUSE_PRESSED
                && evenement.isPrimaryButtonDown()
                && !gestionnaireSelections.survolEstVide()) {
            initiale = gestionnaireSelections.positionReelleCurseur();
            elements = new HashSet<>(gestionnaireSelections
                    .getElementsSelectionnes());
        } else if (!gestionnaireSelections.selectionEstVide()) {
            if (evenement.getEventType() == MouseEvent.MOUSE_DRAGGED
                    && evenement.isPrimaryButtonDown()) {
                for (final Element element : elements) {
                    element.deplacer(gestionnaireSelections
                            .deplacementReelCurseur());
                }
            } else if (evenement.getButton() == MouseButton.PRIMARY
                    && evenement.getEventType() == MouseEvent.MOUSE_RELEASED) {
                finale = gestionnaireSelections.positionReelleCurseur();
                gestionnaireOutils.getGestionnaireCommandes()
                        .ajouter(new DeplacerElement(elements,
                                initiale.distance(finale)));
                gestionnaireOutils.finOutil();
            }
        }
    }

    @Override
    public void interrompre() {
        if (deplacementEnCours()) {
            finale = gestionnaireOutils.getGestionnaireSelections()
                    .positionReelleCurseur();
            final Vector2D deplacement = finale.distance(initiale);
            for (final Element element : elements) {
                element.deplacer(deplacement);
            }
        }
    }

    @Override
    public Outil dupliquer() {
        return new OutilDeplacementElement(gestionnaireOutils);
    }

    @Override
    public boolean isEnCours() {
        return initiale != null && finale == null;
    }

}
