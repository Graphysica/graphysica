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

import com.sun.istack.internal.NotNull;
import javafx.scene.canvas.Canvas;
import org.graphysica.espace2d.Repere;

/**
 * Les classes implémentant cette interface peuvent être dessinées en
 * surbrillance. La surbrillance permet de signaler à l'utilisateur que son
 * curseur est par-dessus une forme. L'état de surbrillance d'un objet est
 * déterminé à partir de son état de sélection, et dans un contexte où plusieurs
 * objets sont sélectionnés, en fonction de la distance minimale du curseur aux
 * objets sélectionnés. Ce faisant, une seule forme est en surbrillance au
 * défilement du curseur sur l'espace interactif. Plusieurs formes peuvent être
 * en surbrillance dans le cas où une sélection multiple a été effectuée.
 *
 * @author Marc-Antoine Ouimet
 */
interface Surbrillable extends Dessinable {

    /**
     * Dessine normalement l'objet dans un contexte graphique d'espace spécifié.
     *
     * @param toile la toile surlaquelle dessiner l'objet.
     * @param repere le repère de l'espace.
     */
    void dessinerNormal(@NotNull final Canvas toile,
            @NotNull final Repere repere);

    /**
     * Dessine la surbrillance de l'objet dans un contexte graphique d'espace
     * spécifié.
     *
     * @param toile la toile surlaquelle dessiner l'objet.
     * @param repere le repère de l'espace.
     */
    void dessinerSurbrillance(@NotNull final Canvas toile,
            @NotNull final Repere repere);

    /**
     * Renvoie si l'objet est en surbrillance.
     *
     * @return {@code true} si l'objet est en surbrillance.
     */
    boolean isEnSurbrillance();

    /**
     * Modifie l'état de surbrillance de l'objet.
     *
     * @param enSurbrillance le nouvel état de surbrillance de l'objet.
     */
    void setEnSurbrillance(final boolean enSurbrillance);

}
