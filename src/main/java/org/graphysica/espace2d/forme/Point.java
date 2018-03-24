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
import org.graphysica.espace2d.Repere;

/**
 * Un point représente une position dans l'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public final class Point extends Forme {

    /**
     * La couleur par défaut d'un point.
     */
    static final Color COULEUR_PAR_DEFAUT = Color.BLUE;

    /**
     * La couleur de la bordure du point.
     */
    private static final Color COULEUR_BORDURE = Color.BLACK;

    /**
     * La position par défaut d'un point.
     */
    private static final Vector2D POSITION_PAR_DEFAUT = Vector2D.ZERO;

    /**
     * La position réelle du point exprimée en mètres selon la base canonique.
     */
    private final ObjectProperty<Vector2D> position
            = new SimpleObjectProperty<>(POSITION_PAR_DEFAUT);

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
     * @param position la position réelle du point.
     */
    public Point(@NotNull final Vector2D position) {
        this();
        setPosition(position);
    }

    /**
     * Construit un point de taille définie par défaut à la position et de
     * couleur spécifiées.
     *
     * @param position la position réelle du point.
     * @param couleur la couleur du point.
     */
    public Point(@NotNull final Vector2D position, 
            @NotNull final Color couleur) {
        this(position);
        setCouleur(couleur);
    }

    /**
     * Construit un point dont la taille, la couleur et la position sont
     * définies.
     *
     * @param position la position réelle du point.
     * @param couleur la couleur du point.
     * @param taille la taille du point.
     */
    public Point(@NotNull final Vector2D position, 
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
    public Point(@NotNull final ObjectProperty<Vector2D> curseur) {
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
        dessinerRond(toile.getGraphicsContext2D(),
                repere.positionVirtuelle(getPosition()));
    }

    /**
     * Dessine le point en tant que rond.
     *
     * @param contexteGraphique le contexte graphique de dessin du point.
     * @param positionVirtuelle la position du point dans le contexte graphique.
     */
    private void dessinerRond(@NotNull final GraphicsContext contexteGraphique,
            @NotNull final Vector2D positionVirtuelle) {
        contexteGraphique.setFill(COULEUR_BORDURE);
        contexteGraphique.fillOval(
                positionVirtuelle.getX() - getTaille() - TAILLE_BORDURE,
                positionVirtuelle.getY() - getTaille() - TAILLE_BORDURE,
                2 * (getTaille() + TAILLE_BORDURE),
                2 * (getTaille() + TAILLE_BORDURE)
        );
        contexteGraphique.setFill(getCouleur());
        contexteGraphique.fillOval(
                positionVirtuelle.getX() - getTaille(),
                positionVirtuelle.getY() - getTaille(),
                2 * getTaille(),
                2 * getTaille()
        );
    }

    @Override
    public double distance(@NotNull final Vector2D curseur,
            @NotNull final Repere repere) {
        final Vector2D positionVirtuelle = repere.positionVirtuelle(
                getPosition());
        return positionVirtuelle.distance(curseur);
    }

    /**
     * Déplace le point selon un vecteur de déplcaement.
     *
     * @param deplacement le vecteur de déplacement du point.
     */
    public void deplacer(@NotNull final Vector2D deplacement) {
        setPosition(getPosition().add(deplacement));
    }

    public ObjectProperty<Vector2D> positionProperty() {
        return position;
    }

    public Vector2D getPosition() {
        return position.getValue();
    }

    public final void setPosition(@NotNull final Vector2D position) {
        this.position.setValue(position);
    }

    public double getAbscisse() {
        return position.getValue().getX();
    }

    public double getOrdonnee() {
        return position.getValue().getY();
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
