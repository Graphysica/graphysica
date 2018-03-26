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

/**
 * Une commande annulable est une action exécutable sur la construction qui peut
 * être annulée par la suite.
 *
 * @author Marc-Antoine
 */
public abstract class CommandeAnnulable extends Commande implements Annulable {

    @Override
    public boolean estAnnulable() {
        return true;
    }
    
}
