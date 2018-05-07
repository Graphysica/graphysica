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
package org.graphysica.construction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.graphysica.espace2d.Espace;

/**
 * Une construction permet d'élaborer une scène de simulation de physique.
 *
 * @author Marc-Antoine Ouimet
 */
public final class Construction {

    /**
     * Les espaces de la construction.
     */
    private final ObservableList<Espace> espaces
            = FXCollections.observableArrayList();

    /**
     * L'ensemble des éléments de la construction. Comprend les corps physiques
     * et les objets mathématiques.
     */
    private final ObservableList<Element> elements
            = FXCollections.observableArrayList();

    /**
     * Le gestionnaire des commandes de la construction.
     */
    private transient final GestionnaireCommandes gestionnaireCommandes
            = new GestionnaireCommandes();

    /**
     * Le gestionnaire des espaces de cette construction.
     */
    private transient final GestionnaireEspaces gestionnaireEspaces
            = new GestionnaireEspaces(espaces, elements);
    /**
     * Le gestionnaire des sélections de la construction.
     */
    private transient final GestionnaireSelections gestionnaireSelections
            = new GestionnaireSelections(gestionnaireEspaces,
                    espaces, elements);

    /**
     * Le gestionnaire des outils de la construction.
     */
    private transient final GestionnaireOutils gestionnaireOutils
            = new GestionnaireOutils(gestionnaireCommandes,
                    gestionnaireSelections, espaces, elements);

    {
        espaces.add(new Espace(500, 500));
    }

    public ObservableList<Espace> getEspaces() {
        return espaces;
    }

    public ObservableList<Element> getElements() {
        return elements;
    }

    public GestionnaireEspaces getGestionnaireEspaces() {
        return gestionnaireEspaces;
    }

    public GestionnaireCommandes getGestionnaireCommandes() {
        return gestionnaireCommandes;
    }

    public GestionnaireSelections getGestionnaireSelections() {
        return gestionnaireSelections;
    }

    public GestionnaireOutils getGestionnaireOutils() {
        return gestionnaireOutils;
    }

}
