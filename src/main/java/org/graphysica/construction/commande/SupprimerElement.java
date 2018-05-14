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
import java.util.HashSet;
import java.util.Set;
import org.graphysica.construction.Element;

/**
 * Une commande de suppression d'éléments permet de supprimer des éléments et
 * les éléments d'une construction qui en sont dépendants.
 *
 * @author Marc-Antoine Ouimet
 */
public class SupprimerElement extends CommandeAnnulable {

    /**
     * L'ensemble des éléments de la construction.
     */
    private final Set<Element> elementsConstruction;

    /**
     * Les éléments à supprimer par cette commande.
     */
    private final Set<Element> elements;

    /**
     * Les éléments supprimés par cette commande. Comprend l'ensemble des
     * éléments qui ont des dépendances de création sur les {@code elements}.
     */
    private final Set<Element> elementsSupprimes = new HashSet<>();

    /**
     * Construit une commande de suppression d'éléments sur un ensemble défini
     * d'éléments de constructions.
     *
     * @param construction les éléments de la construction de laquelle supprimer
     * les éléments.
     * @param elements les éléments à supprimer.
     */
    public SupprimerElement(@NotNull final Set<Element> construction,
            @NotNull final Set<Element> elements) {
        this.elements = elements;
        this.elementsConstruction = construction;
    }

    /**
     * Supprime les éléments et les éléments qui en sont dépendants.
     */
    @Override
    public void executer() {
        recupererElementsASupprimer();
        elementsConstruction.removeAll(elementsSupprimes);
    }

    /**
     * Récupère les éléments à supprimer.
     */
    private void recupererElementsASupprimer() {
        elementsSupprimes.clear();
        elementsSupprimes.addAll(elements);
        for (final Element element : elements) {
            for (final Element dependant : elementsConstruction) {
                if (!elementsSupprimes.contains(dependant) 
                        && dependant.getDependances().contains(element)) {
                    elementsSupprimes.add(dependant);
                }
            }
        }
    }

    /**
     * Recrée les éléments supprimés.
     */
    @Override
    public void annuler() {
        elementsConstruction.addAll(elementsSupprimes);
    }

    /**
     * Supprime à nouveau les éléments.
     */
    @Override
    public void refaire() {
        executer();
    }

}
