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
import java.util.Set;
import javafx.scene.input.MouseEvent;
import org.graphysica.construction.Element;
import org.graphysica.construction.GestionnaireOutils;
import org.graphysica.construction.GestionnaireSelections;

/**
 *
 * @author Marc-Antoine Ouimet
 */
public class OutilDeplacement extends Outil {

    public OutilDeplacement(
            @NotNull final GestionnaireOutils gestionnaireOutils) {
        super(gestionnaireOutils);
    }

    @Override
    public void gerer(@NotNull final MouseEvent evenement) {
        if (evenement.getEventType() == MouseEvent.MOUSE_DRAGGED 
                && evenement.isPrimaryButtonDown()) {
            final GestionnaireSelections gestionnaireSelections
                    = gestionnaireOutils.getGestionnaireSelections();
            final Set<Element> elementsSelectionnes = gestionnaireSelections
                    .getElementsSelectionnes();
            for (final Element element : elementsSelectionnes) {
                element.deplacer(gestionnaireSelections
                        .deplacementReelCurseur());
            }
        }
    }

    @Override
    public Outil dupliquer() {
        return new OutilDeplacement(gestionnaireOutils);
    }

}
