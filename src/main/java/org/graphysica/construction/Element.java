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
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.forme.Forme;

/**
 * Un élément peut être créé et manipulé dans une construction.
 *
 * @author Marc-Antoine Ouimet
 */
public abstract class Element implements Deplaceable {

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

    /**
     * Déplace les dépendances immédiates de cet élément pour le déplacer
     * lui-même.
     *
     * @param deplacement le déplacement réel de cet élément.
     */
    @Override
    public void deplacer(@NotNull final Vector2D deplacement) {
        if (isDeplaceable()) {
            dependances.forEach((element) -> {
                element.deplacer(deplacement);
            });
        }
    }

    /**
     * Détermine si l'élément peut être déplacé expréssément. Un élément ne peut
     * pas être déplacé si une de ses dépendances immédiates est liée.
     *
     * @return {@code true} si l'élément est déplaceable.
     */
    public boolean isDeplaceable() {
        return dependances.stream().noneMatch((element) -> (element.isLie()));
    }

    /**
     * Détermine si l'élément est lié. Les éléments ne sont pas liés par défaut.
     * Ils peuvent donc être déplacés expréssément.
     *
     * @return {@code true} si l'élément est lié.
     */
    @Override
    public boolean isLie() {
        return false;
    }

}
