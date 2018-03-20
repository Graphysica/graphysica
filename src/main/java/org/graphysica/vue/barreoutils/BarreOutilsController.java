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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Victor Babin
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
    
    @FXML
    private ImageView imgTag;

    @FXML
    private ImageView imgPuzzle;

    @FXML
    private ImageView imgObjets;
    
    private Image icon = null;

    

    private Outil.Buttons currButton = Outil.Buttons.POINTEUR;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        icon = imgObjets.getImage();
        archive.setOnAction((event) -> {
            currButton = Outil.Buttons.ARCHIVE;
        });
        tag.setOnAction((event) -> {
            currButton = Outil.Buttons.TAG;
            imgObjets.setImage(imgTag.getImage());
        });
        curseur.setOnAction((event) -> {
            currButton = Outil.Buttons.POINTEUR;
        });
        curseurPlus.setOnAction((event) -> {
            currButton = Outil.Buttons.SELECTEUR;
        });
        champVectoriel.setOnAction((event) -> {
            currButton = Outil.Buttons.CHAMP_VECTORIEL;
        });
        inspecteur.setOnAction((event) -> {
            currButton = Outil.Buttons.INSPECTEUR;
        });
        puzzle.setOnAction((event) -> {
            currButton = Outil.Buttons.PUZZLE;
            imgObjets.setImage(imgPuzzle.getImage());
        });
    }

}
