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
package org.graphysica.vue.barreoutils;

import com.sun.istack.internal.NotNull;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.construction.Element;
import org.graphysica.construction.GestionnaireOutils;
import org.graphysica.construction.commande.Commande;
import org.graphysica.construction.commande.CreerElement;
import org.graphysica.construction.mathematiques.Point;
import org.graphysica.construction.mathematiques.PointConcret;
import org.graphysica.espace2d.Espace;
import org.graphysica.espace2d.position.Position;
import org.graphysica.espace2d.position.PositionReelle;

/**
 *
 * @author Victor Babin <vicbab@Graphysica>
 */
public class OutilPoint extends Outil {

    public OutilPoint(boolean aProchaineEtape, 
           GestionnaireOutils gestionnaireOutils) {
        super(false, gestionnaireOutils);
    }
    
    @Override
    public void prochaineEtape() {
        
    }

    @Override
    public void derniereEtape() {
        PositionReelle position = gestionnaireOutils.getConstruction()
                .getGestionnaireSelections().getPositionReelle();
        
        gestionnaireOutils.getConstruction().executerCommande(
                new CreerElement(new PointConcret(position), 
                        gestionnaireOutils.getConstruction()));
    }
    
    
    
}
