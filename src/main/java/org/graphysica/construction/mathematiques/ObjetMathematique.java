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

import java.io.Serializable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import org.graphysica.construction.Element;
import org.graphysica.espace2d.forme.Forme;

/**
 * Un objet mathématique peut être construit dans le cadre d'une construction.
 * Par conséquent, ils doivent être sérialisable afin que l'utilisateur puisse
 * enregistrer leur état pour le recharger par la suite. Ce faisant, l'objet
 * mathématique doit décrire tant ses propriétés concrètes que visuelles.
 *
 * @author Marc-Antoine Ouimet
 */
public abstract class ObjetMathematique extends Element
        implements Serializable {

    /**
     * La forme représentant cet objet mathématique.
     */
    transient Forme forme = null;

    /**
     * La couleur de cet objet mathématique.
     */
    private final ObjectProperty<Color> couleur = new SimpleObjectProperty<>();

    /**
     * Crée une forme représentant cet objet mathématique dans l'espace.
     *
     * @return une forme représentant cet objet mathématique.
     */
    abstract Forme creerForme();

    /**
     * Récupère la forme représentant cet objet mathématique dans l'espace.
     * Initialise cette forme si elle n'a pas préalablement été créée.
     *
     * @return la forme représentant cet objet mathématique.
     */
    public Forme getForme() {
        if (forme == null) {
            forme = creerForme();
        }
        return forme;
    }

    public final ObjectProperty<Color> couleurProperty() {
        return couleur;
    }

}
