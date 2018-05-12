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
import javafx.collections.ObservableSet;

/**
 * Gère les notifications de modifications d'ajout et de retrait d'éléments d'un
 * ensemble observable.
 *
 * @author Marc-Antoine Ouimet
 * @param <E> le type d'élément de l'ensemble observable.
 */
@SuppressWarnings("unchecked")
public abstract class SetChangeListener<E>
        implements javafx.collections.SetChangeListener<E> {

    /**
     * Construit un événement d'actualisation d'ensemble observable.
     */
    public SetChangeListener() {
    }

    /**
     * Construit un événement d'actualisation d'ensemble observable en gérant
     * l'ajout des éléments spécifiés.
     *
     * @param elements les éléments à gérer.
     */
    public SetChangeListener(@NotNull final ObservableSet<E> elements) {
        elements.stream().forEach((element) -> {
            onAdd(element);
        });
    }

    @Override
    public void onChanged(@NotNull final Change changement) {
        if (changement.wasAdded()) {
            onAdd((E) changement.getElementAdded());
        } else if (changement.wasRemoved()) {
            onRemove((E) changement.getElementRemoved());
        }
    }

    /**
     * Appelée à chaque ajout sur l'ensemble observable.
     *
     * @param element l'élément ajouté à l'ensemble observable.
     */
    public abstract void onAdd(@NotNull final E element);

    /**
     * Appelée à chaque retrait de l'ensemble observable.
     *
     * @param element l'élément retirer de l'ensmble observable.
     */
    public abstract void onRemove(@NotNull final E element);

}
