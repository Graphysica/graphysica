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
import javafx.event.ActionEvent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.graphysica.construction.GestionnaireOutils;
import org.graphysica.construction.outil.Outil;
import org.graphysica.construction.outil.OutilCreationDroite;
import org.graphysica.construction.outil.OutilCreationPoint;
import org.graphysica.espace2d.forme.Taille;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Une barre d'outils permet de sélectionner des outils pour interagir avec la
 * construction.
 *
 * @author Marc-Antoine Ouimet
 */
public final class BarreOutils extends ToolBar {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            BarreOutils.class);

    /**
     * Le chemin du fichier des propriétés des items d'outil.
     */
    private static final String CHEMIN_PROPRIETES
            = "/config/outil.properties";

    /**
     * Les propriétés de chemin des icônes des outils.
     */
    private static final Properties PROPRIETES = new Properties();

    /**
     * La dimension des icônes carrées de cette barre d'outils, exprimée en
     * pixels.
     */
    private static final int DIMENSION = 32;

    /**
     * Le gestionnaire d'outils que contrôle cette barre d'outils.
     */
    private final GestionnaireOutils gestionnaireOutils;

    /**
     * Construit une barre d'outils sur un gestionnaire d'outils.
     *
     * @param gestionnaireOutils
     */
    public BarreOutils(@NotNull final GestionnaireOutils gestionnaireOutils) {
        this.gestionnaireOutils = gestionnaireOutils;
        // TODO: Changer pour l'outil de déplacement
        gestionnaireOutils.setOutilActif(new OutilCreationPoint(
                gestionnaireOutils));
        assembler();
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
                    "Fichier de propriétés d'outils introuvable au chemin "
                    + CHEMIN_PROPRIETES, npex);
        } catch (final IOException ioex) {
            LOGGER.error("Erreur lors de la lecture du fichier de "
                    + " propriétés au chemin " + CHEMIN_PROPRIETES, ioex);
        }
    }

    /**
     * Assemble la barre d'outils.
     */
    private void assembler() {
        final MenuButton groupeSelection = new MenuButton();
        final ItemOutil outilDeplacement = new ItemOutil("deplacer", "Déplacer",
                groupeSelection, new OutilCreationPoint(gestionnaireOutils));
        groupeSelection.setGraphic(outilDeplacement.affichageImage());
        groupeSelection.getItems().addAll(outilDeplacement);

        final MenuButton groupePrimitif = new MenuButton();
        final ItemOutil outilPoint = new ItemOutil("point", "Point",
                groupePrimitif, new OutilCreationPoint(gestionnaireOutils));
        groupePrimitif.setGraphic(outilPoint.affichageImage());
        groupePrimitif.getItems().addAll(outilPoint);

        final MenuButton groupeLignes = new MenuButton();
        final ItemOutil outilDroite = new ItemOutil("droite", "Droite",
                groupeLignes, new OutilCreationDroite(gestionnaireOutils));
        groupeLignes.setGraphic(outilDroite.affichageImage());
        groupeLignes.getItems().addAll(outilDroite);

        getItems().addAll(groupeSelection, groupePrimitif, groupeLignes);
    }

    /**
     * Un item d'outil permet de représenter un outil dans la barre d'outils.
     */
    private class ItemOutil extends MenuItem {

        /**
         * L'icône de cet item d'outil.
         */
        private final Image image;

        /**
         * Le groupement d'outils dans lequel se retrouve cet item d'outil.
         */
        private final MenuButton groupe;

        /**
         * L'outil généré par cet item d'outil.
         */
        private final Outil outil;

        /**
         * Construit un item d'outils aux attributs définis.
         *
         * @param propriete la propriété d'icône de cet item d'outil.
         * @param nom le nom d'affichage de cet item d'outil.
         * @param groupe le groupe dans lequel se retrouve cet item d'outil.
         * @param outil l'outil généré par cet item d'outil.
         */
        public ItemOutil(@NotNull final String propriete,
                @NotNull final String nom, @NotNull final MenuButton groupe,
                @NotNull final Outil outil) {
            super(nom);
            this.outil = outil;
            this.groupe = groupe;
            final String nomFichierImage = PROPRIETES.getProperty(propriete);
            image = new Image("/images/icons/" + nomFichierImage + ".png");
            setGraphic(affichageImage());
            setOnAction((ActionEvent evenement) -> {
                gererAction();
            });
        }

        /**
         * Construit un affichage d'image sur l'icône de cet item d'outil.
         *
         * @return l'affichage d'icône de l'outil.
         */
        private ImageView affichageImage() {
            final ImageView affichage = new ImageView(image);
            affichage.setFitHeight(DIMENSION);
            affichage.setFitWidth(DIMENSION);
            affichage.setSmooth(true);
            return affichage;
        }

        /**
         * Gère l'action de cet item d'outil en actualisant l'outil sélectionné
         * dans le gestionnaire d'outils de la construction et en remplaçant
         * l'icône d'affichage du groupement d'outils dans lequel se retrouve
         * cet item d'outil.
         */
        private void gererAction() {
            gestionnaireOutils.setOutilActif(outil.dupliquer());
            groupe.setGraphic(affichageImage());
        }

    }

}
