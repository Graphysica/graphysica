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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import org.graphysica.espace2d.forme.Forme;

/**
 * Un module de formes encapsule les transactions sur un ensembles de formes
 * observables dessinées dans plusieurs espaces.
 *
 * @author Marc-Antoine Ouimet
 */
public final class Formes {

    /**
     * L'ensemble des espaces à actualiser lorsqu'une forme est ajoutée ou
     * retirée.
     */
    private final Set<Espace> espaces = new HashSet<>();

    /**
     * L'ensemble des formes dessinées dans les espaces.
     */
    private final ObservableSet<Forme> formes;

    /**
     * Construit un ensemble vide de formes.
     */
    Formes() {
        this(new LinkedHashSet<>());
    }

    /**
     * Consstruit un ensemble de formes à partir d'un ensemble spécifié.
     *
     * @param formes l'ensemble de formes à ajouter.
     */
    Formes(@NotNull final LinkedHashSet<Forme> formes) {
        this.formes = FXCollections.observableSet(formes);
    }

    /**
     * Ajoute une forme à l'ensemble des formes et lie ses propriétés
     * d'actualisation aux événements d'actualisation des espaces.
     *
     * @param forme la forme à ajouter.
     */
    public void ajouter(@NotNull final Forme forme) {
        formes.add(forme);
        espaces.stream().forEach((@NotNull final Espace espace) -> {
            forme.getProprietesActualisation().stream().forEach((propriete) -> {
                        propriete.addListener(espace.evenementActualisation);
            });
        });
    }

    /**
     * Ajoute des formes à l'ensemble des formes et lie leurs propriétés
     * d'actualisation aux événements d'actualisation des espaces.
     *
     * @param formes les formes à ajouter
     */
    public void ajouter(@NotNull final Forme... formes) {
        for (final Forme forme : formes) {
            ajouter(forme);
        }
    }

    /**
     * Retire une forme de l'ensemble des formes et délie ses propriétés des
     * événements d'actualisation des espaces.
     *
     * @param forme la forme à retirer.
     */
    public void retirer(@NotNull final Forme forme) {
        formes.remove(forme);
        espaces.stream().forEach((@NotNull final Espace espace) -> {
            forme.getProprietesActualisation().stream().forEach((propriete) -> {
                        propriete.removeListener(espace.evenementActualisation);
            });
        });
    }

    /**
     * Retire des formes de l'ensemble des formes et délie leurs propriétés des
     * événements d'actualisation des espaces.
     *
     *
     * @param formes les formes à retirer.
     */
    public void retirer(@NotNull final Forme... formes) {
        for (final Forme forme : formes) {
            retirer(forme);
        }
    }

    /**
     * Ajoute un espace à ce module de formes. Lie l'ensemble des formes à
     * l'événement d'actualisation de l'espace spécifié.
     *
     * @param espace l'espace à ajouter.
     */
    public void ajouterEspace(@NotNull final Espace espace) {
        espaces.add(espace);
        formes.addListener(espace.evenementActualisation);
        formes.stream().forEach((@NotNull final Forme forme) -> {
            forme.getProprietesActualisation().stream().forEach((propriete) -> {
                        propriete.addListener(espace.evenementActualisation);
            });
        });
    }

    /**
     * Retire un espace de ce module de formes. Délie l'ensemble des formes de
     * l'événement d'actualisation de l'espace spécifié.
     *
     * @param espace l'espace à retirer.
     */
    public void retirerEspace(@NotNull final Espace espace) {
        espaces.remove(espace);
        formes.removeListener(espace.evenementActualisation);
        formes.stream().forEach((@NotNull final Forme forme) -> {
            forme.getProprietesActualisation().stream().forEach((propriete) -> {
                        propriete.removeListener(espace.evenementActualisation);
            });
        });
    }

    /**
     * Récupère l'ensemble des formes de ce module de formes.
     *
     * @return l'ensemble des formes.
     */
    public ObservableSet<Forme> get() {
        return formes;
    }

}
