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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

/**
 * Teste la sérialisation et le déplacement de positions.
 *
 * @author Marc-Antoine Ouimet
 */
public class PositionTest {

    /**
     * Le chemin du fichier des positions sérialisées.
     */
    private static final String CHEMIN_FICHIER_POSITIONS
            = "/serialisation/positions.json";

    /**
     * L'ensemble des positions sérialisées au chemin
     * {@code CHEMIN_FICHIER_POSITIONS}.
     */
    private static final Position[] POSITIONS = {
        Position.a(Vector2D.ZERO, Position.Type.REELLE),
        Position.a(Vector2D.ZERO, Position.Type.VIRTUELLE),
        Position.a(new Vector2D(1, 1), Position.Type.REELLE),
        Position.a(new Vector2D(1, 1), Position.Type.VIRTUELLE),
        Position.a(new Vector2D(-1, 1), Position.Type.REELLE),
        Position.a(new Vector2D(-1, 1), Position.Type.VIRTUELLE),
        Position.a(new Vector2D(1, -1), Position.Type.REELLE),
        Position.a(new Vector2D(1, -1), Position.Type.VIRTUELLE),
        Position.a(new Vector2D(-2, -3), Position.Type.REELLE),
        Position.a(new Vector2D(-2, -3), Position.Type.VIRTUELLE),
        Position.a(new Vector2D(2, 3), Position.Type.REELLE),
        Position.a(new Vector2D(2, 3), Position.Type.VIRTUELLE)
    };

    /**
     * Génère le fichier des positions sérialisées.
     *
     * @throws IOException s'il y a une erreur de I/O.
     */
    private static void genererFichierTests() throws IOException {
        final URL chemin = PositionTest.class.getResource(
                CHEMIN_FICHIER_POSITIONS);
        final Gson gson = new GsonBuilder().create();
        try (final FileWriter ecriture = new FileWriter(
                new File(chemin.getPath()))) {
            ecriture.write(gson.toJson(POSITIONS));
        }
    }

    @Test
    public void testSerialisation() throws IOException {
        genererFichierTests();
        final Gson gson = new GsonBuilder().create();
        try (JsonReader lecture = new JsonReader(new InputStreamReader(
                PositionTest.class.getResourceAsStream(
                        CHEMIN_FICHIER_POSITIONS)))) {
            final Position[] positions = gson.fromJson(lecture,
                    Position[].class);
            assertArrayEquals(POSITIONS, positions);
        }
    }

}
