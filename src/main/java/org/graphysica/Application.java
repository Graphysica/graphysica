package org.graphysica;

import com.sun.istack.internal.NotNull;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.graphysica.construction.Construction;
import org.graphysica.espace2d.Espace;
import org.graphysica.vue.AffichageConstruction;

/**
 * Initialise et lance l'application Graphysica.
 * 
 * @author Marc-Antoine Ouimet
 * @author Victor Babin
 */
public final class Application extends javafx.application.Application {

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
        construction.getEspaces().add(new Espace());
    }

    @Override
    public void init() throws Exception {
        chargerPolices(POLICES);
    }

    @Override
    public void start(@NotNull final Stage stage) throws Exception {
        final Scene scene = new Scene(new AffichageConstruction(construction));
        scene.getStylesheets().add("/styles/styles.css");
        stage.setTitle("Graphysica");
        stage.getIcons().add(new Image(getClass().getResourceAsStream(
                "/images/graphysica-icone.png")));
        stage.setScene(scene);
        stage.show();
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

}
