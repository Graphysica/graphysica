/**
 *
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
package org.graphysica.vue.proprietes;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *

 * @author Victor Babin
 */
public abstract class ProprietesChamp {
    
    /**
     * Étiquette associée au champ de texte.
     */
    private Label etiquetteChamp;
    
    /**
     * Champ de texte pour l'entrée utilisateur.
     */
    private TextField champTexte;
    
}