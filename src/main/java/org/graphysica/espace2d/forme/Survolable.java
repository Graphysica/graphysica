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
 * Les classes implémentant cette interface peuvent être dessinées en survol. Le
 * survol permet de signaler à l'utilisateur que son curseur est par-dessus une
 * forme. Plusieurs formes peuvent aussi être en survol dans le cas où une
 * sélection multiple a été effectuée.
 *
 * @author Marc-Antoine Ouimet
 */
interface Survolable extends Dessinable, Selectionnable {

    /**
     * Dessine normalement l'objet dans un contexte graphique d'espace spécifié.
     *
     * @param toile la toile sur laquelle dessiner l'objet.
     * @param repere le repère de l'espace.
     */
    void dessinerNormal(@NotNull final Canvas toile,
            @NotNull final Repere repere);

    /**
     * Dessine le survol de l'objet dans un contexte graphique d'espace
     * spécifié. =
     *
     * @param toile la toile sur laquelle dessiner l'objet.
     * @param repere le repère de l'espace.
     */
    void dessinerSurvol(@NotNull final Canvas toile,
            @NotNull final Repere repere);

    /**
     * Renvoie si l'objet est en survol.
     *
     * @return {@code true} si l'objet est en survol.
     */
    boolean isEnSurvol();

    /**
     * Modifie l'état de survol de l'objet.
     *
     * @param enSurvol le nouvel état de survol de l'objet.
     */
    void setEnSurvol(final boolean enSurvol);

}
