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
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.position.Position;
import org.graphysica.espace2d.Repere;
import static org.graphysica.espace2d.position.Type.VIRTUELLE;
import org.jfree.fx.FXGraphics2D;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXFormula.TeXIconBuilder;
import org.scilab.forge.jlatexmath.TeXIcon;

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
     * Le texte de cette étiquette.
     */
    private final StringProperty texte = new SimpleStringProperty();

    /**
     * L'icône de la formule TeX.
     */
    private TeXIcon icone;

    /**
     * L'événement de reconstruction de l'icône de la formule. L'icône de la
     * formule TeX doit être reconstruite si la taille de caractère du texte ou
     * le texte lui-même est modifié.
     */
    private final InvalidationListener reconstruireIcone
            = (@NotNull final Observable observable) -> {
                construireIcone();
            };

    /**
     * La taille des caractères de cette étiquette exprimée en points.
     */
    private final IntegerProperty tailleCaracteres = new SimpleIntegerProperty(
            TAILLE_CARACTERE_PAR_DEFAUT);

    /**
     * La position d'ancrage de cette étiquette.
     */
    private final ObjectProperty<Position> positionAncrage
            = new SimpleObjectProperty<>();

    /**
     * La position virtuelle relative de l'étiquette par rapport à la position
     * d'ancrage.
     */
    private final ObjectProperty<Vector2D> positionRelative
            = new SimpleObjectProperty<>(new Vector2D(5, -25));

    /**
     * Le dernier contexte graphique de dessin de cette étiquette. Il est
     * modifié lorsqu'un nouvel appel de dessin de cette étiquette est effectué
     * à partir d'un autre contexte graphique.
     */
    private GraphicsContext dernierContexteGraphique;

    /**
     * Le contexte graphique adapté du {@code dernierContexteGraphique}. Il est
     * modifié si {@code dernierContexteGraphique} est modifié. Il permet
     * d'empêcher qu'un contexte graphique adaptif à Swing soit instancié à
     * chaque dessin de l'étiquette.
     */
    private FXGraphics2D contexteGraphique;

    /**
     * Construit une étiquette dont le texte est défini.
     *
     * @param texte le texte de l'étiquette.
     */
    Etiquette(@NotNull final String texte) {
        setTexte(texte);
    }

    /**
     * Construit une étiquette dont le texte et la taille sont définis.
     *
     * @param texte le texte de l'étiquette.
     * @param tailleCaracters la taille des caractères du texte de l'étiquette,
     * exprimée en points.
     */
    Etiquette(@NotNull final String texte,
            @NotNull final IntegerProperty tailleCaracters) {
        this(texte);
        this.tailleCaracteres.bind(tailleCaracters);
    }

    /**
     * Construit une étiquette ancrée à une position d'ancrage définie.
     *
     * @param texte le texte de l'étiquette.
     * @param positionAncrage la position d'ancrage de l'étiquette.
     */
    public Etiquette(@NotNull final StringProperty texte,
            @NotNull final ObjectProperty<? extends Position> positionAncrage) {
        this.texte.bind(texte);
        this.positionAncrage.bind(positionAncrage);
    }

    /**
     * Construit une étiquette ancrée à une position d'ancrage définie.
     *
     * @param texte le texte de l'étiquette.
     * @param positionAncrage la position d'ancrage de l'étiquette.
     * @param tailleCaracters la taille des caractères du texte de l'étiquette,
     * exprimée en points.
     */
    public Etiquette(@NotNull final StringProperty texte,
            @NotNull final ObjectProperty<? extends Position> positionAncrage,
            @NotNull final IntegerProperty tailleCaracters) {
        this(texte, positionAncrage);
        this.tailleCaracteres.bind(tailleCaracters);
    }
    
    /**
     * Construit une étiquette ancrée à une position d'ancrage définie.
     *
     * @param texte le texte de l'étiquette.
     * @param positionAncrage la position d'ancrage de l'étiquette.
     * @param couleur la couleur du texte de l'étiquette.
     */
    public Etiquette(@NotNull final StringProperty texte,
            @NotNull final ObjectProperty<? extends Position> positionAncrage,
            @NotNull final ObjectProperty<Color> couleur) {
        this(texte, positionAncrage);
        couleurProperty().bind(couleur);
    }
    
    /**
     * Construit une étiquette ancrée à une position d'ancrage définie.
     *
     * @param texte le texte de l'étiquette.
     * @param positionAncrage la position d'ancrage de l'étiquette.
     * @param positionRelative la position relative de l'étiquette.
     * @param couleur la couleur du texte de l'étiquette.
     */
    public Etiquette(@NotNull final StringProperty texte,
            @NotNull final ObjectProperty<? extends Position> positionAncrage,
            @NotNull final Vector2D positionRelative,
            @NotNull final ObjectProperty<Color> couleur) {
        this(texte, positionAncrage);
        setPositionRelative(positionRelative);
        couleurProperty().bind(couleur);
    }
    
    /**
     * Construit une étiquette ancrée à une position d'ancrage définie.
     *
     * @param texte le texte de l'étiquette.
     * @param positionAncrage la position d'ancrage de l'étiquette.
     * @param positionRelative la position relative de l'étiquette.
     */
    public Etiquette(@NotNull final StringProperty texte,
            @NotNull final ObjectProperty<? extends Position> positionAncrage,
            @NotNull final Vector2D positionRelative) {
        this(texte, positionAncrage);
        setPositionRelative(positionRelative);
    }
    
    /**
     * Construit une étiquette ancrée à une position d'ancrage définie.
     *
     * @param texte le texte de l'étiquette.
     * @param positionAncrage la position d'ancrage de l'étiquette.
     * @param tailleCaracters la taille des caractères du texte de l'étiquette,
     * exprimée en points.
     * @param couleur la couleur du texte de l'étiquette.
     */
    public Etiquette(@NotNull final StringProperty texte,
            @NotNull final ObjectProperty<? extends Position> positionAncrage,
            @NotNull final IntegerProperty tailleCaracters,
            @NotNull final ObjectProperty<Color> couleur) {
        this(texte, positionAncrage);
        this.tailleCaracteres.bind(tailleCaracters);
        couleurProperty().bind(couleur);
    }

    static {
        TeXFormula.setDefaultDPI();
    }

    {
        proprietes.add(texte);
        proprietes.add(tailleCaracteres);
        proprietes.add(positionAncrage);
        proprietes.add(positionRelative);
        texte.addListener(reconstruireIcone);
        tailleCaracteres.addListener(reconstruireIcone);
    }

    /**
     * Actualise le contexte graphique de dessin de cette étiquette.
     *
     * @param toile la toile de prochain dessin de cette étiquette.
     */
    private void actualiserContexteGraphique(@NotNull final Canvas toile) {
        if (contexteGraphique == null
                || dernierContexteGraphique != toile.getGraphicsContext2D()) {
            toile.getGraphicsContext2D().setFill(getCouleur());
            contexteGraphique = new FXGraphics2D(toile.getGraphicsContext2D());
        }
    }

    @Override
    public void dessinerNormal(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        actualiserContexteGraphique(toile);
        if (icone == null) {
            construireIcone();
        }
        final Vector2D position = coinSuperieurGauche(repere).virtuelle(repere);
        contexteGraphique.setColor(couleur());
        icone.paintIcon(null, contexteGraphique,
                (int) (position.getX()), (int) (position.getY()));
        if (isEnSurvol()) {
            dessinerSurvol(toile, repere);
        }
    }

    @Override
    public void dessinerSurvol(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        final Vector2D coinSuperieurGauche = coinSuperieurGauche(repere)
                .virtuelle(repere);
        final GraphicsContext contexteGraphique = toile.getGraphicsContext2D();
        contexteGraphique.setStroke(getCouleur().deriveColor(0, 0, 0, 0.1));
        contexteGraphique.setLineWidth(1);
        contexteGraphique.strokeRoundRect(coinSuperieurGauche.getX(),
                coinSuperieurGauche.getY(), getLargeur(), getHauteur(), 5, 5);
    }

    /**
     * Construit l'icône de la formule TeX à partir du texte.
     */
    private void construireIcone() {
        final TeXFormula formule = new TeXFormula(getTexte());
        icone = formule.new TeXIconBuilder()
                .setStyle(TeXConstants.STYLE_DISPLAY)
                .setSize(getTailleCaractere())
                .setWidth(TeXConstants.UNIT_POINT, 100, TeXConstants.ALIGN_LEFT)
                .setIsMaxWidth(true).build();
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
        final Vector2D positionCurseur = curseur.virtuelle(repere);
        final Vector2D coinSuperieurGauche = coinSuperieurGauche(repere)
                .virtuelle(repere);
        final Vector2D coinInferieurDroit = coinInferieurDroit(repere)
                .virtuelle(repere);
        boolean curseurSurEtiquette = true;
        curseurSurEtiquette &= positionCurseur.getX()
                >= coinSuperieurGauche.getX();
        curseurSurEtiquette &= positionCurseur.getX()
                <= coinInferieurDroit.getX();
        curseurSurEtiquette &= positionCurseur.getY()
                >= coinSuperieurGauche.getY();
        curseurSurEtiquette &= positionCurseur.getY()
                <= coinInferieurDroit.getY();
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
                new Vector2D(getLargeur(), getHauteur()), VIRTUELLE,
                repere);
    }

    private String getTexte() {
        return texte.getValue();
    }

    private void setTexte(@NotNull final String texte) {
        this.texte.setValue(texte);
    }

    public final StringProperty texteProperty() {
        return texte;
    }

    private int getTailleCaractere() {
        return tailleCaracteres.getValue();
    }

    public final void setTailleCaractere(int tailleCaractere) {
        if (tailleCaractere <= 0) {
            tailleCaractere = TAILLE_CARACTERE_PAR_DEFAUT;
        }
        this.tailleCaracteres.setValue(tailleCaractere);
    }

    public final IntegerProperty tailleCaractereProperty() {
        return tailleCaracteres;
    }

    private Vector2D getPositionRelative() {
        return positionRelative.getValue();
    }

    public final void setPositionRelative(
            @NotNull final Vector2D positionRelative) {
        this.positionRelative.setValue(positionRelative);
    }

    private Position getPositionAncrage() {
        return positionAncrage.getValue();
    }

    void setPositionAncrage(@NotNull final Position positionAncrage) {
        this.positionAncrage.setValue(positionAncrage);
    }

    public final double getLargeur() {
        return icone.getIconWidth();
    }

    public final double getHauteur() {
        return icone.getIconHeight();
    }

}
