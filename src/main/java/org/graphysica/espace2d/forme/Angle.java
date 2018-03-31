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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;
import org.graphysica.espace2d.position.Position;
import org.graphysica.espace2d.Repere;

/**
 * Un angle décrit l'espace angulaire entre deux points à partir d'un sommet.
 *
 * @author Marc-Antoine Ouimet
 */
@SuppressWarnings("unchecked")
public class Angle extends Forme {

    /**
     * La couleur par défaut d'un angle.
     */
    static final Color COULEUR_PAR_DEFAUT = Color.GREEN;

    /**
     * Le multiplicateur de conversion de la taille du secteur à son rayon.
     */
    private static final int MULTIPLICATEUR_RAYON = 4;

    /**
     * La taille du secteur représentant l'angle. Le rayon du tracé du secteur
     * correspond à {@code MULTIPLICATEUR_RAYON} fois cette taille.
     */
    private final Taille taille = Taille.de("angle");

    /**
     * Le vecteur représentant l'horizontale, soit l'angle de zéro.
     */
    private static final Vector2D HORIZONTALE = new Vector2D(1, 0);

    /**
     * Le premier point extrême de l'angle.
     */
    private final ObjectProperty<Position> point1
            = new SimpleObjectProperty<>();

    /**
     * Le somme de l'angle.
     */
    private final ObjectProperty<Position> sommet
            = new SimpleObjectProperty<>();

    /**
     * Le deuxième point extrême de l'angle.
     */
    private final ObjectProperty<Position> point2
            = new SimpleObjectProperty<>();

    /**
     * L'opacité de la couleur de remplissage de l'angle.
     */
    private final DoubleProperty opacite = new SimpleDoubleProperty(0.5);

    /**
     * Construit un angle entre deux points à partir d'un sommet.
     *
     * @param point1 le premier point délimitant l'angle.
     * @param sommet le sommet, qui est à l'origine du secteur.
     * @param point2 le deuxième point délimitant l'angle.
     */
    public Angle(@NotNull final Position point1, @NotNull final Position sommet,
            @NotNull final Position point2) {
        setPoint1(point1);
        setSommet(sommet);
        setPoint2(point2);
    }

    {
        proprietesActualisation.add(taille);
        proprietesActualisation.add(point1);
        proprietesActualisation.add(sommet);
        proprietesActualisation.add(point2);
        proprietesActualisation.add(opacite);
    }

    /**
     * Dessine l'angle en tant que secteur dont l'ouverture est comprise entre
     * 0º et 180º.
     *
     * @param toile la toile sur laquelle dessiner l'angle.
     * @param repere le repère de l'espace.
     */
    @Override
    public void dessiner(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        if (isDefini()) {
            // TODO: Dessiner la surbrillance si l'angle est en surbrillance
            // Adapter l'angle à la convention du tracé
            double angle = -getAngle(repere);
            double angleInitial = -getAngleInitial(repere);
            // Restreindre l'angle du tracé à [0º, 180º]
            if (angle > 180) {
                angle -= 360;
                angleInitial += 360;
            }
            final int rayon = taille.getValue() * MULTIPLICATEUR_RAYON;
            final Vector2D centreVirtuel = getSommet().virtuelle(repere);
            final GraphicsContext contexteGraphique = toile
                    .getGraphicsContext2D();
            final Color couleurRemplissage = couleurTransparente(getCouleur(),
                    opacite.getValue());
            contexteGraphique.setFill(couleurRemplissage);
            contexteGraphique.fillArc(
                    centreVirtuel.getX() - rayon,
                    centreVirtuel.getY() - rayon,
                    2 * rayon, 2 * rayon,
                    angleInitial, angle,
                    ArcType.ROUND);
            contexteGraphique.setStroke(getCouleur());
            contexteGraphique.setLineWidth(1);
            contexteGraphique.strokeArc(
                    centreVirtuel.getX() - rayon,
                    centreVirtuel.getY() - rayon,
                    2 * rayon, 2 * rayon,
                    angleInitial, angle,
                    ArcType.ROUND);
        }
    }

    @Override
    public void dessinerSurbrillance(@NotNull final Canvas toile,
            @NotNull final Repere repere) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public final Position getPoint1() {
        return point1.getValue();
    }

    public final void setPoint1(@NotNull final Position point1) {
        this.point1.setValue(point1);
    }

    public final Position getSommet() {
        return sommet.getValue();
    }

    public final void setSommet(@NotNull final Position sommet) {
        this.sommet.setValue(sommet);
    }

    public final Position getPoint2() {
        return point2.getValue();
    }

    public final void setPoint2(@NotNull final Position point2) {
        this.point2.setValue(point2);
    }

    /**
     * Calcule l'angle entre le vecteur partant du sommet vers le premier point
     * et l'horizontale.
     *
     * @param repere le repère d'espace de cet angle.
     * @return la valeur de l'angle initial.
     * @see Angle#HORIZONTALE
     * @throws MathArithmeticException si le sommet est confondu à l'un ou
     * l'autre des points de l'angle.
     * @see Angle#isDefini()
     */
    public double getAngleInitial(@NotNull final Repere repere) 
            throws MathArithmeticException {
        final Vector2D vecteur1 = getPoint1().reelle(repere).subtract(
                getSommet().reelle(repere));
        return FastMath.toDegrees(angle(vecteur1, HORIZONTALE));
    }

    /**
     * Calcule l'angle entre les vecteurs partant du sommet vers les deux
     * points.
     *
     * @param repere le repère d'espace de cet angle.
     * @return la valeur de l'angle représenté.
     * @throws MathArithmeticException si le sommet est confondu à l'un ou
     * l'autre des points de l'angle.
     * @see Angle#isDefini()
     */
    public double getAngle(@NotNull final Repere repere) 
            throws MathArithmeticException {
        final Vector2D vecteur1 = getPoint1().reelle(repere).subtract(
                getSommet().reelle(repere));
        final Vector2D vecteur2 = getPoint2().reelle(repere).subtract(
                getSommet().reelle(repere));
        return -FastMath.toDegrees(angle(vecteur1, vecteur2));
    }

    /**
     * Récupère l'angle trigonométrique entre deux vecteurs, en radians.
     *
     * @param vecteur1 le premier vecteur.
     * @param vecteur2 le deuxième vecteur.
     * @return l'angle entre les vecteurs en radians, partant de
     * {@code vecteur1} vers {@code vecteur2}.
     */
    private static double angle(@NotNull final Vector2D vecteur1,
            @NotNull final Vector2D vecteur2) {
        return FastMath.atan2(vecteur2.getY(), vecteur2.getX())
                - FastMath.atan2(vecteur1.getY(), vecteur1.getX());
    }

    /**
     * Récupère une couleur de même teinte et d'opacité définie.
     *
     * @param couleur la teinte à conserver.
     * @param opacite l'opacité de la nouvelle couleur.
     * @return la couleur de même teinte et d'opacité définie.
     */
    private static Color couleurTransparente(@NotNull final Color couleur,
            final double opacite) {
        return new Color(couleur.getRed(), couleur.getGreen(),
                couleur.getBlue(), opacite);
    }

    /**
     * Détermine si l'angle est défini, c'est-à-dire si les points sont
     * distincts du sommet.
     *
     * @return si l'angle est défini.
     */
    public boolean isDefini() {
        return !(point1.equals(sommet) || point2.equals(sommet));
    }

    @Override
    public double distance(@NotNull final Position curseur, 
            @NotNull final Repere repere) {
        // TODO: Déterminer la distance entre le curseur et un secteur
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
