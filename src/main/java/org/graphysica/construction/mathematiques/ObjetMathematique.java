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
     * La couleur de cet objet mathématique.
     */
    private final ObjectProperty<Color> couleur = new SimpleObjectProperty<>(
            Color.BLACK);

    public final ObjectProperty<Color> couleurProperty() {
        return couleur;
    }

}
