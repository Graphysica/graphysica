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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;
import org.graphysica.espace2d.Toile;

/**
 * Un angle décrit l'espace angulaire entre deux points à partir d'un sommet.
 *
 * @author Marc-Antoine
 */
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
    private final ObjectProperty<Taille> taille = new SimpleObjectProperty(
            Taille.de("angle"));

    /**
     * Le vecteur représentant l'horizontale, soit l'angle de zéro.
     */
    private static final Vector2D HORIZONTALE = new Vector2D(1, 0);

    private final ObjectProperty<Vector2D> point1
            = new SimpleObjectProperty<>();

    private final ObjectProperty<Vector2D> sommet
            = new SimpleObjectProperty<>();

    private final ObjectProperty<Vector2D> point2
            = new SimpleObjectProperty<>();

    private final DoubleProperty opacite = new SimpleDoubleProperty(0.5);

    public Angle(@NotNull final Point point1, @NotNull final Point sommet,
            @NotNull final Point point2) {
        setCouleur(COULEUR_PAR_DEFAUT);
        setPoint1(point1.getPosition());
        this.point1.bind(point1.positionProperty());
        setSommet(sommet.getPosition());
        this.sommet.bind(sommet.positionProperty());
        setPoint2(point2.getPosition());
        this.point2.bind(point2.positionProperty());
    }

    {
        proprietesActualisation.add(taille);
        proprietesActualisation.add(point1);
        proprietesActualisation.add(sommet);
        proprietesActualisation.add(point2);
        proprietesActualisation.add(opacite);
    }

    @Override
    public void dessiner(@NotNull final Toile toile) {
        final int rayon = taille.getValue().get() * MULTIPLICATEUR_RAYON;
        final Vector2D centreVirtuel = toile.positionVirtuelle(getSommet());
        final GraphicsContext contexteGraphique = toile.getGraphicsContext2D();
        final Color couleurRemplissage = couleurTransparente(getCouleur(),
                opacite.getValue());
        final double angleInitial = getAngleInitial();
        System.out.println("angleInitial = " + angleInitial);
        final double angle = getAngle();
        System.out.println("angle = " + angle);
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

    public final Vector2D getPoint1() {
        return point1.getValue();
    }

    public final void setPoint1(@NotNull final Vector2D point1) {
        this.point1.setValue(point1);
        this.point1.unbind();
    }

    public final Vector2D getSommet() {
        return sommet.getValue();
    }

    public final void setSommet(@NotNull final Vector2D sommet) {
        this.sommet.setValue(sommet);
        this.sommet.unbind();
    }

    public final Vector2D getPoint2() {
        return point2.getValue();
    }

    public final void setPoint2(@NotNull final Vector2D point2) {
        this.point2.setValue(point2);
        this.point2.unbind();
    }

    /**
     * Calcule l'angle entre le vecteur partant du sommet vers le premier point
     * et l'horizontale.
     *
     * @return la valeur de l'angle initial.
     * @see Angle#HORIZONTALE
     */
    public double getAngleInitial() {
        final Vector2D vecteur1 = getPoint1().subtract(getSommet());
        System.out.println("getAngleInitial()::" + Vector2D.angle(vecteur1, HORIZONTALE));
        return FastMath.toDegrees(Vector2D.angle(vecteur1, HORIZONTALE));
    }

    //TODO: Régler le signe de l'angle
    /**
     * Calcule l'angle entre les vecteurs partant du sommet vers les deux
     * points.
     *
     * @return la valeur de l'angle représenté.
     */
    public double getAngle() {
        final Vector2D vecteur1 = getPoint1().subtract(getSommet());
        final Vector2D vecteur2 = getPoint2().subtract(getSommet());
        System.out.println("getAngle()::" + Vector2D.angle(vecteur1, vecteur2));
        return FastMath.toDegrees(Vector2D.angle(vecteur1, vecteur2));
    }

    //TODO: Régler le signe de l'angle
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

}
