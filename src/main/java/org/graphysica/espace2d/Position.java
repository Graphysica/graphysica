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

import com.google.gson.annotations.JsonAdapter;
import com.sun.istack.internal.NotNull;
import java.util.Objects;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.gson.PositionJsonAdaptateur;

/**
 * Une position est un emplacement immuable dans un espace. Une position permet
 * de convertir un couple de valeurs correspondant à un emplacement dans un
 * repère d'espace
 *
 * @author Marc-Antoine Ouimet
 */
@JsonAdapter(PositionJsonAdaptateur.class)
public abstract class Position {

    /**
     * L'énumération des types de position.
     */
    public static enum Type {
        REELLE, VIRTUELLE
    }

    /**
     * La valeur de cette position.
     */
    protected final Vector2D position;

    /**
     * Construit une position dont la valeur et le repère sont définis.
     *
     * @param position la valeur de la position.
     */
    protected Position(@NotNull final Vector2D position) {
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
     * Déplace cette position selon un déplacement de type défini.
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
                return Position.a(reelle(repere).add(deplacement), Type.REELLE);
            case VIRTUELLE:
                return Position.a(virtuelle(repere).add(deplacement),
                        Type.VIRTUELLE);
            default:
                throw new IllegalArgumentException(
                        "Type de position non supporté.");
        }
    }

    /**
     * Construit une nouvelle position de valeur et de type spécifié dans un
     * repère d'espace.
     *
     * @param position la valeur de la position.
     * @param type le type de la position.
     * @return la position construite.
     */
    public static Position a(@NotNull final Vector2D position,
            @NotNull final Type type) {
        switch (type) {
            case REELLE:
                return new Reelle(position);
            case VIRTUELLE:
                return new Virtuelle(position);
            default:
                throw new IllegalArgumentException(
                        "Type de position non supporté.");
        }
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
        if (this == objet) {
            return true;
        } else if (objet == null) {
            return false;
        } else if (getClass() != objet.getClass()) {
            return false;
        } else {
            final Position compare = (Position) objet;
            return getType().equals(compare.getType())
                    && getValeur().equals(compare.getValeur());
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(position);
        return hash;
    }

    /**
     * Une position réelle est exprimée en unitées réelles.
     */
    @JsonAdapter(PositionJsonAdaptateur.class)
    private static class Reelle extends Position {

        /**
         * Construit une position réelle dont la valeur et le repère sont
         * définis.
         *
         * @param position la valeur de la position.
         */
        public Reelle(@NotNull final Vector2D position) {
            super(position);
        }

        @Override
        public Vector2D reelle(@NotNull final Repere repere) {
            return position;
        }

        @Override
        public Vector2D virtuelle(@NotNull final Repere repere) {
            return repere.positionVirtuelle(position);
        }

        @Override
        public Position.Type getType() {
            return Position.Type.REELLE;
        }

    }

    /**
     * Une position virtuelle est exprimée en unités d'affichage à l'écran.
     */
    @JsonAdapter(PositionJsonAdaptateur.class)
    private static class Virtuelle extends Position {

        /**
         * Construit une position virtuelle dont la valeur et le repère sont
         * définis.
         *
         * @param position la valeur de la position.
         */
        public Virtuelle(@NotNull final Vector2D position) {
            super(position);
        }

        @Override
        public Vector2D reelle(@NotNull final Repere repere) {
            return repere.positionReelle(position);
        }

        @Override
        public Vector2D virtuelle(@NotNull final Repere repere) {
            return position;
        }

        @Override
        public Position.Type getType() {
            return Position.Type.VIRTUELLE;
        }

    }

}
