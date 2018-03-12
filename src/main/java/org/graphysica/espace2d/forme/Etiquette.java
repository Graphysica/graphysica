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
import java.awt.image.BufferedImage;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Toile;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

/**
 * Une étiquette permet d'afficher du texte en format TeX à une position
 * relative d'une position d'ancrage.
 *
 * @author Marc-Antoine
 */
public class Etiquette extends Forme {

    /**
     * La taille par défaut des caractères de cette étiquette exprimée en
     * points.
     */
    private static final int TAILLE_CARACTERE_PAR_DEFAUT = 16;

    /**
     * La couleur par défaut des étiquettes.
     */
    private static final Color COULEUR_PAR_DEFAUT = Color.BLACK;

    /**
     * Le texte de cette étiquette.
     */
    private final StringProperty texte = new SimpleStringProperty();

    /**
     * L'image de la formule TeX.
     */
    private Image imageFormule;

    /**
     * L'événement de reconstruction de l'image de la formule. L'image de la
     * formule TeX doit être reconstruite si la couleur de l'étiquette, la
     * taille de caractère du texte ou le texte lui-même est modifié.
     */
    private final InvalidationListener reconstruireImage
            = (@NotNull final Observable observable) -> {
                construireImage();
            };

    /**
     * La taille des caractères de cette étiquette exprimée en points.
     */
    private final IntegerProperty tailleCaractere = new SimpleIntegerProperty(
            TAILLE_CARACTERE_PAR_DEFAUT);

    /**
     * La position réelle d'ancrage de cette étiquette.
     */
    private final ObjectProperty<Vector2D> positionAncrage
            = new SimpleObjectProperty<>(Vector2D.ZERO);

    /**
     * La position virtuelle relative de l'étiquette par rapport à la position
     * d'ancrage.
     */
    private final ObjectProperty<Vector2D> positionRelative
            = new SimpleObjectProperty<>(new Vector2D(5, -25));

    public Etiquette(@NotNull final String texte) {
        setTexte(texte);
        setCouleur(COULEUR_PAR_DEFAUT);
    }

    public Etiquette(@NotNull final String texte,
            @NotNull final Color couleur) {
        this(texte);
        setCouleur(couleur);
    }

    public Etiquette(@NotNull final String texte, final int taille) {
        this(texte);
        setTailleCaractere(taille);
    }

    public Etiquette(@NotNull final String texte, final int taille,
            @NotNull final Color couleur) {
        this(texte, taille);
        setCouleur(couleur);
    }

    public Etiquette(@NotNull final String texte,
            @NotNull final ObjectProperty<Vector2D> positionAncrage) {
        this(texte);
        this.positionAncrage.bind(positionAncrage);
    }

    public Etiquette(@NotNull final String texte, final int taille,
            @NotNull final ObjectProperty<Vector2D> positionAncrage) {
        this(texte, positionAncrage);
        setTailleCaractere(taille);
    }

    public Etiquette(@NotNull final String texte, final int taille,
            @NotNull final Color couleur,
            @NotNull final ObjectProperty<Vector2D> positionAncrage) {
        this(texte, taille, positionAncrage);
        setCouleur(couleur);
    }

    {
        proprietesActualisation.add(texte);
        proprietesActualisation.add(tailleCaractere);
        proprietesActualisation.add(positionAncrage);
        proprietesActualisation.add(positionRelative);
        texte.addListener(reconstruireImage);
        tailleCaractere.addListener(reconstruireImage);
        couleurProperty().addListener(reconstruireImage);
    }

    @Override
    public void dessiner(@NotNull final Toile toile) {
        if (imageFormule == null) {
            construireImage();
        }
        final GraphicsContext contexteGraphique = toile.getGraphicsContext2D();
        final Vector2D position = toile.positionVirtuelle(getPositionAncrage())
                .add(getPositionRelative());
        contexteGraphique.drawImage(imageFormule, (int) (position.getX()),
                (int) (position.getY()));
    }

    /**
     * Construit l'image de la formule TeX à partir du texte.
     */
    private void construireImage() {
        //TODO: ajuster TeXFormula#setDPITarget(float) à l'écran 
        imageFormule = SwingFXUtils.toFXImage(
                (BufferedImage) new TeXFormula(getTexte()).createBufferedImage(
                        TeXConstants.STYLE_TEXT, getTailleCaractere(),
                        couleur(), null), null);
    }

    /**
     * Convertit une couleur de javafx.scene.paint.Color vers java.awt.Color.
     *
     * @return la couleur convertie.
     */
    private java.awt.Color couleur() {
        final Color couleur = getCouleur();
        return new java.awt.Color((float) couleur.getRed(),
                (float) couleur.getGreen(), (float) couleur.getBlue(),
                (float) couleur.getOpacity());
    }

    public final String getTexte() {
        return texte.getValue();
    }

    public final void setTexte(@NotNull final String texte) {
        this.texte.setValue(texte);
    }

    public final StringProperty texteProperty() {
        return texte;
    }

    public final int getTailleCaractere() {
        return tailleCaractere.getValue();
    }

    public final void setTailleCaractere(int tailleCaractere) {
        if (tailleCaractere <= 0) {
            tailleCaractere = TAILLE_CARACTERE_PAR_DEFAUT;
        }
        this.tailleCaractere.setValue(tailleCaractere);
    }

    public final IntegerProperty tailleCaractereProperty() {
        return tailleCaractere;
    }

    private Vector2D getPositionRelative() {
        return positionRelative.getValue();
    }

    public final void setPositionRelative(
            @NotNull final Vector2D positionRelative) {
        this.positionRelative.setValue(positionRelative);
    }

    private Vector2D getPositionAncrage() {
        return positionAncrage.getValue();
    }

}
