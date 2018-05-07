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
import com.sun.istack.internal.Nullable;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.graphysica.espace2d.Espace;
import org.graphysica.espace2d.forme.Forme;

/**
 * Un gestionnaire d'espaces permet de dupliquer des espaces, de les supprimer
 * et de déterminer l'espace actuellement modifié par l'utilisateur.
 *
 * @author Marc-Antoine Ouimet
 */
public class GestionnaireEspaces {

    /**
     * Les espaces de ce gestionnaire d'espaces.
     */
    private final ObservableList<Espace> espaces;

    /**
     * Les éléments de ce gestionnaire d'espaces.
     */
    private final ObservableList<Element> elements;

    /**
     * L'association des gestionnaires d'entrée du curseur aux espaces.
     */
    private final Map<Espace, GestionEntree> gestionsEntree = new HashMap<>();

    /**
     * L'espace actif de ce gestionnaire d'espaces.
     */
    @NotNull
    private Espace espaceActif;

    /**
     * Construit un gestionnaire d'espaces sur un ensemble d'espaces et un
     * ensemble d'éléments définis.
     *
     * @param espaces les espaces gérés.
     * @param elements les éléments gérés.
     */
    GestionnaireEspaces(@NotNull final ObservableList<Espace> espaces,
            @NotNull final ObservableList<Element> elements) {
        this.espaces = espaces;
        if (!espaces.isEmpty()) {
            espaceActif = espaces.get(0);
        }
        for (final Espace espace : espaces) {
            ajouterEspace(espace);
        }
        this.elements = elements;
        this.espaces.addListener(changementEspaces);
        this.elements.addListener(changementElements);
    }

    /**
     * L'événement d'actualisation de la liste des espaces.
     */
    private final ListChangeListener<Espace> changementEspaces = (@NotNull
            final ListChangeListener.Change<? extends Espace> changements) -> {
        while (changements.next()) {
            changements.getAddedSubList().stream().forEach((espace) -> {
                ajouterEspace(espace);
            });
            changements.getRemoved().stream().forEach((espace) -> {
                retirerEspace(espace);
            });
        }
    };

    /**
     * Ajoute un espace aux espaces de la construction.
     *
     * @param espace l'espace à ajouter.
     */
    private void ajouterEspace(@NotNull final Espace espace) {
        final GestionEntree gestionEntree = new GestionEntree(espace);
        espace.addEventFilter(MouseEvent.MOUSE_ENTERED, gestionEntree);
        gestionsEntree.put(espace, new GestionEntree(espace));
        for (final Element element : elements) {
            espace.getFormes().addAll(element.creerFormes());
        }
    }

    /**
     * Retirer un espace des espaces de la construction.
     *
     * @param espace l'eapce à retirer.
     */
    private void retirerEspace(@NotNull final Espace espace) {
        final GestionEntree gestionEntree = gestionsEntree.remove(espace);
        espace.removeEventFilter(MouseEvent.MOUSE_ENTERED, gestionEntree);
        for (final Element element : elements) {
            espace.getFormes().removeAll(element.getFormes());
        }
    }

    /**
     * Duplique l'espace actif.
     */
    public void dupliquerEspace() {
        espaces.add(new Espace(espaceActif()));
    }

    /**
     * Supprime un espace défini.
     *
     * @param espace l'espace à supprimer.
     */
    public void supprimerEspace(@NotNull final Espace espace) {
        espaces.remove(espace);
    }

    /**
     * L'événement d'actualisation de la liste des éléments.
     */
    private final ListChangeListener<Element> changementElements = (@NotNull
            final ListChangeListener.Change<? extends Element> changements) -> {
        while (changements.next()) {
            changements.getAddedSubList().stream().forEach((element) -> {
                ajouterElement(element);
            });
            changements.getRemoved().stream().forEach((element) -> {
                retirerElement(element);
            });
        }
    };

    /**
     * Ajoute un élément aux espaces en y ajoutant ses formes. Chaque espace a
     * sa propre version des formes de l'élément.
     *
     * @param element l'élément à ajouter.
     */
    private void ajouterElement(@NotNull final Element element) {
        for (final Espace espace : espaces) {
            espace.getFormes().addAll(element.creerFormes());
        }
    }

    /**
     * Retire un élément des espaces en retirant ses formes.
     *
     * @param element l'élément à retirer.
     */
    private void retirerElement(@NotNull final Element element) {
        for (final Espace espace : espaces) {
            espace.getFormes().removeAll(element.getFormes());
        }
    }

    /**
     * Récupère l'espace modifié actuellement par l'utilisateur.
     *
     * @return l'espace actif d'édition de la construction.
     */
    @NotNull
    public Espace espaceActif() {
        if (espaceActif == null && !espaces.isEmpty()) {
            espaceActif = espaces.get(0);
        }
        return espaceActif;
    }

    /**
     * Récupère l'élément correspondant à une forme définie parmi les espaces.
     *
     * @param forme la forme dont on cherche l'élément.
     * @return l'élément correspondant à la forme parmi les espaces.
     */
    @Nullable
    public Element elementCorrespondant(@NotNull final Forme forme) {
        for (final Element element : elements) {
            if (element.getFormes().contains(forme)) {
                return element;
            }
        }
        return null;
    }

    /**
     * Une gestion sur un espace.
     */
    private abstract class Gestion implements EventHandler<MouseEvent> {

        /**
         * L'espace de cette gestion.
         */
        private final Espace espace;

        /**
         * Construit une gestion sur un espace défini.
         *
         * @param espace l'espace à gérer.
         */
        public Gestion(@NotNull final Espace espace) {
            this.espace = espace;
        }

        public Espace getEspace() {
            return espace;
        }

    }

    /**
     * Une gestion d'entrée de curseur sur un espace actualise l'espace actif.
     */
    private class GestionEntree extends Gestion {

        /**
         * Construit une gestion d'entrée sur un espace défini.
         *
         * @param espace l'espace à gérer.
         */
        public GestionEntree(@NotNull final Espace espace) {
            super(espace);
        }

        @Override
        public void handle(@NotNull final MouseEvent evenement) {
            espaceActif = getEspace();
        }

    }

}
