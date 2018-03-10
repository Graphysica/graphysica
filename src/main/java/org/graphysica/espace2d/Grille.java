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

import org.graphysica.espace2d.forme.DroiteVerticale;
import org.graphysica.espace2d.forme.Ligne;
import org.graphysica.espace2d.forme.Taille;
import org.graphysica.espace2d.forme.DroiteHorizontale;
import org.graphysica.espace2d.forme.Forme;
import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * //TODO: Retravailler... Peut-être avec des manipulations bitwise.
 * @author Marc-Antoine Ouimet
 */
public final class Grille extends Forme {

    private static final Color COULEUR_PAR_DEFAUT = new Color(0, 0, 0, 0.3);
    
    /**
     * L'espacement minimum des graduations de la grille exprimée en pixels.
     */
    private final ObjectProperty<Vector2D> espacement
            = new SimpleObjectProperty<>(new Vector2D(50, 50));

    /**
     * L'épaisseur du tracé de la grille.
     */
    private final ObjectProperty<Taille> epaisseur
            = new SimpleObjectProperty<>(Taille.de("grille"));

    /**
     * La liste ordonnée des droites horizontales de la grille. L'ordre des
     * droites maintenu par la grille correspond à leur ordre d'affichage à
     * partir de la gauche. Ce faisant, le premier élément de la liste sera le
     * plus à gauche de la grille dans la toile et le dernier élément de la
     * liste sera le plus à droite de la grille dans la toile.
     */
    private final ArrayList<DroiteHorizontale> droitesHorizontales
            = new ArrayList<>();

    /**
     * La liste ordonnée des droites verticales de la grille. L'ordre des
     * droites maintenu par la grille correspond à leur ordre d'affichage à
     * partir du haut. Ce faisant, le premier élément de la liste sera le
     * plus en haut de la grille dans la toile et le dernier élément de la
     * liste sera le plus en bas de la grille dans la toile.
     */
    private final ArrayList<DroiteVerticale> droitesVerticales
            = new ArrayList<>();

    public Grille(@NotNull final Toile toile) {
        setCouleur(COULEUR_PAR_DEFAUT);
        initialiser(toile);
    }

    {
        proprietesActualisation.add(epaisseur);
    }

    private void initialiser(@NotNull final Toile toile) {
        final int lignesVerticales = (int) (toile.getWidth()
                / getEspacement().getX());
        final double espacementHorizontal = getEspacement().getX();
        double abscisse = toile.getOrigine().getX();
        while (abscisse < 0) {
            abscisse += espacementHorizontal;
        }
        while (abscisse > espacementHorizontal) {
            abscisse -= espacementHorizontal;
        }
        for (int i = 0; i < lignesVerticales; i++) {
            droitesVerticales.add(new DroiteVerticale(
                    toile.abscisseReelle(abscisse), COULEUR_PAR_DEFAUT));
            abscisse += espacementHorizontal;
        }
        final int lignesHorizontales = (int) (toile.getHeight()
                / getEspacement().getY());
        final double espacementVertical = getEspacement().getX();
        double ordonnee = toile.getOrigine().getY();
        while (ordonnee < 0) {
            ordonnee += espacementVertical;
        }
        while (ordonnee > espacementVertical) {
            ordonnee -= espacementVertical;
        }
        for (int i = 0; i < lignesHorizontales; i++) {
            droitesHorizontales.add(new DroiteHorizontale(
                    toile.ordonneeReelle(ordonnee), COULEUR_PAR_DEFAUT));
            ordonnee += espacementVertical;
        }
    }

    /**
     * Calcule le nombre de lignes horizontales à afficher par mètre.
     *
     * @param toile la toile affichant la grille.
     * @return le nombre de lignes horizontales à afficher par mètre.
     */
    private double lignesHorizontalesParMetre(@NotNull final Toile toile) {
        return toile.getEchelle().getX() / getEspacement().getX();
    }
    
    private double metresParLigneHorizontale(@NotNull final Toile toile) {
        return 1 / lignesHorizontalesParMetre(toile);
    }

    /**
     * Calcule le nombre de lignes horizontales à afficher sur la toile.
     *
     * @param toile la toile affichant la grille.
     * @return le nombre de lignes horizontales à afficher sur la toile.
     * @deprecated refaire avec première et dernière abscisse
     */
    private int lignesHorizontales(@NotNull final Toile toile) {
        final double largeur = toile.abscisseReelle(toile.getWidth())
                - toile.abscisseReelle(0);
        return (int) (Math.abs(largeur * lignesHorizontalesParMetre(toile)));
    }

    /**
     * Calcule la première abscisse de la grille à partir de la gauche.
     *
     * @param toile la toile affichant la grille.
     * @return l'abscisse de la droite verticale la plus à gauche dans la toile.
     */
    private double premiereAbscisse(@NotNull final Toile toile) {
        final double abscisseOrigine = toile.getOrigine().getX();
        final int nombreDroites = (int) (abscisseOrigine
                / toile.getEchelle().getX());
        return abscisseOrigine - nombreDroites * toile.getEchelle().getX();
    }

    private double derniereAbscisse(@NotNull final Toile toile) {
        final double abscisseOrigine = toile.getOrigine().getX();
        final int nombreDroites = (int) ((abscisseOrigine - toile.getWidth())
                / toile.getEchelle().getX());
        return abscisseOrigine + nombreDroites * toile.getEchelle().getX();
    }

    /**
     * Calcule le nombre de lignes verticales à afficher par mètre.
     *
     * @param toile la toile affichant la grille.
     * @return le nombre de lignes verticales à afficher par mètre.
     */
    private double lignesVerticalesParMetre(@NotNull final Toile toile) {
        return toile.getEchelle().getY() / getEspacement().getY();
    }
    
    private double metreParLigneVerticale(@NotNull final Toile toile) {
        return 1 / lignesVerticalesParMetre(toile);
    }

    /**
     * Calcule le nombre de lignes verticales à afficher sur la toile.
     *
     * @param toile la toile affichant la grille.
     * @return le nombre de lignes verticales à afficher sur la toile.
     * @deprecated refaire avec première et dernière ordonnée
     */
    private int lignesVerticales(@NotNull final Toile toile) {
        final double hauteur = toile.ordonneeReelle(toile.getHeight())
                - toile.ordonneeReelle(0);
        return (int) (Math.abs(hauteur * lignesVerticalesParMetre(toile)));
    }

    /**
     * Calcule la première ordonnée de la grille à partir du haut.
     *
     * @param toile la toile affichnt la grille.
     * @return l'ordonnée de la droite horizontale la plus en haut dans la
     * toile.
     */
    private double premiereOrdonnee(@NotNull final Toile toile) {
        final double ordonneeOrigine = toile.getOrigine().getY(); // px
        System.out.println("ordonneeOrigine = " + ordonneeOrigine);
        final double ecartHaut = ordonneeOrigine 
                / toile.getEchelle().getY(); // m
        System.out.println("ecartHaut = " + ecartHaut);
        int lignesParMetre = (int) (lignesHorizontalesParMetre(toile));
        System.out.println("lignesParMetre = " + lignesParMetre);
        final int lignesEcart = (int) (ecartHaut * lignesParMetre); // lignes
        System.out.println("lignesEcart = " + lignesEcart);
        return ordonneeOrigine - lignesEcart * metresParLigneHorizontale(toile);
    }

    private double derniereOrdonnee(@NotNull final Toile toile) {
        final double ordonneeOrigine = toile.getOrigine().getY();
        final int nombreDroites = (int) ((ordonneeOrigine - toile.getHeight())
                / toile.getEchelle().getY());
        return ordonneeOrigine + nombreDroites * toile.getEchelle().getY();
    }

    @Override
    public void dessiner(@NotNull final Toile toile) {
        final Vector2D origine = toile.getOrigine();
        
    }

//    private void retirerLignesNonVisibles(@NotNull final Toile toile) {
//        final List<Ligne> lignesARetirer = new ArrayList<>();
//        for (final Ligne ligne : lignes) {
//            System.out.println("estVisible = " + estVisible(ligne, toile));
//            if (!estVisible(ligne, toile)) {
//                lignesARetirer.add(ligne);
//            }
//        }
//        lignes.removeAll(lignesARetirer);
//    }
    private boolean estVisible(@NotNull final Ligne ligne,
            @NotNull final Toile toile) {
        if (ligne instanceof DroiteHorizontale) {
            final DroiteHorizontale droite = (DroiteHorizontale) ligne;
            return droite.isVisible(toile);
        } else {
            final DroiteVerticale droite = (DroiteVerticale) ligne;
            return droite.isVisible(toile);
        }
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
