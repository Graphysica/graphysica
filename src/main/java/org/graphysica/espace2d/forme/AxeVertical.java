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
 * Un axe vertical permet de représenter les valeurs d'ordonnée de l'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public class AxeVertical extends Axe {

    /**
     * Construit un axe vertical dont l'espacement minimal virtuel est défini.
     *
     * @param espacement la valeur virtuelle d'espacement minimal entre les
     * graduations de l'axe.
     */
    protected AxeVertical(final double espacement) {
        super(espacement);
    }

    @Override
    public void dessinerNormal(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        if (isEnSurbrillance()) {
            dessinerSurbrillance(toile, repere);
        }
        final double[] graduationsHorizontales = repere
                .graduationsHorizontales(toile.getHeight(), getEspacement());
        final double[] ordonneesReelles = repere.ordonneesReellees(
                graduationsHorizontales);
        actualiserEtiquettes(ordonneesReelles, formatValeurs(repere));
        final double positionReelleAxe = positionReelleAxe(toile, repere);
        setOrigine(new PositionReelle(new Vector2D(positionReelleAxe,
                repere.ordonneeReelle(toile.getHeight()))));
        setArrivee(new PositionReelle(new Vector2D(positionReelleAxe,
                repere.ordonneeReelle(0))));
        dessinerGraduations(toile, graduationsHorizontales,
                positionVirtuelleAxe(toile, repere));
        fleche.dessiner(toile, repere);
        actualiserPositionEtiquettes(toile, repere);
        etiquettes.values().forEach((etiquette) -> {
            etiquette.dessiner(toile, repere);
        });
    }

    @Override
    protected void dessinerGraduations(@NotNull final Canvas toile,
            @NotNull final double[] valeursVirtuelles,
            final double positionAxe) {
        final GraphicsContext contexteGraphique = toile.getGraphicsContext2D();
        contexteGraphique.setStroke(Color.BLACK);
        contexteGraphique.setLineWidth(1);
        for (final double ordonneeVirtuelle : valeursVirtuelles) {
            contexteGraphique.strokeLine(
                    positionAxe + getTailleGraduation(), ordonneeVirtuelle,
                    positionAxe - getTailleGraduation(), ordonneeVirtuelle);
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
        return getEspacement() / repere.getEchelle().getY();
    }

    /**
     * Actualise la position des étiquettes de cet axe.
     *
     * @param toile la toile affichant cet axe.
     * @param repere le repère de l'espace à graduer.
     */
    private void actualiserPositionEtiquettes(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        final double abscisseReelleAxe = positionReelleAxe(toile, repere);
        positionVirtuelle = positionVirtuelleAxe(toile, repere);
        final Iterator<Map.Entry<Double, Etiquette>> iteration = etiquettes
                .entrySet().iterator();
        while (iteration.hasNext()) {
            final Map.Entry<Double, Etiquette> entree = iteration.next();
            final double valeur = entree.getKey();
            final Etiquette etiquette = entree.getValue();
            etiquette.setPositionAncrage(new PositionReelle(
                    new Vector2D(abscisseReelleAxe, valeur)));
            if (positionVirtuelle >= toile.getWidth()
                    - etiquette.getLargeur()) {
                etiquette.setPositionRelative(new Vector2D(
                        toile.getWidth() - positionVirtuelle
                        - etiquette.getLargeur(),
                        -etiquette.getHauteur() / 2));
            } else {
                etiquette.setPositionRelative(new Vector2D(
                        0, -etiquette.getHauteur() / 2));
            }
        }
    }

    /**
     * Calcule l'abscisse virtuelle de cet axe vertical.
     *
     * @param toile la toile affichant cet axe.
     * @param repere le repère de l'espace à graduer.
     * @return l'abscisse virtuelle de l'axe.
     */
    private double positionVirtuelleAxe(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        final double abscisseVirtuelleZero = repere.abscisseVirtuelle(0);
        if (abscisseVirtuelleZero < 0) {
            return 0;
        } else if (abscisseVirtuelleZero > toile.getWidth()) {
            return toile.getWidth();
        } else {
            return abscisseVirtuelleZero;
        }
    }

    /**
     * Calcule l'abscisse réelle de cet axe horizontal.
     *
     * @param toile la toile affichant cet axe.
     * @param repere le repère de l'espace à graduer.
     * @return l'abscisse réelle de l'axe.
     */
    private double positionReelleAxe(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        return repere.abscisseReelle(positionVirtuelleAxe(toile, repere));
    }

    @Override
    public double distance(@NotNull final Position curseur,
            @NotNull final Repere repere) {
        return Math.abs(curseur.virtuelle(repere).getX() - positionVirtuelle);
    }

}
