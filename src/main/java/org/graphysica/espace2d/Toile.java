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

import org.graphysica.espace2d.forme.OrdreRendu;
import org.graphysica.espace2d.forme.Grille;
import org.graphysica.espace2d.forme.Forme;
import com.sun.istack.internal.NotNull;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.forme.Axe;
import org.slf4j.LoggerFactory;

/**
 * Une toile permettant d'afficher un ensemble de formes.
 *
 * @author Marc-Antoine Ouimet
 */
public class Toile extends Canvas implements Actualisable {

    /**
     * L'utilitaire d'enregistrement de trace d'exécution.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(
            Toile.class);

    /**
     * La puissance de graduation de la toile.
     */
    public static final int PUISSANCE = 5;

    /**
     * La liste observable des formes dessinées sur la toile.
     */
    private final ObservableList<Forme> formes
            = FXCollections.observableArrayList();

    /**
     * L'ordre de rendu des formes sur la toile.
     */
    private final OrdreRendu ordreRendu = new OrdreRendu();

    /**
     * L'origine virtuelle de l'espace exprimée en pixels selon l'origine de
     * l'écran. Par défaut, l'origine de l'espace est au centre du panneau.
     */
    protected final ObjectProperty<Vector2D> origine
            = new SimpleObjectProperty<>(
                    new Vector2D(getWidth() / 2, getHeight() / 2));

    /**
     * L'échelle de l'espace exprimée en pixels par mètre.
     */
    protected final ObjectProperty<Vector2D> echelle
            = new SimpleObjectProperty<>(new Vector2D(100, 100));

    /**
     * L'événement d'actualisation des formes en cas d'invalidation de leurs
     * propriétés.
     */
    protected final InvalidationListener evenementActualisation
            = (@NotNull final Observable observable) -> {
                actualiser();
            };

    /**
     * La grille secondaire de la toile. Représente les graduations plus
     * précises de l'espace.
     */
    private final Grille grilleSecondaire = new Grille(
            new Vector2D(getEchelle().getX() / 4, getEchelle().getY() / 4),
            Color.gray(0.9));

    /**
     * La grille principale de la toile. Représente les graduations plus
     * grossières de l'espace.
     */
    private final Grille grillePrincipale = new Grille(
            new Vector2D(getEchelle().getX(), getEchelle().getY()),
            Color.gray(0.5));

    public Toile(final double largeur, final double hauteur) {
        super(largeur, hauteur);
    }

    {
        formes.addListener(evenementActualisation);
        // Traiter le déplacement de l'espace
        origine.addListener(evenementActualisation);
        // Traiter la redimension de l'espace
        echelle.addListener(evenementActualisation);
        ajouter(grilleSecondaire, grillePrincipale);
        ajouter(Axe.nouvelAxe(Axe.Sens.HORIZONTAL,
                grillePrincipale.getEspacement().getY()),
                Axe.nouvelAxe(Axe.Sens.VERTICAL,
                        grillePrincipale.getEspacement().getX()));
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
        return -ordonneeReelle * getEchelle().getY() + getOrigine().getY();
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
        return -(getOrigine().getX() - abscisseVirtuelle) / getEchelle().getX();
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
        return (getOrigine().getY() - ordonneeVirtuelle) / getEchelle().getY();
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

    /**
     * Actualise l'affichage de cette toile en redessinant chacune de ses
     * formes. Si la classe d'une forme ne fait pas partie des définitions de
     * l'ordre de rendu, elle n'est pas dessinée.
     *
     * @see Toile#ordreRendu
     * @see OrdreRendu
     */
    @Override
    public void actualiser() {
        effacerAffichage();
        int formesAffichables = 0;
        for (final Class classe : ordreRendu) {
            for (final Forme forme : formes) {
                if (classe.isInstance(forme)) {
                    formesAffichables++;
                    if (forme.isAffichee()) {
                        forme.dessiner(this);
                    }
                }
            }
        }
        if (formesAffichables != formes.size()) {
            LOGGER.warn(String.format("Des classes de formes ne sont pas "
                    + "comprises dans l'ordre de rendu de la toile. "
                    + "%d formes sur %d sont affichables.", formesAffichables,
                    formes.size()));
        }
    }

    /**
     * Réinitialise l'image rendue par cette toile.
     */
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
        this.formes.add(forme);
        forme.getProprietesActualisation().forEach((propriete) -> {
            propriete.addListener(evenementActualisation);
        });
    }

    /**
     * Ajoute des formes à la toile et lie leurs propriétés à l'actualisation de
     * la toile.
     *
     * @param formes les formes à ajouter à la toile.
     */
    public void ajouter(@NotNull final Forme... formes) {
        for (final Forme forme : formes) {
            ajouter(forme);
        }
    }

    /**
     * Retire une forme de la toile et délie ses propriétés de l'actualisation
     * de la toile.
     *
     * @param forme la forme à retirer de la toile.
     */
    public void retirer(@NotNull final Forme forme) {
        final boolean retiree = formes.remove(forme);
        if (retiree) {
            forme.getProprietesActualisation().forEach((propriete) -> {
                propriete.removeListener(evenementActualisation);
            });
        }
    }

    /**
     * Retire les formes de la toile et délie leurs propriétés de
     * l'actualisation de la toile.
     *
     * @param formes les formes à retirer de la toile.
     */
    public void retirer(@NotNull final Forme... formes) {
        for (final Forme forme : formes) {
            retirer(forme);
        }
    }

    /**
     * Calcule la position virtuelle des graduations horizontales de la toile
     * qui correspond à des valeurs d'ordonnées de l'espace.
     *
     * @param espacementMinimal l'espacement virtuel minimal entre chaque
     * graduation.
     * @return l'ensemble des valeurs d'abscisse des graduations horizontales de
     * la toile.
     */
    public double[] graduationsHorizontales(final double espacementMinimal) {
        final double espacementMinimalReel = espacementMinimal / getEchelle()
                .getY();
        final int exposant = (int) (Math.log(espacementMinimalReel)
                / Math.log(PUISSANCE));
        final double espacementReel = Math.pow(PUISSANCE, exposant);
        final double espacementVirtuel = espacementReel * getEchelle().getY();
        double ordonneeAncrage = getOrigine().getY() % espacementVirtuel;
        ordonneeAncrage = ordonneeAncrage > 0
                ? ordonneeAncrage - espacementVirtuel : ordonneeAncrage;
        final double[] graduationsHorizontales = new double[(int) (getHeight()
                / espacementVirtuel) + 2];
        double y = ordonneeAncrage;
        for (int i = 0; i < graduationsHorizontales.length; i++) {
            graduationsHorizontales[i] = y;
            y += espacementVirtuel;
        }
        return graduationsHorizontales;
    }

    /**
     * Calcule la position virtuelle des graduations verticales de la toile qui
     * correspondent à des valeurs d'abscisses de l'espace.
     *
     * @param espacementMinimal l'espacement virtuel minimal entre chaque
     * graduation.
     * @return l'ensemble des valeurs d'ordonnée des graduations verticales de
     * la toile.
     */
    public double[] graduationsVerticales(final double espacementMinimal) {
        final double espacementMinimalReel = espacementMinimal / getEchelle()
                .getX();
        final int exposant = (int) (Math.log(espacementMinimalReel)
                / Math.log(PUISSANCE));
        final double espacementReel = Math.pow(PUISSANCE, exposant);
        final double espacementVirtuel = espacementReel * getEchelle().getX();
        double abscisseAncrage = getOrigine().getX() % espacementVirtuel;
        abscisseAncrage = abscisseAncrage > 0
                ? abscisseAncrage - espacementVirtuel : abscisseAncrage;
        final double[] graduationsVerticales = new double[(int) (getWidth()
                / espacementVirtuel) + 2];
        double x = abscisseAncrage;
        for (int i = 0; i < graduationsVerticales.length; i++) {
            graduationsVerticales[i] = x;
            x += espacementVirtuel;
        }
        return graduationsVerticales;
    }

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

}
