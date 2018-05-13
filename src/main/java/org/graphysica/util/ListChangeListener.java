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
package org.graphysica.util;

import com.sun.istack.internal.NotNull;
import javafx.collections.ObservableList;

/**
 * Gère les notifications de modifications d'ajout et de retrait d'éléments
 * d'une liste observable.
 *
 * @author Marc-Antoine Ouimet
 * @param <E> le type d'élément de la liste observable.
 */
public abstract class ListChangeListener<E>
        implements javafx.collections.ListChangeListener<E> {

    /**
     * Construit un événement d'actualisation de liste observable.
     */
    public ListChangeListener() {
    }

    /**
     * Construit un événement d'actualisation de liste observable en gérant
     * l'ajout des éléments spécifiés.
     *
     * @param elements les éléments à gérer.
     */
    public ListChangeListener(@NotNull final ObservableList<E> elements) {
        elements.stream().forEach((element) -> {
            onAdd(element);
        });
    }

    @Override
    public final void onChanged(
            @NotNull final Change<? extends E> changements) {
        while (changements.next()) {
            if (changements.wasAdded()) {
                changements.getAddedSubList().forEach(
                        (element) -> onAdd(element));
            }
            if (changements.wasRemoved()) {
                changements.getRemoved().forEach(
                        (element) -> onRemove(element));
            }
        }
    }

    /**
     * Appelée à chaque ajout sur la liste observable.
     *
     * @param element l'élément ajouté à la liste observable.
     */
    public abstract void onAdd(@NotNull final E element);

    /**
     * Appelée à chaque retrait de la liste observable.
     *
     * @param element l'élément retirer de la liste observable.
     */
    public abstract void onRemove(@NotNull final E element);

}
