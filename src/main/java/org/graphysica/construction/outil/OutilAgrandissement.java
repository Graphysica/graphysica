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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.graphysica.construction.GestionnaireOutils;

/**
 * Un outil d'agrandissement ponctuel permet de zoomer une fois dans l'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public class OutilAgrandissement extends Outil {
    
    /**
     * Indique si l'outil est en agrandissement.
     */
    private boolean enAgrandissement = false;

    /**
     * Construit un outil d'agrandissement d'espace au gestionnaire d'outils
     * défini.
     *
     * @param gestionnaireOutils le gestionnaire d'outils de cet outil
     * d'agrandissement d'espace.
     */
    public OutilAgrandissement(
            @NotNull final GestionnaireOutils gestionnaireOutils) {
        super(gestionnaireOutils);
    }

    @Override
    public void gerer(@NotNull final MouseEvent evenement) {
        if (evenement.getButton() == MouseButton.PRIMARY) {
            if (evenement.getEventType() == MouseEvent.MOUSE_PRESSED) {
                enAgrandissement = true;
                gestionnaireOutils.getGestionnaireEspaces().espaceActif()
                        .zoomer();
            } else if (evenement.getEventType() == MouseEvent.MOUSE_RELEASED) {
                enAgrandissement = false;
                gestionnaireOutils.finOutil();
            }
        }
    }

    @Override
    public boolean isEnCours() {
        return enAgrandissement;
    }

    @Override
    public void interrompre() {
    }

    @Override
    public Outil dupliquer() {
        return new OutilAgrandissement(gestionnaireOutils);
    }

}
