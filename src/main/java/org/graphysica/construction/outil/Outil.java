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
package org.graphysica.construction.outil;

import com.sun.istack.internal.NotNull;
import javafx.scene.input.MouseEvent;
import org.graphysica.construction.GestionnaireOutils;

/**
 * Un outil est un module encapsulé qui gère des événements de la souris sur les
 * espaces gérés par un gestionnaire d'outils.
 *
 * @author Victor Babin
 * @author Marc-Antoine Ouimet
 */
public abstract class Outil {

    /**
     * Gestionnaire d'outils pour la construction.
     */
    protected final GestionnaireOutils gestionnaireOutils;

    /**
     * Construit un outil sur un gestionnaire d'outils défini.
     *
     * @param gestionnaireOutils le gestionnaire d'outils de la construction.
     */
    public Outil(@NotNull final GestionnaireOutils gestionnaireOutils) {
        this.gestionnaireOutils = gestionnaireOutils;
    }

    /**
     * Gère l'événement de la souris dans le contexte de cet outil.
     *
     * @param evenement l'événement de la souris.
     */
    public abstract void gerer(@NotNull final MouseEvent evenement);

    /**
     * Duplique cet outil.
     *
     * @return l'outil dupliqué.
     */
    @NotNull
    public abstract Outil dupliquer();

}
