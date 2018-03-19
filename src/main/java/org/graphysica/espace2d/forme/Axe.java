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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Toile;

/**
 *
 * @author Marc-Antoine
 */
public class Axe extends Forme {

    /**
     * L'espacement minimum des graduations de l'axe exprimée en pixels.
     */
    private final DoubleProperty espacement = new SimpleDoubleProperty();

    /**
     * L'énumération des sens possibles d'un axe.
     */
    public enum Sens {
        VERTICAL,
        HORIZONTAL
    }

    /**
     * Le sens de cet axe.
     */
    private final Sens sens;

    /**
     * L'ensemble des étiquettes de graduation de l'axe, chacune étant associée
     * à sa valeur réelle de graduation.
     */
    private final Map<Double, Etiquette> etiquettes = new HashMap<>();

    /**
     * La flèche représentant cet axe.
     */
    private final Fleche fleche = new Fleche(Vector2D.ZERO, Vector2D.ZERO);

    /**
     * La taille des caractères des étiquettes de graduation de cet axe.
     */
    private final IntegerProperty tailleCaracteres 
            = new SimpleIntegerProperty(14);

    /**
     * Construit un axe dont le sens et l'espacement minimale
     *
     * @param sens
     * @param espacement
     */
    public Axe(@NotNull final Sens sens, final double espacement) {
        this.sens = sens;
        setEspacement(espacement);
    }

    {
        proprietesActualisation.add(espacement);
        proprietesActualisation.add(tailleCaracteres);
    }

    @Override
    public void dessiner(@NotNull final Toile toile) {
        switch (sens) {
            case HORIZONTAL: {
                final double[] graduationsVerticales = toile
                        .graduationsVerticales(getEspacement());
                final double[] abscissesReelles = toile.abscissesReelles(
                        graduationsVerticales);
                actualiserEtiquettes(abscissesReelles, formatValeurs(toile));
                final double positionReelleAxe = positionReelleAxe(toile);
                fleche.setOrigine(new Vector2D(
                        toile.abscisseReelle(0), positionReelleAxe));
                fleche.setArrivee(new Vector2D(
                        toile.abscisseReelle(toile.getWidth()),
                        positionReelleAxe));
                dessinerGraduations(toile, graduationsVerticales,
                        positionVirtuelleAxe(toile));
                break;
            }
            case VERTICAL: {
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
                break;
            }
            default: {
                throw new UnsupportedOperationException(
                        "Sens d'axe non supporté: " + sens + ".");
            }
        }
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
        switch (sens) {
            case HORIZONTAL: {
                for (final double abscisseVirtuelle : valeursVirtuelles) {
                    contexteGraphique.strokeLine(
                            abscisseVirtuelle, positionAxe + 3,
                            abscisseVirtuelle, positionAxe - 3);
                }
                break;
            }
            case VERTICAL: {
                for (final double ordonneeVirtuelle : valeursVirtuelles) {
                    contexteGraphique.strokeLine(
                            positionAxe + 3, ordonneeVirtuelle,
                            positionAxe - 3, ordonneeVirtuelle);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException(
                        "Sens d'axe non supporté: " + sens + ".");
            }
        }
    }

    /**
     * Calcule la position réelle de l'axe sur la toile. Il peut s'agir de
     * l'ordonnée ou l'abscisse de l'axe en tant que droite horizontale ou
     * verticale, le cas échéant.
     *
     * @param toile la toile affichant cet axe.
     * @return l'abscisse ou l'ordonnée de l'axe selon son sens.
     */
    private double positionReelleAxe(@NotNull final Toile toile) {
        switch (sens) {
            case HORIZONTAL: {
                final double ordonneeVirtuelleZero = toile.ordonneeVirtuelle(0);
                if (ordonneeVirtuelleZero < 0) {
                    return toile.ordonneeReelle(0);
                } else if (ordonneeVirtuelleZero > toile.getHeight()) {
                    return toile.ordonneeReelle(toile.getHeight());
                } else {
                    return 0;
                }
            }
            case VERTICAL: {
                final double abscisseVirtuelleZero = toile.abscisseVirtuelle(0);
                if (abscisseVirtuelleZero < 0) {
                    return toile.abscisseReelle(0);
                } else if (abscisseVirtuelleZero > toile.getWidth()) {
                    return toile.abscisseReelle(toile.getWidth());
                } else {
                    return 0;
                }
            }
            default: {
                throw new UnsupportedOperationException(
                        "Sens d'axe non supporté: " + sens + ".");
            }
        }
    }

    /**
     * Calcule la position virtuelle de l'axe sur la toile. Il peut s'agir de
     * l'ordonnée ou l'abscisse de l'axe en tant que droite horizontale ou
     * verticale, le cas échéant.
     *
     * @param toile la toile affichant cet axe.
     * @return l'abscisse ou l'ordonnée de l'axe selon son sens.
     */
    private double positionVirtuelleAxe(@NotNull final Toile toile) {
        switch (sens) {
            case HORIZONTAL: {
                return toile.ordonneeVirtuelle(positionReelleAxe(toile));
            }
            case VERTICAL: {
                return toile.abscisseVirtuelle(positionReelleAxe(toile));
            }
            default: {
                throw new UnsupportedOperationException(
                        "Sens d'axe non supporté: " + sens + ".");
            }
        }
    }

    /**
     * Récupère la chaîne de caractères de formattage des valeurs d'étiquette de
     * l'axe.
     *
     * @param toile la toile sur laquelle l'axe est dessiné.
     * @return le format du texte des étiquettes de graduation.
     */
    private String formatValeurs(@NotNull final Toile toile) {
        final double espacementMinimalReel;
        switch (sens) {
            case HORIZONTAL:
                espacementMinimalReel = getEspacement()
                        / toile.getEchelle().getX();
                break;
            case VERTICAL:
                espacementMinimalReel = getEspacement()
                        / toile.getEchelle().getY();
                break;
            default: {
                throw new UnsupportedOperationException(
                        "Sens d'axe non supporté: " + sens + ".");
            }
        }
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
     */
    private void actualiserEtiquettes(@NotNull final double[] valeurs,
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
    private void retirerEtiquettesObsoletes(final double[] valeurs) {
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
    private void ajouterEtiquettes(final double[] valeurs,
            final String format) {
        for (final double valeur : valeurs) {
            if (!etiquettes.containsKey(valeur)) {
                final Etiquette etiquette = new Etiquette(String.format(format,
                        valeur), getTailleCaracteres());
                etiquettes.put(valeur, etiquette);
            }
        }
    }

    /**
     * Actualise la position des étiquettes de cet axe.
     */
    private void actualiserPositionEtiquettes(@NotNull final Toile toile) {
        final double positionReelleAxe = positionReelleAxe(toile);
        final double positionVirtuelleAxe = positionVirtuelleAxe(toile);
        final Iterator<Map.Entry<Double, Etiquette>> iteration = etiquettes
                .entrySet().iterator();
        while (iteration.hasNext()) {
            final Map.Entry<Double, Etiquette> entree = iteration.next();
            final double valeur = entree.getKey();
            final Etiquette etiquette = entree.getValue();
            switch (sens) {
                case HORIZONTAL: {
                    etiquette.setPositionAncrage(
                            new Vector2D(valeur, positionReelleAxe));
                    if (positionVirtuelleAxe >= toile.getHeight()
                            - etiquette.getHauteur()) {
                        etiquette.setPositionRelative(new Vector2D(
                                -etiquette.getLargeur() / 2,
                                toile.getHeight() - positionVirtuelleAxe
                                - etiquette.getHauteur()));
                    } else {
                        etiquette.setPositionRelative(new Vector2D(
                                -etiquette.getLargeur() / 2, 0));
                    }
                    break;
                }
                case VERTICAL: {
                    etiquette.setPositionAncrage(
                            new Vector2D(positionReelleAxe, valeur));
                    if (positionVirtuelleAxe >= toile.getWidth()
                            - etiquette.getLargeur()) {
                        etiquette.setPositionRelative(new Vector2D(
                                toile.getWidth() - positionVirtuelleAxe
                                - etiquette.getLargeur(),
                                -etiquette.getHauteur() / 2));
                    } else {
                        etiquette.setPositionRelative(new Vector2D(
                                0, -etiquette.getHauteur() / 2));
                    }
                    break;
                }
                default: {
                    throw new UnsupportedOperationException(
                            "Sens d'axe non supporté: " + sens + ".");
                }
            }
        }
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
