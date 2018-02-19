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
import javafx.scene.canvas.GraphicsContext;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 *
 * @author Marc-Antoine Ouimet
 */
public class SegmentDroite extends Droite {

    /**
     * Construit un segment de droite lié à la position d'un point d'origine et
     * d'un point d'arrivée.
     *
     * @param origine le point d'origine du segment de droite.
     * @param arrivee le point d'arrivée du segment de droite.
     */
    public SegmentDroite(@NotNull final Point origine,
            @NotNull final Point arrivee) {
        super(origine, arrivee);
        this.point1.setValue(origine.getPosition());
        this.point1.bind(origine.positionProperty());
        this.point2.setValue(arrivee.getPosition());
        this.point2.bind(arrivee.positionProperty());
    }

    @Override
    public void dessiner(@NotNull final Toile toile) {
        final Vector2D point1Virtuel = toile.positionVirtuelle(getOrigine());
        final Vector2D point2Virtuel = toile.positionVirtuelle(getArrivee());
        //TODO: Implémenter le dessin en pointillé
        dessinerContinue(toile.getGraphicsContext2D(),
                point1Virtuel, point2Virtuel);
    }

    /**
     * Dessine le segment de droite en tant que ligne continue aux point
     * d'origine et d'arrivée virtuelles définis.
     *
     * @param contexteGraphique le contexte de dessin.
     * @param origineVirtuelle l'origine virtuelle du segment de droite dans le
     * contexte graphique.
     * @param arriveeVirtuelle l'arrivée virtuelle du segment de droite dans le
     * contexte graphique.
     */
    private void dessinerContinue(
            @NotNull final GraphicsContext contexteGraphique,
            @NotNull final Vector2D origineVirtuelle,
            @NotNull final Vector2D arriveeVirtuelle) {
        contexteGraphique.setStroke(COULEUR_PAR_DEFAUT);
        contexteGraphique.setLineWidth(getEpaisseur());
        contexteGraphique.strokeLine(origineVirtuelle.getX(), 
                origineVirtuelle.getY(), arriveeVirtuelle.getX(), 
                arriveeVirtuelle.getY());
    }

    public Vector2D getOrigine() {
        return point1.getValue();
    }

    public Vector2D getArrivee() {
        return point2.getValue();
    }

}
