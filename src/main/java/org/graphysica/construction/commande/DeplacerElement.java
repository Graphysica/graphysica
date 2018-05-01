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
package org.graphysica.construction.commande;

import com.sun.istack.internal.NotNull;
import java.util.Set;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.construction.Element;
import org.graphysica.espace2d.position.PositionReelle;

/**
 * Une commande de déplacement d'éléments permet de déplacer et d'annuler le
 * déplacement d'un ensemble d'éléments d'une position initiale vers une
 * position finale.
 *
 * @author Marc-Antoine Ouimet
 */
public class DeplacerElement extends CommandeAnnulable {

    /**
     * L'ensemble des éléments déplacés par cette commande de déplacement
     * d'éléments.
     */
    private final Set<Element> elements;

    /**
     * La position initiale du déplacement des éléments.
     */
    private final PositionReelle initiale;

    /**
     * La position finale du déplacement des éléments.
     */
    private final PositionReelle finale;

    /**
     * Construit une commande de déplacement d'éléments d'une position initiale
     * à une position finale.
     *
     * @param elements les éléments à déplacer.
     * @param initiale la position initiale du déplacement.
     * @param finale la position finale du déplacement.
     */
    public DeplacerElement(@NotNull final Set<Element> elements,
            @NotNull final PositionReelle initiale,
            @NotNull final PositionReelle finale) {
        this.elements = elements;
        this.initiale = initiale;
        this.finale = finale;
    }

    /**
     * Déplace l'ensemble des éléments selon un déplacement réel spécifié.
     *
     * @param deplacementReel le déplacement des éléments.
     */
    private void deplacer(@NotNull final Vector2D deplacementReel) {
        for (final Element element : elements) {
            element.deplacer(deplacementReel);
        }
    }

    @Override
    public void executer() {
        deplacer(initiale.distance(finale));
    }

    @Override
    public void annuler() {
        deplacer(finale.distance(initiale));
    }

    @Override
    public void refaire() {
        executer();
    }

}
