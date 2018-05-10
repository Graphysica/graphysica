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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.graphysica.construction.Element;

/**
 * Une commande de création d'éléments permet de créer des éléments sur une
 * construction. Un élément peut tant bien être un corps physique qu'une forme
 * graphique, et que ça soit pour la prévisualisation ou pour la représentation
 * de l'espace. Un outil créera donc d'abord des éléments de prévisualiation des
 * composantes à ajouter à la construction avant de les ajouter concrètement.
 *
 * @author Marc-Antoine Ouimet
 */
public class CreerElement extends CommandeAnnulable {

    /**
     * L'ensemble des éléments de la construction.
     */
    private final Set<Element> elementsConstruction;

    /**
     * Les éléments à créer par cette commande.
     */
    private final Set<Element> elements;

    /**
     * Construit une commande de création d'éléments sur un ensemble d'éléments
     * de constructions défini.
     *
     * @param construction les éléments de la construction sur laquelle créer
     * les éléments.
     * @param elements les éléments à créer.
     */
    public CreerElement(@NotNull final Set<Element> construction,
            @NotNull final Element... elements) {
        this.elements = new HashSet<>(Arrays.asList(elements));
        this.elementsConstruction = construction;
    }

    /**
     * Ajoute les éléments créés à la construction.
     */
    @Override
    public void executer() {
        elementsConstruction.addAll(elements);
    }

    /**
     * Retire les éléments créés de la construction.
     */
    @Override
    public void annuler() {
        elementsConstruction.removeAll(elements);
    }

    /**
     * Recrée l'élément.
     */
    @Override
    public void refaire() {
        executer();
    }

}
