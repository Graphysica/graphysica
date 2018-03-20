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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Toile;

/**
 * Un axe permet de graduer l'espace avec des étiquettes et un sens de
 * propagation.
 *
 * @author Marc-Antoine Ouimet
 */
public abstract class Axe extends Forme {

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
     * La flèche représentant cet axe.
     */
    protected final Fleche fleche = new Fleche(Vector2D.ZERO, Vector2D.ZERO);

    /**
     * La taille des caractères des étiquettes de graduation de cet axe.
     */
    private final IntegerProperty tailleCaracteres
            = new SimpleIntegerProperty(14);

    /**
     * L'énumération des sens possibles d'un axe.
     */
    public static enum Sens {
        VERTICAL,
        HORIZONTAL
    }

    /**
     * Construit un nouvel axe dans le sens et d'espacement de graudations
     * spécifés.
     *
     * @param sens le sens de l'axe.
     * @param espacement l'espacement minimal entre chacune des graduations de
     * l'axe.
     * @return un nouvel axe dans le sens prescrit.
     */
    public static Axe nouvelAxe(@NotNull final Sens sens,
            final double espacement) {
        switch (sens) {
            case HORIZONTAL:
                return new AxeHorizontal(espacement);
            case VERTICAL:
                return new AxeVertical(espacement);
            default:
                throw new UnsupportedOperationException(
                        "Sens d'axe non supporté: " + sens + ".");
        }
    }

    {
        proprietesActualisation.add(espacement);
        proprietesActualisation.add(tailleCaracteres);
    }

    /**
     * Calcule l'espacement minimal réel entre les graduations de l'axe.
     *
     * @param toile la toile affichant cet axe.
     * @return la valeur réelle d'espacement minimal des graduations de l'axe.
     */
    protected abstract double espacementMinimalReel(@NotNull final Toile toile);

    /**
     * Récupère la chaîne de caractères de formattage des valeurs d'étiquette de
     * l'axe. Le format d'affichage des étiquettes de graduation de l'axe permet
     * d'arrondir leurs valeurs de telle sorte qu'un nombre minimal de décimales
     * soient affichées.
     *
     * @param toile la toile sur laquelle l'axe est dessiné.
     * @return le format du texte des étiquettes de graduation.
     */
    protected String formatValeurs(@NotNull final Toile toile) {
        final double espacementMinimalReel = espacementMinimalReel(toile);
        final int exposant = (int) (Math.log(espacementMinimalReel)
                / Math.log(Toile.PUISSANCE));
        if (exposant >= 0) {
            return "%.0f";
        } else {
            return new StringBuilder("%.").append((int) (Math.abs(exposant)))
                    .append("f").toString();
        }
    }

    /**
     * Actualise la quantité d'étiquettes requises pour le tracé de cet axe.
     *
     * @param valeurs l'ensemble des valeurs représentées par les étiquettes.
     * @param format le format d'affichage des valeurs d'étiquettes.
     */
    protected void actualiserEtiquettes(@NotNull final double[] valeurs,
            final String format) {
        retirerEtiquettesObsoletes(valeurs);
        ajouterEtiquettes(valeurs, format);
        etiquettes.remove(-0.0);
        etiquettes.remove(0.0);
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

}
