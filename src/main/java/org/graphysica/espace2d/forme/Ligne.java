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
import org.graphysica.espace2d.position.Position;
import org.graphysica.espace2d.position.PositionReelle;

/**
 * Une ligne a une épaisseur et une couleur par défaut pour relier des points
 * dans l'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public abstract class Ligne extends Forme {

    /**
     * La couleur par défaut d'une droite.
     */
    static final Color COULEUR_PAR_DEFAUT = Color.BLACK;

    /**
     * L'épaisseur du tracé de la droite.
     */
    protected final Taille epaisseur = new Taille(Taille.de("ligne"));

    /**
     * La position d'origine de la trace de la ligne dans le contexte graphique.
     */
    protected Position origineTrace;

    /**
     * La position de l'arrivée de la trace de la ligne dans le contexte
     * graphique.
     */
    protected Position arriveeTrace;

    /**
     * La position du premier point dans la ligne.
     */
    protected final ObjectProperty<Position> position1
            = new SimpleObjectProperty<>(new PositionReelle(Vector2D.ZERO));

    /**
     * La position du deuxième point dans la ligne.
     */
    protected final ObjectProperty<Position> position2
            = new SimpleObjectProperty<>(new PositionReelle(Vector2D.ZERO));

    public Ligne(@NotNull final ObjectProperty<? extends Position> position1,
            @NotNull final ObjectProperty<? extends Position> position2) {
        this.position1.bind(position1);
        this.position2.bind(position2);
    }

    {
        proprietes.add(epaisseur);
        proprietes.add(position1);
        proprietes.add(position2);
    }

    @Override
    public void dessinerNormal(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        calculerOrigineEtArrivee(toile, repere);
        dessinerLigne(toile, origineTrace.virtuelle(repere),
                arriveeTrace.virtuelle(repere), getCouleur(), getEpaisseur());
    }

    @Override
    public void dessinerSurvol(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        calculerOrigineEtArrivee(toile, repere);
        dessinerLigne(toile, origineTrace.virtuelle(repere),
                arriveeTrace.virtuelle(repere),
                getCouleur().deriveColor(1, 1, 1, 0.3),
                3 * getEpaisseur());
    }

    /**
     * Dessine une ligne définie par son origine et son arrivée virtuelles, sa
     * couleur et son épaisseur sur une toile. Dans le cas d'une droite,
     * l'origine et l'arrivée devraient se situer aux bords de la toile.
     *
     * @param toile la toile sur laquelle dessiner la ligne.
     * @param origine l'origine virtuelle de la ligne.
     * @param arrivee l'arrivée virtuelle de la ligne.
     * @param couleur la couleur de la ligne.
     * @param epaisseur l'épaisseur de la ligne.
     */
    protected static void dessinerLigne(@NotNull final Canvas toile,
            @NotNull final Vector2D origine, @NotNull final Vector2D arrivee,
            @NotNull final Color couleur, final double epaisseur) {
        final GraphicsContext contexteGraphique = toile.getGraphicsContext2D();
        contexteGraphique.setStroke(couleur);
        contexteGraphique.setLineWidth(epaisseur);
        contexteGraphique.strokeLine(origine.getX(), origine.getY(),
                arrivee.getX(), arrivee.getY());
    }

    /**
     * Calcule et actualise la position virtuelle de l'origine
     * {@code origineTrace} et de l'arrivée {@code arriveeTrace} de la trace de
     * cette ligne.
     *
     * @param toile la toile sur laquelle dessiner la ligne.
     * @param repere le repère de l'espace.
     */
    public abstract void calculerOrigineEtArrivee(
            @NotNull final Canvas toile, @NotNull final Repere repere);

    protected final int getEpaisseur() {
        return epaisseur.getValue();
    }

    protected final Taille epaisseurProperty() {
        return epaisseur;
    }

    protected final Position getPosition1() {
        return position1.getValue();
    }

    protected final Position getPosition2() {
        return position2.getValue();
    }

}
