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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Toile;

/**
 * Un secteur est une région d'un disque, définie entre deux angles.
 *
 * @author Marc-Antoine
 * @deprecated 
 */
public class Secteur extends Forme {

    /**
     * Le multiplicateur de conversion de la taille du secteur à son rayon.
     */
    private static final int MULTIPLICATEUR_RAYON = 4;

    /**
     * La taille du secteur. Le rayon du tracé du secteur correspond à
     * {@code MULTIPLICATEUR_RAYON} fois cette taille.
     */
    private final ObjectProperty<Taille> taille = new SimpleObjectProperty(
            Taille.de("secteur"));

    /**
     * La position réelle du centre du secteur. Par défaut, est à l'origine de
     * l'espace.
     */
    private final ObjectProperty<Vector2D> centre
            = new SimpleObjectProperty<>(Vector2D.ZERO);

    /**
     * L'angle initial du tracé du secteur, exprimé en degrés. Par défaut,
     * correspond à la valeur d'angle de l'horizontale.
     */
    private final DoubleProperty angleInitial = new SimpleDoubleProperty(0);

    /**
     * L'angle du secteur, exprimé en degrés.
     */
    private final DoubleProperty angle = new SimpleDoubleProperty(60);

    public Secteur() {
        setCouleur(Color.GREEN);
    }

    public Secteur(@NotNull final Vector2D centre) {
        this();
        setCentre(centre);
    }

    public Secteur(@NotNull final Vector2D centre, final double angleInitial,
            final double angle) {
        this(centre);
        setAngleInitial(angleInitial);
        setAngle(angle);
    }

    {
        proprietesActualisation.add(taille);
        proprietesActualisation.add(angleInitial);
        proprietesActualisation.add(angle);
        proprietesActualisation.add(centre);
    }

    @Override
    public void dessiner(@NotNull final Toile toile) {
        final int rayon = taille.getValue().get() * MULTIPLICATEUR_RAYON;
        final Vector2D centreVirtuel = toile.positionVirtuelle(getCentre());
        final GraphicsContext contexteGraphique = toile.getGraphicsContext2D();
        contexteGraphique.setFill(getCouleur());
        contexteGraphique.fillArc(
                centreVirtuel.getX() - rayon,
                centreVirtuel.getY() - rayon,
                2 * rayon, 2 * rayon, 
                getAngleInitial(), getAngle(),
                ArcType.ROUND);
    }

    public final Vector2D getCentre() {
        return centre.getValue();
    }

    public final void setCentre(@NotNull final Vector2D centre) {
        this.centre.setValue(centre);
    }

    public final ObjectProperty<Vector2D> centreProperty() {
        return centre;
    }

    public final double getAngleInitial() {
        return angleInitial.getValue();
    }

    public final void setAngleInitial(final double angleInitial) {
        this.angleInitial.setValue(angleInitial % 360);
    }

    public final DoubleProperty angleInitialProperty() {
        return angleInitial;
    }

    public final double getAngle() {
        return angle.getValue();
    }

    public final void setAngle(final double angle) {
        this.angle.setValue(angle % 360);
    }

    public final DoubleProperty angleProperty() {
        return angle;
    }

}
