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
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javafx.beans.property.SimpleIntegerProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Une taille est un nombre muable compris entre une valeur minimale et une
 * valeur maximale, qui est toujours défini, et qui peut être chargé à partir
 * d'un fichier de configuration.
 *
 * @author Marc-Antoine Ouimet
 */
public final class Taille extends SimpleIntegerProperty {

    private static final Logger LOGGER = LoggerFactory.getLogger(Taille.class);

    /**
     * Le chemin du fichier des propriétés de taille.
     */
    private static final String CHEMIN_PROPRIETES = "/config/taille.properties";

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
     * La taille par défaut.
     */
    static final int TAILLE_PAR_DEFAUT = 4;

    /**
     * Les propriétés de taille.
     */
    private static final Properties PROPRIETES = new Properties();

    /**
     * Construit une taille par défaut.
     */
    public Taille() {
        this(TAILLE_PAR_DEFAUT);
    }

    /**
     * Construit une taille de valeur définie.
     *
     * @param taille la valeur de la taille.
     */
    public Taille(final int taille) {
        set(taille);
    }

    /**
     * Construit une copie d'une taille.
     *
     * @param taille la taille à copier.
     */
    public Taille(@NotNull final Taille taille) {
        this(taille.get());
    }

    static {
        try {
            final InputStream entree = Taille.class
                    .getResourceAsStream(CHEMIN_PROPRIETES);
            if (entree == null) {
                throw new NullPointerException();
            } else {
                PROPRIETES.load(entree);
            }
        } catch (final NullPointerException npex) {
            LOGGER.error(
                    "Fichier de propriétés de taille introuvable au chemin "
                    + CHEMIN_PROPRIETES, npex);
        } catch (final IOException ioex) {
            LOGGER.error("Erreur lors de la lecture du fichier de propriétés "
                    + "au chemin " + CHEMIN_PROPRIETES, ioex);
        }
    }

    /**
     * Charge une propriété de taille à partir de la clé de la propriété.
     *
     * @param propriete la clé de la propriété de taille à récupérer.
     * @return la taille de la propriété ou la taille par défaut en cas
     * d'erreur.
     */
    private static int chargerProprieteTaille(@NotNull final String propriete) {
        final String proprieteRecuperee = PROPRIETES.getProperty(propriete);
        try {
            if (proprieteRecuperee == null) {
                throw new NullPointerException();
            } else {
                return Integer.parseInt(proprieteRecuperee);
            }
        } catch (final NullPointerException npex) {
            LOGGER.error("Aucune propriété '" + propriete + "' au chemin "
                    + CHEMIN_PROPRIETES, npex);
        } catch (final NumberFormatException nfex) {
            LOGGER.error("Format inattendu de la propriété '" + propriete
                    + "' au chemin " + CHEMIN_PROPRIETES, nfex);
        }
        return TAILLE_PAR_DEFAUT;
    }

    /**
     * Récupère la taille d'un objet selon sa clé de propriété du fichier de
     * propriétés au {@code CHEMIN_PROPRIETES}.
     *
     * @param type le type d'objet dont on récupère la taille.
     * @return une taille reflétant la taille de l'objet ou une taille par
     * défaut.
     */
    public static Taille de(@NotNull final String type) {
        return new Taille(chargerProprieteTaille(type));
    }

    @Override
    public void setValue(@NotNull final Number valeur) {
        set(valeur.intValue());
    }

    /**
     * Modifie la taille. Si la taille spécifiée est inférieure à
     * {@code TAILLE_MINIMALE} ou supérieure à {@code TAILLE_MAXIMALE}, défini
     * la taille à sa valeur par défaut.
     *
     * @param taille la nouvelle taille.
     */
    @Override
    public final void set(final int taille) {
        if (taille < TAILLE_MINIMALE ^ taille > TAILLE_MAXIMALE) {
            super.set(TAILLE_PAR_DEFAUT);
        } else {
            super.set(taille);
        }
    }

}
