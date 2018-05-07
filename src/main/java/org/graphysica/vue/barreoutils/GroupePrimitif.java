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
import org.graphysica.construction.outil.OutilCreationPoint;

/**
 * Le groupe des outils de création d'éléments primitif.
 *
 * @author Marc-Antoine Ouimet
 */
class GroupePrimitif extends Groupe {

    public GroupePrimitif(@NotNull final GestionnaireOutils gestionnaireOutils) {
        super(gestionnaireOutils);
    }

    {
        final Item outilPoint = new Item(gestionnaireOutils, "point",
                "Point", new OutilCreationPoint(gestionnaireOutils));
        getItems().addAll(outilPoint);
        definirDernierOutil(outilPoint);
        setGraphic(outilPoint.affichageImage());
    }

}
