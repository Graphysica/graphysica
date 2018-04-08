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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Repere;
import org.graphysica.espace2d.position.Position;
import org.graphysica.espace2d.position.PositionReelle;

/**
 * Un axe permet de graduer l'espace avec des étiquettes et un sens de
 * propagation.
 *
 * @author Marc-Antoine Ouimet
 */
abstract class Axe extends Forme {

    /**
     * La taille des lignes de tracé de graduations transversales à la flèche
     * représentant l'axe.
     */
    private static final Taille TAILLE_GRADUATION = Taille.de("axegraduation");

    /**
     * L'espacement minimum des graduations de l'axe exprimée en pixels.
     */
    private final DoubleProperty espacement = new SimpleDoubleProperty();

    /**
     * L'ensemble des étiquettes de graduation de l'axe, chacune étant associée
     * à sa valeur réelle de graduation.
     */
    protected final Map<Double, Etiquette> etiquettes = new HashMap<>();

    /**
     * L'origine de l'axe.
     */
    protected final ObjectProperty<Position> origine
            = new SimpleObjectProperty<>(new PositionReelle(Vector2D.ZERO));

    /**
     * L'arrivée de l'axe.
     */
    protected final ObjectProperty<Position> arrivee
            = new SimpleObjectProperty<>(new PositionReelle(Vector2D.ZERO));

    /**
     * La flèche représentant cet axe.
     */
    protected final Fleche fleche = new Fleche(origine, arrivee);

    /**
     * La taille des caractères des étiquettes de graduation de cet axe.
     */
    private final IntegerProperty tailleCaracteres
            = new SimpleIntegerProperty(10);

    /**
     * La position virtuelle de l'axe recalculée à chaque dessin de l'axe.
     */
    protected double positionVirtuelle;

    /**
     * Construit un axe dont l'espacement minimal virtuel est défini.
     *
     * @param espacement la valeur virtuelle d'espacement minimal entre les
     * graduations de l'axe.
     */
    protected Axe(final double espacement) {
        setEspacement(espacement);
    }

    {
        proprietesActualisation.add(espacement);
        proprietesActualisation.add(tailleCaracteres);
    }

    @Override
    public void dessinerSurbrillance(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        fleche.dessinerSurbrillance(toile, repere);
    }

    /**
     * Dessine des marques de graduation sur l'axe.
     *
     * @param toile la toile affichant cet axe.
     * @param valeursVirtuelles les valeurs virtuelles de graduation.
     * @param positionAxe la position virtuelle de l'axe.
     */
    protected abstract void dessinerGraduations(@NotNull final Canvas toile,
            @NotNull final double[] valeursVirtuelles,
            final double positionAxe);

    /**
     * Calcule l'espacement minimal réel entre les graduations de l'axe.
     *
     * @param repere le repère de l'espace à graduer.
     * @return la valeur réelle d'espacement minimal des graduations de l'axe.
     */
    protected abstract double espacementMinimalReel(
            @NotNull final Repere repere);

    /**
     * Récupère la chaîne de caractères de formattage des valeurs d'étiquette de
     * l'axe. Le format d'affichage des étiquettes de graduation de l'axe permet
     * d'arrondir leurs valeurs de telle sorte qu'un nombre minimal de décimales
     * soient affichées.
     *
     * @param repere le repère de l'espace à graduer.
     * @return le format du texte des étiquettes de graduation.
     */
    protected String formatValeurs(@NotNull final Repere repere) {
        final double espacementMinimalReel = espacementMinimalReel(repere);
        final int exposant = (int) (Math.log(espacementMinimalReel)
                / Math.log(Repere.PUISSANCE));
        if (exposant >= 6) {
            return "%2.1e";
        } else if (exposant >= 0) {
            return "%.0f";
        } else {
            final int decimales = (int) Math.abs(exposant);
            if (decimales >= 6) {
                return "%2.1e";
            } else {
                return new StringBuilder("%.").append(decimales).append("f")
                        .toString();
            }
        }
    }

    /**
     * Actualise la quantité d'étiquettes requises pour le tracé de cet axe.
     *
     * @param valeurs l'ensemble des valeurs représentées par les étiquettes.
     * @param format le format d'affichage des valeurs d'étiquettes.
     */
    protected void actualiserEtiquettes(@NotNull double[] valeurs,
            final String format) {
        valeurs = valeursSansZero(valeurs);
        retirerEtiquettesObsoletes(valeurs);
        ajouterEtiquettes(valeurs, format);
    }

    /**
     * Récupère un ensemble des valeurs de graduation sans le zéro. Le zéro
     * correspond au minimum absolu des valeurs de graduation.
     *
     * @param valeurs l'ensemble des valeurs représentées par les étiquettes.
     * @return les valeurs de graduation sans le zéro.
     */
    private double[] valeursSansZero(@NotNull final double[] valeurs) {
        if (valeurs[0] < 0 && valeurs[valeurs.length - 1] > 0) {
            /**
             * Les valeurs contiennent un et un seul zéro qui correspond au
             * minimum des valeurs absolues.
             */
            final double[] valeursFiltrees = new double[valeurs.length - 1];
            double minimumAbsolu = Double.MAX_VALUE;
            for (final double valeur : valeurs) {
                final double valeurAbsolue = Math.abs(valeur);
                if (valeurAbsolue < minimumAbsolu) {
                    minimumAbsolu = valeurAbsolue;
                }
            }
            int i = 0;
            for (final double valeur : valeurs) {
                if (Math.abs(valeur) != minimumAbsolu) {
                    valeursFiltrees[i] = valeur;
                    i++;
                }
            }
            return valeursFiltrees;
        } else {
            return valeurs;
        }
    }

    /**
     * Retire les étiquettes de cet axe dont les valeurs ne sont pas comprises
     * parmi un ensemble défini.
     *
     * @param valeurs l'ensemble des valeurs que peuvent prendre les étiquettes.
     */
    protected void retirerEtiquettesObsoletes(final double[] valeurs) {
        final Iterator<Double> iteration = etiquettes.keySet().iterator();
        while (iteration.hasNext()) {
            final double cle = iteration.next();
            boolean contientCle = false;
            for (final double valeur : valeurs) {
                if (valeur == cle) {
                    contientCle = true;
                    break;
                }
            }
            if (!contientCle) {
                iteration.remove();
            }
        }
    }

    /**
     * Ajoute des étiquettes de graduation d'axe selon un ensemble de valeurs à
     * afficher.
     *
     * @param valeurs l'ensemble des valeurs à afficher sur l'axe.
     * @param format le format des étiquettes.
     */
    protected void ajouterEtiquettes(final double[] valeurs,
            final String format) {
        for (final double valeur : valeurs) {
            if (!etiquettes.containsKey(valeur)) {
                final Etiquette etiquette = new Etiquette(String.format(format,
                        valeur), getTailleCaracteres());
                etiquettes.put(valeur, etiquette);
            }
        }
    }

    public int getTailleGraduation() {
        return TAILLE_GRADUATION.getValue();
    }

    public final double getEspacement() {
        return espacement.getValue();
    }

    public final void setEspacement(@NotNull final double espacement) {
        this.espacement.setValue(espacement);
    }

    public final DoubleProperty espacementProperty() {
        return espacement;
    }

    public final int getTailleCaracteres() {
        return tailleCaracteres.getValue();
    }

    public final void setTailleCaracteres(final int tailleCaracters) {
        this.tailleCaracteres.setValue(tailleCaracters);
    }

    public final IntegerProperty tailleCaracteresProperty() {
        return tailleCaracteres;
    }

    protected final void setOrigine(@NotNull final Position origine) {
        this.origine.setValue(origine);
    }

    protected final void setArrivee(@NotNull final Position arrivee) {
        this.arrivee.setValue(arrivee);
    }

}
