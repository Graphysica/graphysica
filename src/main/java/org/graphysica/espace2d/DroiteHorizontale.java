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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Une droite horizontale est perpendiculaire à toutes les droites verticales du
 * plan.
 *
 * @author Marc-Antoine Ouimet
 */
public class DroiteHorizontale extends Ligne {

    /**
     * L'ordonnée réelle de la ligne horizontale.
     */
    private final DoubleProperty ordonnee = new SimpleDoubleProperty();

    public DroiteHorizontale(final double ordonnee) {
        super();
        proprietesActualisation.add(this.ordonnee);
        setOrdonnee(ordonnee);
    }

    @Override
    public void dessiner(@NotNull final Toile toile) {
        if (isVisible(toile)) {
            final double ordonneeVirtuelle = toile.ordonneeVirtuelle(
                    getOrdonnee());
            origineTrace = new Vector2D(0, ordonneeVirtuelle);
            arriveeTrace = new Vector2D(toile.getWidth(), ordonneeVirtuelle);
            dessinerContinue(toile.getGraphicsContext2D());
        }
    }

    public final double getOrdonnee() {
        return ordonnee.getValue();
    }

    public final void setOrdonnee(final double abscisse) {
        this.ordonnee.setValue(abscisse);
    }

    public final DoubleProperty ordonneeProperty() {
        return ordonnee;
    }

    public final boolean isVisible(@NotNull final Toile toile) {
        final double ordonneeVirtuelle = toile.ordonneeVirtuelle(getOrdonnee());
        return ordonneeVirtuelle >= 0 && ordonneeVirtuelle <= toile.getHeight();
    }

}
