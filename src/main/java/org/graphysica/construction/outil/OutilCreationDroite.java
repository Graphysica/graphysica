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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.graphysica.construction.Element;
import org.graphysica.construction.GestionnaireOutils;
import org.graphysica.construction.commande.CreerElement;
import org.graphysica.construction.mathematiques.Droite;
import org.graphysica.construction.mathematiques.Point;
import org.graphysica.construction.mathematiques.PointConcret;
import org.graphysica.espace2d.forme.Forme;

/**
 * Un outil de création de droite permet de créer une droite.
 *
 * @author Marc-Antoine Ouimet
 */
public class OutilCreationDroite extends OutilCreationElement {

    /**
     * Le premier point de création de la droite.
     */
    private Point point1;

    /**
     * Le deuxième point de création de la droite.
     */
    private Point point2;

    /**
     * La droite créée par cet outil de création de droite.
     */
    private Droite droite;

    /**
     * Construit un outil de création de droite au gestionnaire d'outils défini.
     *
     * @param gestionnaireOutils le gestionnaire d'outils de cet outil de
     * création de droite.
     */
    public OutilCreationDroite(
            @NotNull final GestionnaireOutils gestionnaireOutils) {
        super(gestionnaireOutils);
    }

    @Override
    public void gerer(@NotNull final MouseEvent evenement) {
        if (aProchaineEtape()) {
            if (point1 == null) {
                if (evenement.getButton() == MouseButton.PRIMARY && evenement.getEventType() == MouseEvent.MOUSE_PRESSED) {
                    point1 = determinerPoint();
                }
            } else {
                if (point2 == null) {
                    previsualiserPoint2();
                    previsualiserDroite();
                } else if (evenement.getButton() == MouseButton.PRIMARY && evenement.getEventType()
                        == MouseEvent.MOUSE_RELEASED) {
                    aProchaineEtape = false;
                    point2 = determinerPoint();
                    gestionnaireOutils.getElements().add(point2);
                    creerDroite();
                    gestionnaireOutils.finOutil();
                }
            }
        }
    }

    @Override
    public Outil dupliquer() {
        return new OutilCreationDroite(gestionnaireOutils);
    }

    /**
     * Crée un point à l'emplacement réel du curseur.
     *
     * @return le point créé.
     */
    private Point creerPoint() {
        final PointConcret point = new PointConcret(gestionnaireOutils
                .getGestionnaireSelections().positionCurseurProperty());
        point.positionInterneProperty().unbind();
        gestionnaireOutils.getElements().add(point);
        gestionnaireOutils.getGestionnaireCommandes().ajouter(
                new CreerElement(gestionnaireOutils.getElements(), point));
        return point;
    }

    /**
     * Détermine le point à utiliser pour la création de la droite. Crée un
     * point à l'emplacement réel du curseur si aucun autre point n'est
     * sélectionné par l'utilisateur.
     *
     * @return le point déterminé par l'utilisateur.
     */
    private Point determinerPoint() {
        final Set<Element> elementsSurvoles = gestionnaireOutils
                .getGestionnaireSelections().getElementsSurvoles();
        for (final Element element : elementsSurvoles) {
            if (element instanceof Point && element != point1) {
                return (Point) element;
            }
        }
        return creerPoint();
    }

    /**
     * Prévisualise le deuxième point de création de la droite.
     */
    private void previsualiserPoint2() {
        point2 = new PointConcret(gestionnaireOutils
                .getGestionnaireSelections().positionCurseurProperty());
    }

    /**
     * Prévisualise la droite à créer.
     */
    private void previsualiserDroite() {
        droite = new Droite(point1, point2);
        gestionnaireOutils.getElements().add(droite);
        for (final Forme forme : droite.getFormes()) {
            forme.setEnPrevisualisation(true);
        }
    }

    /**
     * Crée la droite passant par les points {@code point1} et {@code point2}.
     */
    private void creerDroite() {
        gestionnaireOutils.getElements().remove(droite);
        point2.positionInterneProperty().unbind();
        droite = new Droite(point1, point2);
        gestionnaireOutils.getElements().add(droite);
        gestionnaireOutils.getGestionnaireCommandes().ajouter(
                new CreerElement(gestionnaireOutils.getElements(), droite));
    }

}
