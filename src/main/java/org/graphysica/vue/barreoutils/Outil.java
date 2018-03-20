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
package org.graphysica.vue.barreoutils;

import com.sun.istack.internal.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.graphysica.espace2d.Point;
import org.graphysica.espace2d.Taille;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Victor Babin
 */
public class Outil {

    private static final Logger LOGGER = LoggerFactory.getLogger(Taille.class);

    public enum Buttons {
        POINT, SELECTION, SEGMENT, DROITE,
        PERPENDICULAIRE, PARALLELE, MEDIATRICE,
        POLYGONE, ANGLE, ANGLE_MESURE_DONNEE,
        ETIQUETTE, AGRANDISSEMENT, REDUCTION,
        DEPLACEMENT, EFFACER, CHAMP_VECTORIEL,
        CORPS, INSPECTEUR, SOL;
    }

    /**
     * Si le bouton est actif.
     */
    private boolean actif = false;

    /**
     * Le chemin du fichier des propriétés d'outil.
     */
    private static String CHEMIN_PROPRIETES = "/config/outil.properties";

    /**
     * Chargement de propriétés (images) des outils.
     * @param propriete la propriété à charger
     * @return la valeur de la propriété
     */
    private static String chargerProprieteOutil(@NotNull final String propriete) {
        try {
            final Properties proprietes = new Properties();
            final InputStream entree = Point.class
                    .getResourceAsStream(CHEMIN_PROPRIETES);
            if (entree != null) {
                proprietes.load(entree);
                final String valeur = proprietes.getProperty(propriete);
                return valeur;
            } else {
                LOGGER.error(
                        "Fichier de propriétés de taille introuvable au chemin "
                        + CHEMIN_PROPRIETES);
            }
        } catch (final IOException ioex) {
            LOGGER.error("Erreur lors de la lecture du fichier de propriétés "
                    + "au chemin " + CHEMIN_PROPRIETES);
        } catch (final NumberFormatException nfex) {
            LOGGER.error("Format inattendu de la propriété '" + propriete
                    + "' au chemin " + CHEMIN_PROPRIETES);
        }
        return "RIEN";
    }

    
    private static void chargerImages() {
        
    }
}
