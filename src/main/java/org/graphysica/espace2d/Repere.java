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

import com.sun.istack.internal.NotNull;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Un repère est un repère orthonormé d'un espace virtuel en deux dimensions aux
 * coordonnées réelles. Un repère permet de déterminer la position d'objets aux
 * coordonnées réelles sur l'écran et de déterminer la position d'objet aux
 * coordoonnées virtuels dans l'espace.
 *
 * @author Marc-Antoine Ouimet
 */
public final class Repere {

    /**
     * L'origine virtuelle de l'espace exprimée en pixels selon l'origine de
     * l'écran. Par défaut, l'origine de l'espace est au centre du panneau.
     */
    private final ObjectProperty<Vector2D> origineVirtuelle
            = new SimpleObjectProperty<>();

    /**
     * L'échelle de l'espace exprimée en pixels par mètre.
     */
    private final ObjectProperty<Vector2D> echelle
            = new SimpleObjectProperty<>();

    /**
     * Construit un repère dans un espace virtuel aux dimensions définies.
     * L'origine du repère est alors centré à l'origine.
     *
     * @param largeur la largeur de l'espace virtuel exprimée en pixels.
     * @param hauteur la hauteur de l'espace virtuel exprimée en pixels.
     * @param echelle l'échelle du repère exprimée en pixels par mètre.
     */
    public Repere(final double largeur, final double hauteur,
            @NotNull final Vector2D echelle) {
        centrer(largeur, hauteur);
        setEchelle(echelle);
    }

    /**
     * Construit un repère dont l'origine virtuelle et l'échelle sont définis.
     *
     * @param origineVirtuelle l'origine virtuelle du repère exprimée en pixels.
     * @param echelle l'échelle du repère exprimée en pixels par mètre.
     */
    public Repere(@NotNull final Vector2D origineVirtuelle,
            @NotNull final Vector2D echelle) {
        setOrigineVirtuelle(origineVirtuelle);
        setEchelle(echelle);
    }

    /**
     * Positionne l'origine du repère au centre de contraintes d'un espace
     * virtuel aux dimensions définies.
     *
     * @param largeur la largeur de l'espace virtuel exprimée en pixels.
     * @param hauteur la hauteur de l'espace virtuel exprimée en pixels.
     */
    public void centrer(final double largeur, final double hauteur) {
        setOrigineVirtuelle(new Vector2D(largeur / 2, hauteur / 2));
    }

    /**
     * Calcule l'abscisse virtuelle d'une abscisse réelle.
     *
     * @param abscisseReelle l'abscisse réelle dont on cherche l'abscisse
     * virtuelle.
     * @return l'abscisse virtuelle reflétant l'abscisse réelle.
     */
    public double abscisseVirtuelle(final double abscisseReelle) {
        return abscisseReelle * getEchelle().getX()
                + getOrigineVirtuelle().getX();
    }

    /**
     * Calcule les abscisses virtuelles d'un ensemble d'abscisses réelles.
     *
     * @param abscissesReelles les abscisses réelles dont on cherche la valeur
     * réelle.
     * @return l'ensemble des abscisses virtuelles reflétant les abscisses
     * réelles.
     */
    public double[] abscissesVirtuelles(final double... abscissesReelles) {
        final double[] abscissesVirtuelles
                = new double[abscissesReelles.length];
        for (int i = 0; i < abscissesReelles.length; i++) {
            abscissesVirtuelles[i] = abscisseVirtuelle(abscissesReelles[i]);
        }
        return abscissesVirtuelles;
    }

    /**
     * Calcule l'ordonnée virtuelle d'une ordonnée réelle.
     *
     * @param ordonneeReelle l'ordonnée réelle dont on cherche l'ordonnée
     * virtuelle.
     * @return l'ordonnée virtuelle reflétant l'ordonnée réelle.
     */
    public double ordonneeVirtuelle(final double ordonneeReelle) {
        return -ordonneeReelle * getEchelle().getY()
                + getOrigineVirtuelle().getY();
    }

    /**
     * Calcule les ordonnées virtuelles d'un ensemble d'ordonnées réelles.
     *
     * @param ordonneesReelles les ordonnées réelles dont on cherche la valeur
     * réelle.
     * @return l'ensemble des ordonnées virtuelles reflétant les ordonnées
     * réelles.
     */
    public double[] ordonneesVirtuelles(final double... ordonneesReelles) {
        final double[] ordonneesVirtuelles
                = new double[ordonneesReelles.length];
        for (int i = 0; i < ordonneesReelles.length; i++) {
            ordonneesVirtuelles[i] = ordonneeVirtuelle(ordonneesReelles[i]);
        }
        return ordonneesVirtuelles;
    }

    /**
     * Détermine la position sur la toile d'une position réelle selon l'échelle
     * et l'origine de la toile.
     *
     * @param positionReelle la position réelle dont on cherche la position
     * virtuelle.
     * @return la position virtuelle de {@code positionReelle}.
     */
    public Vector2D positionVirtuelle(@NotNull final Vector2D positionReelle) {
        return new Vector2D(abscisseVirtuelle(positionReelle.getX()),
                ordonneeVirtuelle(positionReelle.getY()));
    }

    /**
     * Calcule l'abscisse réelle d'une abscisse virtuelle.
     *
     * @param abscisseVirtuelle l'abscisse virtuelle dont on cherche l'abscisse
     * réelle.
     * @return l'abscisse réelle reflétant l'abscisse virtuelle.
     */
    public double abscisseReelle(final double abscisseVirtuelle) {
        return -(getOrigineVirtuelle().getX() - abscisseVirtuelle)
                / getEchelle().getX();
    }

    /**
     * Calcule les abscisses réelles d'un ensemble d'abscisses virtuelles.
     *
     * @param abscissesVirtuelles les abscisses virtuelles dont on cherche la
     * valeur réelle.
     * @return l'ensemble des abscisses réelles reflétant les abscisses
     * virtuelles.
     */
    public double[] abscissesReelles(final double... abscissesVirtuelles) {
        final double[] abscissesReelles
                = new double[abscissesVirtuelles.length];
        for (int i = 0; i < abscissesVirtuelles.length; i++) {
            abscissesReelles[i] = abscisseReelle(abscissesVirtuelles[i]);
        }
        return abscissesReelles;
    }

    /**
     * Calcule l'ordonnée réelle d'une ordonnée virtuelle.
     *
     * @param ordonneeVirtuelle l'ordonnée virtuelle dont on cherche l'ordonnée
     * réelle.
     * @return l'ordonnée réelle reflétant l'ordonnée virtuelle.
     */
    public double ordonneeReelle(final double ordonneeVirtuelle) {
        return (getOrigineVirtuelle().getY() - ordonneeVirtuelle)
                / getEchelle().getY();
    }

    /**
     * Calcule les ordonnées réelles d'un ensemble d'ordonnées virtuelles.
     *
     * @param ordonneesVirtuelles les ordonnées virtuelles dont on cherche la
     * valeur réelle.
     * @return l'ensemble des ordonnées réelles reflétant les ordonnées
     * virtuelles.
     */
    public double[] ordonneesReellees(final double... ordonneesVirtuelles) {
        final double[] ordonneesReelles
                = new double[ordonneesVirtuelles.length];
        for (int i = 0; i < ordonneesVirtuelles.length; i++) {
            ordonneesReelles[i] = ordonneeReelle(ordonneesVirtuelles[i]);
        }
        return ordonneesReelles;
    }

    /**
     * Détermine la position réelle d'une position sur la toile selon l'échelle
     * et l'origine de la toile.
     *
     * @param positionVirtuelle la position virtuelle dont on cherche la
     * position réelle.
     * @return la position réelle de {@code positionVirtuelle}.
     */
    public Vector2D positionReelle(@NotNull final Vector2D positionVirtuelle) {
        return new Vector2D(abscisseReelle(positionVirtuelle.getX()),
                ordonneeReelle(positionVirtuelle.getY()));
    }

    public Vector2D getOrigineVirtuelle() {
        return origineVirtuelle.getValue();
    }

    public void setOrigineVirtuelle(@NotNull final Vector2D origineVirtuelle) {
        this.origineVirtuelle.setValue(origineVirtuelle);
    }

    public ObjectProperty<Vector2D> origineVirtuelleProperty() {
        return origineVirtuelle;
    }

    public Vector2D getEchelle() {
        return echelle.getValue();
    }

    public void setEchelle(@NotNull final Vector2D echelle) {
        this.echelle.setValue(echelle);
    }

    public ObjectProperty<Vector2D> echelleProperty() {
        return echelle;
    }

}
