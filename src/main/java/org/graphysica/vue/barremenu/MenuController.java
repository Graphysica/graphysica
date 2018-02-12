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
package org.graphysica.vue.barremenu;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * Classe controlleur du FXML Menu
 *
 * @author Victor Babin <vicbab@Graphysica>
 */
public class MenuController implements Initializable {

    @FXML
    private Menu mnuFichier;

    @FXML
    private MenuItem nouveau;

    @FXML
    private MenuItem ouvrir;

    @FXML
    private MenuItem recent;

    @FXML
    private MenuItem sauvegarder;

    @FXML
    private MenuItem sauvegarderSous;

    @FXML
    private MenuItem fermer;

    @FXML
    private Menu mnuEdition;

    @FXML
    private MenuItem annuler;

    @FXML
    private MenuItem refaire;

    @FXML
    private MenuItem copier;

    @FXML
    private MenuItem coller;

    @FXML
    private MenuItem toutSelectionner;

    @FXML
    private MenuItem proprietes;

    @FXML
    private Menu mnuAffichage;

    @FXML
    private CheckMenuItem checkConstruction;

    @FXML
    private CheckMenuItem checkSimulation;

    @FXML
    private CheckMenuItem checkLigneDuTemps;

    @FXML
    private CheckMenuItem checkListeObjets;

    @FXML
    private Menu mnuAide;

    @FXML
    private MenuItem manuel;

    @FXML
    private MenuItem aPropos;

    @FXML
    private MenuItem licence;
    
    
    /**
     * Initialisation du controlleur.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
