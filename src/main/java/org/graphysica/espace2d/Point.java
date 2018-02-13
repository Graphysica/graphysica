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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 *
 * @author Marc-Antoine Ouimet
 */
public class Point extends Forme {

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
    private final ObjectProperty<Vector2D> position
            = new SimpleObjectProperty<>(new Vector2D(0, 0));

    /**
     * La taille de la bordure du dessin du point exprimée en pixels.
     */
    private static final int TAILLE_BORDURE = 1;
    
    /**
     * La taille du point.
     */
    private final ObjectProperty<Taille> taille 
            = new SimpleObjectProperty<>(Taille.pointParDefaut());

    public Point() {
    }

    public Point(final int taille) throws IllegalArgumentException {
        this.taille.setValue(new Taille(taille));
    }

    public Point(@NotNull final Color couleur) {
        super(couleur);
    }
    
    public Point(@NotNull final Vector2D position) {
        setPosition(position);
    }

    public Point(final int taille, @NotNull final Color couleur)
            throws IllegalArgumentException {
        super(couleur);
        this.taille.setValue(new Taille(taille));
    }

    public Point(@NotNull final Color couleur,
            final double abscisse, final double ordonnee) {
        super(couleur);
        position.setValue(new Vector2D(abscisse, ordonnee));
    }

    public Point(final int taille, @NotNull final Color couleur,
            final double abscisse, final double ordonnee)
            throws IllegalArgumentException {
        this(couleur, abscisse, ordonnee);
        this.taille.setValue(new Taille(taille));
    }

    public Point(@NotNull final Color couleur, @NotNull Vector2D position) {
        super(couleur);
        this.position.setValue(position);
    }

    public Point(final int taille, @NotNull final Color couleur,
            @NotNull Vector2D position) throws IllegalArgumentException {
        this(couleur, position);
        this.taille.setValue(new Taille(taille));
    }

    @Override
    public void dessiner(@NotNull final Toile toile) {
        dessinerRond(toile.getGraphicsContext2D(), 
                toile.positionVirtuelle(getPosition()));
    }
    
    private void dessinerRond(@NotNull final GraphicsContext contexteGraphique, 
            @NotNull final Vector2D positionVirtuelle) {
        contexteGraphique.setFill(COULEUR_BORDURE);
        //TODO: Déterminer la position graphique de l'objet dans l'espace
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

    public final void setPosition(final double abscisse, final double ordonnee) {
        position.setValue(new Vector2D(abscisse, ordonnee));
    }

    public double getAbscisse() {
        return position.getValue().getX();
    }

    public double getOrdonnee() {
        return position.getValue().getY();
    }
    
    private int getTaille() {
        return taille.getValue().getTaille();
    }

}
