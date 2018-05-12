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
import javafx.collections.ObservableSet;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Espace;
import org.graphysica.espace2d.forme.Forme;
import org.graphysica.util.SetChangeListener;

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
    private final ObservableSet<Espace> espaces;

    /**
     * Les éléments de ce gestionnaire d'espaces.
     */
    private final ObservableSet<Element> elements;

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
    GestionnaireEspaces(@NotNull final ObservableSet<Espace> espaces,
            @NotNull final ObservableSet<Element> elements) {
        this.espaces = espaces;
        this.elements = elements;
        this.espaces.addListener(new EspacesListener(this.espaces));
        this.elements.addListener(new ElementsListener(this.elements));
    }

    /**
     * Duplique l'espace actif.
     */
    public void dupliquerEspace() {
        espaces.add(new Espace());
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
     * Récupère l'espace modifié actuellement par l'utilisateur. L'espace actif
     * n'existe que dans un contexte où des espaces sont définis dans l'ensemble
     * des espaces. Par conséquent, l'appel à cette méthode doit être restreinte
     * aux événements sur des espaces définis de l'ensemble des espaces.
     *
     * @return l'espace actif d'édition de la construction.
     */
    public Espace espaceActif() {
        if (espaceActif == null && !espaces.isEmpty()) {
            espaceActif = espaces.iterator().next();
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
     * L'événement d'actualisation de l'ensemble des éléments. Ajoute ou retire
     * les formes des éléments le cas échéant.
     */
    private class ElementsListener extends SetChangeListener<Element> {

        /**
         * {@inheritDoc}
         */
        public ElementsListener(
                @NotNull final ObservableSet<Element> elements) {
            super(elements);
        }

        @Override
        public void onAdd(@NotNull final Element element) {
            espaces.forEach((espace) -> {
                espace.getFormes().addAll(element.creerFormes());
            });
        }

        @Override
        public void onRemove(@NotNull final Element element) {
            espaces.forEach((espace) -> {
                espace.getFormes().removeAll(element.getFormes());
            });
        }

    }

    /**
     * L'événement d'actualisation de l'ensemble des espaces. Ajoute les formes
     * des éléments de la construction aux nouveaux espaces, et les retire et
     * délit leurs propriétés des espaces retirés.
     */
    private class EspacesListener extends SetChangeListener<Espace> {

        /**
         * {@inheritDoc}
         */
        public EspacesListener(@NotNull final ObservableSet<Espace> espaces) {
            super(espaces);
        }

        @Override
        public void onAdd(@NotNull final Espace espace) {
            ajouterEvenements(espace);
        }

        /**
         * Ajoute les événements de navigation et de gestion d'entrée de la
         * souris à l'espace spécifié.
         *
         * @param espace l'espace sur lequel ajouter les événements.
         */
        private void ajouterEvenements(@NotNull final Espace espace) {
            final Navigation gestionNavigation = new Navigation(espace);
            espace.addEventFilter(KeyEvent.KEY_PRESSED, new Navigation(espace));
            gestionsNavigation.put(espace, gestionNavigation);
            final GestionEntree gestionEntree = new GestionEntree(espace);
            espace.addEventFilter(MouseEvent.MOUSE_ENTERED, gestionEntree);
            gestionsEntree.put(espace, new GestionEntree(espace));
        }

        @Override
        public void onRemove(@NotNull final Espace espace) {
            retirerEvenements(espace);
            retirerFormes(espace);
        }

        /**
         * Ajoute les événements de navigation et de gestion d'entrée de la
         * souris de l'espace spécifié.
         *
         * @param espace l'espace duquel retirer les événements.
         */
        private void retirerEvenements(@NotNull final Espace espace) {
            final Navigation gestionNavigation = gestionsNavigation.remove(
                    espace);
            espace.removeEventFilter(KeyEvent.KEY_PRESSED, gestionNavigation);
            final GestionEntree gestionEntree = gestionsEntree.remove(espace);
            espace.removeEventFilter(MouseEvent.MOUSE_ENTERED, gestionEntree);
        }

        /**
         * Retire les formes des éléments de l'espace spécifié et délie leurs
         * propriétés d'actualisation.
         *
         * @param espace l'espace duquel retirer les formes.
         */
        private void retirerFormes(@NotNull final Espace espace) {
            elements.forEach((element) -> {
                espace.getFormes().removeAll(element.getFormes());
                element.getFormes().stream().forEach((forme) -> {
                    forme.getProprietes().stream().forEach((propriete) -> {
                        propriete.unbind();
                    });
                });
            });
        }

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
                        espace.zoomer();
                        break;
                    case SUBTRACT:
                    case UNDERSCORE:
                    case MINUS:
                        espace.dezoomer();
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
