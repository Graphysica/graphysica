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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Repere;

/**
 * Une aire représente un polygone formé d'une séquence de points dans l'espace.
 * Pour représenter un polygone et ses côtés, il faudra utiliser à la fois une
 * aire et des segments de droite.
 *
 * @author Marc-Antoine Ouimet
 */
public class Aire extends Forme {

    /**
     * La couleur par défaut d'une aire.
     */
    private static final Color COULEUR_PAR_DEFAUT = new Color(0.72, 0.52,
            0.04, 0.4);

    /**
     * La liste ordonnée des points délimitant cette aire.
     */
    private final ObservableList<ObjectProperty<Vector2D>> points
            = FXCollections.observableArrayList();

    // TODO: Constructeur d'aire de prévisualisation
    
    /**
     * Construit une aire sur un ensemble défini de points.
     *
     * @param points l'ensemble de points traçant le polygone de l'aire.
     */
    public Aire(@NotNull final Point... points) {
        for (final Point point : points) {
            this.points.add(point.positionProperty());
        }
        setCouleur(COULEUR_PAR_DEFAUT);
    }

    /**
     * Construit une prévisualisation d'aire définie sur un ensemble de points
     * et la position du curseur.
     *
     * @param points l'ensemble des points définis de l'aire à prévisualiser.
     * @param curseur la position réelle du curseur correspondant au prochain
     * point de {@code points}.
     */
    public Aire(@NotNull final ObjectProperty<Vector2D> curseur,
            @NotNull final Point... points) {
        this(points);
        this.points.add(curseur);
    }

    {
        proprietesActualisation.add(this.points);
    }

    @Override
    public void dessiner(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        final GraphicsContext contexteGraphique = toile.getGraphicsContext2D();
        contexteGraphique.setFill(getCouleur());
        contexteGraphique.fillPolygon(repere.abscissesVirtuelles(abscisses()),
                repere.ordonneesVirtuelles(ordonnees()), points.size());
    }

    /**
     * Récupère l'ensemble des abscisses des points dans leur séquence.
     *
     * @return les abscisses de points.
     */
    private double[] abscisses() {
        final double[] abscisses = new double[points.size()];
        for (int i = 0; i < abscisses.length; i++) {
            abscisses[i] = points.get(i).getValue().getX();
        }
        return abscisses;
    }

    /**
     * Récupère l'ensemble des ordonnées des points dans leur séquence.
     *
     * @return les ordonnées de points.
     */
    private double[] ordonnees() {
        final double[] ordonnees = new double[points.size()];
        for (int i = 0; i < ordonnees.length; i++) {
            ordonnees[i] = points.get(i).getValue().getY();
        }
        return ordonnees;
    }

}
