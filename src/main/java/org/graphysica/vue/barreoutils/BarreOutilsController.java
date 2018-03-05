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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

/**
 * FXML Controller class
 *
 * @author Victor Babin <vicbab@Graphysica>
 */
public class BarreOutilsController implements Initializable {

    @FXML
    private Button curseur;

    @FXML
    private MenuButton mbObjets;

    @FXML
    private MenuItem tag;

    @FXML
    private MenuItem puzzle;

    @FXML
    private Button curseurPlus;

    @FXML
    private Button archive;

    @FXML
    private Button champVectoriel;

    @FXML
    private Button inspecteur;

    public enum Buttons {
        POINTEUR, TAG, PUZZLE, SELECTEUR, ARCHIVE, CHAMP_VECTORIEL, INSPECTEUR;
        //FIXME mauvaise façon de gérer la sélection
    }

    private Buttons currButton = Buttons.POINTEUR;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        archive.setOnAction((event) -> {
           currButton = Buttons.ARCHIVE; 
        });
        tag.setOnAction((event) -> {
            currButton = Buttons.TAG;
        });
        curseur.setOnAction((event) -> {
           currButton = Buttons.POINTEUR; 
        });
        curseurPlus.setOnAction((event) -> {
            currButton = Buttons.SELECTEUR;
        });
        champVectoriel.setOnAction((event) -> {
           currButton = Buttons.CHAMP_VECTORIEL; 
        });
        inspecteur.setOnAction((event) -> {
            currButton = Buttons.INSPECTEUR;
        });
        puzzle.setOnAction((event) -> {
           currButton = Buttons.PUZZLE; 
        });
    }

}
