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
package org.graphysica.construction.commande;

import com.sun.istack.internal.NotNull;
import org.graphysica.construction.Construction;
import org.graphysica.construction.Element;

/**
 * Une commande de création d'élément permet de créer un élément sur une
 * construction.
 *
 * @author Marc-Antoine
 */
public abstract class CreerElement extends CommandeAnnulable {

    /**
     * La construction sur laquelle on crée un élément.
     */
    protected final Construction construction;

    /**
     * L'élément créé.
     */
    protected Element element;

    /**
     * Construit une commande de création d'un élément sur une construction
     * définie.
     *
     * @param construction
     */
    public CreerElement(@NotNull final Construction construction) {
        this.construction = construction;
    }

    /**
     * Ajoute l'élément créé à la construction.
     */
    @Override
    public void executer() {
        construction.ajouterElement(element);
    }
    
    /**
     * Retire l'élément créé de la construction.
     */
    @Override
    public void annuler() {
        construction.retirerElement(element);
    }

    /**
     * Recrée l'élément.
     */
    @Override
    public void refaire() {
        executer();
    }

}
