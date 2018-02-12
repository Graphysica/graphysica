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
package org.graphysica.espace2d;

import com.sun.istack.internal.NotNull;
import javafx.scene.canvas.GraphicsContext;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Les classes implémentant cette interface peuvent être dessinées dans un
 * contexte graphique.
 *
 * @author Marc-Antoine Ouimet
 */
public interface Dessinable {

    /**
     * Dessine l'objet dans un contexte graphique spécifié.
     *
     * @param contexteGraphique le contexte graphique de dessin de l'objet.
     * @param echelleVirtuelle l'échelle virtuelle de dessin de l'objet.
     * @param origineVirtuelle l'origine virtuelle de dessin de l'objet.
     */
    public void dessiner(@NotNull final GraphicsContext contexteGraphique, 
            @NotNull final Vector2D echelleVirtuelle, 
            @NotNull Vector2D origineVirtuelle);

}
