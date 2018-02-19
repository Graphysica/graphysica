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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 *
 * @author Marc-Antoine Ouimet
 */
public class Droite extends Forme {

    /**
     * La couleur par défaut d'une droite.
     */
    static final Color COULEUR_PAR_DEFAUT = Color.BLACK;

    /**
     * La position réelle du premier point dans la droite.
     */
    private final ObjectProperty<Vector2D> point1
            = new SimpleObjectProperty<>();

    /**
     * La position réelle du deuxième point dans la droite.
     */
    private final ObjectProperty<Vector2D> point2
            = new SimpleObjectProperty<>();

    /**
     * La position virtuelle du point 1.
     *
     * @see Droite#point1
     */
    private Vector2D point1Virtuel;

    /**
     * La position virtuelle du point 2.
     *
     * @see Droite#point2
     */
    private Vector2D point2Virtuel;

    /**
     * La position d'origine de la trace de la droite.
     */
    private Vector2D origineTrace;

    /**
     * La position de l'arrivée de la trace de la droite.
     */
    private Vector2D arriveeTrace;

    /**
     * L'écart entre les abscisses des points virtuels.
     *
     * @see Droite#point1Virtuel
     * @see Droite#point2Virtuel
     */
    private double variationAbscisse;

    /**
     * L'écart entre les ordonnées des points virtuels.
     *
     * @see Droite#point1Virtuel
     * @see Droite#point2Virtuel
     */
    private double variationOrdonnee;

    private final ObjectProperty<Taille> epaisseur
            = new SimpleObjectProperty<>(Taille.de("ligne"));

    public Droite(@NotNull final Point point1, @NotNull final Point point2) {
        this.point1.setValue(point1.getPosition());
        this.point1.bind(point1.positionProperty());
        this.point2.setValue(point2.getPosition());
        this.point2.bind(point2.positionProperty());
    }

    @Override
    public void dessiner(@NotNull final Toile toile) {

    }

    private void dessinerContinue(
            @NotNull final GraphicsContext contexteGraphique) {
        contexteGraphique.setStroke(COULEUR_PAR_DEFAUT);
        contexteGraphique.setLineWidth(getEpaisseur());
        contexteGraphique.strokeLine(origineTrace.getX(), origineTrace.getY(),
                arriveeTrace.getX(), arriveeTrace.getY());
    }

    public Vector2D getPoint1() {
        return point1.getValue();
    }

    public Vector2D getPoint2() {
        return point2.getValue();
    }

    private int getEpaisseur() {
        return epaisseur.getValue().getTaille();
    }

    //https://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm
    private static class AlgorithmeCohenSutherland {

        private static final int INTERIEUR = 0b0000;
        private static final int GAUCHE = 0b0001;
        private static final int DROITE = 0b0010;
        private static final int BAS = 0b0100;
        private static final int HAUT = 0b1000;

        /**
         * Détermine le code d'un point virtuel selon le système d'axes de la
         * toile.
         *
         * @param toile la toile définissant les contraintes du contexte
         * graphique.
         * @param point le point virtuel dont on détermine le code.
         * @return le code binaire du point dans l'espace de la toile selon
         * l'algorithme de Cohen-Sutherland.
         */
        private static int code(@NotNull final Vector2D point,
                @NotNull final Toile toile) {
            int code = INTERIEUR;
            if (point.getX() < 0) {
                code |= GAUCHE;
            } else if (point.getX() > toile.getWidth()) {
                code |= DROITE;
            } else if (point.getY() < 0) {
                code |= HAUT;
            } else if (point.getY() > toile.getHeight()) {
                code |= BAS;
            }
            return code;
        }

        //https://stackoverflow.com/questions/8151435/integer-to-binary-array
        private static boolean[] code(int code) {
            final boolean[] codeBooleen = new boolean[4];
            for (int i = codeBooleen.length - 1; i >= 0; i--) {
                codeBooleen[i] = (code & (1 << i)) != 0;
            }
            return codeBooleen;
        }

        public static boolean dansGauche(@NotNull final Vector2D point,
                @NotNull final Toile toile) {
            return code(code(point, toile))[3];
        }

        public static boolean dansDroite(@NotNull final Vector2D point,
                @NotNull final Toile toile) {
            return code(code(point, toile))[2];
        }

        public static boolean dansBas(@NotNull final Vector2D point,
                @NotNull final Toile toile) {
            return code(code(point, toile))[1];
        }

        public static boolean dansHaut(@NotNull final Vector2D point,
                @NotNull final Toile toile) {
            return code(code(point, toile))[0];
        }

    }

}
