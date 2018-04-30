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
package org.graphysica.espace2d.position;

import com.sun.istack.internal.NotNull;
import java.io.Serializable;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Repere;
import static org.graphysica.espace2d.position.Type.REELLE;
import static org.graphysica.espace2d.position.Type.VIRTUELLE;

/**
 * Une position est un emplacement immuable dans un espace. Une position permet
 * de convertir un couple de valeurs correspondant à un emplacement dans un
 * repère d'espace.
 * <p>
 * L'utilisation de cette classe permet de lever des ambivalences à l'égard des
 * calculs effectués sur des points, notamment pour leur déplacement et
 * déterminer la position entre deux points. De plus, cette classe minimse les
 * erreurs d'arithmétique à point flottant dues aux multiples conversions entre
 * les positions virtuelles et réelles, en plus d'optimiser l'allocation en
 * mémoire dans la plupart des cas.
 *
 * @author Marc-Antoine Ouimet
 */
public abstract class Position implements Serializable {

    /**
     * Le message lorsqu'un type de position n'est pas supporté pour une
     * opération.
     */
    private static final String EXCEPTION_TYPE_NON_SUPPORTE
            = "Type de position non supporté.";

    /**
     * La valeur de cette position.
     */
    protected final Vector2D position;

    /**
     * Construit une position dont la valeur et le repère sont définis.
     *
     * @param position la valeur de la position.
     */
    Position(@NotNull final Vector2D position) {
        this.position = position;
    }

    /**
     * Récupère la position réelle de cette position.
     *
     * @param repere le repère dans lequel déterminer la position réelle.
     * @return la position réelle de cette position.
     */
    public abstract Vector2D reelle(@NotNull final Repere repere);

    /**
     * Récupère la position virtuelle de cette position.
     *
     * @param repere le repère dans lequel déterminer la position virtuelle.
     * @return la position virtuelle de cette position.
     */
    public abstract Vector2D virtuelle(@NotNull final Repere repere);

    /**
     * Récupère la nouvelle position lorsque cette position subit un déplacement
     * de type défini. Si le déplacement est réel, alors la position déplacée
     * sera réelle. Si le déplacement est virtuel, alors la position déplacée
     * sera virtuelle.
     *
     * @param deplacement la valeur du déplacement.
     * @param type le type de déplacement.
     * @param repere le repère de l'espace dans lequel a lieu le déplacement.
     * @return la position déplacée.
     */
    public Position deplacer(@NotNull final Vector2D deplacement,
            @NotNull final Type type, @NotNull final Repere repere) {
        switch (type) {
            case REELLE:
                return new PositionReelle(reelle(repere).add(deplacement));
            case VIRTUELLE:
                return new PositionVirtuelle(
                        virtuelle(repere).add(deplacement));
        }
        throw new IllegalArgumentException(EXCEPTION_TYPE_NON_SUPPORTE);
    }

    /**
     * Calcule le module de la distance entre cette position et une position
     * spécifiée.
     *
     * @param position la position distancée de ce point.
     * @param repere le repère d'espace d'emplacement des positions.
     * @param type le type de distance à calculer.
     * @return la distance entre les deux positions.
     */
    public double distance(@NotNull final Position position,
            @NotNull final Type type, @NotNull final Repere repere) {
        switch (type) {
            case REELLE:
                return reelle(repere).distance(position.reelle(repere));
            case VIRTUELLE:
                return virtuelle(repere).distance(position.virtuelle(repere));
        }
        throw new IllegalArgumentException(EXCEPTION_TYPE_NON_SUPPORTE);
    }

    /**
     * Calcule la distance vectorielle orientée de type spécifié de cette
     * position vers une position spécifiée.
     *
     * @param position la position distancée de ce point.
     * @param repere le repère d'espace d'emplacement des positions.
     * @param type le type de distance à calculer.
     * @return la distance entre les deux positions.
     */
    public Vector2D distanceVectorielle(@NotNull final Position position,
            @NotNull final Type type, @NotNull final Repere repere) {
        switch (type) {
            case REELLE:
                return position.reelle(repere).subtract(reelle(repere));
            case VIRTUELLE:
                return position.virtuelle(repere).subtract(virtuelle(repere));
        }
        throw new IllegalArgumentException(EXCEPTION_TYPE_NON_SUPPORTE);
    }

    /**
     * Récupère la valeur brute de cette position. L'interprétation de cette
     * valeur dépend du type de la position.
     *
     * @return la valeur brute de cette position.
     */
    public Vector2D getValeur() {
        return position;
    }

    /**
     * Récupère le type de cette position. Dicte la façon dont il faut
     * interpréter la valeur de cette position.
     *
     * @return le type de cette position.
     */
    public abstract Type getType();

    @Override
    public boolean equals(final Object objet) {
        if (objet == null) {
            return false;
        } else if (!(objet.getClass() == getClass())) {
            return false;
        } else if (this == objet) {
            return true;
        } else {
            return getValeur().equals(((Position) objet).getValeur());
        }
    }

    @Override
    public int hashCode() {
        return getType().hashCode() * getValeur().hashCode();
    }

}
