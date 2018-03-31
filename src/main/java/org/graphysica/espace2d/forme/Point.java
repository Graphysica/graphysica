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
import org.graphysica.espace2d.position.Type;
import static org.graphysica.espace2d.position.Type.VIRTUELLE;

/**
 * Un point représente une position dans l'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public final class Point extends Forme implements Deplaceable {

    /**
     * La couleur par défaut d'un point.
     */
    static final Color COULEUR_PAR_DEFAUT = Color.BLUE;

    /**
     * La couleur de la bordure du point.
     */
    private static final Color COULEUR_BORDURE = Color.BLACK;

    /**
     * La position réelle du point exprimée en mètres selon la base canonique.
     */
    private final ObjectProperty<Position> position
            = new SimpleObjectProperty<>();

    /**
     * La taille de la bordure du dessin du point exprimée en pixels.
     */
    private static final int TAILLE_BORDURE = 1;

    /**
     * La taille du point.
     */
    private final Taille taille = Taille.de("point");

    /**
     * Construit un point de couleur, de taille et de position définies par
     * défaut.
     */
    public Point() {
        super(COULEUR_PAR_DEFAUT);
    }

    /**
     * Construit un point de couleur et de taille définies par défaut à la
     * position spécifiée.
     *
     * @param position la position du point.
     */
    public Point(@NotNull final Position position) {
        this();
        setPosition(position);
    }

    /**
     * Construit un point de taille définie par défaut à la position et de
     * couleur spécifiées.
     *
     * @param position la position du point.
     * @param couleur la couleur du point.
     */
    public Point(@NotNull final Position position,
            @NotNull final Color couleur) {
        this(position);
        setCouleur(couleur);
    }

    /**
     * Construit un point dont la taille, la couleur et la position sont
     * définies.
     *
     * @param position la position du point.
     * @param couleur la couleur du point.
     * @param taille la taille du point.
     */
    public Point(@NotNull final Position position,
            @NotNull final Color couleur, final int taille) {
        this(position, couleur);
        this.taille.setValue(taille);
    }

    /**
     * Construit une prévisualisation de point positionné à la position du
     * curseur.
     *
     * @param curseur la position réelle du curseur.
     */
    public Point(@NotNull final ObjectProperty<Position> curseur) {
        setCouleur(COULEUR_PAR_DEFAUT);
        position.bind(curseur);
    }

    {
        proprietesActualisation.add(position);
        proprietesActualisation.add(taille);
    }

    @Override
    public void dessiner(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        if (isEnSurbrillance()) {
            dessinerSurbrillance(toile, repere);
        }
        dessinerRond(toile, getPosition().virtuelle(repere),
                COULEUR_BORDURE, getTaille() + TAILLE_BORDURE);
        dessinerRond(toile, getPosition().virtuelle(repere),
                getCouleur(), getTaille());
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

    @Override
    public double distance(@NotNull final Position curseur,
            @NotNull final Repere repere) {
        return Math.max(0, 
                getPosition().distance(curseur, VIRTUELLE, repere) - 5);
    }

    @Override
    public void deplacer(@NotNull final Vector2D deplacement,
            @NotNull final Type type, @NotNull final Repere repere) {
        getPosition().deplacer(deplacement, type, repere);
    }

    public ObjectProperty<Position> positionProperty() {
        return position;
    }

    public Position getPosition() {
        return position.getValue();
    }

    public final void setPosition(@NotNull final Position position) {
        this.position.setValue(position);
    }

    private int getTaille() {
        return taille.getValue();
    }

    public void setTaille(final int taille) {
        this.taille.setValue(taille);
    }

    public Taille tailleProperty() {
        return taille;
    }

}
