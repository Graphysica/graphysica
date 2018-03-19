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
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Toile;

/**
 *
 * @author Marc-Antoine
 */
public class Axe extends Forme {

    /**
     * L'espacement minimum des graduations de l'axe exprimée en pixels.
     */
    private final ObjectProperty<Vector2D> espacement
            = new SimpleObjectProperty<>();

    public enum Sens {
        VERTICAL,
        HORIZONTAL
    }

    private final Sens sens;

    public Axe(@NotNull final Sens sens, @NotNull final Vector2D espacement) {
        this.sens = sens;
        setEspacement(espacement);
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
        // Le format des étiquettes
        final String formatX;
        if (exposant.getX() >= 0) {
            formatX = "%.0f";
        } else {
            formatX = "%." + (int) (- exposant.getX()) + "f";
        }
        final String formatY;
        if (exposant.getY() >= 0) {
            formatY = "%.0f";
        } else {
            formatY = "%." + (int) (- exposant.getY()) + "f";
        }
        // Dessiner l'axe
        double x = positionAncrageGrille.getX();
        double y = positionAncrageGrille.getY();
        switch (sens) {
            case HORIZONTAL: {
                final Fleche fleche = new Fleche(
                        new Vector2D(toile.abscisseReelle(0),
                                toile.ordonneeReelle(origine.getY())), 
                        new Vector2D(toile.abscisseReelle(toile.getWidth()), 
                                toile.ordonneeReelle(origine.getY())));
                fleche.dessiner(toile);
                while (x < toile.getWidth()) {
                    final Etiquette etiquette = new Etiquette(
                            String.format(formatX, toile.abscisseReelle(x)),
                            toile.positionReelle(
                                    new Vector2D(x, origine.getY())),
                            Vector2D.ZERO,
                            14
                    );
                    etiquette.dessiner(toile);
                    x += espacementVirtuel.getX();
                }
                break;
            }
            case VERTICAL: {
                final Fleche fleche = new Fleche(
                        new Vector2D(toile.abscisseReelle(origine.getX()),
                                toile.ordonneeReelle(toile.getHeight())), 
                        new Vector2D(toile.abscisseReelle(origine.getX()), 
                                toile.ordonneeReelle(0)));
                fleche.dessiner(toile);
                while (y < toile.getHeight()) {
                    final Etiquette etiquette = new Etiquette(
                            String.format(formatY, toile.ordonneeReelle(y)),
                            toile.positionReelle(
                                    new Vector2D(origine.getX(), y)),
                            Vector2D.ZERO,
                            14
                    );
                    etiquette.dessiner(toile);
                    y += espacementVirtuel.getY();
                }
                break;
            }
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
