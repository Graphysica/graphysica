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
import java.util.HashSet;
import java.util.Set;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.graphysica.construction.Element;
import org.graphysica.espace2d.Repere;

/**
 * Une forme peut être dessinée à l'écran dans un espace avec une couleur
 * d'affichage.
 *
 * @author Marc-Antoine Ouimet
 */
public abstract class Forme extends Element implements Dessinable {

    /**
     * L'ensemble des propriétés de la forme qui provoquent une actualisation
     * lorsqu'elles sont invalidées. Si l'une de ces propriétés est modifiée,
     * alors la forme doit être redessinée.
     */
    protected final Set<Observable> proprietesActualisation = new HashSet<>();

    /**
     * La couleur par défaut d'une forme.
     */
    private static final Color COULEUR_PAR_DEFAUT = Color.BLACK;

    /**
     * La couleur d'affichage de la forme.
     */
    private final ObjectProperty<Color> couleur
            = new SimpleObjectProperty<>(COULEUR_PAR_DEFAUT);

    /**
     * Si la forme est affichée.
     */
    private final BooleanProperty affichee = new SimpleBooleanProperty(true);

    public Forme() {
    }
    
    public Forme(@NotNull final Color couleur) {
        setCouleur(couleur);
    }

    {
        proprietesActualisation.add(couleur);
    }
    
    @Override
    public abstract void dessiner(@NotNull final Canvas toile, 
            @NotNull final Repere repere);

    public Set<Observable> getProprietesActualisation() {
        return proprietesActualisation;
    }
    
    public final Color getCouleur() {
        return couleur.getValue();
    }

    public final void setCouleur(final Color couleur) {
        this.couleur.setValue(couleur);
    }

    public final ObjectProperty<Color> couleurProperty() {
        return couleur;
    }

    public final boolean isAffichee() {
        return affichee.getValue();
    }

    public final void setAffichee(final boolean affichee) {
        this.affichee.setValue(affichee);
    }

    public final BooleanProperty afficheeProperty() {
        return affichee;
    }

}
