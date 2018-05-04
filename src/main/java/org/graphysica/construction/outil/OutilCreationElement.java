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
package org.graphysica.construction.outil;

import com.sun.istack.internal.NotNull;
import java.util.HashSet;
import java.util.Set;
import org.graphysica.construction.GestionnaireOutils;
import org.graphysica.espace2d.forme.Forme;

/**
 * Un outil de création d'élément permet de créer des éléments sur une
 * construction à partir du gestionnaire d'outils.
 *
 * @author Marc-Antoine Ouimet
 */
public abstract class OutilCreationElement extends Outil {

    /**
     * Si cet outil de création d'élément a une prochaine étape.
     */
    protected boolean aProchaineEtape = true;

    /**
     * L'ensemble des formes de prévisualisation de l'élément à créer.
     */
    protected Set<Forme> previsualisations = new HashSet<>();

    /**
     * Construit un outil de création d'élément au gestionnaire d'outils défini.
     *
     * @param gestionnaireOutils le gestionnaire d'outils de cet outil de
     * création d'élément.
     */
    public OutilCreationElement(
            @NotNull final GestionnaireOutils gestionnaireOutils) {
        super(gestionnaireOutils);
    }

    public boolean aProchaineEtape() {
        return aProchaineEtape;
    }

}
