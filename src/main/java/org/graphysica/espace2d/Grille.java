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

import org.graphysica.espace2d.forme.Taille;
import org.graphysica.espace2d.forme.Forme;
import com.sun.istack.internal.NotNull;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * //TODO: Retravailler... Peut-être avec des manipulations bitwise.
 *
 * @author Marc-Antoine Ouimet
 */
public final class Grille extends Forme {

    private static final Color COULEUR_PAR_DEFAUT = new Color(0, 0, 0, 0.3);

    /**
     * L'espacement minimum des graduations de la grille exprimée en pixels.
     */
    private final ObjectProperty<Vector2D> espacement 
            = new SimpleObjectProperty<>();

    /**
     * L'épaisseur du tracé de la grille.
     */
    private final ObjectProperty<Taille> epaisseur
            = new SimpleObjectProperty<>(Taille.de("grille"));

    public Grille(@NotNull final Vector2D espacement) {
        this(espacement, COULEUR_PAR_DEFAUT);
    }
    
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
        final Vector2D origineVirtuelle = toile.positionVirtuelle(origine);
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
        final Vector2D espacementVirtuel = new Vector2D(
                espacementReel.getX() * echelle.getX(), 
                espacementReel.getY() * echelle.getY());
        final Vector2D positionAncrageGrille = new Vector2D(
                origineVirtuelle.getX() % espacementVirtuel.getX(), 
                origineVirtuelle.getY() % espacementVirtuel.getY());
        final GraphicsContext contexteGraphique = toile.getGraphicsContext2D();
        contexteGraphique.setStroke(getCouleur());
        contexteGraphique.setLineWidth(1);
        double x = positionAncrageGrille.getX();
        while (x < toile.getWidth()) {
            contexteGraphique.strokeLine(x, 0, x, toile.getHeight());
            x += espacementVirtuel.getX();
        }
        double y = positionAncrageGrille.getY();
        while (y < toile.getHeight()) {
            contexteGraphique.strokeLine(0, y, toile.getWidth(), y);
            y+= espacementVirtuel.getY();
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
