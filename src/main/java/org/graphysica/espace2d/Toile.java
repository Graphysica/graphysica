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
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Une toile permettant d'afficher un ensemble de formes.
 *
 * @author Marc-Antoine Ouimet
 */
public class Toile extends Canvas implements Actualisable {

    /**
     * La liste observable des formes dessinées sur la toile.
     */
    private final ObservableList<Forme> formes
            = FXCollections.observableArrayList();

    /**
     * L'échelle de l'espace exprimée en pixels par mètre.
     */
    private final ObjectProperty<Vector2D> echelle
            = new SimpleObjectProperty<>(new Vector2D(100, 100));

    /**
     * L'espacement minimum des graduations de la grille exprimée en pixels.
     */
    private final ObjectProperty<Vector2D> espacement
            = new SimpleObjectProperty<>(new Vector2D(50, 50));

    /**
     * L'origine virtuelle de l'espace exprimée en pixels selon l'origine de
     * l'écran. Par défaut, l'origine de l'espace est au centre du panneau.
     */
    private final ObjectProperty<Vector2D> origine
            = new SimpleObjectProperty<>(
                    new Vector2D(getWidth() / 2, getHeight() / 2));

    public Toile(final double largeur, final double hauteur) {
        super(largeur, hauteur);
        traiterDeplacement();
        traiterMiseALechelle();
        formes.add(new Grille(this));
    }

    /**
     * Actualise la toile lorsque son origine est translatée, ce qui déplace
     * l'espace virtuel affiché.
     */
    private void traiterDeplacement() {
        origine.addListener(evenementActualisation);
    }

    /**
     * Actualise la toile lorsque son échelle est modifiée, ce qui agrandi ou
     * rétrécie l'espace virtuel affiché.
     */
    private void traiterMiseALechelle() {
        echelle.addListener(evenementActualisation);
    }

    /**
     * Calcule l'abscisse virtuelle d'une abscisse réelle.
     *
     * @param abscisseReelle l'abscisse réelle dont on cherche l'abscisse
     * virtuelle.
     * @return l'abscisse virtuelle reflétant l'abscisse réelle.
     */
    public double abscisseVirtuelle(final double abscisseReelle) {
        return abscisseReelle * getEchelle().getX() + getOrigine().getX();
    }

    /**
     * Calcule l'ordonnée virtuelle d'une ordonnée réelle.
     *
     * @param ordonneeReelle l'ordonnée réelle dont on cherche l'ordonnée
     * virtuelle.
     * @return l'ordonnée virtuelle reflétant l'ordonnée réelle.
     */
    public double ordonneeVirtuelle(final double ordonneeReelle) {
        return -ordonneeReelle * getEchelle().getY() + getOrigine().getY();
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
        return -(getOrigine().getX() - abscisseVirtuelle) / getEchelle().getX();
    }

    /**
     * Calcule l'ordonnée réelle d'une ordonnée virtuelle.
     *
     * @param ordonneeVirtuelle l'ordonnée virtuelle dont on cherche l'ordonnée
     * réelle.
     * @return l'ordonnée réelle reflétant l'ordonnée virtuelle.
     */
    public double ordonneeReelle(final double ordonneeVirtuelle) {
        return (getOrigine().getY() - ordonneeVirtuelle) / getEchelle().getY();
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

    /**
     * Actualise l'affichage de cette toile.
     */
    @Override
    public void actualiser() {
        effacerAffichage();
        //TODO: Définir un ordre prioritaire de dessin (par exemple, les droites avant les points
        formes.forEach((forme) -> {
            if (forme.isAffichee()) {
                forme.dessiner(this);
            }
        });
    }

    private void effacerAffichage() {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Ajoute une forme à la toile et lie ses propriétés à l'actualisation de la
     * toile.
     *
     * @param forme la forme à ajouter à la toile.
     */
    public void ajouter(@NotNull final Forme forme) {
        formes.add(forme);
        for (final Observable propriete : forme.proprietesActualisation) {
            propriete.addListener(evenementActualisation);
        }
    }

    /**
     * Retire la forme de la toile et délie ses propriétés de l'actualisation de
     * la toile.
     *
     * @param forme la forme à retirer de la toile.
     */
    public void retirer(@NotNull final Forme forme) {
        final boolean retiree = formes.remove(forme);
        if (retiree) {
            for (final Observable propriete : forme.proprietesActualisation) {
                propriete.removeListener(evenementActualisation);
            }
        }
    }

    /**
     * L'événement d'actualisation des formes en cas d'invalidation de leurs
     * propriétés.
     */
    protected final InvalidationListener evenementActualisation
            = (@NotNull final Observable observable) -> {
                actualiser();
            };

    public final ObjectProperty<Vector2D> echelleProperty() {
        return echelle;
    }

    public final Vector2D getEchelle() {
        return echelle.getValue();
    }

    public final void setEchelle(@NotNull final Vector2D echelle) {
        this.echelle.setValue(echelle);
    }

    public final ObjectProperty<Vector2D> origineProperty() {
        return origine;
    }

    public final Vector2D getOrigine() {
        return origine.getValue();
    }

    public final void setOrigine(@NotNull final Vector2D origine) {
        this.origine.setValue(origine);
    }

    public final Vector2D getEspacement() {
        return espacement.getValue();
    }

    public final void setEspacement(@NotNull final Vector2D espacement) {
        this.espacement.setValue(espacement);
    }

    public final ObjectProperty<Vector2D> espacementProperty() {
        return espacement;
    }

}
