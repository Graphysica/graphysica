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
import java.util.HashSet;
import java.util.Set;
import org.graphysica.construction.commande.Commande;
import org.graphysica.espace2d.Espace;
import org.graphysica.espace2d.forme.Forme;

/**
 * Une construction permet d'élaborer une scène de simulation de physique.
 *
 * @author Marc-Antoine
 */
public class Construction {
    
    /**
     * L'espace graphique de la construction.
     */
    private final Espace espace = new Espace();
    
    /**
     * Le gestionnaire des commandes de la construction.
     */
    private final GestionnaireCommandes gestionnaireCommandes
            = new GestionnaireCommandes();

    /**
     * L'ensemble des éléments de la construction. Comprend les corps physiques
     * et leurs formes d'affichage.
     */
    private final Set<Element> elements = new HashSet<>();

    /**
     * Exécute une commande.
     *
     * @param commande la commande à exécuter.
     */
    public void executerCommande(@NotNull final Commande commande) {
        gestionnaireCommandes.executer(commande);
    }

    /**
     * Ajoute un élément à la construction.
     *
     * @param element l'élément à ajouter.
     * @return {@code true} si l'ensembles des éléments ne contenait pas déjà
     * l'élément spécifié.
     */
    public boolean ajouterElement(@NotNull final Element element) {
        if (element instanceof Forme) {
            espace.ajouter((Forme) element);
        }
        return elements.add(element);
    }

    /**
     * Retire un élément de la construction.
     *
     * @param element l'élément à retirer.
     * @return {@code true} si l'ensemble des éléments contenait l'élément
     * spécifié.
     */
    public boolean retirerElement(@NotNull final Element element) {
        if (element instanceof Forme) {
            espace.retirer((Forme) element);
        }
        return elements.remove(element);
    }
    
    public void annuler() {
        gestionnaireCommandes.annuler();
    }
    
    public void refaire() {
        gestionnaireCommandes.refaire();
    }

    public Espace getEspace() {
        return espace;
    }
    
}
