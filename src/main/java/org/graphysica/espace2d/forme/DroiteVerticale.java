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
import javafx.beans.property.SimpleDoubleProperty;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Toile;

/**
 * Une droite verticale est perpendiculaire à toutes les droites horizontales du
 * plan.
 *
 * @author Marc-Antoine Ouimet
 */
public class DroiteVerticale extends Ligne {

    /**
     * L'abscisse réelle de la ligne verticale.
     */
    private final DoubleProperty abscisse = new SimpleDoubleProperty();

    public DroiteVerticale(final double abscisse) {
        super();
        setAbscisse(abscisse);
    }
    
    {
        proprietesActualisation.add(this.abscisse);
    }

    @Override
    public void dessiner(@NotNull final Toile toile) {
        if (isVisible(toile)) {
            final double abscisseVirtuelle = toile.abscisseVirtuelle(
                    getAbscisse());
            origineTrace = new Vector2D(abscisseVirtuelle, 0);
            arriveeTrace = new Vector2D(abscisseVirtuelle, toile.getHeight());
            dessinerContinue(toile.getGraphicsContext2D());
        }
    }

    public final double getAbscisse() {
        return abscisse.getValue();
    }

    public final void setAbscisse(final double abscisse) {
        this.abscisse.setValue(abscisse);
    }

    public final DoubleProperty abscisseProperty() {
        return abscisse;
    }
    
    public boolean isVisible(@NotNull final Toile toile) {
        final double abscisseVirtuelle = toile.abscisseVirtuelle(getAbscisse());
        return abscisseVirtuelle >= 0 && abscisseVirtuelle <= toile.getWidth();
    }

}
