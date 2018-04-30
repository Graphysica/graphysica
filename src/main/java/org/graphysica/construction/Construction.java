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

import com.sun.istack.internal.NotNull;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.graphysica.construction.commande.Commande;
import org.graphysica.construction.outil.OutilCreationPoint;
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
     * Le gestionnaire des commandes de la construction.
     */
    private final GestionnaireCommandes gestionnaireCommandes
            = new GestionnaireCommandes();

    /**
     * L'ensemble des éléments de la construction. Comprend les corps physiques
     * et les objets mathématiques.
     */
    private final ObservableList<Element> elements 
            = FXCollections.observableArrayList();

    /**
     * Le gestionnaire des sélections de la construction.
     */
    private final GestionnaireSelections gestionnaireSelections
            = new GestionnaireSelections(espaces, elements);

    /**
     * Le gestionnaire des outils de la construction.
     */
    private final GestionnaireOutils gestionnaireOutils
            = new GestionnaireOutils(this, espaces);

    {
        espaces.add(new Espace(500, 500));
        gestionnaireOutils.setOutilActif(new OutilCreationPoint(gestionnaireOutils));
    }

    /**
     * Exécute une commande.
     *
     * @param commande la commande à exécuter.
     */
    public void executerCommande(@NotNull final Commande commande) {
        gestionnaireCommandes.executer(commande);
    }

    /**
     * Ajoute des éléments à la construction.
     *
     * @param elements les éléments à ajouter.
     */
    public void ajouter(@NotNull final Element... elements) {
        for (final Element element : elements) {
            getEspace().getFormes().addAll(element.getFormes());
            this.elements.add(element);
        }
    }

    /**
     * Retire des éléments de la construction.
     *
     * @param elements les éléments à retirer.
     */
    public void retirer(@NotNull final Element... elements) {
        for (final Element element : elements) {
            getEspace().getFormes().removeAll(element.getFormes());
            this.elements.remove(element);
        }
    }

    /**
     * Duplique le premier espace de cette construction et l'ajoute aux espaces.
     *
     * @return l'espace dupliqué.
     */
    public Espace dupliquerEspace() {
        final Espace espace = new Espace(getEspace());
        espaces.add(espace);
        return espace;
    }

    public Espace getEspace() {
        return espaces.get(0);
    }

    public ObservableList<Espace> getEspaces() {
        return espaces;
    }

    public ObservableList<Element> getElements() {
        return elements;
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
