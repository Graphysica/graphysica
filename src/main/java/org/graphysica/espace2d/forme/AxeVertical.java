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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Toile;

/**
 * Un axe vertical permet de représenter les valeurs d'ordonnée de l'espace.
 *
 * @author Marc-Antoine
 */
public class AxeVertical extends Axe {

    /**
     * Construit un axe vertical dont l'espacement minimal virtuel est défini.
     *
     * @param espacement la valeur virtuelle d'espacement minimal entre les
     * graduations de l'axe.
     */
    protected AxeVertical(final double espacement) {
        setEspacement(espacement);
    }

    @Override
    public void dessiner(@NotNull final Toile toile) {
        final double[] graduationsHorizontales = toile
                .graduationsHorizontales(getEspacement());
        final double[] ordonneesReelles = toile.ordonneesReellees(
                graduationsHorizontales);
        actualiserEtiquettes(ordonneesReelles, formatValeurs(toile));
        final double positionReelleAxe = positionReelleAxe(toile);
        fleche.setOrigine(new Vector2D(
                positionReelleAxe,
                toile.ordonneeReelle(toile.getHeight())));
        fleche.setArrivee(new Vector2D(
                positionReelleAxe, toile.ordonneeReelle(0)));
        dessinerGraduations(toile, graduationsHorizontales,
                positionVirtuelleAxe(toile));
        fleche.dessiner(toile);
        actualiserPositionEtiquettes(toile);
        etiquettes.values().forEach((etiquette) -> {
            etiquette.dessiner(toile);
        });
    }

    /**
     * Dessine des marques de graduations sur l'axe.
     *
     * @param toile la toile affichant cet axe.
     * @param valeursVirtuelles les valeurs virtuelles de graduation.
     * @param positionAxe la position virtuelle de l'axe.
     */
    private void dessinerGraduations(@NotNull final Toile toile,
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
     * @param toile la toile affichant cet axe.
     * @return l'espacement minimal réel des graduations.
     */
    @Override
    protected double espacementMinimalReel(@NotNull final Toile toile) {
        return getEspacement() / toile.getEchelle().getY();
    }

    /**
     * Actualise la position des étiquettes de cet axe.
     *
     * @param toile la toile affichant cet axe.
     */
    private void actualiserPositionEtiquettes(@NotNull final Toile toile) {
        final double abscisseReelleAxe = positionReelleAxe(toile);
        final double abscisseVirtuelleAxe = positionVirtuelleAxe(toile);
        final Iterator<Map.Entry<Double, Etiquette>> iteration = etiquettes
                .entrySet().iterator();
        while (iteration.hasNext()) {
            final Map.Entry<Double, Etiquette> entree = iteration.next();
            final double valeur = entree.getKey();
            final Etiquette etiquette = entree.getValue();
            etiquette.setPositionAncrage(
                    new Vector2D(abscisseReelleAxe, valeur));
            if (abscisseVirtuelleAxe >= toile.getWidth()
                    - etiquette.getLargeur()) {
                etiquette.setPositionRelative(new Vector2D(
                        toile.getWidth() - abscisseVirtuelleAxe
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
     * @return l'abscisse virtuelle de l'axe.
     */
    private double positionVirtuelleAxe(@NotNull final Toile toile) {
        return toile.abscisseVirtuelle(positionReelleAxe(toile));
    }

    /**
     * Calcule l'abscisse réelle de cet axe horizontal.
     *
     * @param toile la toile affichant cet axe.
     * @return l'abscisse réelle de l'axe.
     */
    private double positionReelleAxe(@NotNull final Toile toile) {
        final double abscisseVirtuelleZero = toile.abscisseVirtuelle(0);
        if (abscisseVirtuelleZero < 0) {
            return toile.abscisseReelle(0);
        } else if (abscisseVirtuelleZero > toile.getWidth()) {
            return toile.abscisseReelle(toile.getWidth());
        } else {
            return 0;
        }
    }

}
