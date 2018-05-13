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
import com.sun.istack.internal.Nullable;
import java.util.Collection;
import java.util.Set;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.graphysica.construction.Element;
import org.graphysica.construction.GestionnaireOutils;
import org.graphysica.construction.commande.CreerElement;
import org.graphysica.construction.mathematiques.DroitePerpendiculaire;
import org.graphysica.construction.mathematiques.Ligne;
import org.graphysica.construction.mathematiques.Point;
import org.graphysica.construction.mathematiques.PointConcret;

/**
 * Un outil de création de droite perpendiculaire permet de créer une droite
 * perpendiculaire à une droite et passant par un point.
 *
 * @author Marc-Antoine Ouimet
 */
public class OutilCreationDroitePerpendiculaire extends OutilCreationElement {

    /**
     * La droite perpendiculaire créée par cet outil de création de droite.
     */
    private DroitePerpendiculaire droite;

    /**
     * Le point compris dans la droite perpendiculaire.
     */
    private Point point;

    /**
     * La ligne perpendiculaire à la droite.
     */
    private Ligne ligne;

    /**
     * Si la droite perpendiculaire est en prévisualisation.
     */
    private boolean enPrevisualisation = false;

    /**
     * Construit un outil de création de droite perpendiculaire au gestionnaire
     * d'outils défini.
     *
     * @param gestionnaireOutils le gestionnaire d'outils de cet outil de
     * création de droite perpendiculaire.
     */
    public OutilCreationDroitePerpendiculaire(
            @NotNull final GestionnaireOutils gestionnaireOutils) {
        super(gestionnaireOutils);
    }

    @Override
    public void gerer(@NotNull final MouseEvent evenement) {
        if (aProchaineEtape()) {
            if (ligne == null) {
                if (evenement.getButton() == MouseButton.PRIMARY
                        && evenement.getEventType()
                        == MouseEvent.MOUSE_PRESSED) {
                    ligne = recupererLigne();
                }
            } else if (!enPrevisualisation) {
                previsualiserDroiteParallele();
            } else if (evenement.getButton() == MouseButton.PRIMARY
                    && evenement.getEventType()
                    == MouseEvent.MOUSE_RELEASED) {
                aProchaineEtape = false;
                point = determinerPoint();
                creerDroiteParallele();
                gestionnaireOutils.finOutil();
            }
        }
    }

    @Override
    public Outil dupliquer() {
        return new OutilCreationDroitePerpendiculaire(gestionnaireOutils);
    }

    /**
     * Récupère la ligne à utiliser pour la création de la droite
     * perpendiculaire.
     *
     * @return la ligne actuellement en survol.
     */
    @Nullable
    private Ligne recupererLigne() {
        final Set<Element> elementsSurvoles = gestionnaireOutils
                .getGestionnaireSelections().getElementsSurvoles();
        for (final Element element : elementsSurvoles) {
            if (element instanceof Ligne) {
                return (Ligne) element;
            }
        }
        return null;
    }

    /**
     * Crée un point à l'emplacement réel du curseur.
     *
     * @return le point créé.
     */
    private Point creerPoint() {
        final PointConcret point = new PointConcret(gestionnaireOutils
                .getGestionnaireSelections().positionReelleCurseur());
        gestionnaireOutils.getElements().add(point);
        gestionnaireOutils.getGestionnaireCommandes().ajouter(
                new CreerElement(gestionnaireOutils.getElements(), point));
        return point;
    }

    /**
     * Détermine le point à utiliser pour la création de la droite
     * perpendiculaire. Crée un point à l'emplacement réel du curseur si aucun
     * autre point n'est sélectionné par l'utilisateur.
     *
     * @return le point déterminé par l'utilisateur.
     */
    private Point determinerPoint() {
        final Set<Element> elementsSurvoles = gestionnaireOutils
                .getGestionnaireSelections().getElementsSurvoles();
        for (final Element element : elementsSurvoles) {
            if (element instanceof Point && element != point) {
                return (Point) element;
            }
        }
        return creerPoint();
    }

    /**
     * Prévisualise la droite perpendiculaire à créer.
     */
    private void previsualiserDroiteParallele() {
        droite = new DroitePerpendiculaire(ligne, gestionnaireOutils
                .getGestionnaireSelections().positionCurseurProperty());
        gestionnaireOutils.getElements().add(droite);
        enPrevisualisation = true;
        droite.getFormes().stream().forEach((forme) -> {
            forme.setEnPrevisualisation(true);
        });
    }

    /**
     * Crée la droite passant par le point {@code point} et perpendiculaire à la
     * ligne {@code ligne}.
     */
    private void creerDroiteParallele() {
        droite.positionInterne1Property().unbind();
        droite.positionInterne1Property().bind(
                point.positionInterneProperty());
        droite.setPoint(point);
        gestionnaireOutils.getGestionnaireCommandes().ajouter(
                new CreerElement(gestionnaireOutils.getElements(), droite));
    }

    @Override
    public void interrompre() {
        droite.positionInterne2Property().unbind();
        final Collection<Element> elements = gestionnaireOutils.getElements();
        elements.remove(droite);
    }

    @Override
    public boolean isEnCours() {
        return ligne != null && aProchaineEtape;
    }

}
