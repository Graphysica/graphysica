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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Une toile interactive permet à l'utilisateur d'interagir sur l'ensemble des
 * formes qu'elle affiche.
 *
 * @author Marc-Antoine Ouimet
 */
public class ToileInteractive extends ToileRedimensionnable {

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
     * La position virtuelle du curseur sur cette toile interactive.
     */
    private final ObjectProperty<Vector2D> positionVirtuelleCurseur 
           = new SimpleObjectProperty<>();

    public ToileInteractive(final double largeur, final double hauteur) {
        super(largeur, hauteur);
    }

    {
        setOnMouseMoved((@NotNull final MouseEvent evenement) -> {
            positionVirtuelleCurseur.setValue(
                    positionVirtuelleCurseur(evenement));
        });
        setOnMouseEntered((@NotNull final MouseEvent evenement) -> {
            setCursor(Cursor.CROSSHAIR);
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
                enregistrerPositionCurseur(evenement);
            }
        });
        setOnMouseReleased((@NotNull final MouseEvent evenement) -> {
            setCursor(Cursor.CROSSHAIR);
        });
        setOnMouseDragged((@NotNull final MouseEvent evenement) -> {
            if (evenement.isMiddleButtonDown()) {
                final Vector2D positionCurseur = new Vector2D(evenement.getX(),
                        evenement.getY());
                deplacer(positionCurseur);
            }
        });
    }

    /**
     * Zoome l'espace de la toile vers la position définie du curseur. Un zoom a
     * lieu si le défilement vertical est positif, un dézoom a lieu si le
     * défilement vertical est négatif, et aucun zoom n'a lieu si le défilement
     * vertical est nul.
     *
     * @param defilementVertical le défilement vertical du zoom.
     * @param positionCurseur la position virtuelle du curseur sur la toile.
     */
    private void zoomer(final double defilementVertical,
            @NotNull final Vector2D positionCurseur) {
        if (defilementVertical != 0) {
            final Vector2D translationOrigine = positionCurseur
                    .subtract(origine.getValue());
            origine.setValue(origine.getValue().add(translationOrigine));
            double facteurZoom = FACTEUR_ZOOM;
            if (defilementVertical < 0) {
                facteurZoom = 1 / FACTEUR_ZOOM;
            }
            setEchelle(new Vector2D(
                    echelle.getValue().getX() * facteurZoom,
                    echelle.getValue().getY() * facteurZoom));
            final Vector2D nouvelleOrigine = origine.getValue().subtract(
                    translationOrigine.scalarMultiply(facteurZoom));
            origine.setValue(new Vector2D((int) nouvelleOrigine.getX(),
                    (int) nouvelleOrigine.getY()));
        }
    }

    /**
     * Déplace l'espace de la toile selon la variation des positions du curseur.
     *
     * @param positionCurseur la position virtuelle actuelle du curseur.
     * @see ToileInteractive#positionPrecendenteCurseur
     */
    private void deplacer(@NotNull final Vector2D positionCurseur) {
        final Vector2D deplacement = positionCurseur.subtract(
                positionPrecendenteCurseur);
        final Vector2D nouvelleOrigine = origine.getValue().add(deplacement);
        origine.setValue(new Vector2D((int) nouvelleOrigine.getX(),
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
    
    public final Vector2D getPositionVirtuelleCurseur() {
        return positionVirtuelleCurseur.getValue();
    }
    
    public final ObjectProperty<Vector2D> positionVirtuelleCurseurProperty() {
        return positionVirtuelleCurseur;
    }
    
}