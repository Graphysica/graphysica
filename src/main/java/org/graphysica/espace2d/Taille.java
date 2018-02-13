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
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Marc-Antoine Ouimet
 */
public class Taille {

    private static final Logger LOGGER = LoggerFactory.getLogger(Taille.class);

    private static String CHEMIN_PROPRIETES = "/config/taille.properties";

    /**
     * La taille minimale d'un point. Doit être positive.
     *
     * @see Point#taille
     * @see Point#setTaille(int)
     */
    static final int TAILLE_MINIMALE = 1;

    /**
     * La taille maximale d'un point. Doit être positive et supérieure à
     * {@code TAILLE_MINIMALE}.
     *
     * @see Point#taille
     * @see Point#setTaille(int)
     */
    static final int TAILLE_MAXIMALE = 10;

    /**
     * La taille sur l'écran, exprimée en pixels et variant entre entre
     * {@code TAILLE_MINIMALE} et {@code TAILLE_MAXIMALE} inclusivement.
     */
    private final IntegerProperty taille = new SimpleIntegerProperty(
            TAILLE_MINIMALE);

    public Taille() {
    }

    public Taille(final int taille) throws IllegalArgumentException {
        setTaille(taille);
    }

    private static int chargerProprieteTaille(@NotNull final String propriete) {
        try {
            final Properties proprietes = new Properties();
            final InputStream entree = Point.class
                    .getResourceAsStream(CHEMIN_PROPRIETES);
            if (entree != null) {
                proprietes.load(entree);
            } else {
                LOGGER.error(
                        "Fichier de propriétés de taille introuvable au chemin "
                        + CHEMIN_PROPRIETES);
            }
            return Integer.parseInt(proprietes.getProperty(propriete));
        } catch (final IOException ioex) {
            LOGGER.error("Erreur lors de la lecture du fichier de propriétés "
                    + "au chemin " + CHEMIN_PROPRIETES);
        }
        return 1;
    }

    public static Taille pointParDefaut() {
        return new Taille(chargerProprieteTaille("point"));
    }
    
    public static Taille ligneParDefaut() {
        return new Taille(chargerProprieteTaille("ligne"));
    }

    public int getTaille() {
        return taille.getValue();
    }

    /**
     * Modifie la taille.
     *
     * @param taille la nouvelle taille.
     * @throws IllegalArgumentException si {@code taille} est inférieure à
     * {@code TAILLE_MINIMALE} ou supérieure à {@code TAILLE_MAXIMALE}.
     */
    public final void setTaille(final int taille)
            throws IllegalArgumentException {
        if (taille < TAILLE_MINIMALE ^ taille > TAILLE_MAXIMALE) {
            throw new IllegalArgumentException(
                    "Taille spécifiée non-comprise entre " + TAILLE_MINIMALE
                    + " et " + TAILLE_MAXIMALE + ": " + taille);
        }
        this.taille.setValue(taille);
    }

    public IntegerProperty tailleProperty() {
        return taille;
    }

}
