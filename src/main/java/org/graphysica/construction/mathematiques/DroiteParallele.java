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
package org.graphysica.construction.mathematiques;

import com.sun.istack.internal.NotNull;
import java.util.HashSet;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.forme.Forme;
import org.graphysica.espace2d.position.PositionReelle;

/**
 * Une droite est un espace linéaire qui bissecte un espace 2D. Une droite
 * parallèle est définie à partir d'une ligne avec laquelle elle doit être
 * parallèle, ainsi qu'un point qui lui est propre.
 * <p>
 * Ici, les positions externes font référence aux positions de définition de la
 * ligne avec laquelle cette droite doit être parallèle, et les positions
 * internes font référence aux positions qui figurent dans la droite.
 *
 * @author Marc-Antoine Ouimet
 */
public class DroiteParallele extends Ligne {

    /**
     * La première position externe de la droite parallèle. La droite passant
     * par {@code positionExterne1} et {@code positionExterne2} est parallèle à
     * cette droite.
     */
    protected final ObjectProperty<PositionReelle> positionExterne1
            = new SimpleObjectProperty<>(new PositionReelle(Vector2D.ZERO));

    /**
     * La deuxième position externe de la droite parallèle. La droite passant
     * par {@code positionExterne1} et {@code positionExterne2} est parallèle à
     * cette droite.
     */
    protected final ObjectProperty<PositionReelle> positionExterne2
            = new SimpleObjectProperty<>(new PositionReelle(Vector2D.ZERO));

    /**
     * L'événement d'actualisation de la deuxième position interne de cette
     * droite parallèle.
     */
    private final InvalidationListener evenementActualisation
            = (@NotNull final Observable observable) -> {
                final Vector2D distance = positionExterne1.getValue().distance(
                        positionInterne1.getValue());
                positionInterne2.setValue(positionExterne2.getValue().deplacer(
                        distance));
            };

    /**
     * Le point par lequel traverse cette droite parallèle.
     */
    private Point point;

    /**
     * Construit une droite parallèle à une ligne et passant par un point
     * défini.
     *
     * @param ligne la ligne avec laquelle cette droite sera parallèle.
     * @param point un point compris dans la droite parallèle.
     */
    public DroiteParallele(@NotNull final Ligne ligne,
            @NotNull final Point point) {
        dependances.add(ligne);
        dependances.add(point);
        positionExterne1.bind(ligne.positionInterne1Property());
        positionExterne2.bind(ligne.positionInterne2Property());
        setPoint(point);
    }

    /**
     * Construit une droite parallèle à une ligne et passant par un point
     * défini.
     *
     * @param ligne la ligne avec laquelle cette droite sera parallèle.
     * @param position la position comprise dans cette droite parallèle.
     */
    public DroiteParallele(@NotNull final Ligne ligne,
            @NotNull final ObjectProperty<PositionReelle> position) {
        dependances.add(ligne);
        positionExterne1.bind(ligne.positionInterne1Property());
        positionExterne2.bind(ligne.positionInterne2Property());
        positionInterne1.bind(position);
    }

    {
        positionExterne1.addListener(evenementActualisation);
        positionExterne2.addListener(evenementActualisation);
        positionInterne1.addListener(evenementActualisation);
    }

    @Override
    public void deplacer(@NotNull final Vector2D deplacement) {
        point.deplacer(deplacement);
    }

    @Override
    public Set<Forme> creerFormes() {
        final Set<Forme> formes = new HashSet<>();
        final org.graphysica.espace2d.forme.Droite forme
                = new org.graphysica.espace2d.forme.Droite(positionInterne1,
                        positionInterne2, couleurProperty());
        formes.add(forme);
        ajouterForme(forme);
        formes.addAll(super.creerFormes());
        return formes;
    }

    public final void setPoint(@NotNull final Point point) {
        retirer(this.point);
        ajouter(point);
        this.point = point;
        positionInterne1.bind(point.positionInterneProperty());
    }
    
}
