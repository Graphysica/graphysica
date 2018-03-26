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
package org.graphysica.simulation;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
<<<<<<< HEAD
 * @author Victor Babin
=======
 * @author Victor Babin <vicbab@Graphysica>
>>>>>>> origin/master
 */
public class ChronometreController implements Initializable {

    @FXML
    private Label etiquetteChrono;

    @FXML
    private Button btnRembobiner;

    @FXML
    private ImageView imgRembobiner;

    @FXML
    private Button btnPlayPause;

    @FXML
    private ImageView imgPlayPause;

    @FXML
    private Button btnAvancer;

    @FXML
    private ImageView imgAvancer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
