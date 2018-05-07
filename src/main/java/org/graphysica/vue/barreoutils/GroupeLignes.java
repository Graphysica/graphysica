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
import org.graphysica.construction.outil.OutilCreationDroite;

/**
 * Le groupe d'outils de création de lignes.
 *
 * @author Marc-Antoine Ouimet
 */
class GroupeLignes extends Groupe {

    public GroupeLignes(@NotNull final GestionnaireOutils gestionnaireOutils) {
        super(gestionnaireOutils);
    }

    {
        final Item outilDroite = new Item(gestionnaireOutils, "droite",
                "Droite", new OutilCreationDroite(gestionnaireOutils));
        getItems().addAll(outilDroite);
        definirDernierOutil(outilDroite);
        setGraphic(outilDroite.affichageImage());
    }

}
