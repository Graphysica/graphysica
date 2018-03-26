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
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Repere;

/**
 * Une droite horizontale est perpendiculaire à toutes les droites verticales du
 * plan.
 *
 * @author Marc-Antoine Ouimet
 */
public class DroiteHorizontale extends Ligne implements 
        Comparable<DroiteHorizontale> {
    
    /**
     * Le vecteur directeur de toutes droites horizontales.
     */
    private static final Vector2D VECTEUR_DIRECTEUR = new Vector2D(1, 0);

    /**
     * L'ordonnée réelle de la ligne horizontale.
     */
    private final DoubleProperty ordonnee = new SimpleDoubleProperty();

    public DroiteHorizontale(final double ordonnee) {
        setOrdonnee(ordonnee);
    }

    public DroiteHorizontale(final double ordonnee, 
            @NotNull final Color couleur) {
        this(ordonnee);
        setCouleur(couleur);
    }
    
    public DroiteHorizontale(final double ordonne, final int epaisseur) {
        this(ordonne);
        setEpaisseur(epaisseur);
    }
    
    public DroiteHorizontale(final double ordonnee, final int epaisseur, 
            @NotNull final Color couleur) {
        this(ordonnee, epaisseur);
        setCouleur(couleur);
    }
    
    {
        proprietesActualisation.add(this.ordonnee);
    }

    @Override
    public void dessiner(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        if (isVisible(toile, repere)) {
            final double ordonneeVirtuelle = repere.ordonneeVirtuelle(
                    getOrdonnee());
            origineTrace = new Vector2D(0, ordonneeVirtuelle);
            arriveeTrace = new Vector2D(toile.getWidth(), ordonneeVirtuelle);
            dessinerContinue(toile.getGraphicsContext2D());
        }
    }

    @Override
    public int compareTo(@NotNull final DroiteHorizontale droite) {
        return Double.compare(getOrdonnee(), droite.getOrdonnee());
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

    public final boolean isVisible(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        final double ordonneeVirtuelle = repere.ordonneeVirtuelle(
                getOrdonnee());
        return ordonneeVirtuelle >= 0 && ordonneeVirtuelle <= toile.getHeight();
    }

    @Override
    public Vector2D getVecteurDirecteur() {
        return VECTEUR_DIRECTEUR;
    }

}
