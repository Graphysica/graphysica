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
import org.graphysica.construction.commande.CreerElement;
import org.graphysica.construction.mathematiques.PointConcret;
import org.graphysica.espace2d.forme.Forme;

/**
 * Un outil de création de point permet de créer un point étiquetté.
 *
 * @author Victor Babin
 * @author Marc-Antoine Ouimet
 */
public class OutilCreationPoint extends OutilCreationElement {

    /**
     * Le point créé par cet outil de création de point.
     */
    private PointConcret point;

    /**
     * Construit un outil de création de point au gestionnaire d'outils défini.
     *
     * @param gestionnaireOutils le gestionnaire d'outils de cet outil de
     * création de point.
     */
    public OutilCreationPoint(
            @NotNull final GestionnaireOutils gestionnaireOutils) {
        super(gestionnaireOutils);
    }

    @Override
    public Outil dupliquer() {
        return new OutilCreationPoint(gestionnaireOutils);
    }

    @Override
    public void gerer(@NotNull final MouseEvent evenement) {
        if (aProchaineEtape()) {
            if (evenement.isPrimaryButtonDown()
                    && evenement.getEventType() == MouseEvent.MOUSE_PRESSED) {
                previsualiserPoint();
            } else if (point != null
                    && evenement.getEventType() == MouseEvent.MOUSE_RELEASED) {
                creerPoint();
                gestionnaireOutils.finOutil();
            }
        }
    }

    /**
     * Crée une prévisualisation de point à l'emplacement du curseur.
     */
    private void previsualiserPoint() {
        point = new PointConcret(gestionnaireOutils
                .getGestionnaireSelections().positionCurseurProperty());
        gestionnaireOutils.getElements().add(point);
        for (final Forme forme : point.getFormes()) {
            forme.setEnPrevisualisation(true);
        }
    }

    /**
     * Crée le point à l'emplacement du curseur et met fin à l'utilisation de
     * cet outil.
     */
    private void creerPoint() {
        point.positionInterneProperty().unbind();
        for (final Forme forme : point.getFormes()) {
            forme.setEnPrevisualisation(false);
        }
        gestionnaireOutils.getGestionnaireCommandes().ajouter(
                new CreerElement(gestionnaireOutils.getElements(), point));
    }

}
