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

import org.graphysica.espace2d.forme.Forme;

/**
 * Un élément peut être créé et manipulé dans une construction.
 *
 * @author Marc-Antoine Ouimet
 */
public abstract class Element {

    private transient Forme[] formes = null;

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

    /**
     * Crée les formes représentant cet élément.
     *
     * @return les formes créées.
     */
    protected abstract Forme[] creerFormes();

    /**
     * Récupère les formes représentant cet élément. S'il s'agit de la première
     * fois que les formes sont demandées, elles sont d'abord crées.
     *
     * @return les formes représentant cet élément.
     */
    public Forme[] getFormes() {
        if (formes == null) {
            formes = creerFormes();
        }
        return formes;
    }

    public long getId() {
        return id;
    }

}
