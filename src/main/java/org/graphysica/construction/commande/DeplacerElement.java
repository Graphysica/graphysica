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
     * Le décplacement réel des éléments.
     */
    private final Vector2D deplacement;

    /**
     * Construit une commande de déplacement d'éléments.
     *
     * @param elements les éléments à déplacer.
     * @param deplacement le déplacement réel des éléments.
     */
    public DeplacerElement(@NotNull final Set<Element> elements,
            @NotNull final Vector2D deplacement) {
        this.elements = elements;
        this.deplacement = deplacement;
    }

    /**
     * Déplace l'ensemble des éléments selon un déplacement réel spécifié.
     *
     * @param deplacement le déplacement réel des éléments.
     */
    private void deplacer(@NotNull final Vector2D deplacement) {
        for (final Element element : elements) {
            element.deplacer(deplacement);
        }
    }

    @Override
    public void executer() {
        deplacer(deplacement);
    }

    @Override
    public void annuler() {
        deplacer(deplacement.negate());
    }

    @Override
    public void refaire() {
        executer();
    }

}
