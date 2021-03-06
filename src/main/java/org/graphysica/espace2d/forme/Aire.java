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

import javafx.scene.paint.Color;

/**
 * Une aire représente une région de l'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public abstract class Aire extends Forme {

    /**
     * La couleur par défaut d'une aire.
     */
    protected static final Color COULEUR_PAR_DEFAUT = new Color(0.72, 0.52,
            0.04, 0.4);

}
