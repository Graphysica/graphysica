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
package org.graphysica.espace2d.forme;

/**
 * Les instances de classes implémentant cette interface peuvent être
 * prévisualisées. Une forme qui est en état de prévisualisation ne devrait pas
 * être en état de surbrillance.
 *
 * @author Marc-Antoine Ouimet
 */
interface Previsualisable {

    /**
     * Renvoie si l'objet est en prévisualisation.
     *
     * @return {@code true} si l'objet est en prévisualisation.
     */
    boolean isEnPrevisualisation();

    /**
     * Modifie l'état de prévisualisation de cet objet.
     *
     * @param enPrevisualisation le nouvel état de prévisualisation de l'objet.
     */
    void setEnPrevisualisation(final boolean enPrevisualisation);

}
