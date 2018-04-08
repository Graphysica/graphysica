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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.position.Position;
import org.graphysica.espace2d.Repere;
import static org.graphysica.espace2d.position.Type.VIRTUELLE;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

/**
 * Une étiquette permet d'afficher du texte en format TeX à une position
 * relative d'une position d'ancrage.
 *
 * @author Marc-Antoine Ouimet
 */
public class Etiquette extends Forme {

    /**
     * La taille par défaut des caractères de cette étiquette exprimée en
     * points.
     */
    private static final int TAILLE_CARACTERE_PAR_DEFAUT = 12;

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
    private final ObjectProperty<Position> positionAncrage
            = new SimpleObjectProperty<>();

    /**
     * La position virtuelle relative de l'étiquette par rapport à la position
     * d'ancrage.
     */
    private final ObjectProperty<Vector2D> positionRelative
            = new SimpleObjectProperty<>(new Vector2D(5, -25));

    public Etiquette(@NotNull final String texte) {
        setTexte(texte);
    }

    public Etiquette(@NotNull final String texte, final int taille) {
        this(texte);
        setTailleCaractere(taille);
    }

    static {
        TeXFormula.setDefaultDPI();
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
    public void dessinerNormal(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        if (imageFormule == null) {
            construireImage();
        }
        final GraphicsContext contexteGraphique = toile.getGraphicsContext2D();
        final Vector2D position = getPositionAncrage().deplacer(
                getPositionRelative(), VIRTUELLE, repere).virtuelle(repere);
        contexteGraphique.drawImage(imageFormule, (int) (position.getX()),
                (int) (position.getY()));
        if (isEnSurbrillance()) {
            dessinerSurbrillance(toile, repere);
        }
    }

    @Override
    public void dessinerSurbrillance(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        final Vector2D coinSuperieurGauche = coinSuperieurGauche(repere)
                .virtuelle(repere);
        final GraphicsContext contexteGraphique = toile.getGraphicsContext2D();
        contexteGraphique.setStroke(getCouleur());
        contexteGraphique.setLineWidth(1);
        contexteGraphique.strokeRect(coinSuperieurGauche.getX(),
                coinSuperieurGauche.getY(), getLargeur(), getHauteur());
    }

    /**
     * Construit l'image de la formule TeX à partir du texte.
     */
    private void construireImage() {
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

    @Override
    public double distance(@NotNull final Position curseur,
            @NotNull final Repere repere) {
        if (curseurSurEtiquette(curseur, repere)) {
            return 0;
        }
        final Vector2D coinSuperieurGauche = coinSuperieurGauche(repere)
                .virtuelle(repere);
        final Vector2D coinSuperieurDroit = coinSuperieurGauche.add(
                new Vector2D(getLargeur(), 0));
        final Vector2D coinInferieurDroit = coinInferieurDroit(repere)
                .virtuelle(repere);
        final Vector2D coinInferieurGauche = coinInferieurDroit.add(
                new Vector2D(-getLargeur(), 0));
        return Math.min(new Segment(coinSuperieurGauche, coinSuperieurDroit,
                null).distance(curseur.virtuelle(repere)), Math.min(new Segment(
                coinSuperieurDroit, coinInferieurDroit, null).distance(
                curseur.virtuelle(repere)), Math.min(
                new Segment(coinInferieurDroit, coinInferieurGauche,
                        null).distance(curseur.virtuelle(repere)), new Segment(
                coinInferieurGauche, coinSuperieurGauche, null)
                .distance(curseur.virtuelle(repere)))));
    }

    /**
     * Détermine si la position du curseur se retrouve dans l'espace
     * rectangulaire délimitant l'étiquette.
     *
     * @param curseur la position du curseur.
     * @param repere le repère d'espace.
     * @return {@code true} si la position du curseur est par-dessus
     * l'étiquette.
     */
    private boolean curseurSurEtiquette(@NotNull final Position curseur,
            @NotNull final Repere repere) {
        final Position coinSuperieurGauche = coinSuperieurGauche(repere);
        final Position coinInferieurDroit = coinInferieurDroit(repere);
        boolean curseurSurEtiquette = true;
        curseurSurEtiquette &= curseur.virtuelle(repere).getX()
                >= coinSuperieurGauche.virtuelle(repere).getX();
        curseurSurEtiquette &= curseur.virtuelle(repere).getX()
                <= coinInferieurDroit.virtuelle(repere).getX();
        curseurSurEtiquette &= curseur.virtuelle(repere).getY()
                >= coinSuperieurGauche.virtuelle(repere).getY();
        curseurSurEtiquette &= curseur.virtuelle(repere).getY()
                <= coinInferieurDroit.virtuelle(repere).getY();
        return curseurSurEtiquette;
    }

    /**
     * Récupère la position supérieure gauche de l'étiquette.
     *
     * @param repere le repère d'espace de l'étiquette.
     * @return la position supérieure gauche de l'étiquette.
     */
    private Position coinSuperieurGauche(@NotNull final Repere repere) {
        return getPositionAncrage().deplacer(
                getPositionRelative(), VIRTUELLE, repere);
    }

    /**
     * Récupère la position inférieure droit de l'étiquette.
     *
     * @param repere le repère d'espace de l'étiquette.
     * @return la position inférieure droit de l'étiquette.
     */
    private Position coinInferieurDroit(@NotNull final Repere repere) {
        return coinSuperieurGauche(repere).deplacer(
                new Vector2D(getLargeur(), -getHauteur()), VIRTUELLE,
                repere);
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

    public final Vector2D getPositionRelative() {
        return positionRelative.getValue();
    }

    public final void setPositionRelative(
            @NotNull final Vector2D positionRelative) {
        this.positionRelative.setValue(positionRelative);
    }

    public final Position getPositionAncrage() {
        return positionAncrage.getValue();
    }

    public final void setPositionAncrage(
            @NotNull final Position positionAncrage) {
        this.positionAncrage.setValue(positionAncrage);
    }

    public final double getLargeur() {
        return imageFormule.getWidth();
    }

    public final double getHauteur() {
        return imageFormule.getHeight();
    }

}
