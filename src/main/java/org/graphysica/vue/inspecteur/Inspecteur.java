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
package org.graphysica.vue.inspecteur;

import com.sun.istack.internal.NotNull;
import javafx.collections.ObservableSet;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.graphysica.construction.Construction;
import org.graphysica.construction.Element;

/**
 * L'inspecteur permet d'afficher les éléments d'une construction.
 *
 * @author Marc-Antoine Ouimet
 */
public class Inspecteur extends TabPane {
    
    /**
     * La largeur préférée des inspecteurs.
     */
    private static final double LARGEUR_PREFEREE = 100;

    /**
     * La construction inspectée par cet inspecteur.
     */
    private final Construction construction;

    /**
     * L'ensemble des éléments de la construction.
     */
    private final ObservableSet<Element> elements;

    /**
     * L'inspecteur des objets de la construction relevant de la mathématique.
     */
    private final InspecteurMathematique inspecteurMathematique;

    /**
     * L'inspecteur des objets de la construction relevant de la physique.
     */
    private final InspecteurPhysiques inspecteurPhysique;

    /**
     * Construit un inspecteur d'éléments sur une construction définie.
     *
     * @param construction la construction inspectée.
     */
    public Inspecteur(@NotNull final Construction construction) {
        this.construction = construction;
        elements = construction.getElements();
        inspecteurMathematique = new InspecteurMathematique(elements);
        inspecteurPhysique = new InspecteurPhysiques(elements);
        final Tab ongletMathematique = new Tab("Mathématique", 
                inspecteurMathematique);
        ongletMathematique.setClosable(false);
        final Tab ongletPhysique = new Tab("Physique", inspecteurPhysique);
        ongletPhysique.setClosable(false);
        getTabs().addAll(ongletMathematique, ongletPhysique);
        setPrefWidth(LARGEUR_PREFEREE);
    }

}
