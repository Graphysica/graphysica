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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Une ligne a une épaisseur et une couleur par défaut.
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
    protected final ObjectProperty<Taille> epaisseur
            = new SimpleObjectProperty<>(Taille.de("ligne"));
    
    /**
     * Dessine la ligne en tant que tracé continu.
     * @param contexteGraphique le contexte de dessin de la ligne.
     */
    protected abstract void dessinerContinue(
            @NotNull final GraphicsContext contexteGraphique);
    
    protected int getEpaisseur() {
        return epaisseur.getValue().getTaille();
    }
    
}
