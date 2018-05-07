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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
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
     * L'association des gestionnaires de navigation aux espaces.
     */
    private final Map<Espace, Navigation> gestionsNavigation = new HashMap<>();

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
        espaces.forEach((espace) -> {
            ajouterEspace(espace);
        });
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
        final Navigation gestionNavigation = new Navigation(espace);
        espace.addEventFilter(KeyEvent.KEY_PRESSED, new Navigation(espace));
        gestionsNavigation.put(espace, gestionNavigation);
        final GestionEntree gestionEntree = new GestionEntree(espace);
        espace.addEventFilter(MouseEvent.MOUSE_ENTERED, gestionEntree);
        gestionsEntree.put(espace, new GestionEntree(espace));
        elements.forEach((element) -> {
            espace.getFormes().addAll(element.creerFormes());
        });
    }

    /**
     * Retirer un espace des espaces de la construction.
     *
     * @param espace l'eapce à retirer.
     */
    private void retirerEspace(@NotNull final Espace espace) {
        final Navigation gestionNavigation = gestionsNavigation.remove(espace);
        espace.removeEventFilter(KeyEvent.KEY_PRESSED, gestionNavigation);
        final GestionEntree gestionEntree = gestionsEntree.remove(espace);
        espace.removeEventFilter(MouseEvent.MOUSE_ENTERED, gestionEntree);
        elements.forEach((element) -> {
            espace.getFormes().removeAll(element.getFormes());
        });
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
        System.out.println(changements.getList().size());
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
        espaces.forEach((espace) -> {
            espace.getFormes().addAll(element.creerFormes());
        });
    }

    /**
     * Retire un élément des espaces en retirant ses formes.
     *
     * @param element l'élément à retirer.
     */
    private void retirerElement(@NotNull final Element element) {
        espaces.forEach((espace) -> {
            espace.getFormes().removeAll(element.getFormes());
        });
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
     * Une gestion d'entrée de curseur sur un espace actualise l'espace actif.
     */
    private class GestionEntree implements EventHandler<MouseEvent> {
        
        /**
         * L'espace de cette gestion d'entrée d'espace.
         */
        private final Espace espace;

        /**
         * Construit une gestion d'entrée sur un espace défini.
         *
         * @param espace l'espace à gérer.
         */
        public GestionEntree(@NotNull final Espace espace) {
            this.espace = espace;
        }

        @Override
        public void handle(@NotNull final MouseEvent evenement) {
            espaceActif = espace;
        }

    }

    /**
     * Un événement de navigation permet de naviguer un espace défini à l'aide
     * du clavier. Il permet de zoomer l'espace avec "Ctrl+'+'" et de dézoomer
     * avec "Ctrl+'-'", et de défiler selon un pas avec "Ctrl+'UP'",
     * "Ctrl+'DOWN'", "Ctrl+'LEFT'" et "Ctrl+'RIGHT'" respectivement pour
     * défiler vers le haut, le bas, la gauche et la droite.
     */
    private static class Navigation implements EventHandler<KeyEvent> {

        /**
         * Le pas du défilement exprimé en pixels.
         */
        private static final double PAS = 25;

        /**
         * L'espace de cette navigation.
         */
        private final Espace espace;

        /**
         * Construit un événement de navigation sur un espace défini.
         *
         * @param espace l'espace à naviguer.
         */
        public Navigation(@NotNull final Espace espace) {
            this.espace = espace;
        }

        @Override
        public void handle(@NotNull final KeyEvent evenement) {
            if (evenement.isControlDown()) {
                switch (evenement.getCode()) {
                    case ADD:
                    case EQUALS:
                    case PLUS:
                        espace.zoomer(1);
                        break;
                    case SUBTRACT:
                    case UNDERSCORE:
                    case MINUS:
                        espace.zoomer(-1);
                        break;
                    case UP:
                        espace.defiler(new Vector2D(0, PAS));
                        break;
                    case DOWN:
                        espace.defiler(new Vector2D(0, -PAS));
                        break;
                    case LEFT:
                        espace.defiler(new Vector2D(PAS, 0));
                        break;
                    case RIGHT:
                        espace.defiler(new Vector2D(-PAS, 0));
                        break;
                }
            }
        }

    }

}
