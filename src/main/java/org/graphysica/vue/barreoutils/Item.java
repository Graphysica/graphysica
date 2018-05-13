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
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.graphysica.construction.outil.Outil;

/**
 * Un item d'outil permet de représenter un outil dans la barre d'outils.
 *
 * @author Marc-Antoine Ouimet
 */
final class Item extends MenuItem {

    /**
     * La dimension des icônes carrées de cette barre d'outils, exprimée en
     * pixels.
     */
    private static final int DIMENSION = 32;

    /**
     * L'icône de cet item d'outil.
     */
    private final Image image;

    /**
     * L'outil généré par cet item d'outil.
     */
    private final Outil outil;

    /**
     * Construit un item d'outils aux attributs définis.
     *
     * @param propriete la propriété d'icône de cet item d'outil, qui correspond
     * au nom du fichier d'icône de l'outil.
     * @param nom le nom d'affichage de cet item d'outil.
     * @param outil l'outil généré par cet item d'outil.
     */
    public Item(@NotNull final String propriete,
            @NotNull final String nom, @NotNull final Outil outil) {
        super(nom);
        this.outil = outil;
        image = new Image("/images/outils/" + propriete + ".png");
        setGraphic(affichageImage());
    }

    /**
     * Construit un affichage d'image sur l'icône de cet item d'outil.
     *
     * @return l'affichage d'icône de l'outil.
     */
    public ImageView affichageImage() {
        final ImageView affichage = new ImageView(image);
        affichage.setFitHeight(DIMENSION);
        affichage.setFitWidth(DIMENSION);
        affichage.setSmooth(true);
        return affichage;
    }

    public Outil getOutil() {
        return outil;
    }

}
