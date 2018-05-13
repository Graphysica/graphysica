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
import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.graphysica.Application;

/**
 * Une alerte d'exception permet à l'utilisateur d'être notifié en cas
 * d'exceptions.
 *
 * @author Marc-Antoine Ouimet
 */
public final class AlerteException extends Alert {

    /**
     * L'URL vers la page de soumission d'erreurs.
     */
    private static final String URL_PROBLEMES
            = "https://github.com/Graphysica/graphysica/issues";

    /**
     * Le panneau interne des informations de la fenêtre modale.
     */
    private final VBox panneauInterne = new VBox();

    /**
     * Construit une alerte d'exceptions sur un ensemble défini d'exceptions.
     *
     * @param exceptions les exceptions à afficher.
     */
    public AlerteException(@NotNull final Throwable... exceptions) {
        super(AlertType.ERROR);
        for (final Throwable exception : exceptions) {
            final StringWriter erreur = new StringWriter();
            exception.printStackTrace(new PrintWriter(erreur));
            panneauInterne.getChildren().add(new TextArea(erreur.toString()));
        }
    }

    {
        getDialogPane().setExpandableContent(panneauInterne);
        final Hyperlink lienProblemes = new Hyperlink("Signaler les erreurs");
        lienProblemes.setOnAction(new OuvrirLien(URL_PROBLEMES));
        panneauInterne.getChildren().addAll(new Label("Nous sommes désolés que "
                + "le programme n'ait pas pu exécuter votre tâche "
                + "correctement."),
                new Label("Vous pouvez signaler les erreurs qui sont survenues "
                        + "à partir du lien suivant."),
                lienProblemes);
    }

    /**
     * Un événement d'ouverture de lien à partir du fureteur de l'utilisateur.
     */
    private static final class OuvrirLien implements EventHandler<ActionEvent> {

        /**
         * Le lien à ouvrir.
         */
        private final String url;

        /**
         * Construit un événement d'ouverture de lien vers une page Web.
         *
         * @param url le lien vers la page Web.
         */
        public OuvrirLien(@NotNull final String url) {
            this.url = url;
        }

        @Override
        public void handle(@NotNull final ActionEvent evenement) {
            Application.getInstance().getHostServices().showDocument(url);
        }

    }

}
