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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 *
 * @author Marc-Antoine Ouimet
 */
public final class Grille extends Forme {

    private static final Color COULEUR_PAR_DEFAUT = new Color(0, 0, 0, 0.1);

    /**
     * L'épaisseur du tracé de la grille.
     */
    protected final ObjectProperty<Taille> epaisseur
            = new SimpleObjectProperty<>(Taille.de("grille"));

    private final Set<Ligne> lignes = new HashSet<>();

    public Grille(@NotNull final Toile toile) {
        super();
        setCouleur(COULEUR_PAR_DEFAUT);
        initialiser(toile);
    }
    
    {
        proprietesActualisation.add(epaisseur);
    }

    private void initialiser(@NotNull final Toile toile) {
        final int lignesVerticales = (int) (toile.getWidth()
                / toile.getEspacement().getX());
        final double espacementHorizontal = toile.getEspacement().getX();
        double abscisse = toile.getOrigine().getX();
        while (abscisse < 0) {
            abscisse += espacementHorizontal;
        }
        while (abscisse > espacementHorizontal) {
            abscisse -= espacementHorizontal;
        }
        for (int i = 0; i < lignesVerticales; i++) {
            lignes.add(new DroiteVerticale(toile.abscisseReelle(abscisse)));
            abscisse += espacementHorizontal;
        }
        final int lignesHorizontales = (int) (toile.getHeight()
                / toile.getEspacement().getY());
        final double espacementVertical = toile.getEspacement().getX();
        double ordonnee = toile.getOrigine().getY();
        while (ordonnee < 0) {
            ordonnee += espacementVertical;
        }
        while (ordonnee > espacementVertical) {
            ordonnee -= espacementVertical;
        }
        for (int i = 0; i < lignesHorizontales; i++) {
            lignes.add(new DroiteHorizontale(toile.ordonneeReelle(ordonnee)));
            ordonnee += espacementVertical;
        }
        for (final Ligne ligne : lignes) {
            ligne.setCouleur(COULEUR_PAR_DEFAUT);
        }
    }

    @Override
    public void dessiner(@NotNull final Toile toile) {
        //retirerLignesNonVisibles(toile);
        //TODO: Ajouter des lignes...
        for (final Ligne ligne : lignes) {
            ligne.dessiner(toile);
        }
    }

    private void retirerLignesNonVisibles(@NotNull final Toile toile) {
        final List<Ligne> lignesARetirer = new ArrayList<>();
        for (final Ligne ligne : lignes) {
            if (!estVisible(ligne, toile)) {
                lignesARetirer.add(ligne);
            }
        }
        lignes.removeAll(lignesARetirer);
    }

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

}
