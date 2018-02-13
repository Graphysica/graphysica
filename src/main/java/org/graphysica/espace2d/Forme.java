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
import javafx.scene.paint.Color;

/**
 * Une forme peut être dessinée à l'écran dans un espace avec une couleur
 * d'affichage.
 *
 * @author Marc-Antoine Ouimet
 */
public abstract class Forme implements Dessinable {

    private static final Color COULEUR_PAR_DEFAUT = Color.BLACK;

    /**
     * La couleur d'affichage de la forme.
     */
    protected final ObjectProperty<Color> couleur
            = new SimpleObjectProperty<>(COULEUR_PAR_DEFAUT);

    public Forme() {
    }

    public Forme(@NotNull final Color couleur) {
        this.couleur.setValue(couleur);
    }

    @Override
    public abstract void dessiner(@NotNull final Toile toile);

    public final Color getCouleur() {
        return couleur.getValue();
    }

    public final void setCouleur(final Color couleur) {
        this.couleur.setValue(couleur);
    }

    public final ObjectProperty<Color> couleurProperty() {
        return couleur;
    }

}
