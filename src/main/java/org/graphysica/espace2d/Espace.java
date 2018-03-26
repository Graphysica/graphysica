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

import org.graphysica.espace2d.forme.OrdreRendu;
import org.graphysica.espace2d.forme.Grille;
import org.graphysica.espace2d.forme.Forme;
import com.sun.istack.internal.NotNull;
import java.util.LinkedHashSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.forme.Axe;
import org.slf4j.LoggerFactory;

/**
 * Un espace permet d'afficher un ensemble de formes dans un repère.
 *
 * @author Marc-Antoine Ouimet
 */
public class Espace extends ToileRedimensionnable implements Actualisable {

    /**
     * L'utilitaire d'enregistrement de traces d'exécution.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(
            Espace.class);

    /**
     * L'ensemble ordonné observable des formes dessinées dans l'espace.
     */
    protected final ObservableSet<Forme> formes
            = FXCollections.observableSet(new LinkedHashSet<>());

    /**
     * L'ordre de rendu des formes dams l'espace.
     */
    protected final OrdreRendu ordreRendu = new OrdreRendu();

    /**
     * Le repère de l'espace.
     */
    protected Repere repere = new Repere();

    /**
     * Construit un espace dont les dimensions virtuelles sont définies.
     *
     * @param largeur la largeur de l'espace exprimée en pixels.
     * @param hauteur la hauteur de l'espace exprimée en pixels.
     */
    public Espace(final double largeur, final double hauteur) {
        super(largeur, hauteur);
        setRepere(new Repere(largeur, hauteur));
    }

    {
        formes.addListener(evenementActualisation);
        ajouter(new Grille(
            new Vector2D(25, 25),
            Color.gray(0.9)));
        ajouter(new Grille(
            new Vector2D(100, 100),
            Color.gray(0.5)));
        ajouter(Axe.nouvelAxe(Axe.Sens.HORIZONTAL, 100));
        ajouter(Axe.nouvelAxe(Axe.Sens.VERTICAL, 100));
    }
    
    /**
     * Actualise l'affichage de cette toile en redessinant chacune de ses
     * formes. Si la classe d'une forme ne fait pas partie des définitions de
     * l'ordre de rendu, elle n'est pas dessinée.
     *
     * @see Espace#ordreRendu
     * @see OrdreRendu
     */
    @Override
    public void actualiser() {
        effacerAffichage();
        int formesAffichables = 0;
        for (final Class classe : ordreRendu) {
            for (final Forme forme : formes) {
                if (classe.isInstance(forme)) {
                    formesAffichables++;
                    if (forme.isAffichee()) {
                        forme.dessiner(this, repere);
                    }
                }
            }
        }
        if (formesAffichables != formes.size()) {
            LOGGER.warn(String.format("Des classes de formes ne sont pas "
                    + "comprises dans l'ordre de rendu de la toile. "
                    + "%d formes sur %d sont affichables.", formesAffichables,
                    formes.size()));
        }
    }

    /**
     * Réinitialise l'image rendue par cette toile.
     */
    private void effacerAffichage() {
        getGraphicsContext2D().setFill(Color.WHITE);
        getGraphicsContext2D().fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Ajoute une forme à la toile et lie ses propriétés à l'actualisation de la
     * toile.
     *
     * @param forme la forme à ajouter à la toile.
     */
    public void ajouter(@NotNull final Forme forme) {
        this.formes.add(forme);
        forme.getProprietesActualisation().forEach((propriete) -> {
            propriete.addListener(evenementActualisation);
        });
    }

    /**
     * Ajoute des formes à la toile et lie leurs propriétés à l'actualisation de
     * la toile.
     *
     * @param formes les formes à ajouter à la toile.
     */
    public void ajouter(@NotNull final Forme... formes) {
        for (final Forme forme : formes) {
            ajouter(forme);
        }
    }

    /**
     * Retire une forme de la toile et délie ses propriétés de l'actualisation
     * de la toile.
     *
     * @param forme la forme à retirer de la toile.
     */
    public void retirer(@NotNull final Forme forme) {
        final boolean retiree = formes.remove(forme);
        if (retiree) {
            forme.getProprietesActualisation().forEach((propriete) -> {
                propriete.removeListener(evenementActualisation);
            });
        }
    }

    /**
     * Retire les formes de la toile et délie leurs propriétés de
     * l'actualisation de la toile.
     *
     * @param formes les formes à retirer de la toile.
     */
    public void retirer(@NotNull final Forme... formes) {
        for (final Forme forme : formes) {
            retirer(forme);
        }
    }

    /**
     * Modifie le repère de cet espace. Délie l'événemenet d'actualisation de
     * l'espace du précédent repère et ajoute l'événement d'actualisation au
     * nouveau repère.
     *
     * @param repere le nouveau repère de l'espace.
     */
    public final void setRepere(@NotNull final Repere repere) {
        this.repere.echelleProperty().unbind();
        this.repere.origineVirtuelleProperty().unbind();
        this.repere = repere;
        repere.echelleProperty().addListener(evenementActualisation);
        repere.origineVirtuelleProperty().addListener(evenementActualisation);
    }

}
