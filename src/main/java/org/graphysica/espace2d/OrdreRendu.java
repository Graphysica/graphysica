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
import java.util.LinkedHashSet;
import org.graphysica.espace2d.forme.Aire;
import org.graphysica.espace2d.forme.Angle;
import org.graphysica.espace2d.forme.Etiquette;
import org.graphysica.espace2d.forme.Forme;
import org.graphysica.espace2d.forme.Grille;
import org.graphysica.espace2d.forme.Ligne;
import org.graphysica.espace2d.forme.Point;

/**
 * Un ordre de rendu est un ensemble ordonné des classes de forme à dessiner sur
 * une toile.
 *
 * @author Marc-Antoine Ouimet
 */
public class OrdreRendu extends LinkedHashSet<Class> {

    {
        add(Grille.class);
        add(Aire.class);
        add(Angle.class);
        add(Ligne.class);
        add(Point.class);
        add(Etiquette.class);
    }

    @Override
    public boolean add(@NotNull final Class classe) 
            throws IllegalArgumentException {
        if (classe.isAssignableFrom(Forme.class)) {
            throw new IllegalArgumentException(
                    "Le type de classe spécifié n'est pas une forme.");
        }
        return super.add(classe);
    }
    
}
