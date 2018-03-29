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
package org.graphysica.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.io.IOException;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Position;
import org.graphysica.espace2d.Position.Type;

/**
 * Un adaptateur de sérialisation d'objets de type {@code Position.class} en
 * JSON.
 *
 * @author Marc-Antoine Ouimet
 */
public final class PositionJsonAdaptateur extends TypeAdapter<Position> {

    @Override
    public void write(@NotNull final JsonWriter sortie,
            @NotNull final Position position) throws IOException {
        sortie.beginObject();
        sortie.name("type");
        sortie.value(position.getType().toString());
        sortie.name("x");
        sortie.value(position.getValeur().getX());
        sortie.name("y");
        sortie.value(position.getValeur().getY());
        sortie.endObject();
    }

    @Override
    public Position read(@NotNull final JsonReader entree) throws IOException {
        final PositionAdaptee adaptation = new PositionAdaptee();
        if (entree.peek().equals(JsonToken.BEGIN_OBJECT)) {
            entree.beginObject();
            while (!entree.peek().equals(JsonToken.END_OBJECT)) {
                if (entree.peek().equals(JsonToken.NAME)) {
                    switch (entree.nextName()) {
                        case "x":
                            adaptation.setX(entree.nextDouble());
                            break;
                        case "y":
                            adaptation.setY(entree.nextDouble());
                            break;
                        case "type":
                            adaptation.setType(recupererType(
                                    entree.nextString()));
                            break;
                        default:
                            entree.skipValue();
                    }
                }
            }
            entree.endObject();
        }
        return Position.a(new Vector2D(adaptation.getX(), adaptation.getY()),
                adaptation.getType());
    }

    /**
     * Récupère le type de position à partir de son nom.
     *
     * @param nom le nom du type de position.
     * @return le type de position correspondant au nom spécifié.
     */
    @Nullable
    private static Type recupererType(@NotNull final String nom) {
        for (final Type type : Type.values()) {
            if (type.toString().equals(nom)) {
                return type;
            }
        }
        return null;
    }

    /**
     * Une position mutable.
     */
    private static final class PositionAdaptee {

        /**
         * Le type de la position.
         */
        private Type type;

        /**
         * L'abscisse de la position.
         */
        private double x;

        /**
         * L'ordonnée de la position.
         */
        private double y;

        public Type getType() {
            return type;
        }

        public void setType(@NotNull final Type type) {
            this.type = type;
        }

        public double getX() {
            return x;
        }

        public void setX(final double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(final double y) {
            this.y = y;
        }

    }

}
