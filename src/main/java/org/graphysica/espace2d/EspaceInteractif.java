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
package org.graphysica.espace2d;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.forme.Forme;

/**
 * Une toile interactive permet à l'utilisateur d'interagir sur l'ensemble des
 * formes qu'elle affiche.
 *
 * @author Marc-Antoine Ouimet
 */
public class EspaceInteractif extends Espace {

    /**
     * Le facteur de zoom utilisé pour zoomer la toile.
     */
    private static final double FACTEUR_ZOOM = 1.1;

    /**
     * La position virtuelle précédente du curseur.
     */
    private final ObjectProperty<Vector2D> positionPrecendenteCurseur
            = new SimpleObjectProperty<>();

    /**
     * La position virtuelle actuelle du curseur.
     */
    private final ObjectProperty<Vector2D> positionActuelleCurseur
            = new SimpleObjectProperty<>();

    /**
     * La position réelle actuelle du curseur.
     */
    private final ObjectProperty<Vector2D> positionReelleCurseur
            = new SimpleObjectProperty<>();

    /**
     * Construit un espace 2D interactif aux dimensions virtuelles définies.
     *
     * @param largeur la largeur virtuelle de l'espace.
     * @param hauteur la hauteur virtuelle de l'espace.
     */
    public EspaceInteractif(final double largeur, final double hauteur) {
        super(largeur, hauteur);
    }

    {
        positionActuelleCurseur.addListener(
                (@NotNull final Observable observable) -> {
                    setPositionReelleCurseur(repere.positionReelle(
                            getPositionActuelleCurseur()));
                });
        setOnMouseEntered((@NotNull final MouseEvent evenement) -> {
            setCursor(Cursor.CROSSHAIR);
        });
        setOnMouseMoved((@NotNull final MouseEvent evenement) -> {
            actualiserPositionsCurseur(evenement);
        });
        setOnScroll((@NotNull final ScrollEvent evenement) -> {
            zoomer(evenement.getDeltaY());
        });
        setOnMousePressed((@NotNull final MouseEvent evenement) -> {
            if (evenement.isMiddleButtonDown()) {
                setCursor(Cursor.CLOSED_HAND);
            }
        });
        setOnMouseReleased((@NotNull final MouseEvent evenement) -> {
            setCursor(Cursor.CROSSHAIR);
        });
        setOnMouseDragged((@NotNull final MouseEvent evenement) -> {
            actualiserPositionsCurseur(evenement);
            if (evenement.isMiddleButtonDown()) {
                defiler();
            }
        });
        setOnMouseDragReleased((@NotNull final MouseEvent evenement) -> {
            final Vector2D origineVirtuelle = repere.getOrigineVirtuelle();
            repere.setOrigineVirtuelle(new Vector2D(
                    (int) origineVirtuelle.getX(),
                    (int) origineVirtuelle.getY()));
        });
    }

    /**
     * Récupère les formes sélectionnées par l'utilisateur en ordre croissant de
     * distance au curseur en considérant l'ordre de rendu des formes. Ce
     * faisant, la forme qui correspond le plus à une sélection ponctuelle de la
     * part de l'utilisateur est le premier élément de l'ensemble récupéré. Il
     * sera possible de déterminer l'intersection entre les premières formes de
     * cet ensemble pour l'outil d'intersection en traversant l'ensemble pour y
     * trouver les deux premières droites ou deux premières figures
     * d'intersection. Il est fort probable que cet ensemble soit vide.
     *
     * @return les formes sélectionnées en ordre croissant de distance.
     */
    public Set<Forme> formesSelectionnees() {
        // Ajouter les formes dans l'ordre inverse
        final List<Forme> formesSelectionnees = new ArrayList<>();
        final Map<Forme, Double> distances = distancesFormes();
        for (final Class classe : ordreRendu) {
            // Retenir les formes de la classe
            final List<Map.Entry<Forme, Double>> formesRetenues
                    = new ArrayList<>();
            for (final Map.Entry<Forme, Double> entree : distances.entrySet()) {
                if (classe.isInstance(entree.getKey())) {
                    formesRetenues.add(entree);
                }
            }
            // Trier en ordre décroissant
            formesRetenues.sort((forme1, forme2)
                    -> forme2.getValue().compareTo(forme1.getValue()));
            for (final Map.Entry<Forme, Double> entree : formesRetenues) {
                formesSelectionnees.add(entree.getKey());
            }
        }
        Collections.reverse(formesSelectionnees);
        return new LinkedHashSet<>(formesSelectionnees);
    }

    /**
     * Récupère les distances entre la position actuelle du curseur et les
     * formes sélectionnées.
     *
     * @return l'association des distances aux formes.
     */
    private Map<Forme, Double> distancesFormes() {
        final Map<Forme, Double> distances = new HashMap<>();
        for (final Forme forme : formes) {
            if (forme.isSelectionne(getPositionActuelleCurseur(), repere)) {
                distances.put(forme, forme.distance(
                        getPositionActuelleCurseur(), repere));
            }
        }
        return distances;
    }

    /**
     * Zoome l'espace de la toile vers la position définie du curseur. Un zoom a
     * lieu si le défilement vertical est positif, un dézoom a lieu si le
     * défilement vertical est négatif, et aucun zoom n'a lieu si le défilement
     * vertical est nul.
     *
     * @param defilementVertical le défilement vertical du zoom.
     */
    public void zoomer(final double defilementVertical) {
        if (defilementVertical != 0) {
            final Vector2D translationOrigine = getPositionActuelleCurseur()
                    .subtract(repere.getOrigineVirtuelle());
            repere.setOrigineVirtuelle(
                    repere.getOrigineVirtuelle().add(translationOrigine));
            double facteurZoom = FACTEUR_ZOOM;
            if (defilementVertical < 0) {
                facteurZoom = 1 / FACTEUR_ZOOM;
            }
            repere.setEchelle(repere.getEchelle().scalarMultiply(
                    facteurZoom));
            final Vector2D nouvelleOrigine = repere.getOrigineVirtuelle()
                    .subtract(translationOrigine.scalarMultiply(facteurZoom));
            repere.setOrigineVirtuelle(
                    new Vector2D((int) nouvelleOrigine.getX(),
                            (int) nouvelleOrigine.getY()));
        }
    }

    /**
     * Défile l'espace de la toile selon la variation des positions du curseur.
     */
    private void defiler() {
        final Vector2D deplacement = getPositionActuelleCurseur()
                .subtract(getPositionPrecedenteCurseur());
        repere.setOrigineVirtuelle(repere.getOrigineVirtuelle()
                .add(deplacement));
    }

    /**
     * Actualise les positions virtuelles précédente et actuelle du curseur.
     */
    private void actualiserPositionsCurseur(
            @NotNull final MouseEvent evenement) {
        final Vector2D positionCaptee
                = capterPositionActuelleCurseur(evenement);
        if (getPositionActuelleCurseur() == null) {
            setPositionActuelleCurseur(positionCaptee);
        }
        setPositionPrecedenteCurseur(getPositionActuelleCurseur());
        setPositionActuelleCurseur(positionCaptee);
    }

    /**
     * Capte la position virtuelle et actuelle du curseur à partir d'un
     * événement de la souris.
     *
     * @param evenement l'événement de la souris.
     * @return la position virtuelle du curseur.
     */
    private Vector2D capterPositionActuelleCurseur(
            @NotNull final MouseEvent evenement) {
        return new Vector2D(evenement.getX(), evenement.getY());
    }

    private Vector2D getPositionPrecedenteCurseur() {
        return positionPrecendenteCurseur.getValue();
    }

    private void setPositionPrecedenteCurseur(
            @NotNull final Vector2D positionPrecedenteCurseur) {
        this.positionPrecendenteCurseur.setValue(positionPrecedenteCurseur);
    }

    private Vector2D getPositionActuelleCurseur() {
        return positionActuelleCurseur.getValue();
    }

    private void setPositionActuelleCurseur(
            @NotNull final Vector2D positionActuelleCurseur) {
        this.positionActuelleCurseur.setValue(positionActuelleCurseur);
    }

    public final Vector2D getPositionReelleCurseur() {
        return positionReelleCurseur.getValue();
    }

    private void setPositionReelleCurseur(
            @NotNull final Vector2D positionReelleCurseur) {
        this.positionReelleCurseur.setValue(positionReelleCurseur);
    }

    /**
     * Récupère la propriété de position réelle du curseur dans l'espace. Permet
     * de lier des positions de forme à la position du curseur pour la
     * prévisualisation ou le déplacement de formes.
     *
     * @return la propriété de position réelle du curseur.
     */
    public final ObjectProperty<Vector2D> positionReelleCurseurProperty() {
        return positionReelleCurseur;
    }

}
