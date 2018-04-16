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

import java.util.HashSet;
import java.util.Set;
import org.graphysica.espace2d.forme.Forme;

/**
 * Un élément peut être créé et manipulé dans une construction.
 *
 * @author Marc-Antoine Ouimet
 */
public abstract class Element {

    /**
     * Les dépendances de création de cet élément. Permet d'effacer cet élément
     * si une de ses dépendances est effacée.
     */
    protected final Set<Element> dependances = new HashSet<>();

    /**
     * L'ensemble des formes d'affichage de cet élément.
     */
    protected transient Set<Forme> formes = new HashSet<>();

    /**
     * Le nombre d'éléments qui ont été construits.
     */
    private static long ELEMENTS = 0;

    /**
     * Le numéro d'identification de l'élément.
     */
    private final long id;

    {
        id = ++ELEMENTS;
    }

    public Set<Forme> getFormes() {
        return formes;
    }

    public long getId() {
        return id;
    }

}
