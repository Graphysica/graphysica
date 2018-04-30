package org.graphysica;

import com.sun.istack.internal.NotNull;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.graphysica.construction.Construction;
import org.graphysica.vue.AffichageConstruction;

public class Application extends javafx.application.Application {

    private final Construction construction = new Construction();

    @Override
    public void start(@NotNull final Stage stage) throws Exception {
        construction.dupliquerEspace();
        construction.dupliquerEspace();
        final Scene scene = new Scene(new AffichageConstruction(construction));
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setTitle("Graphysica");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Lance l'application.
     *
     * @param arguments les arguments de la console.
     */
    public static void main(@NotNull final String[] arguments) {
        launch(arguments);
    }
    
}
