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
     * Si le déplacement est entamé.
     */
    private boolean entame = false;

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

    @Override
    public void gerer(@NotNull final MouseEvent evenement) {
        final GestionnaireSelections gestionnaireSelections
                = gestionnaireOutils.getGestionnaireSelections();
        if (evenement.getEventType() == MouseEvent.MOUSE_PRESSED
                && evenement.getButton() == MouseButton.PRIMARY) {
            // Copier les éléments sélectionnés
            elements = new HashSet<>(gestionnaireSelections
                    .getElementsSelectionnes());
            if (!elements.isEmpty()) {
                initiale = capterPosition();
            }
        }
        if (isEnCours()) {
            if (evenement.isMiddleButtonDown()) {
                finDeplacement();
            } else if (evenement.getButton() == MouseButton.PRIMARY
                    && evenement.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                entame = true;
                deplacer();
            }
        }
        if (entame && evenement.getEventType() == MouseEvent.MOUSE_RELEASED
                && evenement.getButton() == MouseButton.PRIMARY) {
            finDeplacement();
            gestionnaireOutils.finOutil();
        }
    }

    /**
     * Déplace les éléments selon le déplacement du curseur.
     */
    private void deplacer() {
        final Vector2D deplacement = gestionnaireOutils
                .getGestionnaireSelections().deplacementReelCurseur();
        deplacer(deplacement);
    }

    /**
     * Déplace les éléments selon un déplacement réel spécifié.
     *
     * @param deplacementReel le déplacement à effectuer.
     */
    private void deplacer(@NotNull final Vector2D deplacementReel) {
        elements.forEach((element) -> {
            element.deplacer(deplacementReel);
        });
    }

    /**
     * Capte la position réelle du curseur à son emplacement actuel.
     *
     * @return la position réelle du curseur.
     */
    private PositionReelle capterPosition() {
        return gestionnaireOutils.getGestionnaireSelections()
                .positionReelleCurseur();
    }

    /**
     * Met fin au déplacement.
     */
    private void finDeplacement() {
        if (finale == null) {
            finale = gestionnaireOutils.getGestionnaireSelections()
                    .positionReelleCurseur();
            gestionnaireOutils.getGestionnaireCommandes()
                    .ajouter(new DeplacerElement(elements,
                            initiale.distance(finale)));
        }
    }

    @Override
    public void interrompre() {
        finale = capterPosition();
        deplacer(finale.distance(initiale));
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
