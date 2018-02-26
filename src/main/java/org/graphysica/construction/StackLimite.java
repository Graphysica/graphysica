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
import java.util.Stack;

/**
 * Un stack limité est un stack dont la capacité est fixe.
 * <p>
 * D'après 
 * https://stackoverflow.com/questions/7727919/creating-a-fixed-size-stack.
 *
 * @author Marc-Antoine
 * @param <E> le type d'éléments du stack.
 */
public final class StackLimite<E> extends Stack<E> {
    
    /**
     * La capacité par défaut d'un stack limité.
     */
    private static final int CAPACITE_PAR_DEFAUT = 100;
    
    /**
     * La capacité de ce stack limité.
     */
    private int capacite = CAPACITE_PAR_DEFAUT;

    public StackLimite() {
    }

    public StackLimite(final int capacite) {
        setCapacite(capacite);
    }

    @Override
    public E push(@NotNull final E element) {
        while(size() >= capacite) {
            remove(0);
        }
        return super.push(element);
    }
    
    /**
     * Défini la capacité de ce stack limité.
     * @param capacite la nouvelle capacité du stack.
     */
    public final void setCapacite(final int capacite) {
        if (capacite <= 0) {
            this.capacite = CAPACITE_PAR_DEFAUT;
        } else {
            this.capacite = capacite;
        }
    }
    
}
