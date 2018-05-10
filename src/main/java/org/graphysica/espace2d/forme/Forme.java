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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.graphysica.espace2d.position.Position;
import org.graphysica.espace2d.Repere;

/**
 * Une forme peut être dessinée à l'écran dans un espace avec une couleur
 * d'affichage.
 *
 * @author Marc-Antoine Ouimet
 */
public abstract class Forme implements Dessinable, Survolable, Selectionnable,
        Previsualisable, Affichable {

    /**
     * L'ensemble des propriétés de la forme qui provoquent une actualisation
     * lorsqu'elles sont invalidées. Si l'une de ces propriétés est modifiée,
     * alors la forme doit être redessinée pour l'ensemble des espaces qui
     * l'affichent.
     */
    protected final Set<Property> proprietes = new HashSet<>();

    /**
     * La couleur d'affichage de la forme.
     */
    private final ObjectProperty<Color> couleur
            = new SimpleObjectProperty<>(Color.BLACK);

    /**
     * Le seuil de distance de sélection entre la position virtuelle du curseur
     * et cette forme, exprimé en pixels.
     */
    private static final double DISTANCE_SELECTION = 5;

    /**
     * Si la forme est affichée.
     */
    private final BooleanProperty affiche = new SimpleBooleanProperty(true);

    /**
     * Si la forme est en surbrillance.
     */
    private final BooleanProperty enSurbrillance
            = new SimpleBooleanProperty(false);

    /**
     * Si la forme est en prévisualisation.
     */
    private boolean enPrevisualisation = false;

    public Forme() {
    }

    public Forme(@NotNull final ObjectProperty<Color> couleur) {
        couleurProperty().bind(couleur);
    }

    {
        proprietes.add(couleur);
        proprietes.add(affiche);
        proprietes.add(enSurbrillance);
    }

    @Override
    public void dessiner(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        if (isEnSurvol()) {
            dessinerSurvol(toile, repere);
        }
        dessinerNormal(toile, repere);
    }

    @Override
    public abstract void dessinerNormal(@NotNull final Canvas toile,
            @NotNull final Repere repere);

    @Override
    public abstract void dessinerSurvol(@NotNull final Canvas toile,
            @NotNull final Repere repere);

    @Override
    public boolean isSelectionne(@NotNull final Position curseur,
            @NotNull final Repere repere) {
        return distance(curseur, repere) <= DISTANCE_SELECTION;
    }

    public Set<Property> getProprietes() {
        return proprietes;
    }

    public final Color getCouleur() {
        return couleur.getValue();
    }

    final void setCouleur(final Color couleur) {
        this.couleur.setValue(couleur);
    }

    protected final ObjectProperty<Color> couleurProperty() {
        return couleur;
    }

    @Override
    public final boolean isAffiche() {
        return affiche.getValue();
    }

    @Override
    public final void setAffiche(final boolean affichee) {
        this.affiche.setValue(affichee);
    }
    
    public final BooleanProperty afficheProperty() {
        return affiche;
    }

    @Override
    public final boolean isEnSurvol() {
        return enSurbrillance.getValue();
    }

    @Override
    public final void setEnSurvol(final boolean enSurbrillance) {
        this.enSurbrillance.setValue(enSurbrillance);
    }

    @Override
    public final boolean isEnPrevisualisation() {
        return enPrevisualisation;
    }

    @Override
    public final void setEnPrevisualisation(final boolean enPrevisualisation) {
        this.enPrevisualisation = enPrevisualisation;
    }

}
