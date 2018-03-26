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
package org.graphysica.construction;

import com.sun.istack.internal.NotNull;
import java.util.Stack;
import org.graphysica.construction.commande.Commande;
import org.graphysica.construction.commande.CommandeAnnulable;

/**
 * Un gestionnaire de commandes permet d'annuler et de refaire des commandes en
 * série.
 *
 * @author Marc-Antoine
 */
public class GestionnaireCommandes {

    /**
     * Le stack des commandes exécutées et annulables.
     */
    private final Stack<CommandeAnnulable> commandes = new StackLimite<>();

    /**
     * Le stack des commandes annulées.
     */
    private final Stack<CommandeAnnulable> commandesAnnulees = new Stack<>();

    public void executer(@NotNull final Commande commande) {
        commande.executer();
        commandesAnnulees.clear();
        if (commande instanceof CommandeAnnulable) {
            commandes.push((CommandeAnnulable) commande);
        }
    }

    /**
     * Annule la dernière commande exécutée s'il y a lieu.
     */
    public void annuler() {
        if (!commandes.isEmpty()) {
            final CommandeAnnulable commande = commandes.pop();
            commande.annuler();
            commandesAnnulees.push(commande);
        }
    }

    /**
     * Refait la dernière commande annulée s'il y a lieu.
     */
    public void refaire() {
        if (!commandesAnnulees.isEmpty()) {
            final CommandeAnnulable commande = commandesAnnulees.pop();
            commande.refaire();
            commandes.push(commande);
        }
    }

}
