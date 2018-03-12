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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Toile;

/**
 * Une grille permet de subdiviser l'espace selon un espacement virtuel qui
 * correspond à un pourcentage entier de son échelle. La grille n'est pas
 * dessinée à l'aide d'instances de droites horizontales et verticales par
 * soucis de gestion de mémoire.
 *
 * @author Marc-Antoine Ouimet
 */
public final class Grille extends Forme {

    /**
     * L'espacement minimum des graduations de la grille exprimée en pixels.
     */
    private final ObjectProperty<Vector2D> espacement
            = new SimpleObjectProperty<>();

    /**
     * L'épaisseur du tracé de la grille.
     */
    private final IntegerProperty epaisseur = Taille.de("grille");

    /**
     * Construit une grille dont l'espacement et la couleur sont définis.
     *
     * @param espacement l'espacement virtuel entre chaque graduation de la
     * grille.
     * @param couleur la couleur de la grille.
     */
    public Grille(@NotNull final Vector2D espacement,
            @NotNull final Color couleur) {
        setEspacement(espacement);
        setCouleur(couleur);
    }

    {
        proprietesActualisation.add(epaisseur);
    }

    @Override
    public void dessiner(@NotNull final Toile toile) {
        final Vector2D origine = toile.getOrigine();
        final Vector2D echelle = toile.getEchelle();
        final Vector2D espacementMinimal = getEspacement();
        final Vector2D espacementMinimalReel = new Vector2D(
                espacementMinimal.getX() / echelle.getX(),
                espacementMinimal.getY() / echelle.getY());
        final Vector2D exposant = new Vector2D(
                (int) (Math.log(espacementMinimalReel.getX()) / Math.log(2)),
                (int) (Math.log(espacementMinimalReel.getY()) / Math.log(2)));
        final Vector2D espacementReel = new Vector2D(
                Math.pow(2, exposant.getX()), Math.pow(2, exposant.getY()));
        // L'espacement virtuel entre les graduations de la grille
        final Vector2D espacementVirtuel = new Vector2D(
                espacementReel.getX() * echelle.getX(),
                espacementReel.getY() * echelle.getY());
        // La position d'ancrage de la grille dans le coin supérieur gauche
        final Vector2D positionAncrageGrille = new Vector2D(
                origine.getX() % espacementVirtuel.getX(),
                origine.getY() % espacementVirtuel.getY());
        // Tracer la grille
        final GraphicsContext contexteGraphique = toile.getGraphicsContext2D();
        contexteGraphique.setStroke(getCouleur());
        contexteGraphique.setLineWidth(epaisseur.getValue());
        double x = positionAncrageGrille.getX();
        while (x < toile.getWidth()) {
            contexteGraphique.strokeLine(x, 0, x, toile.getHeight());
            x += espacementVirtuel.getX();
        }
        double y = positionAncrageGrille.getY();
        while (y < toile.getHeight()) {
            contexteGraphique.strokeLine(0, y, toile.getWidth(), y);
            y += espacementVirtuel.getY();
        }
    }

    public final Vector2D getEspacement() {
        return espacement.getValue();
    }

    public final void setEspacement(@NotNull final Vector2D espacement) {
        this.espacement.setValue(espacement);
    }

    public final ObjectProperty<Vector2D> espacementProperty() {
        return espacement;
    }

}
