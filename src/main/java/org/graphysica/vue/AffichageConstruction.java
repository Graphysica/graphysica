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
package org.graphysica.vue;

import com.sun.istack.internal.NotNull;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableSet;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.graphysica.construction.Construction;
import org.graphysica.espace2d.Espace;
import org.graphysica.util.SetChangeListener;
import org.graphysica.vue.barremenu.BarreMenu;
import org.graphysica.vue.barreoutils.BarreOutils;
import org.graphysica.vue.inspecteur.Inspecteur;

/**
 * Un affichage de construction permet d'afficher et d'interagir avec une
 * construction.
 *
 * @author Marc-Antoine Ouimet
 * @see Construction
 */
public class AffichageConstruction extends BorderPane {

    /**
     * La largeur préférée des affichages de construction.
     */
    private static final double LARGEUR_PREFEREE = 1280;

    /**
     * La hauteur préférée des affichages de construction.
     */
    private static final double HAUTEUR_PREFEREE = 720;

    /**
     * La construction affichée.
     */
    private final Construction construction;

    /**
     * L'inspecteur des éléments de la construction.
     */
    private final Inspecteur inspecteur;

    /**
     * Le séparateur principal de l'affichage de construction. Sépare
     * l'inspecteur des espaces.
     */
    private final SplitPane separateurPrincipal = new SplitPane();

    /**
     * Le séparateur des espaces de l'affichage de construction.
     */
    private final SplitPane separateurEspaces;

    /**
     * Construit et assemble un affichage de construction sur une construction
     * définie.
     *
     * @param construction la construction affichée.
     */
    public AffichageConstruction(@NotNull final Construction construction) {
        this.construction = construction;
        separateurEspaces = new AffichageEspaces();
        inspecteur = new Inspecteur(construction);
        assembler();
    }

    /**
     * Assemble l'affichage de la construction.
     */
    private void assembler() {
        final MenuBar barreMenu = new BarreMenu(construction, inspecteur);
        final BarreOutils barreOutils = new BarreOutils(
                construction.getGestionnaireOutils());
        setTop(new VBox(barreMenu, barreOutils));
        separateurPrincipal.getItems().addAll(inspecteur, separateurEspaces);
        separateurPrincipal.setDividerPositions(0.2);
        inspecteur.afficheProperty().addListener(new AfficherInspecteur());
        setCenter(separateurPrincipal);
        setPrefSize(LARGEUR_PREFEREE, HAUTEUR_PREFEREE);
    }

    /**
     * Un affichage d'espaces permet d'afficher des espaces liés d'une
     * construction.
     */
    private class AffichageEspaces extends SplitPane {

        /**
         * L'association des espaces à leur panneau parent.
         */
        private final Map<Espace, Pane> panneaux = new HashMap<>();

        /**
         * Initialise un affichage d'espaces sur la construction de l'affichage
         * de construction.
         */
        public AffichageEspaces() {
            final ObservableSet<Espace> espaces = construction.getEspaces();
            espaces.addListener(new EspacesListener(espaces));
        }

        /**
         * L'événement de modification de l'ensemble des espaces de la
         * construction. Permet d'ajouter à l'affichage des espaces un nouveau
         * panneau lié aux dimensions de l'espace dupliqué.
         */
        private class EspacesListener extends SetChangeListener<Espace> {

            /**
             * {@inheritDoc}
             */
            public EspacesListener(
                    @NotNull final ObservableSet<Espace> espaces) {
                super(espaces);
            }

            @Override
            public void onAdd(@NotNull final Espace espace) {
                final Pane panneau = new Pane(espace);
                panneaux.put(espace, panneau);
                espace.widthProperty().bind(panneau.widthProperty());
                espace.heightProperty().bind(panneau.heightProperty());
                getItems().add(panneau);
                espace.widthProperty().addListener(
                        new CentrerEspace(espace));
                espace.heightProperty().addListener(
                        new CentrerEspace(espace));
            }

            @Override
            public void onRemove(@NotNull final Espace espace) {
                getItems().remove(panneaux.remove(espace));
            }

        }

    }

    /**
     * Un événement ponctuel de recentrage d'un espace. Lorsque la propriété est
     * invalidée, recentre l'espace et se soutrait à l'observation de la
     * propriété.
     */
    private static final class CentrerEspace implements InvalidationListener {

        /**
         * L'espace à recentrer.
         */
        private final Espace espace;

        /**
         * Construit un événement de recentrage d'un espace défini.
         *
         * @param espace l'espace à recentrer.
         */
        public CentrerEspace(@NotNull final Espace espace) {
            this.espace = espace;
        }

        @Override
        public void invalidated(@NotNull final Observable observable) {
            espace.centrerOrigine();
            observable.removeListener(this);
        }

    }

    /**
     * L'événement d'affichage de l'inspecteur d'éléments. Ajoute ou retire
     * l'inspecteur le cas échéant de son état d'affichage.
     */
    private final class AfficherInspecteur
            implements InvalidationListener {

        @Override
        public void invalidated(@NotNull final Observable observable) {
            if (inspecteur.isAffiche()) {
                separateurPrincipal.getItems().add(0, inspecteur);
                separateurPrincipal.setDividerPositions(0.2);
            } else {
                separateurPrincipal.getItems().remove(inspecteur);
            }
        }

    }

}
