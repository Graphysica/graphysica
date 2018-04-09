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

/**
 * Les classes implémentant cette interface peuvent annuler et refaire des
 * actions.
 *
 * @author Marc-Antoine Ouimet
 */
public interface Annulable {

    /**
     * Annule la dernière action effectuée.
     */
    void annuler();

    /**
     * Refait la dernière action annulée.
     */
    void refaire();

}
