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
import com.sun.istack.internal.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
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
     * La position virtuelle précédente du curseur, enregistrée lorsque le
     * bouton du milieu de la souris est enfoncé.
     */
    private Vector2D positionPrecendenteCurseur;

    /**
     * La position actuelle et réelle du curseur sur cette toile interactive.
     */
    private final ObjectProperty<Vector2D> positionReelleCurseur
            = new SimpleObjectProperty<>();

    public EspaceInteractif(final double largeur, final double hauteur) {
        super(largeur, hauteur);
    }

    {
        setOnMouseEntered((@NotNull final MouseEvent evenement) -> {
            setCursor(Cursor.CROSSHAIR);
        });
        setOnMouseMoved((@NotNull final MouseEvent evenement) -> {
            actualiserPositionActuelleCurseur(evenement);
            actualiserSurbrillance();
        });
        setOnScroll((@NotNull final ScrollEvent evenement) -> {
            final double defilementVertical = evenement.getDeltaY();
            final Vector2D positionCurseur = new Vector2D(evenement.getX(),
                    evenement.getY());
            zoomer(defilementVertical, positionCurseur);
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
            if (evenement.isMiddleButtonDown()) {
                final Vector2D positionCurseur = new Vector2D(evenement.getX(),
                        evenement.getY());
                defiler(positionCurseur);
            }
        });
    }

    private void actualiserSurbrillance() {
        for (final Forme forme : formes) {
            forme.setEnSurbrillance(false);
        }
        final Forme formeSelectionnee = formeSelectionnee();
        if (formeSelectionnee != null) {
            System.out.println(formeSelectionnee);
            formeSelectionnee.setEnSurbrillance(true);
        }
    }

    /**
     * Récupère la forme qui est actuellement sélectionnée par l'utilisateur.
     * Cette forme correspond à la forme dont la distance avec le curseur est
     * minimale. L'ordre de rendu supplante cette distance minimale. Il ne peut
     * donc y avoir qu'une seule forme sélectionnée à la fois. Un gestionnaire
     * de sélections pourra gérer les sélections multiples. Il arrivera souvent
     * qu'aucune forme ne soit sélectionnée.
     *
     * @return la forme sélectionnée.
     */
    @Nullable
    public Forme formeSelectionnee() {
        final Map<Forme, Double> distances = distancesFormes();
        System.out.println("distances = " + distances);
        final Map<Class, Map.Entry<Forme, Double>> entreesRetenues
                = new LinkedHashMap<>();
        for (final Class classe : ordreRendu) {
            for (final Map.Entry<Forme, Double> entree : distances.entrySet()) {
                if (classe.isInstance(entree.getKey())) {
                    if (!entreesRetenues.containsKey(classe)) {
                        entreesRetenues.put(classe, entree);
                        continue;
                    }
                    final Map.Entry<Forme, Double> entreePrecedente
                            = entreesRetenues.get(classe);
                    if (entree.getValue() < entreePrecedente.getValue()) {
                        entreesRetenues.put(classe, entree);
                    }
                }
            }
        }
        Map.Entry<Forme, Double> distanceMinimale = null;
        for (final Map.Entry<Class, Map.Entry<Forme, Double>> entree
                : entreesRetenues.entrySet()) {
            if (distanceMinimale == null) {
                distanceMinimale = entree.getValue();
                continue;
            }
            final Map.Entry<Forme, Double> distance = entree.getValue();
            if (distance.getValue() <= distanceMinimale.getValue()) {
                distanceMinimale = distance;
            }
        }
        if (distanceMinimale != null) {
            return distanceMinimale.getKey();
        }
        return null;
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
            if (forme.isSelectionne(repere.positionVirtuelle(
                    getPositionReelleCurseur()), repere)) {
                distances.put(forme, forme.distance(repere.positionVirtuelle(
                        getPositionReelleCurseur()), repere));
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
     * @param defilementVertical le défilement vertical du zoom. S'il est
     * négatif, l'espace est dézoomé. S'il est positif, l'espace est zoomé. S'il
     * est nul, l'espace reste tel qu'il est.
     * @param positionCurseur la position virtuelle du curseur sur la toile.
     */
    public void zoomer(final double defilementVertical,
            @NotNull final Vector2D positionCurseur) {
        if (defilementVertical != 0) {
            final Vector2D translationOrigine = positionCurseur
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
     *
     * @param positionCurseur la position virtuelle actuelle du curseur.
     * @see EspaceInteractif#positionPrecendenteCurseur
     */
    private void defiler(@NotNull final Vector2D positionCurseur) {
        final Vector2D deplacement = positionCurseur.subtract(
                positionPrecendenteCurseur);
        final Vector2D nouvelleOrigine = repere.getOrigineVirtuelle()
                .add(deplacement);
        repere.setOrigineVirtuelle(new Vector2D((int) nouvelleOrigine.getX(),
                (int) nouvelleOrigine.getY()));
        positionPrecendenteCurseur = positionCurseur;
    }

    /**
     * Enregistre la position précédente du curseur pour le déplacement de
     * l'espace.
     */
    private void enregistrerPositionCurseur(
            @NotNull final MouseEvent evenement) {
        positionPrecendenteCurseur = positionVirtuelleCurseur(evenement);
    }

    /**
     * Récupère la position virtuelle du curseur sur la toile.
     *
     * @param evenement l'événement du curseur.
     * @return la position virtuelle du curseur.
     */
    public Vector2D positionVirtuelleCurseur(
            @NotNull final MouseEvent evenement) {
        return new Vector2D(evenement.getX(), evenement.getY());
    }

    /**
     * Récupère la position réelle du curseur sur la toile.
     *
     * @param evenement l'événement du curseur.
     * @return la position réelle du curseur.
     */
    public Vector2D positionReelleCurseur(
            @NotNull final MouseEvent evenement) {
        return repere.positionReelle(positionVirtuelleCurseur(evenement));
    }

    /**
     * Actualise la position actuelle et réelle du curseur sur la toile.
     */
    private void actualiserPositionActuelleCurseur(
            @NotNull final MouseEvent evenement) {
        enregistrerPositionCurseur(evenement);
        positionReelleCurseur.setValue(positionReelleCurseur(evenement));
    }

    public final Vector2D getPositionReelleCurseur() {
        return positionReelleCurseur.getValue();
    }

    public final ObjectProperty<Vector2D> positionReelleCurseurProperty() {
        return positionReelleCurseur;
    }

}
