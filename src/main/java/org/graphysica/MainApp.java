package org.graphysica;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.construction.Construction;
import org.graphysica.construction.commande.CreerElement;
import org.graphysica.espace2d.Espace;
import org.graphysica.construction.mathematiques.*;
import org.graphysica.espace2d.forme.Etiquette;
import org.graphysica.espace2d.position.PositionReelle;
import org.graphysica.espace2d.position.PositionVirtuelle;
import org.graphysica.vue.barreoutils.BoutonOutil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);
    private AnchorPane panneauPrincipal;
    private Construction construction = new Construction();
    private final Espace espace = construction.getEspace();
//    private Espace espace;
    private MenuBar menubar;
    private VBox vertical;
    private VBox chronometre;
    private HBox horizontal;
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
        espace.setWidth(800);
        espace.setHeight(600);
//        espace = new Espace(800, 600);
        menuBar = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
        vertical = new VBox();
        horizontal = new HBox();
        chronometre = FXMLLoader.load(getClass().getResource("/fxml/Chronometre.fxml"));
//        toolBar = FXMLLoader.load(getClass().getResource("/fxml/BarreOutils.fxml"));
        toolBar = new ToolBar(new BoutonOutil("point", "point"),
                new BoutonOutil("selection", STYLESHEET_MODENA),
                new BoutonOutil("segment", STYLESHEET_MODENA),
                new BoutonOutil("droite", STYLESHEET_MODENA),
                new BoutonOutil("perpendiculaire", STYLESHEET_MODENA));
        information = FXMLLoader.load(getClass().getResource("/fxml/Information.fxml"));

        initialiserDimensions();
        ajouterObjetsEspace();
        ajouterComposantes();
    }

    private void ajouterComposantes() {
        panneauPrincipal.getChildren().add(vertical);
        vertical.getChildren().add(menuBar);
        vertical.getChildren().add(toolBar);
        vertical.getChildren().add(horizontal);
        horizontal.getChildren().add(information);
        horizontal.getChildren().add(espace);
        vertical.getChildren().add(chronometre);
    }

    private void ajouterObjetsEspace() {
        final Point point1 = new PointConcret(
                new PositionReelle(new Vector2D(2, 2)));
        final Point point2 = new PointConcret(
                new PositionReelle(new Vector2D(1, 0)));
        final Point point3 = new PointConcret(
                new PositionReelle(new Vector2D(1, 1)));
        construction.executerCommande(new CreerElement(point1, construction));
        construction.executerCommande(new CreerElement(point2, construction));
        construction.executerCommande(new CreerElement(point3, construction));
        
        construction.executerCommande(new CreerElement(
                new PointConcret(new PositionReelle(new Vector2D(0, 2))),
                construction));
        construction.executerCommande(new CreerElement(
                new PointConcret(new PositionReelle(new Vector2D(1, 2))),
                construction));
        
        construction.executerCommande(new CreerElement(
                new SegmentDroite(point1, point2), construction));
        construction.executerCommande(new CreerElement(
                new SegmentDroite(point2, point3), construction));
        construction.executerCommande(new CreerElement(
                new SegmentDroite(point3, point1), construction));
        
        espace.getFormes().add(new Etiquette("\\text{Test}",
                new SimpleObjectProperty<>(new PositionVirtuelle(
                        new Vector2D(100, 100)))));
    }

    private void initialiserDimensions() {
        menuBar.prefWidthProperty().bind(panneauPrincipal.widthProperty());
        toolBar.prefWidthProperty().bind(panneauPrincipal.widthProperty());
        vertical.prefWidthProperty().bind(panneauPrincipal.widthProperty());
//        espace.setPrefSize(panneauPrincipal.getPrefWidth() - information.getPrefWidth(), panneauPrincipal.getPrefHeight() - chronometre.getPrefHeight());
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
