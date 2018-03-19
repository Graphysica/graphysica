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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Une ligne a une épaisseur et une couleur par défaut pour relier deux points
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
    protected final IntegerProperty epaisseur = Taille.de("ligne");

    /**
     * La position d'origine de la trace de la ligne dans le contexte graphique.
     */
    protected Vector2D origineTrace;

    /**
     * La position de l'arrivée de la trace de la ligne dans le contexte
     * graphique.
     */
    protected Vector2D arriveeTrace;

    public Ligne() {
        setCouleur(COULEUR_PAR_DEFAUT);
    }

    {
        proprietesActualisation.add(epaisseur);
    }

    /**
     * Dessine la ligne en tant que tracé continu.
     *
     * @param contexteGraphique le contexte de dessin de la ligne.
     */
    protected void dessinerContinue(
            @NotNull final GraphicsContext contexteGraphique) {
        contexteGraphique.setStroke(getCouleur());
        contexteGraphique.setLineWidth(getEpaisseur());
        contexteGraphique.strokeLine(origineTrace.getX(),
                origineTrace.getY(), arriveeTrace.getX(),
                arriveeTrace.getY());
    }

    /**
     * Récupère un vecteur dont l'orientation est la même que celle de la ligne.
     *
     * @return le vecteur directeur de la ligne.
     */
    public abstract Vector2D getVecteurDirecteur();

    public final int getEpaisseur() {
        return epaisseur.getValue();
    }

    public final void setEpaisseur(final int epaisseur) {
        this.epaisseur.setValue(epaisseur);
    }

}
