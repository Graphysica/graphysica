package org.graphysica;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.espace2d.Espace;
import org.graphysica.espace2d.Point;
import org.graphysica.espace2d.SegmentDroite;
import org.graphysica.vue.barreoutils.Outil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);
    private AnchorPane panneauPrincipal;
    private Espace espace;
    private MenuBar menubar;
    private VBox vertical;
    private VBox chronometre;
    private SplitPane splitPane;
    private ToolBar toolBar;
    private TabPane information;
    private MenuBar menuBar;
    
    
    @Override
    public void start(Stage stage) throws Exception {
        panneauPrincipal = new AnchorPane();
        initialiserPanneau();
        
        Scene scene = new Scene(panneauPrincipal);
        LOGGER.debug("");
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setTitle("Graphysica");
        stage.setScene(scene);
        stage.show();
        
        
//        showDialog();
    }

    public void showDialog() {
        Dialog dialog = new TextInputDialog();
        dialog.setHeaderText("Nouveau fichier");
        dialog.setContentText("Titre: ");
        dialog.setTitle("Nouveau fichier");
        dialog.showAndWait().get();
    }

    public void initialiserPanneau() throws IOException {
        panneauPrincipal.setPrefSize(900, 700);
        
        espace = new Espace(869,517);
        menuBar = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
        vertical = new VBox();
        information = FXMLLoader.load(getClass().getResource("/fxml/Information.fxml"));
        splitPane = new SplitPane(information, espace);
        chronometre = FXMLLoader.load(getClass().getResource("/fxml/Chronometre.fxml"));
//        toolBar = FXMLLoader.load(getClass().getResource("/fxml/BarreOutils.fxml"));
        toolBar = new ToolBar(new Outil("sol", ""), new Outil("perpendiculaire", ""));
        
        
        initialiserDimensions();
        ajouterObjetsEspace();
        ajouterComposantes();
    }

    private void ajouterComposantes() {
        panneauPrincipal.getChildren().add(vertical);
        vertical.getChildren().add(menuBar);
        vertical.getChildren().add(toolBar);
        vertical.getChildren().add(splitPane);
        vertical.getChildren().add(chronometre);
    }

    private void ajouterObjetsEspace() {
        espace.ajouter(new Point(Vector2D.ZERO));
        espace.ajouter(new Point(new Vector2D(0.2, 0.5)));
        espace.ajouter(new SegmentDroite(new Point(Vector2D.ZERO), new Point(new Vector2D(0.2, 0.5))));
    }

    private void initialiserDimensions() {
        menuBar.prefWidthProperty().bind(panneauPrincipal.widthProperty());
        toolBar.prefWidthProperty().bind(panneauPrincipal.widthProperty());
        vertical.prefWidthProperty().bind(panneauPrincipal.widthProperty());
        information.setPrefSize(300, panneauPrincipal.getPrefHeight() - chronometre.getPrefHeight());
        espace.setPrefSize(panneauPrincipal.getPrefWidth() - information.getPrefWidth(), panneauPrincipal.getPrefHeight() - chronometre.getPrefHeight());
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
