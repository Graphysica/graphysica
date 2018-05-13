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
package org.graphysica.vue.barreoutils;

import com.sun.istack.internal.NotNull;
import org.graphysica.construction.GestionnaireOutils;
import org.graphysica.construction.outil.OutilDeplacementElement;

/**
 * Le groupe des outils de sélection.
 *
 * @author Marc-Antoine Ouimet
 */
final class GroupeSelection extends Groupe {

    /**
     * {@inheritDoc}
     */
    public GroupeSelection(
            @NotNull final GestionnaireOutils gestionnaireOutils) {
        super(gestionnaireOutils);
    }

    {
        final Item outilDeplacement = new Item("deplacer", "Déplacer",
                new OutilDeplacementElement(gestionnaireOutils));
        getItems().addAll(outilDeplacement);
    }

}
