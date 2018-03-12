package org.graphysica;

import com.sun.istack.internal.NotNull;
import java.util.Random;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.graphysica.construction.Construction;
import org.graphysica.construction.commande.Commande;
import org.graphysica.construction.commande.CreerPoint;
import org.graphysica.espace2d.Espace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);
    
    @Override
    public void start(@NotNull final Stage stage) throws Exception {
        final Construction construction = new Construction();
        final Espace espace = construction.getEspace();
        final Scene scene = new Scene(espace);
        interaction(scene, espace, construction);
        stage.setScene(scene);
        stage.show();
    }

    private void interaction(final Scene scene, final Espace espace, final Construction construction) {
        espace.getToile().requestFocus();
        scene.setOnKeyPressed((KeyEvent event) -> {
            System.out.println("Touche appuyée");
            switch (event.getCode()) {
                case A:
                    final Commande commande = new CreerPoint(construction, positionAleatoire());
                    construction.executerCommande(commande);
                    break;
                case Z:
                    System.out.println("Annulation");
                    construction.annuler();
                    break;
                case Y:
                    System.out.println("Refaire");
                    construction.refaire();
                    break;
            }
        });
    }
    
    private static Vector2D positionAleatoire() {
        final Random aleatoire = new Random();
        return new Vector2D(aleatoire.nextDouble(), aleatoire.nextDouble());
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
