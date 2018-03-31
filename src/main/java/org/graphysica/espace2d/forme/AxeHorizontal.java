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
package org.graphysica.espace2d.forme;

import com.sun.istack.internal.NotNull;
import java.util.Iterator;
import java.util.Map;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.position.Position;
import org.graphysica.espace2d.Repere;
import org.graphysica.espace2d.position.PositionReelle;

/**
 * Un axe horizontal permet de représenter les valeurs d'abscisse de l'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public class AxeHorizontal extends Axe {

    /**
     * Construit un axe horizontal dont l'espacement minimal virtuel est défini.
     *
     * @param espacement la valeur virtuelle d'espacement minimal entre les
     * graduations de l'axe.
     */
    protected AxeHorizontal(final double espacement) {
        setEspacement(espacement);
    }

    @Override
    public void dessiner(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        if (isEnSurbrillance()) {
            dessinerSurbrillance(toile, repere);
        }
        final double[] graduationsVerticales = repere
                .graduationsVerticales(toile.getWidth(), getEspacement());
        final double[] abscissesReelles = repere.abscissesReelles(
                graduationsVerticales);
        actualiserEtiquettes(abscissesReelles, formatValeurs(repere));
        final double positionReelleAxe = positionReelleAxe(toile, repere);
        fleche.setOrigine(new PositionReelle(
                new Vector2D(repere.abscisseReelle(0), positionReelleAxe)));
        fleche.setArrivee(new PositionReelle(
                new Vector2D(repere.abscisseReelle(toile.getWidth()), 
                        positionReelleAxe)));
        dessinerGraduations(toile, graduationsVerticales,
                positionVirtuelleAxe(toile, repere));
        fleche.dessiner(toile, repere);
        actualiserPositionEtiquettes(toile, repere);
        etiquettes.values().forEach((etiquette) -> {
            etiquette.dessiner(toile, repere);
        });
    }

    /**
     * Dessine des marques de graduations sur l'axe.
     *
     * @param toile la toile affichant cet axe.
     * @param valeursVirtuelles les valeurs virtuelles de graduation.
     * @param positionAxe la position virtuelle de l'axe.
     */
    private void dessinerGraduations(@NotNull final Canvas toile,
            @NotNull final double[] valeursVirtuelles,
            final double positionAxe) {
        final GraphicsContext contexteGraphique = toile.getGraphicsContext2D();
        contexteGraphique.setStroke(Color.BLACK);
        contexteGraphique.setLineWidth(1);
        for (final double abscisseVirtuelle : valeursVirtuelles) {
            contexteGraphique.strokeLine(
                    abscisseVirtuelle, positionAxe + getTailleGraduation(),
                    abscisseVirtuelle, positionAxe - getTailleGraduation());
        }
    }

    /**
     * Calcule l'espacement minimal réel entre les graduations de l'axe.
     *
     * @param repere le repère de l'espace à graduer.
     * @return l'espacement minimal réel des graduations.
     */
    @Override
    protected double espacementMinimalReel(@NotNull final Repere repere) {
        return getEspacement() / repere.getEchelle().getX();
    }

    /**
     * Actualise la position des étiquettes de cet axe.
     *
     * @param toile la toile affichant cet axe.
     * @param repere le repère de l'espace à graduer.
     */
    private void actualiserPositionEtiquettes(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        final double ordonneeReelleAxe = positionReelleAxe(toile, repere);
        positionVirtuelle = positionVirtuelleAxe(toile, repere);
        final Iterator<Map.Entry<Double, Etiquette>> iteration = etiquettes
                .entrySet().iterator();
        while (iteration.hasNext()) {
            final Map.Entry<Double, Etiquette> entree = iteration.next();
            final double valeur = entree.getKey();
            final Etiquette etiquette = entree.getValue();
            etiquette.setPositionAncrage(new PositionReelle(
                    new Vector2D(valeur, ordonneeReelleAxe)));
            if (positionVirtuelle >= toile.getHeight()
                    - etiquette.getHauteur()) {
                etiquette.setPositionRelative(new Vector2D(
                        -etiquette.getLargeur() / 2,
                        toile.getHeight() - positionVirtuelle
                        - etiquette.getHauteur()));
            } else {
                etiquette.setPositionRelative(new Vector2D(
                        -etiquette.getLargeur() / 2, 0));
            }
        }
    }

    /**
     * Calcule l'ordonnée virtuelle de cet axe horizontal.
     *
     * @param toile la toile affichant cet axe.
     * @param repere le repère de l'espace à graduer.
     * @return l'ordonnée virtuelle de l'axe.
     */
    private double positionVirtuelleAxe(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        return repere.ordonneeVirtuelle(positionReelleAxe(toile, repere));
    }

    /**
     * Calcule l'ordonnée réelle de cet axe horizontal.
     *
     * @param toile la toile affichant cet axe.
     * @param repere le repère de l'espace à graduer.
     * @return l'ordonnée réelle de l'axe.
     */
    private double positionReelleAxe(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        final double ordonneeVirtuelleZero = repere.ordonneeVirtuelle(0);
        if (ordonneeVirtuelleZero < 0) {
            return repere.ordonneeReelle(0);
        } else if (ordonneeVirtuelleZero > toile.getHeight()) {
            return repere.ordonneeReelle(toile.getHeight());
        } else {
            return 0;
        }
    }

    @Override
    public double distance(@NotNull final Position curseur,
            @NotNull final Repere repere) {
        return Math.abs(curseur.virtuelle(repere).getY() - positionVirtuelle);
    }

}
