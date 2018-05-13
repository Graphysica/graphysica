package org.graphysica;

import com.sun.istack.internal.NotNull;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.graphysica.construction.Construction;
import org.graphysica.espace2d.Espace;
import org.graphysica.vue.AffichageConstruction;
import org.graphysica.vue.AlerteException;

/**
 * Initialise et lance l'application Graphysica. Qu'une seule application
 * devrait être lancée afin que la référence en singleton soit maintenue.
 *
 * @author Marc-Antoine Ouimet
 * @author Victor Babin
 */
public final class Application extends javafx.application.Application {

    /**
     * L'instance de l'application.
     */
    private static Application instance;

    /**
     * L'ensemble des chemins vers les polices à charger.
     */
    private static final String[] POLICES = {
        "/org/scilab/forge/jlatexmath/fonts/base/jlm_cmmi10.ttf",
        "/org/scilab/forge/jlatexmath/fonts/maths/jlm_cmsy10.ttf",
        "/org/scilab/forge/jlatexmath/fonts/latin/jlm_cmr10.ttf"
    };

    /**
     * La construction de l'application.
     */
    private final Construction construction = new Construction();

    /**
     * Lance l'application.
     *
     * @param arguments les arguments de la console.
     */
    public static void main(@NotNull final String[] arguments) {
        launch(arguments);
    }

    {
        instance = this;
        construction.getEspaces().add(new Espace());
    }

    @Override
    public void init() throws Exception {
        chargerPolices(POLICES);
    }

    @Override
    public void start(@NotNull final Stage stage) throws Exception {
        final Scene scene = new Scene(new AffichageConstruction(construction));
        scene.getStylesheets().add(getClass().getResource(
                "/styles/styles.css").toExternalForm());
        stage.setTitle("Graphysica");
        stage.getIcons().add(new Image(getClass().getResourceAsStream(
                "/images/graphysica-icone.png")));
        stage.setScene(scene);
        stage.show();
        Thread.currentThread().setUncaughtExceptionHandler((thread, throwable)
                -> {
            final Alert alerteException = new AlerteException(throwable);
            alerteException.setTitle("Exceptions inattendues");
            alerteException.setContentText(
                    "Des exceptions non gérées sont survenues.");
            alerteException.showAndWait();
        });
    }

    /**
     * Charge un ensemble de polices.
     *
     * @param polices l'ensemble des chemins vers les polices à charger.
     */
    private static void chargerPolices(@NotNull final String... polices) {
        final Class classe = Application.class;
        for (final String police : polices) {
            javafx.scene.text.Font.loadFont(
                    classe.getResourceAsStream(police), 12);
        }
    }

    public static Application getInstance() {
        return instance;
    }

}
