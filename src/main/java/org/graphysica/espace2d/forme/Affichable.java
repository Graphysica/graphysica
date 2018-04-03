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
 * Les objets affichables ont un état d'affichage dans l'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public interface Affichable {

    /**
     * Récupère l'état d'affichage de l'objet.
     *
     * @return {@code true} si l'objet est affiché.
     */
    public boolean isAffiche();

    /**
     * Modifie l'état d'affichage de l'objet.
     *
     * @param isAffiche la nouvelle valeur d'état d'affichage de l'objet.
     */
    public void setAffiche(final boolean isAffiche);

}
