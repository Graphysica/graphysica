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
import org.graphysica.construction.Construction;
import org.graphysica.construction.Element;
import org.graphysica.construction.GestionnaireOutils;
import org.graphysica.construction.commande.CreerElement;
import org.graphysica.construction.mathematiques.Droite;
import org.graphysica.construction.mathematiques.Point;
import org.graphysica.construction.mathematiques.PointConcret;

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
                if (evenement.getEventType() == MouseEvent.MOUSE_PRESSED
                        && evenement.isPrimaryButtonDown()) {
                    point1 = determinerPoint();
                }
            } else {
                final Construction construction
                        = gestionnaireOutils.getConstruction();
                if (previsualisations.isEmpty()) {
                    previsualiserPoint2();
                    previsualiserDroite();
                    construction.getEspace().getFormes()
                            .addAll(previsualisations);
                } else if (evenement.getEventType()
                        == MouseEvent.MOUSE_RELEASED) {
                    aProchaineEtape = false;
                    point2 = determinerPoint();
                    creerDroite();
                    construction.getEspace().getFormes()
                            .removeAll(previsualisations);
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
        final Construction construction = gestionnaireOutils.getConstruction();
        final PointConcret point = new PointConcret(gestionnaireOutils
                .getGestionnaireSelections().positionReelleCurseur());
        gestionnaireOutils.getConstruction().executerCommande(
                new CreerElement(construction, point));
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
        final Set<Element> elementsSelectionnes = gestionnaireOutils
                .getGestionnaireSelections().getElementsSelectionnes();
        for (final Element element : elementsSelectionnes) {
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
        previsualisations.add(
                new org.graphysica.espace2d.forme.Point(gestionnaireOutils
                        .getGestionnaireSelections()
                        .positionCurseurProperty()));
    }

    /**
     * Prévisualise la droite à créer.
     */
    private void previsualiserDroite() {
        previsualisations.add(new org.graphysica.espace2d.forme.Droite(
                point1.positionInterneProperty(),
                gestionnaireOutils.getGestionnaireSelections()
                        .positionCurseurProperty()));
    }

    /**
     * Crée la droite passant par les points {@code point1} et {@code point2}.
     */
    private void creerDroite() {
        final Construction construction = gestionnaireOutils.getConstruction();
        final Droite droite = new Droite(point1, point2);
        construction.getEspace().getFormes().removeAll(previsualisations);
        gestionnaireOutils.getConstruction().executerCommande(
                new CreerElement(construction, droite));
    }

}
