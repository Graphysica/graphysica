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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Une toile permettant d'afficher un ensemble de formes.
 *
 * @author Marc-Antoine Ouimet
 */
public class Toile extends Canvas implements Actualisable {

    /**
     * Le contexte graphique de dessin de la toile.
     */
    private final GraphicsContext contexteGraphique = getGraphicsContext2D();

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
    }
    
    private void traiterDeplacement() {
        origine.addListener(evenementInvalidation -> actualiser());
    }
    
    private void traiterMiseALechelle() {
        echelle.addListener(evenementInvalidation -> actualiser());
    }
    
    public Vector2D positionVirtuelle(@NotNull final Vector2D positionReelle) {
        final Vector2D positionEchelleVirtuelle = new Vector2D(
                positionReelle.getX() * getEchelle().getX(),
                positionReelle.getY() * getEchelle().getY());
        final Vector2D positionSymetrieVirtuelle = new Vector2D(
                positionEchelleVirtuelle.getX(),
                - positionEchelleVirtuelle.getY());
        final Vector2D positionVirtuelle = getOrigine().add(
                positionSymetrieVirtuelle);
        return positionVirtuelle;
    }

    /**
     * Actualise l'affichage de cette toile.
     */
    @Override
    public void actualiser() {
        effacerAffichage();
        //TODO: Définir un ordre prioritaire de dessin (par exemple, les droites avant les points
        formes.forEach((forme) -> {
            forme.dessiner(this);
        });
    }

    private void effacerAffichage() {
        contexteGraphique.clearRect(0, 0, getWidth(), getHeight());
    }
    
    public void ajouter(@NotNull final Forme forme) {
        formes.add(forme);
    }
    
        public ObjectProperty<Vector2D> echelleProperty() {
        return echelle;
    }

    public Vector2D getEchelle() {
        return echelle.getValue();
    }

    public void setEchelle(@NotNull final Vector2D echelle) {
        this.echelle.setValue(echelle);
    }

    public ObjectProperty<Vector2D> origineProperty() {
        return origine;
    }

    public Vector2D getOrigine() {
        return origine.getValue();
    }

    public void setOrigine(@NotNull final Vector2D origine) {
        this.origine.setValue(origine);
    }

}
