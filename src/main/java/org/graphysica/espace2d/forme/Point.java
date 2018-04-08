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
package org.graphysica.espace2d.forme;

import com.sun.istack.internal.NotNull;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.position.Position;
import org.graphysica.espace2d.Repere;
import org.graphysica.espace2d.position.PositionReelle;
import static org.graphysica.espace2d.position.Type.VIRTUELLE;

/**
 * Un point représente une position dans l'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public class Point extends Forme {

    /**
     * La couleur de la bordure du point.
     */
    private static final Color COULEUR_BORDURE = Color.BLACK;

    /**
     * La position réelle du point exprimée en mètres selon la base canonique.
     */
    private final ObjectProperty<Position> position
            = new SimpleObjectProperty<>(new PositionReelle(Vector2D.ZERO));

    /**
     * La taille de la bordure du dessin du point exprimée en pixels.
     */
    private static final int TAILLE_BORDURE = 1;

    /**
     * La taille du point.
     */
    private final Taille taille = Taille.de("point");

    /**
     * Construit un point par défaut.
     */
    public Point() {
    }

    /**
     * Construit un point aux propriétés liées.
     *
     * @param position la propriété de position du point.
     * @param couleur la propriété de couleur du point.
     * @param taille la propriété de taille du point.
     */
    public Point(@NotNull final ObjectProperty<? extends Position> position,
            @NotNull final ObjectProperty<Color> couleur,
            @NotNull final Taille taille) {
        super(couleur);
        positionProperty().bind(position);
        tailleProperty().bind(taille);
    }

    {
        proprietesActualisation.add(position);
        proprietesActualisation.add(taille);
    }

    @Override
    public void dessinerNormal(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        final Vector2D centre = getPosition().virtuelle(repere);
        dessinerRond(toile, centre, COULEUR_BORDURE,
                getTaille() + TAILLE_BORDURE);
        dessinerRond(toile, centre, getCouleur(), getTaille());
    }

    @Override
    public void dessinerSurbrillance(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        final int rayon = getTaille() + 6;
        dessinerRond(toile, getPosition().virtuelle(repere),
                getCouleur().deriveColor(1, 1, 1, 0.3), rayon);
    }

    /**
     * Dessine un rond sur une toile centré à une position virtuelle, de couleur
     * et de rayon virtuel spécifiés.
     *
     * @param toile la toile surlaquelle dessiner le rond.
     * @param positionVirtuelle la position virtuelle du centre du rond.
     * @param couleur la couleur du rond.
     * @param rayon le rayon du rond.
     */
    private static void dessinerRond(@NotNull final Canvas toile,
            @NotNull final Vector2D positionVirtuelle,
            @NotNull final Color couleur, final double rayon) {
        final GraphicsContext contexteGraphique = toile.getGraphicsContext2D();
        contexteGraphique.setFill(couleur);
        contexteGraphique.fillOval(positionVirtuelle.getX() - rayon,
                positionVirtuelle.getY() - rayon, 2 * rayon, 2 * rayon);
    }

    /**
     * Calcule la distance virtuelle entre ce point et la position du curseur
     * pour déterminer la sélection de l'utilisateur.
     *
     * @param curseur la position du curseur.
     * @param repere le repère d'espace.
     * @return la distance virtuelle entre ce point et la position du curseur.
     */
    @Override
    public double distance(@NotNull final Position curseur,
            @NotNull final Repere repere) {
        return Math.max(0, getPosition().distance(curseur, VIRTUELLE, repere)
                - getTaille());
    }

    protected final ObjectProperty<Position> positionProperty() {
        return position;
    }

    protected final Position getPosition() {
        return position.getValue();
    }

    protected final Taille tailleProperty() {
        return taille;
    }

    protected final int getTaille() {
        return taille.getValue();
    }

}
